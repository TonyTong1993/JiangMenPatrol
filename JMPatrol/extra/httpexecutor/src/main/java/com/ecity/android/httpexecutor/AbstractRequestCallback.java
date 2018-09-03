package com.ecity.android.httpexecutor;

import android.util.Log;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ReservedEvent;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * @author maoshoubei
 * @date 2017/10/23
 */
public abstract class AbstractRequestCallback {
    private static final String TAG = "AbstractRequestCallback";

    /**
     * 执行的是否是附件上传
     *
     * @return
     */
    public abstract boolean isFile();

    /**
     * 获取到附件路径
     *
     * @return
     */
    public abstract String getFilePath();

    /**
     * 执行的是post请求还是get请求
     *
     * @return
     */
    public abstract boolean isPost();

    /**
     * 获取到请求的Url
     *
     * @return
     */
    public abstract String getUrl();

    /**
     * 获取请求参数
     *
     * @return map
     * @throws Exception
     */
    public abstract Map<String, String> getParameter() throws Exception;


    /**
     * Get the event id for this request. This id will be used by EventBusUtil
     * when posting an event.
     *
     * @return
     */
    public abstract int getEventId();

    /**
     * Get the message to display when an error happens in local, or server
     * responds with error, but no message is returned.
     *
     * @return
     */
    public String getErrorMessage() {
        // Override in concrete class.
        return "请求失败";
    }

    public Object parseResponse(JSONObject jsonObj) {
        return (jsonObj == null) ? new JSONObject() : jsonObj;
    }

    public void onError(Throwable e) {
        LogUtil.e(TAG, e);

        String msg = e.getMessage();
        if (e instanceof SocketTimeoutException) {
            msg = "请求超时";
        } else {
            msg = isBlankString(msg) ? getErrorMessage() : msg;
        }

        ResponseEvent event = new ResponseEvent(getEventId(), ReservedEvent.Response.ERROR, msg);
        EventBusUtil.post(event);
    }

    public void onCompletion(String response) {
        LogUtil.v(TAG, "Response:\n" + response);
        Log.i("mao",response);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
        } catch (JSONException e) {
            ResponseEvent event = new ResponseEvent(getEventId(), ReservedEvent.Response.ERROR, "服务器返回结果异常");
            EventBusUtil.post(event);
            return;
        }

        ResponseEvent event = null;
        if (jsonObj == null) {
            event = new ResponseEvent(getEventId(), ReservedEvent.Response.ERROR, "访问服务器出现错误。可能是因为当前网络不可用，或网络限制导致无法访问服务器。");
            EventBusUtil.post(event);
            return;
        }

        try {
            String errorStr = "error";
            String isSuccessStr = "isSuccess";
            String successStr = "success";
            String messageStr = "message";
            String msgStr = "msg";

            boolean isOK = true;

            if (jsonObj.has(errorStr)) {
                isOK = false;
            }

            if (jsonObj.has(isSuccessStr)) {
                isOK = jsonObj.getBoolean(isSuccessStr);
            }

            if (jsonObj.has(successStr)) {
                isOK = jsonObj.getBoolean(successStr);
            }

            if (isOK) {
                LogUtil.d(TAG, "request success");
                Object data = parseResponse(jsonObj);
                event = new ResponseEvent(getEventId(), ReservedEvent.Response.OK, data);
            } else {
                String msg = "";
                if (jsonObj.has(errorStr)) {
                    JSONObject errorJson = jsonObj.optJSONObject(errorStr);
                    msg = errorJson.optString(messageStr);
                } else if (jsonObj.has(msgStr)) {
                    msg = jsonObj.optString(msgStr);
                } else if (jsonObj.has(messageStr)) {
                    msg = jsonObj.optString(messageStr);
                }

                if (isBlankString(msg)) {
                    msg = getErrorMessage();
                }
                LogUtil.e(TAG, "request error. " + msg);
                event = new ResponseEvent(getEventId(), ReservedEvent.Response.ERROR, msg);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            event = new ResponseEvent(getEventId(), ReservedEvent.Response.ERROR, getErrorMessage());
        }

        EventBusUtil.post(event);
    }

    private boolean isBlankString(String str) {
        return (str == null) || (str.trim().length() == 0);
    }
}