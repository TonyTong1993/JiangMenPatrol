package com.ecity.z3map.maploader;

import com.ecity.z3map.maploader.model.ECityRect;
import com.ecity.z3map.maploader.model.EMapLoadType;
import com.ecity.z3map.maploader.model.SourceConfig;
import com.esri.android.map.event.OnStatusChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengzhuanzi on 2017/6/15.
 */

public abstract class AMapViewLoader<T> {
    private List<SourceConfig> sourceList;
    private List<SourceConfig> filterList = new ArrayList<SourceConfig>();
    private T mapView;
    private EMapLoadType loadType;
    private Z3MaploadCallback callback;

    public interface Z3MaploadCallback{
        public void onStatusChanged(Object arg0, OnStatusChangedListener.STATUS arg1);
    }
    /**
     * 地图加载模式
     * @return
     */
    public EMapLoadType getLoadType() {
        return loadType;
    }

    /**
     * 获得所有的地图配置信息
     * @return
     */
    public List<SourceConfig> getSourceList() {
        if (null == sourceList) {
            sourceList = new ArrayList<SourceConfig>();
        }

        return sourceList;
    }

    /**
     * 获得该比例尺下的地图配置信息
     * @param scale
     * @return
     */
    public List<SourceConfig> getSourceListForScale(double scale) {
        return getSourceListForScale(getSourceList(),scale);
    }

    /**
     * 获得该比例尺下的地图配置信息
     * @param scale
     * @return
     */
    public List<SourceConfig> getSourceListForScale(List<SourceConfig> srcList,double scale) {
        filterList.clear();
        if (null !=  srcList) {
            int size = srcList.size();
            for(int i = 0; i < size; i++ ) {
                SourceConfig config = srcList.get(i);
                if(null != config && ( config.dispMinScale >= scale && scale >= config.dispMaxScale) ) {
                    filterList.add(config);
                }
            }
        }

        return filterList;
    }

    /**
     * 获得在与此矩形相交的
     * @return
     */
    public List<SourceConfig> getSourceListForExtent(ECityRect rect) {
        return getSourceListForExtent(getSourceList(),rect);
    }

    /**
     * 获得在与此矩形相交的
     * @return
     */
    public List<SourceConfig> getSourceListForExtent(List<SourceConfig> srcList,ECityRect rect) {
        filterList.clear();
        if (null !=  getSourceList() && null != rect) {
            int size = getSourceList().size();
            for(int i = 0; i < size; i++ ) {
                SourceConfig config = getSourceList().get(i);
                if(null != config && rectCrossAlgorithm(config.dispRect,rect) ) {
                    filterList.add(config);
                }
            }
        }

        return filterList;
    }

    /**
     * 获取所在范围内的，符合比例尺的数据源
     * @return
     */
    public List<SourceConfig> getSourceListForExtentInScale(ECityRect rect,double scale) {
        List<SourceConfig> filterList = getSourceListForExtent(getSourceList(),rect);
        return getSourceListForScale(filterList,scale);
    }

    public boolean rectCrossAlgorithm(ECityRect r1,ECityRect r2){
        double nMaxLeft = 0;
        double nMaxTop = 0;
        double nMinRight = 0;
        double nMinBottom = 0;

        //计算两矩形可能的相交矩形的边界
        nMaxLeft = r1.getXMin() >= r2.getXMin() ? r1.getXMin() : r2.getXMin();
        nMaxTop = r1.getYMin() >= r2.getYMin() ? r1.getYMin() : r2.getYMin();
        nMinRight = (r1.getXMin() + r1.getWidth()) <= (r2.getXMin()+ r2.getWidth()) ? (r1.getXMin()+ r1.getWidth()) : (r2.getXMin()+ r2.getWidth());
        nMinBottom = (r1.getYMin() + r1.getHeight()) <= (r2.getYMin()+ r2.getHeight()) ? (r1.getYMin()+ r1.getHeight()) : (r2.getYMin()+ r2.getHeight());
        // 判断是否相交
        if (nMaxLeft > nMinRight || nMaxTop > nMinBottom) {
            return false;
        }

        return true;
    }

    public  void loadMap(List<SourceConfig> sourceList,T mapView,EMapLoadType loadType,Z3MaploadCallback callback) {
        this.sourceList = sourceList;
        this.mapView = mapView;
        this.loadType = loadType;
        this.callback = callback;

        prepareMapView(mapView);
    }
    public abstract void setMapViewToZoomFull();

    public T getMapView() {
        return mapView;
    }

    /***
     * 地图加载回调
     * @return
     */
    public Z3MaploadCallback getCallback() {
        return callback;
    }

    /**
     * 开始加载MapView
     * @param mapView
     */
    public abstract void prepareMapView(T mapView);

}
