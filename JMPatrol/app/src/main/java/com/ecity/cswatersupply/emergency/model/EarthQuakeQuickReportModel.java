package com.ecity.cswatersupply.emergency.model;

import com.ecity.cswatersupply.model.AModel;

/***
 * 灾情速报基础信息
 * @author ml
 *
 */
public class EarthQuakeQuickReportModel extends AModel {
    private static final long serialVersionUID = 1L;
    //编号
    private int gid;
    //地震编号
    private int earthQuakeId;
    //速报id
    private int did;
    //调查时间
    private String surveytTime;
    //调查地址
    private String surveyAddress;
    //地震名称
    private String earthQuakeName;
    //调查人
    private String surveyPerson;
    //经度
    private double longtitude;
    //纬度
    private double latitude;
    //地区
    private String area;
    //上报人id
    private String reporterid;
    //相关附件地址
    private String imageUrl;
    private String audioUrl;
    private String videoUrl;
    private String directory;
    //说明
    private String memo;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getEarthQuakeId() {
        return earthQuakeId;
    }

    public void setEarthQuakeId(int earthQuakeId) {
        this.earthQuakeId = earthQuakeId;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getSurveytTime() {
        return surveytTime;
    }

    public void setSurveytTime(String surveytTime) {
        this.surveytTime = surveytTime;
    }

    public String getSurveyAddress() {
        return surveyAddress;
    }

    public void setSurveyAddress(String surveyAddress) {
        this.surveyAddress = surveyAddress;
    }

    public String getEarthQuakeName() {
        return earthQuakeName;
    }

    public void setEarthQuakeName(String earthQuakeName) {
        this.earthQuakeName = earthQuakeName;
    }

    public String getSurveyPerson() {
        return surveyPerson;
    }

    public void setSurveyPerson(String surveyPerson) {
        this.surveyPerson = surveyPerson;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getReporterid() {
        return reporterid;
    }

    public void setReporterid(String reporterid) {
        this.reporterid = reporterid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

}
