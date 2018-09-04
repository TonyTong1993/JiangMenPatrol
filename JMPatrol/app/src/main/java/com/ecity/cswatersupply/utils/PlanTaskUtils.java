package com.ecity.cswatersupply.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;

public class PlanTaskUtils {

    public static Geometry buildGeometryFromJSON(String json) {
        if (null == json) {
            return null;
        }

        JsonFactory jpf = new JsonFactory();
        JsonParser jsonParser = null;
        try {
            jsonParser = jpf.createJsonParser(json);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Geometry geo = GeometryEngine.jsonToGeometry(jsonParser).getGeometry();
        return geo;
    }

    public static String buildGeoJSONFromGeometry(SpatialReference spatialReference, Geometry geometry) {
        String geoStr = "";
        if (null == geometry || null == spatialReference) {
            return geoStr;
        }
        try {
            geoStr = GeometryEngine.geometryToJson(spatialReference, geometry);
        } catch (Exception e) {
        }
        return geoStr;
    }

    /***
     * 字符串构建线
     * 
     * @param strPolyline
     * @return
     */
    public static Polyline buildPolylineFromString(String strPolyline) {
        Polyline polyline = new Polyline();
        boolean isNewPath = true;

        if (strPolyline != null && !strPolyline.equals("")) {

            String[] pathStrings = strPolyline.split("\\|");
            Hashtable<String, String> pointTable = new Hashtable<String, String>();
            for (int ip = 0; ip < pathStrings.length; ip++) {
                String tmpLineStr = pathStrings[ip];
                String[] lineStrings = tmpLineStr.split(";");
                isNewPath = true;

                for (int i = 0; i < lineStrings.length; i++) {
                    if (pointTable.containsKey(lineStrings[i])) {
                        continue;
                    }
                    pointTable.put(lineStrings[i], "");

                    String[] xy = lineStrings[i].split(",");

                    String x = "0";
                    String y = "0";

                    if (null != xy && xy.length == 2) {
                        x = xy[0];
                        y = xy[1];
                    } else
                        continue;

                    if (x.startsWith("\"") && x.endsWith("\"") && x.length() > 1) {
                        x = x.substring(1, x.length() - 1);
                    }

                    if (y.startsWith("\"") && y.endsWith("\"") && y.length() > 1) {
                        y = y.substring(1, y.length() - 1);
                    }

                    isNewPath = isNewPath || i == 0;

                    if (isNewPath) {
                        polyline.startPath(Double.parseDouble(x), Double.parseDouble(y));
                        isNewPath = false;
                    } else {
                        polyline.lineTo(Double.parseDouble(x), Double.parseDouble(y));
                    }
                }
            }
        }
        return polyline;
    }

    public static boolean isValidPosition(GPSPositionBean bean) {
        if (bean == null)
            return false;
        return !(bean.getx() < 1 || bean.gety() < 1);

    }

    /***
     * GPS坐标串构建线
     * 
     * @param strPolyline
     * @return
     */
    public static Polyline buildPolylineFromGPSPositionListForXY(List<GPSPositionBean> gpsPositions) {
        Polyline polyline = new Polyline();
        if (gpsPositions == null || gpsPositions.size() < 2)
            return polyline;

        double distance = 0;
        long timeOffset = 0;

        Point thisPnt = new Point();
        Point nextPnt = new Point();

        GPSPositionBean thisBean = new GPSPositionBean();
        GPSPositionBean nextBean = new GPSPositionBean();

        /*
         * 直接添加MuiltPath 模式 Polyline tmpMuiltPath = new Polyline();
         */
        Hashtable<String, String> pointTable = new Hashtable<String, String>();
        boolean isNewPath = true;
        try {
            for (int i = 0; i < gpsPositions.size(); i++) {
                if (i == 0) {
                    thisBean = gpsPositions.get(0);
                    nextBean = gpsPositions.get(1);
                    thisPnt = new Point(thisBean.getx(), thisBean.gety());
                    nextPnt = new Point(nextBean.getx(), nextBean.gety());
                } else {
                    thisBean = gpsPositions.get(i - 1);
                    nextBean = gpsPositions.get(i);
                    thisPnt = new Point(nextPnt.getX(), nextPnt.getY());
                    nextPnt = new Point(nextBean.getx(), nextBean.gety());
                }

                // 原点坐标
                if (thisPnt.getX() < 1 || thisPnt.getY() < 1) {
                    continue;
                }
                // 原点坐标
                if (nextPnt.getX() < 1 || nextPnt.getY() < 1) {
                    isNewPath = true;
                    continue;
                }

                String tmpXY = "" + nextPnt.getX() + "_" + nextPnt.getY();
                try {
                    if (pointTable.containsKey(tmpXY)) {
                        continue;
                    }
                    pointTable.put(tmpXY, "");
                } catch (Exception e) {
                }

                timeOffset = Math.abs(DateUtil.changeStringToLong(thisBean.getTime()) - DateUtil.changeStringToLong(nextBean.getTime()));
                distance = GeometryUtil.distance(thisPnt, nextPnt);

                int facter = 500;// 步行
                int facterTime = 1000 * 60 * 10;// 步行
                if ((thisBean.getSpeed() + nextBean.getSpeed()) / 2 > 3 && (thisBean.getSpeed() + nextBean.getSpeed()) / 2 < 6) {
                    facter = 1000;// 高速自行车，低速电动车
                    facterTime = 1000 * 60 * 10;
                } else if ((thisBean.getSpeed() + nextBean.getSpeed()) / 2 > 6 && (thisBean.getSpeed() + nextBean.getSpeed()) / 2 < 10) {
                    facter = 1500;// 高速电动车，摩托车
                    facterTime = 1000 * 60 * 5;
                } else if ((thisBean.getSpeed() + nextBean.getSpeed()) / 2 > 10) {
                    facter = 5000;// 汽车
                    facterTime = 1000 * 60 * 2;
                }

                if (distance > facter || timeOffset > facterTime) {
                    isNewPath = true;
                }

                else {
                    if (isNewPath) {
                        polyline.startPath(thisPnt);
                        if (polyline.getPathCount() < 1)
                            polyline.lineTo(nextPnt);
                        isNewPath = false;
                    } else
                        polyline.lineTo(nextPnt);
                }
            }
            /*
             * 直接添加MuiltPath 模式 if(distance>facter || timeOffset> 1000*60*5) {
             * if(tmpMuiltPath!=null && tmpMuiltPath.getPointCount()>1)
             * polyline.add(tmpMuiltPath, false); isNewPath = true; tmpMuiltPath
             * = null; continue; }
             * 
             * if(isNewPath) { tmpMuiltPath = new Polyline();
             * tmpMuiltPath.startPath(thisPnt); tmpMuiltPath.lineTo(nextPnt);
             * isNewPath = false; } else { if(null == tmpMuiltPath ||
             * tmpMuiltPath.getPointCount()<2) { tmpMuiltPath = new Polyline();
             * tmpMuiltPath.startPath(thisPnt); } tmpMuiltPath.lineTo(nextPnt);
             * } } if(tmpMuiltPath!=null && tmpMuiltPath.getPointCount()>1)
             * polyline.add(tmpMuiltPath, false); tmpMuiltPath = null;
             */
        } catch (Exception e) {
        }
        return polyline;
    }

    /***
     * GPS坐标串构建线
     * 
     * @param strPolyline
     * @return
     */
    public static Polyline buildPolylineFromGPSPositionListForLonLat(List<GPSPositionBean> gpsPositions) {
        Polyline polyline = new Polyline();
        if (gpsPositions == null || gpsPositions.size() < 2)
            return polyline;

        double distance = 0;
        long timeOffset = 0;

        Point thisPnt = new Point();
        Point nextPnt = new Point();

        GPSPositionBean thisBean = new GPSPositionBean();
        GPSPositionBean nextBean = new GPSPositionBean();

        /*
         * 直接添加MuiltPath 模式 Polyline tmpMuiltPath = new Polyline();
         */
        Hashtable<String, String> pointTable = new Hashtable<String, String>();
        boolean isNewPath = true;
        try {
            for (int i = 0; i < gpsPositions.size(); i++) {
                if (i == 0) {
                    thisBean = gpsPositions.get(0);
                    nextBean = gpsPositions.get(1);
                    thisPnt = new Point(thisBean.getlon(), thisBean.getlat());
                    nextPnt = new Point(nextBean.getlon(), nextBean.getlat());
                } else {
                    thisBean = gpsPositions.get(i - 1);
                    nextBean = gpsPositions.get(i);
                    thisPnt = new Point(nextPnt.getX(), nextPnt.getY());
                    nextPnt = new Point(nextBean.getlon(), nextBean.getlat());
                }

                // 原点坐标
                if (thisPnt.getX() < 1 || thisPnt.getY() < 1) {
                    continue;
                }
                // 原点坐标
                if (nextPnt.getX() < 1 || nextPnt.getY() < 1) {
                    isNewPath = true;
                    continue;
                }

                String tmpXY = "" + nextPnt.getX() + "_" + nextPnt.getY();
                try {
                    if (pointTable.containsKey(tmpXY)) {
                        continue;
                    }
                    pointTable.put(tmpXY, "");
                } catch (Exception e) {
                }
                timeOffset = Math.abs(DateUtil.changeStringToLong(thisBean.getTime()) - DateUtil.changeStringToLong(nextBean.getTime()));
                distance = GeometryUtil.distanceLonLat(thisPnt, nextPnt);

                int facter = 500;// 步行
                int facterTime = 1000 * 60 * 10;// 步行
                if ((thisBean.getSpeed() + nextBean.getSpeed()) / 2 > 3 && (thisBean.getSpeed() + nextBean.getSpeed()) / 2 < 6) {
                    facter = 1000;// 高速自行车，低速电动车
                    facterTime = 1000 * 60 * 10;
                } else if ((thisBean.getSpeed() + nextBean.getSpeed()) / 2 > 6 && (thisBean.getSpeed() + nextBean.getSpeed()) / 2 < 10) {
                    facter = 1500;// 高速电动车，摩托车
                    facterTime = 1000 * 60 * 5;
                } else if ((thisBean.getSpeed() + nextBean.getSpeed()) / 2 > 10) {
                    facter = 5000;// 汽车
                    facterTime = 1000 * 60 * 2;
                }

                if (distance > facter || timeOffset > facterTime) {
                    isNewPath = true;
                }

                else {
                    if (isNewPath) {
                        polyline.startPath(thisPnt);
                        if (polyline.getPathCount() < 1)
                            polyline.lineTo(nextPnt);
                        isNewPath = false;
                    } else
                        polyline.lineTo(nextPnt);
                }
            }
            /*
             * 直接添加MuiltPath 模式 if(distance>facter || timeOffset> 1000*60*5) {
             * if(tmpMuiltPath!=null && tmpMuiltPath.getPointCount()>1)
             * polyline.add(tmpMuiltPath, false); isNewPath = true; tmpMuiltPath
             * = null; continue; }
             * 
             * if(isNewPath) { tmpMuiltPath = new Polyline();
             * tmpMuiltPath.startPath(thisPnt); tmpMuiltPath.lineTo(nextPnt);
             * isNewPath = false; } else { if(null == tmpMuiltPath ||
             * tmpMuiltPath.getPointCount()<2) { tmpMuiltPath = new Polyline();
             * tmpMuiltPath.startPath(thisPnt); } tmpMuiltPath.lineTo(nextPnt);
             * } } if(tmpMuiltPath!=null && tmpMuiltPath.getPointCount()>1)
             * polyline.add(tmpMuiltPath, false); tmpMuiltPath = null;
             */
        } catch (Exception e) {
        }
        return polyline;
    }

    /***
     * 字符串构建多边形
     * 
     * @param strPolygon
     * @return
     */
    public static List<Z3PlanTaskPointPart> buildTaskPointsFromString(String PATHPOINTS) {
        List<Z3PlanTaskPointPart> taskPntList = new ArrayList<Z3PlanTaskPointPart>();
        if (PATHPOINTS != null && !PATHPOINTS.equals("")) {
            String[] pntStrings = PATHPOINTS.split("\\|");
            for (int i = 0; i < pntStrings.length; i++) {
                String[] xy = pntStrings[i].split(",");
                if (xy != null && xy.length == 2) {
                    String x = "0";
                    String y = "0";

                    x = xy[0];
                    y = xy[1];

                    if (x.startsWith("\"") && x.endsWith("\"") && x.length() > 1) {
                        x = x.substring(1, x.length() - 1);
                    }

                    if (y.startsWith("\"") && y.endsWith("\"") && y.length() > 1) {
                        y = y.substring(1, y.length() - 1);
                    }

                    Z3PlanTaskPointPart tPnt = new Z3PlanTaskPointPart();
                    tPnt.setArrive(false);
                    taskPntList.add(tPnt);
                }
            }
        }
        return taskPntList;
    }

    /***
     * 字符串构建多边形
     * 
     * @param strPolygon
     * @return
     */
    public static Polygon buildPolygonFromString(String strPolygon) {
        Polygon polygon = new Polygon();
        if (strPolygon != null && !strPolygon.equals("")&&!strPolygon.equals("null")) {
            String[] areaStrings = strPolygon.split(";");
            for (int i = 0; i < areaStrings.length; i++) {
                String[] xy = areaStrings[i].split(",");
                String x = "0";
                String y = "0";

                if (null != xy && xy.length == 2) {
                    x = xy[0];
                    y = xy[1];
                } else
                    continue;

                if (x.startsWith("\"") && x.endsWith("\"") && x.length() > 1) {
                    x = x.substring(1, x.length() - 1);
                }

                if (y.startsWith("\"") && y.endsWith("\"") && y.length() > 1) {
                    y = y.substring(1, y.length() - 1);
                }

                if (i == 0) {
                    polygon.startPath(Double.parseDouble(x), Double.parseDouble(y));
                } else {
                    polygon.lineTo(Double.parseDouble(x), Double.parseDouble(y));
                }
            }
            polygon.closePathWithLine();
        }
        return polygon;
    }

    public static Polygon getBufferForPolyline(Polyline polyline, SpatialReference sp, int buffer) {
        Polygon polygon = new Polygon();

        if (polyline == null || sp == null || polyline.isEmpty())
            return polygon;
        try {
            polygon = GeometryEngine.buffer(polyline, sp, buffer, null);
        } catch (Exception e) {
        }

        return polygon;
    }

    /***
     * 点的缓冲区
     * 
     * @param point
     * @param sp
     * @param buffer
     * @return
     */
    public static Polygon getBufferForPoint(Point point, SpatialReference sp, int buffer) {
        if (point == null || sp == null)
            return null;

        Polygon polygon = new Polygon();
        try {
            polygon = GeometryEngine.buffer(point, sp, buffer, null);
        } catch (Exception e) {
        }

        return polygon;
    }

    /***
     * 判断线与点的缓冲区是否相交
     */
    public static boolean isIntersect(Polygon polygon, Polyline polyline, SpatialReference sp) {
        if (polygon == null || polyline == null || sp == null) {
            return false;
        }

        if (polygon.isEmpty() || polyline.isEmpty()) {
            return false;
        }
        Geometry result = GeometryEngine.intersect(polygon, polyline, sp);
        if (null == result) {
            return false;
        }
        return !result.isEmpty();
    }

    /***
     * 判断线与点的缓冲区是否相交
     */
    public static boolean isPointInPolygon(Polygon polygon, Point point, SpatialReference sp) {
        if (polygon == null || point == null || sp == null)
            return false;

        if (polygon.isEmpty() || point.isEmpty())
            return false;

        return GeometryEngine.contains(polygon, point, sp);
    }

    /****
     * 计算有效路段
     * 
     * @param polygon
     * @param polyline
     * @param sp
     * @return
     */
    public static Polyline getEffectivePolyline(Polygon polygon, Polyline polyline, SpatialReference sp) {
        if (polygon == null || polyline == null || sp == null || polygon.isEmpty() || polyline.isEmpty())
            return new Polyline();

        Geometry result = GeometryEngine.intersect(polygon, polyline, sp);
        if (null == result)
            return new Polyline();
        if (result instanceof Polyline)
            return (Polyline) result;
        return new Polyline();
    }

    /****
     * 覆盖
     * 
     * @param polygon
     * @param polyline
     * @param sp
     * @return
     */
    public static Polyline getCoverPolyline(Polygon polygon, Polyline polyline, SpatialReference sp) {
        if (polygon == null || polyline == null || sp == null || polygon.isEmpty() || polyline.isEmpty())
            return new Polyline();

        Geometry result = GeometryEngine.intersect(polygon, polyline, sp);
        if (null == result)
            return new Polyline();
        if (result instanceof Polyline)
            return (Polyline) result;
        return new Polyline();
    }

    // 判断一个字符串是否都为字母
    public static boolean isAbcStr(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
