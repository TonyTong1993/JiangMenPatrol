package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBtnsView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemCustomView;

import java.util.List;


public class WorkOrderListAdapter extends ArrayListAdapter<WorkOrder> {
    private LayoutInflater inflater;
    private int groupPosition;
    private List<WorkOrder> listData;
    private boolean isNeedBtnViews;
    private String nextStepId;
    private int selectedItemPosition = -1;
    private Context mContext;

    public WorkOrderListAdapter(Context context, List<WorkOrder> listData, int groupPosition, boolean isNeedBtnViews, String nextStepId) {
        super(context);
        mContext = context;
        //this.setList(listData);
        this.inflater = LayoutInflater.from(context);
        this.groupPosition = groupPosition;
        this.listData = listData;
        this.isNeedBtnViews = isNeedBtnViews;
        this.nextStepId = nextStepId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        WorkOrder itemData = listData.get(position);
        String currentWorkOrderState = itemData.getAttributes().get(WorkOrder.KEY_DISPLAY_STATE);
        String btnStr = itemData.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS);
        WorkOrderGroupEnum group = WorkOrderGroupEnum.values()[groupPosition];
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.lv_item_workorderlist, null);
            viewHolder = new ViewHolder(convertView);
            String btnTag = (String) viewHolder.btnsView.getTag();
            String bodyTag = (String) viewHolder.customView.getTag();
            WorkOrderGroupEnum groupTag = (WorkOrderGroupEnum) viewHolder.basicView.getTag();
            setNullViewHolder(groupTag, group, viewHolder, itemData, currentWorkOrderState, btnTag, bodyTag, btnStr, position);
            convertView.setTag(R.id.tag_view, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.id.tag_view);
            String btnTag = (String) viewHolder.btnsView.getTag();
            String bodyTag = (String) viewHolder.customView.getTag();
            WorkOrderGroupEnum groupTag = (WorkOrderGroupEnum) viewHolder.basicView.getTag();
            setNotNullViewHolder(groupTag, group, viewHolder, itemData, currentWorkOrderState, btnTag, bodyTag, btnStr, position);
        }
        setWorkOrderData(viewHolder, itemData, group);
        Drawable drawable;
        if (selectedItemPosition == position) {
            drawable = mContext.getResources().getDrawable(R.drawable.css_bg_lv_item);
            setSelectedItemPosition(-1);
        } else {
            drawable = mContext.getResources().getDrawable(R.color.transparent);
        }
        convertView.setBackground(drawable);
        return convertView;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    private void setEmptyContent(ViewHolder viewHolder) {
        viewHolder.tvBlankContent.setVisibility(View.VISIBLE);
        viewHolder.basicView.setVisibility(View.GONE);
        viewHolder.btnsView.setVisibility(View.GONE);
        viewHolder.customView.setVisibility(View.GONE);
    }

    private void setNotNullViewHolder(WorkOrderGroupEnum groupTag, WorkOrderGroupEnum group, ViewHolder viewHolder, WorkOrder data, String state, String btnTag, String bodyTag, String btnStr, int position) {
        if (data.toString().isEmpty()) {
            setEmptyContent(viewHolder);
        } else {
            if (groupTag != group) {
                viewHolder.basicView.setView(data);
                viewHolder.basicView.setTag(group);
            }
            if (!bodyTag.equals(state)) {
                viewHolder.customView.setView(data, WorkOrderGroupEnum.values()[groupPosition]);
                viewHolder.customView.setTag(state);
            }
            if (isNeedBtnViews) {
                if (!btnTag.equals(btnStr)) {
                    loadViewHolderBtns(viewHolder, data);
                }
            } else {
                viewHolder.btnsView.setVisibility(View.GONE);
            }

        }
    }

    private void setNullViewHolder(WorkOrderGroupEnum groupTag, WorkOrderGroupEnum group, ViewHolder viewHolder, WorkOrder data, String state, String btnTag, String bodyTag,
            String btnStr, int position) {
        if (data.toString().isEmpty()) {
            setEmptyContent(viewHolder);
        } else {
            viewHolder.basicView.setView(data);
            viewHolder.basicView.setTag(group);
            viewHolder.customView.setView(data, WorkOrderGroupEnum.values()[groupPosition]);
            viewHolder.customView.setTag(state);
            if (isNeedBtnViews) {
                loadViewHolderBtns(viewHolder, data);
            } else {
                viewHolder.btnsView.setVisibility(View.GONE);
            }
        }
    }

    private void loadViewHolderBtns(ViewHolder viewHolder, WorkOrder data) {
        if (WorkOrderGroupEnum.values()[groupPosition] != WorkOrderGroupEnum.COMLETE) {
            viewHolder.btnsView.setVisibility(View.VISIBLE);
            viewHolder.btnsView.setView(data,nextStepId);
        } else {
            viewHolder.btnsView.setVisibility(View.GONE);
        }
        viewHolder.btnsView.setTag(data.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS));
    }

    private void setWorkOrderData(ViewHolder viewHolder, WorkOrder itemData, WorkOrderGroupEnum group) {
        viewHolder.basicView.setDisplayInfo(itemData, group);
        viewHolder.customView.setDisplayInfo(itemData, group);
        viewHolder.btnsView.setOnBtnClickListener(itemData, group);
    }

    private static class ViewHolder {
        private WorkOrderListItemBasicView basicView;
        private WorkOrderListItemCustomView customView;
        private WorkOrderListItemBtnsView btnsView;
        private TextView tvBlankContent;

        public ViewHolder(View v) {
            basicView = (WorkOrderListItemBasicView) v.findViewById(R.id.view_list_item_common);
            customView = (WorkOrderListItemCustomView) v.findViewById(R.id.view_list_item_custom);
            btnsView = (WorkOrderListItemBtnsView) v.findViewById(R.id.view_list_item_btns);
            tvBlankContent = (TextView) v.findViewById(R.id.tv_content_blank);
        }
    }
}
