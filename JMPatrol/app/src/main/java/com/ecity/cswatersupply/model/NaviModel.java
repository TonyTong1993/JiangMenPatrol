package com.ecity.cswatersupply.model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class NaviModel implements Serializable{

    private static final long serialVersionUID = 1L;
    private Drawable icon;
    private String name;
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
