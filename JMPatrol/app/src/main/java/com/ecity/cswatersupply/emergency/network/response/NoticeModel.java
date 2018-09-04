package com.ecity.cswatersupply.emergency.network.response;

/**
 * Created by Administrator on 2017/4/18.
 */

public class NoticeModel {
    private String gid;
    private String title;
    private String content;
    private String onlytext;
    private String createrid;
    private String creater;
    private String createtime;

    private boolean isRead;

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

    public String getOnlytext() {
        return onlytext;
    }

    public void setOnlytext(String onlytext) {
        this.onlytext = onlytext;
    }

    public String getCreaterid() {
        return createrid;
    }

    public void setCreaterid(String createrid) {
        this.createrid = createrid;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
