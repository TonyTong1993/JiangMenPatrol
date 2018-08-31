package com.zzz.ecity.android.applibrary.utils;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.R;

import java.lang.reflect.Field;

/**
 * @author clown
 * @date 2017/10/2
 */
public class ResourceUtil {
    public static String getStringById(int resId, Object... formatArgs) {
        try {
            return MyApplication.getApplication().getApplicationContext().getResources()
                    .getString(resId, formatArgs);
        } catch (Exception e) {
            return "";
        }
    }

    public static int getColorById(int resId) {
        try {
            return MyApplication.getApplication().getApplicationContext().getResources()
                    .getColor(resId);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getStringById(int resId) {
        try {
            return MyApplication.getApplication().getApplicationContext().getResources()
                    .getString(resId);
        } catch (Exception e) {
            return "";
        }
    }

    public static String[] getArrayById(int resId) {
        try {
            return MyApplication.getApplication().getApplicationContext().getResources()
                    .getStringArray(resId);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getDimensionPixelSizeById(int resId) {
        try {
            return MyApplication.getApplication().getApplicationContext().getResources()
                    .getDimensionPixelSize(resId);
        } catch (Exception e) {
            return -1;
        }
    }

    public static Drawable getDrawableResourceById(int resId) {
        try {
            return MyApplication.getApplication().getApplicationContext().getResources()
                    .getDrawable(resId);
        } catch (Exception e) {
            return null;
        }
    }

    public static int[] getResouceIdArray(int resouceId) {
        TypedArray ar = MyApplication.getApplication().getApplicationContext().getResources().obtainTypedArray(resouceId);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }

        ar.recycle();
        return resIds;
    }

    public static int getDrawableResourceId(String name) {
        try {
            Field field = R.drawable.class.getField(name);
            int i = field.getInt(new R.drawable());
            return i;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * @param name xml资源名称
     * @return 根据资源名称反射得到xml资源ID
     */
    public static int getXmlResourceId(String name) {
        try {
            Field field = R.xml.class.getField(name);
            int i = field.getInt(new R.xml());
            return i;
        } catch (Exception e) {
            return -1;
        }
    }
}
