package com.ecity.cswatersupply.menu.map;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public abstract class AMapMenuCommand {

    public boolean execute(MapView mapView, IMapOperationContext mapContext) {
        if (null != mapView) {
            mapView.setOnLongPressListener(null);
            mapView.setOnTouchListener(new MapOnTouchListener(null, mapView) {
            });
            mapView.setOnSingleTapListener(null);
        }
        if (null != mapContext) {
            mapContext.cleanMapView();
        }
        return true;
    }
}
