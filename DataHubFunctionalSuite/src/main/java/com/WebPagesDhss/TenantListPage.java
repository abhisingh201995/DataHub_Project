package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class TenantListPage extends basePage {

    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private By addTenantButton= By.xpath("//div[@title='New']//div//button[@aria-label='New']");
    private By editTenantButton= By.xpath("//div[@title='Edit']//div//button[@aria-label='Edit']");

    private By menuBar=By.xpath("//button[@automation-id='openCloseMenuHeaderButton']");
    private By signOutButton=By.xpath("//button[@title='Sign Out']");

    private By searchBar = By.xpath("//div[@col-id='tenantName']//span[@class='ag-icon ag-icon-menu']");

    private By searchTextBox = By.xpath("//input[@id='filterText']");
    private By additionalPropertiesBtn = By.xpath("//div[@class='icon-label']//span[contains(text(),'Additional Properties')]");

    private By refreshBtn = By.xpath("//span[@class='icon-k-loop']");


    public TenantListPage(WebDriver driver) {
        this.driver = driver;
        this.waitForPageToLoad(driver);
        Assert.assertFalse(ifAnyErrorSeen(driver),"Page loaded with error "+ this.getClass().getSimpleName());

    }

    public void selectTenantIdFromGrid(String tenantId){
        seleniumHelper.clickElement(driver,By.xpath("//div[normalize-space()='"+tenantId+"']"));
    }

    public void clickRefresh(){
        seleniumHelper.refreshAndLoadPage(driver);
    }

    public void scrollPageTillTenantFound(String tenantId){
        seleniumHelper.scrollToBottomOfPage(driver , By.xpath("//div[normalize-space()='"+tenantId+"']"));
    }

    public void searchTenant(String tenantName){
        seleniumHelper.clickElement(driver,searchBar);
        seleniumHelper.enterTextElement(driver,searchTextBox,tenantName);

        seleniumHelper.pressEnter(driver,searchTextBox);
    }

    public TenantRegistrationPage clickOnAddTenant(){
        seleniumHelper.clickElement(driver,addTenantButton);
        return new TenantRegistrationPage(driver);
    }

    public TenantRegistrationPage clickOnEditTenant(){
        seleniumHelper.clickElement(driver,editTenantButton);
        return new TenantRegistrationPage(driver);
    }

    public TMSAdminAdditionalProperties clickOnAdditionalProperties(){
        seleniumHelper.clickElement(driver, additionalPropertiesBtn);
        return new TMSAdminAdditionalProperties(driver);
    }

    public DhssLoginPage signOutAdminUser(){
        seleniumHelper.clickElement(driver,menuBar);
        seleniumHelper.clickElement(driver,signOutButton);
        return new DhssLoginPage(driver);
    }

}