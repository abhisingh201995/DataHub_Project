package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.GcpHelper;
import com.Utilities.PostgresDBHelper;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.HomePage;
import com.WebPagesDhd.LoginPage;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = "LoginPageTest")
public class LoginPageTest extends BaseConfiguration {

    private static String title;
    private LoginPage loginPage;
    GcpHelper gcpHelper = new GcpHelper();
    PostgresDBHelper postgresDBHelper = new PostgresDBHelper();

    @Test(dataProvider="userTestDataProvider", description = "Validation of user successful login to DHD")
    public void loginTest(final JSONObject testData) throws IOException {
        DataLoader dataLoader=new DataLoader(testData);
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        WebDriverFactory.getDriver().get(System.getenv("baseUrl")+"admin/");
        HomePage homePage = loginPage.doLoginUser(System.getenv("username"),System.getenv("password"));
        Boolean data= homePage.checkElementPresent("Log out");


    }

}
