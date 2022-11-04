package com.alm.apitest;

import com.alm.apiobject.ALMException;
import com.alm.apiobject.AlmAPIObject;
import com.alm.utils.ALMConstant;
import com.alm.utils.ALMConstant.headline;
import com.alm.utils.CommonParameters;
import com.alm.utils.Utils;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.time.LocalDateTime;
import java.util.*;

import static com.jayway.restassured.RestAssured.expect;

public class BaseAlmAPITest extends CommonParameters{

	private static String ALM_LOGIN_URI;
	private static String ALM_LOGOUT_URI = BASE_URL + "/api/authentication/sign-out";
	public static Map<String,Integer> failedTestCaseCount;
	public static Map<String,Integer> passTestCaseCount;
	public static Map<String,Integer> skippedTestCaseCount;
	public static Map<String,Integer> totalALMTestCaseCount;
	public static Map<String,List<Integer>> headlinesTestCaseCount;

	@BeforeSuite(alwaysRun = true)
	public void loadXmlParameters(ITestContext context) throws ALMException {
		PROJECT_NAME = "Falcon";
		DOMAIN_NAME = "DEFAULT";
		BASE_URL = "http://kap-us-alm2.int.kronos.com:8080/qcbin";
		PAGE_SIZE = 1000;
		EXCEL_REPORT_PATH = "REPORT_PATH";
		ALM_FOLDER_PATH = "Falcon Project/ETS/Data Hub/Regression/DataHubAdmin/E2E";
		ALM_TESTPLAN_FOLDER_PATH = "Falcon Project/ETS/Data Hub/Regression/DataHubAdmin/E2E";
		ALM_LOGIN_URI = BASE_URL + "/api/authentication/sign-in";
		EXECUTION_DATE = LocalDateTime.now().toString();
		RELEASE_BUILD = "8.0.0";
		
				
		String childFolderName = "Smoke Suite";
		if(childFolderName == null) {
			CHILD_FOLDER_NAME = new ArrayList<String>();
		}else {
			if(childFolderName.trim().isEmpty()) {
				CHILD_FOLDER_NAME = new ArrayList<String>();	
			}else {
				CHILD_FOLDER_NAME = Arrays.asList(childFolderName);
			}
		}

		fetchChildFolderList();
	}

	@BeforeClass(alwaysRun = true)
	public void loginToALM() {
		authenticate();
	}

	private static void authenticate() {
		cookies = expect().given().auth()
				.basic(ALMConstant.USERNAME, ALMConstant.PASSWORD).accept(ContentType.JSON)
				.when().post(ALM_LOGIN_URI).thenReturn().cookies();
	}

	public static void logout(){
		expect().given().when().post(ALM_LOGOUT_URI).thenReturn().body();
		cookies = null;
	}

	public BaseAlmAPITest() {
		PROJECT_NAME = "Falcon";
		DOMAIN_NAME = "DEFAULT";
		BASE_URL = "http://kap-us-alm2.int.kronos.com:8080/qcbin";
		PAGE_SIZE = 1000;
		EXCEL_REPORT_PATH = "REPORT_PATH";
		ALM_FOLDER_PATH = "Falcon Project/ETS/Data Hub/Regression/DataHubAdmin/E2E";
		ALM_TESTPLAN_FOLDER_PATH = "Falcon Project/ETS/Data Hub/Regression/DataHubAdmin/E2E";
		ALM_LOGIN_URI = BASE_URL + "/api/authentication/sign-in";
		EXECUTION_DATE = LocalDateTime.now().toString();
		RELEASE_BUILD = "8.0.0";

	}

	private void fetchChildFolderList(){
		if(CHILD_FOLDER_NAME.size()==0) {
			CHILD_FOLDER_NAME = new ArrayList<String>();
			CHILD_FOLDER_NAME.add(ALMConstant.TestSetFolder.HCM_INTEGRATION.getTestSetFolder());
			CHILD_FOLDER_NAME.add(ALMConstant.TestSetFolder.HCA_KPI.getTestSetFolder());
			CHILD_FOLDER_NAME.add(ALMConstant.TestSetFolder.PLATFORM_SERVICES.getTestSetFolder());
		}
	}

	protected List<String> fetchListOfChildFolderId(Response response){
		String responseString = response.asString();
		List<String> folderId = new ArrayList<String>();
		List<String> entitiesList = JsonPath.with(responseString).getList("entities.Fields");

		for(int i = 0 ; i < entitiesList.size() ; i++) {
			folderId.add(JsonPath.with(responseString).get("entities["+i+"].Fields.find{node -> node.Name == 'id'}.values[0].value").toString());
		}

		return folderId;
	}

