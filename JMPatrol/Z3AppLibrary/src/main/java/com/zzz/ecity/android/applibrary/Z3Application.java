package com.zzz.ecity.android.applibrary;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import com.z3app.android.app.AppManager;
import com.zzz.ecity.android.applibrary.utils.FileUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Z3Application extends Application {
    private static Z3Application b;
    private AppManager c;
    private ExecutorService d;
    public static int e;
    private Intent intent;
    BroadcastReceiver a = new BatteryBroadCast();

    public Z3Application() {
    }

    @Override
    public void onCreate() {
        b = this;
        FileUtil.getInstance(this);
        intent = this.registerReceiver(this.a, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.d = Executors.newCachedThreadPool();
        super.onCreate();
    }

    public static Z3Application getApplication() {
        return b;
    }

    public AppManager getAppManager() {
        if(this.c == null) {
            this.c = AppManager.getAppManager();
        }
        return this.c;
    }

    public void exitApp(Boolean var1) {
        this.c.AppExit(this, var1);
    }

    public String getBatteryLevelPercent() {
        return e + "%";
    }

    public int getBatteryLevel() {
        return e;
    }

    public Future submitExecutorService(Callable var1) {
        return this.d.submit(var1);
    }

    public Future submitExecutorService(Runnable var1) {
        return this.d.submit(var1);
    }

   class BatteryBroadCast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
                Z3Application.e = currLevel * 100 / total;
            }
        }
    }
}