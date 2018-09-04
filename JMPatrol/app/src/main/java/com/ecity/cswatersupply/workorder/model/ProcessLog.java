package com.ecity.cswatersupply.workorder.model;

import java.util.ArrayList;

import com.ecity.cswatersupply.model.AModel;
import com.google.gson.annotations.SerializedName;

public class ProcessLog extends AModel{
    private static final long serialVersionUID = 1L;
    public int workorderid = 0;
    public int nodeid = 0;
    public String processtime = "";
    public String username = "";
    public String addtime = "";
    @SerializedName("requirefinishtime")
    public String requireFinishtime = "";//要求完成时间
    
    public ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    public ArrayList<AcceptInfo> accepts = new ArrayList<AcceptInfo>();

}
