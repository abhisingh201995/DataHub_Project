package com.FunctionalUtils;

import com.DataObjects.DhssDataObject.CustomerOwnedGcpDto;
import com.DataObjects.DhssDataObject.PipelineSettingsDTO;
import com.DataObjects.DhssDataObject.ServiceKeyFiledto;
import com.DataObjects.DhssDataObject.TenantPipelineSettingsDTO;
import com.Utilities.ConfigParser;
import com.Utilities.DataLoader;
import com.Utilities.DataPool;
import com.Utilities.Helper;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.HashMap;

public class DHSSPipelineSettingsHelper {

    DataPool dataPool = DataPool.getDataPool();
    ConfigParser configParser = ConfigParser.getConfigParser();
    Helper helper = new Helper();
    PipelineSettingsDTO pipelineSettingsDTO = new PipelineSettingsDTO();
    TenantPipelineSettingsDTO tenantPipelineSettingsDTO = new TenantPipelineSettingsDTO();

    public Response getPipelineSettingsForValidTenantId(String tenantId){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());
        JSONObject data =(JSONObject) dataPool.getTestDataPool().get("PipelineSettingsTests.dhssGetlPipelineSettingsForValidTenantId").get(0);
        DataLoader dataLoader = new DataLoader(data);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), configParser.inputParam.get("ukgOwnedTenant.baseUrlWebDhss") + dataLoader.getUrl() + tenantId, null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get pipeline settings failed with response = "+response.prettyPrint());

        return response;

    }

    public Response getPipelineSettingsForInvalidTenantId(){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());
        JSONObject data =(JSONObject) dataPool.getTestDataPool().get("PipelineSettingsTests.dhssGetlPipelineSettingsForValidTenantId").get(0);
        DataLoader dataLoader = new DataLoader(data);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), configParser.inputParam.get("ukgOwnedTenant.baseUrlWebDhss") + dataLoader.getUrl() + dataLoader.getTestParameter().get("tenantName"), null, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Get pipeline settings failed with response = "+response.prettyPrint());

        return response;

    }

    /**
     *
     * @param pipelineSetting
     * @return
     */
 public Response updatePipelineSettingsForValidTenantId(HashMap<String, String> pipelineSetting){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization", dataPool.getAdminlogindto().getAdminSessionId());
        hashMap.put("X-XSRF-TOKEN", dataPool.getAdminlogindto().getXSRF_Token());

        JSONObject data =(JSONObject) dataPool.getTestDataPool().get("PipelineSettingsTests.dhssUpdateAnyPipelineSettingForValidTenantId").get(0);
        DataLoader dataLoader = new DataLoader(data);

        tenantPipelineSettingsDTO.setPipelineName(pipelineSetting.get("tenantName"));
        tenantPipelineSettingsDTO.setSettingName(pipelineSetting.get("settingName"));
        tenantPipelineSettingsDTO.setSettingType(pipelineSetting.get("settingType"));
        tenantPipelineSettingsDTO.setSettingValue(pipelineSetting.get("settingValue"));

        String payload = new Gson().toJson(tenantPipelineSettingsDTO);
        Response response = helper.sendApiRequestRelaxedHttpsCustomHeader(dataLoader.getMethod(), configParser.inputParam.get("ukgOwnedTenant.baseUrlWebDhss") + dataLoader.getUrl(), payload, hashMap);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Update pipeline settings failed with response = "+response.prettyPrint());

        return response;

    }
}
