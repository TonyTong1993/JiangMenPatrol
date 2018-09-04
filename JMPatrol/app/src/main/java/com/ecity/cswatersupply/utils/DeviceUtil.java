package com.ecity.cswatersupply.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class DeviceUtil {
    /**
     * @return  
     */
    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获得当前屏幕亮度的模式    
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public static int getScreenMode(Activity activity) {
        return Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
    }

    /**
     * @param activity
     * @return paramInt 0-255 dark to bright
     */
    public static int getScreenBrightness(Activity activity) {
        return Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    /**
     * 设置当前屏幕亮度的模式    
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public static void setScreenMode(Activity activity, int value) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    /**
     * @param activity
     * @param value 0-255 dark to bright
     */
    public static void saveScreenBrightness(Activity activity, int value) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
    }

    /**
     * @param activity
     * @param value 0-255 dark to bright
     */
    public static void setScreenBrightness(Activity activity, int value) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = value / 255.0F;
        localWindow.setAttributes(localLayoutParams);
    }

    public static void resetScreenBrightness(Activity activity) {
        setScreenBrightness(activity, -1);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        final Point point = new Point();
        Display display = wm.getDefaultDisplay();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }

        return point.x;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        final Point point = new Point();
        Display display = wm.getDefaultDisplay();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }

        return point.y;
    }
}
