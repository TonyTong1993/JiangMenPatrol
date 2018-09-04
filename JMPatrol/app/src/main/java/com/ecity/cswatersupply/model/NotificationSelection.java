package com.ecity.cswatersupply.model;

import com.ecity.cswatersupply.xg.model.Notification;

/**
 * 用于待办工作里。在Notification的基础上，加了isSelected属性来标记是否选中。
 * @author jonathanma
 *
 */
public class NotificationSelection implements Comparable<NotificationSelection> {
    private Notification notification;
    private boolean isSelected;

    public NotificationSelection(Notification notification, boolean isSelected) {
        super();
        this.notification = notification;
        this.isSelected = isSelected;
    }

    public Notification getNotification() {
        return notification;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int compareTo(NotificationSelection another) {
        if (another != null) {
            return notification.getSentTime().compareTo(another.getNotification().getSentTime());
        }

        return -1;
    }
}
