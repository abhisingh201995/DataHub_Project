package com.DataObjects.DhssDataObject;

public class Adminlogindto {
	
	private String adminSessionID;
	private String XSRF_Token;

	public String getAdminSessionId() {
		return adminSessionID;
	}

	public void setAdminSessionId(String adminSessionID) {
		this.adminSessionID = adminSessionID;
	}

	public String getXSRF_Token() {
		return XSRF_Token;
	}

	public void setXSRF_Token(String xSRF_Token) {
		this.XSRF_Token = xSRF_Token;
	}

}
