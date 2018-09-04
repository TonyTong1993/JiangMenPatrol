/**   
 * 文件名：PipeItem.java   
 *   
 * 版本信息：   
 * 日期：2016年6月29日   
 * Copyright Ecity Corporation 2016    
 * 版权所有   
 *   
 */

package com.ecity.mobile.android.plugins.pipecross.ui;

import org.json.JSONObject;

/**
 * 此类描述的是：
 * 
 * @author: wly
 * @version: 2016年6月29日 下午5:11:24
 */

public class PipeItem {

    PipeItem item;
    boolean hasCenterRoad;
    String layerName;

    String code;
    double groundAltitude;
    double pipeAltitude;
    String pipeAltitudeType;
    String pipeName;
    int interSpace = -1; // 管段间距, 这里只是初始值
    double pipeDepth;
    double diameter0;
    double diameter2;
    double dianeters;
    JSONObject attributes;
    String roadName;
    double x;
    double y;
    double z;
    String pipeShapeType;
    int pipeColor;
    // 在屏幕上的坐标
    float itemX;
    float itemY;

    /**
     * 含pipeitem的构造器
     */
    public PipeItem(PipeItem item, float itemX, float itemY) {
        this.item = item;
        this.itemX = itemX;
        this.itemY = itemY;
    }

    /**
     * 创建一个新的实例 PipeItem.
     * 
     * @param hasCenterRoad
     * @param layerName
     * @param code
     * @param groundAltitude
     * @param pipeAltitude
     * @param pipeAltitudeType
     * @param pipeName
     * @param interSpace
     * @param pipeDepth
     * @param diameter0
     * @param diameter2
     * @param dianeters
     * @param attributes
     * @param roadName
     * @param x
     * @param y
     * @param z
     * @param pipeShapeType
     * @param pipeColor
     */

    public PipeItem(boolean hasCenterRoad, String layerName, String code,
            double groundAltitude, double pipeAltitude,
            String pipeAltitudeType, String pipeName, int interSpace,
            double pipeDepth, double diameter0, double diameter2,
            double dianeters, JSONObject attributes, String roadName, double x,
            double y, double z, String pipeShapeType, int pipeColor) {
        super();
        this.hasCenterRoad = hasCenterRoad;
        this.layerName = layerName;
        this.code = code;
        this.groundAltitude = groundAltitude;
        this.pipeAltitude = pipeAltitude;
        this.pipeAltitudeType = pipeAltitudeType;
        this.pipeName = pipeName;
        this.interSpace = interSpace;
        this.pipeDepth = pipeDepth;
        this.diameter0 = diameter0;
        this.diameter2 = diameter2;
        this.dianeters = dianeters;
        this.attributes = attributes;
        this.roadName = roadName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pipeShapeType = pipeShapeType;
        this.pipeColor = pipeColor;
    }

    /**
     * attributes
     * 
     * @return the attributes
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public JSONObject getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */

    public void setAttributes(JSONObject attributes) {
        this.attributes = attributes;
    }

    /**
     * pipeName
     * 
     * @return the pipeName
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getPipeName() {
        return pipeName;
    }

    /**
     * dianeters
     * 
     * @return the dianeters
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getDianeters() {
        return dianeters;
    }

    /**
     * @param dianeters
     *            the dianeters to set
     */

    public void setDianeters(double dianeters) {
        this.dianeters = dianeters;
    }

    /**
     * @param pipeName
     *            the pipeName to set
     */

    public void setPipeName(String pipeName) {
        this.pipeName = pipeName;
    }

    /**
     * roadName
     * 
     * @return the roadName
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getRoadName() {
        return roadName;
    }

    /**
     * @param roadName
     *            the roadName to set
     */

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    /**
     * x
     * 
     * @return the x
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */

    public void setX(double x) {
        this.x = x;
    }

    /**
     * y
     * 
     * @return the y
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */

    public void setY(double y) {
        this.y = y;
    }

    /**
     * z
     * 
     * @return the z
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getZ() {
        return z;
    }

    /**
     * @param z
     *            the z to set
     */

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * pipeDepth
     * 
     * @return the pipeDepth
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getPipeDepth() {
        return pipeDepth;
    }

    /**
     * @param pipeDepth
     *            the pipeDepth to set
     */

    public void setPipeDepth(double pipeDepth) {
        this.pipeDepth = pipeDepth;
    }

    /**
     * groundAltitude
     * 
     * @return the groundAltitude
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getGroundAltitude() {
        return groundAltitude;
    }

    /**
     * @param groundAltitude
     *            the groundAltitude to set
     */

    public void setGroundAltitude(double groundAltitude) {
        this.groundAltitude = groundAltitude;
    }

    /**
     * pipeAltitude
     * 
     * @return the pipeAltitude
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public double getPipeAltitude() {
        return pipeAltitude;
    }

    /**
     * @param pipeAltitude
     *            the pipeAltitude to set
     */

    public void setPipeAltitude(double pipeAltitude) {
        this.pipeAltitude = pipeAltitude;
    }

    /**
     * interSpace
     * 
     * @return the interSpace
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getInterSpace() {
        return interSpace;
    }

    /**
     * @param interSpace
     *            the interSpace to set
     */

    public void setInterSpace(int interSpace) {
        this.interSpace = interSpace;
    }

    /**
     * itemX
     * 
     * @return the itemX
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public float getItemX() {
        return itemX;
    }

    /**
     * @param itemX
     *            the itemX to set
     */

    public void setItemX(float itemX) {
        this.itemX = itemX;
    }

    /**
     * itemY
     * 
     * @return the itemY
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public float getItemY() {
        return itemY;
    }

    /**
     * @param itemY
     *            the itemY to set
     */

    public void setItemY(float itemY) {
        this.itemY = itemY;
    }

    /**
     * pipeColor
     * 
     * @return the pipeColor
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getPipeColor() {
        return pipeColor;
    }

    /**
     * @param pipeColor
     *            the pipeColor to set
     */

    public void setPipeColor(int pipeColor) {
        this.pipeColor = pipeColor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPipeAltitudeType() {
        return pipeAltitudeType;
    }

    public void setPipeAltitudeType(String pipeAltitudeType) {
        this.pipeAltitudeType = pipeAltitudeType;
    }

}
