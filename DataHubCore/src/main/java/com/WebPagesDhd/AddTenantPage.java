package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class AddTenantPage {

    private MasterPage masterPage;

    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private By pageHeader = By.xpath("//h1[normalize-space()='Add tenant']");

    //form field xpath
    private By dimensionTenantId= By.xpath("//input[@id='id_WFDTENANT']");
    private By tenantDescription= By.xpath("//input[@id='id_tenant_description']");
    private By bigQueryDatasetPrefix= By.xpath("//input[@id='id_GBQTENANT']");
    private By dataProject = By.xpath("//select[@id='id_DATAPROJECT']");
    private By wfdClientId = By.xpath("//input[@id='id_WFDCLIENTID']");
    private By wfdClientSecret  = By.xpath("//input[@id='id_WFDCLIENTSECRET']");
    private By wfdUser = By.xpath("//input[@id='id_WFDUSER']");
    private By wfdPassword = By.xpath("//input[@id='id_WFDPASSWORD']");
    private By wfdAppKey = By.xpath("//input[@id='id_WFDAPPKEY']");
    private By wfdVanityUrl= By.xpath("//input[@id='id_WFDVANITYURL']");
    private By scrubbed = By.xpath("//select[@id='id_SCRUBBED']");
    private By defaultUserType=By.xpath("//select[@id='id_defaultusertype']");
    private By ddva= By.xpath("//select[@id='id_ddva']");
    private By performanceTier= By.xpath("//select[@id='id_PERFORMANCE_TIER']");
    private By timeZone= By.xpath("//select[@id='id_TIMEZONE']");

    private By saveButton=By.xpath("//input[@name='_save']");
    private By successSaveNotification=By.xpath("//div[@class='alert alert-success']");
    private By saveNotification = By.xpath("//div[@role='alert']");

    private By wfdConnectivityButton =By.xpath("//button[normalize-space()='WFD Connectivity']");
    private By wfdConnectivityStatus =By.xpath("//span[@id='successbadge']");

    private By hyperFindConfigured = By.xpath("//button[normalize-space()='Hyperfind Configured']");
    private By hyperFindConfiguredStatus= By.xpath("//span[@id='successbadgehyper']");

    private By installAndExecuteMaintenanceButton=By.xpath("//button[normalize-space()='Install and Execute Maintenance']");


    public AddTenantPage(WebDriver driver) {

        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }

    public Boolean validatePageHeader(){
        Boolean status=seleniumHelper.validateElementPresent(driver,pageHeader);
        Assert.assertTrue(status);
        return status;
    }

    public void addTenant(String dimensionTenantId,
                     String tenantDescription,
                     String bigQueryDatasetPrefix,
                     Integer dataProject,
                     String wfdClientId,
                     String wfdClientSecret,
                     String wfdUser,
                     String wfdPassword,
                     String wfdAppKey,
                     String wfdVanityUrl,
                     String scrubbed,
                     String defaultUserType,
                     String ddva,
                     String performanceTier,
                     String timeZone){

        seleniumHelper.enterTextElement(driver,this.dimensionTenantId,dimensionTenantId);
        seleniumHelper.enterTextElement(driver,this.tenantDescription,tenantDescription);
        seleniumHelper.enterTextElement(driver,this.bigQueryDatasetPrefix,bigQueryDatasetPrefix);

        seleniumHelper.selectFromDropdown(driver,this.dataProject,dataProject);

        seleniumHelper.enterTextElement(driver,this.wfdClientId,wfdClientId);
        seleniumHelper.enterTextElement(driver,this.wfdClientSecret,wfdClientSecret);
        seleniumHelper.enterTextElement(driver,this.wfdUser,wfdUser);
        seleniumHelper.enterTextElement(driver,this.wfdPassword,wfdPassword);
        seleniumHelper.enterTextElement(driver,this.wfdAppKey,wfdAppKey);
        seleniumHelper.enterTextElement(driver,this.wfdVanityUrl,wfdVanityUrl);

        seleniumHelper.selectFromDropdown(driver,this.scrubbed,scrubbed);
        seleniumHelper.selectFromDropdown(driver,this.defaultUserType,defaultUserType);
        seleniumHelper.selectFromDropdown(driver,this.ddva,ddva);
        seleniumHelper.selectFromDropdown(driver,this.performanceTier,performanceTier);
        seleniumHelper.selectFromDropdown(driver,this.timeZone,timeZone);
    }

    public void clickSaveButton(){
        seleniumHelper.clickElement(driver,saveButton);
    }

    public void validateSuccessTenantSaveMessage(){
        seleniumHelper.validateElementPresent(driver,successSaveNotification);
    }

    public String getTenantCreateStatusMessage(){
        return seleniumHelper.getElementText(driver,saveNotification);
    }

    public void clickOnWfdConnectivity(){
        seleniumHelper.clickElement(driver, wfdConnectivityButton);
    }

    public Boolean checkWfdConnectivityStatus(){
        Boolean status=seleniumHelper.validateElementPresentLong(driver, wfdConnectivityStatus);
        return status;
    }

    public void clickOnHyperFindConfigured(){
        seleniumHelper.clickElement(driver, hyperFindConfigured);
    }

    public Boolean checkHyperFindConfiguredStatus(){
        Boolean status=seleniumHelper.validateElementPresentLong(driver, hyperFindConfiguredStatus);
        return status;
    }

    public TenantConfigurationPage clickOnInstallAndExecuteMaintainace(){
        seleniumHelper.clickElement(driver,installAndExecuteMaintenanceButton);
        return new TenantConfigurationPage(driver);
    }
}
