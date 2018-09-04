package com.ecity.cswatersupply.model;

import java.io.Serializable;

public class AppVerionInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isSuccess = false;
	private int versionCode = 0;
	private String versionName = "";
	private String pakage = "";
	private String description = "";
	private int type = 0;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getPakage() {
		return pakage;
	}

	public void setPakage(String pakage) {
		this.pakage = pakage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
