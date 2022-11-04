package com.alm.utils;

import java.util.Arrays;
import java.util.List;

public class ALMConstant {

	public final static String USERNAME = "anupam.sinha";
	public final static String PASSWORD = "Attherate@1229#";
	public final static String TEST_PLAN_BASE_FOLDER = "Subject/Falcon Project/Platform";

public enum getUIJobLinks {
		
		WORKFLOW("Platform_Services_Regression","Workflow"),
		KPI_FRAMEWORK("HCA_And_KPI_Regression","KPI Framework"),
		HCA("HCA_And_KPI_Regression","Targets and Thresholds Standard"),
        HCA_WFM_CONFIG("HCA_And_KPI_Regression","HCA-WFM Config"),
        HCA_COMPUTE("HCA_And_KPI_Regression","HCA-Compute"),
        HCA_REPORTS("HCA_And_KPI_Regression","HCA-Reports"),
        HCA_INTEGRATION("HCA_And_KPI_Regression","HCA-Integration"),
        HCA_TP_lICENSING("HCA_And_KPI_Regression","HCA-TP and Licensing"),
        HCA_SUPPORTABILITY("HCA_And_KPI_Regression","HCA-Supportabilty"),
		TENANT_MANAGEMENT("Platform_Services_Regression","Tenant Management System"),
		AUTHENTICATION_USER_MANAGEMENT("Platform_Services_Regression","Authentication and User Management"),
		LICENSING_PLATFORM("Platform_Services_Regression","Licensing Platform"),
		MULTI_TENANCY("Platform_Services_Regression","Multi-Tenancy"),
		CONTROL_CENTER("Platform_Services_Regression","Control Center"),
		DISPLAY_PROFILE("Platform_Services_Regression","Display Profile"),
		HYPERFINDS("Platform_Services_Regression","HyperFinds"),
		INTEGRATION_HUB("HCM_And_Integration_Regression","Integration Hub"),
		PEOPLE_EDITOR("Platform_Services_Regression","People Editor"),
		//SETUP_APPS("Platform_Services_Regression","Setup Apps"),
		IA_REPORTS("Platform_Services_Regression","IA Reports"),
		CONFIGURATION_APP("Platform_Services_Regression","Configuration App/SDM"),
		LOGGING_MONITORING("Platform_Services_Regression","Logging Monitoring and Auditing"),
		PLATFORM_MODERNIZATION("Platform_Services_Regression","Platform Modernization"),
		INFORMATION_ACCESS("Platform_Services_Regression","Information Access"),
		ROLLING_UPGRADE("Platform_Services_Regression","Rolling Upgrade"),
		TELESTAFF_INTEGRATION("HCM_And_Integration_Regression","TeleStaff Integrations"),
		PLANNER_INTEGRATION("HCM_And_Integration_Regression","Planner Integrations"),
		MULTICURRENCY("Platform_Services_Regression","Multi Currency"),
		HCM_INTEGRATION("HCM_And_Integration_Regression","HCM Integration - R2"),
		APIMANAGEMENT("Platform_Services_Regression","API Management");
	
		
		
		private String reportName;
		private String headline;
		private getUIJobLinks(String reportName,String headline) {
			this.reportName = reportName;
			this.headline = headline;
		}
		
		public String getheadline() {
			return this.headline;
		}
		
		public String getReportName() {
			return this.reportName;
		}
		
