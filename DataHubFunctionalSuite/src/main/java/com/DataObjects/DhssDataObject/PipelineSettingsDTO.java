

package com.DataObjects.DhssDataObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author: Abhishek.Singh
 * @date: Oct 18, 2021
 * @desc: This class will be used as POJO.
 */

public class PipelineSettingsDTO {
    @JsonProperty("edapTenantId")
    public String edapTenantId;
	public String tenantName;
	public String shortName;
	public List<TenantPipelineSettingsDTO> tenantPipelineSettings;

	public String getEdapTenantId() {
		return edapTenantId;
	}

	public void setEdapTenantId(String edapTenantId) {
		this.edapTenantId = edapTenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<TenantPipelineSettingsDTO> getTenantPipelineSettings() {
		return tenantPipelineSettings;
	}

	public void setTenantPipelineSettings(List<TenantPipelineSettingsDTO> tenantPipelineSettings) {
		this.tenantPipelineSettings = tenantPipelineSettings;
	}
}

