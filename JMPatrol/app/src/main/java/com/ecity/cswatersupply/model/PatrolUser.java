package com.ecity.cswatersupply.model;

/***
 * 巡检员信息
 * @author ZiZhengzhuan
 *
 */
public class PatrolUser extends User implements ILonlatProvider {
    private static final long serialVersionUID = 1L;

    //经度
    private double longitude;
    //纬度
    private double latitude;
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
