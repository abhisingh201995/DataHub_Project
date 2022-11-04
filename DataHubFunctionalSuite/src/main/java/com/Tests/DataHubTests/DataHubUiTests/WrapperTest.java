package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.HomePage;
import com.WebPagesDhd.LoginPage;
import com.WebPagesDhd.WrappersPage;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "WrapperTest")
public class WrapperTest extends BaseConfiguration {

    private static String title;
    private LoginPage loginPage;
    private HomePage homePage;

    private String wrapperName;
    WrappersPage wrappersPage;

    @BeforeClass
    public void doLogin(){
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        homePage=loginPage.doLoginUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validation wrapper is successfully created with Global Tenant")
    public void validateWrapperWithGlobalTenant(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        String dateTime= helper.getDateTimeStamp();
        wrapperName= "AutomatedTest_"+dateTime;
        wrappersPage= homePage.clickOnWrapperButton();
        wrappersPage.clickAddWrapperButton();
        wrappersPage.createNewWrapper("AutomatedTest_"+dateTime,dataLoader.getTestParameter().get("wrapperType"));
        dataPool.setWrapper(wrapperName);
    }

    @Test(dataProvider="userTestDataProvider", dependsOnMethods = {"validateWrapperWithGlobalTenant"}, description = "Validate global tenant is not available for non super use")
    public void validateGlobalTenantNonSuperUser(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        loginPage = wrappersPage.getMasterPage().clickLogOut();
        homePage = loginPage.doLoginUser(dataLoader.getTestParameter().get("username"),dataLoader.getTestParameter().get("password"));
        wrappersPage = homePage.clickOnWrapperButton();
        wrappersPage.clickAddWrapperButton();

        for(String item: wrappersPage.getListOfTenants()){
           if(item.equals("Global")){
               Assert.assertTrue(false,"Global tenant is showing for non super user");
           }
        }
    }

    @Test(dataProvider="userTestDataProvider", dependsOnMethods = {"validateGlobalTenantNonSuperUser"}, description = "Validate non super user unable to delete wrapper")
    public void validateNonSuperUserUnableToDeleteWrapper(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        homePage = wrappersPage.getMasterPage().clickHomePage();
        wrappersPage = homePage.clickOnWrapperButton();
        wrappersPage.selectWrapperCheckBox(wrapperName);
        wrappersPage.selectAndGoActionForSelectedWrapper("Delete selected Wrappers");
        wrappersPage.clickDeleteConfirmationFailed();
    }


}
