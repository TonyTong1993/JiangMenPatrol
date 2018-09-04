package com.ecity.cswatersupply.menu.map;

import android.location.Location;
import android.view.View;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.PatrolBusInfo;
import com.ecity.cswatersupply.model.StatusInfoModel;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapNavigationPopView;
import com.ecity.cswatersupply.utils.ACache;
import com.ecity.cswatersupply.utils.BusDisplayUtil;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/***
 * 车辆监控操作类
 *
 */

public class BusMonitorOperatorXtd extends AMapViewOperator implements View.OnClickListener, PersonStatusControlOperator.OnStatusControlListener, SearchPatrolBusPopView.OnPopViewDismissListener {
    public final static String PATROL_BUS_LIST = "patrolBusList";
    private final static String PATROL_LAYER_NAME = "patrollayer";
    private final static String PATROL_TYPE_OFFLINE = "0";
    private final static String PATROL_TYPE_ONLINE = "1";
    private List<PatrolBusInfo> patrolBusList;
    private Map<String, Integer> uids;
    private WeakReference<IMapOperationContext> mOutersActivity;
    private MapView mapView;
    private static MapActivity activity;
    private MapActivityTitleView titleView;
    private MapNavigationPopView popView;
    private static GraphicsLayer patrolLayer;
    private GraphicsLayer annimationLayer;
    private GraphicFlash graphicFlash;
    private PersonStatusControlOperator operator;
    private ScheduledExecutorService backgroundService = null;
    public static boolean isStop = true;
    private List<StatusInfoModel> status;
    private final int interval_device = 5;// 设备状态监听 秒
    private PatrolBusInfo selectPatrolBus;
    private boolean reponsed;
    private SearchPatrolBusPopView mSearchPopView;
    private boolean isShowSearchResult = false;

