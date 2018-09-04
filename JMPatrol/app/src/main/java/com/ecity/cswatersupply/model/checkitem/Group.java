package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String alias;
    private boolean isVisible;
    private List<Group> groups;

    public Group() {
        name = null;
        alias = null;
        isVisible = true;
        groups = null;
    }

    public Group(String name,String alias, boolean isVisible, List<Group> groups) {
        this.name = name;
        this.alias = alias;
        this.isVisible = isVisible;
        this.groups = groups;
    }

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
    
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
