package com.ecity.mobile.android.cityselector.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceInfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String proviceName;
	private String cityName;
	private String districtName;
	private String zipCode;
	
	public PlaceInfoModel()
	{
		proviceName = "";
		cityName = "";
		districtName = "";
		zipCode = "";
	}
	
	public PlaceInfoModel(String proviceName,String cityName,String districtName,String zipCode)
	{
		this.proviceName = proviceName;
		this.cityName = cityName;
		this.districtName = districtName;
		this.zipCode = zipCode;
	}
	
	public String getProviceName() {
		return proviceName;
	}

	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("proviceName", String.valueOf(proviceName));
		map.put("cityName", String.valueOf(cityName));
		map.put("districtName", String.valueOf(districtName));
		map.put("zipCode", String.valueOf(zipCode));
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}
	
	public boolean buildFromJson(String jsonString)
	{
		if(null == jsonString ||jsonString.isEmpty())
			return false;
		boolean flg = false;
		try {
			JSONObject json = new JSONObject(jsonString);
		
			try {
				@SuppressWarnings("unchecked")
				Iterator<String> iterator = json.keys();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					if (key.equalsIgnoreCase("proviceName"))
					{
						proviceName = json.getString(key);
					}else if (key.equalsIgnoreCase("cityName"))
					{
						cityName = json.getString(key);
					}else if (key.equalsIgnoreCase("districtName"))
					{
						districtName = json.getString(key);
					}else if (key.equalsIgnoreCase("zipCode"))
					{
						zipCode = json.getString(key);
					}
				}
				flg = true;
			} catch (Exception e) {
				flg = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flg = false;
		}
		return flg;
	}
	
}