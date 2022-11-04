package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class SeedHomePage {

	private WebDriver driver;
	private SeleniumHelper seleniumHelper = new SeleniumHelper();
	private By SEEDSSOTENNAT_NOTREGISTERED_ERRORMESSAGE = By.id("errorMessageBodyContainer");
	private By SEEDTENNAT_NOTREGISTERED_ERRORMESSAGE = By.xpath("//span[@class='login-message']");
	private By TEXTFILED_APPKEY = By.id("appkey");
	private By TEXTFIELD_USERNAME = By.id("username");
	private By TEXTFIELD_PASSWORD = By.id("password");
	private By TEXTFIELD_CONFIRMPASSWORD = By.id("confirmpassword");
	private By BUTTON_UPDATE = By.id("btnRegister");
	private By BUTTON_ADDPROPERTY = By.id("btnEnternalNavgation");
	private By MSGWRAPPER = By.xpath("//div[contains(@class, 'edap-alert')]//span[contains(@class, 'msg-text')]");
	private By BUTTON_MSGWRAPPERCLOSE = By.xpath("//div[contains(@class, 'edap-alert')]//button[contains(@class, 'btn close')]");
	private By BUTTON_HamburgerMenu = By.xpath("//button[@automation-id='openCloseMenuHeaderButton']//span[@class='icon-k-menu']");
	private By lblSEED_TITLE = By.id("seed-configure-title");
	private By lblREQUIRED_INFO = By.xpath("//span[@id='seed-configure-title-required-info']/following-sibling::span");
	private By lblSEED_TENANT_ID = By.xpath("//label[@id='seed-configure-tenantId-label']//span");
	private By lblSEED_APPKEY = By.xpath("//label[@id='seed-configure-app-key-label']//span");
	private By lblSEED_WORKFORCE_DIMENSIONS_USERNAME = By.xpath("//label[@id='seed-configure-username-label']//span");
	private By lblSEED_WORKFORCE_DIMENSIONS_PASSWORD = By.xpath("//label[@id='seed-configure-password-label']//span");
	private By lblSEED_WORKFORCE_DIMENSIONS_CONFIRM_PASSWORD = By.xpath("//label[@id='seed-configure-confirmpassword-label']//span");
	private By lblSEED_APPKEY_REQUIRED_MESSAGE = By.id("seed-configure-app-key-required-message-label");
	private By lblSEED_WORKFORCE_DIMENSIONS_USERNAME_REQUIRED_MESSAGE = By.id("seed-configure-username-required-message-label");
	private By lblSEED_WORKFORCE_DIMENSIONS_PASSWORD_REQUIRED_MESSAGE = By.id("seed-configure-password-required-message-label");
	private By lblSEED_WORKFORCE_DIMENSIONS_CONFIRM_PASSWORD_REQUIRED_MESSAGE = By.id("seed-configure-confirmpassword-required-message-label");
	private By lblSEED_WORKFORCE_DIMENSIONS_CONFIRM_PASSWORD_MISMATCH_MESSAGE = By.id("seed-configure-confirm-password-mismatch-message-label");
	private By lblSEED_APPKEY_CONFIGURED_MESSAGE = By.xpath("//div[@class='multiple-lines-wrap']");
	private By SIGNOUT_MESSAGE = By.id("errorMessageBodyContainer");
	private By SESSION_INVALID_ERROR_MESSAGE = By.id("errorMessageBodyContainer");
	private By SAVE_SUCCESS_MESSAGE_LBL = By.xpath("//span[@class='msg-text']");

	private By btnMainMenu = By.xpath("//button[@automation-id='openCloseMenuHeaderButton']");
	private By lnkSignOut = By.xpath("//edap-nav-bar//button[@class='btn btn-signout']");
	private By txtSearchText = By.xpath("//input[@id=\"search_box\"]");
	private By lnkHome = By.xpath("//span[@id=\"home123_text\"]");
	private By txtnoMatchFoundText = By.xpath("//edap-nav-bar//div[@class='no-search-results']");

	//TODO -- > Correct the locator
	private By BUTTON_ADDITIONALPROPERTIES= By.id("btnEnternalNavgation");

	public SeedHomePage(WebDriver driver) {
		this.driver = driver;
	}

	public Boolean validatePageTitle() {
		Boolean pageTitle = seleniumHelper.validateElementPresent(driver, lblSEED_TITLE);
		Assert.assertTrue(pageTitle);
		return pageTitle;
	}

	public void saveConfigurationDetails(String TEXTFILED_APPKEY, String TEXTFIELD_USERNAME,
										String TEXTFIELD_PASSWORD, String TEXTFIELD_CONFIRMPASSWORD) {

		seleniumHelper.enterTextElement(driver, this.TEXTFILED_APPKEY, TEXTFILED_APPKEY);
		seleniumHelper.enterTextElement(driver, this.TEXTFIELD_USERNAME, TEXTFIELD_USERNAME);
		seleniumHelper.enterTextElement(driver, this.TEXTFIELD_PASSWORD, TEXTFIELD_PASSWORD);
		seleniumHelper.enterTextElement(driver, this.TEXTFIELD_CONFIRMPASSWORD, TEXTFIELD_CONFIRMPASSWORD);

	}

	public AdditionalPropertiesPage clickAdditionalPropertiesBtn() {
		seleniumHelper.clickElement(driver, BUTTON_ADDITIONALPROPERTIES);
		return new AdditionalPropertiesPage(driver);
	}

	public void clickUpdateBtn() {
		seleniumHelper.clickElement(driver, BUTTON_UPDATE);
	}

	public String saveSuccessLbl() {
		return seleniumHelper.getElementText(driver, SAVE_SUCCESS_MESSAGE_LBL);
	}

	public String validateAppKeyReqMsg() {
		return seleniumHelper.getElementText(driver, lblSEED_APPKEY_REQUIRED_MESSAGE);
	}

	public String validateUsrnmReqMsg() {
		return seleniumHelper.getElementText(driver, lblSEED_WORKFORCE_DIMENSIONS_USERNAME_REQUIRED_MESSAGE);
	}

	public String validatePwdReqMsg() {
		return seleniumHelper.getElementText(driver, lblSEED_WORKFORCE_DIMENSIONS_PASSWORD_REQUIRED_MESSAGE);
	}

	public String validateConfPwdReqMsg() {
		return seleniumHelper.getElementText(driver, lblSEED_WORKFORCE_DIMENSIONS_CONFIRM_PASSWORD_REQUIRED_MESSAGE);
	}

	public String validatePwdMismatchMsg() {
		return seleniumHelper.getElementText(driver, lblSEED_WORKFORCE_DIMENSIONS_CONFIRM_PASSWORD_MISMATCH_MESSAGE);
	}

	public DhssLoginPage signOutSeedUser(){
		seleniumHelper.clickElement(driver,lnkSignOut);
		return new DhssLoginPage(driver);
	}

}

