package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceUiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.Utilities.WebDriverFactory;
import com.WebPagesDhss.DhssLoginPage;
import com.WebPagesDhss.SeedAdminAdditionalProperties;
import com.WebPagesDhss.SeedHomePage;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Test(groups = "DHSS_UI_Service_Key_File_Tests")
public class CustomerOwnedGcpSeedAdditionalPropertiesUITest extends BaseConfiguration {

    private DhssLoginPage dhssLoginPage;
    private SeedAdminAdditionalProperties seedAdminAdditionalProperties;
    private SeedHomePage seedHomePage;

    @BeforeMethod
    public void doLogin(){
    	dhssLoginPage = new DhssLoginPage(WebDriverFactory.getDriver());
        seedHomePage = dhssLoginPage.doLoginSeedUser("customerOwnedTenant");
    }
    

   
  /****  
    // @Test(dataProvider="userTestDataProvider")
    public void validateSaveSeedAdditionalPropertiesTest(final JSONObject testData) {
        DataLoader dataLoader = new DataLoader(testData);
        SoftAssert softAssert = new SoftAssert();
        seedAdminAdditionalProperties = seedHomePage.clickAdditionalPropertiesButton();
        softAssert.assertTrue(seedAdminAdditionalProperties.validatePageTitle(), "Page title not present");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateSaveBtnDisabled(), "Save button enabled");
        seedAdminAdditionalProperties.saveAdditionalProperties(
                dataLoader.getTestParameter().get("gcpProjectId"),
                dataLoader.getTestParameter().get("gcpProjectName"),
                dataLoader.getTestParameter().get("serviceAccKey")
        );
        seedAdminAdditionalProperties.clickSaveBtn();
        seedAdminAdditionalProperties.clickRefreshBtn();
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantId(), "");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantVersion(), "");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantStatus(), "");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantUpdatedOn(), "");
        softAssert.assertAll();
    }

    // we have env limitation, we cannot do CREATE tests as discussed with Jeff and anupam.
   // @Test(dataProvider="userTestDataProvider", dependsOnMethods = "validateSaveSeedAdditionalPropertiesTest")
    public void validateUpdateSeedAdditionalPropertiesTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        seedAdminAdditionalProperties = seedHomePage.clickAdditionalPropertiesButton();
        softAssert.assertTrue(seedAdminAdditionalProperties.validatePageTitle(), "Page title not present");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateSaveBtnDisabled(), "Save button enabled");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateProjectIdDisabled(), "ProjectID box enabled");
        seedAdminAdditionalProperties.updateAdditionalProperties(
                dataLoader.getTestParameter().get("gcpProjectName"),
                dataLoader.getTestParameter().get("serviceAccKey")
        );
        seedAdminAdditionalProperties.clickSaveBtn();
        seedAdminAdditionalProperties.clickRefreshBtn();
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantId(), "");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantVersion(), "");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantStatus(), "");
        softAssert.assertTrue(seedAdminAdditionalProperties.validateTenantUpdatedOn(), "");
        softAssert.assertAll();
    }

   // @Test(dataProvider="userTestDataProvider", dependsOnMethods = "validateSaveSeedAdditionalPropertiesTest")
    public void validateErrorWithInvalidServiceAccKeyTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        seedAdminAdditionalProperties = seedHomePage.clickAdditionalPropertiesButton();
        softAssert.assertTrue(seedAdminAdditionalProperties.validatePageTitle(), "Page title not present");
        seedAdminAdditionalProperties.updateAdditionalProperties(
                dataLoader.getTestParameter().get("gcpProjectName"),
                dataLoader.getTestParameter().get("serviceAccKey")
        );
        seedAdminAdditionalProperties.clickSaveBtn();
        //verify error msg
        //softAssert.assertTrue(seedAdminAdditionalProperties., "");
        softAssert.assertAll();
    }

    //@Test(dataProvider="userTestDataProvider", dependsOnMethods = "validateSaveSeedAdditionalPropertiesTest")
    public void validateErrorWithInvalidProjectTest(final JSONObject testData){
        SoftAssert softAssert = new SoftAssert();
        DataLoader dataLoader = new DataLoader(testData);
        seedAdminAdditionalProperties = seedHomePage.clickAdditionalPropertiesButton();
        softAssert.assertTrue(seedAdminAdditionalProperties.validatePageTitle(), "Page title not present");
        seedAdminAdditionalProperties.updateAdditionalProperties(
                dataLoader.getTestParameter().get("gcpProjectName"),
                dataLoader.getTestParameter().get("serviceAccKey")
        );
        seedAdminAdditionalProperties.clickSaveBtn();
        //verify error msg
        //softAssert.assertTrue(seedAdminAdditionalProperties., "");
        softAssert.assertAll();
    }
    
    
    *****/
    
