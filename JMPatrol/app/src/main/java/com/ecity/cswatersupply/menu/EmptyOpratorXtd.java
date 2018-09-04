package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.menu.map.AMapViewOperator;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.esri.android.map.MapView;

public class EmptyOpratorXtd extends AMapViewOperator {
    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext mapActivity) {
        super.setMapviewOption(mapView, mapActivity);
    }

    @Override
    public void notifyBackEvent(IMapOperationContext mapActivity) {
        if (null != mapActivity) {
            mapActivity.finish();
        }
    }
}
