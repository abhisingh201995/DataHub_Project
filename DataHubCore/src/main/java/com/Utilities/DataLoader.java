package com.Utilities;

import org.json.simple.JSONObject;

import java.util.HashMap;

public class DataLoader extends SuiteInitializer{

    private String url;
    private String method;
    private String payload;
    private String description;
    private HashMap<String,String> testParameter;
    private String testCaseId;
    private String outputStatusCode;
    private String expectedResponse;
    private HashMap<String,String> outputData;
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public HashMap<String, String> getOutputData() {
        return outputData;
    }

    public void setOutputData(HashMap<String, String> outputData) {
        this.outputData = outputData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public HashMap<String, String> getTestParameter() {
        return testParameter;
    }

    public void setTestParameter(HashMap<String, String> testParameter) {
        this.testParameter = testParameter;
    }

    public String getOutputStatusCode() {
        return outputStatusCode;
    }

    public void setOutputStatusCode(String outputStatusCode) {
        this.outputStatusCode = outputStatusCode;
    }

    public String getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
    }


    public DataLoader(JSONObject data){
        try {

            HashMap<String, Object> inputData = new HashMap<>();
            HashMap<String, Object> outputData = new HashMap<>();
            inputData = (HashMap<String, Object>) data.get("input");
            outputData = (HashMap<String, Object>) data.get("output");

            //InputFields
            setPayload((String) inputData.get("payload"));
            setMethod((String) inputData.get("method"));
            setUrl((String) inputData.get("URL"));
            setDescription((String) inputData.get("description"));
            setTestParameter((HashMap<String, String>) inputData.get("parameter"));
            setTestCaseId((String) inputData.get("testcaseId"));
            //OutputFields
            setExpectedResponse((String) outputData.get("response"));
            setOutputStatusCode((String) outputData.get("statusCode"));
            setOutputData((HashMap<String, String>) outputData.get("outputParameter"));

        }catch (Exception e){
            System.out.println(e);
        }

    }


}
