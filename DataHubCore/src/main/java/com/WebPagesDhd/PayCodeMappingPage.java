package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PayCodeMappingPage {

    private MasterPage masterPage;

    private WebDriver driver;


    public PayCodeMappingPage(WebDriver driver) {

        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private By pageHeader = By.xpath("//h1[normalize-space()='Select paycodemapping to change']");
    private By actionDropDown = By.xpath("//select[@name='action']");


    public Boolean validatePageHeader(){
        return seleniumHelper.validateElementPresent(driver,pageHeader);
    }

    public Boolean validateTenantInFilterGrid(String tenant){
        return seleniumHelper.validateElementPresent(driver,By.xpath("//a[@title='"+tenant+"']"));
    }


}
