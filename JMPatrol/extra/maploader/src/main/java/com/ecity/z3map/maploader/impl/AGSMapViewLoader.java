package com.ecity.z3map.maploader.impl;

import com.ecity.z3map.maploader.AMapViewLoader;
import com.ecity.z3map.maploader.ModuleSetting;
import com.ecity.z3map.maploader.controller.AGSLayerLoader;
import com.ecity.z3map.maploader.model.ECityRect;
import com.ecity.z3map.maploader.model.EMapLoadType;
import com.ecity.z3map.maploader.model.MapScaleValue;
import com.ecity.z3map.maploader.model.SourceConfig;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;

import java.util.List;

/**
 * Created by zhengzhuanzi on 2017/6/15.
 */

public class AGSMapViewLoader extends AMapViewLoader<MapView> {
    private OnCutomZoomListener zoomListener;
    private MapLayerLoadStatusChangedListener mapLayerLoadStatusChangedListener;
    private AGSLayerLoader layerLoader;
    private double lastScale = 0;
    private double initScale = 0;
    private Envelope envelope;
    private ECityRect rect;
    private MapScaleValue mapScaleValue;
    private boolean fisrtLoad;
    private int WKID;

    public ECityRect getRect() {
        return rect;
    }

    public double getLastScale() {
        if (lastScale < 0) {
            lastScale = 1;
        }
        return lastScale;
    }

    @Override
    public void setMapViewToZoomFull() {
        if( null != layerLoader ) {
            lastScale = initScale;
            layerLoader.loadLayers();
            layerLoader.setMapViewToZoomFull();
        }
    }

    @Override
    public void prepareMapView(MapView mapView) {
        if (null == mapView) {
            return;
        }

        List<SourceConfig> sourceList = getSourceList();
        if( null != sourceList && sourceList.size() > 0 ) {
            SourceConfig cfg = sourceList.get(0);
            lastScale = cfg.dispMinScale;
            initScale = cfg.dispMinScale;
        }

        ModuleSetting config = ModuleSetting.getModuleSetting();
        mapView.setMaxResolution(config.getMaxResolution());
        mapView.setMinResolution(config.getMinResolution());

        fisrtLoad = true;
        WKID = 102113;
        mapScaleValue = new MapScaleValue();
        rect = new ECityRect(0, 0, 0, 0);
        envelope = new Envelope();
        layerLoader = new AGSLayerLoader(this);
        layerLoader.loadLayers();
        zoomListener = new OnCutomZoomListener();

        mapLayerLoadStatusChangedListener = new MapLayerLoadStatusChangedListener();

        mapView.setOnZoomListener(zoomListener);
        mapView.setOnStatusChangedListener(mapLayerLoadStatusChangedListener);

    }

    // 更新地图比例尺
    private void updateMapScaleBar(double resolution, double mapScale) {
        if (getLoadType() == EMapLoadType.NORMAL) {
            return;
        }
        //mapScaleValue.setParameterOfResolutionToScaleWithResolution(resolution, mapScale, 96);
        //mapScaleValue.getScaleBarLenthWithResolution(resolution,WKID);
        //mapScaleValue.getScaleByMapScale(mapScale);
        int scaleValue = (int) mapScale;

        switch (getLoadType()) {
            case SCALE:
                break;
            case ENVELOPE:
            case BOTH:
                try {
                    getMapView().getExtent().queryEnvelope(envelope);
                    rect = new ECityRect(envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        if (lastScale != scaleValue) {
            lastScale = scaleValue;
            layerLoader.loadLayers();
        }
    }

    public class OnCutomZoomListener implements OnZoomListener {

        public OnCutomZoomListener() {
        }

        @Override
        public void preAction(float v, float v1, double v2) {

        }

        @Override
        public void postAction(float v, float v1, double v2) {
            if (null != getMapView()) {
                updateMapScaleBar(getMapView().getResolution(), getMapView().getScale());
            }
        }

    }

    private class MapLayerLoadStatusChangedListener implements OnStatusChangedListener {
        private static final long serialVersionUID = 1L;

        public MapLayerLoadStatusChangedListener() {
        }

        @Override
        public void onStatusChanged(Object arg0, STATUS arg1) {
            if( null != getCallback() ) {
                getCallback().onStatusChanged(arg0,arg1);
            }

            switch (arg1) {
                case LAYER_LOADED:
                    if (null != getMapView()) {
                        WKID = getMapView().getSpatialReference().getID();
                        if (fisrtLoad) {
                            fisrtLoad = false;
                            updateMapScaleBar(getMapView().getResolution(), getMapView().getScale());
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    }
}
