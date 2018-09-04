package com.ecity.cswatersupply.workorder.model;

import java.io.Serializable;

public class MaterialServerInfo implements Serializable {
    private static final long serialVersionUID = 5798168343455985567L;

    private String gid;
    private String name;
    private String price;
    private String unit;
    private String diameter;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
