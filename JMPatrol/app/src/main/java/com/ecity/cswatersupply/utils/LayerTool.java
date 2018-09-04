package com.ecity.cswatersupply.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Intent;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.z3app.android.util.StringUtil;

public class LayerTool {
    /**
     * @param data
     *            序列化传递值
     * @param mapView
     *            地图主视图
     * @return 控制地图按选定图层显示
     */
    @SuppressWarnings("unchecked")
    public static boolean controlLayersVisibility(Intent data, MapView mapView) {
        // 取出DATA
        Map<Integer, String> sourceList = (Map<Integer, String>) data.getSerializableExtra("SourceList");
        Map<Integer, ArcGISLayerInfo[]> DLayers = (Map<Integer, ArcGISLayerInfo[]>) data.getSerializableExtra("DLayers");

        // 首先根据SourceList来控制Source的显示
        if (sourceList != null && sourceList.size() > 0) {
            Set<Integer> sourceKeys = sourceList.keySet();

            // 创建1级子节点,source节点
            for (Iterator<Integer> it = sourceKeys.iterator(); it.hasNext();) {
                Integer sourceID = it.next();
                if (sourceID < 0) {
                    continue;
                }

                int layerVisible = Integer.parseInt(sourceList.get(sourceID).split(",")[1]) == 0 ? 0 : 1;
                Layer layer = mapView.getLayers()[sourceID];
                if (layerVisible == 1)
                    layer.setVisible(true);
                else
                    layer.setVisible(false);
            }
        }

        // 控制矢量图的显示
        if (DLayers != null && DLayers.size() > 0) {
            Set<Integer> dSourceKeys = sourceList.keySet();
            // 创建1级子节点,图层节点
            for (Iterator<Integer> it = dSourceKeys.iterator(); it.hasNext();) {
                Integer sourceID = it.next();
                if (sourceID < 0) {
                    continue;
                }
                Layer layer = mapView.getLayers()[sourceID];
                if (layer instanceof ArcGISDynamicMapServiceLayer) {
                    //ArcGISDynamicMapServiceLayer dlayer = (ArcGISDynamicMapServiceLayer) layer;
                    Object[] objects = DLayers.get(sourceID);
                    ArcGISLayerInfo[] layerInfos = new ArcGISLayerInfo[objects.length];
                    for (int j = 0; j < objects.length; j++) {
                        layerInfos[j] = (ArcGISLayerInfo) objects[j];
                        // dlayer.getAllLayers()[j].setVisible(layerInfos[j].isVisible());
                    }
                    // 设置到图层
                    // zzz 2013-11-04
                    // dSource.setLayers(layerInfos);
                }
            }
        }
        return true;
    }

    // 是否有矢量图层
    public static boolean isExistDynamicLayer(MapView mapView) {

        for (Layer layer : mapView.getLayers()) {
            if (layer instanceof ArcGISDynamicMapServiceLayer) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * @param mapView
     * @return 通过MapView得到正在使用中的在线矢量图层信息
     */

    public static ArcGISLayerInfo[] getLayersFromMapView(MapView mapView) {
        ArcGISLayerInfo[] layerInfos = null;
        try {
            ArcGISDynamicMapServiceLayer dlayer = null;
            for (Layer layer : mapView.getLayers()) {
                if (layer instanceof ArcGISDynamicMapServiceLayer) {
                    dlayer = (ArcGISDynamicMapServiceLayer) layer;
                    break;
                }
            }

            if (dlayer == null) {
                layerInfos = null;
            } else {
                layerInfos = dlayer.getAllLayers();
            }
        } catch (Exception e) {
            layerInfos = null;
        }

        return layerInfos;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * zzz 2013-10-16 获得绘图图层
     */
    public static GraphicsLayer getGraphicsLayer(MapView mapView) {
        return  getGraphicsLayerByName(mapView,"graphicsLayer");
    }

    /**
     * zzz 2013-10-16 获得绘图图层
     */
    public static GraphicsLayer getPatrolPlanGraphicsLayer(MapView mapView) {
        return  getGraphicsLayerByName(mapView,"patrolPlanGraphicsLayer");
    }

    /*
     * 获取定位点绘制图层,如果没有，则添加
     */
    public static GraphicsLayer getGPSGraphicsLayer(MapView mapView) {
        return  getGraphicsLayerByName(mapView,"gpsgraphicsLayer");
    }

    /**
     * zzz 2013-12-02 获得绘动画图层
     */
    public static GraphicsLayer getAnimationLayer(MapView mapView) {
        return  getGraphicsLayerByName(mapView,"animationLayer");
    }
    
    
    public static GraphicsLayer getEQStationLayer(MapView mapView) {
        return getGraphicsLayerByName(mapView,"eqAnimationLayer");
    }

    public static GraphicsLayer getGraphicsLayerByName(MapView mapView,String layerName) {
        if (StringUtil.isBlank(layerName)) {
            return null;
        }
        GraphicsLayer graphicsLayer = null;
        try {
            for (Layer layer : mapView.getLayers()) {
                if (layer instanceof GraphicsLayer) {
                    if (layer.getName().equals(layerName)) {
                        graphicsLayer = (GraphicsLayer) layer;
                    }
                }
            }
            if (null == graphicsLayer) {
                graphicsLayer = new GraphicsLayer();
                graphicsLayer.setName(layerName);
                mapView.addLayer(graphicsLayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return graphicsLayer;
    }
}
