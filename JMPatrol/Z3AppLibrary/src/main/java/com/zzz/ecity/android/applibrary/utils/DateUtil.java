package com.zzz.ecity.android.applibrary.utils;

import com.ecity.android.log.LogUtil;
import com.z3app.android.util.StringUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = "DateUtil";
    private static SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private static SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private static SimpleDateFormat sf4 = new SimpleDateFormat("yyyy", Locale.CHINA);
    private static SimpleDateFormat sf5 = new SimpleDateFormat("MM", Locale.CHINA);
    private static SimpleDateFormat sf6 = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat sf7 = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private static SimpleDateFormat sf8 = new SimpleDateFormat("dd", Locale.CHINA);
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * getCurrentTime 获得当前时间.
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        String result = "";
        result = sf1.format(new Date());
        return result;

    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM-dd
     */
    public static String getCurrentDate() {
        String result = "";
        result = sf2.format(new Date());
        return result;
    }

    public static String getCurrentDate(Date date) {
        return sf2.format(date);
    }

    public static String getCurrentTime(Date date) {
        return sf1.format(date);
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM
     */
    public static String getMonthDate() {
        String result = "";
        result = sf3.format(new Date());

        return result;
    }


    /**
     * 获得当前时间.
     *
     * @return yyyy
     */
    public static String getYearDate() {
        String result = "";
        result = sf4.format(new Date());
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM-dd
     */
    public static String getDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf2.parse(dateStr);
                result = sf2.format(date);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy
     */
    public static String getYearDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf4.parse(dateStr);
                result = sf4.format(date);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM
     */
    public static String getMonthYearDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf3.parse(dateStr);
                result = sf3.format(date);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return MM
     */
    public static String getMonthDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf5.parse(dateStr);
                result = sf5.format(date);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * 获得时间.
     *
     * @return HH:mm:ss.
     */
    public static String getTime(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf6.parse(dateStr);
                result = sf6.format(date);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * @return
     */
    public static Date fromDate(String dateStr) {
        Date date = null;

        if (!StringUtil.isBlank(dateStr)) {
            try {
                date = sf2.parse(dateStr);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }

        return date;
    }

    public static String fromLong(long ltime) {
        Date date = new Date(ltime);
        return sf1.format(date);
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(Date date) {
        String result = "";
        if (null != date) {
            result = sf1.format(date);
        }

        return result;
    }

    /**
     * @param dateString yyyy-MM-dd HH:mm:ss.
     */
    public static String getDateTime(String dateString) {
        String result = "";
        if (!StringUtil.isBlank(dateString)) {
            try {
                Date date = sf1.parse(dateString);
                result = sf1.format(date);
            } catch (ParseException e) {
                LogUtil.e(TAG, e);
            }
        }

        return result;
    }

    /**
     * @return yyyy-MM-dd
     */
    public static String getDateStr(Date date) {
        String result = "";
        if (null != date) {
            result = sf2.format(date);
        }

        return result;
    }

    /**
     * @return Date
     */
    public static Date getDate1(String dateStr) {
        Date date = null;

        if (!StringUtil.isBlank(dateStr)) {
            try {
                date = sf1.parse(dateStr);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }

        return date;
    }

    public static Date getDate2(String dateStr) {
        Date date = null;

        if (!StringUtil.isBlank(dateStr)) {
            try {
                date = sf2.parse(dateStr);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }

        return date;
    }

    public static Date getBeforeDate(String dateStr, int days) {
        Date date = getDate2(dateStr);

        return getBeforeDate(date, days);
    }

    public static Date getBeforeDate(Date date, int days) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);

        return calendar.getTime();
    }

    public static String changeLongToString(long date) {
        Date dt = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            String str = format.format(dt);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long changeStringToLong(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = sdf.parse(date);
            return dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;
    }

    public static String getOffsetToServiceTime() {
        String date = changeLongToString(offsetToServiceTime());
        if (date == null || date.contains("1970"))
            date = getDateEN();
        return date;
    }

    public static long offsetToServiceTime() {
        long time = WebTimeTask.getInstance().getWebTime();
        String tmpdate = changeLongToString(time);
        if (tmpdate == null || tmpdate.contains("1970")) {
            time = System.currentTimeMillis();
        }
        return time;
    }

    /**
     * 将String类型的time转换成date "yyyy-MM-dd HH:mm:ss"
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 计算一个日期型的时间与当前时间相差多少
     *
     * @param startDate 开始日期
     * @param
     * @return
     */
    public static String twoDateDistance(Date startDate) {
        if (startDate == null) {
            return null;
        }
        Date endDate = Calendar.getInstance().getTime();
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000) {
            return timeLong / 1000 + "秒前";
        } else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 30 * 12) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 30;
            return timeLong + "月前";
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 30 / 12;
            return timeLong + "年前";
        }
    }

    public static String getYearFirstDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return sf2.format(cal.getTime());

    }

    public static String getYearLastDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return sf2.format(cal.getTime());

    }

    public static String getMonthFirstDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return sf2.format(cal.getTime());

    }

    public static String getMonthLastDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return sf2.format(cal.getTime());

    }

    public static List<String> getAllDaysInMonth(int year, int month, final boolean isAscending) {
        month -= 1;
        List<String> days = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        while (calendar.get(Calendar.MONTH) == month) {
            days.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (!isAscending) {
            Collections.reverse(days);
        }

        return days;
    }

    public static String getDaysOfMonth(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sf2.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = (calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return String.valueOf(days);
    }

    public static String getCurrentTimeOnly() {
        return sf6.format(new Date());
    }

    public static Date convertTimeString2Date(String timeStr) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int hoursBetween(Date early, Date late) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间
        calst.set(java.util.Calendar.HOUR_OF_DAY, early.getHours());
        calst.set(java.util.Calendar.MINUTE, early.getMinutes());
        calst.set(java.util.Calendar.SECOND, early.getSeconds());
        caled.set(java.util.Calendar.HOUR_OF_DAY, late.getHours());
        caled.set(java.util.Calendar.MINUTE, late.getMinutes());
        caled.set(java.util.Calendar.SECOND, late.getSeconds());
        //得到两个日期相差的小时
        int hours = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600;

        return hours;
    }

    public static int minutesBetween(Date early, Date late) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间
        calst.set(java.util.Calendar.HOUR_OF_DAY, early.getHours());
        calst.set(java.util.Calendar.MINUTE, early.getMinutes());
        calst.set(java.util.Calendar.SECOND, early.getSeconds());
        caled.set(java.util.Calendar.HOUR_OF_DAY, late.getHours());
        caled.set(java.util.Calendar.MINUTE, late.getMinutes());
        caled.set(java.util.Calendar.SECOND, late.getSeconds());
        //得到两个日期相差的分钟
        int hours = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 60;

        return hours;
    }

    /**
     * getCurrentTime 获得当前小时和分钟.
     *
     * @return HH:mm
     */
    public static String getCurrentHourMin() {
        String result = "";
        result = sf7.format(new Date());
        return result;

    }

    /**
     * 判断是否为今天
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isBeforeToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为今天
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    public static int getDate(String dateString, int dateType) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(dateString);
        cal.setTime(date);
        return cal.get(dateType);
    }

    /**
     * 获取当前月份的第一天
     *
     * @return yyyy-MM-01
     */
    public static String getCurrentMonthAndDayTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return DateUtil.getDateStr(c.getTime());
    }

    /**
     * 获取明天的时间
     */
    public static String getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        return sf2.format(date);
    }

    public static boolean compare(String startTime, String endTime) {
        long startDate = getDate2(startTime).getTime();
        long endDate = getDate2(endTime).getTime();
        long timeLong = (endDate - startDate) / 1000 / 60 / 60 / 24;
        if (timeLong >= 1) {
            return true;
        }
        return false;
    }

    public static String fromLong2(long ltime) {
        Date date = new Date(ltime);
        return sf2.format(date);
    }

    /***
     * 获取两个时间段的时间 以天为单位
     * @return
     */
    public static List<Date> getIntervalDates(String start, String end) {
        List<Date> dates = new ArrayList<Date>();
        String target = start;
        while (compare(target, end)) {
            dates.add(getDate2(target));
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate2(target));
            cal.add(Calendar.DAY_OF_YEAR, 1);
            target = sf2.format(cal.getTime());
        }

        return dates;
    }

    /**
     * 获取当天前或者后i个月的时间字符串
     */
    public static String getMonths(int i) {
        Calendar ca = Calendar.getInstance();
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
        ca.add(Calendar.MONTH, i);
        //ca.add(Calendar.DATE, 0);

        Date date = ca.getTime();
        return sf3.format(date);
    }

    /**
     * 获得输入时间的小时和分钟.
     *
     * @return HH:mm
     */
    public static String getTimeHHmm(String string) {
        try {
            return sf7.format(stringToDate(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得输入时间的小时和分钟.
     *
     * @return HH:mm:SS
     */
    public static String getTimeHHmmSS(String string) {
        try {
            return sf6.format(stringToDate(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        try {
            long now = sf7.parse(curTime).getTime();
            long start = sf7.parse(args[0]).getTime();
            long end = sf7.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
    }

    /**
     * 当前时间是否大于选择的时间
     *
     * @param setTime
     * @return
     */
    public static boolean isCurrentTimeBeyondSetTime(String setTime) {
        String currentTime = getCurrentDate();
        String[] currentTimes = currentTime.split("-");
        String[] setTimes = setTime.split("-");

        int yearGap = Integer.parseInt(currentTimes[0]) - Integer.parseInt(setTimes[0]);
        int monthGap = Integer.parseInt(currentTimes[1]) - Integer.parseInt(setTimes[1]);
        int dayGap = Integer.parseInt(currentTimes[2]) - Integer.parseInt(setTimes[2]);

        if (yearGap == 0) {
            if (monthGap == 0) {
                if (dayGap < 0) {
                    return false;
                } else {
                    return true;
                }
            } else if (monthGap < 0) {
                return false;
            } else {
                return true;
            }
        } else if (yearGap < 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 获取当前时间之前或之后几分钟 minute
     *
     * @param minute -5 5分钟之前  5 5分钟之后
     * @return
     */
    public static String getTimeByMinute(int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, minute);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }

    /**
     * 获取当前时间之前或之后几天 day
     *
     * @param day -5 5天之前  5 5天之后
     * @return
     */
    public static String getTimeByDay(int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, day);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    /**
     * 获取当天 前几天或之后几天 day
     *
     * @param day -5 5天之前  5 5天之后
     * @return
     */
    public static String getDayBefore(int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, day);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 比较两个时间大小
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2, String dateFormat) {


        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


}
