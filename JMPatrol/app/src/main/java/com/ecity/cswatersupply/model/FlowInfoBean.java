package com.ecity.cswatersupply.model;

public class FlowInfoBean {

    private String addTime;//发起时间
    private String addMan;//申请人
    private String type;//流程类型
    private String state;//状态
    private String processinstanceid;//工作流

    public FlowInfoBean(String addTime, String addMan, String type, String state ,String processinstanceid) {
        super();
        this.addTime = addTime;
        this.addMan = addMan;
        this.type = type;
        this.state = state;
        this.processinstanceid = processinstanceid;
    }

    public String getProcessinstanceid() {
        return processinstanceid;
    }

    public void setProcessinstanceid(String processinstanceid) {
        this.processinstanceid = processinstanceid;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddMan() {
        return addMan;
    }

    public void setAddMan(String addMan) {
        this.addMan = addMan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
