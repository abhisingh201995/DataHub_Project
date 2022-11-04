package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class TenantConfigurationPage {

    private MasterPage masterPage;

    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();


    private By addTenantButton= By.xpath("//a[normalize-space()='Add tenant']");
    private By actionDropDown= By.xpath("//select[@name='action']");
    private By pageHeader=By.xpath("//h1[normalize-space()='Select tenant to change']");
    private By homePageLink = By.xpath("//a[normalize-space()='Home']");
    private By deleteTenantButton = By.xpath("//a[normalize-space()='Delete']");

    private By deleteTenantBigQueryTableRadioButton = By.xpath("//input[@id='choiceBigQuery']");
    private By deleteTenantWebsiteDataRadioButton = By.xpath("//input[@id='choiceWebsite']");

    private By deleteTenantFinalButton = By.xpath("//button[normalize-space()='Delete Tenant']");

    public TenantConfigurationPage(WebDriver driver) {

        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }

    public void selectTenantIdFromGrid(String tenantId){
        seleniumHelper.clickElement(driver,By.xpath("//input[@value='"+tenantId+"']"));
    }

    public void selectAction(String action){
        seleniumHelper.selectFromDropdown(driver,actionDropDown,action);
    }

    public AddTenantPage clickOnAddTenant(){
        seleniumHelper.clickElement(driver,addTenantButton);
        return new AddTenantPage(driver);
    }

    public HomePage clickOnHomePage(){
        seleniumHelper.clickElement(driver,homePageLink);
        return new HomePage(driver);
    }

    public Boolean validatePageHeader(){
        Boolean status=seleniumHelper.validateElementPresent(driver,pageHeader);
        Assert.assertTrue(status);
        return status;
    }


    public HomePage deleteTenantBigQueryTableData(String tenantName){
        seleniumHelper.clickElement(driver,By.xpath("//a[normalize-space()='"+tenantName+"']"));
        seleniumHelper.clickElement(driver,deleteTenantButton);
        seleniumHelper.clickElement(driver,deleteTenantBigQueryTableRadioButton);
        seleniumHelper.clickElement(driver,deleteTenantFinalButton);
        return new HomePage(driver);
    }

    public HomePage deleteTenantWebsiteData(String tenantName){
        seleniumHelper.clickElement(driver,By.xpath("//a[normalize-space()='"+tenantName+"']"));
        seleniumHelper.clickElement(driver,deleteTenantButton);
        seleniumHelper.clickElement(driver,deleteTenantWebsiteDataRadioButton);
        seleniumHelper.clickElement(driver,deleteTenantFinalButton);
        return new HomePage(driver);
    }




}
