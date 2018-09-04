package com.ecity.cswatersupply.emergency.model;

import android.location.Location;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.model.ILonlatProvider;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;

/***
 * 避难所信息
 * @author ZiZhengzhuan
 *
 *
 */
public class EQRefugeInfoModel extends AModel implements ILonlatProvider {
    private static final long serialVersionUID = 1L;
    private String id;
    private String number;//编号
    private String name;//名称，清风公园
    private String regionalism;//所属行政区，城西组团
    private String scope;//范围
    private String space;//占地面积
    private String safespace;//有效疏散面积
    private String type;//类型 公园
    private String ssunit;//所属单位
    private String glunit;//管理单位
    private String areaPlan;//场所平面图
    private String peopleNum;//疏散人数
    private String areaPhoto;//场所照片
    private String classfy;//分类
    private String attachment;//附件

    //经度
    private double longitude;
    //纬度
    private double latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionalism() {
        return regionalism;
    }

    public void setRegionalism(String regionalism) {
        this.regionalism = regionalism;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getSafespace() {
        return safespace;
    }

    public void setSafespace(String safespace) {
        this.safespace = safespace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSsunit() {
        return ssunit;
    }

    public void setSsunit(String ssunit) {
        this.ssunit = ssunit;
    }

    public String getGlunit() {
        return glunit;
    }

    public void setGlunit(String glunit) {
        this.glunit = glunit;
    }

    public String getAreaPlan() {
        return areaPlan;
    }

    public void setAreaPlan(String areaPlan) {
        this.areaPlan = areaPlan;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getAreaPhoto() {
        return areaPhoto;
    }

    public void setAreaPhoto(String areaPhoto) {
        this.areaPhoto = areaPhoto;
    }

    public String getClassfy() {
        return classfy;
    }

    public void setClassfy(String classfy) {
        this.classfy = classfy;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("名称：").append(name).append("\n").append("所属行政区：").append(regionalism).append("\n")
                .append("占地面积：").append(space).append("\n").append("有效疏散面积：").append(safespace).append("\n")
                .append("类型：").append(type);

        return builder.toString();
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
