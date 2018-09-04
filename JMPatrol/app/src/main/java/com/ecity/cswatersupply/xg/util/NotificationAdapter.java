package com.ecity.cswatersupply.xg.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.GsonUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationEntity;
import com.ecity.cswatersupply.xg.model.NotificationType;

public enum NotificationAdapter {
    instance;

    public Notification adapt(NotificationEntity entity) {
        Notification notification = new Notification();
        String title = ResourceUtil.getStringById(R.string.notification_title);
        notification.setId(entity.getId());
        notification.setTitle(title);
        notification.setContent(entity.getMessage());
        notification.setType(NotificationType.valueOf(entity.getType()));
        notification.setSentTime(entity.getSendTime());
        notification.setNextStepIds(entity.getNextStepIds());
        notification.setStatus(entity.getStatus());
        notification.setNewType(entity.getNewType());

        return notification;
    }

    public List<Notification> adaptNotifications(JSONArray jsonArray) {
        List<Notification> notifications = new ArrayList<Notification>();
        if ((jsonArray == null) || (jsonArray.length() == 0)) {
            return notifications;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.optJSONObject(i);
            if (json == null) {
                continue;
            }

            Notification notification = adapt(json);
            if (notification != null) {
                notifications.add(notification);
            }
        }

        return notifications;
    }

    private Notification adapt(JSONObject json) {
        Notification notification = null;
        try {
            NotificationEntity entity = GsonUtil.toObject(json.toString(), NotificationEntity.class);
            notification = adapt(entity);
        } catch (Exception e) {
            LogUtil.e(this, e);
        }

        return notification;
    }
}
