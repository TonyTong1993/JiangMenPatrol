package com.ecity.cswatersupply.xg.model;

import com.ecity.cswatersupply.model.AModel;

public class NotificationProEntity extends AModel {

    private static final long serialVersionUID = 7435253569843883788L;

    private String gid;
    private String type;
    private String sendtime;
    private String proid;
    private String funid;
    private String msg;
    private String isread;
    private String isdel;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getFunid() {
        return funid;
    }

    public void setFunid(String funid) {
        this.funid = funid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }
}
