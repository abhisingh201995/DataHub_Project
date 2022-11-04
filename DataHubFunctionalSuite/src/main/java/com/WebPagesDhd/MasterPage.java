package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MasterPage {

    SeleniumHelper seleniumHelper = new SeleniumHelper();
    WebDriver driverUi;

    public MasterPage(WebDriver driverUi) {
        this.driverUi = driverUi;
    }

    private By homePageLink = By.xpath("//a[normalize-space()='Home']");

    private By logOut = By.xpath("//a[normalize-space()='Log out']");
    private By logInAgain = By.xpath("//a[normalize-space()='Log in again']");


    public HomePage clickHomePage(){
        seleniumHelper.clickElement(driverUi,this.homePageLink);
        return new HomePage(driverUi);
    }

    public LoginPage clickLogOut(){
        seleniumHelper.clickElement(driverUi,logOut);
        seleniumHelper.clickElement(driverUi,logInAgain);
        return new LoginPage(driverUi);
    }
}
