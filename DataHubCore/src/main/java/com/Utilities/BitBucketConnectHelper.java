package com.Utilities;
//https://kronos.webhook.office.com/webhookb2/b3d3d9ff-8eb3-46f7-9268-8e601aa9ad89@7b6f35d2-1f98-4e5e-82eb-e78f6ea5a1de/IncomingWebhook/f9e14449a6f4479d8c0ef095e67f82f2/b703d4f7-4ff9-49eb-a455-464b4d931e1d
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class BitBucketConnectHelper extends SuiteInitializer {

    private final ConfigParser configParser = ConfigParser.getConfigParser();

    private String username;
    private String appToken;
    private String encodedCredentials;
    private String repositoryName;
    private String branchName;
    private String filename;

    public BitBucketConnectHelper() {

        username = configParser.inputParam.get("conf.bitBucketUserName");
        appToken = configParser.inputParam.get("conf.bitBucketToken");//enter your 20 character app password here
        try {
            encodedCredentials = Base64.getEncoder().encodeToString((username+":"+appToken).getBytes("UTF-8")); //Bitbucket REST API needs the credentials to be Base64 encoded with "UTF-8" formatting
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        repositoryName = "data-hub";
    }

    public List<String> getFilesFromFolder(String folderPath) {
        String urlToAccess = "https://engstash.int.kronos.com/rest/api/1.0/projects/EXT/repos/"+repositoryName+"/browse/"+folderPath+"?until="+System.getenv("codebase_branch");
        String connectionStreamData="";
        try {
            URL repositoryUrl = new URL (urlToAccess);
            HttpURLConnection connection = (HttpURLConnection) repositoryUrl.openConnection();
            //For authentication

            connection.addRequestProperty("Authorization", "Basic " + encodedCredentials);
            connection.setRequestMethod("GET");
            connection.connect();
            System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());
            //The InputStream is required to read in the data of the GET request
            InputStream connectionDataStream = connection.getInputStream();
            connectionStreamData = IOUtils.toString(connectionDataStream, StandardCharsets.UTF_8);
        }catch (Exception e){

        }
        System.out.println(connectionStreamData);

        HashMap<Object,Object> data = new Gson().fromJson(connectionStreamData, HashMap.class);
        List<LinkedTreeMap> listOfFileData= new ArrayList<>();
        listOfFileData= (List<LinkedTreeMap>) ((LinkedTreeMap)data.get("children")).get("values");
        List<String> listFileName= new ArrayList<>();
        for(LinkedTreeMap item: listOfFileData){
           listFileName.add((String) ((LinkedTreeMap)item.get("path")).get("name"));
        }
        return listFileName;
    }

    public String getFileData(String fileName, String folderPath) {
        String urlToAccess = "https://engstash.int.kronos.com/rest/api/1.0/projects/EXT/repos/"+repositoryName+"/browse/"+folderPath+fileName;
        String connectionStreamData="";
        try {
            URL repositoryUrl = new URL (urlToAccess);
            HttpURLConnection connection = (HttpURLConnection) repositoryUrl.openConnection();
            //For authentication

            connection.addRequestProperty("Authorization", "Basic " + encodedCredentials);
            connection.setRequestMethod("GET");
            connection.connect();
            //The InputStream is required to read in the data of the GET request
            InputStream connectionDataStream = connection.getInputStream();
            connectionStreamData = IOUtils.toString(connectionDataStream, StandardCharsets.US_ASCII);
        }catch (Exception e){

        }

        return connectionStreamData;
    }

}

