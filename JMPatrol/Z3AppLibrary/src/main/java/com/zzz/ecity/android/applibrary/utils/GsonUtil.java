package com.zzz.ecity.android.applibrary.utils;


import com.ecity.android.log.LogUtil;
import com.google.gson.Gson;

/**
 * @author clown
 * @date 2017/10/2
 */
public class GsonUtil {
    private static final Gson GSON = new Gson();

    public static String toJson(Object obj) {
        try {
            return GSON.toJson(obj, obj.getClass());
        } catch (Exception e) {
            LogUtil.e("GsonUtil", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, Class<T> cls) throws Exception {
        try {
            return GSON.fromJson(json, cls);
        } catch (Exception e) {
            LogUtil.e("GsonUtil", e);
            throw new Exception(e);
        }
    }
}
