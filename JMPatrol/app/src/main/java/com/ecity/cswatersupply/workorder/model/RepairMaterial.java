package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.model.AModel;

public class RepairMaterial extends AModel{
    private static final long serialVersionUID = 1L;
    public String name;
    public String diameter;
    public String price;
    public String type;
    public String unit;
    public double count = 1;
    public int gid;
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        sb.append(diameter + " ");
        sb.append(name + " ");
        sb.append(count + "个");
        return sb.toString();
    }
}
