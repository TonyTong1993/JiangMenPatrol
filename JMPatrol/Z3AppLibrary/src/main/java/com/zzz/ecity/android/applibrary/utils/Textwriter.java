package com.zzz.ecity.android.applibrary.utils;

import android.os.Environment;

import com.ecity.android.log.LogUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.MyApplication;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Textwriter {
    private volatile static int recordCount = 0;
    private volatile static int recordIndex = 0;
    private static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        boolean hasSD = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        String SDPATH = Environment.getExternalStorageDirectory().getPath();
        String fileName = null;
        if (hasSD) {
            String path = SDPATH + "//Z3SDK//PipeGPS//Logs//";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName = path + "log-pipedemo-" +recordIndex+"-"+date + ".txt";
        }
        recordCount++;
        if(recordCount>1000){
            recordCount = 0;
            recordIndex++;
        }
        return fileName;
    }
    
    public static void write(String content) {
        if (null == content) {
            return;
        }

        Date date1 = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = df.format(date1);

        String msg = "["+timeStr + "]\r\n";
        msg += content;
        msg += "\r\n";
        try {
            FileWriter writer = new FileWriter(getFileName(), true);
            writer.write(msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void write(String path, String content) {
        if (StringUtil.isBlank(path) || StringUtil.isBlank(content)) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(path, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 从文件内读取字符Json字符传数据
     * @param path 文件路径
     * @return JSONObject对象
     */
    public static JSONObject readJsonString(String path) {
        File file = new File(path);

        if (null == file || !file.exists()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            //从文件读取字符串
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(0);
            byte[] buffer = new byte[(int) raf.length()];
            raf.read(buffer);
            String contentStr = new String(buffer,"UTF-8");
            if (com.ecity.android.db.utils.StringUtil.isBlank(contentStr)) {
                return null;
            }
            //字符串转JSON
            JSONObject objJson = new JSONObject(contentStr);
            return objJson;
        } catch (Exception e) {
            LogUtil.e(MyApplication.getApplication(), e);
            return null;
        }finally{
            try {
                raf.close();
            } catch (IOException e) {
                LogUtil.e(MyApplication.getApplication(), e);
                return null;
            }
        }
    }
}
