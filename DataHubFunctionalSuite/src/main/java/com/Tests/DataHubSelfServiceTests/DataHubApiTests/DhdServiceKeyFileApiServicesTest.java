package com.Tests.DataHubSelfServiceTests.DataHubApiTests;

import com.FunctionalUtils.DHDTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Test(groups = "DHD_Service_Key_File_Tests")
public class DhdServiceKeyFileApiServicesTest extends BaseConfiguration {
    String privateKeyID;
    DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();

    @BeforeClass
    public void  loginSeedUser(){
        dhdTenantHelper.refreshDhdToken();
    }

    @Test(dataProvider = "userTestDataProvider")
    public void generateDhdServiceKeyFileTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.createServiceKeyFile(dataLoader,configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify private key id
        privateKeyID = response.jsonPath().getString("private_key_id");

        softAssert.assertTrue(gcpHelper.validateGcpCredFile(response.getBody().asString()),"Generated private key ID failed to connect with GCP");

        softAssert.assertAll();
    }

    @Test(dataProvider = "userTestDataProvider")
    public void generateDhdServiceKeyFileInvalidTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.createServiceKeyFileForInvalidTenant(dataLoader);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dataProvider = "userTestDataProvider")
    public void generateDhdServiceKeyFileInvalidServiceTypeTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.createServiceKeyFileForInvalidServiceType(dataLoader,configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "generateDhdServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void getDhdServiceKeyFileInvalidTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.getServiceKeyFileForInvalidTenant(dataLoader);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getDhdServiceKeyFileInvalidTenantTest", dataProvider = "userTestDataProvider")
    public void getDhdServiceKeyFileTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.getServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");
        softAssert.assertEquals(response.jsonPath().getString("tenantName"),configParser.inputParam.get("ukgOwnedTenant.tenantName"));
        softAssert.assertEquals(response.jsonPath().getString("shortName"),configParser.inputParam.get("ukgOwnedTenant.shortName"));
        softAssert.assertEquals(response.jsonPath().getString("gcpProjectId"),configParser.inputParam.get("ukgOwnedTenant.gcpProjectId"));

        //This verification will only work when no service key is downloaded earlier. As this is the tenant we are using for our testing.
        //Hence, can't possible to clean up everything that exists from earlier. So commenting this code.
        //softAssert.assertEquals(response.jsonPath().getString("serviceAccountKeys.key"),privateKeyID);

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getDhdServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void deleteDhdServiceKeyFileInvalidTenantAndServiceKeyTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.deleteServiceKeyFileForInvalidTenantAndServiceKey(dataLoader);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getDhdServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void deleteDhdServiceKeyFileInvalidTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.deleteServiceKeyFileForInvalidTenant(dataLoader,privateKeyID);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getDhdServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void deleteDhdServiceKeyFileInvalidServiceKeyTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.deleteServiceKeyFileForInvalidServiceKey(dataLoader,configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "deleteDhdServiceKeyFileInvalidServiceKeyTest", dataProvider = "userTestDataProvider")
    public void deleteDhdServiceKeyFileTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.deleteServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"), privateKeyID);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body
        softAssert.assertEquals(response.getBody().asString(),dataLoader.getExpectedResponse());

        softAssert.assertFalse(gcpHelper.validateGcpCredFile(response.getBody().asString()),"Deleted private key ID successfully able to connect with GCP");

        softAssert.assertAll();
    }
}
