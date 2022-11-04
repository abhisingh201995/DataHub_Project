package com.DataObjects.DhssDataObject;

public class Seedlogindto {
	
	private String access_token;
	private String tenantid;
	private String tenantName;
	private String XSRF_Token;
	public String getAccess_token() {
		return access_token;
	}
	
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public String getTenantid() {
		return tenantid;
	}
	
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getXSRF_Token() {
		return XSRF_Token;
	}

	public void setXSRF_Token(String xSRF_Token) {
		this.XSRF_Token = xSRF_Token;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
}
