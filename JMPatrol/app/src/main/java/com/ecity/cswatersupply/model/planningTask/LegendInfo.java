package com.ecity.cswatersupply.model.planningTask;

import java.util.ArrayList;

public class LegendInfo {

    public enum StatusType {
       POINT(1), PLAN_LINE(2), WALKED_LINE(3), TRAJECTORY_LINE(4);
        StatusType(int style) {
            this.style = style;
        }
        final int style;
    }
    //item_type 用来辨别item属于那一类  point？line
    private String item_type;
    private ArrayList<PointPartLegendInfo> pointParts;
    private int colorid;
    private StatusType status;
    private String legendNameForLine;
    private double lineLength;
    private int isShow;
    //对应的key
    private int key;
    
    public LegendInfo(String item_type,String legendNameForLine,StatusType status, double lineLength, int isShow,int colorid) {
        super();
        this.legendNameForLine = legendNameForLine;
        this.item_type = item_type;
        this.status = status;
        this.isShow = isShow;
        this.lineLength = lineLength;
        this.colorid = colorid;
    }
    
    public LegendInfo(String item_type, ArrayList<PointPartLegendInfo> pointParts,int isShow,StatusType status){
        super();
        this.item_type = item_type;
        this.pointParts = pointParts;
        this.isShow = isShow;
        this.status = status;
    }
    
    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getLegendNameForLine() {
        return legendNameForLine;
    }

    public void setLegendNameForLine(String legendNameForLine) {
        this.legendNameForLine = legendNameForLine;
    }

    public int getColorid() {
        return colorid;
    }

    public void setColorid(int colorid) {
        this.colorid = colorid;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public ArrayList<PointPartLegendInfo> getPointParts() {
        return pointParts;
    }
    public void setPointParts(ArrayList<PointPartLegendInfo> pointParts) {
        this.pointParts = pointParts;
    }
    public StatusType getStatus() {
        return status;
    }
    public void setStatus(StatusType status) {
        this.status = status;
    }
    public int getIsShow() {
        return isShow;
    }
    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public double getLineLength() {
        return lineLength;
    }

    public void setLineLength(double lineLength) {
        this.lineLength = lineLength;
    }
}