    private void initStatusInfoModel() {
        status = new ArrayList<StatusInfoModel>();
        StatusInfoModel model = new StatusInfoModel(PATROL_TYPE_OFFLINE, true, 0);
        status.add(model);
        StatusInfoModel model1 = new StatusInfoModel(PATROL_TYPE_ONLINE, true, 0);
        status.add(model1);
    }

    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext operationContext) {
        if (null == mapView) {
            return;
        }
        if (null == operationContext) {
            return;
        }
        initStatusInfoModel();
        EventBusUtil.register(this);
        mOutersActivity = new WeakReference<IMapOperationContext>(operationContext);
        this.mapView = mapView;
        popView = new MapNavigationPopView(operationContext);
        activity = (MapActivity) mOutersActivity.get();
        uids = new HashMap<String, Integer>();
        patrolBusList = new ArrayList<PatrolBusInfo>();
        titleView = activity.getTitleView();
        titleView.setBtnStyle(MapActivityTitleView.BtnStyle.WITH_SEARCH);
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        if (null == operator) {
            operator = new PersonStatusControlOperator(this);
        }
        operator.showStatus(status, activity, PersonStatusControlOperator.MONITOR_OPERATOR_TYPE_PERSON);
    }

    @Override
    public void notifySearchBtnOnClicked(IMapOperationContext operationContext) {
        super.notifySearchBtnOnClicked(operationContext);
        List<String> searchDataList = constructPatrolUserNameList(patrolBusList);
        mSearchPopView = new SearchPatrolBusPopView(activity, searchDataList,this);
        mSearchPopView.showSearchPopView();
    }

    @Override
    public void notifyBackEvent(IMapOperationContext operationContext) {
        isStop = true;
        if (backgroundService != null) {
            backgroundService.shutdown();
        }
        backgroundService = null;

        EventBusUtil.unregister(this);
        patrolBusList.clear();
        uids.clear();
        if (null != operationContext) {
            operationContext.finish();
        }
    }

    @Override
    public void notifyMapLoaded() {
        patrolLayer = LayerTool.getGraphicsLayerByName(activity.getMapView(), PATROL_LAYER_NAME);
        showCachePatrolBus();
        requestPatrolBusIntervalOfMinutes();
        initMapViewListener();
    }

    private void showCachePatrolBus() {
        ACache aCache = ACache.get(activity);
        patrolBusList = (List<PatrolBusInfo>) aCache.getAsObject(PATROL_BUS_LIST);
        updateGraphics();
    }

    private void requestPatrolBusIntervalOfMinutes() {
        if (isStop) {
            reponsed = true;
            backgroundService = Executors.newSingleThreadScheduledExecutor();
            backgroundService.scheduleAtFixedRate(new TimerIncreasedRunnable(), 0, 1000 * interval_device, TimeUnit.MILLISECONDS);
            isStop = false;
        }
    }

    public void drawPatrolBus(List<PatrolBusInfo> patrolBusList) {
        int onlineNum = 0;
        int offlineNum = 0;

        if (null == patrolLayer) {
            Toast.makeText(activity, R.string.no_patrol_layer, Toast.LENGTH_SHORT).show();
            return;
        }

        if (null == patrolBusList || patrolBusList.size() == 0) {
            Toast.makeText(activity, R.string.no_patrol_bus_infos, Toast.LENGTH_SHORT).show();
            updateStatus(onlineNum, offlineNum);
            uids.clear();
            patrolLayer.removeAll();
            return;
        }

        for (int i = 0; i < patrolBusList.size(); i++) {
            final PatrolBusInfo bus = patrolBusList.get(i);

            if (PATROL_TYPE_ONLINE.equalsIgnoreCase(bus.getDeviceInfo()) && bus.isShowFromSearch()) {
                onlineNum++;
            } else {
                offlineNum++;
            }

            if (judgeDrawUserForStatus(bus.getDeviceInfo()) && bus.isShowFromSearch()) {
                drawBus(bus);
                resetHighLightGraphic(bus);
            } else {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if ((null != selectPatrolBus) && (bus.getGid().equals(selectPatrolBus.getGid()))) {
                            popView.dismiss();
                        }
                    }
                });
                removeBus(bus);
            }
        }

        updateStatus(onlineNum, offlineNum);
    }

    private void resetHighLightGraphic(final PatrolBusInfo bus) {
        if ((null != selectPatrolBus) && (bus.getGid().equals(selectPatrolBus.getGid()))) {
            Map<String, Object> attr = new HashMap<String, Object>();
            attr.put("id", String.valueOf(bus.getGid()));
            attr.put("busno", bus.getBusno());
            double[] xy = CoordTransfer.transToLocal(bus.getLat(), bus.getLng());

            if (null == xy || xy.length == 0) {
                return;
            }

            Point pnt = new Point(xy[0], xy[1]);
            if (null == bus.getUsername()) {
                return;
            }

            PictureMarkerSymbol symbol = BusDisplayUtil.getBusSymbol(bus.getBusno(), PATROL_TYPE_ONLINE.equals(bus.getDeviceInfo()));
            Graphic graphic = new Graphic(pnt, symbol, attr, null);
            highlightGraphic(graphic);

            popView.updatePopData(selectPatrolBus);
        }
    }

    private void updateGraphics() {
        drawPatrolBus(patrolBusList);
    }

    private void drawBus(PatrolBusInfo bus) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("id", String.valueOf(bus.getGid()));
        attr.put("busno", bus.getBusno());

        double[] xy = CoordTransfer.transToLocal(bus.getLat(), bus.getLng());
        if (null == xy || xy.length == 0) {
            return;
        }

        Point pnt = new Point(xy[0], xy[1]);
        if ("".equals(bus.getUsername())) {
            return;
        }
        PictureMarkerSymbol symbol = BusDisplayUtil.getBusSymbol(bus.getBusno(), PATROL_TYPE_ONLINE.equals(bus.getDeviceInfo()));
        Graphic g = new Graphic(pnt, symbol, attr, 0);
        int gid = -1;
        if (!uids.containsKey(bus.getGid())) {
            gid = patrolLayer.addGraphic(g);
            uids.put(bus.getGid(), gid);
        } else {
            patrolLayer.updateGraphic(uids.get(bus.getGid()), g);
        }
    }

    private void removeBus(PatrolBusInfo bus) {
        if (uids.containsKey(bus.getGid())) {
            try {
                patrolLayer.removeGraphic(uids.get(bus.getGid()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            uids.remove(bus.getGid());
        }
    }

    private void updateStatus(int onlineNum, int offlineNum) {
        int size = status.size();
        for (int i = 0; i < size; i++) {
            if (PATROL_TYPE_ONLINE.equalsIgnoreCase(status.get(i).getState())) {
                status.get(i).setNumber(onlineNum);
            } else {
                status.get(i).setNumber(offlineNum);
            }
        }
    }

    private boolean judgeDrawUserForStatus(String state) {
        if (StringUtil.isBlank(state)) {
            return false;
        }

        int size = status.size();
        for (int i = 0; i < size; i++) {
            if (state.equalsIgnoreCase(status.get(i).getState())) {
                return status.get(i).isDisplay();
            }
        }

        return false;
    }

    private void initMapViewListener() {
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSingleTap(float arg0, float arg1) {
                try {
                    int[] gids = patrolLayer.getGraphicIDs(arg0, arg1, 20, 1);
                    //没有点击地图时，停止高亮显示与详情显示
                    if (gids.length < 1) {
                        popView.dismiss();
                        graphicFlash.stopFlash();
                        selectPatrolBus = null;
                    }
                    if (gids.length == 1) {
                        Graphic graphic = patrolLayer.getGraphic(gids[0]);
                        highlightGraphic(graphic);
                        selectPatrolBus = getUserInfoByGraphic(graphic);
                        if (null == selectPatrolBus) {
                            return;
                        }

                        popView.initPopWindow(R.layout.activity_map, selectPatrolBus);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private PatrolBusInfo getUserInfoByGraphic(Graphic graphic) {
        int size = patrolBusList.size();
        for (int i = 0; i < size; i++) {
            PatrolBusInfo user = patrolBusList.get(i);
            if (String.valueOf(user.getGid()).equals(graphic.getAttributeValue("id"))) {
                return user;
            }
        }
        return null;
    }

    private void requestPatrolBus() {
        reponsed = false;
        String groupCode = HostApplication.getApplication().getCurrentUser().getGroupCode();
        boolean isMonitor = true;
        String busNo = null;
        UserService.getInstance().getPatrolBus(groupCode, busNo, isMonitor);
    }

    public void onEventMainThread(ResponseEvent event) {

        if (!event.isOK()) {
            reponsed = true;
            patrolBusList.clear();
            updateGraphics();
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.GET_PATROL_BUS:
                reponsed = true;
                if (!isShowSearchResult) {
                    handleGetPatrolBusDone(event);
                }
                break;
            default:
                break;
        }
    }

    public void handleGetPatrolBusDone(ResponseEvent event) {
        patrolBusList = event.getData();
        User currentUser = HostApplication.getApplication().getCurrentUser();
        Location location = PositionService.getLastLocation();
        for (PatrolBusInfo bus : patrolBusList) {
            bus.setShowFromSearch(true);
            if (bus.getGid().equals(currentUser.getId())) {
                bus.setDeviceInfo("1");
                bus.setLng(location.getLatitude());
                bus.setLat(location.getLongitude());
                break;
            }
        }
        updateGraphics();
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
                String busNo = String.valueOf(graphic.getAttributeValue("busno"));
                PictureMarkerSymbol symbol = BusDisplayUtil.getUserHighLightSymbol(busNo);
                g = new Graphic(geometry, symbol, graphic.getAttributes(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    @Override
    public void onPopViewDismissed(String patrolBusInfo) {
        mSearchPopView = null;
        if (patrolBusInfo != null) {
            showSearchPatrolBusInfo(patrolBusInfo);
        } else {
            if (isStop) {
                isStop = false;
                isShowSearchResult = false;
                showCachePatrolBus();
            }
        }
    }

    private void showSearchPatrolBusInfo(String patrolBusInfo) {
        String strArray[] = patrolBusInfo.split(" ");
        for (int i = 0; i < patrolBusList.size(); i++) {
            if (patrolBusList.get(i).getUsername().equalsIgnoreCase(strArray[0]) && patrolBusList.get(i).getBusno().equalsIgnoreCase(strArray[1])) {
                patrolBusList.get(i).setShowFromSearch(true);
                double[] localPoint = CoordTransfer.transToLocal(patrolBusList.get(i).getLat(), patrolBusList.get(i).getLng());
                showMyLocation(mapView, localPoint);
            } else {
                patrolBusList.get(i).setShowFromSearch(false);
            }
        }
        isStop = true;
        isShowSearchResult = true;
        updateGraphics();
    }

    private class TimerIncreasedRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (isStop || !reponsed) {
                    return;
                }
                requestPatrolBus();
            } catch (Throwable t) {
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        //获取轨迹
        //
    }

    @Override
    public void onStatusControlChanged(StatusInfoModel statusInfo) {
        if (null != statusInfo) {
            int size = status.size();
            for (int i = 0; i < size; i++) {
                if (statusInfo.getState().equalsIgnoreCase(status.get(i).getState())) {
                    status.get(i).setDisplay(statusInfo.isDisplay());
                }
            }
            updateGraphics();
        }
    }

    private List<String> constructPatrolUserNameList(List<PatrolBusInfo> sourceList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            list.add(sourceList.get(i).getUsername() + " " + sourceList.get(i).getBusno());
        }
        return list;
    }

    private void showMyLocation(MapView mapView, double[] localPoint) {

        Point point = new Point();
        if (null != localPoint && 2 == localPoint.length) {
            point.setX(localPoint[0]);
            point.setY(localPoint[1]);
        } else {
            Toast.makeText(activity, R.string.no_location, Toast.LENGTH_LONG).show();
            return;
        }
        mapView.centerAt(point, true);
    }

}

