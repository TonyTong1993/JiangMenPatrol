package com.zzz.ecity.android.applibrary.service;

import android.location.Location;

import com.zzz.ecity.android.applibrary.model.GPSPositionBean;

public abstract class AReportPositionBeanBuilder {
	public GPSPositionBean buildGPSPositionBean(Location location){
		if(!isVialidLocation(location)){
			return null;
		}
		
		return buildCustomGPSPositionBean(location);
	}
	
	public boolean isVialidLocation(Location location){
		if(null == location){
			return false;
		}
		
		if(location.getAccuracy() > 1000){
			return false;
		}
		
		return true;
	}
	/**
	 * 构建坐标上报对象
	 * @return
	 */
	public abstract GPSPositionBean buildCustomGPSPositionBean(Location location);
}
