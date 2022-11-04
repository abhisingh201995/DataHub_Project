package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.FunctionalUtils.DHSSPipelineSettingsHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;

public class PipelineSettingsTests extends BaseConfiguration{

	DHSSPipelineSettingsHelper dHSSPipelineSettingsHelper = new DHSSPipelineSettingsHelper();

	@BeforeClass
	public void  loginSeedUser(){
		dhssTenantHelper.refreshSeedAuthToken("ukgOwnedTenant");
	}

	@Test(dataProvider = "userTestDataProvider")
	public void dhssGetPipelineSettingsForValidTenantId(final JSONObject testData){

		SoftAssert softAssert = new SoftAssert();
		DataLoader dataLoader = new DataLoader(testData);

		Response response = dHSSPipelineSettingsHelper.getPipelineSettingsForValidTenantId(configParser.inputParam.get("ukgOwnedTenant.tenantName"));

	}

	@Test(dataProvider = "userTestDataProvider")
	public void dhssGetPipelineSettingsForInvalidTenantId(final JSONObject testData){

		SoftAssert softAssert = new SoftAssert();
		DataLoader dataLoader = new DataLoader(testData);

		Response response = dHSSPipelineSettingsHelper.getPipelineSettingsForInvalidTenantId();

	}

	@Test(dataProvider = "userTestDataProvider")
	public void dhssUpdateAnyPipelineSettingForValidTenantId(final JSONObject testData){

		SoftAssert softAssert = new SoftAssert();
		DataLoader dataLoader = new DataLoader(testData);
		HashMap<String, String> payload = new HashMap<>();
		payload.put("tenantName",dataLoader.getTestParameter().get("tenantName"));
		payload.put("settingName",dataLoader.getTestParameter().get("settingName"));
		payload.put("settingType",dataLoader.getTestParameter().get("settingType"));
		payload.put("settingValue",dataLoader.getTestParameter().get("settingValue"));

		Response response = dHSSPipelineSettingsHelper.updatePipelineSettingsForValidTenantId(payload);

	}
}
