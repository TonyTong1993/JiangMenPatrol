package com.ecity.cswatersupply.model.planningTask;

public class ArriveLineInfo {
    private int taskid;
    private int gid;
    private int state; // 1 YES, 0 NO
    private String tasktype;
    private double arriveLength;
    private String arriveTime;
    private String geoType;

    public ArriveLineInfo(int taskid, int gid, boolean isArrived, String arriveTime,double arriveLength, String tasktype,String geoType) {
        this.taskid = taskid;
        this.gid = gid;
        this.state = isArrived ? 1 : 0;
        this.arriveTime = arriveTime;
        this.tasktype = tasktype;
        this.arriveLength = arriveLength;
        this.geoType = geoType;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public double getArriveLength() {
        return arriveLength;
    }

    public void setArriveLength(double arriveLength) {
        this.arriveLength = arriveLength;
    }

    public String getGeoType() {
        return geoType;
    }

    public void setGeoType(String geoType) {
        this.geoType = geoType;
    }

}
