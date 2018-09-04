package com.ecity.cswatersupply.emergency.model;

public class DownloadFragmentInfo {
    private String name;
    private EDownloadFragmentType type;
    private int iconId = 0;
    public DownloadFragmentInfo(String name, EDownloadFragmentType type) {
        this(name,type,0);
    }
    public DownloadFragmentInfo(String name, EDownloadFragmentType type,int iconId) {
        super();
        this.name = name;
        this.type = type;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public EDownloadFragmentType getType() {
        return type;
    }
    
    public int getIconId() {
        return iconId;
    }
}