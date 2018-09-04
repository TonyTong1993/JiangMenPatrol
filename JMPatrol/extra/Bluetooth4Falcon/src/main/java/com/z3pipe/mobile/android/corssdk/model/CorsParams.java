package com.z3pipe.mobile.android.corssdk.model;

public class CorsParams {
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMountpoint() {
		return mountpoint;
	}

	public void setMountpoint(String mountpoint) {
		this.mountpoint = mountpoint;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	private String port;
	private String mountpoint;
	private String userId;
	private String psw;

	public CorsParams() {

	}

}
