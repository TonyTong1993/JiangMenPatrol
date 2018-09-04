package com.ecity.cswatersupply.contact.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class ContactGroup {
    // 组织结构名称
    private String groupName;

    //组织结构包含的子结构
    private List<ContactGroup> childGroup;

    //组织结构包含的联系人
    private List<Contact> contacts;

    /**
     * 这个group 分类字段的value
     */
    public List<String> keyValues;

    public ContactGroup() {
        childGroup = new ArrayList<ContactGroup>();
        contacts = new ArrayList<Contact>();
        keyValues = new ArrayList<String>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ContactGroup> getChildGroup() {
        return childGroup;
    }

    public void setChildGroup(List<ContactGroup> childGroup) {
        this.childGroup = childGroup;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof ContactGroup) {
            ContactGroup group = (ContactGroup) o;
            return getValueString().equals(group.getValueString());
        }
        return false;
    }

    public String getValueString() {
        String valueStr = "";
        if (keyValues == null || keyValues.isEmpty()) {
            return valueStr;
        }

        for (Iterator<String> iterator = keyValues.iterator(); iterator.hasNext(); ) {
            String str = (String) iterator.next();
            valueStr += str + ";";
        }
        return valueStr;
    }
}
