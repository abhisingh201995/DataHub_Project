package com.ukg.datahub.perf.test;

public class DHDArchitectureTestWithIncrementalDRParameters extends DHDArchitectureTestParams {

    private String overrideStartDate;
    private String overrideEndDate;
    private String pipelinePerformanceSettings;

    public DHDArchitectureTestWithIncrementalDRParameters() {

    }


    public DHDArchitectureTestWithIncrementalDRParameters(String testId, String testDescription, String maxApiCallsPerMinute, String maxConcurrentApiCalls, String performanceTier, String runType, String pipeline, String tenantName) {
        super(testId, testDescription, maxApiCallsPerMinute, maxConcurrentApiCalls, performanceTier, runType, pipeline, tenantName);
    }

    public String getOverrideStartDate() {
        return overrideStartDate;
    }

    public String getOverrideEndDate() {
        return overrideEndDate;
    }

    public String getPipelinePerformanceSettings() {
        return pipelinePerformanceSettings;
    }

    @Override
    public String toString() {
        return "DHDArchitectureTestWithIncrementalDRParameters{" +
                "overrideStartDate='" + overrideStartDate + '\'' +
                ", overrideEndDate='" + overrideEndDate + '\'' +
                ", pipelinePerformanceSettings='" + pipelinePerformanceSettings + '\'' +
                ", testId='" + testId + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", assertions='" + assertions + '\'' +
                '}';
    }
}
