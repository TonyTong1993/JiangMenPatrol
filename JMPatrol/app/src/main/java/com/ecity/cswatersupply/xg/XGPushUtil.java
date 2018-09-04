package com.ecity.cswatersupply.xg;

import android.content.Context;
import android.content.Intent;

import com.ecity.cswatersupply.utils.DeviceUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

public class XGPushUtil {
    private static final String MIN_VERSION = "2.36";

    public static void registerPush(Context context) {
        //         XGPushConfig.enableDebug(context, true); // call this method before registering, for release, remove this
        XGPushManager.registerPush(context.getApplicationContext());

        if (MIN_VERSION.compareToIgnoreCase(DeviceUtil.getAndroidVersion()) > 0) {
            // 2.36（不包括）之前的版本需要调用以下2行代码
            Intent service = new Intent(context, XGPushService.class);
            context.startService(service);
        }
    }

    public static void registerPush(Context context, XGIOperateCallback registerCallback) {
        XGPushManager.registerPush(context.getApplicationContext(), registerCallback);

        if (MIN_VERSION.compareToIgnoreCase(DeviceUtil.getAndroidVersion()) > 0) {
            // 2.36（不包括）之前的版本需要调用以下2行代码
            Intent service = new Intent(context, XGPushService.class);
            context.startService(service);
        }
    }

    public static void registerPush(Context context, String account) {
        XGPushManager.registerPush(context.getApplicationContext(), account);

        if (MIN_VERSION.compareToIgnoreCase(DeviceUtil.getAndroidVersion()) > 0) {
            // 2.36（不包括）之前的版本需要调用以下2行代码
            Intent service = new Intent(context, XGPushService.class);
            context.startService(service);
        }
    }

    public static void registerPush(Context context, String account, XGIOperateCallback registerCallback) {
        XGPushManager.registerPush(context.getApplicationContext(), account, registerCallback);

        if (MIN_VERSION.compareToIgnoreCase(DeviceUtil.getAndroidVersion()) > 0) {
            // 2.36（不包括）之前的版本需要调用以下2行代码
            Intent service = new Intent(context, XGPushService.class);
            context.startService(service);
        }
    }

    public static void unregisterPush(Context context) {
        XGPushManager.unregisterPush(context.getApplicationContext());
    }

    public static void unregisterPush(Context context, XGIOperateCallback registerCallback) {
        XGPushManager.unregisterPush(context.getApplicationContext(), registerCallback);
    }
}
