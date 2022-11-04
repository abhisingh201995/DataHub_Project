package com.FunctionalUtils;

import com.DataObjects.DhssDataObject.AdditionalPropertDto;
import com.DataObjects.DhssDataObject.CustomerOwnedGcpDto;
import com.DataObjects.DhssDataObject.ServiceKeyFiledto;
import com.Utilities.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class DHSSTenantHelper {

    DataPool dataPool = DataPool.getDataPool();
    ConfigParser configParser = ConfigParser.getConfigParser();
    Helper helper = new Helper();
    ServiceKeyFiledto serviceKeyFiledto = new ServiceKeyFiledto();
    CustomerOwnedGcpDto customerOwnedGcpDto = new CustomerOwnedGcpDto();
    String timeStamp = helper.getDateTimeStamp();

    public void refreshAuthToken(){

        String payload = "{\"username\": \"{username}\",\"password\": \"{password}\"}";
        payload =payload
                .replace("{username}", configParser.inputParam.get("conf.dhssAdminUsername"))
                .replace("{password}",configParser.inputParam.get("conf.dhssAdminPassword"));

        Response response = helper.sendApiRequestRelaxedHttps("POST", dataPool.getDhssEnvBaseUrl()+"admin/tenant/login", payload, null);
        dataPool.getAdminlogindto().setAdminSessionId(response.path("authToken"));
        setAdminSessionInfo();
    }

    public void refreshSeedAuthToken(String tenantType){

        String payload = "{\"userId\": \"{username}\",\"password\": \"{password}\"}";
        payload =payload
                .replace("{username}",  configParser.inputParam.get(tenantType+".tenantSeedUser"))
                .replace("{password}", configParser.inputParam.get(tenantType+".password"));

        Response response = helper.sendApiRequestRelaxedHttps("POST", dataPool.getDhssEnvBaseUrl()+"tenant/seeduser/login", payload, null);
        dataPool.getSeedlogindto().setAccess_token(response.path("authToken"));
        dataPool.getSeedlogindto().setTenantid(response.path("tenantId"));
        setSeedSessionInfo();
    }

    public void setSeedSessionInfo() {
        HashMap<String,String>headersMap = new HashMap<>();
        headersMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader("GET",dataPool.getDhssEnvBaseUrl()+"sessions?action=info",null,headersMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200");
        dataPool.getSeedlogindto().setXSRF_Token(response.getCookie("EDAP-XSRF-TOKEN"));
    }


    public void setAdminSessionInfo() {
        HashMap<String,String>headersMap = new HashMap<>();
        headersMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        headersMap.put("x-tenantId", "edap");
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader("GET",dataPool.getDhssEnvBaseUrl()+"sessions?action=info",null,headersMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200");
        dataPool.getAdminlogindto().setXSRF_Token(response.getCookie("EDAP-XSRF-TOKEN"));
    }

    public String createTenant() {

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        String timeStampNow = helper.getDateTimeStamp();
        JSONObject TenantTestData = (JSONObject) dataPool.getTestDataPool().get("DHSSAdminLoginApiServicesTest.adminLoginCreateTenantTest").get(0);
        DataLoader dataLoader = new DataLoader(TenantTestData);

        String payload = dataPool.getJsonSchemaPool().get("registerTenant.json")
                .replace("{tenantname}", dataLoader.getTestParameter().get("tenantName") + timeStampNow)
                .replace("{tenantshortname}", dataLoader.getTestParameter().get("tenantShortName") + timeStampNow)
                .replace("{edapvanityurl}", dataLoader.getTestParameter().get("tenantVanityURL")+ timeStampNow +".com:9393")
                .replace("{openamurl}", dataLoader.getTestParameter().get("OpenAMURL"))
                .replace("{wfdurl}", dataLoader.getTestParameter().get("WFDURL").replace("{addUniqueChar}",timeStampNow))
                .replace("{environment}", dataLoader.getTestParameter().get("environment"))
                .replace("{solution}", dataLoader.getTestParameter().get("solution"))
                .replace("{seeduser}", dataLoader.getTestParameter().get("tenantSeedUser"))
                .replace("{ssoenabled}", dataLoader.getTestParameter().get("isSSOEnabled"))
                .replace("{clientid}", dataLoader.getTestParameter().get("ClientId"))
                .replace("{clientsecret}", dataLoader.getTestParameter().get("ClientSecret"))
                .replace("{createdBy}", dataLoader.getTestParameter().get("createdBy"));
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataLoader.getDhssEnvBaseUrl() + dataLoader.getUrl(), payload, hashMap);

        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create tenant failed with response = "+ response.prettyPrint());

        return dataLoader.getTestParameter().get("tenantName") + timeStampNow;

    }

    public Response createTenant(DataLoader dataLoader) {

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        String payload = dataPool.getJsonSchemaPool().get("registerTenant.json")
                .replace("{tenantname}", dataLoader.getTestParameter().get("tenantName") + timeStamp)
                .replace("{tenantshortname}", dataLoader.getTestParameter().get("tenantShortName") + timeStamp)
                .replace("{edapvanityurl}", dataLoader.getTestParameter().get("tenantVanityURL")+ timeStamp +".com:9393")
                .replace("{openamurl}", dataLoader.getTestParameter().get("OpenAMURL"))
                .replace("{wfdurl}", dataLoader.getTestParameter().get("WFDURL").replace("{addUniqueChar}",timeStamp))
                .replace("{environment}", dataLoader.getTestParameter().get("environment"))
                .replace("{solution}", dataLoader.getTestParameter().get("solution"))
                .replace("{seeduser}", dataLoader.getTestParameter().get("tenantSeedUser"))
                .replace("{ssoenabled}", dataLoader.getTestParameter().get("isSSOEnabled"))
                .replace("{clientid}", dataLoader.getTestParameter().get("ClientId"))
                .replace("{clientsecret}", dataLoader.getTestParameter().get("ClientSecret"))
                .replace("{createdBy}", dataLoader.getTestParameter().get("createdBy"));
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataLoader.getDhssEnvBaseUrl() + dataLoader.getUrl(), payload, hashMap);

        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create tenant failed with response = "+ response.prettyPrint());

        dataPool.setTenantNameUser(dataLoader.getTestParameter().get("tenantName") + timeStamp);
        dataPool.setTenantShortName(dataLoader.getTestParameter().get("tenantShortName") + timeStamp);
        return response;

    }

    public Response updateTenant(DataLoader dataLoader){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        String payload = dataPool.getJsonSchemaPool().get("updateTenant.json")
                .replace("{tenantname}", dataPool.getTenantNameUser())
                .replace("{tenantshortname}",dataPool.getTenantShortName())
                .replace("{edapvanityurl}", dataLoader.getTestParameter().get("tenantVanityURL")+ helper.getDateTimeStamp() +".com:9393")
                .replace("{openamurl}", dataLoader.getTestParameter().get("OpenAMURL"))
                .replace("{wfdurl}", dataLoader.getTestParameter().get("WFDURL").replace("{addUniqueChar}",helper.getDateTimeStamp()))
                .replace("{environment}", dataLoader.getTestParameter().get("environment"))
                .replace("{solution}", dataLoader.getTestParameter().get("solution"))
                .replace("{seeduser}", dataLoader.getTestParameter().get("tenantSeedUser"))
                .replace("{ssoenabled}", dataLoader.getTestParameter().get("isSSOEnabled"))
                .replace("{clientid}", dataLoader.getTestParameter().get("ClientId"))
                .replace("{clientsecret}", dataLoader.getTestParameter().get("ClientSecret"))
                .replace("{updatedBy}", dataLoader.getTestParameter().get("updatedBy"));
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(),payload,hashMap);

        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create tenant failed with response = "+ response.prettyPrint());
        dataPool.setTenantNameUser(dataLoader.getTestParameter().get("tenantName") + timeStamp);
        dataPool.setTenantShortName(dataLoader.getTestParameter().get("tenantShortName") + timeStamp);

        return response;

    }

    public Response updateTenant(String TenantType, String vanityUrl){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        String payload = dataPool.getJsonSchemaPool().get("updateTenant.json")
                .replace("{tenantname}", configParser.inputParam.get(TenantType+".tenantName"))
                .replace("{tenantshortname}",configParser.inputParam.get(TenantType+".tenantName"))
                .replace("{edapvanityurl}", vanityUrl)
                .replace("{openamurl}", configParser.inputParam.get(TenantType+".OpenAMURL"))
                .replace("{wfdurl}", configParser.inputParam.get(TenantType+".WFDURL"))
                .replace("{environment}", configParser.inputParam.get(TenantType+".environment"))
                .replace("{solution}", configParser.inputParam.get(TenantType+".solution"))
                .replace("{seeduser}", configParser.inputParam.get(TenantType+".tenantSeedUser"))
                .replace("{ssoenabled}", configParser.inputParam.get(TenantType+".isSSOEnabled"))
                .replace("{clientid}", configParser.inputParam.get(TenantType+".ClientId"))
                .replace("{clientsecret}", configParser.inputParam.get(TenantType+".ClientSecret"))
                .replace("{updatedBy}", configParser.inputParam.get(TenantType+".updatedBy"));

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader("POST",dataPool.getDhssEnvBaseUrl()+"admin/tenant/provisioning/update",payload,hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Update tenant failed with response = "+ response.prettyPrint());

        return response;

    }


    public Response saveSeedUser(DataLoader dataLoader){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());


        String payload = dataPool.getJsonSchemaPool().get("saveSeedUser.json")
                .replace("{appkey}",dataLoader.getTestParameter().get("appkey"))
                .replace("{username}",dataLoader.getTestParameter().get("username"))
                .replace("{password}",dataLoader.getTestParameter().get("password"))
                .replace("{createdBy}",dataLoader.getTestParameter().get("createdBy"));
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(),payload,hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Save seed user failed with response = "+ response.prettyPrint());


        return response;

    }

    public Response saveSeedUser(DataLoader dataLoader,String tenantType){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());


        String payload = dataPool.getJsonSchemaPool().get("saveSeedUser.json")
                .replace("{appkey}",configParser.inputParam.get(tenantType+".appkey"))
                .replace("{username}",configParser.inputParam.get(tenantType+".tenantSeedUser"))
                .replace("{password}",configParser.inputParam.get(tenantType+".password"))
                .replace("{createdBy}",configParser.inputParam.get(tenantType+".tenantSeedUser"));
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(),payload,hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Save seed user failed with response = "+ response.prettyPrint());


        return response;

    }

    public Response updateSeedUser(DataLoader dataLoader){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        String payload = dataPool.getJsonSchemaPool().get("saveSeedUser.json")
                .replace("{appkey}",dataLoader.getTestParameter().get("appkey"))
                .replace("{username}",dataLoader.getTestParameter().get("username"))
                .replace("{password}",dataLoader.getTestParameter().get("password"))
                .replace("{createdby}",dataLoader.getTestParameter().get("createdBy"));
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(),payload,hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Update seed user failed with response = "+ response.prettyPrint());

        return response;

    }

    public Response addAdditionalProperty(DataLoader dataLoader, String tenantShortName){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        AdditionalPropertDto additionalPropertDto = new AdditionalPropertDto(
                dataLoader.getTestParameter().get("edapTenantId"),
                dataLoader.getTestParameter().get("tenantName"),
                dataLoader.getTestParameter().get("isViewPointTenant"),
                dataLoader.getTestParameter().get("isUkgProCustomer"),
                dataLoader.getTestParameter().get("isLicensedCustomer"),
                dataLoader.getTestParameter().get("isGcpUkgOwned"),
                dataLoader.getTestParameter().get("isScrubbed"),
                dataLoader.getTestParameter().get("performanceTier"),
                dataLoader.getTestParameter().get("timeZone"),
                dataLoader.getTestParameter().get("gcpProjectId"),
                tenantShortName
        );

        String payload = new Gson().toJson(additionalPropertDto);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),
                dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(),
                payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Addtional property save failed with response = "+ response.prettyPrint());
        return response;
    }

    /**
     * Delete a tenant from the system
     * @param tenantShortName
     */
    public void deleteTenant(String tenantShortName){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        String payload = dataPool.getJsonSchemaPool().get("deleteTenant.json")
                .replace("{TransactionType}","deactivate")
                .replace("{TenantShortName}",tenantShortName);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader("POST",
                dataPool.getDhssEnvBaseUrl()+"admin/tenant/provisioning/deactivate",
                payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Tenant Deactivate failed with following response = "+ response.prettyPrint());


        String payloadDelete = dataPool.getJsonSchemaPool().get("deleteTenant.json")
                .replace("{TransactionType}","delete")
                .replace("{TenantShortName}",tenantShortName);
        Response responseDelete = helper.sendApiRequestRelaxedHttpsCustomHeader("POST",
                dataPool.getDhssEnvBaseUrl()+"admin/tenant/provisioning/delete",
                payloadDelete, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Tenant Delete failed with following response = "+ response.prettyPrint());


        String payloadCleanUp = dataPool.getJsonSchemaPool().get("deleteTenant.json")
                .replace("{TransactionType}","cleanup")
                .replace("{TenantShortName}",tenantShortName);
        Response responseCleanup = helper.sendApiRequestRelaxedHttpsCustomHeader("POST",
                dataPool.getDhssEnvBaseUrl()+"admin/tenant/provisioning/cleanup",
                payloadCleanUp, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Tenant cleanup failed with following response = "+ response.prettyPrint());

    }

    public Response createServiceKeyFile(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        serviceKeyFiledto.setTenantId(tenantId);
        serviceKeyFiledto.setServiceAccountKeyType(dataLoader.getTestParameter().get("serviceAccountKeyType"));

        String payload = new Gson().toJson(serviceKeyFiledto);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl(), payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response createServiceKeyFileForInvalidTenant(DataLoader dataLoader){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        serviceKeyFiledto.setTenantId(dataLoader.getTestParameter().get("tenantId"));
        serviceKeyFiledto.setServiceAccountKeyType(dataLoader.getTestParameter().get("serviceAccountKeyType"));

        String payload = new Gson().toJson(serviceKeyFiledto);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl(), payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response createServiceKeyFileForInvalidServiceType(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        serviceKeyFiledto.setTenantId(tenantId);
        serviceKeyFiledto.setServiceAccountKeyType(dataLoader.getTestParameter().get("serviceAccountKeyType"));

        String payload = new Gson().toJson(serviceKeyFiledto);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl(), payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getServiceKeyFile(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() + tenantId, null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getServiceKeyFileForInvalidTenant(DataLoader dataLoader){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() + dataLoader.getTestParameter().get("tenantId"), null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFile(DataLoader dataLoader, String tenantId, String serviceAccountKeyId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() +"/"+tenantId+"/"+serviceAccountKeyId, null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }


    public Response deleteServiceKeyFileForInvalidTenantAndServiceKey(DataLoader dataLoader){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        String tenantId = dataLoader.getTestParameter().get("tenantId");
        String serviceAccountKeyId = dataLoader.getTestParameter().get("serviceAccountKeyId");

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() +"/"+tenantId+"/"+serviceAccountKeyId, null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFileForInvalidTenant(DataLoader dataLoader, String serviceAccountKeyId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        String tenantId = dataLoader.getTestParameter().get("tenantId");

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() +"/"+tenantId+"/"+serviceAccountKeyId, null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFileForInvalidServiceKey(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        String serviceAccountKeyId = dataLoader.getTestParameter().get("serviceAccountKeyId");

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() +"/"+tenantId+"/"+serviceAccountKeyId, null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }

    public void activateUkgOwnedTenant(){
        updateTenant("customerOwnedTenant","https://goog"+helper.getDateTimeStamp()+".com");
        updateTenant("ukgOwnedTenant",System.getenv("vanityUrl"));

        JSONObject testData = (JSONObject) dataPool.getTestDataPool().get("DHSSAdditionalPropertyApiServicesTest.updateAdditionalPropertyTest").get(0);
        DataLoader dataLoader = new DataLoader(testData);


    }

    public void activateCustomerOwnedTenant(){
        updateTenant("ukgOwnedTenant","https://goog"+helper.getDateTimeStamp()+".com");
        updateTenant("customerOwnedTenant",System.getenv("vanityUrl"));

        JSONObject testData = (JSONObject) dataPool.getTestDataPool().get("DHSSAdditionalPropertyApiServicesTest.updateAdditionalPropertyTest").get(1);
        DataLoader dataLoader = new DataLoader(testData);

    }
    public Response provisionCustomerOwnedGcp(DataLoader dataLoader){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        customerOwnedGcpDto.setTenantId(dataLoader.getTestParameter().get("tenantId"));
        customerOwnedGcpDto.setGcpProjectId(dataLoader.getTestParameter().get("gcpProjectId"));
        customerOwnedGcpDto.setGcpProjectDesc(dataLoader.getTestParameter().get("gcpProjectDesc"));
        customerOwnedGcpDto.setServiceAccountKey(dataLoader.getTestParameter().get("serviceAccountKey"));

        String payload = new Gson().toJson(customerOwnedGcpDto);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),
                dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(),
                payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Provision customer owned gcp failed with response = "+ response.prettyPrint());
        return response;
    }

    public Response updateProvisionedCustomerOwnedGcp(DataLoader dataLoader, CustomerOwnedGcpDto customerOwnedGcpDto){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        customerOwnedGcpDto.setServiceAccountKey(dataLoader.getTestParameter().get("serviceAccountKey"));

        String payload = new Gson().toJson(customerOwnedGcpDto);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(),
                dataPool.getDhssWebEnvBaseUrl()+dataLoader.getUrl(),
                payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Provision customer owned gcp failed with response = "+ response.prettyPrint());
        return response;
    }
    public Response getCustomerOwnedGcpAddProp(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        //Todo add the actual values from dto class

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() + tenantId, null, hashMap);
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get customer ownd gcp properties failed with response = "+response.prettyPrint());

        return response;
    }

    public Response getTenantVersionSeedAddProp(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getSeedlogindto().getAccess_token());
        hashMap.put("X-XSRF-TOKEN", dataPool.getSeedlogindto().getXSRF_Token());

        //Todo add the actual values from dto class

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssWebEnvBaseUrl() + dataLoader.getUrl() + tenantId, null, hashMap);
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get customer ownd gcp properties failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getTenantVersionTMSAdminAddProp(DataLoader dataLoader, String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), dataPool.getDhssEnvBaseUrl() + dataLoader.getUrl() + tenantId, null, hashMap);
        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get tenant version on TMS additional properties page failed with response = "+response.prettyPrint());
        return response;
    }
}

