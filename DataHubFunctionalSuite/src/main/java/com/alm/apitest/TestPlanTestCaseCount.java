package com.alm.apitest;

import com.alm.apiobject.ALMException;
import com.alm.apiobject.AlmAPIObject;
import com.alm.utils.ALMConstant;
import com.alm.utils.ALMConstant.TestType;
import com.alm.utils.TestPlanExcelUtility;
import com.alm.utils.Utils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestPlanTestCaseCount extends BaseAlmAPITest {
	AlmAPIObject almAPIObject;
	
	@BeforeClass
	public void initializeMap() {
		headlinesTestCaseCount = new HashMap<String, List<Integer>>();
	}

	@Test
	public void testUpdateResultAPITestcase() throws ALMException, IOException {
		List<Integer> testCaseCount = new ArrayList<Integer>();
		
		almAPIObject = new AlmAPIObject();
		
		String subjectFolderId = almAPIObject.getTestPlanFolderId("Subject","");
		String falconProjectId = almAPIObject.getTestPlanFolderId("Falcon Project",subjectFolderId);
		String platformId = almAPIObject.getTestPlanFolderId("Platform",falconProjectId);
		String hierarchicalPath;
		List<String> headlineList = Utils.getTestPlanFolderList();

		for(String headline:headlineList) {
			
			if(headline.equals(ALMConstant.headline.CONFIGURATION_APP_SDM.getValue()) || headline.equals(ALMConstant.headline.INFORMATION_ACCESS.getValue())){
				hierarchicalPath = almAPIObject.getTestPlanFolderHierarchicalPath(falconProjectId, headline);
			}else {
				hierarchicalPath = almAPIObject.getTestPlanFolderHierarchicalPath(platformId, headline);
			}
			
			testCaseCount = getTestCaseCountForGivenTestPlanFolder(hierarchicalPath);
			headlinesTestCaseCount.put(headline, testCaseCount);			

		}			
	
	}

	private List<Integer> getTestCaseCountForGivenTestPlanFolder(String hierarchicalPath) {
		int count;
		List<Integer> testCaseCount = new ArrayList<Integer>();
		
		count = almAPIObject.getTotalTestsCountForGivenTestPlanFolder(hierarchicalPath, "Total");
		testCaseCount.add(count);
		count = almAPIObject.getTotalTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.USER_INTERFACE.getValue());
		testCaseCount.add(count);
		count = almAPIObject.getTotalTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.API.getValue());
		testCaseCount.add(count);	
		count = almAPIObject.getTotalTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.OTHER.getValue());
		testCaseCount.add(count);			

		count = almAPIObject.getRegressionTestsCountForGivenTestPlanFolder(hierarchicalPath, "Total");
		testCaseCount.add(count);
		count = almAPIObject.getRegressionTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.USER_INTERFACE.getValue());
		testCaseCount.add(count);
		count = almAPIObject.getRegressionTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.API.getValue());
		testCaseCount.add(count);	
		count = almAPIObject.getRegressionTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.OTHER.getValue());
		testCaseCount.add(count);				

		count = almAPIObject.getAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, "Total");
		testCaseCount.add(count);
		count = almAPIObject.getAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.USER_INTERFACE.getValue());
		testCaseCount.add(count);
		count = almAPIObject.getAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.API.getValue());
		testCaseCount.add(count);
		count = almAPIObject.getAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.OTHER.getValue());
		testCaseCount.add(count);			
		
		count = almAPIObject.getToBeAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, "Total");
		testCaseCount.add(count);
		count = almAPIObject.getToBeAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.USER_INTERFACE.getValue());
		testCaseCount.add(count);
		count = almAPIObject.getToBeAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.API.getValue());
		testCaseCount.add(count);
		count = almAPIObject.getToBeAutomatedTestsCountForGivenTestPlanFolder(hierarchicalPath, TestType.OTHER.getValue());
		testCaseCount.add(count);			
		
		return testCaseCount;	
		
	}	
	
	@AfterClass
	public void generateReport() throws IOException {
		TestPlanExcelUtility.generateReport(headlinesTestCaseCount);		
	}

}
