package com.ecity.cswatersupply.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.ClassBreak;
import com.esri.core.renderer.ClassBreaksRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * 
 * <b>包名：<br/>
 * </b>com.ecity.patrol.tools<br/>
 * <b>文件名：<br/>
 * </b>GeometryUtil.java<br/>
 * <b>版本信息：<br/>
 * </b><br/>
 * <b>创建日期：<br/>
 * </b>2013-12-11-下午12:50:15<br/>
 * <b>类名称：<br/>
 * </b>GeometryUtil<br/>
 * <b>类描述：<br/>
 * </b>几何图形处理<br/>
 * <b>创建人：<br/>
 * </b>ZiZhengzhuan<br/>
 * <b>修改人：<br/>
 * </b>ZiZhengzhuan<br/>
 * <b>修改时间：<br/>
 * </b>2013-12-11 下午12:50:15<br/>
 * <b>修改备注：<br/>
 * </b>无<br/>
 * <b>Copyright (c)</b> 2013版权所有<br/>
 */
public class GeometryUtils {

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

	public static void drawColorfulPoint(MapView mapView, Geometry geometry,
			Integer defaultColor) {
		try {
			ClassBreaksRenderer renderer = new ClassBreaksRenderer();
			renderer.setField("class");
			renderer.setMinValue(0);
			Integer[] colors = new Integer[] {
					Color.argb(0xff, 0xff, 0x00, 0x00),// 红
					Color.argb(0xff, 0xff, 0xf7, 0xf0),// 橙
					Color.argb(0xff, 0xff, 0xff, 0x00),// 黄
					Color.argb(0xff, 0x00, 0xff, 0x00),// 绿
					Color.argb(0xff, 0x00, 0xff, 0xff),// 青
					Color.argb(0xff, 0x00, 0x00, 0xff),// 蓝
					Color.argb(0xff, 0xff, 0x00, 0xff) // 紫
			};

			SimpleMarkerSymbol[] symbols = new SimpleMarkerSymbol[7];
			for (int i = 0; i < colors.length; i++) {
				ClassBreak simpleMarkerClassBreak = new ClassBreak();
				simpleMarkerClassBreak.setClassMaxValue(i);
				SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(
						colors[i], 10, SimpleMarkerSymbol.STYLE.CIRCLE);
				simpleMarkerClassBreak.setSymbol(simpleMarkerSymbol);
				symbols[i] = simpleMarkerSymbol;
				renderer.addClassBreak(simpleMarkerClassBreak);
			}
			if (geometry instanceof Point) {
//				LayerTool.getSketchLayer(mapView).setRenderer(renderer);
				Graphic graphic = null;
				Map<String, Object> att = new Hashtable<String, Object>();
				att.put("class", 0);
				if (defaultColor != null) {
					graphic = new Graphic(geometry, new SimpleMarkerSymbol(
							defaultColor, 10, SimpleMarkerSymbol.STYLE.CIRCLE),
							att, 0);
				} else {
					graphic = new Graphic(geometry, symbols[0], att, 0);
				}
				LayerTool.getGraphicsLayer(mapView).addGraphic(graphic);
			} else if (geometry instanceof Polyline) {
				ClassBreak simpleLineClassBreak = new ClassBreak();
				simpleLineClassBreak.setClassMaxValue(8);
				SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(
						Color.BLACK, 3);
				simpleLineClassBreak.setSymbol(simpleLineSymbol);
				renderer.addClassBreak(simpleLineClassBreak);
				LayerTool.getGraphicsLayer(mapView).setRenderer(renderer);
				Map<String, Object> att = new Hashtable<String, Object>();
				att.put("class", 8);
				Graphic polylineGraphic = new Graphic(geometry,
						simpleLineSymbol, att, 0);
				LayerTool.getGraphicsLayer(mapView).addGraphic(polylineGraphic);
				ArrayList<Point> points = getPoints((MultiPath) geometry);
				for (int i = 0; i < points.size(); i++) {
					Map<String, Object> tatt = new Hashtable<String, Object>();
					tatt.put("class", 0);
					Graphic graphic = new Graphic(geometry, symbols[i % 7],
							tatt, 0);
					LayerTool.getGraphicsLayer(mapView).addGraphic(graphic);
				}
			} else if (geometry instanceof Polygon) {
				ClassBreak simpleFillClassBreak = new ClassBreak();
				simpleFillClassBreak.setClassMaxValue(9);
				SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
						Color.GREEN);
				simpleFillSymbol.setAlpha(50);
				simpleFillSymbol
						.setOutline(new SimpleLineSymbol(Color.BLACK, 1));
				simpleFillClassBreak.setSymbol(simpleFillSymbol);
				renderer.addClassBreak(simpleFillClassBreak);
				LayerTool.getGraphicsLayer(mapView).setRenderer(renderer);
				Map<String, Object> att = new Hashtable<String, Object>();
				att.put("class", 9);
				Graphic polygonGraphic = new Graphic(geometry,
						simpleFillSymbol, att, 0);
				LayerTool.getGraphicsLayer(mapView).addGraphic(polygonGraphic);
				ArrayList<Point> points = getPoints((MultiPath) geometry);
				for (int i = 0; i < points.size() - 1; i++) {
					Map<String, Object> tatt = new Hashtable<String, Object>();
					tatt.put("class", 0);
					Graphic graphic = new Graphic(geometry, symbols[i % 7],
							tatt, 0);
					LayerTool.getGraphicsLayer(mapView).addGraphic(graphic);
				}
			}
			mapView.postInvalidate();
		} catch (Exception e) {
		}
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

