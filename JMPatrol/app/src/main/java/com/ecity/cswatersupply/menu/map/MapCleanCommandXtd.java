package com.ecity.cswatersupply.menu.map;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public class MapCleanCommandXtd extends AMapMenuCommand{
	
    public boolean execute(MapView mapView, IMapOperationContext fragment) {
    	if (null == mapView || !mapView.isLoaded()) {
            return true;
        } 
        if (null != mapView) {
            mapView.setOnLongPressListener(null);
            mapView.setOnTouchListener(new MapOnTouchListener(null, mapView) {
            });
            mapView.setOnSingleTapListener(null);
        }
        if (null != fragment) {
            fragment.cleanMapView();
        }
        
        return true;
    }
}
