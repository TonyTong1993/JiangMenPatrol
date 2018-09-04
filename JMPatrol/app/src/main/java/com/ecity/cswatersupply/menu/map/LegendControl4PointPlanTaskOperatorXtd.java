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
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo;
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo.PointStatusType;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

/**
 * 点的图里控制 
 * @author maliu
 *
 */
public class LegendControl4PointPlanTaskOperatorXtd extends AMapMenuCommand {

	private static LegendControl4PointPlanTaskOperatorXtd instanceControlMenu;
	private Legend4PointPlanTaskListAdapter legendListAdapter = null;
	private MapView mapView;
	private MapActivity mapActivity;
	private PopupWindow popupWindow = null;
	public boolean isshowing = false;
	public boolean[] isCheckeds;
	//map集合对应的key值
	public String[] pointPlanTaskGidsMapKeys = new String[]{"commenPointGidMap","arrivedPointGidMap","feedbackPointGidMap"};
    //记录每种map对应的显示状态
    public boolean[] isGraphicsVisible = new boolean[]{true,true,true};
    //所有的点在图层上返回的uid
    public HashMap<String, Integer> commenPointGidMap;
    //计划路线在图层上的uid
    public HashMap<String, Integer> arrivedPointGidMap;
    //行走轨迹在图层上的uid
    public HashMap<String, Integer> feedbackPointGidMap;
    public List<LegendControl> pointPlanTaskGidMapslist;
    public HashMap<String, Integer> GidsMap;
	

	public static LegendControl4PointPlanTaskOperatorXtd getInstance() {
		if (null == instanceControlMenu) {
			instanceControlMenu = new LegendControl4PointPlanTaskOperatorXtd();
		}
		return instanceControlMenu;
	}

	public void ClearPopwindow() {
		if (null == popupWindow) {
			return;
		}
		popupWindow.dismiss();
	}
	
    public void setPointPlanTaskGidMapslist(List<LegendControl> pointPlanTaskGidMapslist) {
        this.pointPlanTaskGidMapslist = pointPlanTaskGidMapslist;
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
        for (int i = 0; i < pointPlanTaskGidsMapKeys.length; i++) {
             for (LegendControl legendControl:pointPlanTaskGidMapslist) {
                 if (legendControl.getKey().equalsIgnoreCase(pointPlanTaskGidsMapKeys[i])) {
                     isCheckeds[i] = legendControl.isVisible();
                 }
             }
        }
     }
    
    public ArrayList<PointPartLegendInfo> getLegendInfos(){
        ArrayList<PointPartLegendInfo> pointInfos = new ArrayList<PointPartLegendInfo>();
        Z3PlanTask planTask = SessionManager.currentTaskIntMapOpretor;
        ArrayList<Z3PlanTaskPointPart> pointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_ALL,
                planTask.getTaskid());
        if (!ListUtil.isEmpty(pointParts)) {
            ArrayList<Z3PlanTaskPointPart> unArrivedPointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(
                    Constants.PLANTASK_GETPOINT_ID_UNARRIVED, planTask.getTaskid());
            PointPartLegendInfo unArrivedPointInfo = new PointPartLegendInfo(PointStatusType.UN_ARRIVED, ListUtil.isEmpty(unArrivedPointParts) ? 0 : unArrivedPointParts.size());
            pointInfos.add(unArrivedPointInfo);
            ArrayList<Z3PlanTaskPointPart> arrivedPointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(
                    Constants.PLANTASK_GETPOINT_ID_ARRIVED, planTask.getTaskid());
            PointPartLegendInfo arrivedPointInfo = new PointPartLegendInfo(PointStatusType.ARRIVED, ListUtil.isEmpty(arrivedPointParts) ? 0 : arrivedPointParts.size());
            pointInfos.add(arrivedPointInfo);
            if (planTask.getIsContents() == 2) {
                ArrayList<Z3PlanTaskPointPart> feedbackPointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(
                        Constants.PLANTASK_GETPOINT_ID_FEEDBACK, planTask.getTaskid());
                PointPartLegendInfo feedbackPointInfo = new PointPartLegendInfo(PointStatusType.FEEDBACKED, ListUtil.isEmpty(feedbackPointParts) ? 0 : feedbackPointParts.size());
                pointInfos.add(feedbackPointInfo);
            }
        }
        return pointInfos;
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
        legendListAdapter = new Legend4PointPlanTaskListAdapter(mapActivity);
        legendListAdapter.setList(getLegendInfos());
        isCheckeds = new boolean[3];
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
     * 点任务的图例列表适配
     * @author ml
     *
     */
    class Legend4PointPlanTaskListAdapter extends ArrayListAdapter<PointPartLegendInfo>{
        private LayoutInflater mInflater = null;
        public Legend4PointPlanTaskListAdapter(Context context) {
            super(context);
            this.mInflater = LayoutInflater.from(mContext);
        }

		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PointPartLegendInfo info = mList.get(position);
            ViewHolder pointHolder = null;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.lv_item_legend_point,null);
                pointHolder = new ViewHolder();
                pointHolder.point_status_text = (TextView) convertView.findViewById(R.id.point_status_text);
                pointHolder.point_image = (ImageView) convertView.findViewById(R.id.point_image);
                pointHolder.point_num_text = (TextView) convertView.findViewById(R.id.point_num_text);
                pointHolder.point_loadType_tb = (ToggleButton) convertView.findViewById(R.id.point_loadType_tb);
                convertView.setTag(pointHolder);
            }else {
                pointHolder = (ViewHolder) convertView.getTag();
            }
            pointHolder = initPointHolder(position, pointHolder, info);
            convertView.setTag(pointHolder);
            return convertView;
        }
        
        @SuppressWarnings("deprecation")
        public ViewHolder initPointHolder(int position, ViewHolder holder,PointPartLegendInfo info) {
            switch (info.getPointStatus()) {
                case FEEDBACKED:
                    holder.point_status_text.setText(HostApplication.getApplication().getResources().getString(R.string.planningtask_has_feedback));
                    holder.point_image.setBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.map_location_blue));
                    break;
                case ARRIVED:
                    holder.point_status_text.setText(HostApplication.getApplication().getResources().getString(R.string.planningtask_arrived));
                    holder.point_image.setBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.map_location_hl));
                    break;
                case UN_ARRIVED:
                    holder.point_status_text.setText(HostApplication.getApplication().getResources().getString(R.string.planningtask_not_arrived));
                    holder.point_image.setBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.map_location_red));
                    break;
                default:
                    break;
            }
            holder.point_num_text.setText(String.valueOf(info.getPointNum()));
            holder.point_loadType_tb.setOnCheckedChangeListener(new ButtonClickListener(position, mapView));
            holder.point_loadType_tb.setChecked(isCheckeds[position]);
            return holder;
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
                        for (LegendControl legendControl : pointPlanTaskGidMapslist) {
                            if (legendControl.getKey().equalsIgnoreCase(pointPlanTaskGidsMapKeys[index])) {
                                GraphicsLayer graphicsLayer = LayerTool.getGraphicsLayer(mapView);
                                GidsMap = legendControl.getGidMap();
                                Collection<Integer> values = GidsMap.values();
                                Iterator<Integer> uids = values.iterator();
                                while (uids.hasNext()) {
                                    graphicsLayer.setGraphicVisible(uids.next(), isChecked);
                                }
                                legendControl.setVisible(isChecked);
                                isCheckeds[index] = isChecked;
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
        
        class ViewHolder {
            private TextView point_status_text;
            private ImageView point_image;
            private TextView point_num_text;
            private ToggleButton point_loadType_tb;
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
            default:
                break;
        }
    }
}
