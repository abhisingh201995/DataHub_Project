package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class TMSAdminAdditionalProperties extends basePage{

	private WebDriver driver;
	private SeleniumHelper seleniumHelper = new SeleniumHelper();
	private By VIEWPOINT_DD = By.xpath("//edap-smartcombo[@id='viewPointTenant']//input[@tabindex='-1']");
	private By UKG_PRO_CUSTOMER_DD = By.xpath("//edap-smartcombo[@id='ukgProCustomer']//input[@tabindex='-1']");
	private By DH_LICENSE_DD = By.xpath("//edap-smartcombo[@id='licensedCustomer']//input[@tabindex='-1']");
	private By GCP_PROJECT_UKG_OWNED_DD = By.xpath("//edap-smartcombo[@id='gcpUkgOwned']//input[@tabindex='-1']");
	private By SCRUBBED_DD = By.xpath("//edap-smartcombo[@id='scrubbed']//input[@tabindex='-1']");
	private By PERF_TIER_DD = By.xpath("//edap-smartcombo[@id='performanceTier']//input[@tabindex='-1']");
	private By TIMEZONE_DD = By.xpath("//edap-smartcombo[@id='timeZone']//input[@tabindex='-1']");
	private By SAVE_BTN = By.xpath("//button[normalize-space()='Save']");
	private By PROJECTID_DISABLED_TB = By.xpath("//input[@id='projectId' and @disabled]");
	private By GCP_PROJECTDESC_TEXTBOX = By.xpath("//input[@id='gcpProjectDesc']");
	private By PROV_STATUS_DISABLEDTB = By.xpath("//input[@id='provisioningStatus' and @disabled]");
	private By PROVISIONING_BTN = By.xpath("//button[normalize-space()='Provision GCP']");
	private By UPDATEPROVISIONING_BTN = By.xpath("//button[normalize-space()='Update GCP']");
	private By ADDPROPPAGE_TITLE = By.xpath("//span[@class='heading' and contains(text(), 'Additional Tenant Properties')]");
	private By CANCEL_BTN = By.xpath("//button[normalize-space()='Cancel']");
	private By UPDATE_BTN = By.xpath("//button[normalize-space()='Update']");
	private By SAVEBTN_DISABLED = By.xpath("//button[@id='btnSave' and @disabled]");
	private By PROVBTN_DISABLED = By.xpath("//button[@id=' btnGcpProvision' and @disabled]");
	private By TENANTVER = By.xpath("//div[normalize-space()='8.0.0' and @col-id='tenantVersion']");
	private By STARTPROVSTATUS = By.xpath("//div[normalize-space()='Started' and @col-id='upgradeStatus']");
	private By ERRORMSG = By.xpath("//div[contains(text(),'Enter a valid GCP Project Name.')]");
	private By HOME_BTN = By.xpath("//i[@class='item-icon icon-k-home']");
	private By REFRESH_BTN = By.xpath("//button[@title='Refresh']");
	private By HINTTEXT = By.xpath("//span[@class='hint-text' and contains(text(), 'Enter an alphanumeric value between 4 to 30 characters which can contain hyphen, single-quote, double-quote, space, and exclamation point.')]");

	public TMSAdminAdditionalProperties(WebDriver driver) {

		this.driver = driver;
		this.waitForPageToLoad(driver);
		Assert.assertFalse(ifAnyErrorSeen(driver),"Page loaded with error "+ this.getClass().getSimpleName());

	}

	public Boolean validatePageTitle() {
		return seleniumHelper.validateElementPresent(driver, this.ADDPROPPAGE_TITLE);
	}

	public void selectViewPointValue(int value){
		seleniumHelper.clickElement(driver, this.VIEWPOINT_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='viewPointTenant']//li["+value+"]"));
	}

	public void selectUkgProValue(int value){
		seleniumHelper.clickElement(driver, this.UKG_PRO_CUSTOMER_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='ukgProCustomer']//li["+value+"]"));
	}

	public void selectDatahubLicenseValue(int value){
		seleniumHelper.clickElement(driver, this.DH_LICENSE_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='licensedCustomer']//li["+value+"]"));
	}

	public void selectGcpOwnedValue(int value){
		seleniumHelper.clickElement(driver, this.GCP_PROJECT_UKG_OWNED_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='gcpUkgOwned']//li["+value+"]"));
	}

	public void selectScrubbedValue(int value) {
		seleniumHelper.clickElement(driver, this.SCRUBBED_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='scrubbed']//li["+value+"]"));
	}

	public void selectPerfTierValue(int value) {
		seleniumHelper.clickElement(driver, this.PERF_TIER_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='performanceTier']//li["+value+"]"));
	}

	public void selectTimezoneValue(int value) {
		seleniumHelper.clickElement(driver, this.TIMEZONE_DD);
		seleniumHelper.clickElement(driver, By.xpath("//edap-smartcombo[@id='timeZone']//li["+value+"]"));
	}

	public boolean isDisabledDatahubLicense(String value) {
		return seleniumHelper.validateElementPresent(driver, By.xpath("//edap-smartcombo[@id='licensedCustomer']//input[@title='"+value+"' and @disabled]"));
	}

	public boolean isDisabledViewpoint(String value) {
		return seleniumHelper.validateElementPresent(driver, By.xpath("//edap-smartcombo[@id='viewPointTenant']//input[@title='"+value+"' and @disabled]"));
	}

	public boolean isDisabledUkgPro(String value) {
		return seleniumHelper.validateElementPresent(driver, By.xpath("//edap-smartcombo[@id='ukgProCustomer']//input[@title='"+value+"' and @disabled]"));
	}

	public boolean isDisabledGcpOwned(String value) {
		return seleniumHelper.validateElementPresent(driver, By.xpath("//edap-smartcombo[@id='gcpUkgOwned']//input[@title='"+value+"' and @disabled]"));
	}

	public boolean isDisabledProjectId() {
		return seleniumHelper.validateElementPresent(driver, this.PROJECTID_DISABLED_TB);
	}

	public boolean isDisabledProvStatus() {
		return seleniumHelper.validateElementPresent(driver, this.PROV_STATUS_DISABLEDTB);
	}

	public void enterGcpProjectName(String projectDesc) {
		seleniumHelper.enterTextElement(driver, this.GCP_PROJECTDESC_TEXTBOX, projectDesc);

	}

	public boolean isErrorMessageDisplay() {
		return seleniumHelper.validateElementPresent(driver, this.ERRORMSG);
	}

	public void clickSaveBtn() {
		seleniumHelper.clickElement(driver, this.SAVE_BTN);
	}

	public void clickUpdateBtn() {
		seleniumHelper.clickElement(driver, this.UPDATE_BTN);
	}

	public void clickRefreshBtn() {
		seleniumHelper.clickElement(driver, this.REFRESH_BTN);
	}

	public void clickHomeBtn() {
		seleniumHelper.clickElement(driver, this.HOME_BTN);
	}

	public void clickProvisionGcpBtn() {
		seleniumHelper.clickElement(driver, this.PROVISIONING_BTN);
	}

	public void clickUpdateProvisionGcpBtn() {
		seleniumHelper.clickElement(driver, this.UPDATEPROVISIONING_BTN);
	}

	public void clickCancelBtn() {
		seleniumHelper.clickElement(driver, this.CANCEL_BTN);
	}

	public boolean isDisabledProvisionBtn() {
		return seleniumHelper.validateElementPresent(driver, this.PROVBTN_DISABLED);
	}

	public boolean isDisabledSaveBtn() {
		return seleniumHelper.validateElementPresent(driver, this.SAVEBTN_DISABLED);
	}

	public boolean isTenantVersionPresent() {
		return seleniumHelper.validateElementPresent(driver, this.TENANTVER);
	}
	public boolean isProvStatusStart() {
		return seleniumHelper.validateElementPresent(driver, this.STARTPROVSTATUS);
	}
	public boolean isHintTextDisplayed() {
		return seleniumHelper.validateElementPresent(driver, this.HINTTEXT);
	}

}