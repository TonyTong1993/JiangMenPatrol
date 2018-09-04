package com.ecity.cswatersupply.adapter.planningtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo;
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo.PointStatusType;

public class PointPartLegendListAdapter extends ArrayListAdapter<PointPartLegendInfo> {

    private LayoutInflater mInflater;
    private Context mContext;

    public PointPartLegendListAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_legend, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }
        PointPartLegendInfo info = mList.get(position);
        viewHolder.point_ststus_txt.setText(getPointPartStatus(info.getPointStatus()));
        viewHolder.point_ststus_num.setText(info.getPointNum()+"");
        int resId = R.drawable.map_location_red;
        if (getPointPartStatus(info.getPointStatus()).equals(mContext.getResources().getString(R.string.planningtask_arrived))) {
            resId = R.drawable.map_location_hl;
        } else if (getPointPartStatus(info.getPointStatus()).equals(mContext.getResources().getString(R.string.planningtask_has_feedback))) {
            resId = R.drawable.map_location_blue;
        }
        viewHolder.point_ststus_img.setBackgroundResource(resId);
        viewHolder.point_ststus_loadType.setOnCheckedChangeListener(new ButtonClickListener(info.getKey()));
        if (info.getIsShow() == 0) {
            viewHolder.point_ststus_loadType.setChecked(false);
        }else{
            viewHolder.point_ststus_loadType.setChecked(true);
        }
        
        return convertView;
    }
    
    class ButtonClickListener implements OnCheckedChangeListener {
        private int key;
        public ButtonClickListener(int key) {
            super();
            this.key = key;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            // 从新加载地图上的地
            UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_ALLSTATESPOINTPART_STATUS);
            if (isChecked) {
                event.setData(key+","+"1");
            }else{
                event.setData(key+","+"0");
            }
            EventBusUtil.post(event);
        }
    }

    private String getPointPartStatus(PointStatusType pointStatus) {
        String statusString = null ;
        switch (pointStatus) {
            case UN_ARRIVED:
                statusString = mContext.getResources().getString(R.string.planningtask_not_arrived);
                break;
            case ARRIVED:
                statusString = mContext.getResources().getString(R.string.planningtask_arrived);
                break;
            case FEEDBACKED:
                statusString = mContext.getResources().getString(R.string.planningtask_has_feedback);
                break;
            default:
                break;
        }
        return statusString;
    }

    public static class ViewHolder {
        public TextView point_ststus_txt, point_ststus_num;
        public ImageView point_ststus_img;
        public ToggleButton point_ststus_loadType;

        public ViewHolder(View v) {
            point_ststus_txt = (TextView) v.findViewById(R.id.point_ststus_txt);
            point_ststus_num = (TextView) v.findViewById(R.id.point_ststus_num);
            point_ststus_img = (ImageView) v.findViewById(R.id.point_ststus_img);
            point_ststus_loadType = (ToggleButton) v.findViewById(R.id.point_ststus_loadType);
        }
    }
}
