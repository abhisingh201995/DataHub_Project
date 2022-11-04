package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;


import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.SeedHomePage;
import com.WebPagesDhss.TenantListPage;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

@Test(groups = "DhssLoginPageTests")
public class DhssLoginTest extends BaseConfiguration {

    DhssLoginPage dhssLoginPage;

    @Test(dataProvider="userTestDataProvider", description = "Validation admin user login to dhss")
    public void validateAdminUserLoginTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        TenantListPage tenantListPage = dhssLoginPage.loginAdminUser(dataLoader.getUrl(),dataLoader.getTestParameter().get("username"),
                dataLoader.getTestParameter().get("password"));
        tenantListPage.searchTenant("Defect");
        dhssLoginPage = tenantListPage.signOutAdminUser();
    }

    @Test(dataProvider="userTestDataProvider", dependsOnMethods = "validateAdminUserLoginTest", description = "Validation seed user login to dhss")
    public void validateSeedUserLoginTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        dhssTenantHelper.refreshAuthToken();
        dhssTenantHelper.activateUkgOwnedTenant();
        SeedHomePage seedHomePage = dhssLoginPage.doLoginSeedUser("ukgOwnedTenant");
        seedHomePage.signOutSeedUser();
    }


}
