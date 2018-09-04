package com.ecity.cswatersupply.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.utils.SettingsManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/****
 * 应用程序状态监听服务
 *
 * @author ZiZhengzhuan
 */
public class AppStatusService extends Service {
    private ScheduledExecutorService backgroundService = null;
    private static boolean isStop = true;
    private final int INTERVAL = 10;// 设备状态监听 秒
    private static volatile boolean networkAutoSwitch = true;
    public final static int SWITCHLIMIT = 3;
    private volatile static int handleHttpRequestExceptionTimes = 0;

    private static long lastUpdateTime = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkAppStatus() {
        if (isStop) {
            if (backgroundService != null)
                backgroundService.shutdown();
            backgroundService = null;
            stopSelf();
            return;
        }
        if (!isNetworkEnable()) {
            return;
        }
        //
        if (handleHttpRequestExceptionTimes >= SWITCHLIMIT) {
            if (networkAutoSwitch) {
                changeServer();
            }
        }
    }

    /***
     *  除了登录时候，外部不宜使用这个方法
     */
    public static void changeServer(){
        if(System.currentTimeMillis() - lastUpdateTime < 1000*5 ) {
            return;
        }
        int serverType = SettingsManager.getInstance().getServerType();
        if (Constants.TARGET_SERVER_NORMAL == serverType) {
            int lastEnvironmentIndex = SettingsManager.getInstance().getLastEnvironmentIndex();
            if(0 == lastEnvironmentIndex){
                changeServerHost(1);
            } else if (1 == lastEnvironmentIndex){
                changeServerHost(0);
            } else {
                //// TODO: 2017/3/20 there is no logic
            }
            LogUtil.e("request error.", "change http request host");
            handleHttpRequestExceptionTimes = 0;
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    private static void changeServerHost(int position){
        String  ipAddressA = getIpAddress(position);
        String portA = getPort(position);
        String virtualPathA = SettingsManager.TARGET_VIRTUAL_PATH;
        SettingsManager.getInstance().setServerIPA(ipAddressA);
        SettingsManager.getInstance().setServerPortA(portA);
        SettingsManager.getInstance().setServerVirtualPathA(virtualPathA);
        SettingsManager.getInstance().setLastEnvironmentIndex(position);
    }

    /**
     * 获得IP地址
     * @param position
     * @return
     */
    private static String getIpAddress(int position) {
        String ip = "";
        switch (position) {
            case 0:
                ip = SettingsManager.TARGET_SERVER_PRO;
                break;
            case 1:
                ip = SettingsManager.TARGET_SERVER_UAT;
                break;
            case 2:
                ip = SettingsManager.TARGET_SERVER_DEV;
                break;
            default:
                break;
        }

        return ip;
    }

    /**
     * 获得端口号
     * @param position
     * @return
     */
    private static String getPort(int position) {
        String port = "";
        switch (position) {
            case 0:
                port = SettingsManager.TARGET_PRO_PORT;
                break;
            case 1:
                port = SettingsManager.TARGET_UAT_PORT;
                break;
            case 2:
                port = SettingsManager.TARGET_DEV_PORT;
                break;
            default:
                break;
        }

        return port;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isStop == true) {
            backgroundService = Executors.newSingleThreadScheduledExecutor();
            backgroundService.scheduleAtFixedRate(new TimerIncreasedRunnable(),
                    0, 1000 * INTERVAL, TimeUnit.MILLISECONDS);
            isStop = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static void handleHttpRequestException() {
        handleHttpRequestExceptionTimes++;
    }
    /**
     * 开启服务
     * @param context
     */
    public static void startInstance(Context context) {
        Intent intent = new Intent(context, AppStatusService.class);
        context.startService(intent);
    }

    /**
     * 关闭服务
     * @param context
     */
    public static void stopInstance(Context context) {
        Intent intent = new Intent(context, AppStatusService.class);
        context.stopService(intent);
    }

    public static boolean isNetworkAutoSwitch() {
        return networkAutoSwitch;
    }

    public static void setNetworkAutoSwitch(boolean networkAutoSwitch) {
        AppStatusService.networkAutoSwitch = networkAutoSwitch;
    }

    private boolean isNetworkEnable() {
        return NetWorkUtil.isNetworkAvailable(HostApplication.getApplication());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        isStop = true;
        if (backgroundService != null) {
            backgroundService.shutdown();
        }
        backgroundService = null;
        super.onDestroy();
    }

    private class TimerIncreasedRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (isStop) {
                    return;
                }
                checkAppStatus();
            } catch (Throwable t) {
                System.out.println("Error");
            }
        }
    }
}