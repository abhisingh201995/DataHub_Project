package com.Utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jnr.ffi.annotations.In;
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

public class Helper {

    private final ConfigParser configParser = ConfigParser.getConfigParser();
    private final DataPool dataPool = DataPool.getDataPool();
    GcpHelper gcpHelper = new GcpHelper();

    public void loadTestData() throws IOException {

        HashMap<Object,JSONArray> testDataPool = new HashMap<Object, JSONArray>();
        HashMap<String, String> testCaseMapping = new HashMap<>();

        List<String> data =gcpHelper.listBucketObjects(configParser.inputParam.get("conf.gcpBaseProject"),"qa-test-data");
if((configParser.inputParam.get("conf.readTestDataFromGcp").equalsIgnoreCase("yes"))) {
    System.out.println("************* Reading test data from GCP and not from local file.....");    
	for(String file: data) {

            if(file.contains(System.getenv("Release"))) {
                String fileData = gcpHelper.readFileFromBucket(configParser.inputParam.get("conf.gcpBaseProject"),
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
}
else
{
	System.out.println("************* Reading test data from Local and not from GCP.....");    

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
                    testCaseMapping.put((String) key,(String) ((JSONObject)((JSONObject) (((JSONArray) value).get(0))).get("input")).get("testcaseId"));

                });
            }catch (Exception e) {throw new Error("Unable to get json from files"); }
        });
        dataPool.setTestDataPool(testDataPool);
}// endElse
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

    public Response sendApiRequestAlmCustomHeader(String method,String url,String payload,HashMap<String,String> headers){
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

    public List<String> readFilesFromFolder(String folderPath){

        List<String> files = new ArrayList<>();
        File folder= new File(folderPath);

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                files.add(fileEntry.getName());
            }
        }
        return files;
    }

    public boolean isFileDownloaded(String downloadPath, String fileName) {
        boolean flag = false;
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();

        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().equals(fileName))
                return flag=true;
        }

        return flag;
    }

    public String getLatestFileNamefromDir(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile.getName();
    }

}
