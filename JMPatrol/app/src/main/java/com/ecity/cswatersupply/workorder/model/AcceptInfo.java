package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.model.AModel;
import com.google.gson.annotations.SerializedName;

public class AcceptInfo extends AModel{
    private static final long serialVersionUID = 1L;
    @SerializedName("acceptuser")
    public String acceptUser = "";
    @SerializedName("acceptdepartment")
    public String acceptDepartment = "";
}
