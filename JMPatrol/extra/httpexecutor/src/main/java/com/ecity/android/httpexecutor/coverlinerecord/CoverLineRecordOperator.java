package com.ecity.android.httpexecutor.coverlinerecord;

import android.content.Context;
import android.os.Handler;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpexecutor.util.DataBaseUtil;
import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.log.LogUtil;
import com.enn.sop.global.GlobalFunctionInfo;
import com.enn.sop.model.user.User;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jonathanma on 20/4/2017.
 */

public class CoverLineRecordOperator {
    private static final String TAG = "CoverLineRecordOperator";
    private static final long REST_INTERVAL = 1 * 1000;
    private static final long WORK_INTERVAL = 1 * 100;
    private static CoverLineRecordOperator instance;
    private ThreadPoolExecutor threadPoolExecutor;
    private User user;
    private CoverLineRecord currentUploadingRecord;
    private Context context;
    private Handler uploadHandler = new Handler();
    private UploadRunnable uploadRunnable;
    private boolean isStopped;

    static {
        instance = new CoverLineRecordOperator();
        instance.threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public static CoverLineRecordOperator getInstance(Context context) {
        DataBaseUtil.init(context);
        instance.context = context;
        return instance;
    }

    private CoverLineRecordOperator() {
    }


    public void processRecord(final CoverLineRecord record) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LogUtil.d(TAG, "Received one record. recordId = " + record.getId());
                CoverLineRecordPersistenceManager.getInstance().save(Arrays.asList(record));
                LogUtil.d(TAG, "Saved one record.");
            }
        });
    }

    public void startUploadProcess(final User user) {
        if (this.user != null) {
            LogUtil.d(TAG, "Already started. No need to start again.");
            return;
        }

        if (GlobalFunctionInfo.getToken() == null) {
            LogUtil.i(TAG, "User token is invalid. Wait for next time.");
            return;
        }

        LogUtil.d(TAG, "Started.");
        EventBusUtil.register(this);
        this.user = user;

        isStopped = false;
        uploadRunnable = new UploadRunnable();
        uploadHandler.post(uploadRunnable);
    }

    public void stopUploadProcess() {
        LogUtil.d(TAG, "Received request to stop.");
        EventBusUtil.unregister(this);
        isStopped = true;
        this.user = null;
        uploadHandler.removeCallbacks(uploadRunnable);
    }

    public void onEventBackgroundThread(ResponseEvent event) {
        if (currentUploadingRecord == null) {
            return;
        }

        if (event.getId() == currentUploadingRecord.getRequestId()) {
            if (event.isOK()) {
                CoverLineRecordPersistenceManager.getInstance().delete(currentUploadingRecord.getId());
                LogUtil.d(TAG, "Send record succeeded, record is deleted. recordId = " + currentUploadingRecord.getId());
            } else {
                CoverLineRecordPersistenceManager.getInstance().increaseFailCount(currentUploadingRecord.getId());
                LogUtil.d(TAG, "Send record failed. recordId = " + currentUploadingRecord.getId());
            }
            currentUploadingRecord = null;
        }
    }

    private class UploadRunnable implements Runnable {
        @Override
        public void run() {
            if (isStopped) {
                LogUtil.d(TAG, "Stopped.");
                return;
            }

            if (user == null) {
                LogUtil.d(TAG, "User not login. Wait for next time.");
                waitForNext(true);
                return;
            }

            if (GlobalFunctionInfo.getToken() == null) {
                LogUtil.i(TAG, "User token is invalid. Wait for next time.");
                return;
            }

            if (!NetWorkUtil.isNetworkAvailable(context)) {
                LogUtil.d(TAG, "Network not available. Wait for next time.");
                waitForNext(true);
                return;
            }

            if (currentUploadingRecord != null) {
                LogUtil.d(TAG, "Busy. Wait for next time.");
                waitForNext(true);
                return;
            }

            currentUploadingRecord = CoverLineRecordPersistenceManager.getInstance().readOne(Integer.valueOf(user.getId()));
            if (currentUploadingRecord != null) {
                if (isStopped) {
                    return;
                }
                LogUtil.i(TAG, "Sending one record. recordId = " + currentUploadingRecord.getId() + ", userId=" + user.getUsername());
                CoverLineRecordUploader.getInstance().upload(currentUploadingRecord);
                waitForNext(false);
            } else {
                LogUtil.d(TAG, "No record found. Wait for next time.");
                waitForNext(true);
            }
        }
    }

    private void waitForNext(boolean delay) {
        long interval = delay ? REST_INTERVAL : WORK_INTERVAL;
        uploadHandler.postDelayed(uploadRunnable, interval);
    }
}




