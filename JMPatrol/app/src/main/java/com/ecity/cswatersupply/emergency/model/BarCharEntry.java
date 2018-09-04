package com.ecity.cswatersupply.emergency.model;

import java.io.Serializable;

/**
 * 柱状图数据模型 
 * @author Gxx 2016-11-22
 *
 */
public class BarCharEntry implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 房屋数量
     */
    private String value;
    /**
     * 房屋结构
     */
    private String structure;

    

    public BarCharEntry() {
    }

    public BarCharEntry(String value, String structure) {
        this.value = value;
        this.structure = structure;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

}
