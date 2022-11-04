package com.alm.apiobject;

import com.Utilities.Helper;
import com.alm.utils.ALMConstant;
import com.alm.utils.Utils;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.expect;

public class AlmAPIObject extends BaseAlmApiObject {

	private static String GET_TESTSET_FOLDER_URI = BASE_URL + "/rest/domains/%s/projects/%s/test-set-folders";
	private static String GET_TESTSET_URI = BASE_URL + "/rest/domains/%s/projects/%s/test-sets";
	private static String GET_TESTSET_TESTCASES_URI = BASE_URL + "/rest/domains/%s/projects/%s/test-instances";
	private static String GET_TESTPLAN_TESTCASES_URI = BASE_URL + "/rest/domains/%s/projects/%s/tests";
	private static String GET_TESTPLAN_FOLDER_URI = BASE_URL + "/rest/domains/%s/projects/%s/test-folders";

	public String getParentFolderId() throws ALMException {
		Response response;
		String parentId = "0";
		if (!ALM_FOLDER_PATH.contains("\\")) {
			List<String> folderHierarchy = Arrays.asList(ALM_FOLDER_PATH.split("/"));

			for (String folderName : folderHierarchy) {
				response = expect().given().queryParams("query", "{parent-id[" + parentId + "];name['"+folderName+"']}")
						.queryParam("fields", "id,name").queryParam("page-size", PAGE_SIZE).accept(ContentType.JSON).cookies(cookies).when()
						.get(String.format(GET_TESTSET_FOLDER_URI, DOMAIN_NAME, PROJECT_NAME));

				parentId = JsonPath.with(response.asString()).get("entities[0].Fields[1].values[0].value");
			}
		} else {
			throw new ALMException("Please provide correct path with forward slash");
		}

		return parentId;
	}

