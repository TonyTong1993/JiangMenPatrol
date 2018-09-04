package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class PumpInsSelectValue implements Serializable{
    private static final long serialVersionUID = 1L;

    public String gid;
    public String alias;
    public String name;
    public String pumpRoad;
    public String x;
    public String y;
    public String pumpNO;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPumpRoad() {
        return pumpRoad;
    }

    public void setPumpRoad(String pumpRoad) {
        this.pumpRoad = pumpRoad;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getPumpNO() {
        return pumpNO;
    }

    public void setPumpNO(String pumpNO) {
        this.pumpNO = pumpNO;
    }
}
