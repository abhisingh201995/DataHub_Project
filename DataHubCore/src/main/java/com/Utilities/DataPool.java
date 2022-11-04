package com.Utilities;

import com.DataObjects.DhssDataObject.Adminlogindto;
import com.DataObjects.DhssDataObject.Seedlogindto;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import io.grpc.LoadBalancerRegistry;
import io.grpc.internal.PickFirstLoadBalancerProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalDateTime;
import java.util.HashMap;

public class DataPool {


    private static String dhdApiBaseUrl;
    private static String dhdWebBaseUrl;
    private static String dhssApiBaseUrl;
    private static String dhssWebBaseUrl;
    private static String bitBucketUsername;
    private static String bitBucketToken;
    private static String dhssAdminUsername;
    private static String dhssAdminPassword;
    private static String dhdUsername;
    private static String dhdPassword;
    private static String teamsWebHook;

    private String accessTokenDhd;

    private String dateTime= LocalDateTime.now().toString();

    private static Adminlogindto adminlogindto;
    private static Seedlogindto seedlogindto;


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

    private HashMap<String, String> jsonSchemaPool=new HashMap<>();

    public HashMap<String, String> getJsonSchemaPool() {
        return jsonSchemaPool;
    }

    public void setJsonSchemaPool(HashMap<String, String> jsonSchemaPool) {
        this.jsonSchemaPool = jsonSchemaPool;
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

    public static String getDhdApiBaseUrl(String env) {
        return dhdApiBaseUrl.replace("{replaceEnv}",env);
    }

    public static String getDhdWebBaseUrl(String env) {
        return dhdWebBaseUrl.replace("{replaceEnv}",env);
    }

    public static String getDhssApiBaseUrl(String env) {
        return dhssApiBaseUrl.replace("{replaceEnv}",env);
    }

    public static String getDhssWebBaseUrl(String env) {
        return dhssWebBaseUrl.replace("{replaceEnv}",env);
    }

    public static String getBitBucketUsername() {
        return bitBucketUsername;
    }

    public static String getBitBucketToken() {
        return bitBucketToken;
    }

    public static String getDhssAdminUsername() {
        return dhssAdminUsername;
    }

    public static String getDhssAdminPassword() {
        return dhssAdminPassword;
    }

    public static String getDhdUsername() {
        return dhdUsername;
    }

    public static String getDhdPassword() {
        return dhdPassword;
    }

    public static String getTeamsWebHook() {
        return teamsWebHook;
    }

    public static DataPool getDataPool(){
        if(dataPool ==null){
            dataPool =new DataPool();
            adminlogindto = new Adminlogindto();
            seedlogindto =  new Seedlogindto();

            String projectId="repd-e-eng-adm-01";

            String qaSecret;

            LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
            try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
                SecretVersionName secretVersionName = SecretVersionName.of(projectId, "qa_sensitive_key", "latest");
                AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
                qaSecret = response.getPayload().getData().toStringUtf8();
                JSONObject qaData = (JSONObject) new JSONParser().parse(qaSecret);

                dhdUsername = (String) qaData.get("dhdUsername");
                dhdPassword = (String) qaData.get("dhdPassword");
                bitBucketUsername = (String) qaData.get("bitBucketUsername");
                bitBucketToken = (String) qaData.get("bitBucketToken");
                dhssAdminUsername = (String) qaData.get("dhssAdminUsername");
                dhssAdminPassword = (String) qaData.get("dhssAdminPassword");
                teamsWebHook = (String) qaData.get("teamsWebHook");

                dhdWebBaseUrl = (String) qaData.get("server")+"admin/";
                dhdApiBaseUrl = (String) qaData.get("server")+"api/v1/";

                dhssWebBaseUrl = (String) qaData.get("server")+"selfservice/";
                dhssApiBaseUrl = (String) qaData.get("server")+"selfservice/v1/edap/";

            } catch (Exception e) {
                Log.error(e.toString());
            }
        }
        return dataPool;
    }

}
