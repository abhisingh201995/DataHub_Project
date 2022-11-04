package com.ukg.datahub.perf.test;

import com.Utilities.GcpHelper;
import com.ukg.datahub.perf.beans.PipelineData;
import com.ukg.datahub.perf.helpers.SystemValueHandler;
import com.ukg.datahub.perf.metrics.Metrics;
import com.ukg.datahub.perf.metrics.MetricsHttp;
import com.ukg.datahub.perf.reporting.PerformanceReport;
import com.ukg.datahub.perf.utilities.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ukg.datahub.perf.reporting.TestProperties.*;

/**
 * This is the test class to measure DHD infrastructure scalability
 */
public class DHDArchitectureWithHistoricDRTest extends BaseDHDArchitectureTest<DHDArchitectureTestParams> {

    private String dataSetName = "Metrics";
    private Metrics metrics;
    private GcpHelper gcpHelper;

    @Test(description = "This test is executed to measure the scalability of DHD", dataProvider = "testPayloadDataProvider")
    public void testDHDArchWithHistoricalDataRun(DHDArchitectureTestParams dhdArchitectureTestWithHistoricalDRParameters) throws Exception {

        logger.info("**********Starting DHD Infra Scalability Test**********");
        metrics = new Metrics(projectId);
        gcpHelper = new GcpHelper(projectId);

        String TOPIC_ID = "triggerStartPipeline";
        maxConcurrentApiCallsList = CommonUtils.convertDelimitedStringToList(dhdArchitectureTestWithHistoricalDRParameters.getMaxConcurrentApiCalls(), ",");
        pipeLine = dhdArchitectureTestWithHistoricalDRParameters.getPipeline();
        String tenantName = dhdArchitectureTestWithHistoricalDRParameters.getTenantName();
        String runType = dhdArchitectureTestWithHistoricalDRParameters.getRunType();

        try {

            if (StringUtils.isEmpty(tenantName)) {
                throw new RuntimeException("No tenant exist for performance tier" + dhdArchitectureTestWithHistoricalDRParameters.getPerformanceTier());
            }

            int counter = 0;
            for (String maxConcurrentApiCall : maxConcurrentApiCallsList) {
                updatePipelinePerformanceTier("public.tenantmgr_pipeline_performance", "max_concurrent_api_calls", maxConcurrentApiCall, "7000-14000 - OnDemand");
                //Prepare JsonObject to execute given pipeline
                updatedtm = getUpdatedtm();
                String stageTableNameSuffix = CommonUtils.removeSpecialChars(updatedtm);
                JSONObject message = new JSONObject();
                message.put("tenant", tenantName);
                message.put("updateDtm", updatedtm);
                message.put("runtype", runType);
                message.put("pipeline", pipeLine);

                logger.info("Publishing message to " + pipeLine + " pipeline...!!");
                //Execute the pipeline
                gcpHelper.publishToPubSub(projectId
                        , TOPIC_ID, message);

                logger.info("Waiting for the " + pipeLine + " pipeline execution to get finished ..!!");

                //wait for the pipeline status to change to success
                postgresDBHelper.waitForAColumnValueToChange("public.tenantmgr_jobstatus", "status", "updatedtm = '" + updatedtm + "+00'", "Success", GET_JOB_STATUS_CHECKTIMEOUT, JOB_STATUS_CHECK_SLEEPTIME,
                        pipelineFailureStates);
                logger.info("Checking time taken by " + pipeLine + " to get executed...!!");
                //Get the time taken by the given job to get finished
                counter++;
                capturePipelineMetrics(stageTableNameSuffix,maxConcurrentApiCall, counter, dhdArchitectureTestWithHistoricalDRParameters);
            }

        } catch (Exception e) {
            IS_TEST_PASSED = false;
        }



        logger.info("Generating final report ...!!");
        generatePerformanceReport(dhdArchitectureTestWithHistoricalDRParameters, testOutputs);
    }

