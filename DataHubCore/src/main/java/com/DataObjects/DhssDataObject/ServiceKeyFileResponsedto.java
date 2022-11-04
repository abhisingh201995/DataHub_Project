package com.DataObjects.DhssDataObject;

import java.io.Serializable;

public class ServiceKeyFileResponsedto implements Serializable {

	private String type;
	private String project_id;
	private String private_key_id;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getPrivate_key_id() {
		return private_key_id;
	}

	public void setPrivate_key_id(String private_key_id) {
		this.private_key_id = private_key_id;
	}
}
