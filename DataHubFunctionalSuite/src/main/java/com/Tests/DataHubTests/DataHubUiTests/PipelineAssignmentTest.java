package com.Tests.DataHubTests.DataHubUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhd.HomePage;
import com.WebPagesDhd.LoginPage;
import com.WebPagesDhd.PipelineAssignmentPage;
import com.WebPagesDhd.WrappersPage;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "PipelineAssignmentTest")
public class PipelineAssignmentTest extends BaseConfiguration {

    private LoginPage loginPage;
    private HomePage homePage;
    private WrappersPage wrappersPage;
    private PipelineAssignmentPage pipelineAssignmentPage;

    @BeforeClass
    public void doLogin(){
        loginPage = new LoginPage(WebDriverFactory.getDriver());
        homePage=loginPage.doLoginUser();
    }

    @Test(dataProvider="userTestDataProvider", description = "Validate global tenant is successfully assigned in PA assignment")
    public void validatePipelineAssignmentCreatedSuccessfully(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        pipelineAssignmentPage=homePage.clickOnPipelineAssignmentButton();
        pipelineAssignmentPage.clickOnAddPipelineAssignment();
        pipelineAssignmentPage.createPipelineAssignment(dataPool.getWrapper(),"getForecastWeeks","Yes");
    }

    @Test(dataProvider="userTestDataProvider", description = "Validate global tenant is not available for non super use in PA assignment")
    public void validateGlobalTenantNotAvailableForNonSuperUser(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        loginPage = pipelineAssignmentPage.getMasterPage().clickLogOut();
        homePage = loginPage.doLoginUser(dataLoader.getTestParameter().get("username"),dataLoader.getTestParameter().get("password"));
        pipelineAssignmentPage=homePage.clickOnPipelineAssignmentButton();
        pipelineAssignmentPage.clickOnAddPipelineAssignment();
        for(String item: pipelineAssignmentPage.getListOfWrappers()){
            if(item.equals(dataPool.getWrapper())){
                Assert.assertTrue(false,"Global tenant is showing for non super user");
            }
        }
    }

    @Test(dataProvider="userTestDataProvider", dependsOnMethods = {"validateGlobalTenantNotAvailableForNonSuperUser"}, description = "Validate wrapper successfully deleted with confirmation")
    public void validateDeleteWrapper(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        loginPage = pipelineAssignmentPage.getMasterPage().clickLogOut();
        homePage = loginPage.doLoginUser();
        wrappersPage = homePage.clickOnWrapperButton();
        wrappersPage.selectWrapperCheckBox(dataPool.getWrapper());
        wrappersPage.selectAndGoActionForSelectedWrapper("Delete selected Wrappers");
        wrappersPage.clickDeleteConfirmationSuccess();
    }
}
