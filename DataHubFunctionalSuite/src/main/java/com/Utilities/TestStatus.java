package com.Utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestStatus {

    @JsonProperty("testClass")
    private String testClass;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("executionTime")
    private String executionTime;

    @JsonProperty("release")
    private String release;

    @JsonProperty("moduleName")
    private String moduleName;

    @JsonProperty("buildNumber")
    private String buildNumber;

    @JsonProperty("log")
    private String log;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExecutionDate(String executionTime) {
        this.executionTime = executionTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public void setRelease(String release) { this.release = release; }

    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
