package com.z3pipe.mobile.android.corssdk.model;

public class SatelliteInfo {
	/***
	 *  定位状态(解质量)
	 */
	private EQulityType quality;
	private String latitude;
	private String longitude;
	private String date;
	private String counts;
	/**
	 * 水平精度因子
	 */
	private String hdop;
	/***
	 * 椭球高度
	 */
	private String altitude;
	/**
	 * 查分期龄
	 */
	private String DGPS;
	public EQulityType getQuality() {
		return quality;
	}
	public void setQuality(EQulityType quality) {
		this.quality = quality;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public String getHdop() {
		return hdop;
	}
	public void setHdop(String hdop) {
		this.hdop = hdop;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getDGPS() {
		return DGPS;
	}
	public void setDGPS(String dGPS) {
		DGPS = dGPS;
	}
}
