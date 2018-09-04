package com.ecity.cswatersupply.workorder.model;

import java.io.Serializable;

import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeHasSelected;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeId;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeIsName;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeIsSelected;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeLabel;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodePid;

public class TreeViewLocalData implements Serializable {
    private static final long serialVersionUID = 8684875962988114348L;

    @TreeNodeId
    private String id;
    @TreeNodePid
    private String pId;
    @TreeNodeLabel
    private String alias;
    @TreeNodeIsSelected
    private boolean isSelected;//该节点是否被选中
    @TreeNodeHasSelected
    private boolean hasSelected;//该节点是否有子节点被选中
    @TreeNodeIsName
    private String name;

    /*private ArrayList<WorkOrderFinishHandleType> children;
    
    public WorkOrderFinishHandleType() {
        children = new ArrayList<WorkOrderFinishHandleType>();
    }*/

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }
}
