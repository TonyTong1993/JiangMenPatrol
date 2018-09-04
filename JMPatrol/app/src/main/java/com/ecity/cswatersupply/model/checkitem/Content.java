package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;
import java.util.List;

import com.z3app.android.util.StringUtil;

public class Content implements Serializable {
    private static final long serialVersionUID = 1L;

    private String etypeid;
    private String tableName;
    private List<InspectItem> InspectItems;
    private String groupid;

    public Content() {
        groupid = null;
        etypeid = null;
        tableName = null;
        InspectItems = null;
    }

    public Content(String groupid, String etypeid, String tableName, List<InspectItem> InspectItems) {
        this.groupid = groupid;
        this.etypeid = etypeid;
        this.tableName = tableName;
        this.InspectItems = InspectItems;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<InspectItem> getInspectItems() {
        return InspectItems;
    }

    public void setInspectItems(List<InspectItem> inspectItems) {
        InspectItems = inspectItems;
    }

    public String getEtypeid() {
        return etypeid;
    }

    public void setEtypeid(String etypeid) {
        this.etypeid = etypeid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean buildFromJSON(String json) {
        boolean flag = false;
        if (StringUtil.isEmpty(json)) {
            return flag;
        }

        return flag;
    }
}
