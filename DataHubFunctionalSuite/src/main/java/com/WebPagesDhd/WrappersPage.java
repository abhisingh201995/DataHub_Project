package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class WrappersPage {

    private MasterPage masterPage;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();


    private WebDriver driver;

    public WrappersPage(WebDriver driver) {
        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }

    private By addWrapperButton = By.xpath("//a[normalize-space()='Add wrapper']");
    private By homePageLink = By.xpath("//a[normalize-space()='Home']");

    private By wrapperNameTextBox = By.xpath("//input[@id='id_wrapper_name']");
    private By selectTenantDropDown = By.xpath("//select[@id='id_wrapper_tenant']");
    private By saveWrapper = By.xpath("//input[@name='_save']");

    private By wrapperStatus = By.xpath("//li[@class='success']");

    private By actionDropdown = By.xpath("//select[@name='action']");
    private By actionGoButton = By.xpath("//button[normalize-space()='Go']");

    private By deleteConfirmationButton = By.xpath("//input[@value='Yes, I’m sure']");

    private By operationSuccessMessage = By.xpath("//li[@class='success']");
    private By operationFailedMessage = By.xpath("//li[@class='error']");

    public void clickAddWrapperButton(){
        seleniumHelper.clickElement(driver,addWrapperButton);
    }

    public void createNewWrapper(String wrapper, String tenant){
        seleniumHelper.enterTextElement(driver,wrapperNameTextBox,wrapper);
        seleniumHelper.selectFromDropdown(driver,selectTenantDropDown,tenant);
        seleniumHelper.clickElement(driver,saveWrapper);
        seleniumHelper.validateElementPresent(driver,wrapperStatus);
    }


    public void selectWrapperCheckBox(String wrapper){
        seleniumHelper.clickElement(driver,By.xpath("//input[@value='"+wrapper+"']"));
    }

    public void selectAndGoActionForSelectedWrapper(String operation){
        seleniumHelper.selectFromDropdown(driver,actionDropdown,operation);
        seleniumHelper.clickElement(driver,actionGoButton);
    }

    public List<String> getListOfTenants(){
        return seleniumHelper.getListOfElementsInDropDown(driver, selectTenantDropDown);
    }

    public void clickDeleteConfirmationSuccess(){
        //seleniumHelper.clickElement(driver,deleteConfirmationButton);
        seleniumHelper.validateElementPresent(driver,operationSuccessMessage);
    }

    public void clickDeleteConfirmationFailed(){
        //seleniumHelper.clickElement(driver,deleteConfirmationButton);
        seleniumHelper.validateElementPresent(driver,operationFailedMessage);
    }




}
