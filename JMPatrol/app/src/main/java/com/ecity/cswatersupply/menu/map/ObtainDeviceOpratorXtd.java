package com.ecity.cswatersupply.menu.map;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.MapOperatorTipsView;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ListUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;

public class ObtainDeviceOpratorXtd extends AMapViewOperator {
    private WeakReference<IMapOperationContext> mOutersActivity;
    private IMapOperationContext mapActivity;
    private MapOperatorTipsView mMapOperatorTipsView;
    private RectIdentifyOperatorXtd listener;

    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext operationContext) {
        if (null == mapView) {
            return;
        }

        if (null == operationContext) {
            return;
        }

        mOutersActivity = new WeakReference<IMapOperationContext>(operationContext);
        super.setMapviewOption(mapView, operationContext);

        MapActivity mapActivity = (MapActivity) operationContext.getContext();
        mapActivity.getTitleView().setBtnStyle(BtnStyle.RIGHT_ACTION);
        mapActivity.getTitleView().setLegendBackground(mapActivity.getResources().getDrawable(R.drawable.checkbox_hl));
        GraphicsLayer graphicsLayer = null;
        try {
            graphicsLayer = LayerTool.getAnimationLayer(mapView);
        } catch (Exception e) {
        }
        if (null == graphicsLayer) {
            return;
        }

        Bundle bundle = mapActivity.getIntent().getExtras();
        ArrayList<Integer> queryLayerIds = new ArrayList<>();
        if(bundle.containsKey(Constants.QUERY_DEVICE_LAYER_IDS)) {
            queryLayerIds = (ArrayList<Integer>) bundle.getSerializable(Constants.QUERY_DEVICE_LAYER_IDS);
        }
        List<String> queryLayerNames = mapActivity.getIntent().getStringArrayListExtra(MapActivity.DEVICE_VALVE_OPERATOR);
        listener = new RectIdentifyOperatorXtd(false);
        if(!ListUtil.isEmpty(queryLayerIds)) {
            listener.setQueryLayerIds(queryLayerIds);
        }
        listener.setQueryLayerNames(queryLayerNames);
        listener.setUrl(ServiceUrlManager.getInstance().getSpacialSearchUrl());
        listener.setMapviewOption(mapView, operationContext, graphicsLayer);
        mapView.setOnTouchListener(new MapOnTouchListener(operationContext.getContext(), mapView));
        mapView.setOnSingleTapListener(listener);
        this.mapActivity = operationContext;
        mMapOperatorTipsView = mapActivity.getmMapOperatorTipsView();
        mMapOperatorTipsView.registerCleanListener(new MyBtnCleanMapClickListener());
    }

    @Override
    public void notifyBackEvent(IMapOperationContext operationContext) {
        if (null != operationContext) {
            operationContext.finish();
        }
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        MapActivity mapActivity = (MapActivity) operationContext.getContext();
        IdentifyResultsController queryResultsControler = mapActivity.getIdentifyResultController();
        Graphic graphic = null;
        if (queryResultsControler != null) {
            graphic = queryResultsControler.getCurrentItem();
        }

        Intent intent = new Intent();
        if (graphic != null) {
            List<String> queryLayerNames = mapActivity.getIntent().getStringArrayListExtra(MapActivity.DEVICE_VALVE_OPERATOR);
            if (!ListUtil.isEmpty(queryLayerNames)) {
                intent.putExtra("device", graphic);
            } else {
                intent.putExtra("device", graphic);
            }
            mapActivity.setResult(Activity.RESULT_OK, intent);
        } else {
            mapActivity.setResult(Activity.RESULT_CANCELED, intent);
        }
        mapActivity.finish();
    }

    private class MyBtnCleanMapClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mapActivity.cleanMapView();
            listener.setStartPoint(null);
            listener.setEndPoint(null);
            listener.setInQuery(false);
            v.setVisibility(View.GONE);
        }
    }
}
