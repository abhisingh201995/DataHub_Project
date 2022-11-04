package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;

@Test(groups = "DHSS_AdminUser_Tests")
public class DHSSAdminLoginApiServicesTest extends BaseConfiguration {

    Headers headers;
    List<HashMap<String, Object>> sqlResponse;

    private PostgresDBHelper postgresDBHelper = new PostgresDBHelper();

    @Test(dataProvider="userTestDataProvider")
    public void dhssAdminAuthTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);

        dataLoader.setPayload(dataLoader.getPayload()
                .replace("{username}", dataLoader.getTestParameter().get("username"))
                .replace("{password}",dataLoader.getTestParameter().get("password")));

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(), dataLoader.getPayload(), null);
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Login Failed for Admin User");
        dataPool.getAdminlogindto().setAdminSessionId(response.path("authToken"));
        dhssTenantHelper.setAdminSessionInfo();

    }

    @Test(dataProvider="userTestDataProvider")
    public void dhssAdminNegativeAuthTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);

        dataLoader.setPayload(dataLoader.getPayload()
                .replace("{username}", "incorrect")
                .replace("{password}","incorrect"));

        Response response = helper.sendApiRequestRelaxedHttps(dataLoader.getMethod(), dataLoader.getDhssEnvBaseUrl()+dataLoader.getUrl(), dataLoader.getPayload(), null);
        response.prettyPrint();
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Status code is not coming as expected");

    }

    @Test(dependsOnMethods = "dhssAdminAuthTest", dataProvider="userTestDataProvider")
    public void adminLoginCreateTenantTest(final JSONObject testData) throws JSONException{
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.createTenant(dataLoader);

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body

        //todo apply this if assert failed, even if message is same
        //JSONAssert.assertEquals(dataLoader.getExpectedResponse(),response.prettyPrint(), JSONCompareMode.LENIENT);

        //Verify DB state
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +"="+"'"+dataPool.getTenantNameUser()+"'");
        softAssert.assertEquals(sqlResponse.size(),1, "Tenant detail not saved in DHD DB, no row found!");

        sqlResponse.forEach(row->{
            row.forEach((k,v)->{
                if(k.equals("WFDTENANT")){
                    softAssert.assertEquals(v,dataPool.getTenantNameUser(),"Entered value for tenantName is not matching");
                }
                if(k.equals("short_name")){
                    softAssert.assertEquals(v,dataPool.getTenantShortName(),"Entered value for tenantShortName is not matching");
                }
                if(k.equals("WFDCLIENTID")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("ClientId"),"Entered value for ClientId is not matching");
                }
                if(k.equals("WFDCLIENTSECRET")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("ClientSecret"),"Entered value for ClientSecret is not matching");
                }
                if(k.equals("ukg_pro")){
                    softAssert.assertEquals(v,"","isUkgProCustomer is not null");
                }
                if(k.equals("datahub_licensed_pro")){
                    softAssert.assertEquals(v,"","isLicensedCustomer is not null");
                }
                if(k.equals("internally_owned_gcp")){
                    softAssert.assertEquals(v,"","isGcpUkgOwned is not null");
                }
                if(k.equals("SCRUBBED")){
                    softAssert.assertEquals(v,"","isScrubbed is not null");
                }
                if(k.equals("PERFORMANCE_TIER_id")){
                    softAssert.assertEquals(v,"","performanceTier is not null");
                }
                if(k.equals("TIMEZONE")){
                    softAssert.assertEquals(v,"","timeZone is not null");
                }
                if(k.equals("DATAPROJECT_id")){
                    softAssert.assertEquals(v,"","gcpProjectId is not null");
                }
                if(k.equals("local_policy")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("local_policy"),"Entered value for local_policy is not matching");
                }
                if(k.equals("solution")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("solution"),"Entered value for solution is not matching");
                }
                if(k.equals("WFDAPPKEY")){
                    softAssert.assertEquals(v,"","Entered value for solution is not matching");
                }
                if(k.equals("ddva")){
                    softAssert.assertEquals(v,"","ddva is not null");}
            });

        });

        softAssert.assertAll();
    }

    @Test(dependsOnMethods ="adminLoginCreateTenantTest", dataProvider="userTestDataProvider")
    public void adminLoginUpdateTenantTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhssTenantHelper.updateTenant(dataLoader);

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body
        softAssert.assertEquals(response.getBody().asString(),dataLoader.getExpectedResponse());

        //Verify DB state
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +"="+"'"+dataPool.getTenantNameUser()+"'");
        softAssert.assertEquals(sqlResponse.size(),1);

        //Verify the updated values in DB table -- > Env and Solution from test json file
        sqlResponse.forEach(row->{
            row.forEach((k,v)->{
                if(k.equals("WFDTENANT")){
                    softAssert.assertEquals(v,dataPool.getTenantNameUser(),"Entered value for tenantName is not matching");
                }
                if(k.equals("short_name")){
                    softAssert.assertEquals(v,dataPool.getTenantShortName(),"Entered value for tenantShortName is not matching");
                }
                if(k.equals("WFDCLIENTID")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("ClientId"),"Entered value for ClientId is not matching");
                }
                if(k.equals("WFDCLIENTSECRET")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("ClientSecret"),"Entered value for ClientSecret is not matching");
                }
                if(k.equals("ukg_pro")){
                    softAssert.assertEquals(v,"","isUkgProCustomer is not null");
                }
                if(k.equals("datahub_licensed_pro")){
                    softAssert.assertEquals(v,"","isLicensedCustomer is not null");
                }
                if(k.equals("internally_owned_gcp")){
                    softAssert.assertEquals(v,"","isGcpUkgOwned is not null");
                }
                if(k.equals("SCRUBBED")){
                    softAssert.assertEquals(v,"","isScrubbed is not null");
                }
                if(k.equals("PERFORMANCE_TIER_id")){
                    softAssert.assertEquals(v,"","performanceTier is not null");
                }
                if(k.equals("TIMEZONE")){
                    softAssert.assertEquals(v,"","timeZone is not null");
                }
                if(k.equals("DATAPROJECT_id")){
                    softAssert.assertEquals(v,"","gcpProjectId is not null");
                }
                if(k.equals("local_policy")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("local_policy"),"Entered value for local_policy is not matching");
                }
                if(k.equals("solution")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("solution"),"Entered value for solution is not matching");
                }
                if(k.equals("WFDAPPKEY")){
                    softAssert.assertEquals(v,"","Entered value for solution is not matching");
                }
                if(k.equals("ddva")){
                    softAssert.assertEquals(v,"","ddva is not null");}
            });

        });

        softAssert.assertAll();
    }

}