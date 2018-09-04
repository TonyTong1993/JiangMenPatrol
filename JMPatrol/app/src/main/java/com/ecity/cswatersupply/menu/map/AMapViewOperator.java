package com.ecity.cswatersupply.menu.map;

import android.content.Intent;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public abstract class AMapViewOperator {

    public void setMapviewOption(MapView mapView, IMapOperationContext operationContext) {
        if (null != mapView) {
            mapView.setOnLongPressListener(null);
            mapView.setOnTouchListener(new MapOnTouchListener(null, mapView) {
            });
        }
    }

    public void notifyBackEvent(IMapOperationContext operationContext) {

    }

    public void notifyActionEvent(IMapOperationContext operationContext) {
        notifyBackEvent(operationContext);
    }

    public void notifySearchBtnOnClicked(IMapOperationContext operationContext) {
    }

    public void notifyMapLoaded() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
