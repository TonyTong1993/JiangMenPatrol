package com.ecity.cswatersupply.menu.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

public class LocationOperatorXtd extends AMapViewOperator{
    private MapActivity mapActivity;
    private MapView mapView;
    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext operationContext) {
        super.setMapviewOption(mapView, operationContext);
        if (null == mapView || null == operationContext) {
            return;
        }
        mapActivity = (MapActivity) operationContext.getContext();
        this.mapView = mapView;
    }

    @Override
    public void notifyBackEvent(IMapOperationContext operationContext) {
        super.notifyBackEvent(operationContext);
        operationContext.finish();
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        super.notifyActionEvent(operationContext);
        operationContext.finish();
    }

    @Override
    public void notifyMapLoaded() {
        super.notifyMapLoaded();
        showLocation();
    }

    private void showLocation() {
        Bundle bundle = mapActivity.getIntent().getExtras();
        double x = Double.valueOf(bundle.getString(MapActivity.EVENT_LLOCATION_LON));
        double y = Double.valueOf(bundle.getString(MapActivity.EVENT_LLOCATION_LAT));
        Point point = new Point(x,y);
        Drawable drawable = HostApplication.getApplication().getResources().getDrawable(R.drawable.marker_point_night_red);
        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(drawable);
        mapView.centerAt(point, true);
        Graphic graphic = new Graphic(point, locationSymbol, null, null);
        GraphicsLayer graphicsLayer = LayerTool.getGPSGraphicsLayer(mapView);
        graphicsLayer.removeAll();
        graphicsLayer.addGraphic(graphic);
    }
}
