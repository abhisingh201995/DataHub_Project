package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private MasterPage masterPage;

    private WebDriver driver;

    SeleniumHelper seleniumHelper = new SeleniumHelper();

    // 1. By Locators: OR
    private By username = By.name("username");
    private By password = By.name("password");
    private By logInButton = By.xpath("//input[@value='Log in']");


    // 2. Constructor of the page class:
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }

    // 3. page actions: features(behavior) of the page the form of methods:

    public String getLoginPageTitle() {
        return driver.getTitle();
    }

    public HomePage doLoginUser(String usr, String pwd) {
        driver.get(System.getenv("baseUrl")+"admin/");
        seleniumHelper.enterTextElement(driver,username,usr);
        seleniumHelper.enterTextElement(driver,password,pwd);
        seleniumHelper.clickElement(driver,logInButton);
        return new HomePage(driver);
    }

    public HomePage doLoginUser() {
        driver.get(System.getenv("baseUrl")+"admin/");
        seleniumHelper.enterTextElement(driver,username,System.getenv("username"));
        seleniumHelper.enterTextElement(driver,password,System.getenv("password"));
        seleniumHelper.clickElement(driver,logInButton);
        return new HomePage(driver);
    }

}
