package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.FunctionalUtils.DHSSTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Test(groups = "DHSS_Seed_User_Tests")
public class DHSSSeedUserSaveApiServicesTest extends BaseConfiguration {

    DHSSTenantHelper dhssSeedUserApiHelper = new DHSSTenantHelper();
    PostgresDBHelper postgresDBHelper = new PostgresDBHelper();

    Headers headers;
    List<HashMap<String, Object>> sqlResponse = new ArrayList<>();

    @Test(dataProvider="userTestDataProvider")
    public void dhssSeedUserAuthTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        dhssTenantHelper.refreshAuthToken();
        dhssTenantHelper.activateCustomerOwnedTenant();

        dataLoader.setPayload(dataLoader.getPayload()
                .replace("{username}", configParser.inputParam.get("customerOwnedTenant.tenantSeedUser"))
                .replace("{password}",configParser.inputParam.get("customerOwnedTenant.password")));

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(), dataLoader.getPayload(), null);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Seed user auth failed with response = "+ response.prettyPrint());

        dataPool.getSeedlogindto().setAccess_token(response.path("authToken"));
        dataPool.getSeedlogindto().setTenantid(response.path("tenantId"));
        dhssSeedUserApiHelper.setSeedSessionInfo();

    }

    @Test(dataProvider="userTestDataProvider")
    public void dhssSeedUserNegativeAuthTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);

        dataLoader.setPayload(dataLoader.getPayload()
                .replace("{username}", "incorrect")
                .replace("{password}","incorrect"));

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(), dataLoader.getPayload(), null);
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        Assert.assertEquals(response.getStatusCode(),dataLoader.getOutputStatusCode());

    }

    @Test(dataProvider = "userTestDataProvider")
    public void AdminLoginNegativeSaveSeedUserTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssSeedUserApiHelper.saveSeedUser(dataLoader);
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

    }

    @Test(dependsOnMethods = "dhssSeedUserAuthTest", dataProvider = "userTestDataProvider")
    public void saveSeedUserTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssSeedUserApiHelper.saveSeedUser(dataLoader,"customerOwnedTenant");
        response.prettyPrint();

        softAssert.assertEquals(response.prettyPrint(), dataLoader.getExpectedResponse(),"Actual response does not match, Response="+response.prettyPrint());

        if(response.getStatusCode()==200) {
            sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("seedUserDetailsQuery") + " where " + "\"short_name\"" + "=" + "'" + dataPool.getSeedlogindto().getTenantid() + "'");
            softAssert.assertEquals(sqlResponse.size(), 1);

            sqlResponse.forEach(row -> {
                row.forEach((k, v) -> {
                    if (k.equals("short_name")) {
                        softAssert.assertEquals(v, dataPool.getSeedlogindto().getTenantid());
                    }
                    if (k.equals("WFDUSER")) {
                        softAssert.assertEquals(v, configParser.inputParam.get("customerOwnedTenant"+".tenantSeedUser"));
                    }
                    if (k.equals("WFDAPPKEY")) {
                        softAssert.assertEquals(v, configParser.inputParam.get("customerOwnedTenant"+".appkey"));
                    }
                });
            });
        }

        softAssert.assertAll();


    }

    @Test(dependsOnMethods = "saveSeedUserTest", dataProvider = "userTestDataProvider")
    public void updateSeedUserTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssSeedUserApiHelper.saveSeedUser(dataLoader,"customerOwnedTenant");
        response.prettyPrint();

        softAssert.assertEquals(response.getBody().toString(), dataLoader.getExpectedResponse());
        headers = response.getHeaders();
        softAssert.assertEquals(headers.get("Content-Type"), dataLoader.getOutputData().get("Content-Type"));

        PostgresDBHelper postgresDBHelper = new PostgresDBHelper();
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("seedUserDetailsQuery")+"where WFDTENANT="+ dataPool.getSeedlogindto().getTenantid());
        softAssert.assertEquals(sqlResponse.size(), 1);

        sqlResponse.forEach(row -> {
            row.forEach((k, v) -> {
                if (k.equals("WFDUSER")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("username"));
                }
                if (k.equals("WFDPASSWORD")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("password"));
                }
                if (k.equals("WFDAPPKEY")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("appkey"));
                }
            });
        });

    }
}
