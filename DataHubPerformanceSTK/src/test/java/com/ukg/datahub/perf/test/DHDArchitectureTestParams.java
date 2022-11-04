package com.ukg.datahub.perf.test;

public class DHDArchitectureTestParams extends AbstractTestParameters {

    private String maxApiCallsPerMinute;
    private String maxConcurrentApiCalls;
    private String performanceTier;
    private String runType;
    private String pipeline;
    private String tenantName;

    public DHDArchitectureTestParams() {
    }


    public DHDArchitectureTestParams(String testId, String testDescription, String maxApiCallsPerMinute, String maxConcurrentApiCalls, String performanceTier, String runType, String pipeline,
                                     String tenantName) {
        super(testId, testDescription);
        this.maxApiCallsPerMinute = maxApiCallsPerMinute;
        this.maxConcurrentApiCalls = maxConcurrentApiCalls;
        this.performanceTier = performanceTier;
        this.runType = runType;
        this.pipeline = pipeline;
        this.tenantName = tenantName;
    }


    public String getMaxApiCallsPerMinute() {
        return maxApiCallsPerMinute;
    }

    public String getMaxConcurrentApiCalls() {
        return maxConcurrentApiCalls;
    }

    public String getPerformanceTier() {
        return performanceTier;
    }

    public String getRunType() {
        return runType;
    }

    public String getPipeline() {
        return pipeline;
    }

    public String getTenantName() {
        return tenantName;
    }

    @Override
    public String toString() {
        return "DHDArchitectureTestParams{" +
                "maxApiCallsPerMinute='" + maxApiCallsPerMinute + '\'' +
                ", maxConcurrentApiCalls='" + maxConcurrentApiCalls + '\'' +
                ", performanceTier='" + performanceTier + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", runType='" + runType + '\'' +
                ", pipeline='" + pipeline + '\'' +
                ", testId='" + testId + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", assertions='" + assertions + '\'' +
                '}';
    }
}
