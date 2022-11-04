package com.WebPagesDhss;

import com.Utilities.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class SeedAdminAdditionalProperties extends basePage{

	private WebDriver driver;
	private SeleniumHelper seleniumHelper = new SeleniumHelper();
	private By PROJECT_ID_TEXTBOX = By.xpath("//input[@id='projectId']");
	private By GCP_PROJECT_NAME_TXTBX = By.xpath("//input[@id='gcpProjectDesc']");
	private By ACC_KEY_TXTBX = By.xpath("//input[@id='serviceAccountKey']");
	private By SAVEXECUTE_BTN = By.xpath("//button[normalize-space()='Save and Execute Maintenance']");
	private By UPDATEXECUTE_BTN = By.xpath("//button[normalize-space()='Update and Execute Maintenance']");
	private By REFRESH_BTN = By.xpath("//button[normalize-space()='Refresh']");
	private By TENANT_ID_LBL = By.xpath("//div[@col-id='edapTenantId' and @comp-id='22']");
	private By TENANT_VER_LBL = By.xpath("//div[@col-id='tenantVersion' and @comp-id='23']");
	private By TENANT_STATUS_LBL = By.xpath("//div[@col-id='upgradeStatus' and @comp-id='24']");
	private By TENANT_UPDATED_LBL = By.xpath("//div[@col-id='updated_dtm' and @comp-id='25']");
	private By ADDPROP_TITLE = By.xpath("//span[@class='heading' and contains(text(), 'Additional Tenant Properties')]");
	private By SAVEBTNDISABLED = By.xpath("//button[normalize-space()='Save and Execute Maintenance' and @disabled]");
    private By PROJECT_IDDISABLED = By.xpath("//input[@id='projectId' and @disabled]");

    
    // FIELD VALIDATRION ERROR ELEMENTS...achauhan
    private By FLD_VALIDATION_PRJCT_DESC = By.xpath("//input[@id='gcpProjectDesc']/..//div[@class='validation-errors']");
    private By FLD_VALIDATION_PRJCT_DESC_HINT = By.xpath("//input[@id='gcpProjectDesc']/..//span[@class='hint-text']");
   
    private By FLD_VALIDATION_SVC_ACC_KEY = By.xpath("//input[@id='serviceAccountKey']/..//small[@class='error error-required']");
//    private By FLD_VALIDATION_SVC_ACC_KEY = By.xpath("//input[@id='serviceAccountKey']/..//div[@class='validation-errors']");
    private By FLD_VALIDATION_SVC_ACC_KEY_HINT = By.xpath("//input[@id='serviceAccountKey']/..//span[@class='hint-text']");
    
    
    private By BCK_BUTTON = By.xpath("//button[contains(text(),'Cancel')]");
    
    private String TABLE = "//div[@role='row']";
    private String TABLE_ROW_0 = "//div[@role='row'][@row-index=0]";
    private String TABLE_COLUMN_STATUS = "//div[@col-id='upgradeStatus']";
    
    private By TABLE_ROW_0_COLUMN_STATUS = By.xpath(TABLE_ROW_0+TABLE_COLUMN_STATUS);
    
    
//    public static final String ERR_PRJCT_NAME_EMPTY = "Enter the Project Description";
    public static final String ERR_PRJCT_NAME_EMPTY = "Project Name required.";
    public static final String ERR_PRJCT_NAME_INVALID = "Please enter a valid Project Name.";
    public static final String ERR_SVC_ACC_KEY_INVALID = "Please enter a valid Service Account Key.";
    
    
//    public static final String HINT_SVC_ACC_KEY = "Enter the valid key format.";
    public static final String HINT_SVC_ACC_KEY = "Enter a valid Service Account Key JSON format.";
    public static final String HINT_PRJCT_NAME = "Enter an alphanumeric value between 4 to 30 characters which can contain hyphen, single-quote, double-quote, space, and exclamation point.";
    
    
    
    
    
    
    
	public SeedAdminAdditionalProperties(WebDriver driver) {
		this.driver = driver;
		this.waitForPageToLoad(driver);
		Assert.assertFalse(ifAnyErrorSeen(driver),"Page loaded with error "+ this.getClass().getSimpleName());

	}

	public Boolean validatePageTitle() {
		return seleniumHelper.validateElementPresent(driver, this.ADDPROP_TITLE);
	}

	public void saveAdditionalProperties(String PROJECT_ID_TEXTBOX, String GCP_PROJECT_NAME_TXTBX,
										String ACC_KEY_TXTBX) {
		seleniumHelper.enterTextElement(driver, this.PROJECT_ID_TEXTBOX, PROJECT_ID_TEXTBOX);
		seleniumHelper.enterTextElement(driver, this.GCP_PROJECT_NAME_TXTBX, GCP_PROJECT_NAME_TXTBX);
		seleniumHelper.enterTextElement(driver, this.ACC_KEY_TXTBX, ACC_KEY_TXTBX);

	}

	public Boolean validateSaveBtnDisabled() {
		return seleniumHelper.validateElementPresent(driver, this.SAVEBTNDISABLED);
	}

	public void updateAdditionalProperties(String GCP_PROJECT_NAME_TXTBX,
										 String ACC_KEY_TXTBX) {
		seleniumHelper.enterTextElement(driver, this.GCP_PROJECT_NAME_TXTBX, GCP_PROJECT_NAME_TXTBX);
		seleniumHelper.enterTextElement(driver, this.ACC_KEY_TXTBX, ACC_KEY_TXTBX);
	}

	public void clickSaveBtn() {
		seleniumHelper.clickElement(driver, this.SAVEXECUTE_BTN);
	}
	
	public SeedAdminAdditionalProperties clickUpdateBtn() {
		seleniumHelper.clickElement(driver, this.UPDATEXECUTE_BTN);
		return new SeedAdminAdditionalProperties(driver);
	}
	
	
	public String getSuccessMessageOnSaveUpdateBtnPress() {
		
		return this.getSuccessTextMessage(driver);
	}
	
	public boolean isClickUpdateBtnClickable() {
		
		// if button is readOnly, then return false for Clickable
		return !seleniumHelper.isReadOnlyField(driver, this.UPDATEXECUTE_BTN);
	}
	
	public SeedHomePage clickBackButton() {
		
		seleniumHelper.clickElement(driver, BCK_BUTTON);
		return new SeedHomePage(driver);
	}
	
	//achauhan
	public SeedAdminAdditionalProperties clearProjectName() {
		
		  seleniumHelper.clearTextBackSpace(driver, GCP_PROJECT_NAME_TXTBX);
		  return new SeedAdminAdditionalProperties(driver);
	}
	
	//achauhan
		public SeedAdminAdditionalProperties clearAccKey() {
			
			  seleniumHelper.clearTextBackSpace(driver, ACC_KEY_TXTBX);
			  return new SeedAdminAdditionalProperties(driver);
		}
		
		
	//achauhan
	public SeedAdminAdditionalProperties fillProjectDesc(String text) {
		seleniumHelper.enterTextElement(driver, GCP_PROJECT_NAME_TXTBX, text);
		return new SeedAdminAdditionalProperties(driver);
	}
	
	//achauhan
		public SeedAdminAdditionalProperties fillServiceAcKey(String text) {
			seleniumHelper.enterTextElement(driver, ACC_KEY_TXTBX, text);
			return new SeedAdminAdditionalProperties(driver);
		}
		
	
	// achauhan
	public String returnProjectNameValidationError() {
		
	//	seleniumHelper.clickElement(driver, ACC_KEY_TXTBX);
		String err= seleniumHelper.getElementText(driver, FLD_VALIDATION_PRJCT_DESC);
		System.out.println(err);
		return err;
	}
	
// achauhan
	public String returnAccSvcKeyValidationError() {
		
		String err= seleniumHelper.getElementText(driver, FLD_VALIDATION_SVC_ACC_KEY);
		System.out.println(err);
		return err;
	}
	
	
	// achauhan
	public String returnAccSvcKeyHintText() {
		
		String txt= seleniumHelper.getElementText(driver, FLD_VALIDATION_SVC_ACC_KEY_HINT);
		System.out.println(txt);
		return txt;
	}
	
	
	// achauhan
	public String returnProjectDescHintText() {
		
		String txt= seleniumHelper.getElementText(driver, FLD_VALIDATION_PRJCT_DESC_HINT);
		System.out.println(txt);
		return txt;
	}
	
	
	//achauhan
	public boolean anyErrorSeen() {
		
		return this.ifAnyErrorSeen(driver);
	}
	

	//achauhan
	public String returnTableStatusColumn() {
		
		String text = seleniumHelper.getElementText(driver, TABLE_ROW_0_COLUMN_STATUS);
		System.out.println(text);
		return text;
	}
	
	//achauhan
	public boolean logOut() {
		
		return this.doLogout(driver);
	}
	
	//achauhan
		public String returnTableStatusColumnAndRefres() {
			
			String text = this.returnTableStatusColumn();
			this.clickRefreshBtn();
			return text;
		}
		
		public String waitForMinutesUntilStatusIsComplete(int minutes) {
			
			int waitTime = minutes*60*1000;// seconds
			int timeLapsed = 0;
			
			while (!(this.returnTableStatusColumn().equals("Completed") || timeLapsed>waitTime)) {
			
				this.returnTableStatusColumnAndRefres();timeLapsed=timeLapsed+5000;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.returnTableStatusColumn().equals("Failed")){
					break;
				}
				
			}
			
			
			//new WebDriverWait(driver, 60*minutes, 60000).until(ExpectedConditions.textToBePresentInElementValue(TABLE_ROW_0_COLUMN_STATUS, "Completed"));
			return this.returnTableStatusColumn();
			
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
	
	
	public void clickRefreshBtn() {
		seleniumHelper.clickElement(driver, this.REFRESH_BTN);
	}

	public Boolean validateProjectIdDisabled() {
		return seleniumHelper.isReadOnlyField(driver, this.PROJECT_ID_TEXTBOX);
	}

	public Boolean validateTenantId() {
		return seleniumHelper.validateElementPresent(driver, this.TENANT_ID_LBL);
	}
	public Boolean validateTenantVersion() {
		return seleniumHelper.validateElementPresent(driver, this.TENANT_VER_LBL);
	}
	public Boolean validateTenantStatus() {
		return seleniumHelper.validateElementPresent(driver, this.TENANT_STATUS_LBL);
	}
	public Boolean validateTenantUpdatedOn() {
		return seleniumHelper.validateElementPresent(driver, this.TENANT_UPDATED_LBL);
	}
}

