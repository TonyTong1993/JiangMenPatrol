package com.ecity.cswatersupply.menu.map;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.StatusInfoModel;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UserDisplayUtil;

public class PersonStatusControlOperator{
    public static final int MONITOR_OPERATOR_TYPE_PERSON = 0;
    public static final int MONITOR_OPERATOR_TYPE_BUS = 1;
    private final static String PATROL_TYPE_OFFLINE = "0";
    private StatusControlListAdapter statusControlListAdapter = null;
    private MapActivity activity;
    private PopupWindow popupWindow = null;
    public List<StatusInfoModel> sourceList;
    public boolean isshowing = false;
    private OnStatusControlListener statusControlListener;
    private int operatorType;
    public interface OnStatusControlListener {
        void onStatusControlChanged(StatusInfoModel statusInfo);
    }

    public PersonStatusControlOperator(OnStatusControlListener statusControlListener) {
        this.statusControlListener = statusControlListener;
    }

    public void ClearPopwindow() {
        if (null == popupWindow) {
            return;
        }
        popupWindow.dismiss();
    }

    public boolean showStatus(List<StatusInfoModel> sourceList, IMapOperationContext fragment, int operatorType) {
        this.sourceList = sourceList;
        this.operatorType = operatorType;
        if (null == sourceList) {
            return false;
        }
        DisplayMetrics dm = new DisplayMetrics();
        fragment.getContext().getWindowManager().getDefaultDisplay().getMetrics(dm);
        activity = (MapActivity) fragment.getContext();
        // 弹出图层选择窗口
        if (isshowing) {
            popupWindow.dismiss();
        } else {
            showWindow(dm.widthPixels, sourceList, operatorType);
        }

        return true;
    }

    private void showWindow(int screenWidth, List<StatusInfoModel> layerSource, int operatorType) {

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_layercontrol, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(linearLayout, screenWidth, LayoutParams.WRAP_CONTENT);
        }
        ListView listView = (ListView) linearLayout.findViewById(R.id.layerList);
        statusControlListAdapter = new StatusControlListAdapter(activity, layerSource, statusControlListener, operatorType);
        listView.setAdapter(statusControlListAdapter);
        statusControlListAdapter.notifyDataSetChanged();

        // 点击外部时，弹窗消失
        popupWindow.setOutsideTouchable(true);
        // 点击返回键时，弹窗消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        // 设置弹出和消失动画
        popupWindow.setAnimationStyle(R.style.layercontrolwin_anim_style);
        // 关闭时，修改isShowing的值
        popupWindow.setOnDismissListener(onDismissListener);
        popupWindow.showAsDropDown(activity.findViewById(R.id.view_title_mapactivity), 0, 0);
        popupWindow.update();
        isshowing = true;
    }

    public void setListData(List<StatusInfoModel> sourceList){
        this.sourceList = sourceList;
    }

    OnDismissListener onDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss() {
            isshowing = false;
        }
    };

    class StatusControlListAdapter extends BaseAdapter {
        private List<StatusInfoModel> mSourceList;
        private LayoutInflater mInflater = null;
        private OnStatusControlListener statusControlListener;
        private int operatorType;
        public StatusControlListAdapter(Context mContext, List<StatusInfoModel> sourceList, OnStatusControlListener statusControlListener, int operatorType) {
            super();
            this.mSourceList = sourceList;
            this.mInflater = LayoutInflater.from(mContext);
            this.statusControlListener = statusControlListener;
            this.operatorType = operatorType;
        }

        @Override
        public int getCount() {
            return null == mSourceList ? 0 : mSourceList.size();
        }

        @Override
        public StatusInfoModel getItem(int position) {
            return mSourceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_list_patrol_status_control, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            StatusInfoModel entity = getItem(position);
            String status = entity.getState();
            if (MONITOR_OPERATOR_TYPE_PERSON == operatorType) {
                if (PATROL_TYPE_OFFLINE.equals(status)) {
                    holder.imageView.setImageResource(R.drawable.patrol_off);
                    holder.status.setText(ResourceUtil.getStringById(R.string.user_offline));
                } else {
                    holder.imageView.setImageResource(R.drawable.patrol_on);
                    holder.status.setText(ResourceUtil.getStringById(R.string.user_online));
                }
            } else {
                if (PATROL_TYPE_OFFLINE.equals(status)) {
                    holder.imageView.setImageResource(R.drawable.patrol_bus_offline);
                    holder.status.setText(ResourceUtil.getStringById(R.string.user_offline));
                } else {
                    holder.imageView.setImageResource(R.drawable.patrol_bus_online);
                    holder.status.setText(ResourceUtil.getStringById(R.string.user_online));
                }
            }
            holder.statusNumber.setText(String.valueOf(entity.getNumber()));
            holder.toggleButton.setOnCheckedChangeListener(new ButtonClickListener(entity,statusControlListener));
            holder.toggleButton.setChecked(entity.isDisplay());

            return convertView;
        }
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView status;
        private TextView statusNumber;
        private ToggleButton toggleButton;

        public ViewHolder(View convertView) {
            this.imageView = (ImageView) convertView.findViewById(R.id.layer_icon);
            this.status = (TextView) convertView.findViewById(R.id.status);
            this.statusNumber = (TextView) convertView.findViewById(R.id.status_number);
            this.toggleButton = (ToggleButton) convertView.findViewById(R.id.tv_loadType);
        }
    }

    class ButtonClickListener implements OnCheckedChangeListener {
        private  StatusInfoModel statusInfo;
        private  OnStatusControlListener statusControlListener;
        public ButtonClickListener( StatusInfoModel statusInfo,OnStatusControlListener statusControlListener) {
            this.statusInfo = statusInfo;
            this.statusControlListener = statusControlListener;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            // 新起一个线程加载地图图层
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        statusInfo.setDisplay(isChecked);
                        if(null != statusControlListener){
                            statusControlListener.onStatusControlChanged(statusInfo);
                        }
                     }catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
