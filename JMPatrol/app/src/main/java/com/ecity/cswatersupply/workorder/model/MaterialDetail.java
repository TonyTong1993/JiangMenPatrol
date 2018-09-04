package com.ecity.cswatersupply.workorder.model;

import android.content.Context;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.IIncreaseInspectItemModel;

import java.io.Serializable;

public class MaterialDetail implements Serializable, IIncreaseInspectItemModel {
    private static final long serialVersionUID = -2727175630517685595L;
    
    private String parentId;
    private String parentName;
    private String price;
    private String unit;
    private String diameter;
    private boolean isSelected;
    private String count;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String mParentId) {
        this.parentId = mParentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String mParentName) {
        this.parentName = mParentName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String generateTitle(Context context) {
        return getParentName() + context.getString(R.string.space) + getDiameter() + getUnit();
    }

    @Override
    public String generateDetail(Context context) {
        return "";
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
