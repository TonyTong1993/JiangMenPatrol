package com.ecity.cswatersupply.utils;

import com.ecity.zzz.pipegps.model.GpsXYZ;
import com.ecity.zzz.pipegps.util.CoordinateConvertor;

/** 
 * @Title CoordTransfer.java
 * @Package com.ecity.GPSPatrol.utils 
 * Description:<br/>
 * @version V1.0 
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月19日
 * @email 
 * @copyright ECity 2015
 */

public class CoordTransfer {
    public static CoordinateConvertor coordinateConvertor = new CoordinateConvertor();
    public enum ETRANSTYPE {
        GaoDe, Parameters
    }

    /***
     * *
     * Description:本地坐标转为经纬度<br/>
     * @param x
     * @param y
     * @return
     * @version V1.0
     */
    public static double[] transToLatlon(double x, double y) {
        return transToLatlon(x, y, ETRANSTYPE.Parameters);
    }

    /***
     * *
     * Description:本地坐标转为经纬度<br/>
     * @param x
     * @param y
     * @return
     * @version V1.0
     */
    public static double[] transToLatlon(double x, double y, ETRANSTYPE type) {
        double[] xy = new double[2];
        xy[0] = 0.0;
        xy[1] = 0.0;

        double pi = 3.14159265358979324;
        double tx = x / 20037508.34 * 180;
        double ty = y / 20037508.34 * 180;

        switch (type) {
            case GaoDe:
                ty = 180 / pi * (2 * Math.atan(Math.exp(ty * pi / 180)) - pi / 2);

                xy[0] = tx;
                xy[1] = ty;
                break;
            case Parameters:
                if (null == coordinateConvertor.gpsTransFull) {
                    xy[0]= x;
                    xy[1]= y;
                    return xy;
                }

                GpsXYZ tgxy = coordinateConvertor.convert2BLH(x, y, 0);
                xy[0] = tgxy.getX();
                xy[1] = tgxy.getY();
                break;

            default:
                ty = 180 / pi * (2 * Math.atan(Math.exp(ty * pi / 180)) - pi / 2);
                xy[0] = tx;
                xy[1] = ty;
                break;
        }
        return xy;
    }

    /***
    * *
    * Description:转为本地坐标<br/>
    * @param lat
    * @param lon
    * @return
    * @version V1.0
    */
    public static double[] transToLocal(double lat, double lon) {
        return transToLocal(lat, lon, ETRANSTYPE.Parameters);
    }

    /***
     * *
     * Description:转为本地坐标<br/>
     * @param lat
     * @param lon
     * @return
     * @version V1.0
     */
    public static double[] transToLocal(double lat, double lon, ETRANSTYPE type) {
        double[] xy = new double[2];
        xy[0] = lon;
        xy[1] = lat;
        if (outOfChina(lat, lon)) {
            return xy;
        }
        switch (type) {
            case GaoDe:
                double[] gaodeLatLng = transform(lat, lon);
                if (null != gaodeLatLng && 2 == gaodeLatLng.length) {
                    try {
                        xy = GeometryUtil.lonLat2Mercator(gaodeLatLng[1], gaodeLatLng[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Parameters:
                if (null == coordinateConvertor.gpsTransFull) {
                    return xy;
                }
                
                GpsXYZ tgxy = new GpsXYZ(lon, lat);
                if (coordinateConvertor.Convert(tgxy)) {
                    xy[0] = tgxy.getX();
                    xy[1] = tgxy.getY();
                    return xy;
                }
                break;
            default:
                break;
        }
        return xy;
    }

    /**********************
     * 
     */
    private final static double pi = 3.14159265358979324;
    private final static double a = 6378245.0;
    private final static double ee = 0.00669342162296594323;

    /***
     * *
     * Description:<br/>
     * @param wgLat
     * @param wgLon
     * @return
     * @version V1.0
     */
    private static double[] transform(double wgLat, double wgLon) {
        double[] latlng = new double[2];
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return latlng;
        }

        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);

        latlng[0] = wgLat + dLat;
        latlng[1] = wgLon + dLon;
        return latlng;
    }

    /***
     * *
     * Description:是否超出中国范围<br/>
     * @param lat
     * @param lon
     * @return
     * @version V1.0
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        return lat < 0.8293 || lat > 55.8271;
    }

    /***
     * *
     * Description:转换纬度为高德坐标<br/>
     * @param x
     * @param y
     * @return
     * @version V1.0
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /***
     * *
     * Description:转换经度为高德坐标<br/>
     * @param x
     * @param y
     * @return
     * @version V1.0
     */
    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}