    /**
     * This method will prepare the lists for test output
     */
    @Override
    protected void preparePipelineMetrics() {

        List<String> pipelineExecutionTimeList = pipelineDataList.stream().map(p -> p.getPipelineExecutionTime()).collect(Collectors.toList());
        List<String> pipelineMemoryList = pipelineDataList.stream().map(p -> p.getPipelineMemory()).collect(Collectors.toList());
        List<String> listOfNumberOfChunks = pipelineDataList.stream().map(p -> p.getNumberOfChunks()).collect(Collectors.toList());
        List<String> getManifestExecutionTime = pipelineDataList.stream().map(p -> p.getGetManifestExecutionTime()).collect(Collectors.toList());
        List<String> getGetBusinessStructureTimeAndMemory = pipelineDataList.stream().map(p -> p.getGetBusinessStructureTimeAndMemory()).collect(Collectors.toList());
        List<String> getBqControllerExecutionTime = pipelineDataList.stream().map(p -> p.getBqControllerExecutionTime()).collect(Collectors.toList());
        List<String> getGetForecastWeeksTimeAndMemory = pipelineDataList.stream().map(p -> p.getGetForecastWeeksTimeAndMemory()).collect(Collectors.toList());
        List<String> getMaxConcurrentAPICall = pipelineDataList.stream().map(p -> p.getMaxConcurrentAPICall()).collect(Collectors.toList());
        List<String> getApiControllerExecutionTime = pipelineDataList.stream().map(p -> p.getApiControllerExecutionTime()).collect(Collectors.toList());
        List<String> getPurgePipelineData = pipelineDataList.stream().map(p -> p.getPurgePipelineData()).collect(Collectors.toList());
        List<String> getVariation = pipelineDataList.stream().map(p -> p.getVariation()).collect(Collectors.toList());
        List<String> getTotalResponseSize = pipelineDataList.stream().map(p -> p.getTotalResponseSize()).collect(Collectors.toList());
        List<String> getApiControllerMemory = pipelineDataList.stream().map(p -> p.getApiControllerMemory()).collect(Collectors.toList());
        List<String> getUpdatedtm = pipelineDataList.stream().map(p -> p.getUpdatedtm()).collect(Collectors.toList());
        List<String> getStartdtm = pipelineDataList.stream().map(p -> p.getPipelineStartdtm()).collect(Collectors.toList());
        List<String> getenddtm = pipelineDataList.stream().map(p -> p.getPipelineEnddtm()).collect(Collectors.toList());

        testOutputs.put("Variation", getVariation);
        testOutputs.put("Max concurrent API calls", getMaxConcurrentAPICall);
        testOutputs.put("Start Time", getStartdtm);
        testOutputs.put("End Time", getenddtm);
        testOutputs.put("Total Execution Time", pipelineExecutionTimeList);
        testOutputs.put("UpdatedTM", getUpdatedtm);
        testOutputs.put("Pipeline Memory", pipelineMemoryList);
        testOutputs.put("List Of Number Of Chunks", listOfNumberOfChunks);
        testOutputs.put("Manifest Execution Time", getManifestExecutionTime);
        testOutputs.put("Business Structure Time And Memory", getGetBusinessStructureTimeAndMemory);
        testOutputs.put("Bq Controller Execution Time", getBqControllerExecutionTime);
        testOutputs.put("Forecast Weeks Time And Memory", getGetForecastWeeksTimeAndMemory);
        testOutputs.put("Purge Pipeline Data", getPurgePipelineData);
        testOutputs.put("Total Response Size", getTotalResponseSize);
        testOutputs.put("Api Controller Memory", getApiControllerMemory);
        testOutputs.put("Api Controller Execution Time", getApiControllerExecutionTime);

        for (String updatedtm : getUpdatedtm){
            String stageTableName = CommonUtils.removeSpecialChars(updatedtm);
            //Drop all stage table post fetching the metrics
            metrics.dropStageTables("datasetName" , stageTableName);
        }

    }


