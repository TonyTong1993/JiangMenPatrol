
package com.zzz.ecity.android.applibrary.service;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.xdandroid.hellodaemon.AbsWorkService;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.model.EPositionMessageType;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.model.PositionConfig;
import com.zzz.ecity.android.applibrary.task.GPSPositionReporter;
import com.zzz.ecity.android.applibrary.task.PositionReportTask;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

/***
 * 坐标服务，用于启动GPS定位，GPS轨迹上报
 * 启动该Service之前 必须先配置好PositionConfig类，包括上报路径、坐标构建器
 * @author ZiZhengzhuan
 *
 */
public class PositionService extends AbsWorkService {
    //是否 任务完成, 不再需要服务运行?
    public static boolean shouldStopService;
    private static CompositeDisposable disposables;
    private static boolean pauseReport = false;
    private static Location lastLocation;
    private ScheduledExecutorService backgroundService = null;
    public static boolean isStop = false;
    private static PowerManager pm;
    private static PowerManager.WakeLock wakeLock;
    private LocationHandlerThread locationHandlerThread = null;
    private Handler locationHandler = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /***
     * 最近一个坐标点
     * @return
     */
    public static Location getLastLocation() {
        return lastLocation;
    }

    public static void setLastLocation(Location lastLocation) {
        PositionService.lastLocation = lastLocation;
    }

    /**
     * 是否暂停上报
     *
     * @return
     */
    public static boolean isPauseReport() {
        return pauseReport;
    }

    public static void setPauseReport(boolean pauseReport) {
        PositionService.pauseReport = pauseReport;
    }

    private void report() {
        if (isStop) {
            cancelJobAlarmSub();
            if (backgroundService != null) {
                backgroundService.shutdown();
            }
            backgroundService = null;
            stopSelf();
            return;
        }
        reportPosition();
    }

    private void reportPosition() {

        if (GPSPositionReporter.getInstance().isTaskRuning()) {
            return;
        }

        List<GPSPositionBean> reportBeanList = getNoReportedGPSPosition();
        GPSPositionReporter.getInstance().reportPositions(reportBeanList);
    }

    private List<GPSPositionBean> getNoReportedGPSPosition() {
        if (!StringUtil.isBlank(PositionConfig.getReportUserId())) {
            return GPSPositionDao.getInstance().getPositionBeans(PositionConfig.getReportUserId(), 7, 0, 1, 50, 0);
        }

        return null;
    }

