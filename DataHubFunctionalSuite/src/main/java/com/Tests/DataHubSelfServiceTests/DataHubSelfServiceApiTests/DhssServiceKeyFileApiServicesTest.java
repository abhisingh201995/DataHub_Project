package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.FunctionalUtils.DHSSTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


@Test(groups = "DHSS_Service_Key_File_Tests")
public class DhssServiceKeyFileApiServicesTest extends BaseConfiguration {
    String firstPrivateKeyID;
    String secondPrivateKeyID;
    DHSSTenantHelper dhssTenantHelper = new DHSSTenantHelper();

    @BeforeClass
    public void  loginSeedUser(){
        dhssTenantHelper.refreshAuthToken();
        dhssTenantHelper.activateUkgOwnedTenant();
        dhssTenantHelper.refreshSeedAuthToken("ukgOwnedTenant");
    }

    @Test(dataProvider = "userTestDataProvider")
    public void generateDhssServiceKeyFileTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response1 = dhssTenantHelper.createServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response1.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify private key id
        firstPrivateKeyID = response1.jsonPath().getString("private_key_id");

        softAssert.assertTrue(gcpHelper.validateGcpCredFile(response1.getBody().asString()),"Generated private key ID failed to connect with GCP");

        //Create a new service key for same tenant
        Response response2 = dhssTenantHelper.createServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response2.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify private key id
        secondPrivateKeyID = response2.jsonPath().getString("private_key_id");

        softAssert.assertTrue(gcpHelper.validateGcpCredFile(response2.getBody().asString()),"Generated private key ID failed to connect with GCP");

        softAssert.assertAll();

    }

    @Test(dataProvider = "userTestDataProvider")
    public void generateDhssServiceKeyFileInvalidTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.createServiceKeyFileForInvalidTenant(dataLoader);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dataProvider = "userTestDataProvider")
    public void generateDhssServiceKeyFileInvalidServiceTypeTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.createServiceKeyFileForInvalidServiceType(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code -- > It is giving 200 for invalid servicetype params
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "generateDhssServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void getDhssServiceKeyFileInvalidTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.getServiceKeyFileForInvalidTenant(dataLoader);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getDhssServiceKeyFileInvalidTenantTest", dataProvider = "userTestDataProvider")
    public void getDhssServiceKeyFileTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.getServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");
        softAssert.assertEquals(response.jsonPath().getString("tenantName"),configParser.inputParam.get("ukgOwnedTenant.tenantName"));
        softAssert.assertEquals(response.jsonPath().getString("shortName"),configParser.inputParam.get("ukgOwnedTenant.shortName"));
        softAssert.assertEquals(response.jsonPath().getString("gcpProjectId"),configParser.inputParam.get("ukgOwnedTenant.gcpProjectId"));

        //This verification will only work when no service key is downloaded earlier. As this is the tenant we are using for our testing.
        //Hence, can't possible to clean up everything that exists from earlier. So commenting this code.

        /*HashMap<Object,Object> data = new HashMap<>();
        data=response.as(HashMap.class);
        //Verify body for first service account key
        firstServiceKeyAccountId = (String) ((List<HashMap<Object,Object>>)(data.get("serviceAccountKeys"))).get(0).get("key");
        softAssert.assertEquals(firstServiceKeyAccountId,firstPrivateKeyID);
        //Verify body for second service account key
        secondServiceKeyAccountId = (String) ((List<HashMap<Object,Object>>)(data.get("serviceAccountKeys"))).get(1).get("key");
        softAssert.assertEquals(secondServiceKeyAccountId,secondPrivateKeyID);*/

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = "getDhssServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void deleteDhssServiceKeyFileInvalidTenantAndServiceKeyTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.deleteServiceKeyFileForInvalidTenantAndServiceKey(dataLoader);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getDhssServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void deleteDhssServiceKeyFileInvalidTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.deleteServiceKeyFileForInvalidTenant(dataLoader, firstPrivateKeyID);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = "getDhssServiceKeyFileTest", dataProvider = "userTestDataProvider")
    public void deleteDhssServiceKeyFileInvalidServiceKeyTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.deleteServiceKeyFileForInvalidServiceKey(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"));

        // Verify status code
        softAssert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "deleteDhssServiceKeyFileInvalidServiceKeyTest", dataProvider = "userTestDataProvider")
    public void deleteDhssServiceKeyFileTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        //Verify deletion of first service account key
        Response response1 = dhssTenantHelper.deleteServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"),firstPrivateKeyID);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response1.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body
        softAssert.assertEquals(response1.prettyPrint(),dataLoader.getExpectedResponse());

        softAssert.assertFalse(gcpHelper.validateGcpCredFile(response1.getBody().asString()),"Deleted private key ID successfully able to connect with GCP");

        //Verify deletion of second service account key
        Response response2 = dhssTenantHelper.deleteServiceKeyFile(dataLoader, configParser.inputParam.get("ukgOwnedTenant.tenantName"),secondPrivateKeyID);

        // Verify status code
        softAssert.assertEquals(String.valueOf(response2.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body
        softAssert.assertEquals(response2.prettyPrint(),dataLoader.getExpectedResponse());

        softAssert.assertFalse(gcpHelper.validateGcpCredFile(response2.getBody().asString()),"Deleted private key ID successfully able to connect with GCP");

        softAssert.assertAll();
    }
}
