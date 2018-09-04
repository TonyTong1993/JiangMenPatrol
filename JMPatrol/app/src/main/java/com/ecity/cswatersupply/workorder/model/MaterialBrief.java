package com.ecity.cswatersupply.workorder.model;

import java.io.Serializable;
import java.util.List;

public class MaterialBrief implements Serializable {
    private static final long serialVersionUID = 2267993104909640972L;

    private String id;
    private String mName;
    private String unit;
    private List<String> mSelectValues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getSelectValues() {
        return mSelectValues;
    }

    public void setSelectValues(List<String> mSelectValues) {
        this.mSelectValues = mSelectValues;
    }
}
