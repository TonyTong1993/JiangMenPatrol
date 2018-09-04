package com.ecity.cswatersupply.xg.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.z3app.android.util.StringUtil;

public class NotificationListener extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = super.onStartCommand(intent, flags, startId);
        EventBusUtil.register(this); // Register to receive notification events.
        return id;
    }

    @Override
    public void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(Notification notification) {
        //可能有数据库操作
        NotificationUtil.showNotificationInStatusBar(this, notification);
        handleTaskNotificationType(notification, notification.getType());
    }
    
    public void handleTaskNotificationType(Notification notification,NotificationType type){
        switch (type) {
            case xj_point_arrived:
                String xj_nextStepId = notification.getNextStepIds();
                if (!StringUtil.isBlank(xj_nextStepId)) {
                    UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_REFLASH_XJ_POINT_STATUS, this);
                    event.setData(xj_nextStepId);
                    EventBusUtil.post(event);
                }
                break;
            case yh_point_arrived:
                String yh_nextStepId = notification.getNextStepIds();
                if (!StringUtil.isBlank(yh_nextStepId)) {
                    UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_REFLASH_YH_POINT_STATUS, this);
                    event.setData(yh_nextStepId);
                    EventBusUtil.post(event);
                }
                break;
            default:
                break;
        }
    }
    
    

    public class MyBinder extends Binder {
        public NotificationListener getService() {
            return NotificationListener.this;
        }
    }

    public static void start() {
        Context context = HostApplication.getApplication().getApplicationContext();
        Intent intent = new Intent(context, NotificationListener.class);
        context.startService(intent);
    }
}