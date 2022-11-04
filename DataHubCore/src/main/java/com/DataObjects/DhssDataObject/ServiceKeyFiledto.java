package com.DataObjects.DhssDataObject;

import java.io.Serializable;

public class ServiceKeyFiledto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tenantName;
	private String serviceAccountKeyType;
	private String serviceAccountKeyId;

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
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
