package com.ecity.cswatersupply.emergency.model;

import android.location.Location;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.model.ILonlatProvider;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;
import com.zzz.ecity.android.applibrary.service.PositionService;

import java.text.DecimalFormat;

/***
 * 地震信息
 * @author ZiZhengzhuan
 *
 */
public class EarthQuakeInfoModel extends AModel implements ILonlatProvider {
    private static final long serialVersionUID = 1L;
    private int id;
    //发震时间
    private String time;
    //震级
    private double ML;
    private String MS;
    //震源深度
    private String depth;
    private String location;
    //描述
    private String description;
    //照片
    private String images;
    //波形文件
    private String waveFile;
    private String memo;
    //发震地点
    private String region;
    private String type;
    private String influence;
    private int flag;
    private String status;
    private int deaths;

    //经度
    private double longitude;
    //纬度
    private double latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getML() {
        return ML;
    }

    public void setML(double mL) {
        ML = mL;
    }

    public String getMS() {
        return MS;
    }

    public void setMS(String mS) {
        MS = mS;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getWaveFile() {
        return waveFile;
    }

    public void setWaveFile(String waveFile) {
        this.waveFile = waveFile;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public String toString() {
        DecimalFormat myformat=new java.text.DecimalFormat("0.000000");
        String ti = StringUtil.isBlank(time) ? "无": time;
        String des = StringUtil.isBlank(location) ? region : location;
        String loc = StringUtil.isBlank(des) ? "无": des;
        String de = StringUtil.isBlank(depth) ? "无": depth;

        return ResourceUtil.getStringById(R.string.earth_quake_start_time) + ti
                + "\n" + ResourceUtil.getStringById(R.string.earth_quake_region) + loc
                + "\n" + ResourceUtil.getStringById(R.string.earth_location) + ResourceUtil.getStringById(R.string.earth_quake_longitude) + myformat.format(getLongitude()) + ResourceUtil.getStringById(R.string.earth_quake_latitude) + myformat.format(getLatitude())
                + "\n" + ResourceUtil.getStringById(R.string.earth_quake_length_from_you) + getQuakeLenFromYou() + ResourceUtil.getStringById(R.string.earth_quake_length_unit)
                + "\n" + ResourceUtil.getStringById(R.string.earth_quake_ML) + ML
                + "\n" + ResourceUtil.getStringById(R.string.earth_quake_deepth) + de;
    }

    public String getQuakeLenFromYou() {
        Location location = PositionService.getLastLocation();
        if (location != null) {
            double length = GeometryUtil.calculateLength(location.getLongitude(), location.getLatitude(), getLongitude(), getLatitude());
            length = length / 1000;
            length = Math.round(length * 100) / 100;
            return String.valueOf(length);
        } else {
            return "正在获取位置信息，请稍后…";
        }
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
