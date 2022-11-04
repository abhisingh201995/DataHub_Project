

package com.DataObjects.DhssDataObject;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: amit.chauhan
 * @date: Oct 13, 2021
 * @desc: This class will be used as POJO.
 */

	public final class LocationTypeMappingsDTO{
		public int edapTenantId;
		public String tenantName;
	    public String shortName;
	    public LocationTypeMappingsAssignmentList locationTypeMappingsAssignmentList;
	    public List<LocationTypeMapping> locationTypeMappings;
	
	
	    public static class LocationTypeMappingsAssignmentList{
	    	public String minimum;
			public String maximum;
	    }
	    
	    public static class LocationTypeMapping{
	    	public int locationTypeId;
			public String locationTypeName;
	    	public String locationTypeDescription;
	    	public String locationTypeCategory;
	    	public String locationTypeOrder;
	    	public String locationTypeMappingAssignment;
	    }
	
	}

