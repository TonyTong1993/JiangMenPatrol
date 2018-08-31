package com.zzz.ecity.android.applibrary.utils;

public class MercatorUtil {
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
	public static double calculateLength(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
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
    public static double[] lonLat2Mercator(double lon,double lat){
        if(Math.abs(lon) > 180 || Math.abs(lat)>90) {
            throw null;
        }
        
        double pi = 3.14159265358979324;
        double[] xy = new double[2];
        double x = lon *20037508.342789/180;
    
        double y = Math.log(Math.tan((90+lat)*pi/360))/(pi/180);
    
        y = y *20037508.34789/180;
        xy[0] = x;
        xy[1] = y;
        return xy;
    }
}
