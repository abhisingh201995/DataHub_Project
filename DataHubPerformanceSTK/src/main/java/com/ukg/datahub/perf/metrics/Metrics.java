package com.ukg.datahub.perf.metrics;

import com.Utilities.GcpHelper;
import com.ukg.datahub.perf.constants.Constants;
import com.ukg.datahub.perf.datainsertion.QueryTemplate;
import com.ukg.datahub.perf.utilities.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;

public class Metrics {

    private Logger logger = LogManager.getLogger(this.getClass());
    private GcpHelper gcpHelper;
    private String projectId;

    public Metrics(String projectId) {
        this.projectId = projectId;
        gcpHelper = new GcpHelper(projectId);
    }

    public void createCFHistoryTable(String datasetName) {

        if (StringUtils.isEmpty(datasetName)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }
        try {
            String script = FileUtils.loadFile(QueryTemplate.CREATE_CFHISTORY_TABLE.getScriptFilePath())
                    .replacing("projectId").by(projectId).replacing("datasetName").by(datasetName).resolve();
            gcpHelper.queryBatch(script);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the CreateCFHistory.sql file", e);
        }
    }

    public void createBQHistoryTable(String datasetName) {
        if (StringUtils.isEmpty(datasetName)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            String script = FileUtils.loadFile(QueryTemplate.CREATE_BQHISTORY_TABLE.getScriptFilePath())
                    .replacing("projectId").by(projectId).replacing("datasetName").by(datasetName).resolve();
            gcpHelper.queryBatch(script);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the CreateBQHistory.sql file", e);
        }
    }

    public void createMetricHistoryTable(String datasetName) {
        if (StringUtils.isEmpty(datasetName)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }
        try {
            String script = FileUtils.loadFile(QueryTemplate.CREATE_METRICSHISTORY_TABLE.getScriptFilePath())
                    .replacing("projectId").by(projectId).replacing("datasetName").by(datasetName).resolve();
            gcpHelper.queryBatch(script);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the CreateMetricsHistory.sql file", e);
        }
    }

