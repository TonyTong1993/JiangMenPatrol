package com.ecity.cswatersupply.network.response.loginresponse;

import com.ecity.z3map.maploader.model.EMapLoadType;
import com.ecity.z3map.maploader.model.SourceConfig;
import com.esri.core.geometry.Envelope;

import java.util.ArrayList;

public class MobileConfig {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private String cityName = "";
    // 百度城市id
    private String cityId;
    // 百度城市范围
    private String cityRect;

    private Envelope initExtent;
    // 地图最大分辨率
    private double MaxResolution;
    // 地图最小分辨率
    private double MinResolution;

    private EMapLoadType mapLoadType = EMapLoadType.NORMAL;
    /***
     * 源配置 数组
     */
    private ArrayList<SourceConfig> sourceConfigArrayList;

    public ArrayList<SourceConfig> getSourceConfigArrayList() {
        return sourceConfigArrayList;
    }

    public void setSourceConfigArrayList(
            ArrayList<SourceConfig> sourceConfigArrayList) {
        this.sourceConfigArrayList = sourceConfigArrayList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Envelope getInitExtent() {
        return initExtent;
    }

    public void setInitExtent(Envelope initExtent) {
        this.initExtent = initExtent;
    }

    public double getMaxResolution() {
        return MaxResolution;
    }

    public void setMaxResolution(double maxResolution) {
        MaxResolution = maxResolution;
    }

    public double getMinResolution() {
        return MinResolution;
    }

    public void setMinResolution(double minResolution) {
        MinResolution = minResolution;
    }

    public String getCityRect() {
        return cityRect;
    }

    public void setCityRect(String cityRect) {
        this.cityRect = cityRect;
    }

    public EMapLoadType getMapLoadType() {
        return mapLoadType;
    }

    public void setMapLoadType(EMapLoadType mapLoadType) {
        this.mapLoadType = mapLoadType;
    }

    // 服务地址
    public static class TaskServer {
        public String name;
        public String value;
    }

    public void initSourceConfigArrayList() {
        sourceConfigArrayList = new ArrayList<SourceConfig>();
    }

    public void addSourceConfig(SourceConfig sourceconfig) {
        if (sourceConfigArrayList == null) {
            sourceConfigArrayList = new ArrayList<SourceConfig>();
        }
        sourceConfigArrayList.add(sourceconfig);
    }

}