	public String getFolderId(String parentId,String folderName) {
		String folderId = null;
		Response response = expect().given().queryParams("query", "{parent-id[" + parentId + "];name['"+folderName+"']}")
				.queryParam("fields", "id,name").queryParam("page-size", PAGE_SIZE).accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTSET_FOLDER_URI, DOMAIN_NAME, PROJECT_NAME));
		folderId = JsonPath.with(response.asString()).get("entities[0].Fields[1].values[0].value");
		return folderId;
	}

	public String getFolderHierarchicalPath(String folderId) {
		Response response = expect().given().queryParams("query", "{id[" + folderId + "]}")
				.queryParam("fields", "hierarchical-path,name").queryParam("page-size", PAGE_SIZE).accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTSET_FOLDER_URI, DOMAIN_NAME, PROJECT_NAME));
		if(JsonPath.with(response.asString()).getInt("TotalResults") == 0) {
			return null;
		}else {
			return JsonPath.with(response.asString()).get("entities[0].Fields[0].values[0].value");
		}

	}

	public Response getChildIdList(String parentId) {
		Response response = expect().given().queryParams("query", "{parent-id[" + parentId + "]}")
				.queryParam("fields", "name,id").queryParam("page-size", PAGE_SIZE).accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTSET_FOLDER_URI, DOMAIN_NAME, PROJECT_NAME));

		return response;
	}

	public String getTestSet(String hierarchicalPath,String testcaseType){
		Response response = expect().given().queryParams("query", "{test-set-folder.hierarchical-path[" + hierarchicalPath + "*];name['"+testcaseType+"']}")
				.queryParam("fields", "name,id").queryParam("page-size", PAGE_SIZE).accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTSET_URI, DOMAIN_NAME, PROJECT_NAME));
		String testsetId = JsonPath.with(response.asString()).get("entities[0].Fields[1].values[0].value").toString();
		return testsetId;
	}

	public Response getTestCaseStatusForGivenTestSetId(String testSetId)  {
		if(EXECUTION_DATE == null ) {
			EXECUTION_DATE = Utils.getCurrentDate("yyyy-MM-dd");
		}
		if(EXECUTION_DATE.trim().isEmpty()) {
			EXECUTION_DATE = Utils.getCurrentDate("yyyy-MM-dd");
		}
		Response response = expect().given()
				.queryParams("query", "{cycle-id[" + testSetId + "]}", "test-id", "status", "page-size", PAGE_SIZE)
				.accept(ContentType.JSON).cookies(cookies).when().get(String.format(GET_TESTSET_TESTCASES_URI, DOMAIN_NAME, PROJECT_NAME));
		return response;
	}

	public String getTestPlanFolderId(String folderName,String parentId) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		if(parentId==null || parentId.isEmpty() ) {
			queryParameter.put("query", "{name['"+ folderName +"']}");
		}else {
			queryParameter.put("query", "{name['"+ folderName +"'];parent-id['"+parentId+"']}");
		}
		queryParameter.put("fields", "id,name");
		queryParameter.put("page-size", PAGE_SIZE);
		Response response = expect().given()
				.queryParams(queryParameter)
				.accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTPLAN_FOLDER_URI, DOMAIN_NAME, PROJECT_NAME));
		String folderId = JsonPath.with(response.asString()).get("entities[0].Fields[1].values[0].value");
		
		return folderId;
	}

	public String getTestPlanFolderHierarchicalPath(String parentId,String folderName) {
		if(folderName.contains(":"))
		{
			String[] folderNameList = folderName.split(":");
			parentId = getTestPlanFolderId(folderNameList[0],parentId);
			return (getTestPlanFolderHierarchicalPath(parentId,folderNameList[1]));
		}
		else
		{
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		queryParameter.put("query", "{name['"+ folderName +"'];parent-id['"+parentId+"']}");
		
		queryParameter.put("fields", "hierarchical-path");
		queryParameter.put("page-size", PAGE_SIZE);
		Response response = expect().given()
				.queryParams(queryParameter)
				.accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTPLAN_FOLDER_URI, DOMAIN_NAME, PROJECT_NAME));
		String hierarchicalPath = JsonPath.with(response.asString()).get("entities[0].Fields[0].values[0].value");
		
		return hierarchicalPath;
		}
	}
	
	public Response getTestCaseForGivenTestPlanFolder(String folderHierarchicalPath,String testCaseType) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		if(testCaseType.equals("API")) {
			queryParameter.put("query",
					"{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-01['Automated'];user-template-03['Regression Testing'];user-template-03['"+testCaseType+" Testing']}");
		}else {
			queryParameter.put("query",
					"{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-01['Automated'];user-template-03['Regression Testing'];user-template-03['"+ALMConstant.TestType.USER_INTERFACE.getValue()+" Testing']}");
		}

		queryParameter.put("fields", "id");
		queryParameter.put("page-size", PAGE_SIZE);
		Response response = expect().given()
				.queryParams(queryParameter)
				.accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTPLAN_TESTCASES_URI, DOMAIN_NAME, PROJECT_NAME));
		return response;
	}


	public int getTotalTestsCountForGivenTestPlanFolder(String folderHierarchicalPath, String testCaseType) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		
		if (testCaseType.equals("Total")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')]}");
		} else if (testCaseType.equals("Other")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03[not('API Testing')];user-template-03[not('User Interface Testing')]}");
		} else {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03['"+testCaseType+" Testing']}");
		} 
		
		return getTestCaseCountForGivenQuery(queryParameter);
	}
	
	public int getRegressionTestsCountForGivenTestPlanFolder(String folderHierarchicalPath, String testCaseType) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		
		if (testCaseType.equals("Total")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03['Regression Testing']}");
		} else if (testCaseType.equals("Other")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03[not('API Testing')];user-template-03[not('User Interface Testing')];user-template-03['Regression Testing']}");
		} else {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03['"+testCaseType+" Testing'];user-template-03['Regression Testing']}");
		} 
		
		return getTestCaseCountForGivenQuery(queryParameter);
	}
	
	public int getAutomatedTestsCountForGivenTestPlanFolder(String folderHierarchicalPath, String testCaseType) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
	
		if (testCaseType.equals("Total")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-01['Automated']}");
		} else if (testCaseType.equals("Other")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03[not('API Testing')];user-template-03[not('User Interface Testing')];user-template-01['Automated']}");
		} else {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03['"+testCaseType+" Testing'];user-template-01['Automated']}");
		} 		
		
		return getTestCaseCountForGivenQuery(queryParameter);
	}
	
	public int getAutomatedRegressionTestsCountForGivenTestPlanFolder(String folderHierarchicalPath, String testCaseType) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
	
		if (testCaseType.equals("Total")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-01['Automated'];user-template-03['Regression Testing']}");
		} else if (testCaseType.equals("Other")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03[not('API Testing')];user-template-03[not('User Interface Testing')];user-template-01['Automated'];user-template-03['Regression Testing']}");
		} else {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03['"+testCaseType+" Testing'];user-template-01['Automated'];user-template-03['Regression Testing']}");
		} 		
		
		return getTestCaseCountForGivenQuery(queryParameter);
	}
	
	public int getToBeAutomatedTestsCountForGivenTestPlanFolder(String folderHierarchicalPath, String testCaseType) {
		Map<String,Object> queryParameter = new HashMap<String,Object>();
	
		if (testCaseType.equals("Total")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-01['Manual'];user-template-14['Yes'];user-template-03['Regression Testing']}");
		} else if (testCaseType.equals("Other")) {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03[not('API Testing')];user-template-03[not('User Interface Testing')];user-template-01['Manual'];user-template-14['Yes'];user-template-03['Regression Testing']}");
		} else {
			queryParameter.put("query", "{test-folder.hierarchical-path['"+folderHierarchicalPath+"*'];user-template-06[not('Closed')];user-template-06[not('Invalid')];user-template-03['"+testCaseType+" Testing'];user-template-01['Manual'];user-template-14['Yes'];user-template-03['Regression Testing']}");
		} 		
		
		return getTestCaseCountForGivenQuery(queryParameter);
	}
	
	protected int getTestCaseCountForGivenQuery(Map<String,Object> queryParameter) {
		queryParameter.put("fields", "id");
		queryParameter.put("page-size", PAGE_SIZE);
		Response response = expect().given()
				.queryParams(queryParameter)
				.accept(ContentType.JSON).cookies(cookies).when()
				.get(String.format(GET_TESTPLAN_TESTCASES_URI, DOMAIN_NAME, PROJECT_NAME));
		
		int testCaseCount = Integer.parseInt(response.getBody().jsonPath().getString("TotalResults"));
		return testCaseCount;
		
	}	

	public Response updateTestCaseStatus(String testCaseId,String status){

		String body="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Entity Type=\"test-instance\"><Fields><Field Name=\"status\"><Value>"+status+"</Value></Field></Fields></Entity>";


		Response response = expect().given()
				.body(body)
				.accept("application/xml").contentType("application/xml").cookies(cookies).when().put(String.format(GET_TESTSET_TESTCASES_URI, DOMAIN_NAME, PROJECT_NAME)+"/"+testCaseId);
		return response;



	}
	
}
