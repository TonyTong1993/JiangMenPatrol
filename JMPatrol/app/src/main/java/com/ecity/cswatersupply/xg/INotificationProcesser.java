package com.ecity.cswatersupply.xg;

import android.content.Context;

import com.ecity.cswatersupply.xg.model.Notification;

public interface INotificationProcesser {
    void processNotification(Context context, Notification notification);
}
