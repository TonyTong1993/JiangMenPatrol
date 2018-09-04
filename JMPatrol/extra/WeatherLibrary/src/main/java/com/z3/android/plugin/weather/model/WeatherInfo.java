package com.z3.android.plugin.weather.model;

import java.io.Serializable;

/**
 * The Model of weather
 * @author SunShanai
 * @version  In March 17, 2015 10:27:45 p.m.
 */
public class WeatherInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String cityName = "";
	private String fchh = "";//System update time
	private String date = "";
	private String week = "";
	private String temperature = "";
	private String weather = "";//weather condition
	private String wind = "";
	private String allInfo = "";
	
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getFchh() {
		return fchh;
	}
	public void setFchh(String fchh) {
		this.fchh = fchh;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	public String getAllInfo() {
		return allInfo;
	}
	public void setAllInfo(String allInfo) {
		this.allInfo = allInfo;
	}
	
}
