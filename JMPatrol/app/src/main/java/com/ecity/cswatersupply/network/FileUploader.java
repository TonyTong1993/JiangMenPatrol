package com.ecity.cswatersupply.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.httpforandroid.http.HttpRequest;
import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.utils.ListUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.MyApplication;

public class FileUploader {
    public static final int UPLOAD_ALL_FILES_DONE = 1;
    public static final int UPLOAD_SINGLE_FILE_DONE = 2;
    public static final int UPLOAD_FAIL = 3;
    public static final int UPLOAD_SINGLE_FILE_FAIL = 5;
    public static final int UPLOAD_ALL_FORMS_DONE = 4;
    private List<String> filePaths;
    private String url;
    private Map<String, String> parameters;
    private Handler mHandler;
    private IFileUploaderReponseParser responseParser;

    private FileUploader(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        this.filePaths = filePaths;
        this.url = url;
        this.parameters = parameters;
        this.mHandler = handler;
    }

    private static FileUploader newInstance(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        return new FileUploader(filePaths, url, parameters, handler);
    }

    private static FileUploader newInstance(List<String> filePaths, String url, Map<String, String> parameters, Handler handler, IFileUploaderReponseParser responseParser) {
        FileUploader uploader = new FileUploader(filePaths, url, parameters, handler);
        uploader.responseParser = responseParser;

        return uploader;
    }

    /**
     * Upload files to url.
     * @param filePaths Files to upload.
     * @param url
     * @param parameters Parameters of files. All files shares a same parameter map.
     */
    public static void execute(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        FileUploader.newInstance(filePaths, url, parameters, handler).execute();
    }

    public static void execute(List<String> filePaths, String url, Map<String, String> parameters, Handler handler, IFileUploaderReponseParser responseParser) {
        FileUploader.newInstance(filePaths, url, parameters, handler, responseParser).execute();
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

        MyApplication.getApplication().submitExecutorService(new WorkerRunnable());
    }

    private class WorkerRunnable implements Runnable{
        @Override
        public void run() {
            if (!ListUtil.isEmpty(filePaths)) {
                int uploadfailCount = 0;
                for (String file : filePaths) {
                    Message msg = uploadFile(file);
                    if (null != msg && UPLOAD_SINGLE_FILE_FAIL == msg.what) {
                        uploadfailCount++;
                    }
                    
                    mHandler.sendMessage(msg);
                }

                Message msg = Message.obtain();
                String info = "";
                if (uploadfailCount > 0) {
                    msg.what = UPLOAD_FAIL;
                    info = HostApplication.getApplication().getString(R.string.upload_fail);
                } else {
                    msg.what = UPLOAD_ALL_FILES_DONE;
                    info = HostApplication.getApplication().getString(R.string.upload_success);
                }

                msg.obj = info;
                mHandler.sendMessage(msg);

            } else {
                if (null == parameters) {
                    parameters = new HashMap<String, String>();
                    parameters.put("f", "json");
                }
                
                Message msg = upload(url, parameters);
                mHandler.sendMessage(msg);
            }
        }
    }

    private Message uploadFile(String filePath) {
        return upload(url, parameters, filePath);
    }

    private Message upload(String url, Map<String, String> parameters) {
        List<NameValuePair> nameValuePairArray = new ArrayList<NameValuePair>();
        HttpRequest.mapToList(url, parameters, nameValuePairArray);
        UrlEncodedFormEntity entity = null;
        String completeUrl = url;
        //completeUrl += "?token=" + Constants.REQUEST_TOKEN;
        if (!nameValuePairArray.isEmpty()) {
            //completeUrl += "?" + URLEncodedUtils.format(nameValuePairArray, "UTF-8").toString();
            try {
                entity = new UrlEncodedFormEntity(nameValuePairArray, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Message msg = null;
        HttpPost httpRequest = new HttpPost(completeUrl);
        try {
            if (null != entity) {
                httpRequest.setEntity(entity);
            }
            HttpClient httpClient = getHttpClient();
            LogUtil.d(this, "Sending request. url=" + completeUrl);
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getEntity() != null) {
                String result = (EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8));
                msg = onCompletion(result);
            }
        } catch (Exception e) {
            LogUtil.e(this, e);
            msg = Message.obtain();
            msg.what = UPLOAD_FAIL;
            msg.obj = e.getMessage();
        }
        return msg;
    }

    private Message upload(String url, Map<String, String> parameters, String path) {
        List<NameValuePair> nameValuePairArray = new ArrayList<NameValuePair>();
        HttpRequest.mapToList(url, parameters, nameValuePairArray);

        String completeUrl = url;
        //completeUrl += "?token=" + Constants.REQUEST_TOKEN;
        if (!nameValuePairArray.isEmpty()) {
            completeUrl += "?" + URLEncodedUtils.format(nameValuePairArray, "UTF-8").toString();
        }
        Message msg = null;
        HttpPost httpRequest = new HttpPost(completeUrl);
        try {
            if (!StringUtil.isBlank(path)) {
                //httpRequest.setEntity(new UrlEncodedFormEntity(nameValuePairArray,"utf-8"));
                httpRequest.setEntity(new ByteArrayEntity(getFileBytes(path)));
            }
            HttpClient httpClient = getHttpClient();
            
            LogUtil.d(this, "Sending request. url=" + completeUrl);
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getEntity() != null) {
                String result = (EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8));
                msg = onCompletion(result, path);
            }
        } catch (Exception e) {
            LogUtil.e(this, e);
            msg = Message.obtain();
            msg.what = UPLOAD_SINGLE_FILE_FAIL;
            msg.obj = e.getMessage();
        }

