package com.WebPagesDhss;

import com.Utilities.NgUtilities;
import com.Utilities.SeleniumHelper;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.ByAngularButtonText;
import com.paulhammant.ngwebdriver.ByAngularModel;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SampleAngularPage {

    private WebDriver driver;
    private NgWebDriver ngWebDriver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();
    private NgUtilities ngUtilities = new NgUtilities();



    //Angular Web Element

    ByAngularModel element = ByAngular.model("//model name replace");
    ByAngularModel element2 = ByAngular.model("//model name replace");

    ByAngularButtonText sampleButton = ByAngular.buttonText("//button text");

    //HTML web element

    By sample1 = By.xpath("//xpath locater");

    //constructor to initialize selenium and Ng webdriver
    public SampleAngularPage(WebDriver driver, NgWebDriver ngWebDriver) {
        this.driver = driver;
        this.ngWebDriver = ngWebDriver;
    }

    // sample page interaction

    public void fillAngularForm(ByAngularModel element, String text){
        ngUtilities.enterTextToNgElement(driver,ngWebDriver,text,element);
    }
}
