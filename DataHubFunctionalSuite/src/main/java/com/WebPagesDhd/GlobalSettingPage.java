package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GlobalSettingPage {

    private MasterPage masterPage;
    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    public GlobalSettingPage(WebDriver driver) {
        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public void validateMaxPubSubLimit(String limit){
        seleniumHelper.validateElementPresent(driver, By.xpath("//td[normalize-space()='"+limit+"']"));
    }
}
