package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.HomePage;
import com.WebPagesDhd.LoginPage;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "UpdateEnvironmentTest")
public class UpdateEnvironmentTest extends BaseConfiguration {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeClass
    public void doLogin(){
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        homePage=loginPage.doLoginUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of environment update")
    public void validateUpgradeEnvironment(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);

    }

    @Test(dataProvider="userTestDataProvider", description = "Validation of tenant upgrade")
    public void validateTenantUpgrade(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);

    }

}
