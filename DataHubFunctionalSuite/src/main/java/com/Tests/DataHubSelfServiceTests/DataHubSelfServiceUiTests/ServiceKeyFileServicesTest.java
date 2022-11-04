package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.SeleniumHelper;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.AdditionalPropertiesPage;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.SeedHomePage;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * TestCaseId : 227756 , 227758
 * Author: Abhishek Singh
 * **/

@Test(groups = "DHSS_UI_Service_Key_File_Tests")
public class ServiceKeyFileServicesTest extends BaseConfiguration {

    int tableRowCountBeforeKeyDownloaded;
    int tableRowCountAfterKeyDownloaded;
    int tableRowCountAfterKeyDeleted;
    String latestDownloadedFileName;
    String latestFileFromDownloads;
    private DhssLoginPage dhssLoginPage;
    private SeedHomePage seedHomePage;
    private AdditionalPropertiesPage additionalPropertiesPage;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    @BeforeClass
    public void loginSeedUserPortal() {
        dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        seedHomePage = dhssLoginPage.doLoginSeedUser("ukgOwnedTenant");
    }

    @Test(dataProvider="userTestDataProvider")
    public void generateServiceKeyFileTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        additionalPropertiesPage = seedHomePage.clickAdditionalPropertiesBtn();
        softAssert.assertTrue(additionalPropertiesPage.validatePageTitle(configParser.inputParam.get("ukgOwnedTenant.tenantName")), "Additional Properties page is not displayed");
        softAssert.assertTrue(additionalPropertiesPage.validateProjectIdField(), "Bad behavior. Project ID field is editable");
        softAssert.assertTrue(additionalPropertiesPage.validateGCPProjectNameField(), "Bad behavior. GCP Project Name field is editable");
        softAssert.assertEquals(additionalPropertiesPage.getActualProjectIdLabel(),dataLoader.getOutputData().get("expectedProjectIDLabel"), "Project Id label is incorrect");
        softAssert.assertEquals(additionalPropertiesPage.getActualGCPProjectNameLabel(),dataLoader.getOutputData().get("expectedGCPProjectNameLabel") ,"GCP Project Name label is incorrect");
        softAssert.assertEquals(additionalPropertiesPage.getActualFileNameColumnLabel(),dataLoader.getOutputData().get("expectedFileNameColumnLabel"), "FileName column label is incorrect");
        softAssert.assertEquals(additionalPropertiesPage.getActualStatusColumnLabel(),dataLoader.getOutputData().get("expectedStatusColumnLabel"), "Status column label is incorrect");
        softAssert.assertEquals(additionalPropertiesPage.getActualKeyColumnLabel(),dataLoader.getOutputData().get("expectedKeyColumnLabel"), "Key column label is incorrect");
        softAssert.assertEquals(additionalPropertiesPage.getActualKeyCreationDateColumnLabel(),dataLoader.getOutputData().get("expectedKeyCreationDateColumnLabel"), "Key Creation Date column label is incorrect");
        softAssert.assertEquals(additionalPropertiesPage.getActualKeyExpirationDateColumnLabel(),dataLoader.getOutputData().get("expectedKeyExpirationDateColumnLabel"), "Key Expiration Date column label is incorrect");

        softAssert.assertFalse(additionalPropertiesPage.validateSaveExecuteMaintenanceButton(), "Bad behavior. Save and Execute Maintenance button is displayed.");
        tableRowCountBeforeKeyDownloaded = additionalPropertiesPage.getTableRowCountForUKGOwnedGCP();

        //Verify success message for download
        softAssert.assertEquals(additionalPropertiesPage.clickAddDownloadButtonAndGetSuccessMessage(),dataLoader.getOutputData().get("expectedFileDownloadSuccessMessage"),"File-I download success message is incorrect");
        seleniumHelper.waitTime(7000);// sleeping here because we need some time to let the first success message disappear
        softAssert.assertEquals(additionalPropertiesPage.clickAddDownloadButtonAndGetSuccessMessage(),dataLoader.getOutputData().get("expectedFileDownloadSuccessMessage"),"File-II download success message is incorrect");

