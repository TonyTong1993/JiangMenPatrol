package com.ecity.android.httpexecutor;

import android.app.Application;
import android.util.Log;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ReservedEvent;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.log.LogUtil;
import com.enn.sop.global.GlobalFunctionInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author maoshoubei
 * @date 2017/10/23
 */
public class RequestExecutor {
    private static final String TAG = "RequestExecutor";
    /**
     * 设置连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 60;
    /**
     * 设置读取超时时间
     */
    public final static int READ_TIMEOUT = 100;
    /**
     * 是指写的超时时间
     */
    public final static int WRITE_TIMEOUT = 60;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private AbstractRequestCallback mCallback;

    private RequestExecutor(AbstractRequestCallback callback) {
        mCallback = callback;
    }

    private static RequestExecutor newInstance(AbstractRequestCallback callback) {
        return new RequestExecutor(callback);
    }

    public static void execute(final AbstractRequestCallback callback) {
        RequestExecutor.newInstance(callback).execute();
    }

    private void execute() {
        Application application = GlobalFunctionInfo.getInstance().getApplication();
        if (!NetWorkUtil.checkConnection(application)) {
            mCallback.onError(new Exception(application.getString(R.string.network_not_available)));
            return;
        }

        Map<String, String> parameters = null;
        try {
            parameters = mCallback.getParameter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (parameters == null) {
            parameters = new HashMap<String, String>(16);
        }

        if (mCallback.isFile()) {
            asyncFile(mCallback.getUrl(), parameters, mCallback.getFilePath());
        } else if (mCallback.isPost()) {
            asyncPost(mCallback.getUrl(), parameters);
        } else {
            asyncGet(mCallback.getUrl(), parameters);
        }
    }


    private void asyncGet(String url, Map<String, String> parameters) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        parameters.put("token", GlobalFunctionInfo.getToken());
        Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            urlBuilder.addQueryParameter(entry.getKey(), parameters.get(entry.getKey()));
        }

        String urlStr = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        Log.d(TAG, "GET-URL=" + urlStr);
        Log.i("mao", "GET-URL=" + urlStr);
        runRequest(request);
    }

    private void asyncPost(String url, Map<String, String> parameters) {
        JSONObject json = null;
        try {
            json = RequestExecutorUtil.map2Json(parameters);
        } catch (JSONException e) {
            mCallback.onError(e);
            return;
        }

        if (GlobalFunctionInfo.getToken() == null) {
            LogUtil.i(TAG, "User token is null. Ignore request.");
            return;
        }

        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("token", GlobalFunctionInfo.getToken())
                .addHeader("plat", "mobile")
                .build();
        Log.d(TAG, "POST-URL=" + url);
        Log.i("mao", "POST-URL=" + url);
        runRequest(request);
    }

    private void asyncFile(String url, Map<String, String> parameters, String path) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        for (String key : parameters.keySet()) {
            builder.addFormDataPart(key, parameters.get(key));
        }
        builder.addFormDataPart("file", path,
                RequestBody.create(JSON, new File(path)));

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("token", GlobalFunctionInfo.getToken())
                .build();

        runRequest(request);
    }

    private void runRequest(Request request) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                if (isClientTimeout(responseStr)) {
                    // 若已经超时，提示用户
                    ResponseEvent event = new ResponseEvent(ReservedEvent.Response.TOKEN_TIMEOUT, ReservedEvent.Response.ERROR, "token验证失败");
                    EventBusUtil.post(event);
                } else {
                    mCallback.onCompletion(responseStr);
                }
            }
        });
    }

    /**
     * 验证 App 是否长时间没有发送请求给后台，导致后台判定 App 端 token 失效。
     * @param response
     * @return
     */
    private boolean isClientTimeout(String response) {
        // {"msg":"token验证失败:null","code":"401","success":false}
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            // 若 token 失效，服务端返回会返回固定的编码 401
            return ("401".equals(jsonObj.optString("code")));
        } catch (JSONException e) {
            return false;
        }
    }
}