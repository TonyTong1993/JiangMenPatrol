package com.ecity.cswatersupply.emergency;

import java.util.List;

import android.graphics.Color;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.model.EQMonitorStationModel;
import com.ecity.cswatersupply.emergency.model.EQRefugeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.network.request.GetEQStationParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.ui.widght.MapNavigationPopView;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.xg.model.Notification;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

public class EmergencyRefreshHelper {
    private static EmergencyRefreshHelper instance;

    private List<EarthQuakeInfoModel> lastEQList;
    private List<EQMonitorStationModel> lastEQStationList;
    private List<EQRefugeInfoModel> lastEQRefugeList;
    private long lastUpdateTime;
    private boolean resumed;
    private IMapOperationContext mapOperation;
    private GraphicsLayer eqLayer;
    private GraphicsLayer eqStationLayer;
    private GraphicsLayer eqRefugeLayer;
    private MapView mapView;
    private GraphicFlash graphicFlash;
    private GraphicsLayer annimationLayer;
    private MapNavigationPopView naviPop;

    private EmergencyRefreshHelper() {
    }

    public static EmergencyRefreshHelper getInstance() {
        if (null == instance) {
            instance = new EmergencyRefreshHelper();
        }

        return instance;
    }

    public void initGraphicsLayer(MapView mapView, IMapOperationContext mapOperation) {
        try {
            EQModuleConfig config = EQModuleConfig.getConfig();
            if (config.isModuleUseable() || config.isCZModuleUseable()) {
                this.mapOperation = mapOperation;
                this.mapView = mapView;
                naviPop = new MapNavigationPopView(mapOperation);
                eqLayer = LayerTool.getGraphicsLayerByName(mapOperation.getMapView(), EQModuleConfig.EQLAYERNAME);
                eqStationLayer = LayerTool.getGraphicsLayerByName(mapOperation.getMapView(), EQModuleConfig.STATIONLAYERNAME);
                eqRefugeLayer = LayerTool.getGraphicsLayerByName(mapOperation.getMapView(), EQModuleConfig.REFUGENAME);
            }
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    public void onResumeLazy() {
        resumed = true;
        if (!validate()) {
            return;
        }
        EventBusUtil.register(this);
        EmergencyService.getInstance().getEarthQuackeList(new GetEarthQuakeParameter(), ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL);
        EmergencyService.getInstance().getAllEQStationList(new GetEQStationParameter());
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                EmergencyService.getInstance().getRefugeList();
            }
        } catch (Exception e) {
        }
    }