		public static getUIJobLinks getReportKeyByHeadline(String headline) {
			for(getUIJobLinks uijobLink : getUIJobLinks.values()){
				if(uijobLink.getheadline().equalsIgnoreCase(headline)){
					return uijobLink;
				}
			}
			return null;
		}
	}
	

public enum getAPIJobLinks {
		APIMANAGEMENT("Platform_Services_Regression","API Management"),
		WORKFLOW("Platform_Services_Regression","Workflow"),
		KPI_FRAMEWORK("HCA_And_KPI_Regression","KPI Framework"),
		HCA("HCA_And_KPI_Regression","Targets and Thresholds Standard"),
        HCA_WFM_CONFIG("HCA_And_KPI_Regression","HCA-WFM Config"),
        HCA_COMPUTE("HCA_And_KPI_Regression","HCA-Compute"),
        HCA_REPORTS("HCA_And_KPI_Regression","HCA-Reports"),
        HCA_INTEGRATION("HCA_And_KPI_Regression","HCA-Integration"),
        HCA_TP_lICENSING("HCA_And_KPI_Regression","HCA-TP and Licensing"),
        HCA_SUPPORTABILITY("HCA_And_KPI_Regression","HCA-Supportabilty"),
		TENANT_MANAGEMENT("Platform_Services_Regression","Tenant Management System"),
		AUTHENTICATION_USER_MANAGEMENT("Platform_Services_Regression","Authentication and User Management"),
		LICENSING_PLATFORM("Platform_Services_Regression","Licensing Platform"),
		MULTI_TENANCY("Platform_Services_Regression","Multi-Tenancy"),
		CONTROL_CENTER("Platform_Services_Regression","Control Center"),
		DISPLAY_PROFILE("Platform_Services_Regression","Display Profile"),
		HYPERFINDS("Platform_Services_Regression","HyperFinds"),
		INTEGRATION_HUB("HCM_And_Integration_Regression","Integration Hub"),
		PEOPLE_EDITOR("Platform_Services_Regression","People Editor"),
		//SETUP_APPS("Platform_Services_Regression","Setup Apps"),
		IA_REPORTS("Platform_Services_Regression","IA Reports"),
		CONFIGURATION_APP("Platform_Services_Regression","Configuration App/SDM"),
		LOGGING_MONITORING("Platform_Services_Regression","Logging Monitoring and Auditing"),
		PLATFORM_MODERNIZATION("Platform_Services_Regression","Platform Modernization"),
		INFORMATION_ACCESS("Platform_Services_Regression","Information Access"),
		ROLLING_UPGRADE("Platform_Services_Regression","Rolling Upgrade"),
		TELESTAFF_INTEGRATION("HCM_And_Integration_Regression","TeleStaff Integrations"),
		PLANNER_INTEGRATION("HCM_And_Integration_Regression","Planner Integrations"),
		MULTICURRENCY("Platform_Services_Regression","Multi Currency"),
		HCM_INTEGRATION("HCM_And_Integration_Regression","HCM Integration - R2"),
		ARCHITECTURAL_HEADLINE("Platform_Services_Regression","ArchitecturalHeadline");
	
		
		
		private String reportName;
		private String headline;
		private getAPIJobLinks(String reportName,String headline) {
			this.reportName = reportName;
			this.headline = headline;
		}
		
		public String getheadline() {
			return this.headline;
		}
		
		public String getReportName() {
			return this.reportName;
		}
		
		public static getAPIJobLinks getReportKeyByHeadline(String headline) {
			for(getAPIJobLinks apijobLink : getAPIJobLinks.values()){
				if(apijobLink.getheadline().equalsIgnoreCase(headline)){
					return apijobLink;
				}
			}
			return null;
		}
	}
	
	public enum HeadlineDomainMapping {
		HCMandIntegration("HCM & Integration",Arrays.asList("Integration Hub","TeleStaff Integrations","Planner Integrations","HCM Integration - R2")),
        HCAandKPI("HCA & KPI",Arrays.asList("KPI Framework","Targets and Thresholds Standard","HCA-WFM Config","HCA-Compute","HCA-Reports","HCA-Integration","HCA-TP and Licensing","HCA-Supportabilty")),
        PlatformServices("Platform Services",Arrays.asList("ArchitecturalHeadline","Configuration App/SDM","API Management","Control Center","Display Profile","Workflow","Licensing Platform","Rolling Upgrade","Multi-Tenancy","Tenant Management System","HyperFinds","People Editor","Setup Apps","IA Reports","Logging Monitoring and Auditing","Platform Modernization","Multi Currency","Authentication and User Management","Information Access"));
		//PlatformSupportability("Platform Supportability",Arrays.asList("Authentication and User Management")),
		//INFORMATIONACCESS("Information Access",Arrays.asList("Information Access"));
		
		private List<String> headline;
		private String domain;
		
		private HeadlineDomainMapping(String domain,List<String> headline) {
			this.headline = headline;
			this.domain = domain;
		}
		
		public List<String> getHeadline(){
			return headline;
		}
		
		public String getDomain(){
			return domain;
		}
		
		public static List<String> getHeadlineForDomain(String domain) {
			for(HeadlineDomainMapping headlineDomainMapping: HeadlineDomainMapping.values()){
				if (headlineDomainMapping.getDomain().equals(domain)) {
					return headlineDomainMapping.getHeadline();
				}				
			}
			return null;
			
		}
	}
	
	public enum CookiesParameter {

