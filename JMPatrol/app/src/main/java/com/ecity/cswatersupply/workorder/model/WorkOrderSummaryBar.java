package com.ecity.cswatersupply.workorder.model;


public class WorkOrderSummaryBar {
    private String personName;
    private WorkOrderSummaryBarBean barDatas;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public WorkOrderSummaryBarBean getBarDatas() {
        return barDatas;
    }

    public void setBarDatas(WorkOrderSummaryBarBean barDatas) {
        this.barDatas = barDatas;
    }

}