package com.WebPagesDhss;

import com.FunctionalUtils.DHSSTenantHelper;
import com.Utilities.ConfigParser;
import com.Utilities.NgUtilities;
import com.Utilities.SeleniumHelper;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class DhssLoginPage extends basePage {

    private WebDriver driver;
    ConfigParser configParser = ConfigParser.getConfigParser();
    private NgWebDriver ngWebDriver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();
    private NgUtilities ngUtilities = new NgUtilities();
    private DHSSTenantHelper dhssTenantHelper = new DHSSTenantHelper();

    By username = By.xpath("//input[@id='username']");
    By password = By.xpath("//input[@id='password']");
    By loginButton = By.xpath("//button[@type='submit']");
    By seedSsoUsername = By.xpath("//input[@id='idToken1']");
    By seedSsoPwd = By.xpath("//input[@id='idToken2']");
    By seedSsoLogin = By.xpath("//input[@id='loginButton_0']");
    
    
    By fedSsoUsername = By.id("idToken1");
    By fedSsoPassword = By.id("idToken2");
    By fedSsoSubmitButton = By.id("loginButton_0");

    public DhssLoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitForPageToLoad(driver);
        Assert.assertFalse(ifAnyErrorSeen(driver),"Page loaded with error "+ this.getClass().getSimpleName());
    }

    public TenantListPage loginAdminUser(String url,String usr, String pwd){
        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+url);
        seleniumHelper.enterTextElement(driver,username,usr);
        seleniumHelper.enterTextElement(driver,password,pwd);
        seleniumHelper.clickElement(driver,loginButton);
        return new TenantListPage(driver);
    }

    public SeedHomePage loginSeedUser(String url, String tenantType){
        seleniumHelper.navigateToUrl(driver,configParser.inputParam.get(tenantType + ".baseUrlWebDhss")+url);
        seleniumHelper.enterTextElement(driver,username,configParser.inputParam.get(tenantType+".tenantSeedUser"));
        seleniumHelper.enterTextElement(driver,password,configParser.inputParam.get(tenantType+".password"));
        seleniumHelper.clickElement(driver,loginButton);
        dhssTenantHelper.refreshSeedAuthToken(tenantType);
        return new SeedHomePage(driver);
    }

    public TenantListPage doLoginAdminUser(){

        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+"admin/tenantadmin/adminbasiclogin");
        seleniumHelper.enterTextElement(driver,username,configParser.inputParam.get("conf.dhssAdminUsername"));
        seleniumHelper.enterTextElement(driver,password,configParser.inputParam.get("conf.dhssAdminPassword"));
        seleniumHelper.clickElement(driver,loginButton);
        dhssTenantHelper.refreshAuthToken();
        return new TenantListPage(driver);
    }

    public SeedHomePage doLoginSeedUser(String tenantType){
        dhssTenantHelper.refreshAuthToken();
        if(tenantType.equals("customerOwnedTenant")){
            dhssTenantHelper.activateCustomerOwnedTenant();
        }else if(tenantType.equals("ukgOwnedTenant")){
            dhssTenantHelper.activateUkgOwnedTenant();
        }
        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+"seedadmin/seed/seedlogin");
        seleniumHelper.enterTextElement(driver,username,configParser.inputParam.get(tenantType+".tenantSeedUser"));
        seleniumHelper.enterTextElement(driver,password,configParser.inputParam.get(tenantType+".password"));
        seleniumHelper.clickElement(driver,loginButton);
        return new SeedHomePage(driver);
    }

    public SeedHomePage doLoginSeedAdminSSOUser(String tenantType){
        dhssTenantHelper.refreshAuthToken();
        if(tenantType.equals("customerOwnedTenant")){
            dhssTenantHelper.activateCustomerOwnedTenant();
        }else if(tenantType.equals("ukgOwnedTenant")){
            dhssTenantHelper.activateUkgOwnedTenant();
        }
        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+"seedadminsso/seed/seedlogin");
        seleniumHelper.enterTextElement(driver,seedSsoUsername,configParser.inputParam.get(tenantType+".tenantSeedUser"));
        seleniumHelper.enterTextElement(driver,seedSsoPwd,configParser.inputParam.get(tenantType+".password"));
        seleniumHelper.clickElement(driver,seedSsoLogin);
        return new SeedHomePage(driver);
    }
    
    public SeedHomePage doLoginFederatedSeedAdminSSOUser(String tenantType){
		 dhssTenantHelper.refreshAuthToken();
		 if(tenantType.equals("customerOwnedTenant")){
		 dhssTenantHelper.activateCustomerOwnedTenant(); }else
		 if(tenantType.equals("ukgOwnedTenant")){
		 dhssTenantHelper.activateUkgOwnedTenant(); }

    	String url = configParser.inputParam.get(tenantType + ".fedTenantVanityUrl");
    	seleniumHelper.navigateToUrl(driver,url);
    	seleniumHelper.enterTextElement(driver,fedSsoUsername,configParser.inputParam.get(tenantType+".FedUser"));
    	seleniumHelper.enterTextElement(driver,fedSsoPassword,configParser.inputParam.get(tenantType+".password"));
    	seleniumHelper.clickElement(driver,fedSsoSubmitButton);
    	return new SeedHomePage(driver);
    }
    
    
}
