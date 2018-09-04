package com.ecity.cswatersupply.menu.map;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.planningtask.PointPartAttrAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.FZPointPartFeedbackModifyOperator;
import com.ecity.cswatersupply.menu.LinePartInPlaceFeedbackOperator;
import com.ecity.cswatersupply.menu.PointPartInPlaceFeedbackOperator;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.planningTask.ArriveInfo;
import com.ecity.cswatersupply.model.planningTask.LegendControl;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPolygonPart;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.service.PlanningTaskService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.FZFeedBackCustomReportActivity;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.activities.ReportEventTypeSelectActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.ui.activities.planningtask.TaskAttrListsActivity;
import com.ecity.cswatersupply.ui.widght.MapLegendView;
import com.ecity.cswatersupply.ui.widght.MapNavigationFlexflowView;
import com.ecity.cswatersupply.utils.BitmapUtils;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CustomRenderer;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.PlanTaskUtils;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.z3app.android.util.StringUtil;

/**
 * 养护任务
 *
 * @author maliu
 */
@SuppressLint("NewApi")
public class FleflowReportPlanningTaskOperatorXtd extends AMapViewOperator {

    //轨迹线标识
    public static String TRACKLINEID = "00001";
    //到位线表示
    public static String ARRIVEDLINEID = "00002";
    private final static int MMAPLEGENDVIEW_SHOW = 0;
    private final static int MAPNAVIGATIONFLEXFLOWVIEW_SHOW = 1;
    private final static int POINT_ARRIVIED = 2;
    private final static int POINT_FEEDBACK = 3;
    //获取设备属性  判断是显示全部属性还是弹出底部功能框 false为 弹出功能框 true为显示所有的属性信息
    private static boolean isshow_infodetails = false;
    private WeakReference<IMapOperationContext> mOutersActivity;
    private WeakReference<MapView> mOutersMapView;
    public Z3PlanTask plantask;
    public MapActivity activity;
    MapView mapView;
    // 标记是否是第一次加载图层
    public boolean isFristLoad = true;
    // 样式集合
    // 没到位 没反馈 点
    private PictureMarkerSymbol POINT_COMMON_MARKER_SYMBOL;
    // 没到位 没反馈 点
    private PictureMarkerSymbol POINT_HIGHLIGHT_MARKER_SYMBOL;
    // 已到位 没反馈 点
    private PictureMarkerSymbol POINT_ARRIVED_MARKER_SYMBOL;
    // 已到位 已反馈 点
    private PictureMarkerSymbol POINT_FEEDBACK_MARKER_SYMBOL;
    private PictureMarkerSymbol POINT_OTHER_USER_ARRIVED_SYMBOL;

    private SimpleLineSymbol LINE_COMMON_MARKER_SYMBOL;
    private SimpleLineSymbol LINE_ARRIVED_MARKER_SYMBOL;
    private SimpleLineSymbol LINE_FEEDBACK_MARKER_SYMBOL;
    private SimpleFillSymbol POLYGON_MARKER_SYMBOL;
    private SimpleLineSymbol POLYGON_OUTLINE_SYMBOL;
    private SimpleLineSymbol TRACK_LINE_SYMBOL;
    private final int markerSizeWidth = 20;
    private final int markerSizeHeight = 28;
    private MapNavigationFlexflowView ffMapNavigationView;
    private MapLegendView mMapLegendView;
    //    public ArrayList<LegendInfo> infos;
    private boolean isdrawing = false;
    public Z3PlanTaskPointPart pointPartClicked;
    public Z3PlanTaskLinePart linePartClicked;
    private GraphicsLayer annimationLayer;
    private GraphicFlash graphicFlash;
    //存储添加到GraphicsLayer的gid，以便更新Graphic
    private Map<String, Integer> feedbackGrahicGids;
    private Map<String, Integer> markGrahicGids;
    //线图例参数  begin
    //所有的点在图层上返回的uid
    public HashMap<String, Integer> pointGidMap;
    //计划路线在图层上的uid
    public HashMap<String, Integer> planLineGidMap;
    //行走轨迹在图层上的uid
    public HashMap<String, Integer> trackLineGidMap;
    //行走轨迹在计划线上覆盖之后形成的线在图层上的uid
    public HashMap<String, Integer> arrivedLineGidMap;
    //线所有需要控制显示的map集合
    public List<LegendControl> linePlanTaskGidMapslist;
    //map集合对应的key值
    public String[] linePlanTaskGidsMapKeys = new String[]{"pointGidMap", "planLineGidMap", "trackLineGidMap", "arrivedLineGidMap"};
    //线图例参数  end

    //点图例参数 begin
    //未到位点 gid
    public HashMap<String, Integer> commenPointGidMap;
    //到位但是未反馈点gid
    public HashMap<String, Integer> arrivedPointGidMap;
    //反馈点gid
    public HashMap<String, Integer> feedbackPointGidMap;
    //点所有需要控制显示的map集合
    public List<LegendControl> pointPlanTaskGidMapslist;
    //map集合对应的key值
    public String[] pointPlanTaskGidsMapKeys = new String[]{"commenPointGidMap", "arrivedPointGidMap", "feedbackPointGidMap"};
    //点图例参数 end

    //当前task属于点任务还是线任务
    public boolean isPointTask = true;
    //是否加载过图例
    private boolean isLegentLoaded = false;
    private GraphicsLayer graphicsLayer;

