package com.example.zzht.jmpatrol.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author clown
 * @date 2017/10/2
 */
public class SystemUtils {
    /**
     * Get the usage of Memory
     *
     * @param context
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";
        String str2 = "";
        double memTotal = 0;
        double memFree = 0;

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);

            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("MemTotal")) {
                    memTotal = Double.valueOf(str2.split(":")[1].trim().split("k")[0].trim()) * 1024;

                    break;
                }
            }

            localBufferedReader.close();
            fr.close();

            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);

            memFree = mi.availMem;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (int) ((1 - (memFree / memTotal)) * 100) + "%";
    }

    /**
     * Get the usage of CPU
     */
    public static String readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");
            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            Thread.sleep(360);
            reader.seek(0);

            load = reader.readLine();
            reader.close();
            toks = load.split(" ");
            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return ((int) (100 * (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)))) + "%";

        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    /**
     * 可以在非主线程执行的toast
     *
     * @param context  c
     * @param text     t
     * @param duration d
     */
    public static void showToast(Context context, String text, int duration) {
        try {
            Looper.prepare();
            Toast.makeText(context, text, duration).show();
            Looper.loop();
        } catch (Exception e) {
            try {

                Toast.makeText(context, text, duration).show();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    /**
     * 启动Activity
     *
     * @param context c
     * @param clazz   c
     */
    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz, String type, String str) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(type, str);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz, String type, int i) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(type, i);
        context.startActivity(intent);
    }

    public static void startSingeleActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
    /**
     * 用来获取版本信息
     *
     * @param context c
     * @return string
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
     /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
