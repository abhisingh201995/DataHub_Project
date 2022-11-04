package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

public final class CustomDriverMappingsTests_DHSS extends BaseConfiguration {


	
	/**
	 * @author: amit.chauhan
	 * @throws IOException 
	 * @date: Oct 18, 2021
	 * @desc: FOR VALID TENANT, GET CUSTOM DRIVER MAPPINGS.....
	 */
	@Test(dataProvider = "userTestDataProvider")
	public void getValidTenantTest(final JSONObject testData) throws IOException {
				
		ObjectMapper mapper = new ObjectMapper();
		SoftAssert softAssert = new SoftAssert();
		DataLoader dataLoader = new DataLoader(testData);
		
		String expected = dataLoader.getDesiredJsonResponse().toString();// Read from the dataFile.
		
		JsonNode readTree = mapper.readTree(expected);
		
		
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readTree));
	}
	/**
	 * @author: amit.chauhan
	 * @throws IOException 
	 * @date: Oct 18, 2021
	 * @desc: FOR VALID TENANT, UPDATE CUSTOM DRIVER MAPPINGS.....
	 */
	@Test(dataProvider = "userTestDataProvider")
	public void updateCustomDriverMapping_ValidTenant(final JSONObject testData) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		SoftAssert softAssert = new SoftAssert();
		DataLoader dataLoader = new DataLoader(testData);
		
		//String expected = dataLoader.getDesiredJsonResponse().toString();// Read from the dataFile.
		
		String jsonPayLoad =dataLoader.getDesiredJsonPayload().toString();
		
		JsonNode readTree = mapper.readTree(jsonPayLoad);
		
		
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readTree));
	}
	
}
