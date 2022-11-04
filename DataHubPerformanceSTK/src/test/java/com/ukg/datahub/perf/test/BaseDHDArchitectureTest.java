package com.ukg.datahub.perf.test;

import com.Utilities.PostgresDBHelper;
import com.google.gson.Gson;
import com.ukg.datahub.perf.beans.PipelineData;
import com.ukg.datahub.perf.beans.PipelineStatus;
import com.ukg.datahub.perf.datainsertion.QueryTemplate;
import com.ukg.datahub.perf.helpers.SystemValueHandler;
import com.ukg.datahub.perf.metrics.Metrics;
import com.ukg.datahub.perf.utilities.CommonUtils;
import com.ukg.datahub.perf.utilities.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.TimeZone;


public abstract class BaseDHDArchitectureTest<T extends DHDArchitectureTestParams> extends BaseTest {

    protected PostgresDBHelper postgresDBHelper;
    protected boolean IS_TEST_PASSED = true;
    protected Gson gson ;
    protected Map<String, Object> testOutputs;
    protected List<PipelineData> pipelineDataList ;
    protected final long JOB_STATUS_CHECK_SLEEPTIME = 300000;
    protected final double GET_JOB_STATUS_CHECKTIMEOUT = 2.16e+7;
    protected String testId = null;
    protected String projectId = SystemValueHandler.fetchExecutionProperty("targetGcpDataProject");
    protected String updatedtm;
    protected String tenantName;
    protected List<String> maxConcurrentApiCallsList;
    protected String pipeLine;
    public static final List<String> pipelineFailureStates = new ArrayList<>();


    @BeforeTest
    public void beforeTest() {
        System.out.println(SystemValueHandler.fetchExecutionProperty("testResultBucket"));
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS",System.getProperty("user.dir")+"\\"+SystemValueHandler.fetchExecutionProperty("GOOGLE_APPLICATION_CREDENTIALS"));
        postgresDBHelper = new PostgresDBHelper(projectId);
        gson = new Gson();
        testOutputs = new LinkedHashMap<>();
        pipelineDataList = new ArrayList<>();
    }


    /**
     * This method will update the given column value in the given table
     *
     * @param tableName
     * @param cloumnName
     * @param columnValue
     * @param tier
     */
    protected void updatePipelinePerformanceTier(String tableName, String cloumnName, String columnValue, String tier) {
        try {
            String updateQuery = FileUtils.loadFile(QueryTemplate.UPDATE_TABLE.getScriptFilePath())
                    .replacing("tableName").by(tableName)
                    .replacing("columnName").by(cloumnName)
                    .replacing("columnValue").by(columnValue)
                    .replacing("whereColumn").by("TIER")
                    .replacing("whereColumnValue").by(tier)
                    .resolve();
            String existingValue = getExistingValueFromPerformanceTier(tableName, cloumnName, tier);
            if (StringUtils.isNotEmpty(existingValue)) {
                if (!columnValue.equalsIgnoreCase(existingValue)) {
                    logger.info("Query to update the column values :" + updateQuery);
                    postgresDBHelper.updateQueryPostgresTable(updateQuery);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update the pipeline performance tier to " + tier, e);
        }
    }

    protected String getExistingValueFromPerformanceTier(String tableName, String cloumnName, String tier) {
        try {
            String condition = "\"TIER\"  = '" + tier + "'";
            return postgresDBHelper.queryOnCondition(tableName, cloumnName, condition);
        } catch (Exception e) {
            logger.error("Failed to fetch the value of " + cloumnName + "from " + tableName, e);
        }
        return null;
    }

    /**
     * This method will generate the updatedTM from current timestamp
     *
     * @return Updatedtm
     */
    protected String getUpdatedtm() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
    }

    /**
     * This method is used to capture the start and time of the pipeline
     *
     * @param pipelineData
     */
    protected void setStartAndEndTimeForPipelineExecution(PipelineData pipelineData) {

        try {
            logger.info("Fetching pipeline start and end time..!!");
            String getStartdtmAndEndDtm = "SELECT startdtm,enddtm from public.tenantmgr_jobstatus where updatedtm = '" + updatedtm + "+00'";
            ResultSet getStartdtmAndEndDtmResultSet = postgresDBHelper.getPostgresTableResultSet(getStartdtmAndEndDtm);
            Date startdtm = null;
            Date enddtm = null;

            while (getStartdtmAndEndDtmResultSet.next()) {
                startdtm = getStartdtmAndEndDtmResultSet.getTimestamp("startdtm");
                enddtm = getStartdtmAndEndDtmResultSet.getTimestamp("enddtm");
            }

            SimpleDateFormat sdf = CommonUtils.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String startTime = (startdtm == null) ? "0" : sdf.format(startdtm);
            String endTime = (enddtm == null) ? "0" : sdf.format(enddtm);
            pipelineData.setPipelineStartdtm(startTime);
            pipelineData.setPipelineEnddtm(endTime);
        } catch (SQLException se) {
            logger.error("Failed to fetch the start and end time for pipeline execution", se);
        }
    }

    /**
     * This method is used to extract the pipeline execution time
     *
     * @return
     */
    protected int getPipelineExecutionTime() {
        int timeTaken = 0;
        try {
            logger.info("Fetching pipeline execution time..!!");
            String query = "SELECT cast((extract (epoch from (cast(enddtm as timestamp)- cast(startdtm as timestamp)))) AS integer ) from public.tenantmgr_jobstatus where updatedtm = '" + updatedtm + "+00'";
            ResultSet resultSet = postgresDBHelper.getPostgresTableResultSet(query);

            while (resultSet.next()) {
                timeTaken = resultSet.getInt(1);
            }
        } catch (SQLException se) {
            logger.error("Failed to fetch the pipeline execution time", se);
        }
        return timeTaken;
    }

    /**
     * This method will create the history tables in the admin environment
     */
    @BeforeSuite
    public void createHistoryTables() {

        pipelineFailureStates.add(PipelineStatus.FAILED.getValue());
        pipelineFailureStates.add(PipelineStatus.FAILED_TO_START.getValue());
        pipelineFailureStates.add(PipelineStatus.TERMINATED.getValue());
        pipelineFailureStates.add(PipelineStatus.NOT_RUNNING.getValue());
        pipelineFailureStates.add(PipelineStatus.KILL.getValue());


        //TODO change this project id to admin project id
        String adminProjectId = SystemValueHandler.fetchExecutionProperty("targetGcpDataProject");
        String dataSetName = "Metrics";

        Metrics metrics = new Metrics(adminProjectId);
        metrics.createDataset(dataSetName);
        metrics.createBQHistoryTable(dataSetName);
        metrics.createMetricHistoryTable(dataSetName);
        metrics.createRunHistoryTable(dataSetName);
        metrics.createCFHistoryTable(dataSetName);
    }

    protected abstract void generatePerformanceReport(T dhdArchitectureTestParams, Map<String, Object> testOutputs);
    protected abstract void preparePipelineMetrics();

}
