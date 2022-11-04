package com.Tests.DataHubSelfServiceTests.DataHubSelfServiceApiTests;

import com.DataObjects.DhssDataObject.LocationTypeMappingsDTO;
import com.Tests.ParentTests.BaseConfiguration;
import com.Utilities.DataLoader;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public final class LocationTypeMappingsTests extends BaseConfiguration{

	String baseUri = "https://run.mocky.io/v3/";
	String basePath = "3e61b5b6-5e3c-40d9-bf67-ee0e14af4abe";
	
	@Test(dataProvider = "userTestDataProvider")
	public void dhd_GetLTM_validTenant(final JSONObject testData) throws IOException {
		
		
		SoftAssert softAssert = new SoftAssert();
		DataLoader dataLoader = new DataLoader(testData);
		//System.out.println(dataLoader.getOutputStatusCode());	
		//System.out.println(dataLoader.getDesiredResponse());
		
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(baseUri).setBasePath(basePath).build();
//		 LocationTypeMappingsDTO as = RestAssured.given(reqSpec).when().get().as(LocationTypeMappingsDTO.class);
		 
		 
		
		String expected = dataLoader.getDesiredJsonResponse().toString();// Read from the dataFile.
		
		Response response = RestAssured.given(reqSpec).when().get();
		String actual = response.prettyPrint();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode exp_node = mapper.readTree(expected);
		JsonNode act_node = mapper.readTree(actual);
		
		assertEquals(exp_node, act_node);//
//		assertEquals(false, false);   STATUS CODES...
		
		softAssert.assertEquals(exp_node, act_node);
		softAssert.assertAll();
	}
	
	@Test(dataProvider = "userTestDataProvider")
	public void dhd_GetLTM_InvalidCharsTenant(final JSONObject testData) throws IOException {
		 
		 RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri(baseUri).setBasePath(basePath).build();
		 LocationTypeMappingsDTO as = RestAssured.given(reqSpec).when().get().as(LocationTypeMappingsDTO.class);
		 DataLoader dataLoader = new DataLoader(testData);
		 ObjectMapper mapper = new ObjectMapper();
		 mapper.setSerializationInclusion(Include.NON_NULL);
		// System.out.println(as.shortName);
		 String expected = dataLoader.getDesiredJsonResponse().toString();// Read from the dataFile.
			/*
			 * ObjectMapper mapper = new ObjectMapper(); String writeValueAsString =
			 * mapper.writeValueAsString(as);
			 * 
			 * String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(as);
			 * System.out.println(json);
			 */
		 LocationTypeMappingsDTO readValue = mapper.readValue(expected, LocationTypeMappingsDTO.class);
		 
		 System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readValue));
		 
	}
	
	@Test(dataProvider = "userTestDataProvider")
	public void dhd_GetLTM_NonExistentTenant() {

	}
}
