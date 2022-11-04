package com.FunctionalUtils;

import com.DataObjects.DhssDataObject.DhssTenantInfoDto;
import com.DataObjects.DhssDataObject.ProvisionGcpDhdDto;
import com.Utilities.AccessTokenType;
import com.Utilities.DataLoader;
import com.Utilities.DataPool;
import com.Utilities.Helper;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DHDApiTenantHelper {

    public DHDApiTenantHelper(String projectId) {
        this.projectId=projectId;
         this.env = projectId.split("-")[2]+projectId.split("-")[3];
    }

    String projectId;
    private Response response;
    DataPool dataPool = DataPool.getDataPool();
    Helper helper = new Helper(projectId);
    DhssTenantInfoDto dhssTenantInfoDto = new DhssTenantInfoDto();

    String timeStamp = helper.getDateTimeStamp();
    DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

    String env;

    public Response createDhdTenant(HashMap<String,String> dataLoader) {

        dhssTenantInfoDto.setAppKey(dataLoader.get("appKey"));
        dhssTenantInfoDto.setClientId(dataLoader.get("clientId"));
        dhssTenantInfoDto.setClientSecret(dataLoader.get("clientSecret"));
        dhssTenantInfoDto.setCreatedBy(dataLoader.get("createdBy"));
        try{
            dhssTenantInfoDto.setCreatedDate((Date)format.parse(dataLoader.get("createdDate")));
            dhssTenantInfoDto.setUpdatedDate(null);

        }catch (Exception e){
            System.out.println(e);
        }


        dhssTenantInfoDto.setDescription("");
        dhssTenantInfoDto.setEdapTenantId(Long.parseLong(dataLoader.get("edapTenantId")));
        dhssTenantInfoDto.setEnvironment(dataLoader.get("environment"));
        dhssTenantInfoDto.setFederatedTenantVanityURL(null);
        dhssTenantInfoDto.setIsActive(Boolean.parseBoolean(dataLoader.get("isActive")));
        dhssTenantInfoDto.setIsDeleted(Boolean.parseBoolean(dataLoader.get("isDeleted")));
        dhssTenantInfoDto.setIsGcpUkgOwned(dataLoader.get("isGcpUkgOwned"));
        dhssTenantInfoDto.setIsLicensedCustomer(dataLoader.get("isLicensedCustomer"));
        dhssTenantInfoDto.setIsScrubbed(dataLoader.get("isScrubbed"));
        dhssTenantInfoDto.setIsSSOEnabled(Boolean.parseBoolean(dataLoader.get("isSSOEnabled")));
        dhssTenantInfoDto.setIsUkgProCustomer(dataLoader.get("isUkgProCustomer"));
        dhssTenantInfoDto.setIsViewPointTenant(dataLoader.get("isViewPointTenant"));
        dhssTenantInfoDto.setLocalePolicy(dataLoader.get("localePolicy"));
        dhssTenantInfoDto.setGcpProjectId(dataLoader.get("gcpProjectId"));
        dhssTenantInfoDto.setOpenAMURL(dataLoader.get("openAMURL"));
        dhssTenantInfoDto.setPerformanceTier(dataLoader.get("performanceTier"));
        dhssTenantInfoDto.setShortName(dataLoader.get("shortName"));
        dhssTenantInfoDto.setSolution(dataLoader.get("solution"));
        dhssTenantInfoDto.setTenantName(dataLoader.get("tenantName")+timeStamp);
        dhssTenantInfoDto.setTenantSeedUser(dataLoader.get("tenantSeedUser"));
        dhssTenantInfoDto.setWfdUsername(dataLoader.get("wfdUsername"));
        dhssTenantInfoDto.setTenantSSOUrl(null);
        dhssTenantInfoDto.setTenantVanityURL(dataLoader.get("tenantVanityURL")+ helper.getDateTimeStamp() +".com:9393");
        dhssTenantInfoDto.setTimeZone(dataLoader.get("timeZone"));
        dhssTenantInfoDto.setUpdatedBy(dataLoader.get("updatedBy"));
        dhssTenantInfoDto.setWfdPwd(dataLoader.get("wfdPwd"));
        dhssTenantInfoDto.setWfdURL(dataLoader.get("wfdURL").replace("{addUniqueChar}",timeStamp));


        String payload = new Gson().toJson(dhssTenantInfoDto);

        response = helper.sendApiRequestRelaxedHttps("POST", dataPool.getDhdApiBaseUrl(env) + "dhd/admin/tenant/provisioning/create", payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Create tenant failed with response = "+response.prettyPrint());


        dataPool.setTenantsDataHubDirector(dataLoader.get("tenantName")+timeStamp);

        return response;

    }

    public Response updateDhdTenant(HashMap<String,String> dataLoader) {

        dhssTenantInfoDto.setAppKey(dataLoader.get("appKey"));
        dhssTenantInfoDto.setClientId(dataLoader.get("clientId"));
        dhssTenantInfoDto.setClientSecret(dataLoader.get("clientSecret"));
        dhssTenantInfoDto.setCreatedBy(dataLoader.get("createdBy"));
        try{
            dhssTenantInfoDto.setCreatedDate((Date)format.parse(dataLoader.get("createdDate")));
            dhssTenantInfoDto.setUpdatedDate(null);

        }catch (Exception e){
            System.out.println(e);
        }


        dhssTenantInfoDto.setDescription("");
        dhssTenantInfoDto.setEdapTenantId(Long.parseLong(dataLoader.get("edapTenantId")));
        dhssTenantInfoDto.setEnvironment(dataLoader.get("environment"));
        dhssTenantInfoDto.setFederatedTenantVanityURL(null);
        dhssTenantInfoDto.setIsActive(Boolean.parseBoolean(dataLoader.get("isActive")));
        dhssTenantInfoDto.setIsDeleted(Boolean.parseBoolean(dataLoader.get("isDeleted")));
        dhssTenantInfoDto.setIsGcpUkgOwned(dataLoader.get("isGcpUkgOwned"));
        dhssTenantInfoDto.setIsLicensedCustomer(dataLoader.get("isLicensedCustomer"));
        dhssTenantInfoDto.setIsScrubbed(dataLoader.get("isScrubbed"));
        dhssTenantInfoDto.setIsSSOEnabled(Boolean.parseBoolean(dataLoader.get("isSSOEnabled")));
        dhssTenantInfoDto.setIsUkgProCustomer(dataLoader.get("isUkgProCustomer"));
        dhssTenantInfoDto.setIsViewPointTenant(dataLoader.get("isViewPointTenant"));
        dhssTenantInfoDto.setLocalePolicy(dataLoader.get("localePolicy"));
        dhssTenantInfoDto.setGcpProjectId(dataLoader.get("gcpProjectId"));
        dhssTenantInfoDto.setOpenAMURL(dataLoader.get("openAMURL"));
        dhssTenantInfoDto.setPerformanceTier(dataLoader.get("performanceTier"));
        dhssTenantInfoDto.setShortName(dataLoader.get("shortName"));
        dhssTenantInfoDto.setSolution(dataLoader.get("solution"));
        dhssTenantInfoDto.setTenantName(dataPool.getTenantsDataHubDirector());
        dhssTenantInfoDto.setTenantSeedUser(dataLoader.get("tenantSeedUser"));
        dhssTenantInfoDto.setWfdUsername(dataLoader.get("wfdUsername"));
        dhssTenantInfoDto.setTenantSSOUrl(null);
        dhssTenantInfoDto.setTenantVanityURL(dataLoader.get("tenantVanityURL")+ helper.getDateTimeStamp() +".com:9393");
        dhssTenantInfoDto.setTimeZone(dataLoader.get("timeZone"));
        dhssTenantInfoDto.setUpdatedBy(dataLoader.get("updatedBy"));
        dhssTenantInfoDto.setWfdPwd(dataLoader.get("wfdPwd"));
        dhssTenantInfoDto.setWfdURL(dataLoader.get("wfdURL").replace("{addUniqueChar}",timeStamp));


        String payload = new Gson().toJson(dhssTenantInfoDto);

        response = helper.sendApiRequestRelaxedHttps("PUT" , dataPool.getDhdApiBaseUrl(env) + "dhd/admin/tenant/provisioning/update", payload, "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Create tenant failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getTenantVersionDetail(String tenantName){

        Response response = helper.sendApiRequestRelaxedHttps("GET",
                dataPool.getDhdApiBaseUrl(env)+"dhd/admin/tenant/versionDetails/?tenantName="+tenantName,
                null,
                "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Get tenant detail for tenant failed with response="+response.prettyPrint());

        return response;

    }

    public Response getTenantGcpDetail(String tenantName){

        Response response = helper.sendApiRequestRelaxedHttps("GET",
                dataPool.getDhdApiBaseUrl(env)+"dhd/admin/tenant/gcpProperties/?tenantName="+tenantName,
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
                dataPool.getDhdApiBaseUrl(env)+"dhd/admin/tenant/provisioning/provisionGcp/provision",
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
                dataPool.getDhdApiBaseUrl(env)+"dhd/admin/tenant/provisioning/provisionGcp/provision",
                payload,
                "Bearer "+dataPool.getAllAccessToken().get(AccessTokenType.Dhd_Access_Token));
        Assert.assertEquals(String.valueOf(response.getStatusCode()),"200","Update GCP detail for tenant failed with response="+response.prettyPrint());

        return response;
    }

    public void refreshDhdToken(){
        String payload = "{\"username\": \"{username}\",\"password\": \"{password}\"}";
        payload =payload
                .replace("{username}", dataPool.getDhdUsername())
                .replace("{password}",dataPool.getDhdPassword());

        Response response = helper.sendApiRequestRelaxedHttps("POST", dataPool.getDhdApiBaseUrl(env)+"api-token-auth/", payload, null);

        dataPool.setAccessTokenDhd(
                dataPool.getAllAccessToken()
                        .put(AccessTokenType.Dhd_Access_Token,response.path("token")));


    }
}
