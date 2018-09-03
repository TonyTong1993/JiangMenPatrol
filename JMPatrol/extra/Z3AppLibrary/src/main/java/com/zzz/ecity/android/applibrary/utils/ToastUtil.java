package com.zzz.ecity.android.applibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.zzz.ecity.android.applibrary.MyApplication;


public class ToastUtil {
    private static Context context = MyApplication.getApplication();
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    public static void show(String msg, int length) {
        showMessage(context, msg, length);
    }

    public static void showShort(final String msg) {
        showMessage(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(final String msg) {
        showMessage(context, msg, Toast.LENGTH_LONG);
    }

    public static void showShort(final int msg) {
        showMessage(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(final int msg) {
        showMessage(context, msg, Toast.LENGTH_LONG);
    }

    public static void showMessage(final Context context, final int msg, final int length) {
        showMessage(context, context.getString(msg), length);
    }

    public static void showMessage(final Context context, final String msg, final int length) {
        if ((context == null) || (TextUtils.isEmpty(msg))) {
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
                                toast.cancel();
                                toast = Toast.makeText(context, msg, length);
                            } else {
                                toast = Toast.makeText(context, msg, length);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    public static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}