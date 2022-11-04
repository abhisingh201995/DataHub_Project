package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class TenantRegistrationPage extends basePage{

    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private By pageHeader = By.xpath("//h1[normalize-space()='Add tenant']");

    private By TEXTFIELD_TNTNAME= By.id("tenantName");
    private By TEXTFIELD_TNTSRTNAME= By.id("shortName");
    private By TEXTFIELD_TNTVANITYURL=By.id("tenantVanityURL");
    private By TEXTFIELD_TNTAUTHURL= By.id("OpenAMURL");
    private By TEXTFIELD_TNTWFDURL= By.id("WFDURL");
    private By TEXTFIELD_TNTCLNTID= By.id("ClientId");
    private By TEXTFIELD_TNTCLNTSCRT=By.id("ClientSecret");
    private By DROPDOWN_TNTLOCL=By.xpath("//edap-smartcombo[@id='localePolicy']//input[1]");
    private By DROPDOWN_TNTLOCLVAL= By.xpath("//edap-smartcombo[@id='localePolicy']//li[1]");
    private By DROPDOWN_TNTENV=By.xpath("//edap-smartcombo[@id='environment']//input");
    private By DROPDOWN_TNTENVVAL=By.xpath("//span[@title='Production']");
    private By TEXTFIELD_TNTSOL= By.xpath("//input[@id='solution']");
    private By TEXTFIELD_TNTSEED= By.xpath("//input[@id='tenantSeedUser']");
    private By BUTTON_TNTRGSTR= By.id("btnRegister");
    private By MSGWRAPPER= By.xpath("//div[contains(@class, 'edap-alert')]//span[contains(@class, 'msg-text')]");
    private By BUTTON_MSGWRAPPERCLOSE=By.xpath("//div[contains(@class, 'edap-alert')]//button[contains(@class, 'btn close')]");
    private By FORM_ERRORMESSAGES= By.xpath("//small[@class='error error-required']/div");
    private By successSaveNotification=By.xpath("//div[@class='alert alert-success']");
    private By saveNotification = By.xpath("//div[@role='alert']");
    private By successUpdateNotification=By.xpath("//div[@class='alert alert-success']");
    private By updateNotification = By.xpath("//div[@role='alert']");

    private By ssoCheckbox = By.xpath("//label[@class='edap-checkbox sso-checkbox']/i[@class='edap-checkbox-icon']");
    private By dimensionFedratedUrlTextBox= By.id("tenantSSOURL");


    public TenantRegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.waitForPageToLoad(driver);
        Assert.assertFalse(ifAnyErrorSeen(driver),"Page loaded with error "+ this.getClass().getSimpleName());

    }

    public Boolean validatePageHeader(){
        Boolean status=seleniumHelper.validateElementPresent(driver,pageHeader);
        Assert.assertTrue(status);
        return status;
    }

    public void addDHSSTenant(String TEXTFIELD_TNTNAME,
                          String TEXTFIELD_TNTSRTNAME,
                          String TEXTFIELD_TNTVANITYURL,
                          String TEXTFIELD_TNTAUTHURL,
                          String TEXTFIELD_TNTWFDURL,
                          String TEXTFIELD_TNTCLNTID,
                          String TEXTFIELD_TNTCLNTSCRT,
                          String TEXTFIELD_TNTSOL,
                          String TEXTFIELD_TNTSEED
                          ,String fedrated,String fedratedUrl){

        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTNAME,TEXTFIELD_TNTNAME);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTSRTNAME,TEXTFIELD_TNTSRTNAME);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTVANITYURL,TEXTFIELD_TNTVANITYURL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTAUTHURL,TEXTFIELD_TNTAUTHURL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTWFDURL,TEXTFIELD_TNTWFDURL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTCLNTID,TEXTFIELD_TNTCLNTID);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTCLNTSCRT,TEXTFIELD_TNTCLNTSCRT);
        seleniumHelper.clickElement(driver,this.DROPDOWN_TNTLOCL);
        seleniumHelper.clickElement(driver,this.DROPDOWN_TNTLOCLVAL);
        seleniumHelper.clickElement(driver, this.DROPDOWN_TNTENV);
        seleniumHelper.clickElement(driver,this.DROPDOWN_TNTENVVAL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTSOL,TEXTFIELD_TNTSOL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTSEED,TEXTFIELD_TNTSEED);

        if(fedrated.equals("true")){
            seleniumHelper.clickElement(driver,this.ssoCheckbox);
            seleniumHelper.enterTextElement(driver,dimensionFedratedUrlTextBox,fedratedUrl);
        }
    }

    public void updateDHSSTenant(String TEXTFIELD_TNTNAME,
                              String TEXTFIELD_TNTVANITYURL,
                              String TEXTFIELD_TNTAUTHURL,
                              String TEXTFIELD_TNTWFDURL,
                              String TEXTFIELD_TNTCLNTID,
                              String TEXTFIELD_TNTCLNTSCRT,
                              String TEXTFIELD_TNTSOL,
                              String TEXTFIELD_TNTSEED
    ){

        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTNAME,TEXTFIELD_TNTNAME);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTVANITYURL,TEXTFIELD_TNTVANITYURL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTAUTHURL,TEXTFIELD_TNTAUTHURL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTWFDURL,TEXTFIELD_TNTWFDURL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTCLNTID,TEXTFIELD_TNTCLNTID);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTCLNTSCRT,TEXTFIELD_TNTCLNTSCRT);
        seleniumHelper.clickElement(driver,this.DROPDOWN_TNTLOCL);
        seleniumHelper.clickElement(driver,this.DROPDOWN_TNTLOCLVAL);
        seleniumHelper.clickElement(driver, this.DROPDOWN_TNTENV);
        seleniumHelper.clickElement(driver,this.DROPDOWN_TNTENVVAL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTSOL,TEXTFIELD_TNTSOL);
        seleniumHelper.enterTextElement(driver,this.TEXTFIELD_TNTSEED,TEXTFIELD_TNTSEED);
    }

    public void clickRegisterButton(){ seleniumHelper.clickElement(driver,BUTTON_TNTRGSTR);
    }

    public String getTenantCreateStatusMessage(){
        return seleniumHelper.getElementText(driver,saveNotification);
    }

    public void validateSuccessTenantSaveMessage(){ seleniumHelper.validateElementPresent(driver,successSaveNotification);
    }

    public String getTenantUpdateStatusMessage(){
        return seleniumHelper.getElementText(driver,updateNotification);
    }

    public void validateSuccessTenantUpdateMessage(){ seleniumHelper.validateElementPresent(driver,successUpdateNotification);
    }


    public void getTenantFailedStatusMessage(){ seleniumHelper.getElementText(driver,MSGWRAPPER);
    }

    public void validateFailureTenantErrorMessage(){ seleniumHelper.validateElementPresent(driver,FORM_ERRORMESSAGES);
    }

    public void closeTenantFailedStatusMessageAlert(){ seleniumHelper.clickElement(driver,BUTTON_MSGWRAPPERCLOSE);
    }
}
