package com.ecity.cswatersupply.project.adapter;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.GsonUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationProEntity;
import com.ecity.cswatersupply.xg.model.NotificationType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectNotificationAdapter {

    private static ProjectNotificationAdapter instance;

    static {
        instance = new ProjectNotificationAdapter();
    }

    public static ProjectNotificationAdapter getInstance() {
        return instance;
    }

    public Notification adapt(NotificationProEntity entity) {
        Notification notification = new Notification();
        String title = ResourceUtil.getStringById(R.string.notification_title);
        notification.setId(Integer.valueOf(entity.getProid()));
        notification.setTitle(title);
        notification.setContent(entity.getMsg());
        notification.setType(NotificationType.valueOf(entity.getType()));
        notification.setSentTime(entity.getSendtime());
        notification.setNextStepIds(entity.getFunid());

        notification.setGid(entity.getGid());

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
            NotificationProEntity entity = GsonUtil.toObject(json.toString(), NotificationProEntity.class);
            notification = adapt(entity);
        } catch (Exception e) {
            LogUtil.e(this, e);
        }

        return notification;
    }
}
