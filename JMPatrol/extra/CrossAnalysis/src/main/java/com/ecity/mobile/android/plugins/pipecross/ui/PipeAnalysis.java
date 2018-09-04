
/**   
 * 文件名：PipeAnalysis.java   
 *   
 * 版本信息：   
 * 日期：2016年6月29日   
 * Copyright Ecity Corporation 2016    
 * 版权所有   
 *   
 */

package com.ecity.mobile.android.plugins.pipecross.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * 此类描述的是：
 * 
 * @author: wly
 * @version: 2016年6月29日 下午6:49:16
 */

public class PipeAnalysis {

    public static List<PipeItem> data = new ArrayList<PipeItem>();
    public static double minPipeDep, maxPipeDep;
    public static HashMap<String, String> compass = new HashMap<String, String>();

    /**
     * 
     * 此方法描述的是： 管网数据处理
     * 
     * @author: wly
     * @version: 2016年7月15日 下午5:31:14
     */
    public static void formatData(JSONArray jsonArray) throws JSONException {
        PipeItem newItem = null;
        boolean hasCenterRoad = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            if (item.has("hasCenterRoad")) {
                hasCenterRoad = item.getBoolean("hasCenterRoad");
            } else {
                hasCenterRoad = true;
            }
            String layerName = item.getString("layerName");
            String code = item.getString("code");
            double groundAltitude = item.getDouble("groundEleva");
            double pipeDepth = item.getDouble("depth");
            //对于pipeAltitudeType和pipeColor还会重新设置
            double dia1 = item.getDouble("diam0") * 0.5;
            double dia2 = item.getDouble("diam1") * 0.5;
            double radius;
            String roadName;
            if (item.has("roadName")) {
                roadName = item.getString("roadName");
            } else {
                roadName = "未知名道路";
            }
            if ((0.0005 * dia2) > 0) {
                radius = dia2 * 0.0005;
            } else {
                radius = dia1 * 0.0005;
            }
            double pipeAltitude = 0;
            if (item.getDouble("z") != 0) {
                pipeAltitude = item.getDouble("z");
            } else {
                pipeAltitude = groundAltitude - pipeDepth;
            }
            pipeAltitude = Math.round(pipeAltitude * 100) / 100.00;
            groundAltitude = Math.round(groundAltitude * 100) / 100.00;
            if (groundAltitude < minPipeDep) {
                minPipeDep = groundAltitude;
            }
            if (groundAltitude > maxPipeDep) {
                maxPipeDep = groundAltitude;
            }
            String netName = item.getString("netName");
            int interSpace = -1;
            double currentDiameter = dia1;
            JSONObject attribute = item.getJSONObject("attributes");
            String pipeShapeType = "circle";
            double z;
            if (item.getDouble("z") == 0) {
                z = groundAltitude - pipeDepth - dia2;
            } else {
                z = item.getDouble("z");
            }
            newItem = new PipeItem(hasCenterRoad, layerName, code, groundAltitude, pipeAltitude, null,
                    netName, interSpace, pipeDepth, dia1, dia2, currentDiameter, attribute, roadName,
                    item.getDouble("x"), item.getDouble("y"), z, pipeShapeType, 0);
            data.add(newItem);
        }
        if (data.size() > 1) {
            sortPipe(data);
        }
    }

    /**
     * 按照官网的x坐标排序
     */
    @SuppressWarnings("unchecked")
    public static void sortPipe(List<PipeItem> data) {
        ComparatorPipe comp = new ComparatorPipe();
        Collections.sort(data, comp);
    }

    /**
     * 
     * 此方法描述的是： 指南针角度
     * 
     * @author: wly
     * @version: 2016年7月15日 下午5:30:37
     */
    public static double getCompass() {
        double compassY = CrossResultActivity.qureyY1 - CrossResultActivity.qureyY2;
        double compassX = CrossResultActivity.qureyX1 - CrossResultActivity.qureyX2;
        double compassR = Math.sqrt(Math.pow(compassX, 2) + Math.pow(compassY, 2));
        double theta;
        if (Math.abs(compassX / compassR) <= 0.1) {
            if (compassY > 0) {
                theta = Math.PI * 0.5;// 90'

            } else {
                theta = Math.PI * 1.5;// 270'
            }
        } else {
            if (Math.abs(compassY / compassR) <= 0.1) {
                if (compassX > 0) {
                    theta = 0.0;// 90'

                } else {
                    theta = Math.PI;
                }
            } else {
                double sin = compassY / compassR;
                double cos = compassX / compassR;
                if (sin > 0) {
                    if (cos > 0) {
                        theta = Math.asin(sin);// 第一象限
                    } else {
                        theta = Math.PI - Math.asin(sin);// 第二象限
                    }
                } else {
                    if (cos > 0) {
                        theta = 2 * Math.PI + Math.asin(sin);// 第si象限
                    } else {
                        theta = Math.PI - Math.asin(sin);// 第san象限
                    }
                }
            }
        }
        // 指针方向从“切线方向”变为“道路方向”，逆时针旋转90度 by re
        theta -= Math.PI * 0.5;
        return theta;
    }

    private static class ComparatorPipe implements Comparator {

        @Override
        public int compare(Object obj1, Object obj2) {
            PipeItem item1 = (PipeItem) obj1;
            PipeItem item2 = (PipeItem) obj2;
            // 比较X坐标
            if (item1.getX() > item2.getX()) {
                return 1;
            } else if (item1.getX() < item2.getX()) {
                return -1;
            } else {
                return -1;
            }

        }

    }

}
