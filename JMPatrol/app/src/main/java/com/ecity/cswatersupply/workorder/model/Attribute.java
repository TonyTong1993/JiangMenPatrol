package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.model.AModel;
import com.google.gson.annotations.SerializedName;

public class Attribute extends AModel {
    private static final long serialVersionUID = 1L;
    @SerializedName("name")
    public String fieldName = "";
    @SerializedName("alias")
    public String fieldAlias = "";

    public String fieldType = "";

    @SerializedName("value")
    public String fieldValue = "";
}
