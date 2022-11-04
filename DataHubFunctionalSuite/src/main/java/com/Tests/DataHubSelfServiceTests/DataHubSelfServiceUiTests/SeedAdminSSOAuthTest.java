package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.SeedHomePage;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Test(groups = "SeedUserUiTests")
public class SeedAdminSSOAuthTest extends BaseConfiguration {

    private SeedHomePage seedHomePage;
    private DhssLoginPage dhssLoginPage;

    @Test(dataProvider="userTestDataProvider", description = "Validation seed admin sso user login to dhss")
    public void validateSeedAdminSSOLoginTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        SoftAssert softAssert = new SoftAssert();
        seedHomePage = dhssLoginPage.doLoginSeedAdminSSOUser("customerOwnedTenant");
        softAssert.assertTrue(seedHomePage.validatePageTitle(), "Invalid page title");
        seedHomePage.signOutSeedSSOUser();
    }
    
    
    @Test(dataProvider = "userTestDataProvider", description = "do federated sso login..")
	public void validateFedSeedAdminSSOLoginTest(final JSONObject testData) {
    	
    	DataLoader dataLoader = new DataLoader(testData);
    	
    	 dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
    	dhssLoginPage.doLoginFederatedSeedAdminSSOUser("customerOwnedTenantFederated");
    	System.out.println(dataLoader.getTestParameter().get("username"));
    	System.out.println(dataLoader.getUrl());
	}

}