    /**
     * This method will capture the pipeline performance
     *
     *
     * @param stageTableNameSuffix
     * @param maxConcurrentApiCall
     * @param variation
     * @throws SQLException
     */
    protected void capturePipelineMetrics(String stageTableNameSuffix, String maxConcurrentApiCall, int variation, DHDArchitectureTestParams dhdArchitectureTestWithHistoricalDRParameters) {

        try {
            PipelineData pipelineData = new PipelineData();
            String[] CF_USER_MEMORY_BYTES = {"cloud_function",
                    "cloudfunctions.googleapis.com/function/user_memory_bytes"};

            setStartAndEndTimeForPipelineExecution(pipelineData);
            int timeTaken = getPipelineExecutionTime();

            //TODO Replace the dataset name
            metrics.createDataset(dataSetName);

            //TODO replace "region-us" with run time value
            metrics.generateBQMetricsStage(dataSetName, updatedtm, stageTableNameSuffix, "region-us");
            metrics.generateCFMetricsStage(dataSetName, updatedtm, stageTableNameSuffix);

            String metricsJson = MetricsHttp.doRequestPost(projectId, CF_USER_MEMORY_BYTES, pipelineData.getPipelineStartdtm(),//start time
                    pipelineData.getPipelineEnddtm());

            //TODO what metricsName and wrapper needs to be passed MetricName
            metrics.createAndInsertIntoMetricsStage(dataSetName, stageTableNameSuffix, updatedtm, tenantName,
                    "_", dhdArchitectureTestWithHistoricalDRParameters.getPipeline(), CF_USER_MEMORY_BYTES[1],
                    pipelineData.getPipelineStartdtm(), pipelineData.getPipelineEnddtm(), metricsJson);

            logger.info("Final time taken by the job " + CommonUtils.formatSeconds(timeTaken));
            pipelineData.setPipelineExecutionTime(CommonUtils.formatSeconds(timeTaken));
            pipelineData.setUpdatedtm(updatedtm);
            pipelineData.setPipelineMemory("Yet to capture");
            pipelineData.setNumberOfChunks("Yet to capture");
            pipelineData.setApiControllerExecutionTime("Yet to capture");
            pipelineData.setApiControllerMemory("Yet to capture");
            pipelineData.setGetBusinessStructureTimeAndMemory("Yet to capture");
            pipelineData.setBqControllerExecutionTime("Yet to capture");
            pipelineData.setGetForecastWeeksTimeAndMemory("Yet to capture");
            pipelineData.setGetManifestExecutionTime("Yet to capture");
            pipelineData.setGetBusinessStructureTimeAndMemory("Yet to capture");
            pipelineData.setGetManifestMemory("Yet to capture");
            pipelineData.setPurgePipelineData("Yet to capture");
            pipelineData.setTotalResponseSize("Yet to capture");
            pipelineData.setMaxConcurrentAPICall(maxConcurrentApiCall);
            pipelineData.setVariation("#" + String.valueOf(variation));
            pipelineDataList.add(pipelineData);

            //TODO this project id needs to be replaced with admin project's id
            metrics.moveBQDataToHistoryTable( dataSetName, projectId, dataSetName, stageTableNameSuffix);
            metrics.moveCFDataToHistoryTable(dataSetName, projectId, dataSetName, stageTableNameSuffix);
            metrics.moveMetricDataToHistoryTable(dataSetName, projectId, dataSetName, stageTableNameSuffix);
            metrics.generateMetricsMeasure(dataSetName);
        } catch (Exception e) {
            logger.error("Failed to capture the metrics", e);
        }
    }

    /**
     * This method will generate the performance report
     */
    @Override
    protected void generatePerformanceReport(DHDArchitectureTestParams dhdArchitectureTestWithHistoricalDRParameters, Map<String, Object> testOutputs) {

        PerformanceReport performanceReport = new PerformanceReport()
                .addProperty(PR_PROP_PRODUCT_LOGIN_URL, SystemValueHandler.fetchExecutionProperty("PRODUCT_LOGIN"))
                .addProperty(PR_PROP_TEST_DESC, dhdArchitectureTestWithHistoricalDRParameters.getTestDescription())
                .addProperty(PR_PROP_TEST_ID, dhdArchitectureTestWithHistoricalDRParameters.getTestId())
                .addProperty(PR_PROP_TENANT_NAME, SystemValueHandler.fetchExecutionProperty(dhdArchitectureTestWithHistoricalDRParameters.getPerformanceTier()))
                .addProperty(PR_PROP_DB_NAME, SystemValueHandler.fetchExecutionProperty("DB_NAME"))
                .addProperty(PR_PROP_DB_USER, SystemValueHandler.fetchExecutionProperty("DB_USER"))
                .addProperty(PR_PROP_GCP_DATA_PROJECT, SystemValueHandler.fetchExecutionProperty("targetGcpDataProject"))
                .setPassed(IS_TEST_PASSED)
                .setOwners(SystemValueHandler.fetchExecutionProperty("TEST_OWNER"))
                .addParameter("Max Concurrent Api Calls", dhdArchitectureTestWithHistoricalDRParameters.getMaxConcurrentApiCalls())
                .addParameter("Pipeline", dhdArchitectureTestWithHistoricalDRParameters.getPipeline())
                .addParameter("Mode", dhdArchitectureTestWithHistoricalDRParameters.getRunType())
                .addParameter("Performance Tier", dhdArchitectureTestWithHistoricalDRParameters.getPerformanceTier())
                .addParameter("Tenant Name",SystemValueHandler.fetchExecutionProperty(dhdArchitectureTestWithHistoricalDRParameters.getPerformanceTier()))
                .addOutputData(testOutputs);

        // Setting testNG test output in test report
        setTestOutput(dhdArchitectureTestWithHistoricalDRParameters, performanceReport, true);
        performanceReport.publish();
        Reporter.log(performanceReport.getPerformanceReportHtml());
    }
}
