package com.zzz.ecity.android.applibrary.utils;

import java.lang.reflect.Field;

import com.zzz.ecity.android.applibrary.R;
public class ICONResourceUtil {
	public static int getDrawableResourceId(String name) {
		try {
			Field field = R.drawable.class.getField(name);
			int i = field.getInt(new R.drawable());
			return i;
		} catch (Exception e) {
			return -1;
		}
	}
}
