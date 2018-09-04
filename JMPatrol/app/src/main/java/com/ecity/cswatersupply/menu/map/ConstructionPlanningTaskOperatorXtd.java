package com.ecity.cswatersupply.menu.map;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.menu.map.CustomPopupMenu.OnEventMenuClickListener;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPolygonPart;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.activities.ReportEventTypeSelectActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.AnalyzeMapMenuXML;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.PlanTaskUtils;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

/**
 * 施工场地巡检任务
 * @author Gengxiuxiu
 *
 */
public class ConstructionPlanningTaskOperatorXtd extends AMapViewOperator {

    public static final String PLAN_TASK_ID = "PLAN_TASK_ID";
    private WeakReference<IMapOperationContext> mOutersActivity;
    private WeakReference<MapView> mOutersMapView;
    public Z3PlanTask plantask;
    public MapActivity activity;
    MapView mapView;
    public boolean isdrawing;
    private Button report;
    private MapActivityTitleView titleView;
    private SimpleFillSymbol POLYGON_MARKER_SYMBOL;
    private SimpleLineSymbol POLYGON_OUTLINE_SYMBOL;

    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext mapActivity) {
        if (null == mapView) {
            return;
        }
        mOutersActivity = new WeakReference<IMapOperationContext>(mapActivity);
        mOutersMapView = new WeakReference<MapView>(mapView);
        activity = (MapActivity) mOutersActivity.get();
        titleView = activity.getTitleView();
        //zzz 2017-06-01
        titleView.setBtnStyle(BtnStyle.ONLY_BACK);
        titleView.setOnLegendListener(new SelectEventClickListener());
        report = activity.getReport();
        report.setVisibility(View.VISIBLE);
        report.setOnClickListener(new ReportEventClickListener());

        POLYGON_MARKER_SYMBOL = new SimpleFillSymbol(activity.getResources().getColor(R.color.geo_fill_color), com.esri.core.symbol.SimpleFillSymbol.STYLE.SOLID);
        POLYGON_OUTLINE_SYMBOL = new SimpleLineSymbol(activity.getResources().getColor(R.color.red), 2, SimpleLineSymbol.STYLE.DASHDOT);
    }

    @Override
    public void notifyMapLoaded() {
        if (!isdrawing) {
            isdrawing = true;
            mapView = mOutersMapView.get();
            drawImageLayer();
        }
    }

    @Override
    public void notifyBackEvent(IMapOperationContext mapActivity) {
        if (null != mapActivity) {
            mapActivity.finish();
        }
    }

    // 画图像层 开始进入地图界面
    public void drawImageLayer() {

        if (null == activity || null == mapView) {
            return;
        }
        Intent intent = activity.getUserIntent();
        try {
            if (intent.hasExtra("planning_tasks")) {
                plantask = (Z3PlanTask) intent.getSerializableExtra("planning_tasks");
                if (plantask == null) {
                    return;
                }
                drawGeomotry(plantask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 画图
    public void drawGeomotry(Z3PlanTask task) {
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
                mapView.setExtent(gon);
                mapView.getExtent().queryEnvelope(SessionManager.lastInitExtent);
            }
        }
    }

    private class SelectEventClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            List<AEventMenu> menus = AnalyzeMapMenuXML.analyzeMenuXML(activity,R.xml.construction_planningtask_eventmenu);
            CustomPopupMenu moreBtnPopup = new CustomPopupMenu(activity, menus, AEventMenu.class);
            int itemCount = menus.size();
            int padding = ResourceUtil.getDimensionPixelSizeById(R.dimen.lv_item_padding_up_down_level_1);
            int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.popmenu_item_w) + (padding << 1);
            int height = ResourceUtil.getDimensionPixelSizeById(R.dimen.popmenu_item_h) * itemCount + (itemCount + 1) * padding;
            moreBtnPopup.initPopup(width, LayoutParams.WRAP_CONTENT);
            int[] location = new int[2];
            arg0.getLocationOnScreen(location);
            // 在屏幕上半边
            if (moreBtnPopup.atTopHalfOfScreen(location)) {
                moreBtnPopup.setAnimation(R.style.popup_tophalf_anim);
                moreBtnPopup.showAtLocation(arg0, (location[0] + arg0.getWidth() / 2) - width / 2, location[1] + arg0.getHeight() + 5, Gravity.NO_GRAVITY);
            } else {
                // 在屏幕下半边
                moreBtnPopup.setAnimation(R.style.popup_bottomhalf_anim);
                moreBtnPopup.showAtLocation(arg0, (location[0] + arg0.getWidth() / 2) - width / 2, location[1] - height - 5, Gravity.NO_GRAVITY);
            }
            moreBtnPopup.setOnEventMenuClickListener(new OnEventMenuClickListener() {

                @Override
                public void onMenuItemClick(AEventMenu menu, int pos) {
                    menu.getEventCommand().setTaskid(plantask.getTaskid());
                    menu.getEventCommand().execute();
                }
            });
        }
    }

    public class ReportEventClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.PLAN_TASK_ID, plantask.getTaskid());
            UIHelper.startActivityWithExtra(ReportEventTypeSelectActivity.class, bundle);
        }
    }

}