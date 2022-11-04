package com.Utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.reporters.Files;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.given;

public class Helper extends SuiteInitializer {


    public Helper(String projectId) {

        this.projectId=projectId;
        this.gcpHelper = new GcpHelper(projectId);
    }
    String projectId;
    GcpHelper gcpHelper;

    public void loadTestData() throws IOException {

        HashMap<Object,JSONArray> testDataPool = new HashMap<Object, JSONArray>();

        List<String> data =gcpHelper.listBucketObjects("qa-test-data");

        for(String file: data) {

            if(file.contains("CoreTestData")) {
                String fileData = gcpHelper.readFileFromBucket(
                        "qa-test-data",
                        file);
                if (!fileData.equals("")) {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) jsonParser.parse(fileData);
                    } catch (ParseException e) {

                    }
                    jsonObject.forEach((key, value) -> {
                        testDataPool.put(key, (JSONArray) value);
                    });
                }
            }
        }

        dataPool.setTestDataPool(testDataPool);


   // for local testing only
        Map<String,String> testDataPath= new HashMap<>();
        File folder= new File("src/main/resources/TestData");

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                testDataPath.put(fileEntry.getName(),fileEntry.getCanonicalPath());
            }
        }

        testDataPath.forEach((k,v)->{
            try{
                File file = new File(v);
                String content = (String) Files.readFile(file);
                JSONParser jsonParser=new JSONParser();
                JSONObject jsonObject=(JSONObject) jsonParser.parse(content);
                jsonObject.forEach((key,value)->{
                    testDataPool.put(key, (JSONArray) value);

                });
            }catch (Exception e) {throw new Error("Unable to get json from files"); }
        });
        dataPool.setTestDataPool(testDataPool);

     //end local testing
    }

    public Object[][] testNgProvider(JSONArray jsonElements){
        Object[][] testData=new Object[jsonElements.size()][];
        for(int i=0;i<jsonElements.size();i++){
            testData[i] = new Object[1];
            testData[i][0]= jsonElements.get(i);
        }
        return testData;

    }

    public void authenticationOAuth(){
        RequestSpecification specification=given().auth().preemptive().basic("","").formParam("","");
        Response response=specification.request("","").then().extract().response();
    }

    public String getDateTimeStamp(){
        String dateTime= LocalDateTime.now().toString().replace(":","").replace(".","").replace("-","");
        return dateTime;
    }

    public Response sendApiRequest(String method,String url,String payload, String authToken){
        RequestSpecification spec = given();
        spec = spec.
                contentType("application/json").
                accept("application/json");
        if(payload!=null){
            spec=spec.body(payload);
        }
        if(authToken!=null){
            spec=spec.header("Authorization",authToken);

        }
        Response response=spec.request(method,url).then().extract().response();
        return response;
    }

    public Response sendApiRequestRelaxedHttps(String method,String url,String payload, String authToken){
        RequestSpecification spec = given().relaxedHTTPSValidation();
        spec = spec.
                contentType("application/json").
                accept("application/json");
        if(payload!=null){
            spec=spec.body(payload);
        }
        if(authToken!=null){
            spec=spec.header("Authorization",authToken);

        }
        Response response=spec.request(method,url).then().extract().response();
        return response;
    }

    public Response sendApiRequestRelaxedHttpsCustomHeader(String method,String url,String payload,HashMap<String,String> headers){
        RequestSpecification spec = given().relaxedHTTPSValidation();
        spec = spec.headers(headers);
        if(payload!=null){
            spec=spec.body(payload);
        }
        Response response=spec.request(method,url).then().extract().response();
        return response;
    }

    public void sendMessageOnTeams(String message){
        ObjectMapper OM = new ObjectMapper();
        String CONTENT_TYPE = "Content-Type";
        String CONTENT_TYPE_VALUE = "application/json";

        sendApiRequest("POST",configParser.inputParam.get("conf.teamsWebHook"),message,null);

    }

    public boolean isToday(Date date){
        Calendar today = Calendar.getInstance();
        Calendar specifiedDate  = Calendar.getInstance();
        specifiedDate.setTime(date);

        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                &&  today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH)
                &&  today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }

    public void validateApiResponseStatusCode(Response response, DataLoader dataLoader){
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect with response="+response.prettyPrint());

    }


}
