package com.ecity.cswatersupply.emergency.service;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadManager;
import com.aspsine.multithreaddownload.DownloadRequest;
import com.aspsine.multithreaddownload.util.L;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.utils.DownloadUtils;
import com.ecity.cswatersupply.event.UIEventStatus;

/**
 * Created by aspsine on 15/7/28.
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();

    public static final String ACTION_DOWNLOAD_BROAD_CAST = "com.aspsine.multithreaddownload.demo:action_download_broad_cast";

    public static final String ACTION_DOWNLOAD = "com.aspsine.multithreaddownload.demo:action_download";

    public static final String ACTION_PAUSE = "com.aspsine.multithreaddownload.demo:action_pause";

    public static final String ACTION_CANCEL = "com.aspsine.multithreaddownload.demo:action_cancel";

    public static final String ACTION_PAUSE_ALL = "com.aspsine.multithreaddownload.demo:action_pause_all";

    public static final String ACTION_CANCEL_ALL = "com.aspsine.multithreaddownload.demo:action_cancel_all";

    public static final String EXTRA_TAG = "extra_tag";

    public static final String EXTRA_DETAIL_INFO = "extra_detail_info";
    private DownloadManager mDownloadManager;

    private NotificationManagerCompat mNotificationManager;

    public static void intentDownload(Context context, String tag, EmergencyPlanModel info) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_DETAIL_INFO, info);
        context.startService(intent);
    }

    public static void intentPause(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_PAUSE);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
    }

    public static void destory(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            EmergencyPlanModel EmergencyPlanModel = (EmergencyPlanModel) intent.getSerializableExtra(EXTRA_DETAIL_INFO);
            String tag = intent.getStringExtra(EXTRA_TAG);
            if (action.equalsIgnoreCase(ACTION_DOWNLOAD)) {
                download(EmergencyPlanModel, tag);
            } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
                pause(tag);
            } else if (action.equalsIgnoreCase(ACTION_CANCEL)) {
                cancel(tag);
            } else if (action.equalsIgnoreCase(ACTION_PAUSE_ALL)) {
                pauseAll();
            } else if (action.equalsIgnoreCase(ACTION_CANCEL_ALL)) {
                cancelAll();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(final EmergencyPlanModel emergency, String tag) {
        File dir = new File(emergency.getSavePath());
        final DownloadRequest request = new DownloadRequest.Builder().setName(emergency.getDoc())
                .setUri(emergency.getUrl()).setFolder(dir).build();
        mDownloadManager.download(request, tag,
                new DownloadCallBack(emergency, mNotificationManager, getApplicationContext()));
    }

    private void pause(String tag) {
        mDownloadManager.pause(tag);
    }

    private void cancel(String tag) {
        mDownloadManager.cancel(tag);
    }

    private void pauseAll() {
        mDownloadManager.pauseAll();
    }

    private void cancelAll() {
        mDownloadManager.cancelAll();
    }

    public static class DownloadCallBack implements CallBack {

        private EmergencyPlanModel dfInfo;

        private LocalBroadcastManager mLocalBroadcastManager;

        private NotificationCompat.Builder mBuilder;

        private NotificationManagerCompat mNotificationManager;

        private long mLastTime;

        private int mPosition = 0;

        public DownloadCallBack(EmergencyPlanModel EmergencyPlanModel,
                                NotificationManagerCompat notificationManager, Context context) {
            dfInfo = EmergencyPlanModel;
            mPosition = dfInfo.getUrl().hashCode();
            mNotificationManager = notificationManager;
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
            mBuilder = new NotificationCompat.Builder(context);
        }

        @Override
        public void onStarted() {
            L.i(TAG, "onStart()");
            mBuilder.setSmallIcon(R.drawable.ic_water_supply_login).setContentTitle(dfInfo.getDoc()).setContentText("Init Download")
                    .setProgress(100, 0, true).setTicker("开始下载 " + dfInfo.getDoc());
            updateNotification();
        }

        @Override
        public void onConnecting() {
            L.i(TAG, "onConnecting()");
            mBuilder.setContentText("正在连接").setProgress(100, 0, true);
            updateNotification();

            dfInfo.setStatus(EmergencyPlanModel.STATUS_CONNECTING);
            sendBroadCast(dfInfo);
        }

        @Override
        public void onConnected(long total, boolean isRangeSupport) {
            L.i(TAG, "onConnected()");
            mBuilder.setContentText("已连接").setProgress(100, 0, true);
            updateNotification();
        }

        @Override
        public void onProgress(long finished, long total, int progress) {

            if (mLastTime == 0) {
                mLastTime = System.currentTimeMillis();
            }

            dfInfo.setStatus(EmergencyPlanModel.STATUS_DOWNLOADING);
            dfInfo.setProgress(progress);
            dfInfo.setDownloadPerSize(DownloadUtils.getDownloadPerSize(finished, total));

            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime > 500) {
                L.i(TAG, "onProgress()");
                mBuilder.setContentText("正在下载");
                mBuilder.setProgress(100, progress, false);
                updateNotification();

                sendBroadCast(dfInfo);

                mLastTime = currentTime;
            }
        }

        @Override
        public void onCompleted() {
            L.i(TAG, "onCompleted()");
            mBuilder.setContentText("下载完成");
            mBuilder.setProgress(0, 0, false);
            mBuilder.setTicker(dfInfo.getDoc() + " 下载完成");
            updateNotification();

            dfInfo.setStatus(EmergencyPlanModel.STATUS_COMPLETE);
            dfInfo.setProgress(100);
//            sendBroadCast(dfInfo);

            UIEvent uiEvent = new UIEvent(UIEventStatus.EMERGENCY_DOWN_DONE, dfInfo);
            EventBusUtil.post(uiEvent);

        }

        @Override
        public void onDownloadPaused() {
            L.i(TAG, "onDownloadPaused()");
            mBuilder.setContentText("暂停下载");
            mBuilder.setTicker(dfInfo.getDoc() + " 暂停下载");
            mBuilder.setProgress(100, dfInfo.getProgress(), false);
            updateNotification();

            dfInfo.setStatus(EmergencyPlanModel.STATUS_PAUSED);
            sendBroadCast(dfInfo);
        }

        @Override
        public void onDownloadCanceled() {
            L.i(TAG, "onDownloadCanceled()");
            mBuilder.setContentText("取消下载");
            mBuilder.setTicker(dfInfo.getDoc() + " 取消下载");
            updateNotification();

            // there is 1000 ms memory leak, shouldn't be a problem
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(mPosition + 1000);
                }
            }, 1000);

            dfInfo.setStatus(EmergencyPlanModel.STATUS_NOT_DOWNLOAD);
            dfInfo.setProgress(0);
            dfInfo.setDownloadPerSize("");
            sendBroadCast(dfInfo);
        }

        @Override
        public void onFailed(DownloadException e) {
            L.i(TAG, "onFailed()");
            e.printStackTrace();
            mBuilder.setContentText("下载失败");
            mBuilder.setTicker("" + dfInfo.getDoc() + " 下载失败");
            mBuilder.setProgress(100, dfInfo.getProgress(), false);
            updateNotification();

            dfInfo.setStatus(EmergencyPlanModel.STATUS_DOWNLOAD_ERROR);
            sendBroadCast(dfInfo);
        }

        private void updateNotification() {
            mNotificationManager.notify(mPosition + 1000, mBuilder.build());
        }

        private void sendBroadCast(EmergencyPlanModel EmergencyPlanModel) {
            Intent intent = new Intent();
            intent.setAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
            intent.putExtra(EXTRA_DETAIL_INFO, EmergencyPlanModel);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = DownloadManager.getInstance();
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadManager.pauseAll();
    }

}