	protected void updateTestCaseExecutionResultCount(Response response,String folderName) {
		String responseString = response.asString();
		failedTestCaseCount.put(folderName, StringUtils.countMatches(responseString, "Failed"));
		skippedTestCaseCount.put(folderName, StringUtils.countMatches(responseString, "No Run"));
		passTestCaseCount.put(folderName, StringUtils.countMatches(responseString, "Passed"));
	}

	protected void checkReleaseBuildOrNot() {
		if(RELEASE_BUILD.equalsIgnoreCase("no")) {
			failedTestCaseCount.remove(headline.ROLLING_UPGRADE.getValue());
			failedTestCaseCount.remove(headline.TENANT_MANAGEMENT_SYSTEM.getValue());
			
			skippedTestCaseCount.remove(headline.ROLLING_UPGRADE.getValue());
			skippedTestCaseCount.remove(headline.TENANT_MANAGEMENT_SYSTEM.getValue());
			
			passTestCaseCount.remove(headline.ROLLING_UPGRADE.getValue());
			passTestCaseCount.remove(headline.TENANT_MANAGEMENT_SYSTEM.getValue());
		}
	}
	protected List<String> fetchListOfChildFolderName(Response response) {
		String responseString = response.asString();
		List<String> folderNames = new ArrayList<String>();
		List<String> entitiesList = JsonPath.with(responseString).getList("entities.Fields");

		for(int i = 0 ; i < entitiesList.size() ; i++) {
			folderNames.add(JsonPath.with(responseString).get("entities["+i+"].Fields.find{node -> node.Name == 'name'}.values[0].value").toString());
		}

		return folderNames;
	}
	
	protected void updateTestPlanTestCaseCount(AlmAPIObject almApiObject, String testcaseType) {
		String subjectFolderId = almApiObject.getTestPlanFolderId("Subject","");
		String falconProjectId = almApiObject.getTestPlanFolderId("Falcon Project",subjectFolderId);
		String platformId = almApiObject.getTestPlanFolderId("Platform",falconProjectId);
		String hierarchicalPath ;
		List<String> headlineList = Utils.getHeadlineList();
		for(String headline:headlineList) {
			if(headline.equals(ALMConstant.headline.CONFIGURATION_APP_SDM.getValue()) || headline.equals(ALMConstant.headline.INFORMATION_ACCESS.getValue())){
				hierarchicalPath = almApiObject.getTestPlanFolderHierarchicalPath(falconProjectId, headline);
			}else if(headline.equals(ALMConstant.headline.PLANNER_INTEGRATION.getValue())){
				String telestaffId = almApiObject.getTestPlanFolderId(ALMConstant.headline.TELESTAFF_INTEGRATION.getValue(),platformId);
				hierarchicalPath = almApiObject.getTestPlanFolderHierarchicalPath(telestaffId, headline);
            }else if(headline.equals("Targets and Thresholds Standard")||headline.equals("HCA-WFM Config")||headline.equals("HCA-Compute")||headline.equals("HCA-Reports")||headline.equals("HCA-Integration")||headline.equals("HCA-TP and Licensing")||headline.equals("HCA-Supportabilty")) {				String hcaID = almApiObject.getTestPlanFolderId("Healthcare Analytics",platformId);
            	hierarchicalPath = almApiObject.getTestPlanFolderHierarchicalPath(hcaID, headline);			
            }
			else {
				hierarchicalPath = almApiObject.getTestPlanFolderHierarchicalPath(platformId, headline);
			}
			
			Response response = almApiObject.getTestCaseForGivenTestPlanFolder(hierarchicalPath, testcaseType);
			this.countTestPlanTestCases(response,headline);
		}
	}

	private void countTestPlanTestCases(Response response,String headline) {
		int count = Integer.parseInt(JsonPath.with(response.asString()).get("TotalResults").toString());
		if(RELEASE_BUILD.equalsIgnoreCase("yes") && headline.equals(ALMConstant.headline.AUTHENTICATION_TP.getValue())) {
			int authnCount = totalALMTestCaseCount.get(ALMConstant.headline.AUTHENTICATION_USER_MANAGEMENT.getValue());
			count = count + authnCount;
			totalALMTestCaseCount.put(ALMConstant.headline.AUTHENTICATION_USER_MANAGEMENT.getValue(), count);
			
		}else if(RELEASE_BUILD.equalsIgnoreCase("no") && headline.equals(ALMConstant.headline.AUTHENTICATION_TP.getValue())){
			
		}else {
			
			totalALMTestCaseCount.put(headline, count);
		}
		
	}
}	