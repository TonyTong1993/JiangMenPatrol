package com.ecity.cswatersupply.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.log.LogUtil;

/**
 * 缓存
 * 
 * @author gaokai
 *
 */
public class CacheManager {
    // wifi环境下缓存时间为10分钟
    private static final long WIFI_CACHE_TIME = 10 * 60 * 1000;
    // 其他网络环境缓存1小时
    private static final long OTHER_CACHE_TIME = 60 * 60 * 1000;

    /*
     * 保存缓存对象
     * 
     * @param context
     * 
     * @param ser 序列化对象
     * 
     * @param file
     * 
     * @return
     */
    public static boolean saveObject(Context context, Serializable ser,
            String file) {
        if (context == null) {
            return false;
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            LogUtil.e(context, e);
            return false;
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                LogUtil.e(context, e);
            }
            try {
                fos.close();
            } catch (IOException e) {
                LogUtil.e(context, e);
            }
        }
    }

    /*
     * 读取缓存文件
     * 
     * @param context
     * 
     * @param file
     * 
     * @return
     */
    public static Serializable readObject(Context context, String file) {
        if (!isExistDataCache(context, file)) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            LogUtil.e(context, e);
            if (e instanceof EOFException) {
                // 执行成功
            } else {
                // 反序列化失败，删除该缓存
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (Exception e) {
                LogUtil.e(context, e);
            }
        }
        return null;
    }

    /*
     * 判断缓存是否存在
     */
    private static boolean isExistDataCache(Context context, String file) {
        if (context == null) {
            return false;
        }
        File data = context.getFileStreamPath(file);
        return data.exists();
    }

    public static boolean deleteCache(Context context, String file){
        if (context == null) {
            return false;
        }
        File data = context.getFileStreamPath(file);
        if (data.exists()) {
           return data.delete();
        }
        return true;
    }
    /*
     * 判断缓存是否有效
     * 
     * @param context
     * 
     * @param file
     * 
     * @return
     */
    private static boolean isCacheDataFailure(Context context, String file) {
        File data = context.getFileStreamPath(file);
        if (!data.exists()) {
            return false;
        }
        boolean failure = false;
        long existTime = System.currentTimeMillis() - data.lastModified();
        if (NetWorkUtil.isWifi(context)) {
            failure = (existTime > WIFI_CACHE_TIME);
        } else {
            failure = (existTime > OTHER_CACHE_TIME);
        }
        // 缓存如果失效，删除
        if (failure) {
            data.delete();
        }
        return failure;
    }

    public static boolean isReadCacheData(Context context, boolean refresh,
            String key) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            return false;
        }
        // 若不是主动刷新，缓存存在且还没失效，优先取缓存
        return !refresh && isExistDataCache(context, key)
                && !isCacheDataFailure(context, key);
    }
}
