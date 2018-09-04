package com.ecity.cswatersupply.ui.widght.treeview.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable {
    private static final long serialVersionUID = -2281105242257855714L;

    private String id;
    private String pid;// 父id
    private String name;// 节点名
    private String group;// 节点名

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @SuppressWarnings("unused")
    private int level;// 树的层级
    private boolean isExpand = false;// 是否展开
    private int icon;// 图标
    private TreeNode parent;// 父节点
    private boolean isSelected;//该节点是否被选中
    private boolean hasSelected;//该节点下是否有子节点被选中
    private List<TreeNode> children = new ArrayList<TreeNode>();

    public TreeNode() {
        super();
    }

    public TreeNode(String id, String pid, String name) {
        super();
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * 得到当前节点的层级
     * 
     * @return
     */
    public int getLevel() {
        return parent == null ? 1 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        // 如果是折叠则将其子节点也折叠
        if (!isExpand) {
            for (TreeNode node : children) {
                node.setExpand(isExpand);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    /**
     * 是否是根节点
     * 
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 是否是二级节点
     * @return
     */
    public boolean isSecondNode() {
        return !isRoot() && !isLeaf();
    }

    /**
     * 判断父节点是否是展开状态
     * 
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null) {
            return false;
        } else {
            return parent.isExpand();
        }
    }

    /**
     * 判断是否是叶子节点
     * 
     * @return
     */
    public boolean isLeaf() {
        return children.size() <= 0;
    }
}
