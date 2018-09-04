package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.model.AModel;

public class WorkOrderPieStaticsData extends AModel {
    private static final long serialVersionUID = 1L;
    private String group;
    private String groupTrueName;
    private String strDate;
    private String endDate;
    private String category;
    private boolean isYearType;

    public String getGroupTrueName() {
        return groupTrueName;
    }

    public void setGroupTrueName(String groupTrueName) {
        this.groupTrueName = groupTrueName;
    }
    public boolean isYearType() {
        return isYearType;
    }

    public void setYearType(boolean isYearType) {
        this.isYearType = isYearType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}