package com.ecity.cswatersupply.model;

/**
 * Menu in list view.
 * 
 * @author jonathanma
 * 
 */
public class ListViewMenuItem {
    public String title;
    private String detail;
    private int iconId;

    public ListViewMenuItem(String title, String detail, int iconId) {
        super();
        this.title = title;
        this.detail = detail;
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getIconId() {
        return iconId;
    }
}
