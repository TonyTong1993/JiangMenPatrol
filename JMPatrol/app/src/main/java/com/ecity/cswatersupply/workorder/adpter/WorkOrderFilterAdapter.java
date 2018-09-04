package com.ecity.cswatersupply.workorder.adpter;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;

/**
 * 新工单界面适配器
 * 
 * 二级界面由公共区、自定义区和按钮区三部分组成。公共区：所有工单公用的界面；自定义区：根据分组或者工单状态不同，自定义布局；
 * 按钮区：根据工单状态，自定义组装按钮。
 * 
 * @author gaokai
 *
 */
public class WorkOrderFilterAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    protected Context mContext;
    private Map<String,String> workOrderCount;

    public WorkOrderFilterAdapter(Context context, Map<String,String> workOrderCount) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.workOrderCount = workOrderCount;
    }

    /**
     * 设置图标和指示器
     */
    private void setGroupDrawable(GroupViewHolder groupViewHolder, WorkOrderGroupEnum page) {
        boolean hasNew = page.hasNew();
        Drawable d = null;
        if (hasNew) {
            d = mContext.getResources().getDrawable(R.drawable.tips_red_no_text);
        } else {
            d = mContext.getResources().getDrawable(R.drawable.indicator_collapsed);
        }
        groupViewHolder.ivIndicator.setImageDrawable(d);
        groupViewHolder.ivGroupIcon.setImageDrawable(mContext.getResources().getDrawable(page.getIconResId()));
    }

    private void setGroupTitle(GroupViewHolder groupViewHolder, WorkOrderGroupEnum page, int groupPosition) {
        groupViewHolder.tvGroup.setText(mContext.getResources().getString(page.getTitle()));
        String msg = "";
        String count="";
        if (groupPosition==0) {
            count = workOrderCount.get("waitamount");
        } else if (groupPosition==1) {
            count = workOrderCount.get("executeamount");
        } else {
            count = workOrderCount.get("finishamount");
        }
        if (Integer.valueOf(count) != 0) {
            msg = ResourceUtil.getStringById(R.string.workorder_tips_count, Integer.valueOf(count));
        } else {
            msg = ResourceUtil.getStringById(R.string.workorder_empty, Integer.valueOf(count));

        }
        groupViewHolder.tvTips.setText(msg);
        int color = ResourceUtil.getColorById(page.getColorResId());
        groupViewHolder.tvTips.setTextColor(color);
    }

    private static class GroupViewHolder {
        private ImageView ivGroupIcon;
        private TextView tvGroup;
        private TextView tvTips;
        private ImageView ivIndicator;

        public GroupViewHolder(View convertView) {
            ivGroupIcon = (ImageView) convertView.findViewById(R.id.iv_group);
            ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            tvGroup = (TextView) convertView.findViewById(R.id.tv_group_label);
            tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
        }
    }

    @Override
    public int getCount() {
        return WorkOrderGroupEnum.values().length;
    }

    @Override
    public Object getItem(int position) {
        return WorkOrderGroupEnum.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        WorkOrderGroupEnum page = WorkOrderGroupEnum.values()[position];
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_workorder_expandablelistview_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        setGroupDrawable(groupViewHolder, page);
        setGroupTitle(groupViewHolder, page, position);
        return convertView;
    }
}
