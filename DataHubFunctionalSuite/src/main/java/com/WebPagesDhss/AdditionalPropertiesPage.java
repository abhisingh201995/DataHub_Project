package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class AdditionalPropertiesPage extends basePage {
    private WebDriver driver;
    private SeleniumHelper seleniumHelper = new SeleniumHelper();

    private By TEXTFIELD_PROJECTID= By.id("projectId");
    private By TEXTFIELD_GCPPROJECTNAME= By.id("gcpProjectDesc");
    private By PROJECTID_LBL = By.xpath("//span[normalize-space()='Project id']");
    private By GCPPROJECTNAME_LBL = By.xpath("//span[normalize-space()='GCP Project Name']");
    private By FILENAME_LBL = By.xpath("(//div[@col-id='fileName'])[1]");
    private By STATUS_LBL = By.xpath("(//div[@col-id='status'])[1]");
    private By KEY_LBL = By.xpath("(//div[@col-id='key'])[1]");
    private By KEYCREATIONDATE_LBL = By.xpath("(//div[@col-id='keyCreationDate'])[1]");
    private By KEYEXPIRATIONDATE_LBL = By.xpath("(//div[@col-id='keyExpirationDate'])[1]");
    private By BUTTON_ADDANDDOWNLOAD= By.cssSelector("button[title='Add and Download Key']");
    private By SAVEXECUTE_BTN = By.xpath("//button[normalize-space()='Save and Execute Maintenance']");
    private By ADDPROP_TITLE = By.xpath("//span[@class='heading']");
    private By BCK_BUTTON = By.xpath("//button[contains(text(),'Cancel')]");
    private By MODAL_DIALOG_BOX_CONTENT = By.xpath("//div[contains(text(),' You are about to delete the Service Account Key. Do you want to proceed?')]");
    private By MODAL_DIALOG_BOX_CROSSBTN = By.xpath("//div[@id='Dhss_Modal_Delete_Service_Account_Key']//i[@class='icon-k-close edap-modal-close']");
    private By MODAL_DIALOG_BOX_NOBTN = By.xpath("//button[@class='btn btn-default edap-dialog-button btnDialogNo']");
    private By MODAL_DIALOG_BOX_YESBTN = By.xpath("//button[@class='btn btn-primary edap-dialog-button btnDialogYes']");

    public AdditionalPropertiesPage(WebDriver driver) {
        this.driver = driver;
        this.waitForPageToLoad(driver);
        Assert.assertFalse(ifAnyErrorSeen(driver),"Page loaded with error "+ this.getClass().getSimpleName());

    }

    public Boolean validatePageTitle(String tenantName) {
        seleniumHelper.validateElementPresent(driver, this.ADDPROP_TITLE);

        Boolean flag= false;
        //todo optimize the implementation
        if(seleniumHelper.getElementText(driver,this.ADDPROP_TITLE).contains(tenantName)==true){
            if(seleniumHelper.getElementText(driver,this.ADDPROP_TITLE).contains("Additional Tenant Properties")==true){
                flag= true;
            }

        }else{
            flag= false;
        }
        return flag;

    }

    public boolean validateProjectIdField(){
        return seleniumHelper.isReadOnlyField(driver,TEXTFIELD_PROJECTID );
    }

    public String getProjectIdFieldText(){
        return seleniumHelper.getElementAttribute(driver,TEXTFIELD_PROJECTID);
    }

    public String getActualFileNameColumnLabel() {
        return seleniumHelper.getElementText(driver, this.FILENAME_LBL);
    }

    public String getActualStatusColumnLabel() {
        return seleniumHelper.getElementText(driver, this.STATUS_LBL);
    }

    public String getActualKeyColumnLabel() {
        return seleniumHelper.getElementText(driver, this.KEY_LBL);
    }

    public String getActualKeyCreationDateColumnLabel() {
        return seleniumHelper.getElementText(driver, this.KEYCREATIONDATE_LBL);
    }

    public String getActualKeyExpirationDateColumnLabel() {
        return seleniumHelper.getElementText(driver, this.KEYEXPIRATIONDATE_LBL);
    }
    public String getActualProjectIdLabel() {
        return seleniumHelper.getElementText(driver,this.PROJECTID_LBL);
    }

    public String getActualGCPProjectNameLabel() {
        return seleniumHelper.getElementText(driver, this.GCPPROJECTNAME_LBL);
    }


    public boolean validateGCPProjectNameField(){
        return seleniumHelper.isReadOnlyField(driver,TEXTFIELD_GCPPROJECTNAME );
    }

    public boolean validateSaveExecuteMaintenanceButton(){
           return seleniumHelper.validateElementPresent(driver, SAVEXECUTE_BTN);
    }

    public boolean validateAddAndDownloadButton(){return seleniumHelper.validateElementPresent(driver, BUTTON_ADDANDDOWNLOAD);

    }

    public void clickAddAndDownloadButton(){ seleniumHelper.clickElement(driver,BUTTON_ADDANDDOWNLOAD);
    }

    public String clickAddDownloadButtonAndGetSuccessMessage(){
        this.clickAddAndDownloadButton();
        return this.getSuccessTextMessage(driver);

    }

    public SeedHomePage clickBackButton() {

        seleniumHelper.clickElement(driver, BCK_BUTTON);
        return new SeedHomePage(driver);
    }

    public String getModalDialogBoxContent(){return seleniumHelper.getElementText(driver,MODAL_DIALOG_BOX_CONTENT);
    }

    public void clickModalDialogCrossButton(){ seleniumHelper.clickElement(driver,MODAL_DIALOG_BOX_CROSSBTN);
    }

    public void clickModalDialogNoButton(){ seleniumHelper.clickElement(driver,MODAL_DIALOG_BOX_NOBTN);
    }

    public void clickModalDialogYesButton(){ seleniumHelper.clickElement(driver,MODAL_DIALOG_BOX_YESBTN);
    }

    public void clickDeleteButton(int key){
        //seleniumHelper.refreshAndLoadPage(driver);
        seleniumHelper.clickElement(driver,By.xpath("(//a[@class='cell_button'])["+key+"]"));
    }

    public String GetSuccessMessageAfterYesButtonPressed(){
        return this.getSuccessTextMessage(driver);

    }

    public int  getTableRowCountForUKGOwnedGCP(){
        int key=1;
        Boolean status=true;
        while(status==true) {
           status = seleniumHelper.validateElementPresent(driver, By.xpath("(//a[@class='cell_button'])[" + key + "]"));
           key++;
        }
        return key-2;
    }

    public String getFileNameFromUKGOwnedGCPTable(int key){
        return seleniumHelper.getElementText(driver,By.xpath("(//div[@col-id='fileName'])[" + (key+1) + "]"));
    }

    public String getFirstTwelveCharOfServiceKeyFromTable(int key){
        String str =  seleniumHelper.getElementText(driver,By.xpath("(//div[@col-id='key'])[" + (key+1) + "]") );
        return str.substring(0,12);
    }
}
