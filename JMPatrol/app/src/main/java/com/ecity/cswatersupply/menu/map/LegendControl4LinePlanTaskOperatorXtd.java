package com.ecity.cswatersupply.menu.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.planningTask.ArriveInfo;
import com.ecity.cswatersupply.model.planningTask.LegendControl;
import com.ecity.cswatersupply.model.planningTask.LegendInfo;
import com.ecity.cswatersupply.model.planningTask.LegendInfo.StatusType;
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo;
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo.PointStatusType;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.DecimaUtils;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polyline;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

/**
 * 线的图里控制 
 * @author maliu
 *
 */
public class LegendControl4LinePlanTaskOperatorXtd extends AMapMenuCommand {

	private static LegendControl4LinePlanTaskOperatorXtd instanceControlMenu;
	private Legend4LinePlanTaskListAdapter legendListAdapter = null;
	private MapView mapView;
	private MapActivity mapActivity;
	private PopupWindow popupWindow = null;
	public boolean isshowing = false;
	public boolean[] isCheckeds;
	//map集合对应的key值
    public String[] gidsMapKeys = new String[]{"planLineGidMap","trackLineGidMap","arrivedLineGidMap","pointGidMap"};
    //记录每种map对应的显示状态
    public boolean[] isGraphicsVisible = new boolean[]{true,true,true,true};
    //所有的点在图层上返回的uid
    public HashMap<String, Integer> pointGidMap;
    //计划路线在图层上的uid
    public HashMap<String, Integer> planLineGidMap;
    //行走轨迹在图层上的uid
    public HashMap<String, Integer> trackLineGidMap;
    //行走轨迹在计划线上覆盖之后形成的线在图层上的uid
    public HashMap<String, Integer> arrivedLineGidMap;
    public List<LegendControl> GidMapslist;
    public HashMap<String, Integer> GidsMap;
	

	public static LegendControl4LinePlanTaskOperatorXtd getInstance() {
		if (null == instanceControlMenu) {
			instanceControlMenu = new LegendControl4LinePlanTaskOperatorXtd();
		}
		return instanceControlMenu;
	}

	public void ClearPopwindow() {
		if (null == popupWindow) {
			return;
		}
		popupWindow.dismiss();
	}
	
    public void setGidMapslist(List<LegendControl> gidMapslist) {
        GidMapslist = gidMapslist;
    }

    @Override
    public boolean execute(MapView mapView, IMapOperationContext mapActivity) {
        if (null == mapView) {
            return false;
        }

        if (null == mapActivity) {
            return false;
        }
        this.mapActivity = (MapActivity) mapActivity;
        this.mapView = mapView;
        GidsMap = new HashMap<String, Integer>();
        // 弹出图层选择窗口
        if (isshowing) {
            popupWindow.dismiss();
        } else {
            showWindow();
        }
        EventBusUtil.register(this);
        return true;
    }
    
     private void initChecked(){
        for (int i = 0; i < gidsMapKeys.length; i++) {
             for (LegendControl legendControl:GidMapslist) {
                 if (legendControl.getKey().equalsIgnoreCase(gidsMapKeys[i])) {
                     isCheckeds[i] = legendControl.isVisible();
                 }
             }
        }
     }
    
