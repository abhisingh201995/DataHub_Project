package com.DataObjects.DhssDataObject;

import java.util.List;

//@SuppressWarnings("unused")
public final class CustomDriverMappingsDTO {

	private int edapTenantId;
	private String tenantName; 
	private String shortName;
	private customDriverMappingsAssignmentList customDriverMappingsAssignmentList;
	List<customDriverMappings> customDriverMappings;
	
	public class customDriverMappingsAssignmentList {
		public String minimum;
		public String maximum;
	}
	
	public class customDriverMappings{
		
		public int customDriverId;
		public String customDriverName;
    	public String customDriverCurrency; // i think this shud be boolean
    	public String customDriverDecimalPos;
    	public String customDriverMappingAssignment;
	}
}
