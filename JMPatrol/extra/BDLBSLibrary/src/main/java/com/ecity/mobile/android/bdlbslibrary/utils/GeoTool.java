package com.ecity.mobile.android.bdlbslibrary.utils;

import java.text.DecimalFormat;

/**
 * <b>包名：<br/></b>com.ecity.patrol.tools<br/>
 * <b>文件名：<br/></b>GeometryUtil.java<br/>
 * <b>版本信息：<br/></b><br/>
 * <b>创建日期：<br/></b>2013-12-11-下午12:50:15<br/>
 * <b>类名称：<br/></b>GeometryUtil<br/>
 * <b>类描述：<br/></b>几何图形处理<br/>
 * <b>创建人：<br/></b>ZiZhengzhuan<br/>
 * <b>修改人：<br/></b>ZiZhengzhuan<br/>
 * <b>修改时间：<br/></b>2013-12-11 下午12:50:15<br/>
 * <b>修改备注：<br/></b>无<br/>
 * <b>Copyright (c)</b> 2013版权所有<br/>
 */
public class GeoTool {
    /***
     *
     * @param point 目标点
     * @param circleCenter 圆心
     * @param r 半径
     * @return
     */
    public static String calcTowPointsDistance(double x1, double y1, double x2, double y2) {
        double distance = distance(x1, y1, x2, y2);

        if (distance < 1000) {
            return ((int) distance) + "米";
        } else {
            return new DecimalFormat("0.00").format(distance) + "千米";
        }
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

    }

    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double calculateLength(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

    /****
     * 经纬度转墨卡托
     * @param lon
     * @param lat
     * @return
     * @throws Exception
     */
    public static double[] lonLat2Mercator(double lon, double lat) throws Exception {
        if (Math.abs(lon) > 180 || Math.abs(lat) > 90) throw new Exception("无效的经纬度坐标!");

        double pi = 3.14159265358979324;
        double[] xy = new double[2];
        double x = lon * 20037508.342789 / 180;

        double y = Math.log(Math.tan((90 + lat) * pi / 360)) / (pi / 180);

        y = y * 20037508.34789 / 180;
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    /***
     * 墨卡托转经纬度
     * @param mercatorX
     * @param mercatorY
     * @return
     */
    public static double[] Mercator2lonLat(double mercatorX, double mercatorY) {
        double pi = 3.14159265358979324;
        double[] xy = new double[2];
        double x = mercatorX / 20037508.34 * 180;
        double y = mercatorY / 20037508.34 * 180;

        y = 180 / pi * (2 * Math.atan(Math.exp(y * pi / 180)) - pi / 2);

        xy[0] = x;
        xy[1] = y;
        return xy;
    }
}