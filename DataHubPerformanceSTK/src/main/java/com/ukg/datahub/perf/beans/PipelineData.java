package com.ukg.datahub.perf.beans;


import com.ukg.datahub.perf.pipeline.PipelinePerformanceSettings;

public class PipelineData {

    private String updatedtm;
    private String pipelineStartdtm;
    private String pipelineEnddtm;
    private String pipelineExecutionTime;
    private String pipelineMemory;
    private String numberOfChunks;
    private String getManifestExecutionTime;
    private String getManifestMemory;
    private String apiControllerExecutionTime;
    private String apiControllerMemory;
    private String getBusinessStructureTimeAndMemory;
    private String getForecastWeeksTimeAndMemory;
    private String TotalResponseSize;
    private String bqControllerMemory;
    private String bqControllerExecutionTime;
    private String maxConcurrentAPICall;
    private PipelinePerformanceSettings pipelinePerformanceSettings;
    private String variation;
    public String pipelineState;
    public String errorMessage;

    public String getPipelineState() {
        return pipelineState;
    }

    public void setPipelineState(String pipelineState) {
        this.pipelineState = pipelineState;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PipelinePerformanceSettings getPipelinePerformanceSettings() {
        return pipelinePerformanceSettings;
    }

    public void setPipelinePerformanceSettings(PipelinePerformanceSettings pipelinePerformanceSettings) {
        this.pipelinePerformanceSettings = pipelinePerformanceSettings;
    }

    public String getMaxConcurrentAPICall() {
        return maxConcurrentAPICall;
    }

    public void setMaxConcurrentAPICall(String maxConcurrentAPICall) {
        this.maxConcurrentAPICall = maxConcurrentAPICall;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }


    public String getGetManifestExecutionTime() {
        return getManifestExecutionTime;
    }

    public void setGetManifestExecutionTime(String getManifestExecutionTime) {
        this.getManifestExecutionTime = getManifestExecutionTime;
    }

    public String getGetManifestMemory() {
        return getManifestMemory;
    }

    public void setGetManifestMemory(String getManifestMemory) {
        this.getManifestMemory = getManifestMemory;
    }

    public String getApiControllerExecutionTime() {
        return apiControllerExecutionTime;
    }

    public void setApiControllerExecutionTime(String apiControllerExecutionTime) {
        this.apiControllerExecutionTime = apiControllerExecutionTime;
    }

    public String getApiControllerMemory() {
        return apiControllerMemory;
    }

    public void setApiControllerMemory(String apiControllerMemory) {
        this.apiControllerMemory = apiControllerMemory;
    }

    public String getGetBusinessStructureTimeAndMemory() {
        return getBusinessStructureTimeAndMemory;
    }

    public void setGetBusinessStructureTimeAndMemory(String getBusinessStructureTimeAndMemory) {
        this.getBusinessStructureTimeAndMemory = getBusinessStructureTimeAndMemory;
    }

    public String getGetForecastWeeksTimeAndMemory() {
        return getForecastWeeksTimeAndMemory;
    }

    public void setGetForecastWeeksTimeAndMemory(String getForecastWeeksTimeAndMemory) {
        this.getForecastWeeksTimeAndMemory = getForecastWeeksTimeAndMemory;
    }

    public String getTotalResponseSize() {
        return TotalResponseSize;
    }

    public void setTotalResponseSize(String totalResponseSize) {
        TotalResponseSize = totalResponseSize;
    }

    public String getBqControllerMemory() {
        return bqControllerMemory;
    }

    public void setBqControllerMemory(String bqControllerMemory) {
        this.bqControllerMemory = bqControllerMemory;
    }

    public String getBqControllerExecutionTime() {
        return bqControllerExecutionTime;
    }

    public void setBqControllerExecutionTime(String bqControllerExecutionTime) {
        this.bqControllerExecutionTime = bqControllerExecutionTime;
    }

    public String getLoadDataBQhttpMemory() {
        return loadDataBQhttpMemory;
    }

    public void setLoadDataBQhttpMemory(String loadDataBQhttpMemory) {
        this.loadDataBQhttpMemory = loadDataBQhttpMemory;
    }

    public String getLoadDataBQhttpWxecutionTime() {
        return loadDataBQhttpWxecutionTime;
    }

    public void setLoadDataBQhttpWxecutionTime(String loadDataBQhttpWxecutionTime) {
        this.loadDataBQhttpWxecutionTime = loadDataBQhttpWxecutionTime;
    }

    public String getPurgePipelineData() {
        return purgePipelineData;
    }

    public void setPurgePipelineData(String purgePipelineData) {
        this.purgePipelineData = purgePipelineData;
    }

    private String loadDataBQhttpMemory;
    private String loadDataBQhttpWxecutionTime;
    private String purgePipelineData;


    public String getPipelineExecutionTime() {
        return pipelineExecutionTime;
    }

    public void setPipelineExecutionTime(String pipelineExecutionTime) {
        this.pipelineExecutionTime = pipelineExecutionTime;
    }

    public String getPipelineMemory() {
        return pipelineMemory;
    }

    public void setPipelineMemory(String pipelineMemory) {
        this.pipelineMemory = pipelineMemory;
    }

    public String getNumberOfChunks() {
        return numberOfChunks;
    }

    public void setNumberOfChunks(String numberOfChunks) {
        this.numberOfChunks = numberOfChunks;
    }


    public String getPipelineStartdtm() {
        return pipelineStartdtm;
    }

    public void setPipelineStartdtm(String pipelineStartdtm) {
        this.pipelineStartdtm = pipelineStartdtm;
    }

    public String getPipelineEnddtm() {
        return pipelineEnddtm;
    }

    public void setPipelineEnddtm(String pipelineEnddtm) {
        this.pipelineEnddtm = pipelineEnddtm;
    }

    public String getUpdatedtm() {
        return updatedtm;
    }

    public void setUpdatedtm(String updatedtm) {
        this.updatedtm = updatedtm;
    }

}
