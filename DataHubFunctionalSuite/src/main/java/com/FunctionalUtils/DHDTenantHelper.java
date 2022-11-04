package com.FunctionalUtils;

import com.DataObjects.DhssDataObject.DhssTenantInfoDto;
import com.DataObjects.DhssDataObject.ProvisionGcpDhdDto;
import com.DataObjects.DhssDataObject.ServiceKeyFiledto;
import com.Utilities.AccessTokenType;
import com.Utilities.DataLoader;
import com.Utilities.DataPool;
import com.Utilities.Helper;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DHDTenantHelper {
    private Response response;
    DataPool dataPool = DataPool.getDataPool();
    Helper helper = new Helper();
    DhssTenantInfoDto dhssTenantInfoDto = new DhssTenantInfoDto();
    ServiceKeyFiledto serviceKeyFiledto = new ServiceKeyFiledto();

    String timeStamp = helper.getDateTimeStamp();
    DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

    public Response createDhdTenant(DataLoader dataLoader) {

        dhssTenantInfoDto.setAppKey(dataLoader.getTestParameter().get("appKey"));
        dhssTenantInfoDto.setClientId(dataLoader.getTestParameter().get("clientId"));
        dhssTenantInfoDto.setClientSecret(dataLoader.getTestParameter().get("clientSecret"));
        dhssTenantInfoDto.setCreatedBy(dataLoader.getTestParameter().get("createdBy"));
        try{
            dhssTenantInfoDto.setCreatedDate((Date)format.parse(dataLoader.getTestParameter().get("createdDate")));
            dhssTenantInfoDto.setUpdatedDate(null);

        }catch (Exception e){
            System.out.println(e);
        }


        dhssTenantInfoDto.setDescription("");
        dhssTenantInfoDto.setEdapTenantId(Long.parseLong(dataLoader.getTestParameter().get("edapTenantId")));
        dhssTenantInfoDto.setEnvironment(dataLoader.getTestParameter().get("environment"));
        dhssTenantInfoDto.setFederatedTenantVanityURL(null);
        dhssTenantInfoDto.setIsActive(Boolean.parseBoolean(dataLoader.getTestParameter().get("isActive")));
        dhssTenantInfoDto.setIsDeleted(Boolean.parseBoolean(dataLoader.getTestParameter().get("isDeleted")));
        dhssTenantInfoDto.setIsGcpUkgOwned(dataLoader.getTestParameter().get("isGcpUkgOwned"));
        dhssTenantInfoDto.setIsLicensedCustomer(dataLoader.getTestParameter().get("isLicensedCustomer"));
        dhssTenantInfoDto.setIsScrubbed(dataLoader.getTestParameter().get("isScrubbed"));
        dhssTenantInfoDto.setIsSSOEnabled(Boolean.parseBoolean(dataLoader.getTestParameter().get("isSSOEnabled")));
        dhssTenantInfoDto.setIsUkgProCustomer(dataLoader.getTestParameter().get("isUkgProCustomer"));
        dhssTenantInfoDto.setIsViewPointTenant(dataLoader.getTestParameter().get("isViewPointTenant"));
        dhssTenantInfoDto.setLocalePolicy(dataLoader.getTestParameter().get("localePolicy"));
        dhssTenantInfoDto.setGcpProjectId(dataLoader.getTestParameter().get("gcpProjectId"));
        dhssTenantInfoDto.setOpenAMURL(dataLoader.getTestParameter().get("openAMURL"));
        dhssTenantInfoDto.setPerformanceTier(dataLoader.getTestParameter().get("performanceTier"));
        dhssTenantInfoDto.setShortName(dataLoader.getTestParameter().get("shortName"));
        dhssTenantInfoDto.setSolution(dataLoader.getTestParameter().get("solution"));
        dhssTenantInfoDto.setTenantName(dataLoader.getTestParameter().get("tenantName")+timeStamp);
        dhssTenantInfoDto.setTenantSeedUser(dataLoader.getTestParameter().get("tenantSeedUser"));
        dhssTenantInfoDto.setWfdUsername(dataLoader.getTestParameter().get("wfdUsername"));
        dhssTenantInfoDto.setTenantSSOUrl(null);
        dhssTenantInfoDto.setTenantVanityURL(dataLoader.getTestParameter().get("tenantVanityURL")+ helper.getDateTimeStamp() +".com:9393");
        dhssTenantInfoDto.setTimeZone(dataLoader.getTestParameter().get("timeZone"));
        dhssTenantInfoDto.setUpdatedBy(dataLoader.getTestParameter().get("updatedBy"));
        dhssTenantInfoDto.setWfdPwd(dataLoader.getTestParameter().get("wfdPwd"));
        dhssTenantInfoDto.setWfdURL(dataLoader.getTestParameter().get("wfdURL").replace("{addUniqueChar}",timeStamp));


        String payload = new Gson().toJson(dhssTenantInfoDto);

        response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl(), payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create tenant failed with response = "+response.prettyPrint());


        dataPool.setTenantsDataHubDirector(dataLoader.getTestParameter().get("tenantName")+timeStamp);

        return response;

    }

    public Response updateDhdTenant(DataLoader dataLoader) {

        dhssTenantInfoDto.setAppKey(dataLoader.getTestParameter().get("appKey"));
        dhssTenantInfoDto.setClientId(dataLoader.getTestParameter().get("clientId"));
        dhssTenantInfoDto.setClientSecret(dataLoader.getTestParameter().get("clientSecret"));
        dhssTenantInfoDto.setCreatedBy(dataLoader.getTestParameter().get("createdBy"));
        try{
            dhssTenantInfoDto.setCreatedDate((Date)format.parse(dataLoader.getTestParameter().get("createdDate")));
            dhssTenantInfoDto.setUpdatedDate(null);

        }catch (Exception e){
            System.out.println(e);
        }


        dhssTenantInfoDto.setDescription("");
        dhssTenantInfoDto.setEdapTenantId(Long.parseLong(dataLoader.getTestParameter().get("edapTenantId")));
        dhssTenantInfoDto.setEnvironment(dataLoader.getTestParameter().get("environment"));
        dhssTenantInfoDto.setFederatedTenantVanityURL(null);
        dhssTenantInfoDto.setIsActive(Boolean.parseBoolean(dataLoader.getTestParameter().get("isActive")));
        dhssTenantInfoDto.setIsDeleted(Boolean.parseBoolean(dataLoader.getTestParameter().get("isDeleted")));
        dhssTenantInfoDto.setIsGcpUkgOwned(dataLoader.getTestParameter().get("isGcpUkgOwned"));
        dhssTenantInfoDto.setIsLicensedCustomer(dataLoader.getTestParameter().get("isLicensedCustomer"));
        dhssTenantInfoDto.setIsScrubbed(dataLoader.getTestParameter().get("isScrubbed"));
        dhssTenantInfoDto.setIsSSOEnabled(Boolean.parseBoolean(dataLoader.getTestParameter().get("isSSOEnabled")));
        dhssTenantInfoDto.setIsUkgProCustomer(dataLoader.getTestParameter().get("isUkgProCustomer"));
        dhssTenantInfoDto.setIsViewPointTenant(dataLoader.getTestParameter().get("isViewPointTenant"));
        dhssTenantInfoDto.setLocalePolicy(dataLoader.getTestParameter().get("localePolicy"));
        dhssTenantInfoDto.setGcpProjectId(dataLoader.getTestParameter().get("gcpProjectId"));
        dhssTenantInfoDto.setOpenAMURL(dataLoader.getTestParameter().get("openAMURL"));
        dhssTenantInfoDto.setPerformanceTier(dataLoader.getTestParameter().get("performanceTier"));
        dhssTenantInfoDto.setShortName(dataLoader.getTestParameter().get("shortName"));
        dhssTenantInfoDto.setSolution(dataLoader.getTestParameter().get("solution"));
        dhssTenantInfoDto.setTenantName(dataPool.getTenantsDataHubDirector());
        dhssTenantInfoDto.setTenantSeedUser(dataLoader.getTestParameter().get("tenantSeedUser"));
        dhssTenantInfoDto.setWfdUsername(dataLoader.getTestParameter().get("wfdUsername"));
        dhssTenantInfoDto.setTenantSSOUrl(null);
        dhssTenantInfoDto.setTenantVanityURL(dataLoader.getTestParameter().get("tenantVanityURL")+ helper.getDateTimeStamp() +".com:9393");
        dhssTenantInfoDto.setTimeZone(dataLoader.getTestParameter().get("timeZone"));
        dhssTenantInfoDto.setUpdatedBy(dataLoader.getTestParameter().get("updatedBy"));
        dhssTenantInfoDto.setWfdPwd(dataLoader.getTestParameter().get("wfdPwd"));
        dhssTenantInfoDto.setWfdURL(dataLoader.getTestParameter().get("wfdURL").replace("{addUniqueChar}",timeStamp));


        String payload = new Gson().toJson(dhssTenantInfoDto);

        response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl(), payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create tenant failed with response = "+response.prettyPrint());


        //dataPool.setTenantsDataHubDirector(dataLoader.getTestParameter().get("tenantName")+timeStamp);

        return response;

    }

    public Response getTenantVersionDetail(String tenantName){

        Response response = helper.sendApiRequestRelaxedHttps("GET",
                dataPool.getEnvBaseUrl()+"dhd/admin/tenant/versionDetails/?tenantName="+tenantName,
                null,
                "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Get tenant detail for tenant failed with response="+response.prettyPrint());

        return response;

    }

    public Response getTenantGcpDetail(String tenantName){

        Response response = helper.sendApiRequestRelaxedHttps("GET",
                dataPool.getEnvBaseUrl()+"dhd/admin/tenant/gcpProperties/?tenantName="+tenantName,
                null,
                "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));

        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Get GCP detail for tenant failed with response="+response.prettyPrint());
        return response;

    }

    public Response provisionGcpProjectViaDhd(String tenantName, String gcpProjectId, String gcpProjectName,
                                              String serviceAccountKey){

        String payload= new Gson().toJson(new ProvisionGcpDhdDto(
                tenantName,gcpProjectId,gcpProjectName,serviceAccountKey

        ));

        Response response = helper.sendApiRequestRelaxedHttps("POST",
                dataPool.getEnvBaseUrl()+"dhd/admin/tenant/provisioning/provisionGcp/provision",
                payload,
                "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));

        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Provision GCP project for tenant failed with response="+response.prettyPrint());

        return response;

    }

    public Response updateProvisionGcpProjectViaDhd(String tenantName, String gcpProjectId, String gcpProjectName,
                                                    String serviceAccountKey){

        String payload= new Gson().toJson(new ProvisionGcpDhdDto(
                tenantName,gcpProjectId,gcpProjectName,serviceAccountKey

        ));

        Response response = helper.sendApiRequestRelaxedHttps("PUT",
                dataPool.getEnvBaseUrl()+"dhd/admin/tenant/provisioning/provisionGcp/provision",
                payload,
                "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Update GCP detail for tenant failed with response="+response.prettyPrint());

        return response;
    }

    public void refreshDhdToken(){
        String payload = "{\"username\": \"{username}\",\"password\": \"{password}\"}";
        payload =payload
                .replace("{username}", System.getenv("username"))
                .replace("{password}",System.getenv("password"));

        Response response = helper.sendApiRequestRelaxedHttps("POST", dataPool.getEnvBaseUrl()+"api-token-auth/", payload, null);

        dataPool.setAccessTokenDhd(response.path("token"));

        dataPool.setAccessTokenDhd(
                dataPool.getAllAccessToken()
                        .put(AccessTokenType.Dhd_Access_Token,response.path("token")));

    }

    public Response createServiceKeyFile(DataLoader dataLoader, String tenantName){

        serviceKeyFiledto.setTenantName(tenantName);
        serviceKeyFiledto.setServiceAccountKeyType(dataLoader.getTestParameter().get("serviceAccountKeyType"));

        String payload = new Gson().toJson(serviceKeyFiledto);
        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl(), payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response createServiceKeyFileForInvalidTenant(DataLoader dataLoader){

        serviceKeyFiledto.setTenantName(dataLoader.getTestParameter().get("tenantName"));
        serviceKeyFiledto.setServiceAccountKeyType(dataLoader.getTestParameter().get("serviceAccountKeyType"));

        String payload = new Gson().toJson(serviceKeyFiledto);
        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl(), payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response createServiceKeyFileForInvalidServiceType(DataLoader dataLoader, String tenantName){

        serviceKeyFiledto.setTenantName(tenantName);
        serviceKeyFiledto.setServiceAccountKeyType(dataLoader.getTestParameter().get("serviceAccountKeyType"));

        String payload = new Gson().toJson(serviceKeyFiledto);
        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl(), payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Create service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getServiceKeyFile(DataLoader dataLoader, String tenantName){

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl() + tenantName , null, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getServiceKeyFileForInvalidTenant(DataLoader dataLoader){

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl() + dataLoader.getTestParameter().get("tenantName") , null, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFile(DataLoader dataLoader, String tenantName, String serviceAccountKeyId ){

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl()+ tenantName +"&serviceAccountKeyId="+serviceAccountKeyId, null, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFileForInvalidTenantAndServiceKey(DataLoader dataLoader ){

        String TenantName = dataLoader.getTestParameter().get("tenantName");
        String ServiceAccountKeyId = dataLoader.getTestParameter().get("serviceAccountKeyId");

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl()+ TenantName +"&serviceAccountKeyId="+ServiceAccountKeyId, null, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFileForInvalidTenant(DataLoader dataLoader, String serviceAccountKeyId ){

        String TenantName = dataLoader.getTestParameter().get("tenantName");

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl()+ TenantName +"&serviceAccountKeyId="+serviceAccountKeyId, null, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }

    public Response deleteServiceKeyFileForInvalidServiceKey(DataLoader dataLoader, String tenantName ){

        String ServiceAccountKeyId = dataLoader.getTestParameter().get("serviceAccountKeyId");

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getEnvBaseUrl() + dataLoader.getUrl()+ tenantName +"&serviceAccountKeyId="+ServiceAccountKeyId, null, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Delete service key failed with response = "+response.prettyPrint());

        return response;

    }
}