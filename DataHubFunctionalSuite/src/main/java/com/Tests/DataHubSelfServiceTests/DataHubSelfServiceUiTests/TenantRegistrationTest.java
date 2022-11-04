package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.PostgresDBHelper;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.TenantListPage;
import com.WebPagesDhss.TenantRegistrationPage;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;

@Test(groups = "TenantRegistrationTest")
public class TenantRegistrationTest extends BaseConfiguration {

    private DhssLoginPage dhssLoginPage;
    private TenantRegistrationPage tenantRegistrationPage;
    private TenantListPage tenantListPage;
    private PostgresDBHelper postgresDBHelper = new PostgresDBHelper();
    private String tenantName;


    List<HashMap<String, Object>> sqlResponse;

    @BeforeClass
    public void doLogin(){
        dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        tenantListPage = dhssLoginPage.doLoginAdminUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of creation and installation of Tenant in DHSS UI")
    public void addAndInstallTenantTest(final JSONObject testData) {
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        String dateTime= helper.getDateTimeStamp();
        tenantRegistrationPage = tenantListPage.clickOnAddTenant();
        tenantName=dataLoader.getTestParameter().get("tenantName")+dateTime;
        tenantRegistrationPage.addDHSSTenant(
                tenantName,
                dataLoader.getTestParameter().get("tenantShortName")+dateTime,
                dataLoader.getTestParameter().get("tenantVanityURL")+dateTime+".com:9393",
                dataLoader.getTestParameter().get("OpenAMURL"),
                dataLoader.getTestParameter().get("WFDURL")+dateTime+".dev.mykronos.com",
                dataLoader.getTestParameter().get("ClientID"),
                dataLoader.getTestParameter().get("ClientSecret"),
                dataLoader.getTestParameter().get("solution"),
                dataLoader.getTestParameter().get("tenantSeedUser"),
                dataLoader.getTestParameter().get("federated"),
                dataLoader.getTestParameter().get("dimensionFederatedUrl")
        );
        tenantRegistrationPage.clickRegisterButton();

        //Verify DB state
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +" ="+"'"+tenantName+"'");
        softAssert.assertEquals(sqlResponse.size(),1,"Tenant detail not saved in DHD db for tenant name="+tenantName);
        softAssert.assertAll();
    }

    @Test(dataProvider="userTestDataProvider",dependsOnMethods = "addAndInstallTenantTest", description = "Validation of update and installation of Tenant in DHSS UI")
    public void editAndInstallTenantTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        String tenantName=this.tenantName;
        String dateTime= helper.getDateTimeStamp();

        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);

        tenantRegistrationPage = tenantListPage.clickOnEditTenant();
        tenantRegistrationPage.updateDHSSTenant(
                tenantName,
                dataLoader.getTestParameter().get("tenantVanityURL")+dateTime+".com:9393",
                dataLoader.getTestParameter().get("OpenAMURL"),
                dataLoader.getTestParameter().get("WFDURL")+dateTime+".dev.mykronos.com",
                dataLoader.getTestParameter().get("ClientID"),
                dataLoader.getTestParameter().get("ClientSecret"),
                dataLoader.getTestParameter().get("solution"),
                dataLoader.getTestParameter().get("tenantSeedUser")
        );
        tenantRegistrationPage.clickRegisterButton();

        //Verify DB state
        sqlResponse = postgresDBHelper.queryPostgresTable(dataLoader.getTestParameter().get("tenantRetrievalSqlQuery")+" where "+ "\"WFDTENANT\"" +" ="+"'"+tenantName+"'");
        softAssert.assertEquals(sqlResponse.size(),1,"Tenant not edited in DHD, Tenant name:= "+tenantName);

        softAssert.assertAll();
    }
}
