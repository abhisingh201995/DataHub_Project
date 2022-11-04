package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.SeedHomePage;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Test(groups = "SeedUserUiTests")
public class SeedConfigurationDetailsSaveUpdateTest extends BaseConfiguration {

    private SeedHomePage seedHomePage;
    private DhssLoginPage dhssLoginPage;
    List<HashMap<String, Object>> sqlResponse = new ArrayList<>();

    @BeforeClass
    public void loginSeedPortal() {
        dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        seedHomePage = dhssLoginPage.doLoginSeedUser("ukgOwnedTenant");
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of save and update seed integration user in DHSS UI")
    public void validateSaveSeedConfigurationTest(final JSONObject testData) {
        seedHomePage.validatePageTitle();
        DataLoader dataLoader = new DataLoader(testData);
        seedHomePage.saveConfigurationDetails(
                configParser.inputParam.get("ukgOwnedTenant.appkey"),
                configParser.inputParam.get("ukgOwnedTenant.tenantSeedUser"),
                configParser.inputParam.get("ukgOwnedTenant.password"),
                configParser.inputParam.get("ukgOwnedTenant.password")
        );
        seedHomePage.clickUpdateBtn();
        SoftAssert softAssert = new SoftAssert();
        PostgresDBHelper postgresDBHelper = new PostgresDBHelper();
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("seedUserDetailsQuery")+" where "+ "\"short_name\"" +" ="+"'"+dataPool.getSeedlogindto().getTenantid()+"'");
        softAssert.assertEquals(sqlResponse.size(), 1);

        sqlResponse.forEach(row -> {
            row.forEach((k, v) -> {
                if (k.equals("WFDUSER")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("username"));
                }
                if (k.equals("WFDAPPKEY")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("appkey"));
                }
            });
        });

        //softAssert.assertAll();

    }


    @Test(dataProvider="userTestDataProvider", dependsOnMethods = "validateSaveSeedConfigurationTest", description = "Validation of save and update seed integration user in DHSS UI")
    public void validateEditSeedConfigurationTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        seedHomePage.saveConfigurationDetails(
                dataLoader.getTestParameter().get("appkey"),
                dataLoader.getTestParameter().get("username"),
                dataLoader.getTestParameter().get("password"),
                dataLoader.getTestParameter().get("password")
        );
        SoftAssert softAssert = new SoftAssert();
        PostgresDBHelper postgresDBHelper = new PostgresDBHelper();
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("seedUserDetailsQuery") + " where "+ "\"short_name\"" +" ="+"'"+dataPool.getSeedlogindto().getTenantid()+"'");
        softAssert.assertEquals(sqlResponse.size(), 1);

        sqlResponse.forEach(row -> {
            row.forEach((k, v) -> {
                if (k.equals("WFDUSER")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("username"));
                }
                if (k.equals("WFDAPPKEY")) {
                    softAssert.assertEquals(v, dataLoader.getTestParameter().get("appkey"));
                }
            });
        });
    }

}
