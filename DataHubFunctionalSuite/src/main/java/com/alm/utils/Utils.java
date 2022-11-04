package com.alm.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static List<String> getHeadlineList(){
		List<String> headlineList = new ArrayList<String>();
		for(ALMConstant.headline headLine: ALMConstant.headline.values()){
			headlineList.add(headLine.getValue());
		}
		return headlineList;
	}
	
	public static List<String> getTestPlanFolderList(){
		List<String> testPlanFolderList = new ArrayList<String>();
		for(ALMConstant.TestPlanFolder testPlanFolder: ALMConstant.TestPlanFolder.values()){
			testPlanFolderList.add(testPlanFolder.getTestPlanFolder());
		}
		return testPlanFolderList;
	}
	
	public static List<String> getTestPlanFolderListRegression(){
		List<String> testPlanFolderList = new ArrayList<String>();
		for(ALMConstant.TestPlanFolderRegression testPlanFolder: ALMConstant.TestPlanFolderRegression.values()){
			testPlanFolderList.add(testPlanFolder.getTestPlanFolderRegression());
		}
		return testPlanFolderList;
	}
	

	public static String getCurrentDate(String format) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format)).toString();
	}
	
}
