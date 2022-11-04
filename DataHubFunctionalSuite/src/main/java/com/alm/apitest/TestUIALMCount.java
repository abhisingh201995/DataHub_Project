package com.alm.apitest;

import com.alm.apiobject.ALMException;
import com.alm.apiobject.AlmAPIObject;
import com.alm.utils.ALMConstant;
import com.alm.utils.ExcelUtility;
import com.alm.utils.Utils;
import com.jayway.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class TestUIALMCount extends BaseAlmAPITest {

	AlmAPIObject almApiObject;

	@BeforeClass
	public void initializeMap() {
		failedTestCaseCount = new HashMap<String, Integer>();
		passTestCaseCount = new HashMap<String, Integer>();
		skippedTestCaseCount = new HashMap<String, Integer>();
		totalALMTestCaseCount = new HashMap<String, Integer>();
	}

	@Test
	public void testUpdateResultUITestcase() throws ALMException {
		
		almApiObject = new AlmAPIObject();
		String parentId = almApiObject.getParentFolderId();
		for (String childFolderName : CHILD_FOLDER_NAME) {
			String folderId = almApiObject.getFolderId(parentId, childFolderName);
			Response response = almApiObject.getChildIdList(folderId);
			List<String> folderIdList = this.fetchListOfChildFolderId(response);
			List<String> folderNameList = this.fetchListOfChildFolderName(response);
			List<String> headLineList = Utils.getHeadlineList();
			if(folderIdList.size()!=0 && (!childFolderName.equals("Information Access"))) {
				for (int counter = 0; counter < folderIdList.size(); counter++) {
					if (ALMConstant.getUIJobLinks.getReportKeyByHeadline(folderNameList.get(counter))!=null) {
						String hierarchicalPath = almApiObject.getFolderHierarchicalPath(folderIdList.get(counter));
						if(hierarchicalPath == null) {
							hierarchicalPath = almApiObject.getFolderHierarchicalPath(folderId);
						}
						String testSetId = almApiObject.getTestSet(hierarchicalPath,
								ALMConstant.TestType.UI.getValue());
						response = almApiObject.getTestCaseStatusForGivenTestSetId(testSetId);
						this.updateTestCaseExecutionResultCount(response, folderNameList.get(counter));
					}
				}
			}else {
				String hierarchicalPath = almApiObject.getFolderHierarchicalPath(folderId);
				String testSetId = almApiObject.getTestSet(hierarchicalPath,
						ALMConstant.TestType.UI.getValue());
				response = almApiObject.getTestCaseStatusForGivenTestSetId(testSetId);
				this.updateTestCaseExecutionResultCount(response,childFolderName);
			}
		}

		updateTestPlanTestCaseCount(almApiObject,ALMConstant.TestType.UI.getValue());
	}

	@AfterClass
	public void generateReport() throws Exception {
		checkReleaseBuildOrNot();
		ExcelUtility.generateResultReport(ALMConstant.TestType.UI.getValue(), EXCEL_REPORT_PATH, failedTestCaseCount,
				passTestCaseCount, skippedTestCaseCount,totalALMTestCaseCount);
		failedTestCaseCount = null;
		passTestCaseCount = null;
		skippedTestCaseCount = null;
		totalALMTestCaseCount = null;
	}

}
