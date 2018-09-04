package com.ecity.cswatersupply.emergency.model;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.model.ILonlatProvider;
import com.ecity.cswatersupply.utils.ResourceUtil;
/**
 * 地震台站监测台站信息
 * @author ZiZhengzhuan
 *
 */
public class EQMonitorStationModel extends AModel implements ILonlatProvider{
    private static final long serialVersionUID = 1L;
    private String id;
    private String stationCode;
    //台站名称
    private String stationName;
    //台站启用时间
    private String time;
    //描述
    private String description;
    //台站地址
    private String location;
    //台站类型
    private String stationType;
    private String geologicalType;
    private String lightningLevel;
    //台站负责人
    private String manager;
    private String tel;
    //负责人电话
    private String moblieNumber;
    //相关照片
    private String images;
    private String memo;
    
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
    public String getStationCode() {
        return stationCode;
    }
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getStationType() {
        return stationType;
    }
    public void setStationType(String stationType) {
        this.stationType = stationType;
    }
    public String getGeologicalType() {
        return geologicalType;
    }
    public void setGeologicalType(String geologicalType) {
        this.geologicalType = geologicalType;
    }
    public String getLightningLevel() {
        return lightningLevel;
    }
    public void setLightningLevel(String lightningLevel) {
        this.lightningLevel = lightningLevel;
    }
    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getMoblieNumber() {
        return moblieNumber;
    }
    public void setMoblieNumber(String moblieNumber) {
        this.moblieNumber = moblieNumber;
    }
    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String toString() {

        return ResourceUtil.getStringById(R.string.eq_station_name) + stationName 
                + "\n" + ResourceUtil.getStringById(R.string.eq_station_location) + location 
                + "\n" + ResourceUtil.getStringById(R.string.earth_location) + ResourceUtil.getStringById(R.string.earth_quake_longitude) + getLongitude() + ResourceUtil.getStringById(R.string.earth_quake_latitude) + getLatitude()
                + "\n" + ResourceUtil.getStringById(R.string.eq_station_start_time) + time
                + "\n" + ResourceUtil.getStringById(R.string.eq_station_type) + stationType
                + "\n" + ResourceUtil.getStringById(R.string.eq_station_manager) + manager;
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
