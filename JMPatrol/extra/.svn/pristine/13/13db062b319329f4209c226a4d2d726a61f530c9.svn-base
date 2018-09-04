package com.ecity.z3map.maploader.controller;

import com.ecity.android.map.layer.ECityCacheableTiledServiceLayer;
import com.ecity.android.map.layer.WMTSLayer;
import com.ecity.z3map.maploader.impl.AGSMapViewLoader;
import com.ecity.z3map.maploader.model.ESourceType;
import com.ecity.z3map.maploader.model.SourceConfig;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengzhuanzi on 2017/6/16.
 */

public class AGSLayerLoader {
    private AGSMapViewLoader agsMapViewLoader;
    private List<GraphicsLayer> glayers;
    private LinkedHashMap<String, Layer> layerLinkedHashMap;

    public AGSLayerLoader(AGSMapViewLoader agsMapViewLoader) {
        this.agsMapViewLoader = agsMapViewLoader;
        glayers = new ArrayList<GraphicsLayer>();
        layerLinkedHashMap = new LinkedHashMap<String, Layer>();
    }
    public void setMapViewToZoomFull(){
        try {
            Layer[] newlayers = agsMapViewLoader.getMapView().getLayers();
            newlayers[0].setVisible(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadLayers() {
        if (null == agsMapViewLoader) {
            return;
        }

        if (null == agsMapViewLoader.getMapView()) {
            return;
        }

        List<SourceConfig> sourceList = null;

        switch (agsMapViewLoader.getLoadType()) {
            case NORMAL:
                sourceList = agsMapViewLoader.getSourceList();
                break;
            case SCALE:
                sourceList = agsMapViewLoader.getSourceListForScale(agsMapViewLoader.getLastScale());
                break;
            case ENVELOPE:
                sourceList = agsMapViewLoader.getSourceListForExtent(agsMapViewLoader.getRect());
                break;
            case BOTH:
                sourceList = agsMapViewLoader.getSourceListForExtentInScale(agsMapViewLoader.getRect(), agsMapViewLoader.getLastScale());
                break;
            default:
                sourceList = agsMapViewLoader.getSourceList();
                break;
        }

        if (null != sourceList) {
            try {
                storeGraphicsLayer();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                internalLoad(sourceList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                restoreGraphicsLayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void storeGraphicsLayer() {
        glayers.clear();
        Layer[] layers = agsMapViewLoader.getMapView().getLayers();
        if (null != layers && layers.length > 0) {
            for (int i = 0; i < layers.length; i++) {
                Layer layer = layers[i];
                if (null != layer && layer instanceof GraphicsLayer) {
                    try {
                        glayers.add((GraphicsLayer) layer);
                        agsMapViewLoader.getMapView().removeLayer(layer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void restoreGraphicsLayer() {
        if (null != glayers && !glayers.isEmpty()) {
            int size = glayers.size();
            for (int i = 0; i < size; i++) {
                GraphicsLayer layer = glayers.get(i);
                agsMapViewLoader.getMapView().addLayer(layer);
            }
        }
    }

    private void internalLoad(List<SourceConfig> sourceList) {
        Layer[] layers = agsMapViewLoader.getMapView().getLayers();

        if (null != layers && layers.length > 0) {
            for (int j = 0; j < layers.length; j++) {

                Layer layer = layers[j];
                if (null != layer) {
                    boolean isContain = false;
                    for (int i = 0; i < sourceList.size(); i++) {
                        SourceConfig tmpObj = sourceList.get(i);
                        if (layer.getName().equalsIgnoreCase(tmpObj.serverName)) {
                            isContain = true;
                            break;
                        }
                    }

                    if (!isContain) {
                        if (isFirstLayer(layer.getName())) {
                            layer.setVisible(false);
                        } else {
                            agsMapViewLoader.getMapView().removeLayer(layer);
                        }
                    }
                }
            }

            Layer[] newlayers = agsMapViewLoader.getMapView().getLayers();
            if (null == newlayers || newlayers.length == 0) {
                for (int i = 0; i < sourceList.size(); i++) {
                    SourceConfig tmpObj = sourceList.get(i);
                    Layer ly = buildLayer(tmpObj);
                    if (null != ly) {
                        agsMapViewLoader.getMapView().addLayer(ly);
                    }
                }
            } else {
                for (int i = 0; i < sourceList.size(); i++) {
                    SourceConfig tmpObj = sourceList.get(i);
                    if (null != newlayers && newlayers.length > 0) {
                        boolean isContain = false;
                        for (int j = 0; j < newlayers.length; j++) {
                            Layer layer = newlayers[j];
                            if (null != layer && layer.getName().equalsIgnoreCase(tmpObj.serverName)) {
                                isContain = true;
                                layer.setVisible(true);
                                break;
                            }
                        }

                        if (!isContain) {
                            Layer ly = buildLayer(tmpObj);
                            if (null != ly) {
                                agsMapViewLoader.getMapView().addLayer(ly);
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < sourceList.size(); i++) {
                SourceConfig tmpObj = sourceList.get(i);
                Layer ly = buildLayer(tmpObj);
                if (null != ly) {
                    agsMapViewLoader.getMapView().addLayer(ly);
                }
            }
        }
    }

    private boolean isFirstLayer(String layerName) {

        if (null == layerName || layerName.equalsIgnoreCase("")) {
            return false;
        }

        if (!layerLinkedHashMap.containsKey(layerName)) {
            return false;
        }

        Iterator<LinkedHashMap.Entry<String, Layer>> iterator = layerLinkedHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Layer> entry = iterator.next();
            return entry.getKey().equalsIgnoreCase(layerName);
        }

        return false;
    }

    private Layer buildLayer(SourceConfig config) {
        Layer layer = null;
        if (null == config) {
            return layer;
        }

        if (ESourceType.ARCGISLOCALTILEDLAYER != config.sourceType && config.URL.equals("")) {
            return layer;
        }

        if (layerLinkedHashMap.containsKey(config.serverName)) {
            return layerLinkedHashMap.get(config.serverName);
        }

        switch (config.sourceType) {
            case ARCGISLOCALTILEDLAYER:
                if (null != config.tpkUrl && config.tpkUrl.length() > 0) {
                    String urlTpkName = config.tpkUrl.split("/")[config.tpkUrl.split("/").length - 1];
                    urlTpkName = urlTpkName.toLowerCase();
                    if (!urlTpkName.endsWith(".tpk")) {
                        urlTpkName += ".tpk";
                    }
                    String localPath = "file://" + config.sourceLocalPath + urlTpkName;
                    layer = new ArcGISLocalTiledLayer(localPath);
                }
                break;
            case ARCGISTILEDMAPSERVICELAYER:
                layer = new ArcGISTiledMapServiceLayer(config.URL);
                break;
            case ARCGISDYNAMICMAPSERVICELAYER:
                layer = new ArcGISDynamicMapServiceLayer(config.URL);
                break;
            case ARCGISFEATURELAYER:
                layer = new ArcGISFeatureLayer(config.URL, ArcGISFeatureLayer.MODE.ONDEMAND);
                break;
            case ARCGISIMAGESERVICELAYER:
                layer = new ArcGISImageServiceLayer(config.URL, null);
                break;
            case WMTSLAYER:
                //ArcGISImageServiceLayer(config.URL, null);
                layer = new WMTSLayer(config.sourceLocalPath, config.URL, config.tpkUrl, config.serverName, true);
                break;
            case ECITYTILEDMAPSERVICELAYER:
                layer = new ECityCacheableTiledServiceLayer(config.sourceLocalPath, config.URL, config.serverName, true);
                break;
            case DBLAYER:

                break;
        }

        if (layer != null) {

            if (null != config.isVisible && config.isVisible.equalsIgnoreCase("0")) {
                layer.setVisible(false);
            } else {
                layer.setVisible(true);
            }

            layer.setName(config.serverName);
        }

        layerLinkedHashMap.put(config.serverName, layer);

        return layer;
    }
}
