package com.ecity.cswatersupply.contact.model;

import com.z3app.android.util.StringUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class Contact implements Serializable{


    private String id;
    // 组织 与 GroupContact中的GroupName对应
    private String type;
    // 部门
    private String department;
    private String name;
    private String sex;
    // 职位
    private String position;
    // 手机号码
    private String mobile;
    // 电话号码
    private String tel;
    // 城市
    private String city;
    //区
    private String region;
    //街道
    private String village;
    //社区
    private String community;

    public Contact() {

    }

    public Contact(String type, String department, String name, String sex, String position,String mobile,String tel) {
        this.type = type;
        this.department = department;
        this.name = name;
        this.sex = sex;
        this.position = position;
        this.mobile = mobile;
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        if( StringUtil.isBlank(name) ) {
            name = "#";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @param key
     * @return
     * 获取对应字段的值
     */
    public String getValueOfKey(String key){
        //需要自己去实现，先只写几个简单示例，可以不用写这么复杂的。。。
        if (key == null) {
            return null;
        }
        if (key.equals("typeName")) {
            return type;
        }
        else if (key.equals("dept")) {
            return department;
        }
        else if (key.equals("prefecturalLevelcity")) {
            return city;
        }
        else if (key.equals("cityCounty")) {
            return region;
        }
        else if (key.equals("villages")) {
            return village;
        }
        else {
            //自己加上别的字段吧，或者使用反射之类的其他形式，写这么多if，明显是不行的。。。
            //这里只是示范
            return null;
        }

    }
}
