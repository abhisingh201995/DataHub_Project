package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.TMSAdminAdditionalProperties;
import com.WebPagesDhss.TenantListPage;
import com.WebPagesDhss.TenantRegistrationPage;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Test(groups = "TMSAdminAdditionalPropertiesUiTests")
public class TMSAdminAdditionalPropertiesUITest extends BaseConfiguration {

    private TMSAdminAdditionalProperties tmsAdminAdditionalProperties;
    private DhssLoginPage dhssLoginPage;
    private TenantListPage tenantListPage;
    private TenantRegistrationPage tenantRegistrationPage;
    List<HashMap<String, Object>> sqlResponse = new ArrayList<>();
    String tenantName;

    @BeforeClass
    public void doLogin(){
        dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        tenantListPage = dhssLoginPage.doLoginAdminUser();
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateSaveAdditionalPropertiesViewpointYes(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tenantListPage.clickRefresh();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.validatePageTitle(), "Page title not present");
        tmsAdminAdditionalProperties.selectViewPointValue(2);
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "UKG owned dropdown is enabled");
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(3);
        tmsAdminAdditionalProperties.isDisabledSaveBtn();
        tmsAdminAdditionalProperties.selectTimezoneValue(2);
        tmsAdminAdditionalProperties.clickSaveBtn();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint dropdown is enabled");
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateUpdateAdditionalPropertiesViewPointYes(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is JointCustomer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP owned dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint dropdown is enabled");
        tmsAdminAdditionalProperties.selectScrubbedValue(3);
        tmsAdminAdditionalProperties.selectPerfTierValue(2);
        tmsAdminAdditionalProperties.selectTimezoneValue(4);
        tmsAdminAdditionalProperties.clickUpdateBtn();
        dhssTenantHelper.deleteTenant(tenantName);
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateSaveAdditionalPropertiesViewpointNoJointCustYes(final JSONObject testData) throws InterruptedException {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tmsAdminAdditionalProperties.clickHomeBtn();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(3);
        tmsAdminAdditionalProperties.selectUkgProValue(2);
        tmsAdminAdditionalProperties.selectDatahubLicenseValue(2);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint tenant dropdown is enabled");
        tmsAdminAdditionalProperties.selectGcpOwnedValue(2);
        tmsAdminAdditionalProperties.selectScrubbedValue(3);
        tmsAdminAdditionalProperties.selectPerfTierValue(4);
        tmsAdminAdditionalProperties.selectTimezoneValue(2);
        tmsAdminAdditionalProperties.clickSaveBtn();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP Project UKG owned is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledProjectId(), "Project ID is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledProvStatus(), "Provisioning status is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isHintTextDisplayed(), "Hint text not present");
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectName"));
//        tmsAdminAdditionalProperties.clickProvisionGcpBtn();
//        Thread.sleep(5000);
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isProvStatusStart(), "Provision status not present");
//        tmsAdminAdditionalProperties.clickRefreshBtn();
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateUpdateAdditionalPropertiesViewPointNoJointCustYes(final JSONObject testData) throws InterruptedException {
        DataLoader dataLoader = new DataLoader(testData);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP owned dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint dropdown is enabled");
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(3);
        tmsAdminAdditionalProperties.selectTimezoneValue(3);
        tmsAdminAdditionalProperties.clickUpdateBtn();
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectName"));
//        tmsAdminAdditionalProperties.clickUpdateProvisionGcpBtn();
//        Thread.sleep(50000);
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isProvStatusStart(), "Provision status not present");
//        tmsAdminAdditionalProperties.clickRefreshBtn();
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
        dhssTenantHelper.deleteTenant(tenantName);
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateSaveAdditionalPropertiesViewpointNoJointCustYesDhNo(final JSONObject testData) throws InterruptedException {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tmsAdminAdditionalProperties.clickHomeBtn();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(3);
        tmsAdminAdditionalProperties.selectUkgProValue(2);
        tmsAdminAdditionalProperties.selectDatahubLicenseValue(3);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint tenant dropdown is enabled");
        tmsAdminAdditionalProperties.selectGcpOwnedValue(2);
        tmsAdminAdditionalProperties.selectScrubbedValue(3);
        tmsAdminAdditionalProperties.selectPerfTierValue(4);
        tmsAdminAdditionalProperties.selectTimezoneValue(2);
        tmsAdminAdditionalProperties.clickSaveBtn();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledProjectId(), "Project ID is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledProvStatus(), "Provisioning status is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isHintTextDisplayed(), "Hint text not present");
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectName"));
//        tmsAdminAdditionalProperties.clickProvisionGcpBtn();
//        Thread.sleep(5000);
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isProvStatusStart(), "Provision status not present");
//        tmsAdminAdditionalProperties.clickRefreshBtn();
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
        dhssTenantHelper.deleteTenant(tenantName);
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateSaveAdditionalPropertiesViewpointNoJointCustYesDhYes(final JSONObject testData) throws InterruptedException {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tmsAdminAdditionalProperties.clickHomeBtn();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(3);
        tmsAdminAdditionalProperties.selectUkgProValue(2);
        tmsAdminAdditionalProperties.selectDatahubLicenseValue(2);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint tenant dropdown is enabled");
        tmsAdminAdditionalProperties.selectGcpOwnedValue(3);
        tmsAdminAdditionalProperties.selectScrubbedValue(3);
        tmsAdminAdditionalProperties.selectPerfTierValue(4);
        tmsAdminAdditionalProperties.selectTimezoneValue(2);
        tmsAdminAdditionalProperties.clickSaveBtn();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "Is GCP UKG owned dropdown is enabled");
        dhssTenantHelper.deleteTenant(tenantName);
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateSaveAdditionalPropertiesViewpointNoUkgProNoGcpOwnedNo(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tmsAdminAdditionalProperties.clickHomeBtn();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(3);
        tmsAdminAdditionalProperties.selectUkgProValue(3);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint Tenant dropdown is enabled");
        tmsAdminAdditionalProperties.selectGcpOwnedValue(3);
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(3);
        tmsAdminAdditionalProperties.selectTimezoneValue(6);
        tmsAdminAdditionalProperties.clickSaveBtn();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP owned dropdown is enabled");
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateUpdateAdditionalPropertiesViewPointNoUkgProNo(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP owned dropdown is enabled");
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(3);
        tmsAdminAdditionalProperties.selectTimezoneValue(3);
        tmsAdminAdditionalProperties.clickUpdateBtn();
        dhssTenantHelper.deleteTenant(tenantName);

    }

    @Test(dataProvider="userTestDataProvider")
    public void validateSaveAdditionalPropertiesViewpointNoUkgProNoGcpOwnedYes(final JSONObject testData) throws InterruptedException {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tmsAdminAdditionalProperties.clickHomeBtn();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(3);
        tmsAdminAdditionalProperties.selectUkgProValue(3);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint Tenant dropdown is enabled");
        tmsAdminAdditionalProperties.selectGcpOwnedValue(2);
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(2);
        tmsAdminAdditionalProperties.selectTimezoneValue(5);
        tmsAdminAdditionalProperties.clickSaveBtn();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP owned dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledProjectId(), "Project ID is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledProvStatus(), "Provisioning status is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isHintTextDisplayed(), "Hint text not present");
        tmsAdminAdditionalProperties.isDisabledProvisionBtn();
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectName"));
//        tmsAdminAdditionalProperties.clickProvisionGcpBtn();
//        Thread.sleep(50000);
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isProvStatusStart(), "Provision status not present");
//        tmsAdminAdditionalProperties.clickRefreshBtn();
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateUpdateAdditionalPropertiesViewpointNoUkgProNoGcpOwnedYes(final JSONObject testData) throws InterruptedException {
        DataLoader dataLoader = new DataLoader(testData);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint Tenant dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "GCP owned dropdown is enabled");
        tmsAdminAdditionalProperties.selectScrubbedValue(3);
        tmsAdminAdditionalProperties.selectPerfTierValue(3);
        tmsAdminAdditionalProperties.selectTimezoneValue(4);
        tmsAdminAdditionalProperties.clickUpdateBtn();
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectName"));
//        tmsAdminAdditionalProperties.clickUpdateProvisionGcpBtn();
//        Thread.sleep(50000);
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isProvStatusStart(), "Provision status not present");
//        tmsAdminAdditionalProperties.clickRefreshBtn();
//        softAssert.assertTrue(tmsAdminAdditionalProperties.isTenantVersionPresent(), "Tenant Version not present");
        dhssTenantHelper.deleteTenant(tenantName);
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateCancelAdditionalProperties(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantName = dhssTenantHelper.createTenant();
        tmsAdminAdditionalProperties.clickHomeBtn();
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(2);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledUkgPro(dataLoader.getTestParameter().get("isUkgProCustomer")), "Is Joint Customer dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledGcpOwned(dataLoader.getTestParameter().get("isGcpUkgOwned")), "UKG owned dropdown is enabled");
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(3);
        tmsAdminAdditionalProperties.selectTimezoneValue(2);
        tmsAdminAdditionalProperties.clickCancelBtn();
    }

    @Test(dataProvider="userTestDataProvider")
    public void validateErrorWithInvalidProjectName(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        tenantListPage.searchTenant(tenantName);
        tenantListPage.selectTenantIdFromGrid(tenantName);
        tmsAdminAdditionalProperties = tenantListPage.clickOnAdditionalProperties();
        tmsAdminAdditionalProperties.selectViewPointValue(3);
        tmsAdminAdditionalProperties.selectUkgProValue(3);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledDatahubLicense(dataLoader.getTestParameter().get("isDataHubLicensed")), "Data hub license dropdown is enabled");
        softAssert.assertTrue(tmsAdminAdditionalProperties.isDisabledViewpoint(dataLoader.getTestParameter().get("isViewpointTenant")), "Viewpoint Tenant dropdown is enabled");
        tmsAdminAdditionalProperties.selectGcpOwnedValue(2);
        tmsAdminAdditionalProperties.selectScrubbedValue(2);
        tmsAdminAdditionalProperties.selectPerfTierValue(2);
        tmsAdminAdditionalProperties.selectTimezoneValue(5);
        tmsAdminAdditionalProperties.clickSaveBtn();
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectNameMin"));
        softAssert.assertTrue(tmsAdminAdditionalProperties.isErrorMessageDisplay(), "Error message does not display");
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectNameMax"));
        softAssert.assertTrue(tmsAdminAdditionalProperties.isErrorMessageDisplay(), "Error message does not display");
        tmsAdminAdditionalProperties.enterGcpProjectName(dataLoader.getTestParameter().get("projectNameInvalid"));
        softAssert.assertTrue(tmsAdminAdditionalProperties.isErrorMessageDisplay(), "Error message does not display");
        dhssTenantHelper.deleteTenant(tenantName);
    }

}