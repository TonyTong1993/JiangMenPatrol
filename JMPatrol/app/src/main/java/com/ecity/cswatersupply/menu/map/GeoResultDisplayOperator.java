package com.ecity.cswatersupply.menu.map;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.map.SearchResult;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.MapNavigationView;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

public class GeoResultDisplayOperator extends AMapViewOperator {
    private WeakReference<MapView> mOutersMapView;
    public Z3PlanTask plantask;
    public MapActivity activity;
    MapView mapView;
    private int uid = -1;
    private MapNavigationView navigationView;
    private MapMainTabFragment fragment;
    private SearchResult result;

    public void setMapviewOption(MapView mMapView, IMapOperationContext mapMainTabFragment) {
        if (null == mMapView) {
            return;
        }
        mOutersMapView = new WeakReference<MapView>(mMapView);
        mapView = mOutersMapView.get();
        mapView.setOnLongPressListener(null);
        fragment = (MapMainTabFragment) mapMainTabFragment;
    }

    public void notifyBackEvent(MapActivity mapActivity) {
        if (null != mapActivity) {
            mapActivity.finish();
        }
    }

    @Override
    public void notifyMapLoaded() {

        result = fragment.getResult();
        navigationView = fragment.getNavigationView();
        if (null == mapView) {
            return;
        }
        drawGraphics();
    }

    public PictureMarkerSymbol getPointPicture(int index) throws IOException {
        PictureMarkerSymbol pointPicture = null;
        try {
            pointPicture = new PictureMarkerSymbol(getDrawableByName("icon_mark" + String.valueOf(index)));
            pointPicture.setOffsetY(16);
            pointPicture.setAngle(0);
        } catch (Exception e) {
            LogUtil.e(activity, e);
        }
        return pointPicture;
    }

    private void drawGraphics() {
        if (null == mapView) {
            return;
        }

        if (null == result) {
            return;
        }
        
        PictureMarkerSymbol pSymbol = new PictureMarkerSymbol(HostApplication.getApplication().getResources().getDrawable(R.drawable.map_location_middle_red));
        Point p = (Point) result.graphic.getGeometry();
        mapView.centerAt(p, true);
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("titleName", result.titleName);
        attr.put("address", result.address);
        Graphic g = new Graphic(p, pSymbol, attr, 0);
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        if (-1 == uid) {
            uid = graphicsLayer.addGraphic(g);
        } else {
            graphicsLayer.updateGraphic(uid, g);
        }
        navigationView.setVisibility(View.VISIBLE);
        navigationView.setDeviceTxtVisibility(View.GONE);
        navigationView.setAddressTxtVisibility(View.VISIBLE);
        navigationView.setDetailAddressTxtVisibility(View.VISIBLE);
        navigationView.setText(result.titleName);
        navigationView.setDetailAddressText(result.address);
    }

    private Drawable getDrawableByName(String name) {
        int id = -1;
        Resources res = HostApplication.getApplication().getApplicationContext().getResources();
        try {
            id = res.getIdentifier(name, "drawable", HostApplication.getApplication().getApplicationContext().getPackageName());

        } catch (Exception e) {
        }
        return res.getDrawable(id);
    }
}
