package com.ecity.cswatersupply.model.checkitem;

import com.ecity.cswatersupply.model.AModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspectItemSelectValue extends AModel {
    private static final long serialVersionUID = 1L;

    @SerializedName("name")
    public String gid;
    @SerializedName("alias")
    public String name;
    @SerializedName("selectValues")
    private List<InspectItemSelectValue> childSelectValue;

    public InspectItemSelectValue() {
    }

    public InspectItemSelectValue(String gid, String name) {
        this.gid = gid;
        this.name = name;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InspectItemSelectValue> getChildSelectValue() {
        return childSelectValue;
    }

    public void setChildSelectValue(List<InspectItemSelectValue> childSelectValue) {
        this.childSelectValue = childSelectValue;
    }
}
