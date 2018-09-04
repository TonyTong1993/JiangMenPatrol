package com.ecity.cswatersupply.menu.map;

import com.esri.android.map.MapView;

public class EmptyOpratorXtd extends AMapViewOperator {
    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext mapMainTabFragment) {
        super.setMapviewOption(mapView, mapMainTabFragment);
    }

    @Override
    public void notifyBackEvent(IMapOperationContext mapMainTabFragment) {
        if (null != mapMainTabFragment) {
            mapMainTabFragment.getContext().finish();
        }
    }
}