        //additionalPropertiesPage.clickAddAndDownloadButton();
        tableRowCountAfterKeyDownloaded = additionalPropertiesPage.getTableRowCountForUKGOwnedGCP();
        softAssert.assertEquals(tableRowCountBeforeKeyDownloaded, tableRowCountAfterKeyDownloaded-2);

       softAssert.assertAll();
    }

    @Test(dataProvider="userTestDataProvider")
    public void verifyExpectedFileName(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        int lastRow=additionalPropertiesPage.getTableRowCountForUKGOwnedGCP();
        latestDownloadedFileName = additionalPropertiesPage.getFileNameFromUKGOwnedGCPTable(lastRow);
        latestFileFromDownloads = helper.getLatestFileNamefromDir(dataLoader.getOutputData().get("downloadPath"));
        softAssert.assertEquals(latestDownloadedFileName,latestFileFromDownloads, "Failed to download Expected document");

        softAssert.assertAll();
    }

    @Test
    public void verifyExpectedFileNameFormat(){
        SoftAssert softAssert = new SoftAssert();

        String projectIdFieldText =additionalPropertiesPage.getProjectIdFieldText();
        int lastRow=additionalPropertiesPage.getTableRowCountForUKGOwnedGCP();
        String firstTwelveCharOfServiceKey = additionalPropertiesPage.getFirstTwelveCharOfServiceKeyFromTable(lastRow);
        softAssert.assertEquals(latestDownloadedFileName,projectIdFieldText+"-"+firstTwelveCharOfServiceKey+".json", "File format is invalid.");

        //Verify Delete button on UKG owned GCP Seed admin page
        seedHomePage= additionalPropertiesPage.clickBackButton();
        softAssert.assertTrue(seedHomePage.verifyAdditionalPropertiesBtn(),"Click failed for back button");
        softAssert.assertAll();
    }

    @Test(dataProvider="userTestDataProvider", dependsOnMethods="generateServiceKeyFileTest")
    public void deleteServiceKeyFileTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);

        additionalPropertiesPage = seedHomePage.clickAdditionalPropertiesBtn();

        //Verify Delete pop appear and then press "X"--->pop should disappear
        //Verify success message for delete. Delete last entry first ( Got one issue in staging for this scenario )
        additionalPropertiesPage.clickDeleteButton(2);
        softAssert.assertEquals(additionalPropertiesPage.getModalDialogBoxContent(),dataLoader.getOutputData().get("expectedFileModalBoxMessage"),"Delete pop up content is not correct");
        additionalPropertiesPage.clickModalDialogCrossButton();

        //Verify Delete pop appear and then press "NO"--->pop should disappear
        additionalPropertiesPage.clickDeleteButton(2);
        additionalPropertiesPage.clickModalDialogNoButton();

        //Verify Delete pop appear and then press "Yes"---->pop should disappear and entry got deleted
        additionalPropertiesPage.clickDeleteButton(2);
        additionalPropertiesPage.clickModalDialogYesButton();
        softAssert.assertEquals(additionalPropertiesPage.GetSuccessMessageAfterYesButtonPressed(),dataLoader.getOutputData().get("expectedFileDeleteSuccessMessage"),"File-II delete success message is incorrect");

        seleniumHelper.waitTime(7000);// sleeping here because we need some time to let the first success message disappear

        //Verify Delete pop appear and then press "Yes"---->pop should disappear and entry got deleted
        additionalPropertiesPage.clickDeleteButton(1);
        additionalPropertiesPage.clickModalDialogYesButton();
        softAssert.assertEquals(additionalPropertiesPage.GetSuccessMessageAfterYesButtonPressed(),dataLoader.getOutputData().get("expectedFileDeleteSuccessMessage"),"File-I delete success message is incorrect");

        tableRowCountAfterKeyDeleted = additionalPropertiesPage.getTableRowCountForUKGOwnedGCP();
        softAssert.assertEquals(tableRowCountBeforeKeyDownloaded, tableRowCountAfterKeyDeleted);

        softAssert.assertAll();
    }
}
