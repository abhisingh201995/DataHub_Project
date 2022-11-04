package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.FunctionalUtils.DHSSTenantHelper;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;

@Test(groups = "Additional_Property_Tests")
public class DHSSAdditionalPropertyApiServicesTest extends BaseConfiguration {

    DHSSTenantHelper dhssTenantHelper = new DHSSTenantHelper();
    PostgresDBHelper postgresDBHelper = new PostgresDBHelper();

    @Test(dataProvider="userTestDataProvider")
    public void updateAdditionalPropertyTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        SoftAssert softAssert = new SoftAssert();
        String tenantName = dhssTenantHelper.createTenant();
        Response response = dhssTenantHelper.addAdditionalProperty(dataLoader,tenantName);

        List<HashMap<String,Object> >sqlResponse;
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +"="+"'"+dataPool.getTenantNameUser()+"'");

        sqlResponse.forEach(row->{
            row.forEach((k,v)-> {
                if (k.equals("datahub_licensed_pro")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("isLicensedCustomer"), "Is licensed customer not updated");
                }
                if (k.equals("ukg_pro")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("isUkgProCustomer"), "Is ukg pro field not updated");
                }
                if (k.equals("internally_owned_gcp")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("isGcpUkgOwned"), "Is ukg owned field not updated");
                }
                if (k.equals("ddva")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("isViewPointTenant"), "Is view point tenant field not updated");
                }
                if (k.equals("SCRUBBED")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("isScrubbed"), "Is scrubbed field not updated");
                }
                if (k.equals("PERFORMANCE_TIER_id")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("performanceTier"), "Performance tier field not updated");
                }
                if (k.equals("TIMEZONE")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("timeZone"), "Timezone field not updated");
                }
            });

        });

        dhssTenantHelper.deleteTenant(tenantName);

    }

}
