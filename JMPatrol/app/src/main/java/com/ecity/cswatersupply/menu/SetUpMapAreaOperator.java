package com.ecity.cswatersupply.menu;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.AMapViewOperator;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.MapOperatorTipsView;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.PlanTaskUtils;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

public class SetUpMapAreaOperator extends AMapViewOperator {
    public static final String INTENT_KEY_AREA = "INTENT_KEY_AREA";

    private WeakReference<IMapOperationContext> mOutersActivity;
    private WeakReference<MapView> mOutersMapView;
    private TextView tvAddArea;
    private MapActivityTitleView customTitleView;
    private boolean isFirstPoint = true;
    private IMapOperationContext activity;
    private MapOperatorTipsView mMapOperatorTipsView;
    private Polygon polygon;
    private MapView mMapView;
    private GraphicsLayer graphicLayer;
    private SimpleFillSymbol mFillSymbol;

    @Override
    public void setMapviewOption(final MapView mapView, IMapOperationContext mapActivity) {
        if (null == mapView) {
            return;
        }

        super.setMapviewOption(mapView, mapActivity);

        mOutersActivity = new WeakReference<IMapOperationContext>(mapActivity);
        mOutersMapView = new WeakReference<MapView>(mapView);
        activity = mOutersActivity.get();
        mMapView = mOutersMapView.get();
        graphicLayer = LayerTool.getGraphicsLayer(mMapView);
        if (null == graphicLayer) {
            return;
        }

        tvAddArea = (TextView) activity.getContext().findViewById(R.id.tv_add_another_area);
        tvAddArea.setVisibility(View.VISIBLE);
        customTitleView = (MapActivityTitleView) activity.getContext().findViewById(R.id.view_title_mapactivity);
        customTitleView.setBtnStyle(BtnStyle.ONLY_CONFIRM);
        customTitleView.setRightActionBtnText(R.string.ok);
        mMapOperatorTipsView = mapActivity.getmMapOperatorTipsView();
        mMapOperatorTipsView.registerCleanListener(new MyBtnCleanMapClickListener());
        SimpleLineSymbol polygonOutline = new SimpleLineSymbol(Color.DKGRAY, 1, SimpleLineSymbol.STYLE.SOLID);
        mFillSymbol = new SimpleFillSymbol(0x602c68d6, SimpleFillSymbol.STYLE.SOLID);
        mFillSymbol.setOutline(polygonOutline);

        String selectedArea = activity.getContext().getIntent().getStringExtra(INTENT_KEY_AREA);
        if (null != selectedArea) {
            polygon = (Polygon) PlanTaskUtils.buildGeometryFromJSON(selectedArea);
            Graphic polygonGraphic = new Graphic(polygon, mFillSymbol);
            graphicLayer.removeAll();
            graphicLayer.addGraphic(polygonGraphic);
            activity.getmMapOperatorTipsView().displayButtonClean();
        }
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        Intent intent = new Intent();
        if (polygon != null) {
            String geomArea = PlanTaskUtils.buildGeoJSONFromGeometry(mMapView.getSpatialReference(), polygon);
            intent.putExtra(INTENT_KEY_AREA, geomArea);
        }
        operationContext.getContext().setResult(ResultCode.GETGEOMAREA, intent);
        operationContext.finish();
        if (null != graphicLayer) {
            graphicLayer.removeAll();
        }
    }

    @Override
    public void notifyBackEvent(IMapOperationContext operationContext) {
        operationContext.finish();
    }

    @Override
    public void notifyMapLoaded() {
        super.notifyMapLoaded();
        setListener(graphicLayer, mFillSymbol);
    }

    private class MyBtnCleanMapClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            activity.cleanMapView();
            graphicLayer.removeAll();
            isFirstPoint = true;
            polygon = null;
            v.setVisibility(View.GONE);
        }
    }

    private void setListener(final GraphicsLayer setAreaLayer, final SimpleFillSymbol fillSymbol) {
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSingleTap(float arg0, float arg1) {
                Point selectPoint = mMapView.toMapPoint(arg0, arg1);
                if (isFirstPoint && polygon == null) {
                    if (polygon == null) {
                        polygon = new Polygon();
                    }
                    polygon.startPath(selectPoint);
                    isFirstPoint = false;
                } else {
                    polygon.lineTo(selectPoint);
                }
                activity.getmMapOperatorTipsView().displayButtonClean();
                Graphic polygonGraphic = new Graphic(polygon, fillSymbol);
                setAreaLayer.removeAll();
                setAreaLayer.addGraphic(polygonGraphic);
            }
        });

        mMapView.setOnLongPressListener(new OnLongPressListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onLongPress(float arg0, float arg1) {
                setAreaLayer.removeAll();
                polygon = null;
                isFirstPoint = true;
            }
        });

        tvAddArea.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isFirstPoint = true;
            }
        });
    }
}