    public void initMapViewListener() {
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSingleTap(float arg0, float arg1) {
                try {
                    AModel infoModel = null;
                    int[] eqGids = eqLayer.getGraphicIDs(arg0, arg1, 20, 1);

                    if ((1 == eqGids.length)) {
                        Graphic graphic = eqLayer.getGraphic(eqGids[0]);
                        highlightGraphic(graphic);
                        infoModel = getQuakeInfoByGraphic(graphic);

                    }

                    if (null == infoModel) {
                        int[] esGids = eqStationLayer.getGraphicIDs(arg0, arg1, 20, 1);
                        if ((1 == esGids.length)) {
                            Graphic graphic = eqStationLayer.getGraphic(esGids[0]);
                            highlightGraphic(graphic);
                            infoModel = getQuakeStationInfoByGraphic(graphic);
                        }
                    }

                    if (null == infoModel) {
                        int[] esGids = eqRefugeLayer.getGraphicIDs(arg0, arg1, 20, 1);
                        if ((1 == esGids.length)) {
                            Graphic graphic = eqRefugeLayer.getGraphic(esGids[0]);
                            highlightGraphic(graphic);
                            infoModel = getEQRefugeInfoByGraphic(graphic);
                        }
                    }

                    //TODO 需增加避难所图标显示

                    if (null == infoModel) {
                        naviPop.dismiss();
                        graphicFlash.stopFlash();
                        return;
                    }

                    naviPop.initPopWindow(R.layout.fragment_map, infoModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private EarthQuakeInfoModel getQuakeInfoByGraphic(Graphic graphic) {
        int size = lastEQList.size();
        for (int i = 0; i < size; i++) {
            EarthQuakeInfoModel model = lastEQList.get(i);
            if (String.valueOf(model.getId()).equals(graphic.getAttributeValue("id"))) {
                return model;
            }
        }
        return null;
    }

    private EQMonitorStationModel getQuakeStationInfoByGraphic(Graphic graphic) {
        int size = lastEQStationList.size();
        for (int i = 0; i < size; i++) {
            EQMonitorStationModel model = lastEQStationList.get(i);
            if (String.valueOf(model.getId()).equals(graphic.getAttributeValue("id"))) {
                return model;
            }
        }
        return null;
    }

    private EQRefugeInfoModel getEQRefugeInfoByGraphic(Graphic graphic) {
        int size = lastEQRefugeList.size();
        for (int i = 0; i < size; i++) {
            EQRefugeInfoModel model = lastEQRefugeList.get(i);
            if (String.valueOf(model.getId()).equals(graphic.getAttributeValue("id"))) {
                return model;
            }
        }
        return null;
    }

    private void highlightGraphic(Graphic result) {
        if (null == result) {
            return;
        }

        Graphic g_pipeNew = getHighlightGraphic(result);
        if (null == annimationLayer) {
            annimationLayer = LayerTool.getEQStationLayer(mapView);
        }
        if (null == annimationLayer) {
            return;
        }

        if (null != graphicFlash) {
            graphicFlash.stopFlash();
            graphicFlash = null;
        }
        graphicFlash = new GraphicFlash(result, g_pipeNew, annimationLayer);
        graphicFlash.startFlash(400);
    }

    private Graphic getHighlightGraphic(Graphic graphic) {
        Graphic g = null;
        try {
            if (null != graphic) {
                Geometry geometry = graphic.getGeometry();
                String level = String.valueOf(graphic.getAttributeValue("level"));
                if ("null".equals(level)) {
                    Symbol symbol = EarthQuakeDisplayUtil.buildEQMarkerSymbol(R.drawable.icon_eqlevel_green);
                    g = new Graphic(geometry, symbol);
                } else {
                    Symbol symbol = EarthQuakeDisplayUtil.buildEQMarkerSymbol(R.drawable.icon_eqlevel_green, Double.parseDouble(level), Color.GREEN);
                    g = new Graphic(geometry, symbol, graphic.getAttributes(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    public void onPauseLazy() {
        lastUpdateTime = 0;
        resumed = false;
        this.mapOperation = null;
        EventBusUtil.unregister(this);
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.XGPUSH_CLICK_VIEW_MAP:
                Notification data = event.getData();
                EarthQuakeInfoModel earthQuakeInfoModel = data.getEarthQuakeInfoModel();
                Graphic g = drawEarthQuakeInfoFromXG(earthQuakeInfoModel);
                highlightGraphic(g);
                naviPop.initPopWindow(R.layout.fragment_map, earthQuakeInfoModel);//
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL:
                // 得到所有地震信息
                handleGetEarthQuakeInfo(event);
                break;
            case ResponseEventStatus.EMERGENCY_GET_EQMONITORSTATION_ALL:
                // 得到所有监测点
                handleGetEQMonitorStation(event);
                break;
            case ResponseEventStatus.EMERGENCY_GET_REFUGE_INFO:
                // 得到所有避难所
                handleGetEQRefugeInfo(event);
                break;
            default:
                break;
        }
    }

    private boolean validate() {
        EQModuleConfig config = null;
        try {
            config = EQModuleConfig.getConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == config) {
            return false;
        }

        if (!config.isCZModuleUseable() && !config.isModuleUseable()) {
            return false;
        }
        //最多5分钟更新一次
        if ((System.currentTimeMillis() - lastUpdateTime) > 1000 * 60 * 5) {
            lastUpdateTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    private void handleGetEarthQuakeInfo(ResponseEvent event) {
        lastEQList = event.getData();
        SessionManager.quakeInfoList = event.getData();
        drawEarthQuakeInfo();
    }

    private void handleGetEQMonitorStation(ResponseEvent event) {
        lastEQStationList = event.getData();
        drawEQStation();
    }

    private void handleGetEQRefugeInfo(ResponseEvent event) {
        lastEQRefugeList = event.getData();
        drawEQRefuge();
    }


    private void drawEarthQuakeInfo() {
        if (!resumed) {
            return;
        }

        if (null == this.mapOperation || null == eqLayer) {
            return;
        }

        EarthQuakeDisplayUtil.drawEarthQuakesOnLayer(eqLayer, lastEQList);
    }

    private void drawEQStation() {
        if (!resumed) {
            return;
        }

        if (null == this.mapOperation || null == eqStationLayer) {
            return;
        }

        EarthQuakeDisplayUtil.drawEQStationsOnLayer(eqStationLayer, lastEQStationList);
    }

    private void drawEQRefuge() {
        if (!resumed) {
            return;
        }

        if (null == this.mapOperation || null == eqStationLayer) {
            return;
        }

        EarthQuakeDisplayUtil.drawEQRefugeOnLayer(eqRefugeLayer, lastEQRefugeList);
    }

    private Graphic drawEarthQuakeInfoFromXG(EarthQuakeInfoModel model) {
        if (!resumed) {
            return null;
        }

        if (null == this.mapOperation || null == eqLayer) {
            return null;
        }

        Graphic graphic = EarthQuakeDisplayUtil.drawEarthQuakesOnLayerFromXG(eqLayer, model);
        return graphic;
    }
}
