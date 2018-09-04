package com.ecity.cswatersupply.utils;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;

/**
 * 
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
public class GeometryUtil {

	/**
	 * 外接矩形
	 * 
	 * @param multipathGeometry
	 * @return
	 */
	public static Envelope getMultiPathEnvelope(Geometry multipathGeometry) {

		return getMultiPathEnvelope(multipathGeometry, 0);
	}

	/**
	 * 扩大的外接矩形
	 * 
	 * @param multipathGeometry
	 * @param expansion
	 *            扩大
	 * @return
	 */
	public static Envelope getMultiPathEnvelope(Geometry multipathGeometry,
			double expansion) {
		Envelope envelope = new Envelope();
		try {
			ArrayList<Point> points = getPoints((MultiPath) multipathGeometry);
			double xmin = points.get(0).getX();
			double ymin = points.get(0).getY();
			double xmax = points.get(0).getX();
			double ymax = points.get(0).getY();

			for (int i = 0; i < points.size(); i++) {
				if (xmin > points.get(i).getX()) {
					xmin = points.get(i).getX();
				}

				if (ymin > points.get(i).getY()) {
					ymin = points.get(i).getY();
				}

				if (xmax < points.get(i).getX()) {
					xmax = points.get(i).getX();
				}

				if (ymax < points.get(i).getY()) {
					ymax = points.get(i).getY();
				}
			}

			envelope.setXMin(xmin - expansion);
			envelope.setYMin(ymin - expansion);
			envelope.setXMax(xmax + expansion);
			envelope.setYMax(ymax + expansion);

		} catch (Exception e) {
			envelope = null;
		}
		return envelope;
	}

	public static Point GetGeometryCenter(Geometry geometry) {
		Point center = null;
		try {
			if (geometry instanceof Point) {
				return (Point) geometry;
			} else if (geometry instanceof Polyline) {
				ArrayList<Point> points = getPoints((MultiPath) geometry);
				if (points.size() % 2 != 0) {
					double x = points.get(Math.round(points.size() / 2)).getX();
					double y = points.get(Math.round(points.size() / 2)).getY();
					center = new Point(x, y);
					// center.setX(points.get(Math.round(points.size() /
					// 2)).getX());
					// center.setY(points.get(Math.round(points.size() /
					// 2)).getY());
				} else {
					double x = (points.get(Math.round(points.size() / 2 - 1))
							.getX() + points.get(Math.round(points.size() / 2))
							.getX()) / 2.0;
					double y = (points.get(Math.round(points.size() / 2 - 1))
							.getY() + points.get(Math.round(points.size() / 2))
							.getY()) / 2.0;
					center = new Point(x, y);
					// center.setX((points.get(Math.round(points.size() / 2 -
					// 1)).getX() + points.get(Math.round(points.size() /
					// 2)).getX()) / 2.0);
					// center.setY((points.get(Math.round(points.size() / 2 -
					// 1)).getY() + points.get(Math.round(points.size() /
					// 2)).getY()) / 2.0);
				}
				return center;
			} else if (geometry instanceof Polygon) {
				double x = 0;
				double y = 0;
				for (int i = 0; i < ((Polygon) geometry).getPointCount(); i++) {
					x += ((Polygon) geometry).getPoint(i).getX();
					y += ((Polygon) geometry).getPoint(i).getY();
				}
				double px = 0;
				double py = 0;
				px = x * 1.0 / (((Polygon) geometry).getPointCount() * 1.0);
				py = y * 1.0 / (((Polygon) geometry).getPointCount() * 1.0);
				center = new Point(px, py);
				return center;
			} else {
				return new Point(0, 0);
			}
		} catch (Exception e) {
			return new Point(0, 0);
		}
	}

	public static ArrayList<Point> getPoints(MultiPath multiPath) {
		ArrayList<Point> points = new ArrayList<Point>();
		try {
			if (multiPath instanceof Polyline) {
				for (int i = 0; i < multiPath.getPointCount(); i++) {
					points.add(multiPath.getPoint(i));
				}
			} else {
				for (int i = 0; i < multiPath.getPointCount(); i++) {
					points.add(multiPath.getPoint(i));
				}
				points.add(multiPath.getPoint(0));
			}
		} catch (Exception e) {
			points = null;
		}
		return points;
	}

	public static String getPointsString(Geometry geometry) {
		String pointsString = "";
		try {
			if (geometry instanceof Point) {
				pointsString = ((Point) geometry).getX() + ","
						+ ((Point) geometry).getY();
			} else if (geometry instanceof Envelope) {
				pointsString = ((Envelope) geometry).getXMin() + ","
						+ ((Envelope) geometry).getYMin() + ","
						+ ((Envelope) geometry).getXMax() + ","
						+ ((Envelope) geometry).getYMax();
			} else if (geometry instanceof Polygon) {
				for (int i = 0; i < ((Polygon) geometry).getPointCount(); i++) {
					pointsString += ((Polygon) geometry).getPoint(i).getX()
							+ "," + ((Polygon) geometry).getPoint(i).getY()
							+ ",";
				}
				pointsString += ((Polygon) geometry).getPoint(0).getX() + ","
						+ ((Polygon) geometry).getPoint(0).getY();
			}
		} catch (Exception e) {
			pointsString = "";
		}

		return pointsString;
	}

	public static boolean IsPolygonContainPoint(Point point, Polygon polygon) {
		int nCross = 0;
		for (int i = 0; i < polygon.getPointCount(); i++) {
			Point p1 = polygon.getPoint(i);
			Point p2 = polygon.getPoint((i + 1) % polygon.getPointCount());

			if (p1.getY() == p2.getY()) {
				continue;
			}
			if (point.getY() < Math.min(p1.getY(), p2.getY())) {
				continue;
			}
			if (point.getY() >= Math.max(p1.getY(), p2.getY())) {
				continue;
			}
			double x = (point.getY() - p1.getY()) * (p2.getX() - p1.getX())
					/ (p2.getY() - p1.getY()) + p1.getX();
			if (x > point.getX()) {
				nCross++;
			}
		}
		return (nCross % 2 == 1);
	}

	public static boolean IsEnvelopeContainPoint(Point point, Envelope envelope) {

		return point.getX() > envelope.getXMin()
				&& point.getX() < envelope.getXMax()
				&& point.getY() > envelope.getYMin()
				&& point.getY() < envelope.getYMax();
	}
