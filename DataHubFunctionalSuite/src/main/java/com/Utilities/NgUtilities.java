package com.Utilities;

import com.paulhammant.ngwebdriver.ByAngularModel;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.WebDriver;

public class NgUtilities {

    public void enterTextToNgElement(WebDriver driver, NgWebDriver ngWebDriver, String data, ByAngularModel element){
        driver.findElement(element).clear();
        driver.findElement(element).sendKeys(data);
        ngWebDriver.waitForAngularRequestsToFinish();
    }


}
