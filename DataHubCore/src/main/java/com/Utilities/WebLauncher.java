package com.Utilities;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class WebLauncher {

    public WebDriver driver;
    @BeforeMethod
    public void launchBrowser(){
        WebDriverFactory webDriverFactory = new WebDriverFactory();
        driver = webDriverFactory.initializeWebDriver(System.getenv("browser"), System.getenv("browserMode"));
    }

    @AfterMethod(alwaysRun = true)
    public void quitBrowser() {
        driver.quit();
    }
}
