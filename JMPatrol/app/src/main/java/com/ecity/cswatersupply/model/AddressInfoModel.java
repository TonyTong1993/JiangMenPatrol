package com.ecity.cswatersupply.model;

import java.io.Serializable;

import com.esri.core.geometry.Point;

public class AddressInfoModel implements Serializable{

    private static final long serialVersionUID = 1L;
    //地址类型
    private String type;
    private String name;
    //详细地址
    private String address;
    //百度坐标
    private double naviX;
    private double naviY;
    //本地坐标
    private double X;
    private double Y;

    private Point point;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getNaviX() {
        return naviX;
    }
    public void setNaviX(double naviX) {
        this.naviX = naviX;
    }
    public double getNaviY() {
        return naviY;
    }
    public void setNaviY(double naviY) {
        this.naviY = naviY;
    }
    public double getX() {
        return X;
    }
    public void setX(double x) {
        X = x;
    }
    public double getY() {
        return Y;
    }
    public void setY(double y) {
        Y = y;
    }
    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean equals(Object o) {
        if(o == null)
        {
            return false;
        }
        if (o == this)
        {
           return true;
        }
        if (getClass() != o.getClass())
        {
            return false;
        }
        AddressInfoModel infoModel = (AddressInfoModel) o;
        return this.name.equals(infoModel.getName());
    }

}
