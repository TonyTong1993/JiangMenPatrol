package com.z3pipe.mobile.android.corssdk.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MsgInfo {
	/* GGA string 字符 ，基站编号（cell id）， UTC时间 */
	private String ggamsg, gstmsg, CellID, time;
	/* 纬度，经度，高程（海拔高+椭球高），水平精度 ，纬度精度误差，经度精度误差，高度精度误差 */
	private String latitude = "0.0", longitude = "0.0", antenna = "0.0",
			spheroid = "0.0", horizontalAccuracy = "0.0",
			latitudeError = "0.0", longitudeError = "0.0",
			altitudeError = "0.0";
	/* 定位质量，卫星数，差分龄期 */
	private String quality, number, differential_age;
	// 日期格式
	private SimpleDateFormat sfDateFormat = new SimpleDateFormat("yyyy-MM-dd",
			Locale.CHINA);
	String date = sfDateFormat.format(new Date());

	private DecimalFormat dFormat2 = new DecimalFormat("##0.000");

	public String getGgamsg() {
		return ggamsg;
	}

	public void setGgamsg(String ggamsg) {
		this.ggamsg = ggamsg;
	}

	public String getGstmsg() {
		return gstmsg;
	}

	public void setGstmsg(String gstmsg) {
		this.gstmsg = gstmsg;
	}

	public String getCellID() {
		return CellID;
	}

	public void setCellID(String cellID) {
		CellID = cellID;
	}

	public String getDate() {
		return date + " " + time.substring(0, 2) + ":" + time.substring(2, 4)
				+ ":" + time.substring(4, 6);
	}

	public void setDate(String date) {
		this.time = date;
	}

	public String getLatitude() {
		double a = Double.parseDouble(latitude) * 0.01;// 小数点左移两位
		String aString = String.valueOf(a);
		String degree = aString.substring(0, 2);

		String bString = "0." + aString.substring(aString.indexOf(".") + 1);
		double b = Double.parseDouble(bString) * 60;
		bString = String.valueOf(b);
		String mm = bString.substring(0, bString.indexOf("."));

		String cString = "0." + bString.substring(bString.indexOf(".") + 1);
		double c = Double.parseDouble(cString) * 60;
		cString = String.valueOf(c);
		String ss = cString.substring(0, cString.indexOf(".") + 7);

		String rltString = degree + "°" + mm + "′" + ss + "″";

		return rltString;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		double a = Double.parseDouble(longitude) * 0.01;// 小数点左移两位
		String aString = String.valueOf(a);
		String degree = aString.substring(0, 3);

		String bString = "0." + aString.substring(aString.indexOf(".") + 1);
		double b = Double.parseDouble(bString) * 60;
		bString = String.valueOf(b);
		String mm = bString.substring(0, bString.indexOf("."));

		String cString = "0." + bString.substring(bString.indexOf(".") + 1);
		double c = Double.parseDouble(cString) * 60;
		cString = String.valueOf(c);
		String ss = cString.substring(0, cString.indexOf(".") + 7);

		String rltString = degree + "°" + mm + "′" + ss + "″";

		return rltString;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAltitude() {
		double l = Double.parseDouble(antenna) + Double.parseDouble(spheroid);
		if (l == 0.0) {
			return "0.0";
		}
		return dFormat2.format(l);
	}

	public String getHorizontalAccuracy() {
		double l = Double.valueOf(horizontalAccuracy);
		if (l == 0.0) {
			return "0.0";
		}
		return dFormat2.format(l);
	}

	public void setHorizontalAccuracy(String horizontalAccuracy) {
		this.horizontalAccuracy = horizontalAccuracy;
	}

	public String getLatitudeError() {
		double l = Double.parseDouble(latitudeError);
		if (l == 0.0) {
			return "0.0";
		}
		return dFormat2.format(l);
	}

	public void setLatitudeError(String latitudeError) {
		this.latitudeError = latitudeError;
	}

	public String getLongitudeError() {
		double l = Double.parseDouble(longitudeError);
		if (l == 0.0) {
			return "0.0";
		}
		return dFormat2.format(l);
	}

	public void setLongitudeError(String longitudeError) {
		this.longitudeError = longitudeError;
	}

	public String getAltitudeError() {
		double l = Double.parseDouble(altitudeError);
		if (l == 0.0) {
			return "0.0";
		}
		return dFormat2.format(l);
	}

	public String getAntenna() {
		double l = Double.parseDouble(antenna);
		if (l == 0.0) {
			return "0.0";
		}
		return dFormat2.format(l);
	}

	public void setAntenna(String antenna) {
		this.antenna = antenna;
	}

	public String getSpheroid() {
		return spheroid;
	}

	public void setSpheroid(String spheroid) {
		this.spheroid = spheroid;
	}

	public void setAltitudeError(String altitudeError) {
		this.altitudeError = altitudeError;
	}

	public String getQuality() {
		String state = "未定位";
		if ("1".equalsIgnoreCase(quality)) {
			state = "单点";
		} else if ("2".equalsIgnoreCase(quality)) {
			state = "差分";
		} else if ("5".equalsIgnoreCase(quality)) {
			state = "浮点";
		} else if ("4".equalsIgnoreCase(quality)) {
			state = "固定";
		}
		
		return state;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getNumber() {
		int num = 0;
		num = Integer.parseInt(number);
		return String.valueOf(num);
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDifferential_age() {
		return differential_age;
	}

	public void setDifferential_age(String differential_age) {
		this.differential_age = differential_age;
	}
}
