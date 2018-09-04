package com.ecity.medialibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * @author lotus
 *
 */
public class TimeMill2DateUtil {
    /**
     * 时间戳与日期转换工具
     * @param TimeMills 时间戳
     * @return
     */
    public static String TimeMill2Date(String TimeMills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Long milliseconds = Long.parseLong(TimeMills);
        Date date = new Date(milliseconds);
        String result = dateFormat.format(date);
        return result;
    }
    
}