    /**
     * author: amit chauhan
     * date: 3 Oct 2021
       desc:  Assuming that the page is already filled.
       validate fields as per Jira: https://engjira.int.kronos.com/browse/DIM-289897
       TODO: none
     */
   
    @Test(dataProvider="userTestDataProvider")
	public void validateErrorOnFields_noUpdateButtonPress(final JSONObject testData) {
    	SoftAssert sft = new SoftAssert();
    	// Leave prjct Desc empty
    	SeedAdminAdditionalProperties SdAdminAdnlPropPage = seedHomePage.clickAdditionalPropertiesButton();
    	
    	String error_pname = SdAdminAdnlPropPage.clearProjectName().returnProjectNameValidationError();
    	sft.assertEquals(error_pname,seedAdminAdditionalProperties.ERR_PRJCT_NAME_EMPTY);
    	 
    	// When Entered Value is invalid, this button shud not be clickable....
     	sft.assertFalse(SdAdminAdnlPropPage.isClickUpdateBtnClickable(),"Button should not be clickable....");
    	
    	//enter invalid val in project desc,  4 to 30 characters. All chars allowed.
    	//Allowed characters are: lowercase and uppercase letters, numbers, hyphen, single-quote, double-quote, space, and exclamation point.
    	
    	String error_pname_smallersize = SdAdminAdnlPropPage.clearProjectName().fillProjectDesc("341").
    			returnProjectNameValidationError();
    	sft.assertEquals(error_pname_smallersize,seedAdminAdditionalProperties.ERR_PRJCT_NAME_INVALID);
    	// When Entered Value is invalid, this button shud not be clickable....
     	sft.assertFalse(SdAdminAdnlPropPage.isClickUpdateBtnClickable(),"Button should not be clickable....");
    
    	String error_pname_size32 = SdAdminAdnlPropPage.clearProjectName().fillProjectDesc("32sasasasasasasasaswewewewwewaa$").
    			returnProjectNameValidationError();
    	sft.assertEquals(error_pname_size32,seedAdminAdditionalProperties.ERR_PRJCT_NAME_INVALID);
    	// When Entered Value is invalid, this button shud not be clickable....
     	sft.assertFalse(SdAdminAdnlPropPage.isClickUpdateBtnClickable(),"Button should not be clickable....");
    	
    	String error_pname_invalidchar = SdAdminAdnlPropPage.clearProjectName().fillProjectDesc("32$").
    			returnProjectNameValidationError();
    	sft.assertEquals(error_pname_invalidchar,seedAdminAdditionalProperties.ERR_PRJCT_NAME_INVALID);
    	// When Entered Value is invalid, this button shud not be clickable....
     	sft.assertFalse(SdAdminAdnlPropPage.isClickUpdateBtnClickable(),"Button should not be clickable....");
    	
    	
    	
    	
    	String error_invalid_json = SdAdminAdnlPropPage.clearAccKey().fillServiceAcKey("{name:some,work:some}").
    	returnAccSvcKeyValidationError();// Invalid json
    	
    	sft.assertEquals(error_invalid_json, seedAdminAdditionalProperties.ERR_SVC_ACC_KEY_INVALID);
    	
    	
    	sft.assertEquals(SdAdminAdnlPropPage.returnAccSvcKeyHintText(),seedAdminAdditionalProperties.HINT_SVC_ACC_KEY);
    	sft.assertEquals(SdAdminAdnlPropPage.returnProjectDescHintText(),seedAdminAdditionalProperties.HINT_PRJCT_NAME);
    	
    	
    	sft.assertEquals(SdAdminAdnlPropPage.returnTableStatusColumn(),"Completed");
    	sft.assertTrue(SdAdminAdnlPropPage.logOut());

    	sft.assertAll();
    	
	}

    
    /**
     * author: amit chauhan
     * date: 6 Oct 2021
       desc:  we will only update Acc Key with Invalid val. Ideally Update button should not be enabled.
       But Ina says, Button will be ebanled and it will do nothing if pressed.
       We need to verify that Update Button Press does not do anything. 
     */
    
