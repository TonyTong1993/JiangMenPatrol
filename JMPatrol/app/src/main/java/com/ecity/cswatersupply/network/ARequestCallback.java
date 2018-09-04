package com.ecity.cswatersupply.network;

import java.net.SocketTimeoutException;

import org.json.JSONObject;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.utils.GsonUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;

public abstract class ARequestCallback<T> implements IRequestCallback {
    private static final String TAG = "ARequestCallback"; // Used for logging,
                                                          // if use 'this', the
                                                          // log file will use
                                                          // default tag, not
                                                          // current class name.

    public boolean isForComplexResponse() {
        return true;
    }

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
        return HostApplication.getApplication().getString(R.string.network_default_error_msg);
    }

    /**
     * @return The file to
     */
    public Object getFile() {
        return null;
    }

    /**
     * Get the class of server response model.
     * 
     * @return
     */
    public abstract Class<T> getResponseClass();

    // 默认用Gson解析json，当然也可以自定义解析，如果重写此方法，customParse必须要实现
    protected boolean getParseByGson() {
        return true;
    }

    // 设置自定义解析json方式（工单解析比较复杂，Gson不方便，要单独处理）
    protected Object customParse(JSONObject jsonObj) {
        return new Object();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e);

        String msg = e.getMessage();
        if (e instanceof SocketTimeoutException) {
            msg = HostApplication.getApplication().getString(R.string.network_request_timeout);
        } else {
            msg = StringUtil.isBlank(msg) ? getErrorMessage() : msg;
        }

        ResponseEvent event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, msg);
        EventBusUtil.post(event);
    }

    @Override
    public void onCompletion(short flag, JSONObject jsonObj) {
        //        LogUtil.v(this, "Response:\n" + jsonObj);

        if (flag == -1) { // Has been handled in onError. No need to handle in
                          // this method.
            return;
        }

        ResponseEvent event = null;
        if (jsonObj == null) {
            event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.network_access_error));
            EventBusUtil.post(event);
            return;
        }

        try {
            boolean isOK = true;

            if (jsonObj.has("error")) {
                isOK = false;
            }

            if (jsonObj.has("isSuccess")) {
                isOK = jsonObj.getBoolean("isSuccess");
            }

            if (jsonObj.has("success")) {
                isOK = jsonObj.getBoolean("success");
            }

            if (isOK) {
                LogUtil.d(TAG, "request success");
                Object data = new Object();
                if (getParseByGson()) {
                    data = GsonUtil.toObject(jsonObj.toString(), getResponseClass());
                } else {
                    data = customParse(jsonObj);
                }
                event = new ResponseEvent(getEventId(), ResponseEventStatus.OK, data);
            } else {
                String msg = "";
                if (jsonObj.has("error")) {
                    JSONObject errorJson = jsonObj.optJSONObject("error");
                    msg = errorJson.getString("message");
                } else if (jsonObj.has("msg")) {
                    msg = jsonObj.optString("msg");
                } else if (jsonObj.has("message")) {
                    msg = jsonObj.optString("message");
                }

                if (StringUtil.isBlank(msg)) {
                    msg = getErrorMessage();
                }
                LogUtil.e(TAG, "request error. " + msg);
                event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, msg);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, getErrorMessage());
        }
        EventBusUtil.post(event);
    }

    @Override
    public void onCompletion(short flag, String response) {
    }
}
