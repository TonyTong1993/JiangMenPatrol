package com.ecity.cswatersupply.menu.map;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.EmergencyRefreshHelper;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public class EmptyMapCommandXtd extends AMapMenuCommand {
    //地图进行清除操作时，保留地震信息和台站信息的OnSingleTapListener事件
    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        if (null != mapView) {
            mapView.setOnLongPressListener(null);
            mapView.setOnTouchListener(new MapOnTouchListener(null, mapView) {
            });
            mapView.setOnSingleTapListener(null);
        }
        if (null != fragment) {
            fragment.cleanMapView();
        }
        if (null != mapView) {
            EQModuleConfig config;
            try {
                config = EQModuleConfig.getConfig();
                if (config.isModuleUseable()) {
                    EmergencyRefreshHelper.getInstance().initMapViewListener();
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
        return true;
    }
}
