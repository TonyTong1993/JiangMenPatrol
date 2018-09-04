package com.ecity.cswatersupply.network;

import java.io.InputStream;
import java.net.HttpURLConnection;

import android.content.Context;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.utils.HttpUtils;
import com.ecity.cswatersupply.utils.HttpUtils.FileDownloadCallback;
import com.zzz.ecity.android.applibrary.MyApplication;

public class FileDownloader {
    private String url;
    private String targetPath;
    private Class<?> targetClass;

    private FileDownloader(String url, String targetPath, Class<?> targetClass) {
        this.url = url;
        this.targetPath = targetPath;
        this.targetClass = targetClass;
    }

    private static FileDownloader newInstance(String url, String targetPath, Class<?> targetClass) {
        return new FileDownloader(url, targetPath, targetClass);
    }

    /**
     * Client needs to register itself using {@link EventBusUtil} and implement
     * onMainThreadEvent(FileDownloadEvent) method so that it can receive
     * download event.
     * 
     * @param url
     *            where to get the file
     * @param targetPath
     *            where to save the file, targetPath includes file name
     */
    public static void execute(String url, String targetPath, Class<?> targetClass) {
        FileDownloader.newInstance(url, targetPath, targetClass).execute();
    }

    private void execute() {
        MyApplication.getApplication().submitExecutorService(downloadRunnable);
    }

    private Runnable downloadRunnable = new Runnable() {
        @Override
        public void run() {
            download();
        }
    };

    private void download() {
        HttpURLConnection connection = HttpUtils.getConnection(url);
        InputStream inStream = HttpUtils.getInputStream(connection);
        int resourceSize = (connection == null) ? 0 : connection.getContentLength();
        if (inStream == null) {
            notifyResourceNotAvailable();
        } else {
            HttpUtils.saveInputStream(resourceSize, inStream, targetPath, downloadCallback);
        }
    }

    private void notifyResourceNotAvailable() {
        Context context = HostApplication.getApplication();
        ResponseEvent event = new ResponseEvent(ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH, ResponseEventStatus.ERROR, context.getString(R.string.documents_download_fail1));
        event.setData(url);
        event.setTargetClass(targetClass);
        EventBusUtil.post(event);
    }

    private FileDownloadCallback downloadCallback = new FileDownloadCallback() {

        @Override
        public void onSuccess() {
            Context context = HostApplication.getApplication();
            ResponseEvent event = new ResponseEvent(ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH, ResponseEventStatus.OK,
                    context.getString(R.string.documents_download_success));
            event.setData(targetPath);
            event.setTargetClass(targetClass);
            EventBusUtil.post(event);
        }

        @Override
        public void onProgressUpdate(double progress) {
            ResponseEvent event = new ResponseEvent(ResponseEventStatus.FILE_OPERATION_UPDATE_PROGRESS, ResponseEventStatus.OK, String.valueOf(progress));
            event.setData(url);
            event.setTargetClass(targetClass);
            EventBusUtil.post(event);
        }

        @Override
        public void onError(Throwable throwable) {
            Context context = HostApplication.getApplication();
            ResponseEvent event = new ResponseEvent(ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH, ResponseEventStatus.ERROR,
                    context.getString(R.string.documents_download_fail2));
            event.setData(url);
            event.setTargetClass(targetClass);
            EventBusUtil.post(event);
        }
    };
}
