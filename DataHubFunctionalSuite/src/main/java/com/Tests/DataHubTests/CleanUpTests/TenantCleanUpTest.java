package com.Tests.DataHubTests.CleanUpTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.*;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test(groups = "CleanUpTests")
public class TenantCleanUpTest extends BaseConfiguration {

    private LoginPage loginPage;
    private HomePage homePage;
    private WrappersPage wrappersPage;
    private TenantConfigurationPage tenantConfigurationPage;
    private AddTenantPage addTenantPage;

    private List<String > tenantList=new ArrayList<>();

    @BeforeClass
    public void doLogin(){
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        homePage=loginPage.doLoginUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validate wrapper successfully deleted with confirmation")
    public void validateDeleteWrapper(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        wrappersPage = homePage.clickOnWrapperButton();
        wrappersPage.selectWrapperCheckBox(dataPool.getWrapper());
        wrappersPage.selectAndGoActionForSelectedWrapper("Delete selected Wrappers");
        wrappersPage.clickDeleteConfirmationSuccess();
        homePage = wrappersPage.getMasterPage().clickHomePage();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of deletion of tenant")
    public void deleteTenantBigQueryTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantConfigurationPage = homePage.clickOnTenantConfigurationButton();
        tenantConfigurationPage.validatePageHeader();
        tenantConfigurationPage.deleteTenantBigQueryTableData(dataPool.getTenantsDataHubDirector());

    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of deletion of tenant")
    public void deleteTenantWebsiteDataTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantConfigurationPage = homePage.clickOnTenantConfigurationButton();
        tenantConfigurationPage.validatePageHeader();
        tenantConfigurationPage.deleteTenantWebsiteData(dataPool.getTenantsDataHubDirector());

    }

}
