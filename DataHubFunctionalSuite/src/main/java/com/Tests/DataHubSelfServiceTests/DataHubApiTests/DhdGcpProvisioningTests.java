package com.Tests.DataHubSelfServiceTests.DataHubApiTests;

import com.FunctionalUtils.DHDTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Test(groups = "Dhd_Gcp_Provision_Test")
public class DhdGcpProvisioningTests extends BaseConfiguration {

    String gcpProjectId;
    String gcpProjectName;
    String serviceAccountKey;


    @Test(dataProvider="userTestDataProvider")
    public void dhdGetTenantDetailTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();
        dhdTenantHelper.getTenantGcpDetail(dataPool.getTenantsDataHubDirector());
    }

    @Test(dataProvider="userTestDataProvider")
    public void dhdGetTenantVersionTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();
        Response response = dhdTenantHelper.getTenantVersionDetail(dataPool.getTenantsDataHubDirector());
        gcpProjectId= response.getBody().path("gcpProjectId");
        gcpProjectName=response.getBody().path("gcpProjectName");
    }

    @Test(dependsOnMethods = "dhdGetTenantVersionTest", dataProvider="userTestDataProvider")
    public void dhdProvisionGcpTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();
        serviceAccountKey = "Key"+helper.getDateTimeStamp();
        gcpProjectName="TestKey"+helper.getDateTimeStamp();
        dhdTenantHelper.provisionGcpProjectViaDhd(dataPool.getTenantsDataHubDirector(),gcpProjectId,gcpProjectName,serviceAccountKey);
    }

    @Test(dependsOnMethods = "dhdProvisionGcpTest", dataProvider="userTestDataProvider")
    public void dhdUpdateProvisionGcpTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();
        serviceAccountKey = "Key"+helper.getDateTimeStamp();
        gcpProjectName="TestKeyUpdated"+helper.getDateTimeStamp();
        dhdTenantHelper.updateProvisionGcpProjectViaDhd(dataPool.getTenantsDataHubDirector(),gcpProjectId,gcpProjectName,serviceAccountKey);
    }
}