/***
 * 
 * @param point 目标点
 * @param circleCenter 圆心
 * @param r 半径
 * @return
 */
	public static boolean IsCircleContainPoint(Point point, Point circleCenter,double r) {
		if(point == null || circleCenter == null)
			return false;
		double d = distance(point,circleCenter);
		return d <= r;
	}
	
	public static String calcTowPointsDistance(double x1, double y1, double x2,
			double y2) {
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

	public static double distance(Point point1, Point point2) {
		return distance(point1.getX(), point1.getY(), point2.getX(),
				point2.getY());

	}

	public static double distanceToCloselyPoint(Point point, Polygon polygon) {
		if (IsPolygonContainPoint(point, polygon)) {
			return 0;
		} else {
			double distance = Double.MAX_VALUE;
			for (int i = 0; i < polygon.getPointCount(); i++) {
				if (i == 0) {
					distance = distance(point, polygon.getPoint(i));
				} else {
					double d = distance(point, polygon.getPoint(i));
					distance = Math.min(distance, d);
				}
			}
			return distance;
		}

	}

	public static Boolean IsInEnvelope(Envelope smallEnvelope,
			Envelope largeEnvelope) {
		// return smallEnvelope.getXMin() >= largeEnvelope.getXMin() &&
		// smallEnvelope.getXMax() <= largeEnvelope.getXMax()
		// && smallEnvelope.getYMin() >= largeEnvelope.getYMin() &&
		// smallEnvelope.getYMax() <= largeEnvelope.getYMax();
		if (smallEnvelope == null || largeEnvelope == null) {
			return false;
		}
		return smallEnvelope.getXMin() >= largeEnvelope.getXMin()
				&& smallEnvelope.getXMax() <= largeEnvelope.getXMax()
				&& smallEnvelope.getYMin() >= largeEnvelope.getYMin();
	}
	
    /** 
     * 获取圆的图形对象 
     * 
     * @param center 
     * @param radius 
     * @return 
     */  
    public static Polygon getCircle(Point center, double radius) {  
        Polygon polygon = new Polygon();  
        getCircle(center, radius, polygon);  
        return polygon;  
    }  
  
    private static void getCircle(Point center, double radius, Polygon circle) {  
        circle.setEmpty();  
        Point[] points = getPoints(center, radius);  
        circle.startPath(points[0]);  
        for (int i = 1; i < points.length; i++)  
            circle.lineTo(points[i]);  
    }  
  
    private static Point[] getPoints(Point center, double radius) {  
        Point[] points = new Point[50];  
        double sin;  
        double cos;  
        double x;  
        double y;  
        for (double i = 0; i < 50; i++) {  
            sin = Math.sin(Math.PI * 2 * i / 50);  
            cos = Math.cos(Math.PI * 2 * i / 50);  
            x = center.getX() + radius * sin;  
            y = center.getY() + radius * cos;  
            points[(int) i] = new Point(x, y);  
        }  
        return points;  
    }  
    
	private static final double EARTH_RADIUS = 6378137;
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double calculateLength(double lng1, double lat1, double lng2, double lat2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000.0;
	   return s;
	}
	
	
	public static double distanceLonLat(Point point1, Point point2) {
		if(null == point1 || null == point2)
			return 0;
		try {
			return calculateLength(point1.getX(), point1.getY(),point2.getX(), point2.getY());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static double distanceMercator(Point point1, Point point2) {
		double p1[];
		double p2[];
		try {
			
			p1 = mercator2lonLat(point1.getX(), point1.getY());
			p2 = mercator2lonLat(point2.getX(), point2.getY());
			
			return calculateLength(p1[0],p1[1],p2[0],p2[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public static double calculateLength(Polyline pln,boolean isMercator)
	{
		if(null == pln || pln.getPointCount() <2)
			return 0;

		Polyline src = (Polyline) pln.copy();
		Polyline goalGeo = (Polyline) pln.copy();
		
		if(isMercator)
			goalGeo = (Polyline) transformMercator2LonLat(src);
		
		double len = 0;
		int pathId = 0;
		int lastPathid = 0;
		for (int i = 0; i < goalGeo.getPointCount()-1; i++) {
			try {
				pathId = goalGeo.getPathIndexFromPointIndex(i+1);
				if(lastPathid != pathId)
				{
					lastPathid = pathId;
				}
				else
				{
					Point p1 = goalGeo.getPoint(i);
					Point p2 = goalGeo.getPoint(i+1);
					len += distanceLonLat(p1,p2);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return len+0.5;
	}
	
	/****
	 * 经纬度转墨卡托
	 * @param lon
	 * @param lat
	 * @return
	 * @throws Exception 
	 */
	public static double[] lonLat2Mercator(double lon,double lat) throws Exception
	{
		if(Math.abs(lon) > 180 || Math.abs(lat)>90)
			throw new Exception("无效的经纬度坐标!");
		
		double pi = 3.14159265358979324;
		double[] xy = new double[2];
		double x = lon *20037508.342789/180;
	
		double y = Math.log(Math.tan((90+lat)*pi/360))/(pi/180);
	
		y = y *20037508.34789/180;
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
	public static double[] mercator2lonLat(double mercatorX,double mercatorY)
	{
		return CoordTransfer.transToLatlon(mercatorX, mercatorY);
	}
	/***
	 * 将经纬度类型的几何数据转为墨卡托类型的几何数据
	 * @param geo
	 * @return
	 */
	public static Geometry transformLonLat2Mercator(Geometry geo)
	{
		if(null == geo)
			return null;
		Point pnt = null;
		if(geo instanceof Point)
		{
			double[] xy;
			try {
				xy = lonLat2Mercator(((Point)geo).getX(),((Point)geo).getY());
				pnt = new Point(xy[0],xy[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return pnt;
		}
		else if(geo instanceof Polyline)
		{
			Polyline src = (Polyline) geo;
			Polyline goalGeo = (Polyline) src.copy();
			
			for (int i = 0; i < src.getPointCount(); i++) {
				double[] xy;
				try {
					xy = lonLat2Mercator(src.getPoint(i).getX(),src.getPoint(i).getY());
					goalGeo.setPoint(i, new Point(xy[0], xy[1]));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return goalGeo;
		}
		else if(geo instanceof Polygon)
		{
			Polygon src = (Polygon) geo;
			Polygon goalGeo = (Polygon) geo.copy();
			for (int i = 0; i < src.getPointCount(); i++) {
				
				double[] xy;
				try {
					xy = lonLat2Mercator(src.getPoint(i).getX(),src.getPoint(i).getY());
					goalGeo.setPoint(i, new Point(xy[0], xy[1]));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return goalGeo;
		}
		return null;

	}
	/***
	 * 将墨卡托类型的几何数据转为经纬度类型的几何数据
	 * @param geo
	 * @return
	 */
	public static Geometry transformMercator2LonLat(Geometry geo)
	{
		if(null == geo)
			return null;
		if(geo instanceof Point)
		{
			double[] xy = mercator2lonLat(((Point)geo).getX(),((Point)geo).getY());
			Point pnt = new Point(xy[0],xy[1]);
			return pnt;
		}
		else if(geo instanceof Polyline)
		{
			Polyline src = (Polyline) geo;
			Polyline goalGeo = (Polyline) src.copy();
			
			for (int i = 0; i < src.getPointCount(); i++) {
				double[] xy = mercator2lonLat(src.getPoint(i).getX(),src.getPoint(i).getY());
				goalGeo.setPoint(i, new Point(xy[0], xy[1]));
			}
			return goalGeo;
		}
		else if(geo instanceof Polygon)
		{
			Polygon src = (Polygon) geo;
			Polygon goalGeo = (Polygon) geo.copy();
			for (int i = 0; i < src.getPointCount(); i++) {
				
				double[] xy = mercator2lonLat(src.getPoint(i).getX(),src.getPoint(i).getY());
				goalGeo.setPoint(i, new Point(xy[0], xy[1]));
			}
			return goalGeo;
		}
		return null;
	}
	
}
