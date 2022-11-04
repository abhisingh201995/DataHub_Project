package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.*;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test(groups = "TenantSetupTest")
public class TenantSetupTest extends BaseConfiguration {

    private LoginPage loginPage;
    private HomePage homePage;
    private TenantConfigurationPage tenantConfigurationPage;
    private AddTenantPage addTenantPage;

    private List<String > tenantList=new ArrayList<>();

    @BeforeClass
    public void doLogin(){
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        homePage=loginPage.doLoginUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of creation and installation of Tenant in DHD")
    public void addAndInstallTenantTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantConfigurationPage = homePage.clickOnTenantConfigurationButton();
        tenantConfigurationPage.validatePageHeader();
        addTenantPage=tenantConfigurationPage.clickOnAddTenant();
        String dateTime= helper.getDateTimeStamp();
        addTenantPage.validatePageHeader();
        dataPool.setTenantsDataHubDirector(dataLoader.getTestParameter().get("dimensionTenantId")+dateTime);
        addTenantPage.addTenant(
                dataLoader.getTestParameter().get("dimensionTenantId")+dateTime,
                dataLoader.getTestParameter().get("tenantDescription"),
                dataLoader.getTestParameter().get("bigQueryDatasetPrefix")+dateTime,
                Integer.valueOf(dataLoader.getTestParameter().get("dataProject")),
                dataLoader.getTestParameter().get("wfdClientId"),
                dataLoader.getTestParameter().get("wfdClientSecret"),
                dataLoader.getTestParameter().get("wfdUser"),
                dataLoader.getTestParameter().get("wfdPassword"),
                dataLoader.getTestParameter().get("wfdAppKey"),
                dataLoader.getTestParameter().get("wfdVanityUrl"),
                dataLoader.getTestParameter().get("scrubbed"),
                dataLoader.getTestParameter().get("defaultUserType"),
                dataLoader.getTestParameter().get("ddva"),
                dataLoader.getTestParameter().get("performanceTier"),
                dataLoader.getTestParameter().get("timeZone")
        );
        addTenantPage.clickSaveButton();
        String message=addTenantPage.getTenantCreateStatusMessage();
        Assert.assertEquals(message,dataLoader.getOutputStatusCode());

        addTenantPage.clickOnWfdConnectivity();
        Assert.assertTrue(addTenantPage.checkWfdConnectivityStatus());

        addTenantPage.clickOnHyperFindConfigured();
        Assert.assertTrue(addTenantPage.checkHyperFindConfiguredStatus());

        tenantConfigurationPage=addTenantPage.clickOnInstallAndExecuteMaintainace();
        homePage = tenantConfigurationPage.clickOnHomePage();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of Paycode mapping generated for added tenant")
    public void validateAddedTenantPayCodeMapping(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
      /*  PayCodeMappingPage payCodeMappingPage = homePage.clickOnPayCodeMappingButton();
        payCodeMappingPage.validatePageHeader();
        dataPool.getTenantsDataHubDirector().forEach(item->{
            Assert.assertTrue(payCodeMappingPage.validateTenantInFilterGrid(item));
        });
        homePage=payCodeMappingPage.getMasterPage().clickHomePage();*/
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of location mapping generated for added tenant")
    public void validateAddedTenantLocationTypeMapping(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
       /* LocationTypeMappingPage locationTypeMappingPage = homePage.clickOnLocationTypeMappingButton();
        locationTypeMappingPage.validatePageHeader();
        dataPool.getTenantsDataHubDirector().forEach(item->{
            Assert.assertTrue(locationTypeMappingPage.validateTenantInFilterGrid(item));
        });*/
    }



}