    public ArrayList<LegendInfo> getLegendInfos(){
        ArrayList<LegendInfo> info = new ArrayList<LegendInfo>();
        Z3PlanTask planTask = SessionManager.currentTaskIntMapOpretor;
        //计划路线  
        LegendInfo planLineLegendInfo = new LegendInfo("line", HostApplication.getApplication().getString(R.string.plan_line), 
                StatusType.PLAN_LINE, planTask.getLinelen(), 1, R.color.red);
        info.add(planLineLegendInfo);
        //行走轨迹
        Polyline walkedLine = (Polyline) planTask.getTrackLinegeom();
        double walkLinelen = GeometryUtil.calculateLength(walkedLine, true);
        LegendInfo walkedLineLegendInfo = new LegendInfo("line", HostApplication.getApplication().getString(R.string.walked_line), 
                StatusType.WALKED_LINE, walkLinelen, 1, R.color.track_line_color);
        info.add(walkedLineLegendInfo);
        //覆盖轨迹
        double trajectoryLinelen = PlanningTaskManager.getInstance().getArrivedLineLength(planTask.getTaskid());
        LegendInfo trajectoryLineLegendInfo = new LegendInfo("line", HostApplication.getApplication().getString(R.string.trajectory_line), 
                StatusType.TRAJECTORY_LINE, trajectoryLinelen, 1, R.color.purple);
        info.add(trajectoryLineLegendInfo);
        
        ArrayList<Z3PlanTaskPointPart> pointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_ALL,planTask.getTaskid());
        if (!ListUtil.isEmpty(pointParts)) {
            ArrayList<PointPartLegendInfo> pointInfos = new ArrayList<PointPartLegendInfo>();
            if (planTask.getIsContents() == 2) {
                ArrayList<Z3PlanTaskPointPart> feedbackPointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_FEEDBACK, planTask.getTaskid());
                PointPartLegendInfo feedbackPointInfo = new PointPartLegendInfo(PointStatusType.FEEDBACKED, ListUtil.isEmpty(feedbackPointParts)? 0:feedbackPointParts.size());
                pointInfos.add(feedbackPointInfo);
            }
            ArrayList<Z3PlanTaskPointPart> arrivedPointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_ARRIVED, planTask.getTaskid());
            PointPartLegendInfo arrivedPointInfo = new PointPartLegendInfo(PointStatusType.ARRIVED, ListUtil.isEmpty(arrivedPointParts)? 0:arrivedPointParts.size());
            pointInfos.add(arrivedPointInfo);
            PointPartLegendInfo unArrivedPointInfo = new PointPartLegendInfo(PointStatusType.UN_ARRIVED, ListUtil.isEmpty(pointParts)? 0:pointParts.size());
            pointInfos.add(unArrivedPointInfo);
            LegendInfo pointLegendInfo = new LegendInfo("point", pointInfos, 1, StatusType.POINT);
            info.add(pointLegendInfo);
        }
        return info;
    }

	@SuppressWarnings("deprecation")
    private void showWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        mapActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mapActivity).inflate(R.layout.dialog_layercontrol, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(linearLayout, screenWidth - 40,LayoutParams.WRAP_CONTENT);
        }
        ListView listView = (ListView) linearLayout.findViewById(R.id.layerList);
        legendListAdapter = new Legend4LinePlanTaskListAdapter(mapActivity);
        legendListAdapter.setList(getLegendInfos());
        isCheckeds = new boolean[4];
        initChecked();
        listView.setAdapter(legendListAdapter);
        legendListAdapter.notifyDataSetChanged();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.layercontrolwin_anim_style);
        popupWindow.setOnDismissListener(onDismissListener);
        popupWindow.showAsDropDown(mapActivity.findViewById(R.id.view_title_mapactivity), 20, 30);
        popupWindow.update();
        isshowing = true;
    }
    
    OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            isshowing = false;
            EventBusUtil.unregister(this);
        }
    };
    /**
     * 线任务的图例列表适配
     * @author ml
     *
     */
    class Legend4LinePlanTaskListAdapter extends ArrayListAdapter<LegendInfo> {
        private static final int TYPE_COUNT = 2;
        private static final int TYPE_POINT = 0;
        private static final int TYPE_LINE = 1;
        private LayoutInflater mInflater = null;
        private int currentType = 0;

        public Legend4LinePlanTaskListAdapter(Context mContext) {
            super(mContext);
            this.mInflater = LayoutInflater.from(mContext);
        }
        
        @Override
        public int getItemViewType(int position) {
            if ("point".equals(mList.get(position).getItem_type())) {
                return TYPE_POINT;//
            } else if ("line".equals(mList.get(position).getItem_type())) {
                return TYPE_LINE;//
            }else {
                return 3;
            }
        }
        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LegendInfo info = mList.get(position);
            currentType = getItemViewType(position);
            if (currentType == TYPE_LINE) {
                LineViewHolder lineHolder = null;
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.lv_item_legend_line,null);
                    lineHolder = new LineViewHolder();
                    lineHolder.line_flag = (TextView) convertView.findViewById(R.id.line_flag);
                    lineHolder.line_ststus_txt = (TextView) convertView.findViewById(R.id.line_ststus_txt);
                    lineHolder.line_ststus_num = (TextView)convertView.findViewById(R.id.line_ststus_num);
                    lineHolder.line_ststus_loadType = (ToggleButton) convertView.findViewById(R.id.line_ststus_loadType);
                    convertView.setTag(lineHolder);
                } else {
                    lineHolder = (LineViewHolder) convertView.getTag();
                }
                lineHolder.line_flag.setBackgroundResource(info.getColorid());
                lineHolder.line_ststus_txt.setText(info.getLegendNameForLine());
                lineHolder.line_ststus_num.setText(DecimaUtils.DoubleFormat2(info.getLineLength())+"米");
                lineHolder.line_ststus_num.setTextColor(HostApplication.getApplication().getResources().getColor(info.getColorid()));
                lineHolder.line_ststus_loadType.setOnCheckedChangeListener(new ButtonClickListener(position, mapView));
                lineHolder.line_ststus_loadType.setChecked(isCheckeds[position]);
                return convertView;
            }else if (currentType == TYPE_POINT) {
                pointViewHolder pointHolder = null;
                convertView = mInflater.inflate(R.layout.lv_item_legend_line_point,null);
                pointHolder = new pointViewHolder();
                pointHolder.point_commen_arrived_txt = (TextView) convertView.findViewById(R.id.point_commen_arrived_txt);
                pointHolder.feedback_linearLayout = (LinearLayout)convertView.findViewById(R.id.point_feedback_ll);
                pointHolder.point_feedback = (ImageView) convertView.findViewById(R.id.point_feedback);
                pointHolder.point_ststus_loadType = (ToggleButton) convertView.findViewById(R.id.point_ststus_loadType);
                ArrayList<PointPartLegendInfo> pointParts = info.getPointParts();
                pointHolder = initPointHolder(position, pointHolder, pointParts);
                convertView.setTag(pointHolder);
                return convertView;
            }
        return convertView;
    }
        
        public pointViewHolder initPointHolder(int position, pointViewHolder holder,ArrayList<PointPartLegendInfo> pointParts) {
            StringBuffer buf = new StringBuffer();
            int unArrivedPointNum;
            int ArrivedPointNum;
            int feedbackNum;
            for (PointPartLegendInfo info : pointParts) {
                switch (info.getPointStatus()) {
                case FEEDBACKED:
                    feedbackNum = info.getPointNum();
                    buf.append(feedbackNum+ "/");
                    break;
                case ARRIVED:
                    ArrivedPointNum = info.getPointNum();
                    buf.append(ArrivedPointNum + "/");
                    break;
                case UN_ARRIVED:
                    unArrivedPointNum = info.getPointNum();
                    buf.append(unArrivedPointNum);
                    break;
                default:
                    break;
                }
            }
            if (SessionManager.currentTaskIntMapOpretor.getIsContents() == 2) {
                holder.feedback_linearLayout.setVisibility(View.VISIBLE);
            }
            holder.point_commen_arrived_txt.setText(buf.toString());
            holder.point_ststus_loadType.setOnCheckedChangeListener(new ButtonClickListener(position, mapView));
            holder.point_ststus_loadType.setChecked(isCheckeds[position]);
            return holder;
        }

        class LineViewHolder {
            private TextView line_flag, line_ststus_txt, line_ststus_num;
            private ToggleButton line_ststus_loadType;
        }

        class pointViewHolder {
            private LinearLayout feedback_linearLayout;
            private ImageView point_feedback;
            private TextView point_commen_arrived_txt;
            private ToggleButton point_ststus_loadType;
        }
    
        class ButtonClickListener implements OnCheckedChangeListener {
            private int index;
            private MapView mapView;

            public ButtonClickListener(int index, MapView mapView) {
                super();
                this.index = index;
                this.mapView = mapView;
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView,final boolean isChecked) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (LegendControl legendControl : GidMapslist) {
                            if (legendControl.getKey().equalsIgnoreCase(gidsMapKeys[index])) {
                                GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
                                GidsMap = legendControl.getGidMap();
                                Collection<Integer> values = GidsMap.values();
                                Iterator<Integer> uids = values.iterator();
                                while (uids.hasNext()) {
                                    graphicsLayer.setGraphicVisible(uids.next(), isChecked);
                                }
                                legendControl.setVisible(isChecked);
                                isCheckeds[index] = isChecked;
                                
                                // 
                                UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_ALLSTATESPOINTPART_STATUS);
                                event.setData(legendControl);
                                EventBusUtil.post(event);
                                break;
                            }
                        }
                    }
                }).start();
            }
        }
    }
    
    public void onEventMainThread(UIEvent event) {
        if (event == null) {
            return;
        }
        if (null == SessionManager.currentTaskIntMapOpretor) {
            return;
        }
        switch (event.getId()) {
            case UIEventStatus.PLANNINGTASK_POINT_PART_NOTIFICATION:
                List<ArriveInfo> arriveInfos = event.getData();
                int plantaskId_now = 0;
                //判断当前的任务和事件发送过来的任务是否一致
                for (ArriveInfo info : arriveInfos) {
                   int plantaskId = info.getPlanTaskId();
                   if (SessionManager.currentTaskIntMapOpretor.getPlanid() == plantaskId) {
                       plantaskId_now = plantaskId;
                       break;
                   }
                }
                if (plantaskId_now != 0) {
                    legendListAdapter.setList(getLegendInfos());
                    legendListAdapter.notifyDataSetChanged();
                }
                break;
            case UIEventStatus.PLANNINGTASK_TRACKLINE_NOTIFICATION:
                Object data = event.getData();
                Geometry trackLinegeom = (Geometry) data;
                SessionManager.currentTaskIntMapOpretor.setTrackLinegeom(trackLinegeom);
                legendListAdapter.setList(getLegendInfos());
                legendListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
