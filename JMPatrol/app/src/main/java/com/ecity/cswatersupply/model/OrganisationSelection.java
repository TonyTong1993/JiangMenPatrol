package com.ecity.cswatersupply.model;

import java.util.List;

import com.zzz.ecity.android.applibrary.utils.ListUtil;

public class OrganisationSelection extends AModel {
    private static final long serialVersionUID = -3616138130324120825L;
    private int id;
    private String name;
    private String code;
    private boolean isSelected;
    private boolean isUser;
    private String phone;
    private String email;
    private String avatar;
    private List<OrganisationSelection> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 设置下面用户的选中状态。
     * @param isSelected
     */
    public void setChildUsersSelected(boolean isSelected) {
        if (children == null) {
            return;
        }

        for (OrganisationSelection child : children) {
            if (child.isUser()) {
                child.setSelected(isSelected);
            } else {
                child.setChildUsersSelected(isSelected);
            }
        }
    }

    public void setChildOrganisationsSelected(boolean isSelected) {
        if (children == null) {
            return;
        }

        for (OrganisationSelection child : children) {
            if (!child.isUser()) {
                child.setSelected(isSelected);
                child.setChildOrganisationsSelected(isSelected);
            }
        }
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }

    public List<OrganisationSelection> getChildren() {
        this.children = ListUtil.getSafeList(children);
        return children;
    }

    public void addChild(OrganisationSelection child) {
        this.children = ListUtil.getSafeList(children);
        this.children.add(child);
    }
}
