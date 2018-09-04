package com.ecity.mobile.android.bdlbslibrary.model;

import java.util.List;

public class AddressInfo {
    private String title;
    private double latitude;//纬度
    private double longitude;//经度
    private float radius;//定位精度半径
    private String addrStr;//反地理编码，文字描述的地址
    private String province;//省份信息
    private String city;//城市信息
    private String district;//区县信息
    private float direction;//手机方向,范围0-360，手机上部正朝向北的方向为0度方向
    private List<String> listAddressInfo;//根据关键字搜索的建议信息列表

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AddressInfo copy() {
        AddressInfo copy = new AddressInfo();
        copy.setLatitude(this.getLatitude());
        copy.setLongitude(this.getLongitude());
        copy.setRadius(this.getRadius());
        copy.setAddrStr(this.getAddrStr());
        copy.setProvince(this.getProvince());
        copy.setGetCity(this.getCity());
        copy.setDistrict(this.getDistrict());
        copy.setDirection(this.getDirection());
        copy.setTitle(this.getTitle());
        return copy;
    }

    public List<String> getListAddressInfo() {
        return listAddressInfo;
    }

    public void setListAddressInfo(List<String> listAddressInfo) {
        this.listAddressInfo = listAddressInfo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getGetCity() {
        return city;
    }

    public void setGetCity(String getCity) {
        this.city = getCity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}