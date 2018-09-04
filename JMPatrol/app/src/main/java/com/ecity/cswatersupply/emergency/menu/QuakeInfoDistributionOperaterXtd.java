package com.ecity.cswatersupply.emergency.menu;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.EarthQuakeDisplayUtil;
import com.ecity.cswatersupply.emergency.fragment.EarthquakeFragment;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.SearchType;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.map.AMapViewOperator;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.MapNavigationPopView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

/**
 * 绘制地震信息操作类
 * @author Gxx 2016-11-24
 *
 */
public class QuakeInfoDistributionOperaterXtd extends AMapViewOperator {
    private WeakReference<IMapOperationContext> mOutersActivity;
    private MapActivity activity;
    private MapActivityTitleView titleView;
    private MapView mapView;
    private GraphicsLayer eqLayer;
    private GraphicsLayer annimationLayer;
    private GraphicFlash graphicFlash;
    private EarthQuakeInfoModel model;
    private Graphic graphic;
    private MapNavigationPopView popView;
    private IRequestParameter searchParameter;
    private ExecutorService executorService;

    public void setMapviewOption(MapView mapView, IMapOperationContext operationContext) {
        if (null == mapView) {
            return;
        }
        if (null == operationContext) {
            return;
        }
        mOutersActivity = new WeakReference<IMapOperationContext>(operationContext);
        this.mapView = mapView;
        activity = (MapActivity) mOutersActivity.get();
        popView = new MapNavigationPopView(operationContext);
        titleView = activity.getTitleView();
        titleView.setBtnStyle(BtnStyle.ONLY_BACK);
//        titleView.setLegendBackground(activity.getResources().getDrawable(R.drawable.btn_seacher_nor));
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        Intent intent = new Intent(activity, CustomReportActivity1.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.earth_quake_infor_search_title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, QuakeInfoSearchOperater.class.getName());
        bundle.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.ok);
        //SearchType.EARTH_QUAKE为地震信息查询；SearchType.QUICK_REPORT为速报信息查询
        bundle.putInt(Constants.EARTH_QUAKE_INFO_SEARCH_TYPE, SearchType.EARTH_QUAKE.getValue());
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, RequestCode.EARTH_QUAKE_INFO_SEARCH, bundle);
    }

    @Override
    public void notifyMapLoaded() {
        if(null == mapView || null == SessionManager.quakeInfoList) {
            return;
        }
        EventBusUtil.register(this);
        eqLayer = LayerTool.getGraphicsLayerByName(activity.getMapView(), EQModuleConfig.EQLAYERNAME);
        handleIntent();
        executorService = Executors.newScheduledThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                drawEarthQuakeInfo();
            }
        });
        initMapViewListener();
    }

    public void notifyBackEvent(IMapOperationContext operationContext) {
        EventBusUtil.unregister(this);
        if(null != executorService) {
            executorService.shutdown();
        }
        if (null != activity) {
            activity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ResultCode.EARTH_QUAKE_INFO_OK:
                getSearchResult(data);
                break;
            case ResultCode.EARTH_QUAKE_INFO_SEARCH_CANCEL:
                break;
            default:
                break;
        }
    }

    private void getSearchResult(Intent data) {
        searchParameter = (IRequestParameter) data.getSerializableExtra(Constants.EARTH_QUAKE_INFO_PARAM);
        if(null == searchParameter) {
            Toast.makeText(activity, R.string.no_earth_quake_infos, Toast.LENGTH_LONG).show();
            return;
        }
       //调用网络
       EmergencyService.getInstance().getEarthQuackeList(searchParameter, ResponseEventStatus.EMERGENCY_GET_DISTRION_QUAKE_INFO);
       LoadingDialogUtil.show(activity, R.string.str_searching);
    }

    private void initMapViewListener(){
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void onSingleTap(float arg0, float arg1) {
                try {
                    int[] gids = eqLayer.getGraphicIDs(arg0, arg1, 20, 1);
                    //没有点击地震时，停止高亮显示与详情显示
                    if(gids.length < 1) {
                        popView.dismiss();
                        graphicFlash.stopFlash();
                    }
                    if(gids.length == 1){
                        Graphic graphic = eqLayer.getGraphic(gids[0]);
                        highlightGraphic(graphic);
                        EarthQuakeInfoModel infoModel = getQuakeInfoPositionByGraphic(graphic);
                        if(null == infoModel) {
                            return;
                        }
                        
                        popView.initPopWindow(R.layout.activity_map,infoModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private EarthQuakeInfoModel getQuakeInfoPositionByGraphic(Graphic graphic) {
            int size = SessionManager.quakeInfoList.size();
            for(int i = 0; i < size; i++) {
                EarthQuakeInfoModel model = SessionManager.quakeInfoList.get(i);
                if(String.valueOf(model.getId()).equals(graphic.getAttributeValue("id"))) {
                    return model;
                }
            }
        return null;
    }

    private void handleIntent() {
        Intent intent = activity.getUserIntent();
        if (intent.hasExtra(Constants.EARTH_QUAKE_LIST_CLICK)) {
            model = (EarthQuakeInfoModel) intent.getSerializableExtra(Constants.EARTH_QUAKE_LIST_CLICK);
        }
        //点击listview的item时，地图上高亮显示点击的那一条地震信息
        if(null != model) {
            popView.initPopWindow(R.layout.activity_map, model);
            Map<String,Object> attr = new HashMap<String, Object>();
            attr.put("id", String.valueOf(model.getId()));
            attr.put("level", ""+ model.getML());
            Point pnt = new Point(model.getLongitude(), model.getLatitude());
            mapView.centerAt(pnt, true);
            Symbol symbol = EarthQuakeDisplayUtil.getEQMarkerSymbol(Double.valueOf(model.getML()));
            graphic = new Graphic(pnt,symbol,attr,0);
            //点击listview的item时，地图上高亮显示点击的那一条地震信息
            highlightGraphic(graphic);
        }
    }

    private void drawEarthQuakeInfo() {
        if (null == eqLayer) {
            return;
        }
        EarthQuakeDisplayUtil.drawEarthQuakesOnLayer(eqLayer, SessionManager.quakeInfoList);
    }

    private void highlightGraphic(Graphic result) {
        if (null == result) {
            return;
        }

        Graphic g_pipeNew = getHighlightGraphic(result);
        if (null == annimationLayer) {
            annimationLayer = LayerTool.getAnimationLayer(mapView);
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
                Symbol symbol = EarthQuakeDisplayUtil.buildEQMarkerSymbol(R.drawable.icon_eqlevel_green, Double.parseDouble(level), Color.GREEN);
                g = new Graphic(geometry, symbol, graphic.getAttributes(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    public void onEventMainThread(ResponseEvent event) {
        if(!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_DISTRION_QUAKE_INFO:
                handleGetEarthQuakeInfoList(event);
                break;
            default:
                break;
        }
    }

    private void handleGetEarthQuakeInfoList(ResponseEvent event) {
        SessionManager.quakeInfoList = event.getData();
        if(null == SessionManager.quakeInfoList) {
            Toast.makeText(activity, R.string.no_earth_quake_infos, Toast.LENGTH_LONG).show();
            return;
        }
        if(!isQuakeInfoExist(SessionManager.quakeInfoList, model)) {
            popView.dismiss();
            annimationLayer.removeAll();
            annimationLayer = null;
        }
        drawEarthQuakeInfo();
    }

    private boolean isQuakeInfoExist(List<EarthQuakeInfoModel> quakeInfoLists, EarthQuakeInfoModel model) {
        if(null == model) {
            return true;
        }
        for(EarthQuakeInfoModel mInfoModel : quakeInfoLists) {
            if(model.getId() == mInfoModel.getId()) {
                return true;
            }
        }
        return false;
    }
}
