package com.ukg.datahub.perf.test;

import com.Utilities.GcpHelper;
import com.Utilities.PostgresDBHelper;
import com.ukg.datahub.perf.datainsertion.DataInsertionServiceImpl;
import com.ukg.datahub.perf.datainsertion.InsertionDetails;
import com.ukg.datahub.perf.helpers.SystemValueHandler;
import com.ukg.datahub.perf.reporting.PerformanceReport;
import com.ukg.datahub.perf.utilities.CommonUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.ukg.datahub.perf.reporting.TestProperties.*;
import static com.ukg.datahub.perf.test.BaseDHDArchitectureTest.pipelineFailureStates;

/**
 * author : Prashant Agarwal
 * This is the test class to test the performance of the summary pipeline with bulk data in the pipeline object
 */

public class MillionInsertPerformanceTest extends BaseTest<MillionInsertPerformanceTestParameters> {

    private PostgresDBHelper postgresDBHelper;
    private GcpHelper gcpHelper;
    private DataInsertionServiceImpl dataInsertionImpl;
    private String projectId = SystemValueHandler.fetchExecutionProperty("targetGcpDataProject");
    private final String TOPIC_ID = "triggerStartPipeline";
    private final long jobStatusCheckSleepTime = 60000;
    private final long getJobStatusCheckTimeOut = 216000;
    private Map<String, Object> testOutputs;
    private String updatedtm;
    boolean isTestPassed = true;

    /**
     * Test method to determine the performance of the pipelines with bulk data in pipeline objects
     */
    @Test(description = "This test will test the performance of the summary pipeline after million data insert to the pipeline objects", dataProvider = "testPayloadDataProvider")
    public void testMillionInsertPerformance(MillionInsertPerformanceTestParameters millionInsertPerformanceTestParameters) {

        try {
            logger.info("******************** STARING TEST **********************");
            String tenantName = millionInsertPerformanceTestParameters.getTenantName();
            String runType = millionInsertPerformanceTestParameters.getRunType();
            String pipeLine = millionInsertPerformanceTestParameters.getPipeline();
            int peopleRowCount = millionInsertPerformanceTestParameters.getPeopleRowCount();

            //Set insertion details
            InsertionDetails insertionDetails = new InsertionDetails();
            insertionDetails.setProjectId(projectId);
            insertionDetails.setTenantName(tenantName);
            insertionDetails.setpeoplesTableRowCount(peopleRowCount);

            //Call insert data to populate data in people and timecard tables
            dataInsertionImpl = new DataInsertionServiceImpl();
            //Insert actual data
            dataInsertionImpl.insertData(insertionDetails);
            postgresDBHelper = new PostgresDBHelper(projectId);
            gcpHelper = new GcpHelper(projectId);

            //Prepare JsonObject to execute given pipeline
            JSONObject message = new JSONObject();
            message.put("tenant", tenantName);
            message.put("updateDtm", updatedtm);
            message.put("runtype", runType);
            message.put("pipeline", pipeLine);

            logger.info("Publishing message to " + pipeLine + " pipeline...!!");
            //Execute the pipeline
            gcpHelper.publishToPubSub(projectId
                    , TOPIC_ID, message);

            //wait for the pipeline status to change to success
            postgresDBHelper.waitForAColumnValueToChange("public.tenantmgr_jobstatus", "status", "updatedtm = '" + updatedtm + "+00'", "Success", getJobStatusCheckTimeOut, jobStatusCheckSleepTime,
                    pipelineFailureStates);

            logger.info("Checking tike taken by " + pipeLine + " to get executed...!!");
            //Get the time taken by the given job to get finished
            captureTimeTakenByJobToFinish(millionInsertPerformanceTestParameters);

        } catch (Exception e) {
            isTestPassed = false;
            Assert.fail("Test case failed ", e);
        } finally {
            logger.info("Generating final report ...!!");
            generatePerformanceReport(millionInsertPerformanceTestParameters, testOutputs);
        }
    }

    private void captureTimeTakenByJobToFinish(MillionInsertPerformanceTestParameters millionInsertPerformanceTestParameters) throws SQLException {

        String query = "SELECT cast((extract (epoch from (cast(enddtm as timestamp)- cast(startdtm as timestamp)))) AS integer ) from public.tenantmgr_jobstatus where updatedtm = '" + updatedtm + "+00'";
        ResultSet resultSet = postgresDBHelper.getPostgresTableResultSet(query);
        int timeTaken = 0;
        while (resultSet.next()) {
            timeTaken = resultSet.getInt(1);
        }

        logger.info("Final time taken by the job " + CommonUtils.formatSeconds(timeTaken));

        testOutputs = new LinkedHashMap<>();
        testOutputs.putIfAbsent("Time Taken By Summary Pipeline to get executed", CommonUtils.formatSeconds(timeTaken));
    }

    /**
     * This method will generate the performance report
     */
    private void generatePerformanceReport(MillionInsertPerformanceTestParameters millionInsertPerformanceTestParameters, Map<String, Object> testOutputs) {

        PerformanceReport performanceReport = new PerformanceReport()
                .addProperty(PR_PROP_PRODUCT_LOGIN_URL, SystemValueHandler.fetchExecutionProperty("PRODUCT_LOGIN"))
                .addProperty(PR_PROP_TEST_DESC, millionInsertPerformanceTestParameters.getTestDescription())
                .addProperty(PR_PROP_TEST_ID, millionInsertPerformanceTestParameters.getTestId())
                .addProperty(PR_PROP_TENANT_NAME, millionInsertPerformanceTestParameters.getTenantName())
                .addProperty(PR_PROP_DB_NAME, SystemValueHandler.fetchExecutionProperty("DB_NAME"))
                .addProperty(PR_PROP_DB_USER, SystemValueHandler.fetchExecutionProperty("DB_USER"))
                .addProperty(PR_PROP_GCP_DATA_PROJECT, SystemValueHandler.fetchExecutionProperty("targetGcpDataProject"))
                .setPassed(isTestPassed)
                .setOwners(SystemValueHandler.fetchExecutionProperty("TEST_OWNER"))
                .addOutputData(testOutputs);

        // Setting testNG test output in test report
        setTestOutput(millionInsertPerformanceTestParameters, performanceReport, true);
        performanceReport.publish();
        Reporter.log(performanceReport.getPerformanceReportHtml());
    }

    @BeforeTest
    public void init() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        updatedtm = sdf.format(new Date());
    }
}
