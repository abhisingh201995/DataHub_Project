package com.ukg.datahub.perf.test;

import com.Utilities.GcpHelper;
import com.ukg.datahub.perf.beans.PipelineData;
import com.ukg.datahub.perf.constants.Constants;
import com.ukg.datahub.perf.helpers.SystemValueHandler;
import com.ukg.datahub.perf.metrics.Metrics;
import com.ukg.datahub.perf.metrics.MetricsHttp;
import com.ukg.datahub.perf.pipeline.PipelinePerformanceSettings;
import com.ukg.datahub.perf.reporting.PerformanceReport;
import com.ukg.datahub.perf.utilities.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ukg.datahub.perf.reporting.TestProperties.*;

/**
 * This is the test class to measure DHD infrastructure scalability
 */
public class DHDArchitectureWithIncrementalDRTest extends BaseDHDArchitectureTest<DHDArchitectureTestWithIncrementalDRParameters> {

    private String dataSetName = "Metrics";
    private Metrics metrics;
    private GcpHelper gcpHelper;
    private String errorMessage;
    private SoftAssert softAssert = new SoftAssert();
    Map<String, String> runHistoryMap;

    @Test(description = "This test is executed to test DHD Architecture with Incremental Data Run", dataProvider = "testPayloadDataProvider")
    public void testDHDArchWithIncrementalDataRun(DHDArchitectureTestWithIncrementalDRParameters dhdArchitectureTestWithIncrementalDRParameters) throws Exception {

        logger.info("**********Starting DHD Infra Scalability Test**********");

        metrics = new Metrics(projectId);
        gcpHelper = new GcpHelper(projectId);
        runHistoryMap = new HashMap<>();
        String TOPIC_ID = "triggerStartPipeline";
        maxConcurrentApiCallsList = CommonUtils.convertDelimitedStringToList(dhdArchitectureTestWithIncrementalDRParameters.getMaxConcurrentApiCalls(), ",");
        pipeLine = dhdArchitectureTestWithIncrementalDRParameters.getPipeline();
        testId = (dhdArchitectureTestWithIncrementalDRParameters.getTestId() == null) ? generateUniquetestId() + pipeLine + "_test" : dhdArchitectureTestWithIncrementalDRParameters.getTestId() + pipeLine + "_test";
        String performanceTier = dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier();
        tenantName = dhdArchitectureTestWithIncrementalDRParameters.getTenantName();
        PipelineData pipelineData;
        String runType = dhdArchitectureTestWithIncrementalDRParameters.getRunType();
        String overrideStartDate = dhdArchitectureTestWithIncrementalDRParameters.getOverrideStartDate();
        String overrideEndDate = dhdArchitectureTestWithIncrementalDRParameters.getOverrideEndDate();
        List<PipelinePerformanceSettings> pipelinePerformanceSettings = deserializePerformanceSettings(dhdArchitectureTestWithIncrementalDRParameters);
        runHistoryMap.put("testId", testId);
        runHistoryMap.put("runMode", runType);
        runHistoryMap.put("pipeline", pipeLine);
        runHistoryMap.put("performanceTier", performanceTier);
        runHistoryMap.put("tenantName", tenantName);
        runHistoryMap.put("overrideStartDate", overrideStartDate);
        runHistoryMap.put("overrideEndDate", overrideEndDate);
        runHistoryMap.put("wrapper", Constants.ElementNames.WRAPPER);

        try {

            if (StringUtils.isEmpty(tenantName)) {
                throw new RuntimeException("No tenant exist for performance tier" + dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier());
            }
            for (PipelinePerformanceSettings pipelinePerformanceSetting : pipelinePerformanceSettings) {
                int variation = 0;
                updatePipelinePerformanceSettings(pipelinePerformanceSetting, dhdArchitectureTestWithIncrementalDRParameters);
                for (String maxConcurrentApiCall : maxConcurrentApiCallsList) {
                    updatePipelinePerformanceTier("public.tenantmgr_pipeline_performance", "max_concurrent_api_calls", maxConcurrentApiCall, dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier());
                    //Prepare JsonObject to execute given pipeline
                    updatedtm = getUpdatedtm();
                    pipelineData = new PipelineData();
                    String stageTableNameSuffix = CommonUtils.removeSpecialChars(updatedtm);
                    runHistoryMap.put("updatedtm", updatedtm);
                    logger.info("updatedtm " + updatedtm);
                    JSONObject message = new JSONObject();
                    message.put("tenant", tenantName);
                    message.put("updateDtm", updatedtm);
                    message.put("runtype", runType);
                    message.put("pipeline", pipeLine);
                    message.put("wrapper", Constants.ElementNames.WRAPPER);

                    if (overrideStartDate != null && overrideEndDate != null) {
                        message.put("overrideStartDate", overrideStartDate);
                        message.put("overrideEndDate", overrideEndDate);
                    }

                    logger.info("Publishing message to " + pipeLine + " pipeline...!!");

                    //Execute the pipeline
                    gcpHelper.publishToPubSub(projectId
                            , TOPIC_ID, message);

                    logger.info("Waiting for the " + pipeLine + " pipeline execution to get finished ..!!");

                    //wait for the pipeline status to change to success
                    String pipeLineState = postgresDBHelper.waitForAColumnValueToChange("public.tenantmgr_jobstatus", "status",
                            "updatedtm = '" + updatedtm + "+00'", "Success", GET_JOB_STATUS_CHECKTIMEOUT, JOB_STATUS_CHECK_SLEEPTIME,
                            pipelineFailureStates);

                    pipelineData.setPipelineState(pipeLineState);

                    logger.info("Pipeline execution finished , inserting test data to the RunHistory table");
                    //Add the values of test parameters to be added to the run history table
                    runHistoryMap.put("personCount", String.valueOf(pipelinePerformanceSetting.getPersonCount()));
                    runHistoryMap.put("dayCount", String.valueOf(pipelinePerformanceSetting.getDayCount()));
                    runHistoryMap.put("maxConcurrentApiCall", maxConcurrentApiCall);
                    runHistoryMap.put("pipelineStatus", pipeLineState);

                    logger.info("Checking time taken by " + pipeLine + " to get executed...!!");
                    //Get the time taken by the given job to get finished
                    variation++;
                    capturePipelineMetrics(pipelineData, stageTableNameSuffix, maxConcurrentApiCall, variation, pipelinePerformanceSetting, dhdArchitectureTestWithIncrementalDRParameters);
                }
            }

            metrics.generateMetricsMeasure(projectId);
            preparePipelineMetrics();
            logger.info("Generating final report ...!!");
            //TODO//Code to pull data into the report//

        } catch (Exception e) {
            softAssert.fail(e.toString());
            IS_TEST_PASSED = false;
            e.printStackTrace();
            errorMessage = e.getMessage();
            logger.error("Exception Occurs while executing the test", e);
        }

        generatePerformanceReport(dhdArchitectureTestWithIncrementalDRParameters, testOutputs);
        softAssert.assertAll();
    }

