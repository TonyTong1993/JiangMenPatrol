package com.zzz.ecity.android.applibrary.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import com.z3app.android.util.DateUtil;
import com.z3app.android.util.StringUtil;

public class DateUtilExt {
	private static final String TAG = DateUtilExt.class.getSimpleName();
	private static SimpleDateFormat sdf_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	private static SimpleDateFormat sdf_YYYYMMDD = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);
	private static SimpleDateFormat sdf_HHMMSS = new SimpleDateFormat(
			"HH:mm:ss", Locale.CHINA);
	
    public static String getCurrentTime() {
        String result = "";
        result = sdf_YMDHMS.format(new Date());
        return result;
    }
    
    /** 
     * 格式化时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        String result = "";
        
        if (null != date) {
            result = sdf_YMDHMS.format(date);
        }
        return result;
    }
	public static String getOffsetToServiceTime() {
		String date = DateUtil.changeLongToString(offsetToServiceTime());
		if (date == null || date.contains("1970")) {
            date = DateUtil.getDateEN();
        }
		return date;
	}

	public static long offsetToServiceTime() {
		long time = WebTimeTask.getInstance().getWebTime();
		String tmpdate = DateUtil.changeLongToString(time);
		if (tmpdate == null || tmpdate.contains("1970")) {
			time = System.currentTimeMillis();
		}
		return time;
	}

	public static String getServiceDate() {
		long time = WebTimeTask.getInstance().getWebTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date(time));
		if (date == null || date.contains("1970")) {
			time = System.currentTimeMillis();
		}

		return getDate(new Date(time));
	}

	/**
	 * 获得当前时间.
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getDate(Date date) {
		String result = "";
		if (null != date) {
			result = sdf_YYYYMMDD.format(date);
		}
		return result;
	}
	
    public static Date getDate2(String dateStr) {
        Date date = null;

        if ((null != dateStr) && (dateStr.trim().length() !=0)) {
            try {
                date = sdf_YYYYMMDD.parse(dateStr);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return date;
    }

	/**
	 * 获得当前时间.
	 * 
	 * @return HH:mm:ss
	 */
	public static String getTime(Date date) {
		String result = "";
		if (null != date) {
			result = sdf_HHMMSS.format(date);
		}
		return result;
	}

	public static boolean timeCompare(String time1, String time2,
			SimpleDateFormat format) {
		// 格式化时间
		try {
			Date beginTime = format.parse(time1);
			Date endTime = format.parse(time2);
			if ((endTime.getTime() - beginTime.getTime()) > 0) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<String> getDaysBeforeDays(int days) {
		return getDaysBeforeDays(offsetToServiceTime(), days);
	}

	public static List<String> getDaysBeforeDays(long time, int offday) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(time));
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<String> days = new ArrayList<String>();
		days.add(date);
		for (int i = 0; i < offday; i++) {
			c.add(Calendar.DATE, -1);

			Date tdate = c.getTime();
			String timeStr = format.format(tdate);
			days.add(timeStr);
		}
		return days;
	}

	/***
	 * 
	 * @param beginTime
	 * @param endTime
	 */
	public static long timeSubtraction(String beginTime, String endTime) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date begin = null;
		Date end = null;
		long between = 0;
		try {
			begin = dfs.parse(beginTime);
			end = dfs.parse(endTime);
			between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return between;
	}

	/***
	 * java 比较时间大小
	 * 
	 * @param beginTime
	 *            yyyy-MM-dd HH:mm:ss
	 * @param endTime
	 *            yyyy-MM-dd HH:mm:ss
	 * 
	 * @return -1 beginTime>endTime;0 beginTime = endTime ;1 beginTime<endTime
	 *         99 无效
	 */
	public static int timeCompare(String beginTime, String endTime) {

		if (StringUtil.isEmpty(beginTime) || StringUtil.isEmpty(endTime)) {
			return 99;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(beginTime));
			c2.setTime(df.parse(endTime));
		} catch (java.text.ParseException e) {
			System.err.println("格式不正确");
		}
		int result = c1.compareTo(c2);
		if (result == 0) {
			return 0;
		} else if (result < 0) {
			return -1;
		} else {
			return 1;
		}
	}
}
