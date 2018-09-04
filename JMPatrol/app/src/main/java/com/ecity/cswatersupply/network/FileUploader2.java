package com.ecity.cswatersupply.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.httpforandroid.httpext.AsyncHttpClient;
import com.ecity.android.httpforandroid.httpext.AsyncHttpResponseHandler;
import com.ecity.android.httpforandroid.httpext.RequestParams;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

/**
 * 只用于NetServer附件口
 * 
 * @author gaokai
 *
 */
public class FileUploader2 {
    public static final int UPLOAD_ALL_FILES_DONE = 1;
    public static final int UPLOAD_SINGLE_FILE_DONE = 2;
    public static final int UPLOAD_FAIL = 3;
    public static final int UPLOAD_SINGLE_FILE_FAIL = 5;
    public static final int UPLOAD_ALL_FORMS_DONE = 4;

    private List<String> filePaths;
    private String url;
    private List<String> failPaths;// 上传失败的地址
    private RequestParams requestParams;
    private int currentFileIndex;

    private FileUploader2(List<String> filePaths, String url, RequestParams requestParams) {
        this.filePaths = filePaths;
        this.url = url;
        this.requestParams = requestParams;
        failPaths = new ArrayList<String>();
    }

    private static FileUploader2 newInstance(List<String> filePaths, String url, RequestParams parameters) {
        return new FileUploader2(filePaths, url, parameters);
    }

    public static void execute(List<String> filePaths, String url, RequestParams parameters) {
        FileUploader2.newInstance(filePaths, url, parameters).execute();
    }

    private void execute() {
        Application application = HostApplication.getApplication();
        if (!NetWorkUtil.checkConnection(application)) {
            UIEvent event = new UIEvent(UIEventStatus.TOAST);
            event.setMessage(application.getString(R.string.network_not_available));
            event.setTargetClass(MainActivity.class);
            EventBusUtil.post(event);
            return;
        }
        MyApplication.getApplication().submitExecutorService(workerRunnable);
    }

    private Runnable workerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!ListUtil.isEmpty(filePaths)) {
                uploadNextFile();
            } else {
                notifyUploadAllDone();
            }
        }
    };

    private void upload(String url, RequestParams parameters, String path) {
        File f = new File(path);
        if (!f.exists()) {
            return;
        }
        try {
            parameters.put("attachment", new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, parameters, new Callback(path));
    }

    public class Callback extends AsyncHttpResponseHandler {

        private String path;

        public Callback(String path) {
            this.path = path;
        }

        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            JSONObject jsonObject = null;
            ResponseEvent event = new ResponseEvent(ResponseEventStatus.UPLOAD_SINGLE_FILE);
            try {
                jsonObject = new JSONObject(content);
            } catch (JSONException e) {
                LogUtil.e(this, e.getMessage());
            }
            if (jsonObject != null) {
                jsonObject = jsonObject.optJSONObject("addAttachmentResult");
                String guid = jsonObject.optString("objectId", "");
                event.setStatus(ResponseEventStatus.OK);
                event.setMessage(guid);
                event.setData(path);
            } else {
                event.setStatus(ResponseEventStatus.ERROR);
            }
            EventBusUtil.post(event);
            uploadNextFile();
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            failPaths.add(path);
            ResponseEvent event = new ResponseEvent(ResponseEventStatus.UPLOAD_SINGLE_FILE, ResponseEventStatus.ERROR);
            EventBusUtil.post(event);
            uploadNextFile();
        }
    }

    private void uploadNextFile() {
        if (currentFileIndex < filePaths.size()) {
            String filePath = filePaths.get(currentFileIndex);
            upload(url, requestParams, filePath);
            currentFileIndex++;
        } else {
            notifyUploadAllDone();
        }
    }

    private void notifyUploadAllDone() {
        ResponseEvent responseEvent = new ResponseEvent(ResponseEventStatus.UPLOAD_ALL_FILES);
        if (ListUtil.isEmpty(failPaths)) {// 全部上传成功
            responseEvent.setStatus(ResponseEventStatus.OK);
            EventBusUtil.post(responseEvent);
        } else {
            responseEvent.setStatus(ResponseEventStatus.ERROR);
            responseEvent.setData(failPaths);
            EventBusUtil.post(responseEvent);
        }
    }
}
