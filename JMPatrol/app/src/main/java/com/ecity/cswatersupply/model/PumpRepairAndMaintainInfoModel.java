package com.ecity.cswatersupply.model;

/***  
 * Created by MaoShouBei on 2017/5/19.
 */

public class PumpRepairAndMaintainInfoModel extends AModel {
    public static final int OPERATE_TYPE_REPAIR = 1;
    public static final int OPERATE_TYPE_EVENT_REPORT = 2;
    public static final int OPERATE_TYPE_MAINTAIN_REPORT = 3;

    private String maintainType;
    private String maintainTime;
    private String maintainContent;
    private String maintainUserName;
    private String businessKey;


    public String getMaintainType() {
        return maintainType;
    }

    public void setMaintainType(String maintainType) {
        this.maintainType = maintainType;
    }

    public String getMaintainTime() {
        return maintainTime;
    }

    public void setMaintainTime(String maintainTime) {
        this.maintainTime = maintainTime;
    }

    public String getMaintainContent() {
        return maintainContent;
    }

    public void setMaintainContent(String maintainContent) {
        this.maintainContent = maintainContent;
    }

    public String getMaintainUserName() {
        return maintainUserName;
    }

    public void setMaintainUserName(String maintainUserName) {
        this.maintainUserName = maintainUserName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