    public void createRunHistoryTable(String datasetName) {
        if (StringUtils.isEmpty(datasetName)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            String createRunHistoryTable = FileUtils.loadFile(QueryTemplate.CREATE_RUNHISTORY_TABLE.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .resolve();

            gcpHelper.queryBatch(createRunHistoryTable);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the CreateRunHistory.sql file", e);
        }
    }

    /**
     * @param datasetName
     * @param stageTableNameSuffix
     */
    public void dropStageTables(String datasetName, String stageTableNameSuffix) {
        if (StringUtils.isEmpty(datasetName) || StringUtils.isEmpty(stageTableNameSuffix)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }
        try {
            String script = FileUtils.loadFile(QueryTemplate.DROP_STAGE_TABLES.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("stageTableNameSuffix").by(stageTableNameSuffix)
                    .resolve();
            gcpHelper.queryBatch(script);
        } catch (Exception e) {
            logger.error("Failed to drop the stage table", e);
        }
    }

    /**
     * @param projectId
     * @param datasetName
     * @param runParams
     */
    public void insertIntoRunHistoryTable(String projectId, String datasetName, Map<String, String> runParams) {
        try {
            String insertIntoRunHistoryTable = FileUtils.loadFile(QueryTemplate.INSERT_INTO_RUNHISTORY_TABLE.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("testId").by(runParams.get("testId"))
                    .replacing("runMode").by(runParams.get("runMode"))
                    .replacing("pipeline").by(runParams.get("pipeline"))
                    .replacing("tenantName").by(runParams.get("tenantName"))
                    .replacing("overrideEndDate").by(runParams.get("overrideEndDate"))
                    .replacing("overrideStartDate").by(runParams.get("overrideStartDate"))
                    .replacing("wrapper").by(Constants.ElementNames.WRAPPER)
                    .replacing("updateDtm").by(runParams.get("updatedtm"))
                    .replacing("performanceTier").by(runParams.get("performanceTier"))
                    .replacing("pipelineState").by(runParams.get("pipelineStatus"))
                    .replacing("dayCount").by(runParams.get("dayCount"))
                    .replacing("maxConcurrentApiCall").by(runParams.get("maxConcurrentApiCall"))
                    .replacing("personCount").by(runParams.get("personCount"))
                    .replacing("pipelineEndDate").by(runParams.get("pipelineEndDate"))
                    .replacing("pipelineStartDate").by(runParams.get("pipelineStartDate"))
                    .resolve();

            gcpHelper.queryBatch(insertIntoRunHistoryTable);
        } catch (Exception e) {
            logger.error("Failed to insert into run history table ", e);
        }
    }

    /**
     * @param datasetName
     * @param projectId2
     * @param datasetName2
     * @param stageTableNameSuffix
     */
    public void moveCFDataToHistoryTable(String datasetName, String projectId2, String datasetName2,
                                         String stageTableNameSuffix) {

        if (StringUtils.isEmpty(datasetName) || StringUtils.isEmpty(projectId2)
                || StringUtils.isEmpty(datasetName2) || StringUtils.isEmpty(stageTableNameSuffix)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            String script = FileUtils.loadFile(QueryTemplate.MOVE_TO_CFHISTORY.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("projectId2").by(projectId2)
                    .replacing("datasetName2").by(datasetName2)
                    .replacing("stageTableNameSuffix").by(stageTableNameSuffix)
                    .resolve();
            gcpHelper.queryBatch(script);
            logger.info("Data from staging table cf_stage_" + stageTableNameSuffix + " is moved to CFHistory table in " + projectId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the MoveToCFHistory.sql file", e);
        }
    }

    public void moveBQDataToHistoryTable(String datasetName, String projectId2, String datasetName2,
                                         String stageTableNameSuffix) {

        if (StringUtils.isEmpty(datasetName) || StringUtils.isEmpty(projectId2)
                || StringUtils.isEmpty(datasetName2) || StringUtils.isEmpty(stageTableNameSuffix)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            String script = FileUtils.loadFile(QueryTemplate.MOVE_TO_BQHISTORY.getScriptFilePath()).replacing("projectId")
                    .by(projectId).replacing("datasetName").by(datasetName).replacing("projectId2").by(projectId2)
                    .replacing("datasetName2").by(datasetName2).replacing("stageTableNameSuffix").by(stageTableNameSuffix).resolve();
            gcpHelper.queryBatch(script);
            logger.info("Data from staging table bq_stage_" + stageTableNameSuffix + " is moved to BQHistory table in " + projectId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the MoveToBQHistory.sql file", e);
        }
    }

    public void moveMetricDataToHistoryTable(String datasetName, String projectId2, String datasetName2,
                                             String stageTableNameSuffix) {

        if (StringUtils.isEmpty(datasetName) || StringUtils.isEmpty(projectId2)
                || StringUtils.isEmpty(datasetName2) || StringUtils.isEmpty(stageTableNameSuffix)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            String script = FileUtils.loadFile(QueryTemplate.MOVE_TO_METRICHISTORY.getScriptFilePath()).replacing("projectId")
                    .by(projectId).replacing("datasetName").by(datasetName).replacing("projectId2").by(projectId2)
                    .replacing("datasetName2").by(datasetName2).replacing("stageTableNameSuffix").by(stageTableNameSuffix).resolve();
            gcpHelper.queryBatch(script);
            logger.info("Data from staging table metric_stage_" + stageTableNameSuffix + " is moved to MetricsHistory table in " + projectId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read the MoveToMetricsHistory.sql file", e);
        }
    }

    public void generateCFMetricsStage(String datasetName, String updatedtm, String stageTableNameSuffix) {

        if (StringUtils.isEmpty(datasetName) ||
                StringUtils.isEmpty(updatedtm) || StringUtils.isEmpty(stageTableNameSuffix)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            logger.info("Generating CF metrics stage table");
            String script = FileUtils.loadFile(QueryTemplate.GEN_CFMETRICS_STAGE.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("updatedtm").by(updatedtm)
                    .replacing("stageTableNameSuffix").by(stageTableNameSuffix)
                    .resolve();
            gcpHelper.queryBatch(script);
            logger.info("cf_stage_" + stageTableNameSuffix + " staging table is generated with the data in " + projectId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read the GenCFMetricStage.sql file", e);
        }
    }

    public void generateBQMetricsStage(String datasetName, String updatedtm, String stageTableNameSuffix, String regionId) {

        if (StringUtils.isEmpty(datasetName) || StringUtils.isEmpty(updatedtm) ||
                StringUtils.isEmpty(stageTableNameSuffix) || StringUtils.isEmpty(regionId)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }
        try {
            logger.info("Generating BQ metrics stage table");
            String script = FileUtils.loadFile(QueryTemplate.GEN_BQMETRICS_STAGE.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("updatedtm").by(updatedtm)
                    .replacing("stageTableNameSuffix").by(stageTableNameSuffix)
                    .replacing("regionId").by(regionId)
                    .resolve();
            gcpHelper.queryBatch(script);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read the GenBQMetricStage table", e);
        }
    }


    /**
     * @param datasetName
     * @param updatedtm
     * @param tenant
     * @param wrapper
     * @param pipeline
     * @param metric_name
     * @param start_ts
     * @param end_ts
     */
    public void createAndInsertIntoMetricsStage(@NotNull String datasetName, @NotNull String stageTableNameSuffix, String updatedtm, String tenant,
                                                String wrapper, String pipeline, String metric_name, String start_ts, String end_ts, String json_result) {

        if (StringUtils.isEmpty(updatedtm) || StringUtils.isEmpty(tenant) || StringUtils.isEmpty(wrapper)
                || StringUtils.isEmpty(pipeline) || StringUtils.isEmpty(metric_name) || StringUtils.isEmpty(start_ts)
                || StringUtils.isEmpty(end_ts) || StringUtils.isEmpty(json_result)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }
        try {

            logger.info("Generating Metrics stage table");
            String createMetricsStage = FileUtils.loadFile(QueryTemplate.CREATE_METRIC_STAGE.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("stageTableNameSuffix").by(stageTableNameSuffix)
                    .resolve();

            gcpHelper.queryBatch(createMetricsStage);

            logger.info("Inserting values to Metrics stage table");
            String insertIntoMetricsStage = FileUtils.loadFile(QueryTemplate.INSERT_INTO_METRIC_STAGE.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("datasetName").by(datasetName)
                    .replacing("updatedtm").by(updatedtm)
                    .replacing("tenant").by(tenant)
                    .replacing("stageTableNameSuffix").by(stageTableNameSuffix)
                    .replacing("wrapper").by(wrapper)
                    .replacing("pipeline").by(pipeline)
                    .replacing("metric_name").by(metric_name)
                    .replacing("start_ts").by(start_ts)
                    .replacing("end_ts").by(end_ts)
                    .replacing("json_result").by(json_result)
                    .resolve();

            gcpHelper.queryBatch(insertIntoMetricsStage);
            logger.info("metric_stage_" + stageTableNameSuffix + " staging table is generated with the data in " + projectId);

        } catch (Exception e) {
            logger.error("Failed to create or insert into Metrics stage table", e);
        }
    }

    /**
     * This method will create the Metrics measure table
     *
     * @param datasetName
     */
    public void generateMetricsMeasure(String datasetName) {

        if (StringUtils.isEmpty(datasetName)) {
            throw new IllegalArgumentException("Please provide valid arguments to execute the query");
        }

        try {
            String generateMetricsMeasure = FileUtils.loadFile(QueryTemplate.GENERATE_METRICS_MEASURES.getScriptFilePath())
                    .replacing("datasetName").by(datasetName)
                    .resolve();
            gcpHelper.queryBatch(generateMetricsMeasure);
        } catch (Exception e) {
            logger.error("Failed to generate the Metrics measure table", e);
        }
    }

    public void createDataset(String dataSetName) {
        gcpHelper.createDataset(dataSetName);
    }

}
