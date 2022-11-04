package com.Utilities;

import com.paulhammant.ngwebdriver.NgWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {
    public WebDriver driver;
    public NgWebDriver ngWebDriver;
    public JavascriptExecutor jsDriver;


    public static ThreadLocal<WebDriver> driverInstance = new ThreadLocal<>();
    public static ThreadLocal<NgWebDriver> ngWebDriverInstance = new ThreadLocal<>();

    public WebDriver initializeWebDriver(String browser, String mode){
        //TODO -- > Update when run in jenkins.
        String fileDownloadPath = "./";

        Map<String, Object> prefsMap = new HashMap<String, Object>();
        prefsMap.put("profile.default_content_settings.popups", 0);
        prefsMap.put("download.default_directory", fileDownloadPath);

        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if(mode.equals("headless")){
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--whitelisted-ips");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-extensions");
                options.setExperimentalOption("prefs", prefsMap);
                options.addArguments("--test-type");
                options.addArguments("--disable-extensions");
            }
            driverInstance.set(new ChromeDriver(options));
        } else if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if(mode.equals("headless")){
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--whitelisted-ips");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-extensions");
            }
            driverInstance.set(new FirefoxDriver(options));
        }

        getDriver().manage().deleteAllCookies();

        return getDriver();

    }

    public NgWebDriver initializeNgWebDriver(){
        jsDriver= (JavascriptExecutor) getDriver();
        ngWebDriver = new NgWebDriver(jsDriver);
        ngWebDriverInstance.set(ngWebDriver);

        return getNgWebDriver();
    }

    public static synchronized NgWebDriver getNgWebDriver(){
        return ngWebDriverInstance.get();
    }

    public static synchronized WebDriver getDriver(){
        return driverInstance.get();
    }

}
