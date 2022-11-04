package com.Utilities;

import com.DataObjects.DhssDataObject.Adminlogindto;
import com.DataObjects.DhssDataObject.Seedlogindto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;

public class DataPool {


    private String accessTokenDhd;

    private String dateTime= LocalDateTime.now().toString();

    private static Adminlogindto adminlogindto;
    private static Seedlogindto seedlogindto;

    private static String envBaseUrl=System.getenv("baseUrl");
    private static String dhssEnvBaseUrl=System.getenv("baseUrlDhss");
    private static String dhssWebEnvBaseUrl=System.getenv("baseUrlWebDhss");

    public static String getDhssWebEnvBaseUrl() {
        return dhssWebEnvBaseUrl;
    }

    public static void setDhssWebEnvBaseUrl(String dhssWebEnvBaseUrl) {
        DataPool.dhssWebEnvBaseUrl = dhssWebEnvBaseUrl;
    }

    private HashMap<Object,String> AllAccessToken = new HashMap<>();

    private String tenantsDataHubDirector;

    private String wrapper;
    private static DataPool dataPool;
    private JSONObject testdata;

    private String tenantNameUser;
    private String tenantShortName;


    public String getDateTime() {
        return dateTime;
    }

    public String getTenantShortName() {
        return tenantShortName;
    }

    public void setTenantShortName(String tenantShortName) {
        this.tenantShortName = tenantShortName;
    }

    public String getTenantNameUser() {
        return tenantNameUser;
    }

    public void setTenantNameUser(String tenantNameUser) {
        this.tenantNameUser = tenantNameUser;
    }

    public String getWrapper() {
        return wrapper;
    }

    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    public String getTenantsDataHubDirector() {
        return tenantsDataHubDirector;
    }

    public void setTenantsDataHubDirector(String tenantsDataHubDirector) {
        this.tenantsDataHubDirector = tenantsDataHubDirector;
    }

    public HashMap<Object, JSONArray> getTestDataPool() {
        return testDataPool;
    }

    public void setTestDataPool(HashMap<Object, JSONArray> testDataPool) {
        this.testDataPool = testDataPool;
    }

    public String getAccessTokenDhd() {
        return accessTokenDhd;
    }

    public void setAccessTokenDhd(String accessTokenDhd) {
        this.accessTokenDhd = accessTokenDhd;
    }

    public HashMap<Object, String> getAllAccessToken() {
        return AllAccessToken;
    }

    public void setAllAccessToken(HashMap<Object, String> allAccessToken) {
        this.AllAccessToken = allAccessToken;
    }


    private HashMap<Object, JSONArray> testDataPool=new HashMap<>();

    private HashMap<String, String> testcaseMapping=new HashMap<>();

    public HashMap<String, String> getTestcaseMapping() {
        return testcaseMapping;
    }

    public void setTestcaseMapping(HashMap<String, String> testcaseMapping) {
        this.testcaseMapping = testcaseMapping;
    }

    private HashMap<String, String> jsonSchemaPool=new HashMap<>();

    public HashMap<String, String> getJsonSchemaPool() {
        return jsonSchemaPool;
    }

    public void setJsonSchemaPool(HashMap<String, String> jsonSchemaPool) {
        this.jsonSchemaPool = jsonSchemaPool;
    }

    public String getEnvBaseUrl() {
        return envBaseUrl;
    }

    public String getDhssEnvBaseUrl() {
        return dhssEnvBaseUrl;
    }

    public Adminlogindto getAdminlogindto() {
        return adminlogindto;
    }

    public void setAdminlogindto(Adminlogindto adminlogindto) {
        this.adminlogindto = adminlogindto;
    }

    public Seedlogindto getSeedlogindto() {
        return seedlogindto;
    }

    public void setSeedlogindto(Seedlogindto seedlogindto) {
        this.seedlogindto = seedlogindto;
    }

    public static DataPool getDataPool(){
        if(dataPool ==null){
            dataPool =new DataPool();
            adminlogindto = new Adminlogindto();
            seedlogindto =  new Seedlogindto();
        }
        return dataPool;
    }

}
