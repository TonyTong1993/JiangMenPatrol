package com.ecity.cswatersupply.xg;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.tencent.android.tpush.XGIOperateCallback;

public class MyXGRegisterCallback implements XGIOperateCallback {
    private static MyXGRegisterCallback instance;

    private MyXGRegisterCallback() {
    }

    static {
        instance = new MyXGRegisterCallback();
    }

    public static MyXGRegisterCallback getInstance() {
        return instance;
    }

    @Override
    public void onFail(Object data, int errCode, String msg) {
        LogUtil.e(this, "Register failed. ErrorCode:" + errCode + ", ErrorMsg: " + msg);
        String errorMsg = HostApplication.getApplication().getString(R.string.notification_register_fail);
        EventBusUtil.post(new ResponseEvent(ResponseEventStatus.NOTIFICATION_REGISTER_FAIL, ResponseEventStatus.ERROR, errorMsg));
    }

    @Override
    public void onSuccess(final Object data, int flag) {
        EventBusUtil.post(new ResponseEvent(ResponseEventStatus.NOTIFICATION_REGISTER_SUCCESS, ResponseEventStatus.OK, null));
    }
}
