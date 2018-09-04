package com.ecity.cswatersupply.model.planningTask;

import java.util.ArrayList;

public class PointPartLegendInfo {

    public enum PointStatusType {
        UN_ARRIVED(0), ARRIVED(1), FEEDBACKED(2);
        PointStatusType(int style) {
            this.style = style;
        }
        final int style;
    }
    private ArrayList<Z3PlanTaskPointPart> pointParts;
    private String statusName;
    //是否到位  是否反馈
    private PointStatusType pointStatus;
    //当前状态下的点的数量
    private int pointNum;
    //是否显示这个状态下的点
    private int isShow;
    //对应的key
    private int key;
    
    public PointPartLegendInfo(int key,PointStatusType pointStatus, String statusName,ArrayList<Z3PlanTaskPointPart> pointParts, int pointNum, int isShow) {
        super();
        this.pointStatus = pointStatus;
        this.pointParts = pointParts;
        this.statusName = statusName;
        this.pointNum = pointNum;
        this.isShow = isShow;
        this.key = key;
    }
    
    public PointPartLegendInfo(PointStatusType pointStatus,int pointNum) {
        super();
        this.pointStatus = pointStatus;
        this.pointNum = pointNum;
    }
    
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public ArrayList<Z3PlanTaskPointPart> getPointParts() {
        return pointParts;
    }
    public void setPointParts(ArrayList<Z3PlanTaskPointPart> pointParts) {
        this.pointParts = pointParts;
    }
    public PointStatusType getPointStatus() {
        return pointStatus;
    }
    public void setPointStatus(PointStatusType pointStatus) {
        this.pointStatus = pointStatus;
    }
    public int getPointNum() {
        return pointNum;
    }
    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }
    public int getIsShow() {
        return isShow;
    }
    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
