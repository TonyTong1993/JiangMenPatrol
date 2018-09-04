package com.ecity.cswatersupply.project.model;

import java.io.Serializable;

public class ValueModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6718118732697717864L;

    private String name;
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
