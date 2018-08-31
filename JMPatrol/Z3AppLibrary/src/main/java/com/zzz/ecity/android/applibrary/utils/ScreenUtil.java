package com.zzz.ecity.android.applibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by bruce on 14-11-6.
 */
public class ScreenUtil {

	/**
	 * dp To px
	 * @param resources
	 * @param dp
	 * @return
	 */
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    /**
     * sp to px
     * @param resources
     * @param sp
     * @return
     */
    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
    /**
     * dp to px 
     * @param context
     * @param dp
     * @return
     */
	public static float dpTopx(Context context, float dp) {
		if (context == null) {
			return -1;
		}
		return dp * context.getResources().getDisplayMetrics().density;
	}

    /**
     * dp to px
     * @param context
     * @param dp
     * @return
     */
	public static float dp2px(Context context, float dp) {
		if (context == null) {
			return -1;
		}
		return dp * context.getResources().getDisplayMetrics().density;
	}
	/**
	 * 此方法描述的是： 单位转换
	 */
	public static float pxTodp(Context context, float px) {
		if (context == null) {
			return -1;
		}
		return px / context.getResources().getDisplayMetrics().density;

	}

	/**
	 * 此方法描述的是： 获得当前屏幕宽度
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 * @param context
	 * @param dpValue
	 *            dp值
	 * @return 返回像素值
	 */
	public static int dipTopx(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * 
	 * @param context
	 * @param pxValue
	 *            像素值
	 * @return 返回dp值
	 */
	public static int pxTodip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