		ALM_USER("ALM_USER"),
		LWSSO_COOKIE_KEY("LWSSO_COOKIE_KEY"),
		QCSESSION("QCSession"),
		XSRF_TOKEN("XSRF-TOKEN");
		
		private String value;
		private CookiesParameter(String value) {
			this.value = value;
		}
		
		public String getCookieValue() {
			return this.value;
		}
	}
	
	public enum TestSetFolder{
		HCM_INTEGRATION("HCM & Integration"),
		HCA_KPI("HCA & KPI"),
		PLATFORM_SERVICES("Platform Services");
		private String testSetFolder;
		
		private TestSetFolder(String testSetFolder) {
			this.testSetFolder = testSetFolder;
		}
		
		public String getTestSetFolder() {
			return this.testSetFolder;
		}
		
	}
	
	public enum TestType{
		API("API"),
		UI("UI"),
		USER_INTERFACE("User Interface"),
		OTHER("Other");
		private String value;
		
		private TestType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		
	}
	
	public enum headline{
		
		CONFIGURATION_APP_SDM("Configuration App/SDM"),
		IA_REPORTS("IA Reports"),
		LOGGING_MONITORING_AUDITING("Logging Monitoring and Auditing"),
		PLATFORM_MODERNIZATION("Platform Modernization"),
		API_MANAGEMENT("API Management"),
		CONTROL_CENTER("Control Center"),
		DISPLAY_PROFILE("Display Profile"),
		HYPERFINDS("HyperFinds"),
		INTEGRATION_HUB("Integration Hub"),
		PEOPLE_EDITOR("People Editor"),
		AUTHENTICATION_USER_MANAGEMENT("Authentication and User Management"),
		KPI_FRAMEWORK("KPI Framework"),
		LICENSING_PLATFORM("Licensing Platform"),
		MULTI_TENANCY("Multi-Tenancy"),
		TENANT_MANAGEMENT_SYSTEM("Tenant Management System"),
		WORKFLOW("Workflow"),
		INFORMATION_ACCESS("Information Access"),
		ROLLING_UPGRADE("Rolling Upgrade"),
		AUTHENTICATION_TP("Authentication - TP"),
		TELESTAFF_INTEGRATION("TeleStaff Integrations"),
		PLANNER_INTEGRATION("Planner Integrations"),
		MULTICURRENCY("Multi Currency"),
		HCM_INTEGRATION("HCM Integration - R2"),
		HCA("Targets and Thresholds Standard"),
		ARCHITECTURAL_HEADLINE("ArchitecturalHeadline"),
        HCA_WFM_CONFIG("HCA-WFM Config"),
        HCA_COMPUTE("HCA-Compute"),
        HCA_REPORTS("HCA-Reports"),
        HCA_INTEGRATION("HCA-Integration"),
        HCA_TP_lICENSING("HCA-TP and Licensing");
		
		private String value;
		
		private headline(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
	}
	
	public enum TestPlanFolder{
		API_MANAGEMENT("API Management","UI& Integ"),
		CONFIGURATION_APP_SDM("Configuration App/SDM","Reports & Supp"),
		AUTHENTICATION_AND_USER_MANAGEMENT("Authentication and User Management","Core"),
		AUTHORIZATION("Authorization","UI& Integ"),
		COMMON_APP("Common App","UI& Integ"),
		CONTROL_CENTER("Control Center","UI& Integ"),
		DISPLAY_PROFILE("Display Profile","UI& Integ"),
		IA_REPORTS("IA Reports","Reports & Supp"),
		INTEGRATION_HUB("Integration Hub","UI& Integ"),
		KPI_FRAMEWORK("KPI Framework","Core"),
		LICENSING_PLATFORM("Licensing Platform","Core"),
		LOGGING_MONITORING_AND_AUDITING("Logging Monitoring and Auditing","Reports & Supp"),
		MULTI_TENANCY("Multi-Tenancy","Core"),
		PEOPLE_EDITOR("People Editor","UI& Integ"),
		PLATFORM_MODERNIZATION("Platform Modernization","Reports & Supp"),
		TENANT_MANAGEMENT_SYSTEM("Tenant Management System","Core"),
		WORKFLOW("Workflow","Core"),
		HYPERFINDS("HyperFinds","UI& Integ"),
		HCM_INTEGRATION_R2("HCM Integration - R2","UI& Integ"),
		TELESTAFF_INTEGRATION_R3("Telestaff Integration","UI& Integ"),
		INFORMATION_ACCESS("Information Access","IA"),
		HEALTHCARE_ANALYTICS("Healthcare Analytics","Core");
		
