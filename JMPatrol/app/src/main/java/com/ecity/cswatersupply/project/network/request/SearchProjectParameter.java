package com.ecity.cswatersupply.project.network.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/23.
 */

public class SearchProjectParameter implements Serializable {
    private static final long serialVersionUID = 1553930455101772851L;

    private int proTypeid;
    private String proType;
    private String proName;
    private String proCode;
    private int proProcess;
    private int proStatus;
    private String startTime;
    private String endTime;

    public SearchProjectParameter() {
        super();
    }

    public SearchProjectParameter(int proType, String proName, int proProcess, int proStatus, int startTimeAgo, String startTimeUp, String startTimeDown, String endTimeUp,
                                  String endTimeDown, String hkState, String jsState, String delegatestatus, String isSmallBusiness, String fszyCode) {
        super();
        this.proName = proName;
        this.proProcess = proProcess;
        this.proStatus = proStatus;
    }

    public int getProTypeid() {
        return proTypeid;
    }

    public void setProTypeid(int proTypeid) {
        this.proTypeid = proTypeid;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getProProcess() {
        return proProcess;
    }

    public void setProProcess(int proProcess) {
        this.proProcess = proProcess;
    }

    public int getProStatus() {
        return proStatus;
    }

    public void setProStatus(int proStatus) {
        this.proStatus = proStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }
}
