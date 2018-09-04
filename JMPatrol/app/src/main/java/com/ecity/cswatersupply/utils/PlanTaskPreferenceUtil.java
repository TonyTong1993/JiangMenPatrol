package com.ecity.cswatersupply.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PlanTaskPreferenceUtil {
    public static String PREFERENCE_NAME = "plantask_ecity";

    /**
     * put string preferences
     * 
     * @param context
     * @param key
     *            The name of the preference to modify
     * @param value
     *            The new value for the preference
     * @return True if the new values were successfully written to persistent
     *         storage.
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     * 
     * @param context
     * @param key
     *            The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * get string preferences
     * 
     * @param context
     * @param key
     *            The name of the preference to retrieve
     * @param defaultValue
     *            Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws
     *         ClassCastException if there is a preference with this name that
     *         is not a string
     */
    public static String getString(Context context, String key,
            String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static void clearTasks(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
