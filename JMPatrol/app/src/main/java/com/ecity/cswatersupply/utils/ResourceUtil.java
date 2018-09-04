package com.ecity.cswatersupply.utils;

import java.lang.reflect.Field;

import android.graphics.drawable.Drawable;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;

public class ResourceUtil {
	public static String getStringById(int resId, Object... formatArgs) {
        try {
            return HostApplication.getApplication().getApplicationContext().getResources()
                    .getString(resId, formatArgs);
        } catch (Exception e) {
            return "";
        }
    }
	
	public static int getColorById(int resId) {
        try {
            return HostApplication.getApplication().getApplicationContext().getResources()
                    .getColor(resId);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public static String getStringById(int resId) {
        try {
            return HostApplication.getApplication().getApplicationContext().getResources()
                    .getString(resId);
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String[] getArrayById(int resId) {
        try {
            return HostApplication.getApplication().getApplicationContext().getResources()
                    .getStringArray(resId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static int getDimensionPixelSizeById(int resId) {
        try {
            return HostApplication.getApplication().getApplicationContext().getResources()
                    .getDimensionPixelSize(resId);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public static Drawable getDrawableResourceById(int resId) {
        try {
            return HostApplication.getApplication().getApplicationContext().getResources()
                    .getDrawable(resId);
        } catch (Exception e) {
            return null;
        }
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
	 * @param name
	 *            xml资源名称
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
