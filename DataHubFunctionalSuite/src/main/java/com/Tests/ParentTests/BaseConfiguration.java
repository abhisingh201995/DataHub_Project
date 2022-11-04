package com.Tests.ParentTests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import com.FunctionalUtils.DHDTenantHelper;
import com.FunctionalUtils.DHSSTenantHelper;
import com.Utilities.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.*;

import javax.sql.DataSource;


public class BaseConfiguration {

    public Logger log;
    public final DataPool dataPool = DataPool.getDataPool();
    public final GcpHelper gcpHelper = new GcpHelper();
    public final Helper helper=new Helper();
    public final ConfigParser configParser = ConfigParser.getConfigParser();
    public final DHSSTenantHelper dhssTenantHelper = new DHSSTenantHelper();
    public final DHDTenantHelper dhdTenantHelper = new DHDTenantHelper();
    //public final PostgresDBHelper postgresDBHelper = new PostgresDBHelper();
    public final JsonSchemaReader jsonSchemaReader = new JsonSchemaReader();

    public WebDriver driver;


    @DataProvider(name="userTestDataProvider")
    public Object[][] userTestDataProvider(Method m){
        String testDataKey=this.getClass().getSimpleName()+"."+m.getName();
        JSONArray testJson = dataPool.getTestDataPool().get(testDataKey);
        return helper.testNgProvider(testJson);
    }


    @BeforeSuite
    public void loadData(){
        try {
            helper.loadTestData();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @BeforeClass(alwaysRun = true)
    public void launchBrowser() {
        log = Logger.getLogger(BaseConfiguration.class);
        String className = this.getClass().getName();
        if(className.contains("ApiServices")){

        }else {// achauhan: if browser env var value is "" or "api", then dont do any launch browser
            if (!(System.getenv("browser").equals("")||(System.getenv("browser").equals("api")))) {
                WebDriverFactory webDriverFactory = new WebDriverFactory();
                driver = webDriverFactory.initializeWebDriver(System.getenv("browser"), System.getenv("browserMode"));
            }
        }

    }

    @AfterClass(alwaysRun = true)
    public void quitBrowser() {
        String className = this.getClass().getName();
        if(className.contains("ApiServices")){

        }else { // achauhan: if browser env var value is "" or "api", then dont do any quit browser
        	if (!(System.getenv("browser").equals("")||(System.getenv("browser").equals("api")))) {
                driver.quit();
            }
        }
    }



    @AfterMethod
    public void createFailedCaseList(ITestResult test){
        if(test.getStatus()==ITestResult.FAILURE){
            //Code to add failed cases name



        }
    }

    public String failedScreenshot(){
        File scrFile= ((TakesScreenshot)WebDriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
        String fileName=System.currentTimeMillis()+".png";
        String path="./TestScreenshots/"+fileName;
        try{
            FileUtils.copyFile(scrFile, new File(path));
        }catch (IOException e){
            e.printStackTrace();
        }
        gcpHelper.uploadObject(configParser.inputParam.get("conf.gcpBaseProject"),configParser.inputParam.get("conf.testResultBucket"),fileName,path);

        return "https://storage.cloud.google.com/qa-test-report/"+fileName;
    }
}
