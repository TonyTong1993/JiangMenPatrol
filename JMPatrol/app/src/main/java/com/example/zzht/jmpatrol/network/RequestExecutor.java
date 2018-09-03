package com.example.zzht.jmpatrol.network;

import android.app.Application;

import com.ecity.android.httpforandroid.http.HttpBaseServiceListener;
import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.android.log.LogUtil;
import com.example.zzht.jmpatrol.R;
import com.example.zzht.jmpatrol.global.HostApplication;
import com.example.zzht.jmpatrol.network.request.JsonBaseRequest;
import com.example.zzht.jmpatrol.network.request.StringBaseRequest;
import com.example.zzht.jmpatrol.services.AppStatusService;
import com.example.zzht.jmpatrol.utils.StringUtil;
import com.zzz.ecity.android.applibrary.MyApplication;

import org.json.JSONObject;

public class RequestExecutor {
    private IRequestCallback mCallback;

    private RequestExecutor(IRequestCallback callback) {
        mCallback = callback;
    }

    private static RequestExecutor newInstance(IRequestCallback callback) {
        return new RequestExecutor(callback);
    }

    public static void execute(final IRequestCallback callback) {
        RequestExecutor.newInstance(callback).execute();
    }

    private void execute() {
        Application application = HostApplication.getApplication();
        if (!NetWorkUtil.checkConnection(application)) {
            mCallback.onError(new Exception(application.getString(R.string.network_not_available)));
            return;
        }
        RequestParameter.IRequestParameter paramSource = mCallback.prepareParameters();
        String url = mCallback.getUrl();
        RequestParameter param = new RequestParameter(url, paramSource);
        LogUtil.d(this, "Sending request. url=" + url);
        if (mCallback.isForComplexResponse()) {
            JsonBaseRequest request = new JsonBaseRequest(param, objectListener, mCallback.getRequestType());
            MyApplication.getApplication().submitExecutorService(request);
        }
        else{
            StringBaseRequest request = new StringBaseRequest(param, stringListener, mCallback.getRequestType());
            MyApplication.getApplication().submitExecutorService(request); 
        }
    }

    private HttpBaseServiceListener<JSONObject> objectListener = new HttpBaseServiceListener<JSONObject>() {
        @Override
        public void onCompletion(short completFlg, JSONObject localObject1) {
            if (mCallback != null) {
                mCallback.onCompletion(completFlg, localObject1);
            }
        }

        @Override
        public void onError(Throwable paramThrowable) {
            LogUtil.e(this, "request error.", paramThrowable);
            if (mCallback != null) {
                mCallback.onError(paramThrowable);
            }
            verifyExceptionMessage(paramThrowable);
        }

        private void verifyExceptionMessage(Throwable paramThrowable){
            String exceptionMessage = paramThrowable.getMessage();
            if(StringUtil.isBlank(exceptionMessage)) {
                return;
            }
            exceptionMessage = exceptionMessage.toLowerCase();
            if(exceptionMessage.contains("failed to connect to")
                    || exceptionMessage.contains("refused")
                    || exceptionMessage.contains("timed out")) {
                AppStatusService.handleHttpRequestException();
            }
        }
    };

    private HttpBaseServiceListener<String> stringListener = new HttpBaseServiceListener<String>() {
        @Override
        public void onError(Throwable paramThrowable) {
            LogUtil.e(this, "request error.", paramThrowable);
            if (mCallback != null) {
                mCallback.onError(paramThrowable);
            }

            verifyExceptionMessage(paramThrowable);
        }

        @Override
        public void onCompletion(short completFlg, String localObject1) {
            if (mCallback != null) {
                mCallback.onCompletion(completFlg, localObject1);
            }
        }

        private void verifyExceptionMessage(Throwable paramThrowable){
            String exceptionMessage = paramThrowable.getMessage();
            if(StringUtil.isBlank(exceptionMessage)) {
                return;
            }
            exceptionMessage = exceptionMessage.toLowerCase();
            if(exceptionMessage.contains("failed to connect to")
                    || exceptionMessage.contains("refused")
                    || exceptionMessage.contains("timed out")) {
                AppStatusService.handleHttpRequestException();
            }
        }
    };
}