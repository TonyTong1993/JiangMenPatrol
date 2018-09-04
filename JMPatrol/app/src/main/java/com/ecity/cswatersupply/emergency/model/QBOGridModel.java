package com.ecity.cswatersupply.emergency.model;

/**
 * 速报总览界面GirdView的item数据模型
 * @author Gxx 2016-11-21
 *
 */
public class QBOGridModel {

    private String name;
    private String value;
    private String unit;
    private int color;

    public QBOGridModel(String name, String value, String unit, int color) {
        super();
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