    @SuppressLint("UseSparseArrays")
    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext mapActivity) {
        if (null == mapView) {
            return;
        }
        graphicsLayer = LayerTool.getGraphicsLayer(mapView);

        mOutersActivity = new WeakReference<IMapOperationContext>(mapActivity);
        mOutersMapView = new WeakReference<MapView>(mapView);
        activity = (MapActivity) mOutersActivity.get();

        ffMapNavigationView = activity.getFlexflowMapNavigationView();
        if (HostApplication.getApplication().getProjectStyle() == HostApplication.ProjectStyle.PROJECT_FZXJ) {
            ffMapNavigationView.ib_flexflow_show_line.setVisibility(View.GONE);
        }

        mMapLegendView = activity.getmMapLegendView();

        POINT_COMMON_MARKER_SYMBOL = CustomRenderer.getPictureMarkerSymbolByResuorceID(R.drawable.map_location_red, markerSizeWidth, markerSizeHeight);
        POINT_ARRIVED_MARKER_SYMBOL = CustomRenderer.getPictureMarkerSymbolByResuorceID(R.drawable.map_location_hl, markerSizeWidth, markerSizeHeight);
        POINT_FEEDBACK_MARKER_SYMBOL = CustomRenderer.getPictureMarkerSymbolByResuorceID(R.drawable.map_location_blue, markerSizeWidth, markerSizeHeight);
        POINT_HIGHLIGHT_MARKER_SYMBOL = CustomRenderer.getPictureMarkerSymbolByResuorceID(R.drawable.map_location_green, markerSizeWidth, markerSizeHeight);
        POINT_OTHER_USER_ARRIVED_SYMBOL = CustomRenderer.getPictureMarkerSymbolByResuorceID(R.drawable.map_location_hl, markerSizeWidth, markerSizeHeight);

        LINE_COMMON_MARKER_SYMBOL = new SimpleLineSymbol(activity.getContext().getResources().getColor(R.color.red), 14, SimpleLineSymbol.STYLE.SOLID);
        LINE_ARRIVED_MARKER_SYMBOL = new SimpleLineSymbol(activity.getContext().getResources().getColor(R.color.purple), 6, SimpleLineSymbol.STYLE.SOLID);
        LINE_FEEDBACK_MARKER_SYMBOL = new SimpleLineSymbol(Color.GREEN, 10, SimpleLineSymbol.STYLE.SOLID);

        TRACK_LINE_SYMBOL = new SimpleLineSymbol(activity.getContext().getResources().getColor(R.color.track_line_color), 8, SimpleLineSymbol.STYLE.SOLID);
        POLYGON_MARKER_SYMBOL = new SimpleFillSymbol(activity.getContext().getResources().getColor(R.color.geo_fill_color), com.esri.core.symbol.SimpleFillSymbol.STYLE.SOLID);
        POLYGON_OUTLINE_SYMBOL = new SimpleLineSymbol(activity.getContext().getResources().getColor(R.color.geo_outline_color), 2, SimpleLineSymbol.STYLE.DASHDOT);

        feedbackGrahicGids = new HashMap<String, Integer>();
        markGrahicGids = new HashMap<String, Integer>();

        pointGidMap = new HashMap<String, Integer>();
        planLineGidMap = new HashMap<String, Integer>();
        trackLineGidMap = new HashMap<String, Integer>();
        arrivedLineGidMap = new HashMap<String, Integer>();
        linePlanTaskGidMapslist = new ArrayList<LegendControl>();

        commenPointGidMap = new HashMap<String, Integer>();
        arrivedPointGidMap = new HashMap<String, Integer>();
        feedbackPointGidMap = new HashMap<String, Integer>();
        pointPlanTaskGidMapslist = new ArrayList<LegendControl>();

        mapView.setOnSingleTapListener(m_OnSingleTapListener);
        mapView.setOnLongPressListener(null);

        EventBusUtil.register(this);
    }

    @Override
    public void notifyMapLoaded() {
        if (!isdrawing) {
            isdrawing = true;
            mapView = mOutersMapView.get();
            drawImageLayer();
        }
    }

    // 画图像层 开始进入地图界面
    public void drawImageLayer() {
        if (null == activity || null == mapView) {
            return;
        }
        if (SessionManager.currentTaskIntMapOpretor == null) {
            return;
        }

        plantask = SessionManager.currentTaskIntMapOpretor;
        isPointTask = SessionManager.currentTaskIntMapOpretor.getLinelen() <= 0;

        graphicsLayer.removeAll();
        drawGeomotry(SessionManager.currentTaskIntMapOpretor);
    }

    private void getLinePlanTaskLegendControls() {
        LegendControl pointLegendControl = null;
        LegendControl planLineLegendControl = null;
        LegendControl trackLineLegendControl = null;
        LegendControl arrivedLineLegendControl = null;
        if (ListUtil.isEmpty(linePlanTaskGidMapslist)) {
            pointLegendControl = new LegendControl(pointGidMap, true, linePlanTaskGidsMapKeys[0]);
            planLineLegendControl = new LegendControl(planLineGidMap, true, linePlanTaskGidsMapKeys[1]);
            trackLineLegendControl = new LegendControl(trackLineGidMap, true, linePlanTaskGidsMapKeys[2]);
            arrivedLineLegendControl = new LegendControl(arrivedLineGidMap, true, linePlanTaskGidsMapKeys[3]);
        } else {
            for (LegendControl lc : linePlanTaskGidMapslist) {
                if (lc.getKey().equalsIgnoreCase(linePlanTaskGidsMapKeys[0])) {
                    pointLegendControl = new LegendControl(pointGidMap, lc.isVisible(), linePlanTaskGidsMapKeys[0]);
                }
                if (lc.getKey().equalsIgnoreCase(linePlanTaskGidsMapKeys[1])) {
                    planLineLegendControl = new LegendControl(planLineGidMap, lc.isVisible(), linePlanTaskGidsMapKeys[1]);
                }
                if (lc.getKey().equalsIgnoreCase(linePlanTaskGidsMapKeys[2])) {
                    trackLineLegendControl = new LegendControl(trackLineGidMap, lc.isVisible(), linePlanTaskGidsMapKeys[2]);
                }
                if (lc.getKey().equalsIgnoreCase(linePlanTaskGidsMapKeys[3])) {
                    arrivedLineLegendControl = new LegendControl(arrivedLineGidMap, lc.isVisible(), linePlanTaskGidsMapKeys[3]);
                }
            }
        }
        linePlanTaskGidMapslist.add(pointLegendControl);
        linePlanTaskGidMapslist.add(planLineLegendControl);
        linePlanTaskGidMapslist.add(trackLineLegendControl);
        linePlanTaskGidMapslist.add(arrivedLineLegendControl);
    }

    private void getPointPlanTaskLegendControls() {
        LegendControl commenPointLegendControl = null;
        LegendControl arrivedPointLegendControl = null;
        LegendControl feedbackPointLegendControl = null;
        if (ListUtil.isEmpty(pointPlanTaskGidMapslist)) {
            commenPointLegendControl = new LegendControl(commenPointGidMap, true, pointPlanTaskGidsMapKeys[0]);
            arrivedPointLegendControl = new LegendControl(arrivedPointGidMap, true, pointPlanTaskGidsMapKeys[1]);
            feedbackPointLegendControl = new LegendControl(feedbackPointGidMap, true, pointPlanTaskGidsMapKeys[2]);
        } else {
            for (LegendControl lc : pointPlanTaskGidMapslist) {
                if (lc.getKey().equalsIgnoreCase(pointPlanTaskGidsMapKeys[0])) {
                    commenPointLegendControl = new LegendControl(commenPointGidMap, lc.isVisible(), pointPlanTaskGidsMapKeys[0]);
                }
                if (lc.getKey().equalsIgnoreCase(pointPlanTaskGidsMapKeys[1])) {
                    arrivedPointLegendControl = new LegendControl(arrivedPointGidMap, lc.isVisible(), pointPlanTaskGidsMapKeys[1]);
                }
                if (lc.getKey().equalsIgnoreCase(pointPlanTaskGidsMapKeys[2])) {
                    feedbackPointLegendControl = new LegendControl(feedbackPointGidMap, lc.isVisible(), pointPlanTaskGidsMapKeys[2]);
                }
            }
        }
        pointPlanTaskGidMapslist.add(commenPointLegendControl);
        pointPlanTaskGidMapslist.add(arrivedPointLegendControl);
        pointPlanTaskGidMapslist.add(feedbackPointLegendControl);
    }

    @Override
    public void notifyActionEvent(IMapOperationContext operationContext) {
        isLegentLoaded = true;
        if (!isPointTask) {
            getLinePlanTaskLegendControls();
            LegendControl4LinePlanTaskOperatorXtd operator = new LegendControl4LinePlanTaskOperatorXtd();
            operator.setGidMapslist(linePlanTaskGidMapslist);
            operator.execute(mapView, activity);
        } else {
            getPointPlanTaskLegendControls();
            LegendControl4PointPlanTaskOperatorXtd operator = new LegendControl4PointPlanTaskOperatorXtd();
            operator.setPointPlanTaskGidMapslist(pointPlanTaskGidMapslist);
            operator.execute(mapView, activity);
        }
    }

    //控制头部view和底部view的显示 
    private void ControlViewDisplay(int view_show) {
        switch (view_show) {
            case MMAPLEGENDVIEW_SHOW:
                ffMapNavigationView.setVisibility(View.GONE);
                if (null != graphicFlash) {
                    graphicFlash.stopFlash();
                    graphicFlash = null;
                }
                mMapLegendView.setVisibility(View.VISIBLE);
                break;
            case MAPNAVIGATIONFLEXFLOWVIEW_SHOW:
                ffMapNavigationView.setVisibility(View.VISIBLE);
                mMapLegendView.setVisibility(View.GONE);
                break;

            default:
                break;
        }

    }

    // 画到位点
    public void drawArrivedPoint(ArriveInfo info) {

        if (null == info) {
            return;
        }
        Z3PlanTaskPointPart pointPart = (Z3PlanTaskPointPart) info.getmZ3PlanTaskPart();
        if (!pointPart.isArrive()) {
            return;
        }
        Map<String, Object> mapForPlanId = new HashMap<String, Object>();
        mapForPlanId.put("gid", pointPart.getGid());
        Point point = (Point) PlanTaskUtils.buildGeometryFromJSON(pointPart.getGeom());
        Graphic g = new Graphic(point, POINT_ARRIVED_MARKER_SYMBOL, mapForPlanId, null);
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        if (isPointTask) {
            addOneGrahic(pointPart, graphicsLayer, g, arrivedPointGidMap);
        } else {
            addOneGrahic(pointPart, graphicsLayer, g, pointGidMap);
        }
    }

    // 画反馈点
    public void drawFeedBackPoint(Z3PlanTaskPointPart pointpart) {

        if (null == pointpart || null == pointPartClicked || null == activity) {
            return;
        }
        if (!pointpart.isFeedBack()) {
            return;
        }
        Map<String, Object> mapForPlanId = new HashMap<String, Object>();
        mapForPlanId.put("gid", pointpart.getGid());
        Point point = (Point) PlanTaskUtils.buildGeometryFromJSON(pointpart.getGeom());
        Graphic g = new Graphic(point, POINT_FEEDBACK_MARKER_SYMBOL, mapForPlanId, null);

        String pointTpyeAndId = "";

        try {
            if ((pointPartClicked.getType() == null || pointPartClicked.getType().equalsIgnoreCase("0")) || (null == pointPartClicked.getEquipid() || pointPartClicked.getEquipid().equals("null"))) {
                pointTpyeAndId = activity.getResources().getString(R.string.planningtask_none_device_point);
            } else {
                pointTpyeAndId = pointPartClicked.getType() + "_" + pointPartClicked.getEquipid();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String statusStr = pointTpyeAndId + "(" + activity.getResources().getString(R.string.planningtask_arrived) + "_"
                + activity.getResources().getString(R.string.planningtask_has_feedback) + ")";
        ffMapNavigationView.setBackUpBtnEnable(false);
        ffMapNavigationView.setStatusText(statusStr);
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        if (isPointTask) {
            addOneGrahic(pointpart, graphicsLayer, g, feedbackPointGidMap);
        } else {
            addOneGrahic(pointpart, graphicsLayer, g, pointGidMap);
        }
    }

    // 画覆盖线
    public void drawArrivedLine(ArriveInfo info) {

        if (null == info) {
            return;
        }
        Z3PlanTaskLinePart linePart = (Z3PlanTaskLinePart) info.getmZ3PlanTaskPart();
        //得到当前的覆盖线
        Polyline arriveLine = (Polyline) info.getmGeometry();
        if (null == arriveLine) {
            return;
        }
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        // 附加特别的属性
        Map<String, Object> mapForPlanId = new HashMap<String, Object>();
        mapForPlanId.put("gid", linePart.getGid());
        Graphic g = new Graphic(arriveLine, LINE_ARRIVED_MARKER_SYMBOL, mapForPlanId, null);
        addGrahic(linePart, graphicsLayer, g, arrivedLineGidMap);
    }

    // 画反馈线
    public void drawFeedBackLine(Z3PlanTaskLinePart linePart) {

        if (null == linePart) {
            return;
        }
        Polyline arriveLine = (Polyline) PlanTaskUtils.buildGeometryFromJSON(linePart.getGeom());
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        // 附加特别的属性
        Map<String, Object> mapForPlanId = new HashMap<String, Object>();
        mapForPlanId.put("gid", linePart.getGid());
        Graphic g = new Graphic(arriveLine, LINE_FEEDBACK_MARKER_SYMBOL, mapForPlanId, null);
        addGrahic(linePart, graphicsLayer, g, feedbackGrahicGids);
        ffMapNavigationView.setBackUpBtnEnable(false);
    }

    // 画人员轨迹线
    public void drawTrackLine(Polyline trackLine) {
        mapView = mOutersMapView.get();
        if (null == activity || null == mapView) {
            return;
        }
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        Graphic pointG = new Graphic(trackLine, TRACK_LINE_SYMBOL, null, null);
        if (trackLineGidMap.containsKey(TRACKLINEID)) {
            graphicsLayer.updateGraphic(trackLineGidMap.get(TRACKLINEID), pointG);
        } else {
            int uid = graphicsLayer.addGraphic(pointG);
            trackLineGidMap.put(TRACKLINEID, uid);
        }
    }

    // 点多的时候调用
    public List<Graphic> getMultPointGraphics(final ArrayList<Z3PlanTaskPointPart> pointPartList, final Z3PlanTask task) {

        if (!ListUtil.isEmpty(pointPartList)) {
            List<Graphic> pointPartGraphicList = new ArrayList<Graphic>();
            for (int j = 0; j < pointPartList.size(); j++) {
                final int index = j;
                Z3PlanTaskPointPart pointPart = pointPartList.get(index);
                Geometry geo = PlanTaskUtils.buildGeometryFromJSON(pointPart.getGeom());
                Point point = (Point) geo;
                // 附加特别的属性
                Map<String, Object> mapForPlanId = new HashMap<String, Object>();
                mapForPlanId.put("gid", pointPart.getGid());
                Graphic g = null;
                if (pointPart.isFeedBack() && pointPart.isArrive()) {
                    g = new Graphic(point, POINT_FEEDBACK_MARKER_SYMBOL, mapForPlanId, null);
                } else if ((!pointPart.isFeedBack()) && pointPart.isArrive()) {
                    g = new Graphic(point, POINT_ARRIVED_MARKER_SYMBOL, mapForPlanId, null);
                } else if ((!pointPart.isFeedBack()) && (!pointPart.isArrive())) {
                    g = new Graphic(point, POINT_COMMON_MARKER_SYMBOL, mapForPlanId, null);
                } else {
                    g = new Graphic(point, POINT_COMMON_MARKER_SYMBOL, mapForPlanId, null);
                }
                pointPartGraphicList.add(g);

            }
            return pointPartGraphicList;
        }
        return null;
    }

    // 设置地图的地图信息
    private void setMapExtent(List<Z3PlanTaskPointPart> pointPartlist) {
        if (ListUtil.isEmpty(pointPartlist)) {
            return;
        }
        Point minP = getXY(pointPartlist, true);
        Point maxP = getXY(pointPartlist, false);
        SessionManager.lastInitExtent = new Envelope(minP.getX() - 50, minP.getY() - 50, maxP.getX() + 50, maxP.getY() + 50);
        mapView.setExtent(SessionManager.lastInitExtent);
    }

    private Point getXY(List<Z3PlanTaskPointPart> pointPartlist, boolean isForMin) {
        Point point0 = (Point) PlanTaskUtils.buildGeometryFromJSON(pointPartlist.get(0).getGeom());
        double x = point0.getX();
        double y = point0.getY();
        if (isForMin) {
            for (int i = 1; i < pointPartlist.size(); i++) {
                Point point = (Point) PlanTaskUtils.buildGeometryFromJSON(pointPartlist.get(i).getGeom());
                double pointPartX = point.getX();
                if (x > pointPartX) {
                    x = pointPartX;
                }
                double pointPartY = point.getY();
                if (y > pointPartY) {
                    y = pointPartY;
                }
            }
            return new Point(x, y);
        } else {
            for (int i = 1; i < pointPartlist.size(); i++) {
                Point point = (Point) PlanTaskUtils.buildGeometryFromJSON(pointPartlist.get(i).getGeom());
                double pointPartX = point.getX();
                if (pointPartX > x) {
                    x = pointPartX;
                }
                double pointPartY = point.getY();
                if (pointPartY > y) {
                    y = pointPartY;
                }
            }
            return new Point(x, y);
        }
    }

    // 画图
    public void drawGeomotry(Z3PlanTask task) {
        // 点
        ArrayList<Z3PlanTaskPointPart> pointPartList = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_ALL,
                task.getTaskid());
        setMapExtent(pointPartList);
        List<Graphic> pointPartGraphicList = getMultPointGraphics(pointPartList, task);
        if (ListUtil.isEmpty(pointPartGraphicList) == false) {
            Graphic[] pointPartGraphicArray = new Graphic[pointPartGraphicList.size()];
            for (int i = 0; i < pointPartGraphicList.size(); i++) {
                pointPartGraphicArray[i] = pointPartGraphicList.get(i);
            }
            if (isPointTask) {
                addGrahicArrays(pointPartList, graphicsLayer, pointPartGraphicArray, null);
            } else {
                addGrahicArrays(pointPartList, graphicsLayer, pointPartGraphicArray, pointGidMap);
            }
        }

        // 线
        ArrayList<Z3PlanTaskLinePart> linesPartList = (ArrayList<Z3PlanTaskLinePart>) PlanningTaskManager.getInstance().getLines(task.getTaskid());
        if (null != linesPartList && linesPartList.size() > 0) {
            for (int j = 0; j < linesPartList.size(); j++) {
                // 画一条线
                Z3PlanTaskLinePart linePart = linesPartList.get(j);
                Geometry geo = PlanTaskUtils.buildGeometryFromJSON(linePart.getGeom());
                Polyline line = (Polyline) geo;
                GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
                // 附加特别的属性
                Map<String, Object> mapForPlanId = new HashMap<String, Object>();
                mapForPlanId.put("gid", linePart.getGid());
                Graphic g = new Graphic(line, LINE_COMMON_MARKER_SYMBOL, mapForPlanId, null);

                addGrahic(linePart, graphicsLayer, g, planLineGidMap);
                if (linePart.isFeedBack() && linePart.isCovered()) {
                    g = new Graphic(line, LINE_FEEDBACK_MARKER_SYMBOL, mapForPlanId, null);
                    addGrahic(linePart, graphicsLayer, g, feedbackGrahicGids);
                }
                //                if ((!linePart.isFeedBack()) && (!linePart.isCovered())) {
                //                    Graphic pointG = getMarkerGraphic(task.getTaskid() + "_" + linePart.getGid(), line, mapForPlanId);
                //                    addGrahic(linePart, graphicsLayer, pointG, markGrahicGids);
                //                }
            }
        }

        // 面
        ArrayList<Z3PlanTaskPolygonPart> polygonPartList = (ArrayList<Z3PlanTaskPolygonPart>) PlanningTaskManager.getInstance().getPolygon(task.getTaskid());
        if (null != polygonPartList && polygonPartList.size() > 0) {
            for (int j = 0; j < polygonPartList.size(); j++) {
                Z3PlanTaskPolygonPart polygonPart = polygonPartList.get(j);
                Geometry geo = PlanTaskUtils.buildGeometryFromJSON(polygonPart.getGeom());
                Polygon gon = (Polygon) geo;
                GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
                POLYGON_MARKER_SYMBOL.setOutline(POLYGON_OUTLINE_SYMBOL);
                // 附加特别的属性
                Map<String, Object> mapForPlanId = new HashMap<String, Object>();
                mapForPlanId.put("planid", task.getPlanid());
                Graphic gp = new Graphic(gon, POLYGON_MARKER_SYMBOL, mapForPlanId, null);
                graphicsLayer.addGraphic(gp);
                Point centerPoint = GeometryUtil.GetGeometryCenter(gon);
                @SuppressWarnings("deprecation")
                PictureMarkerSymbol pSynbol = new PictureMarkerSymbol(new BitmapDrawable(BitmapUtils.drawTextToBitmap2(HostApplication.getApplication(), R.drawable.picture_bg,
                        task.getPlanname(), activity.getResources().getColor(R.color.geo_text_color))));
                Graphic pointG = new Graphic(centerPoint, pSynbol, mapForPlanId, null);
                graphicsLayer.addGraphic(pointG);
            }
        }
    }

    private Graphic getMarkerGraphic(String taskidAndGid, Polyline line, Map<String, Object> mapForPlanId) {
        Point centerPoint = GeometryUtil.GetGeometryCenter(line);
        PictureMarkerSymbol pSynbol = new PictureMarkerSymbol(new BitmapDrawable(BitmapUtils.drawTextToBitmap2(HostApplication.getApplication(), R.drawable.picture_bg,
                taskidAndGid, activity.getResources().getColor(R.color.geo_text_color))));
        Graphic pointG = new Graphic(centerPoint, pSynbol, mapForPlanId, null);
        return pointG;
    }

    private void addGrahic(Z3PlanTaskLinePart linePart, GraphicsLayer graphicsLayer, Graphic g, Map<String, Integer> grahicGids) {
        if (grahicGids.containsKey(linePart.getTaskid() + "_" + linePart.getGid())) {
            graphicsLayer.updateGraphic(grahicGids.get(linePart.getTaskid() + "_" + linePart.getGid()), g);
        } else {
            int gid = graphicsLayer.addGraphic(g);
            grahicGids.put(linePart.getTaskid() + "_" + linePart.getGid(), gid);
        }
    }

    private void addGrahicArrays(ArrayList<Z3PlanTaskPointPart> pointParts, GraphicsLayer graphicsLayer, Graphic[] g, Map<String, Integer> grahicGids) {
        for (int i = 0; i < pointParts.size(); i++) {
            Z3PlanTaskPointPart pointPart = pointParts.get(i);
            if (null != grahicGids) {
                addOneGrahic(pointPart, graphicsLayer, g[i], grahicGids);
            } else {
                if (!pointPart.isArrive()) {
                    addOneGrahic(pointPart, graphicsLayer, g[i], commenPointGidMap);
                } else if (!pointPart.isFeedBack() && pointPart.isArrive()) {
                    addOneGrahic(pointPart, graphicsLayer, g[i], arrivedPointGidMap);
                } else {
                    addOneGrahic(pointPart, graphicsLayer, g[i], feedbackPointGidMap);
                }
            }
        }
    }

    private void addOneGrahic(Z3PlanTaskPointPart pointPart, GraphicsLayer graphicsLayer, Graphic g, Map<String, Integer> grahicGids) {
        if (!isPointTask && grahicGids.containsKey(pointPart.getTaskid() + "_" + pointPart.getGid())) {
            int uid = graphicsLayer.addGraphic(g);
            graphicsLayer.updateGraphic(grahicGids.get(pointPart.getTaskid() + "_" + pointPart.getGid()), g);
            grahicGids.put(pointPart.getTaskid() + "_" + pointPart.getGid(), uid);
        } else {
            int uid = graphicsLayer.addGraphic(g);
            grahicGids.put(pointPart.getTaskid() + "_" + pointPart.getGid(), uid);
            if (pointPart.isArrive() && !pointPart.isFeedBack()) {
                deleteOneGrahic(pointPart, graphicsLayer, commenPointGidMap);
            } else if (pointPart.isFeedBack()) {
                deleteOneGrahic(pointPart, graphicsLayer, arrivedPointGidMap);
            }
        }
    }

    private void deleteOneGrahic(Z3PlanTaskPointPart pointPart, GraphicsLayer graphicsLayer, Map<String, Integer> grahicGids) {
        if (grahicGids.containsKey(pointPart.getTaskid() + "_" + pointPart.getGid())) {
            graphicsLayer.removeGraphic(grahicGids.get(pointPart.getTaskid() + "_" + pointPart.getGid()));
        }
    }

    @SuppressWarnings("serial")
    OnSingleTapListener m_OnSingleTapListener = new OnSingleTapListener() {

        public void onSingleTap(float x, float y) {

            if (null == mapView || !mapView.isLoaded()) {
                return;
            }
            //去掉闪烁
            if (null != graphicFlash) {
                graphicFlash.stopFlash();
                graphicFlash = null;
            }
            //影藏底部弹出框
            ffMapNavigationView.setVisibility(View.GONE);
            selectOneGraphic(x, y);
        }

        private void selectOneGraphic(float x, float y) {
            // 获得图层
            GraphicsLayer layer = LayerTool.getGraphicsLayer(mapView);
            if ((layer != null && layer.isInitialized() && layer.isVisible())) {
                Graphic result = getGraphicsFromLayer(x, y, layer);
                //测试点 7参数获取错误将造成模拟点坐标不正确
               /* Point point = mapView.toMapPoint(x, y);
                double[] lonlat = CoordTransfer.transToLatlon(point.getX(), point.getY());
                LogUtil.i("point", "{" + lonlat[0] + "," + lonlat[1] + "}");*/
                if (result != null) {
                    // 获得附加特别的属性
                    if (null == result.getAttributeValue("gid")) {
                        return;
                    }
                    int gid = Integer.valueOf(String.valueOf(result.getAttributeValue("gid")));
                    renderMethod(layer, result);

                    // 根据planid 判断到位反馈情况
                    pointPartClicked = PlanningTaskManager.getInstance().getPlanTaskPointPart(plantask.getTaskid(), gid);
                    linePartClicked = PlanningTaskManager.getInstance().getPlanTaskLinePart(plantask.getTaskid(), gid);
                    if ((null == pointPartClicked) && (null == linePartClicked)) {
                        ToastUtil.showLong(activity.getResources().getString(R.string.get_point_error));
                        return;
                    } else {
                        if (null != pointPartClicked) {
                            isshow_infodetails = false;
                            getpPointInfoDetails();

                        }
                        if (null != linePartClicked) {
                            handleClickLine(linePartClicked);
                        }
                    }

                    ControlViewDisplay(MAPNAVIGATIONFLEXFLOWVIEW_SHOW);
                    ffMapNavigationView.setOnNavigationListener(activity.new GoOnClickListener());
                    try {
                        if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                            ffMapNavigationView.btn_flexflowreport_event.setVisibility(View.GONE);
                        } else {
                            ffMapNavigationView.setOnReportEventListener(new ReportEventClickListener());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        @SuppressWarnings("deprecation")
        private void handleClickLine(Z3PlanTaskLinePart linePartClicked) {
            Polyline polyline = (Polyline) PlanTaskUtils.buildGeometryFromJSON(linePartClicked.getGeom());
            //设置导航的目的地的经纬度
            Geometry geometry = GeometryUtil.GetGeometryCenter(polyline);
            Point point = GeometryUtil.GetGeometryCenter(GeometryUtil.GetGeometryCenter(geometry));
            double[] targetLonLat = CoordTransfer.transToLatlon(point.getX(), point.getY());
            activity.setTargetLonLat(targetLonLat);

            boolean isArrived = linePartClicked.isCovered();
            boolean isFeedBack = linePartClicked.isFeedBack();
            String lineTpyeAndId = null;
            if ((linePartClicked.getType().equals("0")) || (linePartClicked.getEquipid().equals(null))) {
                lineTpyeAndId = activity.getResources().getString(R.string.planningtask_none_device_line);
            } else {
                lineTpyeAndId = linePartClicked.getType() + "_" + linePartClicked.getEquipid();
            }
            //到位之后才能反馈 反馈的必定已经到位
            String statusStr = null;
            if (isArrived) {
                if (isFeedBack) {
                    statusStr = lineTpyeAndId + "(" + activity.getResources().getString(R.string.planningtask_arrived) + "_"
                            + activity.getResources().getString(R.string.planningtask_has_feedback) + ")";
                    ffMapNavigationView.setBackUpBtnEnable(false);
                } else {
                    statusStr = lineTpyeAndId + "(" + activity.getResources().getString(R.string.planningtask_arrived) + "_"
                            + activity.getResources().getString(R.string.planningtask_no_feedback) + ")";
                    ffMapNavigationView.setBackUpBtnEnable(true);
                }
                ffMapNavigationView.setStatusBackground(activity.getResources().getDrawable(R.drawable.textview_style_enable_true));
            } else {
                statusStr = lineTpyeAndId + "(" + activity.getResources().getString(R.string.planningtask_not_arrived) + "_"
                        + activity.getResources().getString(R.string.planningtask_no_feedback) + ")";
                ffMapNavigationView.setStatusBackground(activity.getResources().getDrawable(R.drawable.textview_style_enable_false));
                ffMapNavigationView.setBackUpBtnEnable(false);
            }
            ffMapNavigationView.setStatusText(statusStr);
            ffMapNavigationView.setOnBackUpListener(new LineBackUpClickListener());
            ffMapNavigationView.setOnDetailsListener(new OnLineDetailsClickListener());
        }

        private void renderMethod(GraphicsLayer gLayer, Graphic result) {

            if (null == gLayer || null == result) {
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
                    String type = geometry.getType().name();
                    Symbol symbol = null;
                    if (type.equalsIgnoreCase("polyline")) {
                        symbol = CustomRenderer.getInstance().PIPENETPO_LINE_SOLID_RED();
                    } else if (type.equalsIgnoreCase("polygon")) {
                        symbol = CustomRenderer.getInstance().PIPENETPO_POLYGON_RED_FILL();
                    } else if (type.equalsIgnoreCase("point")) {
                        symbol = POINT_HIGHLIGHT_MARKER_SYMBOL;
                    }
                    g = new Graphic(geometry, symbol, null, 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return g;

        }
    };

    public class ReportEventClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.PLAN_TASK_ID, plantask.getTaskid());
            UIHelper.startActivityWithExtra(ReportEventTypeSelectActivity.class, bundle);
        }
    }

    public class PointBackUpClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (null != graphicFlash) {
                graphicFlash.stopFlash();
            }
            Bundle extra = new Bundle();
            extra.putInt(CustomViewInflater.REPORT_TITLE, R.string.planningtask_report_check_items);
            extra.putSerializable(PointPartInPlaceFeedbackOperator.INTENT_KEY_POINT_PART, pointPartClicked);
            extra.putSerializable(PointPartInPlaceFeedbackOperator.INTENT_KEY_TASK, plantask);

            try {
                if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                    String btnText = ffMapNavigationView.btn_flexflowreport_backup.getText().toString();
                    if(ResourceUtil.getStringById(R.string.planningtask_feedback_modify).equals(btnText)) {
                        extra.putString(CustomViewInflater.REPORT_COMFROM, FZPointPartFeedbackModifyOperator.class.getName());
                    } else {
                        extra.putString(CustomViewInflater.REPORT_COMFROM, PointPartInPlaceFeedbackOperator.class.getName());
                    }
                    extra.putBoolean(FZFeedBackCustomReportActivity.INTENT_KEY_FROM_TASK_FUNC, true);
                    UIHelper.startActivityWithExtra(FZFeedBackCustomReportActivity.class, extra);
                } else {
                    extra.putBoolean(CustomReportActivity1.INTENT_KEY_FROM_TASK_FUNC, true);
                    extra.putString(CustomViewInflater.REPORT_COMFROM, PointPartInPlaceFeedbackOperator.class.getName());
                    UIHelper.startActivityWithExtra(CustomReportActivity1.class, extra);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class LineBackUpClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (null != graphicFlash) {
                graphicFlash.stopFlash();
            }
            Bundle extra = new Bundle();
            extra.putInt(CustomViewInflater.REPORT_TITLE, R.string.planningtask_report_check_items);
            extra.putString(CustomViewInflater.REPORT_COMFROM, LinePartInPlaceFeedbackOperator.class.getName());
            extra.putSerializable(LinePartInPlaceFeedbackOperator.INTENT_KEY_LINE_PART, linePartClicked);
            extra.putInt(LinePartInPlaceFeedbackOperator.INTENT_KEY_TASK_ID, plantask.getTaskid());
            extra.putBoolean(CustomReportActivity1.INTENT_KEY_FROM_TASK_FUNC, true);
            UIHelper.startActivityWithExtra(CustomReportActivity1.class, extra);
        }
    }

    //2017/3/14改
    public class OnDetailsClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            isshow_infodetails = true;
            if (pointPartClicked.getType().equals("0")) {
                ToastUtil.showLong(activity.getContext().getResources().getString(R.string.planningtask_not_have_attrs_data));
            } else {
                getpPointInfoDetails();
            }
        }
    }

    private void getpPointInfoDetails() {
        if (pointPartClicked.getType().equals("0")) {
            handleClickedPoint(pointPartClicked, "");
        } else {
            String infoDetails = PlanningTaskManager.getInstance().getInfoDetaisByPointPart(pointPartClicked);
            if (null == infoDetails || StringUtil.isEmpty(infoDetails)) {
                getPointPartInfoDetailsByNet();
            } else {
                getpPointInfoDetailsByLocation(infoDetails);
            }
        }
    }

    private void getpPointInfoDetailsByLocation(String infoDetails) {
        PointPartAttrInfo info = PointPartAttrAdapter.getInstance().getPointPartAttrAdapter(infoDetails);
        if (info == null || info.getAttrList() == null || info.getAttrList().size() == 0) {
            return;
        }
        if (isshow_infodetails) {
            goToShowPointPartAttrs(info);
        } else {
            List<PointPartAttrInfo.Attr> infoList = info.getAttrList().get(0);
            if (null != infoList && infoList.size() > 0) {
                StringBuffer tips = new StringBuffer();
                tips.append(pointPartClicked.getType());
                for (int i = 0; i < infoList.size(); i++) {
                    PointPartAttrInfo.Attr attr = infoList.get(i);
                    //泵房名称  消火栓编号
                    if (attr.getAttrKey().equalsIgnoreCase(activity.getResources().getString(R.string.planningtask_esrifieldtype_name)) || attr.getAttrKey().equalsIgnoreCase(activity.getResources().getString(R.string.planningtask_esrifieldtype_id))) {
                        if (StringUtil.isEmpty(attr.getAttrValue())) {
                            break;
                        }
                        tips.append("_");
                        tips.append(attr.getAttrValue());
                        break;
                    }
                }
                handleClickedPoint(pointPartClicked, tips.toString());
            }
        }
    }

    private void handleClickedPoint(Z3PlanTaskPointPart pointPartClicked, String tip) {
        //设置导航的目的地的经纬度
        Geometry geometry = PlanTaskUtils.buildGeometryFromJSON(pointPartClicked.getGeom());
        Point point = GeometryUtil.GetGeometryCenter(GeometryUtil.GetGeometryCenter(geometry));
        double[] targetLonLat = CoordTransfer.transToLatlon(point.getX(), point.getY());
        activity.setTargetLonLat(targetLonLat);

        boolean isArrived = pointPartClicked.isArrive();
        boolean isFeedBack = pointPartClicked.isFeedBack();
        //到位之后才能反馈 反馈的必定已经到位
        String statusStr = null;
        if (isArrived) {
            if (isFeedBack) {
                statusStr = tip + "(" + activity.getResources().getString(R.string.planningtask_arrived) + "_"
                        + activity.getResources().getString(R.string.planningtask_has_feedback) + ")";
                try {
                    if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                        ffMapNavigationView.btn_flexflowreport_backup.setText(ResourceUtil.getStringById(R.string.planningtask_feedback_modify));
                        ffMapNavigationView.setBackUpBtnEnable(true);
                    } else {
                        ffMapNavigationView.btn_flexflowreport_backup.setText(ResourceUtil.getStringById(R.string.planningtask_feedback));
                        ffMapNavigationView.setBackUpBtnEnable(false);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                statusStr = tip + "(" + activity.getResources().getString(R.string.planningtask_arrived) + "_"
                        + activity.getResources().getString(R.string.planningtask_no_feedback) + ")";
                ffMapNavigationView.setBackUpBtnEnable(true);
                ffMapNavigationView.btn_flexflowreport_backup.setText(ResourceUtil.getStringById(R.string.planningtask_feedback));
            }
            ffMapNavigationView.setStatusBackground(activity.getResources().getDrawable(R.drawable.textview_style_enable_true));
        } else {
            statusStr = tip + "(" + activity.getResources().getString(R.string.planningtask_not_arrived) + "_"
                    + activity.getResources().getString(R.string.planningtask_no_feedback) + ")";
            ffMapNavigationView.setStatusBackground(activity.getResources().getDrawable(R.drawable.textview_style_enable_false));
            ffMapNavigationView.btn_flexflowreport_backup.setText(ResourceUtil.getStringById(R.string.planningtask_feedback));
            ffMapNavigationView.setBackUpBtnEnable(false);//TODO
        }
        ffMapNavigationView.setStatusText(statusStr);
        ffMapNavigationView.setOnBackUpListener(new PointBackUpClickListener());
        ffMapNavigationView.setOnDetailsListener(new OnDetailsClickListener());
        LoadingDialogUtil.dismiss();
    }

    //获取点设备的信息
    private void getPointPartInfoDetailsByNet() {
        if (pointPartClicked.getType().equals("0")) {
            ToastUtil.showLong(activity.getContext().getResources().getString(R.string.planningtask_not_have_attrs_data));
            handleClickedPoint(pointPartClicked, "");
            return;
        } else {
            getAttrsListForGeometry(pointPartClicked.getEquiporigin(), pointPartClicked.getEquipid());
        }
    }

    public class OnLineDetailsClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (linePartClicked.getType().equals("0")) {
                ToastUtil.showLong(activity.getContext().getResources().getString(R.string.planningtask_not_have_line_attrs_data));
                return;
            } else {
                getAttrsListForGeometry(linePartClicked.getEquiporigin(), linePartClicked.getEquipid());
            }
        }
    }

    private void getAttrsListForGeometry(String equiporigin, String objectIds) {
        // 得到Geometry属性集合
        LoadingDialogUtil.show(mOutersActivity.get().getContext(), R.string.getting_Geometry_attrs);
        String url = ServiceUrlManager.getInstance().getSpacialSearchUrl() + equiporigin.substring(equiporigin.lastIndexOf("/"));
        PlanningTaskService.getInstance().getAttrsForGeometry(url + "/query", objectIds, "click");
    }

    private void handleGetPointPartAttrs(ResponseEvent event) {

        String eventObject = event.getData();
        PlanningTaskManager.getInstance().updatePointInfoDetails(eventObject, pointPartClicked);
        PointPartAttrInfo info = PointPartAttrAdapter.getInstance().getPointPartAttrAdapter(eventObject);
        if (info == null || info.getAttrList() == null || info.getAttrList().size() == 0) {
            return;
        }
        if (isshow_infodetails) {
            goToShowPointPartAttrs(info);
        } else {
            List<PointPartAttrInfo.Attr> infoList = info.getAttrList().get(0);
            if (null != infoList && infoList.size() > 0) {
                StringBuffer tips = new StringBuffer();
                tips.append(pointPartClicked.getType());
                for (int i = 0; i < infoList.size(); i++) {
                    PointPartAttrInfo.Attr attr = infoList.get(i);
                    //泵房名称  消火栓编号
                    if (attr.getAttrKey().equalsIgnoreCase(activity.getResources().getString(R.string.planningtask_esrifieldtype_name)) || attr.getAttrKey().equalsIgnoreCase(activity.getResources().getString(R.string.planningtask_esrifieldtype_id))) {
                        if (StringUtil.isEmpty(attr.getAttrValue())) {
                            break;
                        }
                        tips.append("_");
                        tips.append(attr.getAttrValue());
                        break;
                    }
                }
                handleClickedPoint(pointPartClicked, tips.toString());
            }
        }
    }

    private void goToShowPointPartAttrs(PointPartAttrInfo info) {
        Intent attrIntent = new Intent(HostApplication.getApplication().getAppManager().currentActivity(), TaskAttrListsActivity.class);
        Bundle attrBundle = new Bundle();
        attrBundle.putString(TaskAttrListsActivity.ATTR_LISTVIEW_COMEFROM, PointPartAttrListViewOperator.class.getName());
        attrBundle.putInt(TaskAttrListsActivity.ATTR_TITLE, R.string.planningtask_pointpart_attr);
        attrBundle.putSerializable(TaskAttrListsActivity.ATTRS_LIST, (Serializable) info.getAttrList().get(0));
        attrIntent.putExtras(attrBundle);
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(attrIntent);
    }

    /*
     * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
     * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
     */
    private Graphic getGraphicsFromLayer(float xScreen, float yScreen, GraphicsLayer layer) {
        Graphic result = null;
        try {
            float x = xScreen;
            float y = yScreen;
            int[] idsArr = layer.getGraphicIDs(x, y, 5, 1);
            if (idsArr.length > 0) {
                result = layer.getGraphic(idsArr[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public void notifyBackEvent(IMapOperationContext mapActivity) {
        if (null != mapActivity) {
            if (null != graphicFlash) {
                graphicFlash.stopFlash();
                graphicFlash = null;
            }

            if (null != annimationLayer) {
                annimationLayer.removeAll();
            }
            if (null != pointGidMap) {
                pointGidMap.clear();
            }
            if (null != arrivedLineGidMap) {
                arrivedLineGidMap.clear();
            }
            if (null != trackLineGidMap) {
                trackLineGidMap.clear();
            }
            if (null != commenPointGidMap) {
                trackLineGidMap.clear();
            }
            if (null != arrivedPointGidMap) {
                trackLineGidMap.clear();
            }
            if (null != feedbackPointGidMap) {
                trackLineGidMap.clear();
            }
            if (null != feedbackGrahicGids) {
                feedbackGrahicGids.clear();
            }

            if (null != markGrahicGids) {
                markGrahicGids.clear();
            }


            isdrawing = false;
//            graphicsLayer.removeAll();
//            mapView.removeLayer(graphicsLayer);
            SessionManager.currentTaskIntMapOpretor = null;

            if (HostApplication.getApplication().getProjectStyle() == HostApplication.ProjectStyle.PROJECT_FZXJ) {
                Intent intent = new Intent(activity, PlanningTaskActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                ffMapNavigationView.setVisibility(View.GONE);
                pointPartClicked = null;
                EventBusUtil.unregister(this);
            } else {
                EventBusUtil.unregister(this);
                mapActivity.finish();
            }
        }
    }

    private void handleDrawArriveInfoGeometry(UIEvent event) {
        mapView = mOutersMapView.get();
        if (null == activity || null == mapView || plantask == null) {
            return;
        }

        // 检查是否包含ArriveInfo返回的task
        @SuppressWarnings("unchecked")
        List<ArriveInfo> arriveInfoList = event.getData();
        for (ArriveInfo info : arriveInfoList) {
            int arriveTaskId = info.getPlanTaskId();
            int currentTaskId = plantask.getTaskid();
            if (currentTaskId == arriveTaskId) {
                Z3PlanTaskPart part = info.getmZ3PlanTaskPart();
                if (part instanceof Z3PlanTaskPointPart) {
                    Message msg = Message.obtain();
                    msg.what = POINT_ARRIVIED;
                    msg.obj = info;
                    mPointHandler.sendMessage(msg);
                } else if (part instanceof Z3PlanTaskLinePart) {
                    if (isLegentLoaded && (!isLineCanVisible("arrivedLineGidMap"))) {
                        return;
                    }
                    drawArrivedLine(info);
                }
            }
        }
    }

    private void handleDrawtrackLine(UIEvent event) {
        Polyline trackLine = event.getData();
//        drawTrackLine(trackLine);
    }

    private void handleReflashMapViewAfterChecked(UIEvent event) {
        LegendControl legendControl = event.getData();
        if (isPointTask) {
            for (int i = 0; i < pointPlanTaskGidMapslist.size(); i++) {
                if (pointPlanTaskGidMapslist.get(i).getKey().equalsIgnoreCase(legendControl.getKey())) {
                    pointPlanTaskGidMapslist.remove(pointPlanTaskGidMapslist.get(i));
                    pointPlanTaskGidMapslist.add(legendControl);
                }
            }
        } else {
            for (int i = 0; i < linePlanTaskGidMapslist.size(); i++) {
                if (linePlanTaskGidMapslist.get(i).getKey().equalsIgnoreCase(legendControl.getKey())) {
                    linePlanTaskGidMapslist.remove(linePlanTaskGidMapslist.get(i));
                    linePlanTaskGidMapslist.add(legendControl);
                }
            }
        }
    }

    /**
     * 根据key来判定线图例的显示与否
     *
     * @param key
     * @return
     */
    private boolean isLineCanVisible(String key) {
        for (int i = 0; i < linePlanTaskGidMapslist.size(); i++) {
            if (linePlanTaskGidMapslist.get(i).getKey().equalsIgnoreCase(key)) {
                LegendControl legendControl = linePlanTaskGidMapslist.get(i);
                return legendControl.isVisible();
            }
        }
        return false;
    }

    private void handleReflashViewAfterClickNotification(UIEvent event) {
        mapView = mOutersMapView.get();
        if (null == activity || null == mapView) {
            return;
        }
        String nextStepId = event.getData();
        String[] ids = nextStepId.split(",");
        if (ids.length == 3) {
            ReflashPointStatus(ids);
        }
    }

    private void ReflashPointStatus(String[] str) {
        String cTaskid = str[0];
        String cGid = str[1];
        String cUserid = str[2];
        User currentUser = HostApplication.getApplication().getCurrentUser();
        if ((plantask.getTaskid() + "").equals(cTaskid) && (!currentUser.getGid().equals(cUserid))) {
            Z3PlanTaskPointPart pointPart = PlanningTaskManager.getInstance().getPlanTaskPointPart(Integer.parseInt(cTaskid), Integer.parseInt(cGid));
            if (pointPart.isArrive()) {
                return;
            }
            drawOtherUserArrivedPoint(pointPart);
        }
    }

    private void drawOtherUserArrivedPoint(Z3PlanTaskPointPart pointPart) {
        if (null == pointPart) {
            return;
        }
        if (!pointPart.isFeedBack()) {
            return;
        }
        Map<String, Object> mapForPlanId = new HashMap<String, Object>();
        mapForPlanId.put("gid", pointPart.getGid());
        Point point = (Point) PlanTaskUtils.buildGeometryFromJSON(pointPart.getGeom());
        double ya = point.getY();
        point.setY(ya + 1);
        Graphic g = new Graphic(point, POINT_OTHER_USER_ARRIVED_SYMBOL, mapForPlanId, null);
        GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        int uid = graphicsLayer.addGraphic(g);
        graphicsLayer.updateGraphic(uid, g);
    }

    private void handleDrawFeedBackPoint(UIEvent event) {
        mapView = mOutersMapView.get();
        if (null == activity || null == mapView) {
            return;
        }
        Z3PlanTaskPointPart pointPart = event.getData();
        if (pointPart instanceof Z3PlanTaskPointPart) {
            Message msg = Message.obtain();
            msg.what = POINT_FEEDBACK;
            msg.obj = pointPart;
            mPointHandler.sendMessage(msg);
        }
    }

    //画反馈后的线
    private void handleDrawFeedBackLine(UIEvent event) {
        mapView = mOutersMapView.get();
        if (null == activity || null == mapView) {
            return;
        }
        Z3PlanTaskLinePart linePart = event.getData();
        if (linePart instanceof Z3PlanTaskLinePart) {
            drawFeedBackLine(linePart);
        }
    }

    private PointHandler mPointHandler = new PointHandler();

    private class PointHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POINT_ARRIVIED:
                    ArriveInfo info = (ArriveInfo) msg.obj;
                    drawArrivedPoint(info);
                    break;
                case POINT_FEEDBACK:
                    Z3PlanTaskPointPart pointPart = (Z3PlanTaskPointPart) msg.obj;
                    drawFeedBackPoint(pointPart);
                    break;
                default:
                    break;
            }
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.PLANNINGTASK_TRACKLINE_NOTIFICATION:
                if (!isPointTask) {
                    if (isLegentLoaded && (!isLineCanVisible("trackLineGidMap"))) {
                        return;
                    }
                    handleDrawtrackLine(event);
                }
                break;
            case UIEventStatus.PLANNINGTASK_LINE_FEEDBACK_NOTIFICATION:
                handleDrawFeedBackLine(event);
                break;
            case UIEventStatus.PLANNINGTASK_REFLASH_YH_POINT_STATUS:
                handleReflashViewAfterClickNotification(event);
                break;
            case UIEventStatus.PLANNINGTASK_ALLSTATESPOINTPART_STATUS:
                handleReflashMapViewAfterChecked(event);
                break;
            default:
                break;
        }
    }

    public void onEventBackgroundThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.PLANNINGTASK_POINT_FEEDBACK_NOTIFICATION:
                handleDrawFeedBackPoint(event);
                break;
            case UIEventStatus.PLANNINGTASK_POINT_PART_NOTIFICATION:
                handleDrawArriveInfoGeometry(event);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        switch (event.getId()) {
            case ResponseEventStatus.PLANNING_ATTRS:
                if (!event.isOK()) {
                    ToastUtil.showLong(event.getMessage());
                    handleClickedPoint(pointPartClicked, "");
                    return;
                } else {
                    // 得到属性集合
                    handleGetPointPartAttrs(event);
                }
                break;
            default:
                break;
        }
    }

}