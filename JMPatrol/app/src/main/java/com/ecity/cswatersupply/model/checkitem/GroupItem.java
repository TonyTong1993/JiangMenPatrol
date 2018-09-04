
package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;
import java.util.List;

public class GroupItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String alias;
    private boolean isVisible;
    private List<ChildrenItem> childrenItems;

    public GroupItem() {
        name = null;
        alias = null;
        isVisible = true;
        childrenItems = null;
    }

    public GroupItem(String name,String alias, boolean isVisible, List<ChildrenItem> childrenItems) {
        this.name = name;
        this.alias = alias;
        this.isVisible = isVisible;
        this.childrenItems = childrenItems;
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

    public List<ChildrenItem> getChildrenItems() {
        return childrenItems;
    }

    public void setChildrenItems(List<ChildrenItem> childrenItems) {
        this.childrenItems = childrenItems;
    }
}
