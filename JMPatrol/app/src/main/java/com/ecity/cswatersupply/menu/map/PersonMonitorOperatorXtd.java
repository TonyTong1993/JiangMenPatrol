package com.ecity.cswatersupply.menu.map;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.map.PersonStatusControlOperator.OnStatusControlListener;
import com.ecity.cswatersupply.model.PatrolUser;
import com.ecity.cswatersupply.model.StatusInfoModel;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.MapNavigationPopView;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.UserDisplayUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

/**
 * 人员监控操作类
 * @author Gxx 2106-12-03
 */
public class PersonMonitorOperatorXtd extends AMapViewOperator implements OnClickListener, OnStatusControlListener {


    private final static String PATROL_LAYER_NAME = "patrollayer";
    private final static String PATROL_TYPE_OFFLINE = "0";
    private final static String PATROL_TYPE_ONLINE = "1";
    private List<PatrolUser> patrolMenList;
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
    private PatrolUser selectPatrolUser;
    private boolean reponsed;

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
        //popView = new MapNavigationPopView(operationContext,this);
        activity = (MapActivity) mOutersActivity.get();
        uids = new HashMap<String, Integer>();
        patrolMenList = new ArrayList<PatrolUser>();
        titleView = activity.getTitleView();
        titleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        if (null == operator) {
            operator = new PersonStatusControlOperator(this);
        }
        operator.showStatus(status, activity, PersonStatusControlOperator.MONITOR_OPERATOR_TYPE_PERSON);
    }

    @Override
    public void notifyBackEvent(IMapOperationContext operationContext) {
        isStop = true;
        if (backgroundService != null) {
            backgroundService.shutdown();
        }
        backgroundService = null;

        EventBusUtil.unregister(this);
        patrolMenList.clear();
        uids.clear();
        if (null != operationContext) {
            operationContext.finish();
        }
    }

    @Override
    public void notifyMapLoaded() {
        patrolLayer = LayerTool.getGraphicsLayerByName(activity.getMapView(), PATROL_LAYER_NAME);
        requestPatrolMenIntervalofMinutes();
        initMapViewListener();
    }

    private void requestPatrolMenIntervalofMinutes() {
        if (isStop) {
            reponsed = true;
            backgroundService = Executors.newSingleThreadScheduledExecutor();
            backgroundService.scheduleAtFixedRate(new TimerIncreasedRunnable(), 0, 1000 * interval_device, TimeUnit.MILLISECONDS);
            isStop = false;
        }
    }

    public void drawPatrolMen(List<PatrolUser> patrolMenList) {
        int onlineNum = 0;
        int offlineNum = 0;

        if (null == patrolLayer) {
            Toast.makeText(activity, R.string.no_patrol_layer, Toast.LENGTH_SHORT).show();
            return;
        }

        if (null == patrolMenList || patrolMenList.size() == 0) {
            Toast.makeText(activity, R.string.no_patrol_man_infos, Toast.LENGTH_SHORT).show();
            updateStatus(onlineNum, offlineNum);
            uids.clear();
            patrolLayer.removeAll();
            return;
        }

        for (int i = 0; i < patrolMenList.size(); i++) {
            final PatrolUser user = patrolMenList.get(i);

            if (PATROL_TYPE_ONLINE.equalsIgnoreCase(user.getState())) {
                onlineNum++;
            } else {
                offlineNum++;
            }

            if (judgeDrawUserForStatus(user.getState())) {
                drawUser(user);
                resetHighLightGraphic(user);
            } else {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if((null != selectPatrolUser) && (user.getId().equals(selectPatrolUser.getId()))) {
                            popView.dismiss();
                        }
                    }
                });
                removeUser(user);
            }
        }

        updateStatus(onlineNum, offlineNum);
    }

    private void resetHighLightGraphic(final PatrolUser user) {
        if ((null != selectPatrolUser) && (user.getId().equals(selectPatrolUser.getId()))) {
            Map<String, Object> attr = new HashMap<String, Object>();
            attr.put("id", String.valueOf(user.getId()));
            attr.put("name", user.getTrueName());
            double[] xy = CoordTransfer.transToLocal(user.getLatitude(), user.getLongitude());

            if (null == xy || xy.length == 0) {
                return;
            }

            Point pnt = new Point(xy[0], xy[1]);
            if (null == user.getTrueName()) {
                return;
            }

            PictureMarkerSymbol symbol = UserDisplayUtil.getUserSymbol(user.getTrueName(), PATROL_TYPE_ONLINE.equals(user.getState()));
            Graphic graphic = new Graphic(pnt, symbol, attr, null);
            highlightGraphic(graphic);

            popView.updatePopData(selectPatrolUser);
        }
    }

    private void updateGraphics() {
        drawPatrolMen(patrolMenList);
    }

    private void drawUser(PatrolUser user) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("id", String.valueOf(user.getId()));
        attr.put("name", user.getTrueName());

        double[] xy = CoordTransfer.transToLocal(user.getLatitude(), user.getLongitude());
        if (null == xy || xy.length == 0) {
            return;
        }

        Point pnt = new Point(xy[0], xy[1]);
        if("".equals(user.getTrueName())) {
            return;
        }
        PictureMarkerSymbol symbol = UserDisplayUtil.getUserSymbol(user.getTrueName(), PATROL_TYPE_ONLINE.equals(user.getState()));
        Graphic g = new Graphic(pnt, symbol, attr, 0);
        int gid = -1;
        if (!uids.containsKey(user.getId())) {
            gid = patrolLayer.addGraphic(g);
            uids.put(user.getId(), gid);
        } else {
            patrolLayer.updateGraphic(uids.get(user.getId()), g);
        }
    }

    private void removeUser(PatrolUser user) {
        if (uids.containsKey(user.getId())) {
            try {
                patrolLayer.removeGraphic(uids.get(user.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            uids.remove(user.getId());
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
                    //没有点击地震时，停止高亮显示与详情显示
                    if (gids.length < 1) {
                        popView.dismiss();
                        graphicFlash.stopFlash();
                        selectPatrolUser = null;
                    }
                    if (gids.length == 1) {
                        Graphic graphic = patrolLayer.getGraphic(gids[0]);
                        highlightGraphic(graphic);
                        selectPatrolUser = getUserInfoByGraphic(graphic);
                        if (null == selectPatrolUser) {
                            return;
                        }

                        popView.initPopWindow(R.layout.activity_map, selectPatrolUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private PatrolUser getUserInfoByGraphic(Graphic graphic) {
        int size = patrolMenList.size();
        for (int i = 0; i < size; i++) {
            PatrolUser user = patrolMenList.get(i);
            if (String.valueOf(user.getId()).equals(graphic.getAttributeValue("id"))) {
                return user;
            }
        }
        return null;
    }

    private void requestPatrolMen() {
        reponsed = false;
        UserService.getInstance().GetAllPatrolMan();
    }

    public void onEventMainThread(ResponseEvent event) {

        if (!event.isOK()) {
            reponsed = true;
            patrolMenList.clear();
            updateGraphics();
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.GET_ALL_PATROL_MAN:
                reponsed = true;
                handleGetPatrolMenDone(event);
                break;
            default:
                break;
        }
    }

    public void handleGetPatrolMenDone(ResponseEvent event) {
        patrolMenList = event.getData();
        User currentUser = HostApplication.getApplication().getCurrentUser();
        Location location = PositionService.getLastLocation();
        for(PatrolUser user : patrolMenList) {
            if(user.getId().equals(currentUser.getId())) {
                user.setState("1");
                user.setLatitude(location.getLatitude());
                user.setLongitude(location.getLongitude());
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
                String name = String.valueOf(graphic.getAttributeValue("name"));
                PictureMarkerSymbol symbol = UserDisplayUtil.getUserHighLightSymbol(name);
                g = new Graphic(geometry, symbol, graphic.getAttributes(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    private class TimerIncreasedRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (isStop || !reponsed) {
                    return;
                }
                requestPatrolMen();
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

}
