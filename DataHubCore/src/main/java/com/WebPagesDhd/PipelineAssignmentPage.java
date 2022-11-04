package com.WebPagesDhd;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class PipelineAssignmentPage {

    private MasterPage masterPage;

    private WebDriver driver;

    private SeleniumHelper seleniumHelper = new SeleniumHelper();


    public PipelineAssignmentPage(WebDriver driver) {
        this.driver = driver;
        masterPage=new MasterPage(driver);
    }

    public MasterPage getMasterPage() {
        return masterPage;
    }

    public void setMasterPage(MasterPage masterPage) {
        this.masterPage = masterPage;
    }

    private By addPipelineAssignment = By.xpath("//a[normalize-space()='Add pipelineassignments']");

    private By paWrapperDropdown = By.xpath("//select[@id='id_pa_wrapper']");
    private By paPipelineDropdown = By.xpath("//select[@id='id_pa_pipeline']");
    private By sequence = By.xpath("//input[@id='id_sequence']");
    private By enableDswt = By.xpath("//select[@id='id_enabledswt']");

    private By savePaAssignment = By.xpath("//input[@name='_save']");

    private By sucessMessage = By.xpath("//li[@class='success']");


    public void createPipelineAssignment(String wrapper, String pipeline, String dswt){
        seleniumHelper.selectFromDropdown(driver,paWrapperDropdown,wrapper);
        seleniumHelper.selectFromDropdown(driver,paPipelineDropdown,pipeline);
        seleniumHelper.enterTextElement(driver,sequence,"100");
        seleniumHelper.selectFromDropdown(driver,enableDswt,dswt);
        seleniumHelper.clickElement(driver,savePaAssignment);
        seleniumHelper.validateElementPresent(driver,sucessMessage);
    }

    public List<String> getListOfWrappers(){
        return seleniumHelper.getListOfElementsInDropDown(driver, paWrapperDropdown);
    }

    public void clickOnAddPipelineAssignment(){
        seleniumHelper.clickElement(driver,addPipelineAssignment);
    }



}
