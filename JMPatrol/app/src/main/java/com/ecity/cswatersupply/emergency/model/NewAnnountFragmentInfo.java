package com.ecity.cswatersupply.emergency.model;

public class NewAnnountFragmentInfo {
    private String name;
    private NewsAnnounFragmentType type;
    private int iconId = 0;
    public NewAnnountFragmentInfo(String name, NewsAnnounFragmentType type) {
        this(name,type,0);
    }
    public NewAnnountFragmentInfo(String name, NewsAnnounFragmentType type,int iconId) {
        super();
        this.name = name;
        this.type = type;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public NewsAnnounFragmentType getType() {
        return type;
    }
    
    public int getIconId() {
        return iconId;
    }
}
