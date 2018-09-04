package com.ecity.cswatersupply.model.planningTask;

public class ArrivePointInfo {
    private int taskid;
    private int gid;
    private int state; // 1 YES, 0 NO
    private String tasktype;

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    // 到位时间
    private String arriveTime;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ArrivePointInfo(int taskid, int gid, boolean isArrived, String arriveTime,String tasktype) {
        this.taskid = taskid;
        this.gid = gid;
        this.state = isArrived ? 1 : 0;
        this.arriveTime = arriveTime;
        this.tasktype = tasktype;
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

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }
}
