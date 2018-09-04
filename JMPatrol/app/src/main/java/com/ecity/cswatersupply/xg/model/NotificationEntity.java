package com.ecity.cswatersupply.xg.model;

import com.ecity.cswatersupply.model.AModel;
import com.google.gson.annotations.SerializedName;

public class NotificationEntity extends AModel {
    private static final long serialVersionUID = 1L;

    private int id;
    @SerializedName("item_ids")
    private String nextStepIds;
    @SerializedName("msg")
    private String message;
    @SerializedName("sendtime")
    private String sendTime;
    private String type;
    @SerializedName("isread")
    private String status;
    @SerializedName("ntype")
    private int newType;     /*添加一个 ntype 字段，ntype =0 的消息铃声为常规，ntype=1的消息铃声为需要播放特殊铃声*/

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getNextStepIds() {
        return nextStepIds;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getStatus() {
        return status;
    }

    public int getNewType() {
        return newType;
    }

    public void setNewType(int newType) {
        this.newType = newType;
    }
}