        return msg;
    }

    private Message onCompletion(String responseResult) {
        LogUtil.d(this, "Response:\n" + responseResult);
        Message msg = Message.obtain();
        try {
            JSONObject jsonObj = new JSONObject(responseResult);
            boolean isOK = false;
            if (responseParser != null) {
                isOK = responseParser.isSuccess(responseResult);
            } else {
                if (jsonObj.has("isSuccess")) {
                    isOK = jsonObj.getBoolean("isSuccess");
                }
                if (jsonObj.has("success")) {
                    isOK = jsonObj.getBoolean("success");
                }
            }
            if (isOK) {
                msg.what = UPLOAD_ALL_FORMS_DONE;
                msg.obj = responseResult;
            } else {
                JSONObject errorJson = jsonObj.getJSONObject("error");
                String info = errorJson.getString("message");
                if (StringUtil.isBlank(info)) {
                    info = HostApplication.getApplication().getString(R.string.upload_fail);
                }
                msg.what = UPLOAD_FAIL;
                msg.obj = info;
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
            String info = HostApplication.getApplication().getString(R.string.server_response_invalid);
            msg.what = UPLOAD_FAIL;
            msg.obj = info;
        }

        return msg;
    }

    private Message onCompletion(String responseResult, String filePath) {
        LogUtil.d(this, "Response:\n" + responseResult);
        Message msg = Message.obtain();
        try {
            JSONObject jsonObj = new JSONObject(responseResult);
            boolean isOK = false;
            if (responseParser != null) {
                isOK = responseParser.isSuccess(responseResult);
            } else {
                if (jsonObj.has("isSuccess")) {
                    isOK = jsonObj.getBoolean("isSuccess");
                }
                if (jsonObj.has("success")) {
                    isOK = jsonObj.getBoolean("success");
                }
            }
            if (isOK) {
                msg.what = UPLOAD_SINGLE_FILE_DONE;
                //                String info = HostApplication.getApplication().getString(R.string.upload_success);
                if (HostApplication.getApplication().getProjectStyle()==HostApplication.ProjectStyle.PROJECT_JMGC){
                    msg.obj = jsonObj;
                }else{
                    msg.obj = filePath;
                }
            } else {
                JSONObject errorJson = jsonObj.optJSONObject("error");
                String info = errorJson.optString("message");
                if (StringUtil.isBlank(info)) {
                    info = HostApplication.getApplication().getString(R.string.upload_fail);
                }
                msg.what = UPLOAD_SINGLE_FILE_FAIL;
                msg.obj = info;
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
            String info = HostApplication.getApplication().getString(R.string.server_response_invalid);
            msg.what = UPLOAD_SINGLE_FILE_FAIL;
            msg.obj = info;
        }

        return msg;
    }

    private HttpClient getHttpClient() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "UTF-8");
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 30 * 1000);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpClientParams.setRedirecting(basicHttpParams, true);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
        HttpClient httpClient = new DefaultHttpClient(threadSafeClientConnManager, basicHttpParams);
//        ((DefaultHttpClient) httpClient).setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 15 * 1000);
        HttpConnectionParams.setSoTimeout(params, 30 * 1000);

        return httpClient;
    }

    private byte[] getFileBytes(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = null;
        byte[] buffer = null;
        try {
            fileInputStream = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

        return buffer;
    }
}