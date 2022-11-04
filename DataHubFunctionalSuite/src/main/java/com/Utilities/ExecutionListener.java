package com.Utilities;

import com.Tests.ParentTests.BaseConfiguration;
import com.alm.apiobject.AlmAPIObject;
import com.alm.apitest.BaseAlmAPITest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.jayway.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ExecutionListener extends BaseConfiguration implements ITestListener {

    private final ConfigParser configParser = ConfigParser.getConfigParser();

    String dateTime= LocalDateTime.now().toString().replace(":","").replace(".","");
    String fileName=dateTime+".html";
    String filePath="./TestExecutionReport/Report_";
    private TestStatus testStatus;
    private ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(filePath+ fileName);
    private ExtentReports extentReports;
    private ExtentTest extentTest;
    private static ThreadLocal<ExtentTest> extent_test = new ThreadLocal<ExtentTest>();

    private HashMap<String, String > almTestStatus= new HashMap<>();

    Helper helper = new Helper();


    public void onStart(ITestContext iTestContext) {
        //skip
        Log.info("Test has started");
        extentHtmlReporter.config().setDocumentTitle("Data Hub Automated Tests");
        extentHtmlReporter.config().setReportName("Data Hub Automated Tests");
        extentReports = new ExtentReports();
        extentReports.attachReporter(extentHtmlReporter);
    }

    public void onTestStart(ITestResult iTestResult) {
        this.testStatus = new TestStatus();

        //Extent Report

        extentTest=extentReports.createTest(iTestResult.getMethod().getMethodName());
        extent_test.set(extentTest);


    }

    public void onTestSuccess(ITestResult iTestResult) {
        if(configParser.inputParam.get("conf.publishToDashboard").equals("yes")) {
            this.sendStatus(iTestResult,"PASS");
        }
        extent_test.get().log(Status.PASS,iTestResult.getMethod().getMethodName());


        almTestStatus.put(dataPool.getTestcaseMapping().get(iTestResult.getTestClass().getName()+"."+iTestResult.getMethod().getMethodName()),
                "Passed");

    }

    public void onTestFailure(ITestResult iTestResult) {
        if(configParser.inputParam.get("conf.publishToDashboard").equals("yes")) {
            this.sendStatus(iTestResult,"FAIL");
        }
        extent_test.get().log(Status.FAIL,iTestResult.getMethod().getMethodName());

        if(System.getenv("browser").equals("api")){
            Log.error("Test case has failed due to below exception" + iTestResult.getThrowable());
            extent_test.get().fail(iTestResult.getThrowable());
        }else {
            String screenshotFile = failedScreenshot();
            try {
                Log.error("Test case has failed due to below exception" + iTestResult.getThrowable().getMessage());
                extent_test.get().fail(iTestResult.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotFile).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        almTestStatus.put(dataPool.getTestcaseMapping().get(iTestResult.getTestClass().getName()+"."+iTestResult.getMethod().getMethodName()),
                "Failed");


    }

    public void onTestSkipped(ITestResult iTestResult) {
        if(configParser.inputParam.get("conf.publishToDashboard").equals("yes")) {
            this.sendStatus(iTestResult,"SKIPPED");
        }
        extent_test.get().log(Status.SKIP,iTestResult.getMethod().getMethodName());


    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        //skip
    }


    public void onFinish(ITestContext iTestContext) {
        //skip

        extentReports.flush();
        int passed,failed,skipped;

        failed=iTestContext.getFailedTests().getAllResults().size();
        passed=iTestContext.getPassedTests().getAllResults().size();
        skipped=iTestContext.getSkippedTests().getAllResults().size();

        float passPercent;
        passPercent=passed*100/(passed+failed+skipped);

        gcpHelper.uploadObject(configParser.inputParam.get("conf.gcpBaseProject"),configParser.inputParam.get("conf.testResultBucket"),fileName,filePath+ fileName);
        String gcpFileLoc="https://storage.cloud.google.com/qa-test-report/"+fileName;

        String message= "{'text':'Automated Test Executed successfully, Pass percentage= "+passPercent+"%, Pass count= "+passed+" , Failed count= "+failed+" , skipped count = "+skipped+" , view detailed report on "+gcpFileLoc+"'}";
        helper.sendMessageOnTeams(message);

        //publishing result to ALM

        if(configParser.inputParam.get("conf.publishToAlm").equals("yes")) {

            BaseAlmAPITest baseAlmAPITest = new BaseAlmAPITest();
            baseAlmAPITest.loginToALM();

            AlmAPIObject almApiObject = new AlmAPIObject();
            Response response = almApiObject.getTestCaseStatusForGivenTestSetId(System.getenv("AlmTestSetId"));

            ArrayList<HashMap> data = (response.getBody().path("entities"));

            almTestStatus.forEach((almTestId,status)->{

                data.forEach(item->{
                    ((ArrayList<HashMap>)item.get("Fields")).forEach(field->{
                        boolean flag;
                        if(field.get("Name").equals("test-id")){
                            if(field.get("values").equals(almTestId)){
                                flag=true;
                            }
                        }
                        if(flag=true) {
                            if (field.get("Name").equals("id")) {
                                Response response1 = almApiObject.updateTestCaseStatus((String) ((HashMap)((ArrayList)field.get("values")).get(0)).get("value"), status);
                            }
                        }
                    });
                });


            });

        }

    }


    private void sendStatus(ITestResult iTestResult, String status){
        this.testStatus.setTestClass(iTestResult.getMethod().getMethodName());
        this.testStatus.setDescription(iTestResult.getMethod().getDescription());
        this.testStatus.setStatus(status);
        this.testStatus.setExecutionDate(LocalDateTime.now().toString());
        this.testStatus.setRelease(System.getenv("Release"));
        this.testStatus.setModuleName(iTestResult.getTestClass().getName());
        this.testStatus.setBuildNumber(dataPool.getDateTime());
        //todo add jenkins build number later.
        if(iTestResult.getThrowable()==null){
            this.testStatus.setLog("Test executed successfully");
        }else {
            this.testStatus.setLog(iTestResult.getThrowable().getMessage());
        }
        ResultSender.send(this.testStatus);
    }
}