		private String testPlanFolder;
		private String domain;
		
		private TestPlanFolder(String testPlanFolder,String domain) {
			this.testPlanFolder = testPlanFolder;
			this.domain = domain;
		}
		
		public String getTestPlanFolder() {
			return this.testPlanFolder;
		}
		
		public String getDomain(){
			return this.domain;
		}
		
		public static String getDomainForKey(String key) {
			for(TestPlanFolder testPlanFolder: TestPlanFolder.values()){
				if (testPlanFolder.getTestPlanFolder().equals(key)) {
					return testPlanFolder.getDomain();
				}				
			}
			return null;
			
		}
		
	}
	
	public enum TestPlanFolderRegression{
		KPI_FRAMEWORK("KPI Framework","Core"),
		HCM_INTEGRATION_R2("HCM Integration - R2","UI& Integ"),
		HCM_DATA_DELIVERY("HCM Integration - R2:Data Delivery","UI& Integ"),
		HCM_Download_AND_Upload_File("HCM Integration - R2:Download & Upload File","UI& Integ"),
		HCM_DATA_INTEGRATION("HCM Integration - R2:HCM Data Integration","UI& Integ"),
		HCM_ALERTS_NOTIFICATION("HCM Integration - R2:Alerts & Notifications","UI& Integ"),
		HCM_DIDP("HCM Integration - R2:DIDP","UI& Integ"),
		HCM_TAB("HCM Integration - R2:HCM Tab","UI& Integ"),
		HCM_E2E_TESTING("HCM Integration - R2:E2E Testing","UI& Integ"),
		HCM_REPORTS("HCM Integration - R2:HCM Reports","UI& Integ"),
		HCM_INTEGRATION_DROPDOWN("HCM Integration - R2:Integration dropdown Parameter - Setup","UI& Integ"),
		HCM_MENU_INTEGRATION("HCM Integration - R2:Menu Integration","UI& Integ"),
		HCM_MOBILE("HCM Integration - R2:Mobile Requirements","UI& Integ"),
		HCM_NEAR_REAL_TIME_PERSON_IMPORT("HCM Integration - R2:Near Real Time People Import","UI& Integ"),
		HCM_NRT("HCM Integration - R2:NRT(minutely)","UI& Integ"),
		HCM_SESSION_MANAGEMENT("HCM Integration - R2:Session Management","UI& Integ"),
		HCM_TILES_INTEGRATION("HCM Integration - R2:Tiles Integration","UI& Integ"),
		TELESTAFF_INTEGRATION_R3("TeleStaff and Planner Integrations","UI& Integ"),
		HEALTHCARE_ANALYTICS("Healthcare Analytics","Core"),
		HEALTHCARE_ANALYTICS_BS("Healthcare Analytics:Business Structure","Core"),
		HEALTHCARE_ANALYTICS_STP("Healthcare Analytics:Symbolic Time Periods","Core"),
		HEALTHCARE_ANALYTICS_HCASERVICE("Healthcare Analytics:Productization & Integration","Core"),
		HEALTHCARE_ANALYTICS_TARGETS("Healthcare Analytics:Targets and Thresholds","Core"),
		HEALTHCARE_ANALYTICS_TK("Healthcare Analytics:TK Changes","Core"),
		HEALTHCARE_ANALYTICS_VOLUME("Healthcare Analytics:Volume","Core"),
		HEALTHCARE_ANALYTICS_PAYROLL("Healthcare Analytics:Payroll","Core"),
		HEALTHCARE_ANALYTICS_KPI("Healthcare Analytics:KPI Framework","Core"),
		IHUB("Integration Hub","UI& Integ");
		
		private String testPlanFolder;
		private String domain;
		
		private TestPlanFolderRegression(String testPlanFolder,String domain) {
			this.testPlanFolder = testPlanFolder;
			this.domain = domain;
		}
		
		public String getTestPlanFolderRegression() {
			return this.testPlanFolder;
		}
		
		public String getDomain(){
			return this.domain;
		}
		
		public static String getDomainForKey(String key) {
			for(TestPlanFolderRegression testPlanFolder: TestPlanFolderRegression.values()){
				if (testPlanFolder.getTestPlanFolderRegression().equals(key)) {
					return testPlanFolder.getDomain();
				}				
			}
			return null;
			
		}
		
	}
	
	
	
}
