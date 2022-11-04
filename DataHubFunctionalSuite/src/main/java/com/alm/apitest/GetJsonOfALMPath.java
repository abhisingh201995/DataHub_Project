package com.alm.apitest;

import com.alm.apiobject.ALMException;
import com.alm.apiobject.AlmAPIObject;
import com.alm.utils.ALMConstant;
import com.alm.utils.Utils;
import com.jayway.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetJsonOfALMPath extends BaseAlmAPITest {

	AlmAPIObject almApiObject;

	@BeforeClass
	public void initializeMap() {
		failedTestCaseCount = new HashMap<String, Integer>();
		passTestCaseCount = new HashMap<String, Integer>();
		skippedTestCaseCount = new HashMap<String, Integer>();
		totalALMTestCaseCount = new HashMap<String, Integer>();
	}

	@Test
	public void testGetJsonTestcase() throws ALMException, JSONException, IOException {
		JSONObject jobj = new JSONObject();
		String fileLocation = System.getProperty("user.dir")+ System.getProperty("file.separator") + "almjson.properties";
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
					if (ALMConstant.getAPIJobLinks.getReportKeyByHeadline(folderNameList.get(counter))!=null) {
						String headlineName = folderNameList.get(counter);
						String hierarchicalPath = almApiObject.getFolderHierarchicalPath(folderIdList.get(counter));
						if(hierarchicalPath == null) {
							hierarchicalPath = almApiObject.getFolderHierarchicalPath(folderId);
						}
						JSONObject uiapijobj = new JSONObject();
						String testSetId = almApiObject.getTestSet(hierarchicalPath,
								ALMConstant.TestType.UI.getValue());
						String testSetAPIId = almApiObject.getTestSet(hierarchicalPath,
								ALMConstant.TestType.API.getValue());
						uiapijobj.put("ui", testSetId);
						uiapijobj.put("api", testSetAPIId);
						jobj.put(headlineName,uiapijobj);
					}
				}
			}
			
		}
		setJsonDetailsOnFile(fileLocation, jobj);
	}

	
	/**
	 * function name: setJsonDetailsOnFile description: This method is to
	 * write JSON details on a property file.
	 * 
	 * @param fileLocation
	 * @throws IOException
	 */
	public void setJsonDetailsOnFile(String fileLocation, JSONObject jobj) throws IOException{
		FileWriter file = null;
		 try {file = new FileWriter(fileLocation);
			    file.write("ALM_ID_MAP="+jobj.toString());
	            file.flush();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 	finally {
				try {
					file.flush();
					file.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
}