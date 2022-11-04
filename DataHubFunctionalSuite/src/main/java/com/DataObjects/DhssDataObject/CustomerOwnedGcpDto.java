package com.DataObjects.DhssDataObject;

import java.io.Serializable;

public class CustomerOwnedGcpDto implements Serializable {


	private String tenantId;
	private String gcpProjectId;
	private String gcpProjectDesc;
	private String serviceAccountKey;
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getGcpProjectId() {
		return gcpProjectId;
	}
	public void setGcpProjectId(String gcpProjectId) {
		this.gcpProjectId = gcpProjectId;
	}
	public String getGcpProjectDesc() {
		return gcpProjectDesc;
	}
	public void setGcpProjectDesc(String gcpProjectDesc) {
		this.gcpProjectDesc = gcpProjectDesc;
	}
	public String getServiceAccountKey() {
		return serviceAccountKey;
	}
	public void setServiceAccountKey(String serviceAccountKey) {
		this.serviceAccountKey = serviceAccountKey;
	}
	
}
