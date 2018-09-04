package com.ecity.mobile.android.bdlbslibrary.utils;

import android.util.Base64;

public class StringDecodeUtil {
    public static String getFromBase64(String s) {
        try {
            byte[] b = Base64.decode(s, Base64.DEFAULT);
            return new String(b);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
