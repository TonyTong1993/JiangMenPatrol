package com.ecity.cswatersupply.model;

import java.util.Map.Entry;

public class MapEntry extends AModel implements Entry<String, String> {
    private static final long serialVersionUID = 1L;
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public String setValue(String value) {
        this.value = value;
        return value;
    }

    public MapEntry(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }
}
