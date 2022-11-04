package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

public class HomePage {

    private MasterPage masterPage;

    //page Elements

    private By tenantConfigurationButton = By.xpath("//a[normalize-space()='Tenant Configuration']");
    private By payCodeMappingButton = By.xpath("//a[normalize-space()='Paycode Mapping']");
    private By locationTypeMappingButton = By.xpath("//a[normalize-space()='Location Type Mapping']");
    private By wrapperButton = By.xpath("//a[normalize-space()='Wrappers']");
    private By pipelineAssignmentButton = By.xpath("//a[normalize-space()='Pipeline Assignments']");
    private By globalSetting = By.xpath("//a[normalize-space()='Global Settings']");

    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private HashMap<String, String> links = new HashMap<>();
    public HashMap<String, String> getLinks() {
        return links;
    }

    public HomePage(WebDriver driver) {
        this.driver = driver;
        masterPage=new MasterPage(driver);

        links.put("Log out", "Log out");
        links.put("Tenant Configuration", "Tenant Configuration");
        links.put("Tenant Pipeline Settings", "Tenant Pipeline Settings");
        links.put("Tenant Versions", "Tenant Versions");
        links.put("Paycode Mapping", "Paycode Mapping");
        links.put("Location Type Mapping", "Location Type Mapping");
        links.put("Custom Driver Mapping", "Custom Driver Mapping");
        links.put("Wrapper Scheduler", "Wrapper Scheduler");
        links.put("Job Status", "Job Status");
        links.put("Wrappers", "Wrappers");
        links.put("Pipelines", "Pipelines");
        links.put("Add tenant", "Add tenant");
        links.put("Add tenant pipeline settings", "Add tenant pipeline settings");

    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }


    public void clickLinkButton(String linkName) {
        seleniumHelper.clickElement(driver, By.xpath("//a[normalize-space()='" + linkName + "']"));
    }

    public TenantConfigurationPage clickOnTenantConfigurationButton() {
        seleniumHelper.clickElement(driver, tenantConfigurationButton);
        return new TenantConfigurationPage(driver);
    }

    public PayCodeMappingPage clickOnPayCodeMappingButton() {
        seleniumHelper.clickElement(driver, payCodeMappingButton);
        return new PayCodeMappingPage(driver);
    }

    public LocationTypeMappingPage clickOnLocationTypeMappingButton() {
        seleniumHelper.clickElement(driver, locationTypeMappingButton);
        return new LocationTypeMappingPage(driver);
    }

    public Boolean checkElementPresent(String linkName) {
        Boolean status = false;
        status = seleniumHelper.validateElementPresent(driver, By.xpath("//a[normalize-space()='" + linkName + "']"));
        Assert.assertTrue(status, "Expected element is not present - " + linkName);
        return status;
    }

    public List<WebElement> getAllLinkButton() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        return links;
    }

    public WrappersPage clickOnWrapperButton(){
        seleniumHelper.clickElement(driver,wrapperButton);
        return new WrappersPage(driver);
    }

    public PipelineAssignmentPage clickOnPipelineAssignmentButton(){
        seleniumHelper.clickElement(driver,pipelineAssignmentButton);
        return new PipelineAssignmentPage(driver);
    }

    public GlobalSettingPage clickOnGlobalSetting(){
        seleniumHelper.clickElement(driver,globalSetting);
        return new GlobalSettingPage(driver);

    }


   /* public Boolean validatePageHeader(){
        Boolean status=seleniumHelper.validateElementPresent(driver,//input);
        return status;
    }
*/


}