    @Test(dataProvider="userTestDataProvider")
	public void validateUpdate_With_InvalidKey_noUpdateButtonPress(final JSONObject testData) {
    	SoftAssert sft = new SoftAssert();
    	// Leave prjct Desc empty
    	SeedAdminAdditionalProperties SdAdminAdnlPropPage = seedHomePage.clickAdditionalPropertiesButton();
    	boolean anyErrorSeen = SdAdminAdnlPropPage.anyErrorSeen();// expect no error when u arrive at this page.......
    	SdAdminAdnlPropPage.clearProjectName().fillProjectDesc("someprojectdesc").
    			returnProjectNameValidationError();
    	
    	 SdAdminAdnlPropPage.clearAccKey().fillServiceAcKey("{name:some,work:some}").
     	returnAccSvcKeyValidationError();
    	 
    	 boolean clickUpdateBtnClickable = SdAdminAdnlPropPage.isClickUpdateBtnClickable();
    	 
    	sft.assertFalse(clickUpdateBtnClickable,"Button should not be clickable....");// When Acc Key is invalid, this button shud not be clickable....
    	sft.assertFalse(anyErrorSeen,"You should not get any error when you arrive at this page......");
    	
    	seedHomePage = SdAdminAdnlPropPage.clickBackButton();
    	
    	sft.assertTrue(seedHomePage.validatePageTitle());
    	
    	sft.assertFalse(seedHomePage.ifAnyErrorSeen(driver));
    	sft.assertTrue(seedHomePage.logOut());
    	sft.assertAll();
    	
    }
    
    
    /**
     * author: amit chauhan
     * date: 7 Oct 2021
       desc:  Update project desc and start
     */
    
   @Test(dataProvider="userTestDataProvider")
	public void validateUpdate_withAllValidVals(final JSONObject testData) {
	    DataLoader dataLoader = new DataLoader(testData);
    	SoftAssert sft = new SoftAssert();
    	SeedAdminAdditionalProperties seedAdnlPropPage = seedHomePage.clickAdditionalPropertiesButton();
    	
    	// Expect that ProjectId is not editable.
    	sft.assertTrue(seedAdnlPropPage.validateProjectIdDisabled(),"Validate project id is not editable.....");
    	
    	//Provide some new project_desc
    	seedAdnlPropPage.fillProjectDesc("testAutomation");
    	
    	// provide some new valid key
    	seedAdnlPropPage.fillServiceAcKey(dataLoader.getTestParameter().get("serviceAccountKey")); // this is implemented this way that this will not make any changes if it has all values as ********
    	
    	//start "Update and Maintenance"
    	String sucMsg = seedAdnlPropPage.clickUpdateBtn().getSuccessMessageOnSaveUpdateBtnPress();
    	sft.assertEquals(sucMsg,"Success Update and Execute Maintenance started successfully.");
    	
    	seedAdnlPropPage.waitForMinutesUntilStatusIsComplete(6);
    	
    	sft.assertEquals(seedAdnlPropPage.returnTableStatusColumn(),"Completed");
    	sft.assertTrue(seedAdnlPropPage.logOut());
    	sft.assertAll();
    	
	}
    
     

}