    @Override
    protected int onStart(Intent intent, int flags, int startId) {
        if (!StringUtil.isBlank(PositionConfig.getReportPositionBeanBuilderClassName())) {
            PositionReportTask.getInstance().initReportPositionBeanBuilder(PositionConfig.getReportPositionBeanBuilderClassName());
        } else {
            sendPositionBroadcast(EPositionMessageType.POSITIONBEANBUILDERNOTFOUND, "must setup PositionBeanBuilder first");
        }

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        try {
            wakeLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }

        backgroundService = Executors.newSingleThreadScheduledExecutor();
        if (PositionConfig.REPORT_INTERVAL < 5) {
            PositionConfig.REPORT_INTERVAL = 5;
        }
        backgroundService.scheduleAtFixedRate(new TimerIncreasedRunnable(), 0,
                1000 * PositionConfig.REPORT_INTERVAL, TimeUnit.MILLISECONDS);

        isStop = false;
        startGPS();
        return super.onStart(intent, flags, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return this.onStart(intent, flags, startId);
    }

    public class TimerIncreasedRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (isStop) {
                    return;
                }
                report();
            } catch (Throwable t) {
                Log.e("LocationService", t.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGPS();
        destroyLocation();
        wakeLock.release();

        isStop = true;
        if (backgroundService != null) {
            backgroundService.shutdown();
        }

        backgroundService = null;
    }

    /***
     * 开启GPS
     */
    private void startGPS() {
        boolean locationSimulateSimulated = false;
        if (PositionConfig.isSimulateLocation()) {
            ALoationProducer producer = null;
            if (!StringUtil.isEmpty(PositionConfig.getLoationProducerClassName())) {
                try {
                    producer = (ALoationProducer) Class
                            .forName(PositionConfig.getLoationProducerClassName()).newInstance();
                } catch (Exception e) {
                    producer = null;
                    e.printStackTrace();
                }
            }

            if (null != producer) {
                startSimulateLocation(producer);
                locationSimulateSimulated = true;
            }
        }

        if (!locationSimulateSimulated) {
            //TODO: zzz 2018-08-15 这里本该启动GPS定位，目前已经
        }
    }

    /***
     * 关闭GPS
     */
    private void stopGPS() {
        isStop = true;
        //TODO:zzz 2018-08-15 停止定位 locationClient.stopLocation();
    }

    private void destroyLocation() {
        //TODO:zzz 2018-08-15
    }

    /**
     * 开启服务
     *
     * @param context
     */
    public static void startInstance(Context context) {
        shouldStopService = false;
        //TODO:zzz 2018-08-15 DaemonEnv.startServiceMayBind(PositionService.class);
    }

    /**
     * 关闭服务
     *
     * @param context
     */
    public static void stopInstance(Context context) {
        shouldStopService = true;
        //TODO:zzz 2018-08-15 DaemonEnv.startServiceMayBind(PositionService.class);
    }

    private void startSimulateLocation(final ALoationProducer producer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < producer.getNum(); i++) {
                    if (isStop) {
                        break;
                    }

                    Location location = producer.generate(i);
                    if (null != location) {
                        notifyLocationChanged(location);
                        try {
                            Thread.sleep(producer.getInterval() * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }


    public void notifyLocationChanged(Location location) {

        lastLocation = location;

        if (PositionConfig.isLogLocation()) {
            //TODO:zzz 2018-08-15 logLocation(location);
        }

        sendPositionBroadcast(EPositionMessageType.RECEIVENEWLOCATION, "");

        if (!isPauseReport()) {
            if (!isLocationNeedReport(location)) {
                //不上报精度值大于200米的坐标
                return;
            }

            if (null != locationHandlerThread && null != locationHandler) {
                Message msg = Message.obtain();
                msg.obj = location;
                locationHandler.sendMessage(msg);
            }
        }
    }

    private boolean isLocationNeedReport(Location location) {
        if (location.getAccuracy() > 50) {
            //不上报精度值大于50米的坐标
            return false;
        }
        return true;
    }

    private void sendPositionBroadcast(EPositionMessageType type, String msg) {
        Intent intent = new Intent(PositionConfig.ACTION_POSITION_NAME);
        intent.putExtra(PositionConfig.ACTION_POSITION_MSG_TYPE,
                type.getValue());
        intent.putExtra(PositionConfig.ACTION_POSITION_MSG_CONTENT, msg);
        MyApplication.getApplication().sendBroadcast(intent);
    }

    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        shouldStopService = true;
        //取消对任务的订阅
        if (disposables != null) disposables.dispose();
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     *
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return shouldStopService;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        disposables = new CompositeDisposable();
        locationHandlerThread = new LocationHandlerThread(String.valueOf(startId));
        locationHandlerThread.start();
        locationHandler = new Handler(locationHandlerThread.getLooper(), locationHandlerThread);

        disposables.add(sampleObservable().subscribeWith(new DisposableObserver<Long>() {
            @Override
            public void onComplete() {
                System.out.println("onComplete()");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError()" + e);
            }

            @Override
            public void onNext(Long count) {
                try {
                    //TODO:zzz 2018-08-15 PositionReportTask.getInstance().checkCacheData();
                } catch (Exception e) {
                }
            }
        }));

    }

    static Observable<Long> sampleObservable() {
        Observable observable = Observable.interval(60, TimeUnit.SECONDS).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                cancelJobAlarmSub();
            }
        });
        return observable;
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
        if (null != locationHandlerThread) {
            locationHandlerThread.interrupt();
            locationHandlerThread = null;
        }
    }

    /**
     * 任务是否正在运行?
     *
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return disposables != null && !disposables.isDisposed();
    }

    @Override
    public IBinder onBind(Intent intent, Void v) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        System.out.println("保存数据到磁盘。");
    }


    private class LocationHandlerThread extends HandlerThread implements Handler.Callback {

        public LocationHandlerThread(String name) {
            super(name);
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (null != msg.obj && msg.obj instanceof Location) {
                PositionReportTask.getInstance().addFilterLocation((Location) msg.obj);
            }
            return true;
        }
    }
}
