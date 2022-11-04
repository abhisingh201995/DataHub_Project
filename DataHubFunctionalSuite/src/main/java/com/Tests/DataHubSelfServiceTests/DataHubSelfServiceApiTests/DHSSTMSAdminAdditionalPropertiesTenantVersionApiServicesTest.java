package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.FunctionalUtils.DHSSTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Test(groups = "DHSS_TMSAdmin_TenantVersion_Tests")
public class DHSSTMSAdminAdditionalPropertiesTenantVersionApiServicesTest extends BaseConfiguration {
    DHSSTenantHelper dhssTenantHelper = new DHSSTenantHelper();

    @BeforeClass
    public void  loginAdminUser(){
        dhssTenantHelper.refreshAuthToken();
    }

    @Test(dataProvider = "userTestDataProvider")
    public void getTenantVersionTMSAdminAdditionalProperties(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.getTenantVersionTMSAdminAddProp(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));
        response.prettyPrint();
        //Verify body
        softAssert.assertNotNull(response.jsonPath().getString("edapTenantId"),"EDAP Tenant ID not found");
        //Response Output is in array of array.So we are checking with contains.
        softAssert.assertTrue(response.jsonPath().getString("tenantName").contains(configParser.inputParam.get("ukgOwnedTenant.tenantName")),"Tenant name not found");
        softAssert.assertTrue(response.jsonPath().getString("shortName").contains (configParser.inputParam.get("ukgOwnedTenant.tenantName")),"Tenant Short name not found");
        softAssert.assertNotNull(response.jsonPath().getString("tenantVersion"),"EDAP Tenant Version not found");

        softAssert.assertAll();
    }
}
