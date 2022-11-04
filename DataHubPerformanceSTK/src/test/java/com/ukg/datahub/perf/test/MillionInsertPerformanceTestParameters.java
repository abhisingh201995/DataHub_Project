package com.ukg.datahub.perf.test;

public class MillionInsertPerformanceTestParameters extends AbstractTestParameters {

    private String tenantName;
    private String runType;
    private String pipeline;
    private int peopleRowCount;

    public MillionInsertPerformanceTestParameters() {
    }

    public MillionInsertPerformanceTestParameters(String testId, String testDescription, String tenantName, String runType, String pipeline, int peopleRowCount) {
        super(testId, testDescription);
        this.tenantName = tenantName;
        this.testDescription = testDescription;
        this.runType = runType;
        this.peopleRowCount = peopleRowCount;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public int getPeopleRowCount() {
        return peopleRowCount;
    }

    public void setPeopleRowCount(int peopleRowCount) {
        this.peopleRowCount = peopleRowCount;
    }

    @Override
    public String toString() {
        return "MillionInsertPerformanceTestParameters{" +
                "tenantName='" + tenantName + '\'' +
                "PeopleRowCount='" + peopleRowCount + '\'' +
                ", runType='" + runType + '\'' +
                ", pipeline='" + pipeline + '\'' +
                ", testId='" + testId + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", assertions='" + assertions + '\'' +
                '}';
    }
}
