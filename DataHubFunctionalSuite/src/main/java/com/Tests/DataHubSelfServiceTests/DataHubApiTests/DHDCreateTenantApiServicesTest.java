package com.Tests.DataHubSelfServiceTests.DataHubApiTests;

import com.FunctionalUtils.DHDTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;

@Test(groups = "DHD_Create_Update_Tenant_Tests")
public class DHDCreateTenantApiServicesTest extends BaseConfiguration {

    Headers headers;
    List<HashMap<String, Object>> sqlResponse;
    DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();
    PostgresDBHelper postgresDBHelper = new PostgresDBHelper();

    @Test(dataProvider="userTestDataProvider")
    public void dhdCreateTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.createDhdTenant(dataLoader);

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body
        softAssert.assertEquals(response.getBody().asString(),dataLoader.getExpectedResponse());

        //Verify DB state
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +"="+"'"+dataPool.getTenantsDataHubDirector()+"'");
        softAssert.assertEquals(sqlResponse.size(),1);

        sqlResponse.forEach(row->{
            row.forEach((k,v)->{
                if(k.equals("WFDTENANT")){
                    softAssert.assertEquals(v,dataPool.getTenantsDataHubDirector(),"Entered value for tenantName is not matching");
                }
                if(k.equals("WFDCLIENTID")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("clientId"),"Entered value for ClientId is not matching");
                }
                if(k.equals("WFDCLIENTSECRET")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("clientSecret"),"Entered value for ClientSecret is not matching");
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

                if(k.equals("solution")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("solution"),"Entered value for solution is not matching");
                }
                if(k.equals("ddva")){
                    softAssert.assertEquals(v,"","ddva is not null");}
            });

        });

        softAssert.assertAll();
    }

    @Test(dataProvider="userTestDataProvider")
    public void dhdUpdateTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        Response response = dhdTenantHelper.updateDhdTenant(dataLoader);

        // Verify status code
        Assert.assertEquals(String.valueOf(response.getStatusCode()),dataLoader.getOutputStatusCode(),"Response status code is incorrect");

        //Verify body
        softAssert.assertEquals(response.getBody().asString(),dataLoader.getExpectedResponse());

        //Verify DB state
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +"="+"'"+dataPool.getTenantsDataHubDirector()+"'");
        softAssert.assertEquals(sqlResponse.size(),1);

        sqlResponse.forEach(row->{
            row.forEach((k,v)->{
                if(k.equals("WFDTENANT")){
                    softAssert.assertEquals(v,dataPool.getTenantsDataHubDirector(),"Entered value for tenantName is not matching");
                }
                if(k.equals("WFDCLIENTID")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("clientId"),"Entered value for ClientId is not matching");
                }
                if(k.equals("WFDCLIENTSECRET")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("clientSecret"),"Entered value for ClientSecret is not matching");
                }
                if(k.equals("ukg_pro")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("isUkgProCustomer"),"isUkgProCustomer is not null");
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

                if(k.equals("solution")){
                    softAssert.assertEquals(v,dataLoader.getTestParameter().get("solution"),"Entered value for solution is not matching");
                }
                if(k.equals("ddva")){
                    softAssert.assertEquals(v,"","ddva is not null");}
            });

        });

        softAssert.assertAll();
    }
}