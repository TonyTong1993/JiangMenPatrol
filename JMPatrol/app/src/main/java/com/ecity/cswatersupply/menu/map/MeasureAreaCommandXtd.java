package com.ecity.cswatersupply.menu.map;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.esri.android.map.MapView;

/**
 * 地图测距
 * @author maliu
 *
 */
public class MeasureAreaCommandXtd extends AMapMenuCommand {

    @Override
    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        if ((mapView == null) || (LayerTool.getGraphicsLayer(mapView) == null)) {
            ToastUtil.showLong(fragment.getContext().getResources().getString(R.string.planningtask_not_load_mapview));
            return false;
        }
        mapView.setOnSingleTapListener(null);
        if (mapView.isLoaded()) {
            MapMeasure mapMeasure = new MapMeasure(fragment, mapView, LayerTool.getGraphicsLayer(mapView), MapMeasure.MeasureMode.Area);
            mapMeasure.start();
        }
        return true;
    }
}
