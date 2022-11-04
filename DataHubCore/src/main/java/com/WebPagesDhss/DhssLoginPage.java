package com.WebPagesDhss;

import com.FunctionalUtils.DHSSApiTenantHelper;
import com.Utilities.ConfigParser;
import com.Utilities.NgUtilities;
import com.Utilities.SeleniumHelper;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DhssLoginPage {

    private WebDriver driver;
    ConfigParser configParser = ConfigParser.getConfigParser();
    private NgWebDriver ngWebDriver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();
    private NgUtilities ngUtilities = new NgUtilities();
    private DHSSApiTenantHelper dhssTenantHelper = new DHSSApiTenantHelper(System.getenv("targetGcpDataProject"));

    By username = By.xpath("//input[@id='username']");
    By password = By.xpath("//input[@id='password']");

    By loginButton = By.xpath("//button[@type='submit']");

    public DhssLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public TenantListPage loginAdminUser(String url,String usr, String pwd){
        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+url);
        seleniumHelper.enterTextElement(driver,username,usr);
        seleniumHelper.enterTextElement(driver,password,pwd);
        seleniumHelper.clickElement(driver,loginButton);
        return new TenantListPage(driver);
    }

    public SeedHomePage loginSeedUser(String url, String usr, String pwd){
        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+url);
        seleniumHelper.enterTextElement(driver,username,usr);
        seleniumHelper.enterTextElement(driver,password,pwd);
        seleniumHelper.clickElement(driver,loginButton);
        dhssTenantHelper.refreshSeedAuthToken();
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

    public SeedHomePage doLoginSeedUser(){
        seleniumHelper.navigateToUrl(driver,System.getenv("baseUrlWebDhss")+"seedadmin/seed/seedlogin");
        seleniumHelper.enterTextElement(driver,username,configParser.inputParam.get("conf.dhssSeedUsername"));
        seleniumHelper.enterTextElement(driver,password,configParser.inputParam.get("conf.dhssSeedPassword"));
        seleniumHelper.clickElement(driver,loginButton);
        return new SeedHomePage(driver);
    }
}
