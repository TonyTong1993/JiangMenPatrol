package com.ecity.cswatersupply.xg;

import android.content.Context;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.GsonUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationEntity;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.ecity.cswatersupply.xg.util.NotificationAdapter;
import com.ecity.cswatersupply.xg.util.NotificationFunctionMappingFactory;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.z3app.android.util.StringUtil;

public class MyXGPushReceiver extends XGPushBaseReceiver {

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult clickedResult) {
    }

    // 获取通知
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult showedResult) {
    }

    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult registerResult) {
        LogUtil.i(this, "Registered in notification server. errorCode=" + errorCode);
    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        Notification notification = null;
        try {
            String conentJson = message.getContent();
            NotificationEntity entity = GsonUtil.toObject(conentJson, NotificationEntity.class);
            notification = NotificationAdapter.instance.adapt(entity);
            UIHelper.updateNotification(notification);
        } catch (Exception e) {
            LogUtil.e(this, e);
            notification = handleWebCreatedMessage(context, message);
        }
        handleNotificationType(context, notification);
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        LogUtil.i(this, "Unregistered from notification server. errorCode=" + errorCode);
        // EventBusUtil.post(new LogoutEvent());
    }

    private Notification handleWebCreatedMessage(Context context, XGPushTextMessage message) {
        Notification notification = new Notification();
        String title = message.getTitle();
        if (StringUtil.isBlank(title)) {
            title = context.getString(R.string.notification_title);
        }
        notification.setTitle(title);
        notification.setContent(message.getContent());

        return notification;
    }

    /**
     *  值班的两种类型不需要发通知，其它类型发送通知
     * @param context
     * @param notification
     */
    private void handleNotificationType(Context context, Notification notification) {
        if ((notification.getType() == NotificationType.zb_squeezed_out) || (notification.getType() == NotificationType.zb_time_out)) {
            Class<?> cls = NotificationFunctionMappingFactory.getFunctionHandlerClass(notification.getType().toString());
            try {
                INotificationProcesser handler = (INotificationProcesser) cls.newInstance();
                handler.processNotification(context, notification);
            } catch (InstantiationException e) {
                LogUtil.e(this, e);
            } catch (IllegalAccessException e) {
                LogUtil.e(this, e);
            }
        } else {
            EventBusUtil.post(notification);
        }
    }
}