    /**
     * This method will capture the pipeline performance
     *
     * @param stageTableNameSuffix
     * @param maxConcurrentApiCall
     * @param variation
     * @param dhdArchitectureTestWithIncrementalDRParameters
     * @throws SQLException
     */
    protected void capturePipelineMetrics(PipelineData pipelineData, String stageTableNameSuffix, String maxConcurrentApiCall, int variation, PipelinePerformanceSettings pipelinePerformanceSettings,
                                          DHDArchitectureTestWithIncrementalDRParameters dhdArchitectureTestWithIncrementalDRParameters) throws SQLException {

        String[] CF_USER_MEMORY_BYTES = {"cloud_function",
                "cloudfunctions.googleapis.com/function/user_memory_bytes"};

        setStartAndEndTimeForPipelineExecution(pipelineData);
        int timeTaken = getPipelineExecutionTime();

        //TODO Replace the dataset name
        metrics.createDataset(dataSetName);

        runHistoryMap.put("pipelineStartDate", pipelineData.getPipelineStartdtm());
        runHistoryMap.put("pipelineEndDate", pipelineData.getPipelineEnddtm());

        //Insert Test data to run history table
        metrics.insertIntoRunHistoryTable(projectId, dataSetName, runHistoryMap);
        //TODO replace "region-us" with run time value

        metrics.generateBQMetricsStage(dataSetName, updatedtm, stageTableNameSuffix, "region-us");
        metrics.generateCFMetricsStage(dataSetName, updatedtm, stageTableNameSuffix);

        String metricsJson = MetricsHttp.doRequestPost(projectId, CF_USER_MEMORY_BYTES, pipelineData.getPipelineStartdtm(),//start time
                pipelineData.getPipelineEnddtm());

        //TODO what metricsName and wrapper needs to be passed MetricName
        metrics.createAndInsertIntoMetricsStage(dataSetName, stageTableNameSuffix, updatedtm, tenantName,
                Constants.ElementNames.WRAPPER, dhdArchitectureTestWithIncrementalDRParameters.getPipeline(), CF_USER_MEMORY_BYTES[1],
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
        pipelineData.setPipelinePerformanceSettings(pipelinePerformanceSettings);
        pipelineDataList.add(pipelineData);

        metrics.moveBQDataToHistoryTable(dataSetName, projectId, dataSetName, stageTableNameSuffix);
        metrics.moveCFDataToHistoryTable(dataSetName, projectId, dataSetName, stageTableNameSuffix);
        metrics.moveMetricDataToHistoryTable(dataSetName, projectId, dataSetName, stageTableNameSuffix);
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
        List<String> getpiplinePerformnaceSettings = pipelineDataList.stream().map(p -> p.getPipelinePerformanceSettings() != null ? p.getPipelinePerformanceSettings().toString() : null
        ).collect(Collectors.toList());
        List<String> getpiplineState = pipelineDataList.stream().map(p -> p.getPipelineState()).collect(Collectors.toList());


        testOutputs.put("Pipeline Performance Settings", getpiplinePerformnaceSettings);
        testOutputs.put("Variation", getVariation);
        testOutputs.put("Max concurrent API calls", getMaxConcurrentAPICall);
        testOutputs.put("PipeLine State", getpiplineState);
        testOutputs.put("Start Time", getStartdtm);
        testOutputs.put("End Time", getenddtm);
        testOutputs.put("Total Execution Time", pipelineExecutionTimeList);
        testOutputs.put("UpdatedTM", getUpdatedtm);

        //TODO replace them with actual values
        //testOutputs.put("Pipeline Memory", pipelineMemoryList);
        // testOutputs.put("List Of Number Of Chunks", listOfNumberOfChunks);
        // testOutputs.put("Manifest Execution Time", getManifestExecutionTime);
        // testOutputs.put("Business Structure Time And Memory", getGetBusinessStructureTimeAndMemory);
        //  testOutputs.put("Bq Controller Execution Time", getBqControllerExecutionTime);
        //testOutputs.put("Forecast Weeks Time And Memory", getGetForecastWeeksTimeAndMemory);
        // testOutputs.put("Purge Pipeline Data", getPurgePipelineData);
        //  testOutputs.put("Total Response Size", getTotalResponseSize);
        // testOutputs.put("Api Controller Memory", getApiControllerMemory);
        // testOutputs.put("Api Controller Execution Time", getApiControllerExecutionTime);

        for (String updatedtm : getUpdatedtm) {
            String stageTableName = CommonUtils.removeSpecialChars(updatedtm);
            //Drop all stage table post fetching the metrics
            metrics.dropStageTables(dataSetName, stageTableName);
        }
    }

    /**
     * This method will update the pipeline performance settings in the postgres DB
     *
     * @param pipelinePerformanceSettings
     * @param dhdArchitectureTestWithIncrementalDRParameters
     */
    private void updatePipelinePerformanceSettings(PipelinePerformanceSettings pipelinePerformanceSettings, DHDArchitectureTestWithIncrementalDRParameters dhdArchitectureTestWithIncrementalDRParameters) {

        logger.info("Updating pipeline performance settings " + pipelinePerformanceSettings.toString());
        int personCount = pipelinePerformanceSettings.getPersonCount();
        int dayCount = pipelinePerformanceSettings.getDayCount();

        String updatePersonCountQuery = "UPDATE  public.tenantmgr_pipelineperformancesettings SET setting_numeric_value = " + personCount + " where   \n" +
                "pipeline_name_id = '" + dhdArchitectureTestWithIncrementalDRParameters.getPipeline() + "' and performance_tier_id LIKE " + "'%' || '" + dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier() + "' || '%'" + " and setting_name = 'PERSON_COUNT'";

        String updateDayCountQuery = "UPDATE  public.tenantmgr_pipelineperformancesettings SET setting_numeric_value = " + dayCount + " where   \n" +
                "pipeline_name_id = '" + dhdArchitectureTestWithIncrementalDRParameters.getPipeline() + "' and performance_tier_id LIKE " + "'%' || '" + dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier() + "' || '%'" + " and setting_name = 'DAY_COUNT'";


        String existingPersonCountValue = getExistingValueOfPersonCountFromDB("PERSON_COUNT", dhdArchitectureTestWithIncrementalDRParameters);
        String existingDayCountValue = getExistingValueOfPersonCountFromDB("DAY_COUNT", dhdArchitectureTestWithIncrementalDRParameters);

        if (StringUtils.isNotEmpty(existingPersonCountValue)) {
            if (!(personCount == Integer.parseInt(existingPersonCountValue))) {
                logger.info("Updating pipelineperformancesettings with query    " +
                        updatePersonCountQuery);
                postgresDBHelper.updateQueryPostgresTable(updatePersonCountQuery);
            }
        }

        if (StringUtils.isNotEmpty(existingDayCountValue)) {
            if (!(dayCount == Integer.parseInt(existingDayCountValue))) {
                logger.info("Updating pipelineperformancesettings with query    " +
                        updateDayCountQuery);
                postgresDBHelper.updateQueryPostgresTable(updateDayCountQuery);
            }
        }

    }

    private String getExistingValueOfPersonCountFromDB(String settingName , DHDArchitectureTestWithIncrementalDRParameters dhdArchitectureTestWithIncrementalDRParameters) {

        try {
            String condition = "pipeline_name_id = '" + dhdArchitectureTestWithIncrementalDRParameters.getPipeline() + "' and performance_tier_id LIKE " + "'%' || '" + dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier() + "' || '%'" + " and setting_name = '" + settingName + "'";
            return postgresDBHelper.queryOnCondition("public.tenantmgr_pipelineperformancesettings", "setting_numeric_value", condition);
        } catch (Exception e) {
            logger.error("Failed to fetch the value of " + settingName, e);
        }
        return null;
    }

    /**
     * This method will generate the performance report
     */
    @Override
    protected void generatePerformanceReport(DHDArchitectureTestWithIncrementalDRParameters dhdArchitectureTestWithIncrementalDRParameters, Map<String, Object> testOutputs) {

        PerformanceReport performanceReport = new PerformanceReport()
                .addProperty(PR_PROP_PRODUCT_LOGIN_URL, SystemValueHandler.fetchExecutionProperty("PRODUCT_LOGIN"))
                .addProperty(PR_PROP_TEST_DESC, dhdArchitectureTestWithIncrementalDRParameters.getTestDescription())
                .addProperty(PR_PROP_TEST_ID, testId)
                .addProperty(PR_PROP_TENANT_NAME, SystemValueHandler.fetchExecutionProperty(dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier()))
                .addProperty(PR_PROP_DB_NAME, SystemValueHandler.fetchExecutionProperty("DB_NAME"))
                .addProperty(PR_PROP_DB_USER, SystemValueHandler.fetchExecutionProperty("DB_USER"))
                .addProperty(PR_PROP_GCP_DATA_PROJECT, SystemValueHandler.fetchExecutionProperty("targetGcpDataProject"))
                .setPassed(IS_TEST_PASSED)
                .setOwners(SystemValueHandler.fetchExecutionProperty("TEST_OWNER"))
                .addParameter("Max Concurrent Api Calls", dhdArchitectureTestWithIncrementalDRParameters.getMaxConcurrentApiCalls())
                .addParameter("Pipeline", dhdArchitectureTestWithIncrementalDRParameters.getPipeline())
                .addParameter("Mode", dhdArchitectureTestWithIncrementalDRParameters.getRunType())
                .addParameter("Performance Tier", dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier())
                .addParameter("Tenant Name", SystemValueHandler.fetchExecutionProperty(dhdArchitectureTestWithIncrementalDRParameters.getPerformanceTier()))
                .addParameter("OverRideStartDate", dhdArchitectureTestWithIncrementalDRParameters.getOverrideStartDate())
                .addParameter("OverRideEndDate", dhdArchitectureTestWithIncrementalDRParameters.getOverrideEndDate());


        if (StringUtils.isNotEmpty(errorMessage)) {
            performanceReport.setError(errorMessage);
        } else {
            performanceReport.addOutputData(testOutputs);
        }


        // Setting testNG test output in test report
        setTestOutput(dhdArchitectureTestWithIncrementalDRParameters, performanceReport, true);
        performanceReport.publish();
        Reporter.log(performanceReport.getPerformanceReportHtml());
    }

    /**
     * This method will deserialize the Pipeline Performance Settings and map it to its POJO class
     *
     * @param dhdArchitectureTestWithIncrementalDRParameters
     * @return
     */
    protected List<PipelinePerformanceSettings> deserializePerformanceSettings(DHDArchitectureTestWithIncrementalDRParameters dhdArchitectureTestWithIncrementalDRParameters) {
        String pipelinePerformanceSettings = dhdArchitectureTestWithIncrementalDRParameters.getPipelinePerformanceSettings();
        if (StringUtils.isBlank(pipelinePerformanceSettings)) {
            throw new IllegalArgumentException("pipelinePerformanceSettings Json cannot be blank ");
        }
        List<PipelinePerformanceSettings> pipelinePerformanceSettingsBeans = Arrays.asList(gson.fromJson(pipelinePerformanceSettings, PipelinePerformanceSettings[].class));
        return pipelinePerformanceSettingsBeans;
    }
}
