package com.z3pipe.mobile.android.corssdk.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

import com.z3pipe.mobile.android.corssdk.model.EQulityType;

public class NMEAParser {
	private static String TAG = "com.enrique.bluetooth4falcon.NMEAPraser";

	private static DecimalFormat dFormat2 = new DecimalFormat("##0.000");
	private static SimpleDateFormat sfDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);
	static String date = sfDateFormat.format(new Date());
	static String GGAMsg;

	public static String[] ParseGGA(String msg) {
		// Log.d("parser", msg);
		if (msg.equals(null)) {
			return null;
		}

		String[] items = msg.split(",");

		items[0] = msg;
		GGAMsg = msg;

		return items;
	}

	public static String[] ParseGST(String msg) {
		// Log.d("parser", msg);
		if (msg.equals(null)) {
			return null;
		}

		String[] items = msg.split(",");
		items[0] = msg;

		return items;
	}

	public static String[] ParseBPQ(String msg) {
		if (msg.equals(null)) {
			return null;
		}

		String[] items = msg.split(",");
		items[0] = msg;

		return items;
	}

	public static String getLatitude(String latitude) {
		try {
			if (latitude.equals(null) || latitude.equals("")) {
				return "0°0′0″";
			}

			String aString = String.valueOf(latitude);
			String degree = aString.substring(0, 2);

			String mm = aString.substring(2, 4);

			String cString = "0." + aString.substring(aString.indexOf(".") + 1);
			double c = Double.parseDouble(cString) * 60;
			cString = String.valueOf(c);

			try {
				cString = cString.substring(0, cString.indexOf(".") + 7);
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				Log.e(TAG, e.getMessage());
			}

			String rltString = degree + "°" + mm + "′" + cString + "″";

			return rltString;
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parser", "getLatitude:" + latitude + e.getMessage());
			return "0°0′0″";
		}
	}

	public static String getLongitude(String longitude) {
		try {
			if (longitude.equals(null) || longitude.equals("")) {
				return "0°0′0″";
			}

			String aString = String.valueOf(longitude);
			String degree = aString.substring(0, 3);

			String mm = aString.substring(3, 5);

			String cString = "0." + aString.substring(aString.indexOf(".") + 1);
			double c = Double.parseDouble(cString) * 60;
			cString = String.valueOf(c);

			try {
				cString = cString.substring(0, cString.indexOf(".") + 7); // 取小数点后六位
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				Log.e(TAG, e.getMessage());
			}

			String rltString = degree + "°" + mm + "′" + cString + "″";

			return rltString;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			Log.d("parser", "getLongitude:" + longitude + e.getMessage());
			return "0°0′0″";
		}
	}

	public static String getDate(String time) {
		try {
			if (time.equals(null) || time.equals("")) {
				return "";
			}
			int hour = Integer.valueOf(time.substring(0, 2)) + 8;// 转换成北京时间
			if (hour >= 24) {
				hour -= 24;
			}
			return date + " " + String.valueOf(hour) + ":"
					+ time.substring(2, 4) + ":" + time.substring(4, 6);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parser", "getDate:" + time);
			return "";
		}
	}

	public static EQulityType getQuality(String quality) {
		EQulityType qulity = EQulityType.NOTPOSITIONED;
		if ("1".equalsIgnoreCase(quality)) {
			qulity = EQulityType.SINGLEPOINT;;
		} else if ("2".equalsIgnoreCase(quality)) {
			qulity = EQulityType.PSEUDORANGE;
		} else if ("5".equalsIgnoreCase(quality)) {
			qulity = EQulityType.FLOATINGPOINT;
		} else if ("4".equalsIgnoreCase(quality)) {
			qulity = EQulityType.FIXED;
		}
		
		return qulity;
	}

	public static String getNumber(String number) {
		try {
			int num = 0;
			if (number.equals(null) || number.equals("")) {
				return "0";
			}
			num = Integer.parseInt(number);
			return String.valueOf(num);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parser", "getNumber:" + number);
			return "0";
		}

	}

	/*
	 * 椭球高=天线高+海拔高
	 */
	public static String getAltitude(String antenna, String spheroid) {
		try {
			if (antenna.equals(null) || antenna.equals("")) {
				antenna = "0.0";
			}
			if (spheroid.equals(null) || spheroid.equals("")) {
				spheroid = "0.0";
			}
			double l = Double.parseDouble(antenna)
					+ Double.parseDouble(spheroid);
			if (l == 0.0) {
				return "0.0";
			}
			return dFormat2.format(l);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parser", "getAltitude:" + antenna + spheroid);
			return "0";
		}

	}

	public static String getDgps(String dgps) {
		if (dgps.equals(null) || dgps.equals("")) {
			return "无";
		}
		return dgps;
	}

	public static String getHorizontalError(String latiError, String longiError) {
		try {
			if (latiError.equals(null) || latiError.equals("")) {
				latiError = "0.0";
			}
			if (longiError.equals(null) || longiError.equals("")) {
				longiError = "0.0";
			}
			double latierror = Double.valueOf(latiError);
			double longierror = Double.valueOf(longiError);

			double horizontalError = Math.sqrt(Math.pow(latierror, 2)
					+ Math.pow(longierror, 2));

			return dFormat2.format(horizontalError);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parser", "getHorizontalError:" + latiError + longiError);
			return "0";
		}
	}

	public static String getWholeGGAMsg() {
		if (null != GGAMsg) {
			return GGAMsg + "\r\n";
		}
		return "";
	}

	public static double string2Double(String arg) {
		double dl = 0.0;
		if (!arg.isEmpty() && arg != null) {
			dl = Double.parseDouble(arg);
		}
		return dl;
	}

	public static float string2Float(String arg) {
		float fl = 0;
		if (!arg.isEmpty() && arg != null) {
			fl = Float.parseFloat(arg);
		}
		return fl;
	}
	
	public static double parseLatitude(String latitude) {
		try {
			int deg = Integer.parseInt(latitude.substring(0, 2));
			double min = Double.parseDouble(latitude.substring(2));
			return deg + min / 60.0D;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public static double parseLongitude(String longtitude) {
		try {
			int deg = Integer.parseInt(longtitude.substring(0, 3));
			double min = Double.parseDouble(longtitude.substring(3));
			return deg + min / 60.0D;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0.0;
	}
}