	public static boolean IsGraphicLayerContainGraphic(Graphic graphic,
			GraphicsLayer graphicsLayer) {
		if (graphicsLayer == null || graphic == null)
			return false;
		for (int index : graphicsLayer.getGraphicIDs()) {
			Graphic g = graphicsLayer.getGraphic(index);

			if (g.equals(graphic))
				return true;
		}
		return false;
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
		if(null==point1||null==point2){
			return 0;
		}
		return distance(point1.getX(), point1.getY(), point2.getX(),
				point2.getY());

	}

	public static double distance(Point point, Polygon polygon) {
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

	public static Point getNearestPoint(Point point, Point start, Point end) {
		double x0 = point.getX();
		double y0 = point.getY();
		double x1 = start.getX();
		double y1 = start.getY();
		double x2 = end.getX();
		double y2 = end.getY();

		Point interPoint = new Point();
		// double space = 0;
		double a, b, c;
		a = distance(x1, y1, x2, y2);// 线段的长度
		b = distance(x1, y1, x0, y0);// (x1,y1)到点的距离
		c = distance(x2, y2, x0, y0);// (x2,y2)到点的距离
		if (c + b == a) {// 点在线段上

			if (interPoint == null)
				interPoint = new Point(x0, y0);
			else {
				interPoint.setXY(x0, y0);
			}
			return interPoint;
		}
		if (a <= 0.000001) {// 不是线段，是一个点

			if (interPoint == null)
				interPoint = new Point(x1, y1);
			else {
				interPoint.setXY(x1, y1);
			}
			return interPoint;
		}
		if (c * c >= a * a + b * b) { // 组成直角三角形或钝角三角形，(x1,y1)为直角或钝角

			if (interPoint == null)
				interPoint = new Point(x1, y1);
			else {
				interPoint.setXY(x1, y1);
			}
			return interPoint;
		}
		if (b * b >= a * a + c * c) {// 组成直角三角形或钝角三角形，(x2,y2)为直角或钝角

			if (interPoint == null)
				interPoint = new Point(x2, y2);
			else {
				interPoint.setXY(x2, y2);
			}
			return interPoint;
		}
		// 组成锐角三角形，则求三角形的高
		double p = (a + b + c) / 2;// 半周长
		double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积

		// 计算交点坐标
		double interX = ((x2 - x1) * (y1 - y0) * (y1 - y2)
				+ Math.pow(x2 - x1, 2) * x0 + Math.pow(y1 - y2, 2) * x1)
				/ (Math.pow(x2 - x1, 2) + Math.pow(y1 - y2, 2));
		double interY = y0 + (x2 - x1) * (interX - x0) / (y1 - y2);
		if (interPoint == null)
			interPoint = new Point(interX, interY);
		else {
			interPoint.setXY(interX, interY);
		}
		return interPoint;

	}
	
	

}
