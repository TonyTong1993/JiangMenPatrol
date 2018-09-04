package com.ecity.cswatersupply.emergency.model;

import android.graphics.Bitmap;

public class EmergencyItem {
    private Bitmap bitmap;
    private String detailUrl;
    private String imgurl;
    private String title;
    private String summary;
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getDetailUrl() {
        return detailUrl;
    }
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
