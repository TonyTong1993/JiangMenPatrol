package com.example.zzht.jmpatrol.global;

import android.app.Application;
import android.content.Context;


import java.util.List;

public class HostApplication extends Application {
    private static final int MAX_DIR_SIZE_MB = 100;// 文件路径所占最大空间
    private static final int MAX_FILE_EXSIST_DAY = 20;// 单个文件存在的最长时间（天）
    private static final int MAX_SINGLE_FILE_SIZE_MB = 2 * MAX_DIR_SIZE_MB / MAX_FILE_EXSIST_DAY;// 单个文件最大空间
    public static boolean vpnTest = false;//从属性来看 方法vpnTestInit 是一段废代码 @hxx

    private static HostApplication application;
    private List<String> downloadingFiles;


    // 在自己的Application中添加如下代码

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }



    @Override
    public void onLowMemory() {
//        LogUtil.w(this, "Low Memory. memoryUsage=" + AppUtil.getMemoryUsage() + "M");
        super.onLowMemory();
    }

    public static HostApplication getApplication() {
        return application;
    }


    private void vpnTestInit(boolean flag){
        vpnTest = flag;
    }

}
