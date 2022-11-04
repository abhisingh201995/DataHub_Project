package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.DataObjects.DhssDataObject.CustomerOwnedGcpDto;
import com.FunctionalUtils.DHSSTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;


@Test(groups = "DHSS_SeedAdmin_CustomerOwnedGcp_Tests")
public class SeedAdminAdditionalPropertiesCustomerOwnedGcpApiServicesTest extends BaseConfiguration {
    DHSSTenantHelper dhssTenantHelper = new DHSSTenantHelper();
    PostgresDBHelper postgresDBHelper = new PostgresDBHelper();
    CustomerOwnedGcpDto customerOwnedGcpDto = new CustomerOwnedGcpDto();


    @BeforeClass
    public void loginAdminUser() {
        dhssTenantHelper.refreshAuthToken();
        dhssTenantHelper.activateCustomerOwnedTenant();
        dhssTenantHelper.refreshSeedAuthToken("customerOwnedTenant");
    }

    // This test needs to be commented as we dont want this to run as part of suite, we hav decided not to run CREATE tests regularly.
    // we will run them periodically when needed.
    @Test(dataProvider = "userTestDataProvider")
    public void provisionSeedUserCustomerOwnedGCP(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.provisionCustomerOwnedGcp(dataLoader);
        response.prettyPrint();

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()), dataLoader.getOutputStatusCode(), "Response status code is incorrect");
        //Verify body
        softAssert.assertEquals(response.getBody().asString(), dataLoader.getExpectedResponse());

        List<HashMap<String, Object>> sqlResponse;
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("provisionGcpSqlQuery") + " where " + "\"WFDTENANT\"" + "=" + "'" + dataPool.getTenantNameUser() + "'");

        sqlResponse.forEach(row -> {
            row.forEach((k, v) -> {
                if (k.equals("TVCPROJECT_id")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("gcpProjectId"), "project id not updated");
                }
                if (k.equals("TVCTENANT_id")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("tenantId"), "tenant id not updated");
                }
                if (k.equals("version")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("version"), "version not updated");
                }
            });

        });
    }

    @Test(dataProvider = "userTestDataProvider")
    public void getSeedUserCustomerOwnedGcpProperties(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.getCustomerOwnedGcpAddProp(dataLoader, dataLoader.getTestParameter().get("tenantId"));
        response.prettyPrint();

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()), dataLoader.getOutputStatusCode(), "Response status code is incorrect");

        customerOwnedGcpDto.setTenantId(response.getBody().path("shortName"));
        customerOwnedGcpDto.setGcpProjectId(response.getBody().path("gcpProjectId"));
        customerOwnedGcpDto.setGcpProjectDesc(response.getBody().path("gcpProjectDesc"));

        //Verify body
        softAssert.assertEquals(response.jsonPath().getString("tenantName"), dataLoader.getTestParameter().get("tenantId"));

        //todo add DB validation step instead of hardcoded value
       /* softAssert.assertEquals(response.jsonPath().getString("gcpProjectId"), dataLoader.getTestParameter().get("gcpProjectId"));
        softAssert.assertEquals(response.jsonPath().getString("gcpProjectDesc"), dataLoader.getTestParameter().get("gcpProjectName"));
        softAssert.assertEquals(response.jsonPath().getString("isGcpProvisioned"), true);
        softAssert.assertEquals(response.jsonPath().getString("isServiceKeyPresent"), true);
       */

    }

    @Test(dataProvider = "userTestDataProvider")
    public void getTenantVersionCustomerOwnedGcpProperties(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.getTenantVersionSeedAddProp(dataLoader, dataLoader.getTestParameter().get("tenantId"));
        response.prettyPrint();


        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()), dataLoader.getOutputStatusCode(), "Response status code is incorrect");

        //Verify body

        //todo: add db validation step
        softAssert.assertEquals(response.jsonPath().getString("tenantName"), dataLoader.getTestParameter().get("tenantId"));
        // softAssert.assertEquals(response.jsonPath().getString("tenantVersion"), dataLoader.getTestParameter().get("tenantVersion"));
    }

    @Test(dataProvider = "userTestDataProvider")
    public void updateProvisionedSeedUserCustomerOwnedGCP(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.updateProvisionedCustomerOwnedGcp(dataLoader, customerOwnedGcpDto);
        response.prettyPrint();

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()), dataLoader.getOutputStatusCode(), "Response status code is incorrect");
        //Verify body
        softAssert.assertEquals(response.getBody().asString(), dataLoader.getExpectedResponse());
    }

}
