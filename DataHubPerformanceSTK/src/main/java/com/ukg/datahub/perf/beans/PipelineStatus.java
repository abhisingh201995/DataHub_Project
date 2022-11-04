package com.ukg.datahub.perf.beans;

public enum PipelineStatus {

    SUCCESS("Success"),
    RUNNING("Running"),
    FAILED("Failed"),
    KILL("Kill"),
    FAILED_TO_START("Failed to Start"),
    WARNING("Warning"),
    NOT_RUNNING("Not Running"),
    TERMINATED("Terminated");

    String value;

    PipelineStatus(String value) {
        this.value = value; }

    public String getValue() {
        return value;
    }
}
