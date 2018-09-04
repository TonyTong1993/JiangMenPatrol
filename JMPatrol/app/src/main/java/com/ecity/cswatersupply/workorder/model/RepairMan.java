package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.model.AModel;

public class RepairMan extends AModel{
    private static final long serialVersionUID = 1L;
    
    public int userId;
    public String userName;
    public String rate = "";
    public int isjiaban = 0;
    public String starttime = "";
    public String endtime = "";
    public int gid = 0;
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(userName + " ");
        if (isjiaban == 1) {
            sb.append(" 加班费倍率" + rate);
        }

        return sb.toString();     
    }
}
