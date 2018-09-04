package com.ecity.cswatersupply.workorder.model;

public class WorkOrderSummaryBarBean {
    private String toDealAmount;
    private String dealingAmount;
    private String finishedAmount;
    private String postponeAmount;
    private String overtimeAmount;

    public String getToDealAmount() {
        return toDealAmount;
    }

    public void setToDealAmount(String toDealAmount) {
        this.toDealAmount = toDealAmount;
    }

    public String getDealingAmount() {
        return dealingAmount;
    }

    public void setDealingAmount(String dealingAmount) {
        this.dealingAmount = dealingAmount;
    }

    public String getFinishedAmount() {
        return finishedAmount;
    }

    public void setFinishedAmount(String finishedAmount) {
        this.finishedAmount = finishedAmount;
    }

    public String getPostponeAmount() {
        return postponeAmount;
    }

    public void setPostponeAmount(String postponeAmount) {
        this.postponeAmount = postponeAmount;
    }

    public String getOvertimeAmount() {
        return overtimeAmount;
    }

    public void setOvertimeAmount(String overtimeAmount) {
        this.overtimeAmount = overtimeAmount;
    }

}