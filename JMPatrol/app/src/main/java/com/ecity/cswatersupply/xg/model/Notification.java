package com.ecity.cswatersupply.xg.model;

import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.model.AModel;

public class Notification extends AModel {
    public static final String NOTIFICATION_STATUS_NEW = "0";
    public static final String NOTIFICATION_STATUS_PROCESSED = "1";
    public static final String NOTIFICATION_STATUS_DELETED = "2";

    private static final long serialVersionUID = 1L;

    private int id;
    private String gid;
    private String title;
    private String content;
    private NotificationType type;
    private String sentTime;
    private String receivedTime;
    private String nextStepIds;
    private String status;
    private int newType;     /*添加一个 newtype 字段，newtype =0 的消息铃声为常规，newtype=1的消息铃声为需要播放特殊铃声*/

    EarthQuakeInfoModel earthQuakeInfoModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getNextStepIds() {
        return nextStepIds;
    }

    public void setNextStepIds(String nextStepIds) {
        this.nextStepIds = nextStepIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNew() {
        return NOTIFICATION_STATUS_NEW.equals(status);
    }

    public boolean isRead() {
        return NOTIFICATION_STATUS_PROCESSED.equals(status);
    }

    public boolean isDeleted() {
        return NOTIFICATION_STATUS_DELETED.equals(status);
    }

    public int getNewType() {
        return newType;
    }

    public void setNewType(int newType) {
        this.newType = newType;
    }

    public EarthQuakeInfoModel getEarthQuakeInfoModel() {
        return earthQuakeInfoModel;
    }

    public void setEarthQuakeInfoModel(EarthQuakeInfoModel earthQuakeInfoModel) {
        this.earthQuakeInfoModel = earthQuakeInfoModel;
    }
}
