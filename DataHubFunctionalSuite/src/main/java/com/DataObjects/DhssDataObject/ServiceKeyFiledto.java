package com.DataObjects.DhssDataObject;

import java.io.Serializable;

public class ServiceKeyFiledto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tenantId;
	private String serviceAccountKeyType;
	private String serviceAccountKeyId;
	private String tenantName;

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getServiceAccountKeyType() {
		return serviceAccountKeyType;
	}

	public void setServiceAccountKeyType(String serviceAccountKeyType) {
		this.serviceAccountKeyType = serviceAccountKeyType;
	}

	public String getServiceAccountKeyId() {
		return serviceAccountKeyId;
	}

	public void setServiceAccountKeyId(String serviceAccountKeyId) {
		this.serviceAccountKeyId = serviceAccountKeyId;
	}
}
