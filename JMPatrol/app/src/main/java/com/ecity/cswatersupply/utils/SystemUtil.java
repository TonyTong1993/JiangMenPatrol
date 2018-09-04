package com.ecity.cswatersupply.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.ActivityManager;
import android.content.Context;

public class SystemUtil {

    /**
     * Get the usage of Memory
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
}
