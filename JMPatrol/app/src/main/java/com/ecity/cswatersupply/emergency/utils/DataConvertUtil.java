package com.ecity.cswatersupply.emergency.utils;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;


public class DataConvertUtil {
    
    public static String normalize(String str) {
        StringBuffer buffer = new StringBuffer();
        double number = 0;
        if("".equals(str)) {
            return buffer.append(ResourceUtil.getStringById(R.string.no_data)).toString();
        }
        if(str.contains("E")) {
            String[] temp = str.split("E");
            int len = Integer.valueOf(temp[1]);
            number = Double.valueOf(temp[0]) * 10;
            for(int i = 0; i < len-1; i++) {
                number = number * 10;
            }
        } else {
            number = Double.valueOf(str);
        }
        if(number < 0) {
            return buffer.append(ResourceUtil.getStringById(R.string.no_data)).toString();
        }
        if(number >= 0 && number < 10000) {
            buffer.append(String.valueOf((int)number));
        }
        if(number >= 10000 && number < 100000000) {
            int temp = (int) (number / 10000);
            buffer.append(String.valueOf(temp)).append(ResourceUtil.getStringById(R.string.unit_wan));
        }
        if(number >= 100000000) {
            int temp = (int) (number / 100000000);
            buffer.append(String.valueOf(temp)).append(ResourceUtil.getStringById(R.string.unit_yi));
        }
        return buffer.toString();
    }
}
