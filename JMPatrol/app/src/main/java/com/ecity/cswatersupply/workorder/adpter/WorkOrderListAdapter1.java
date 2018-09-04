package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBtnsView1;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.List;

public class WorkOrderListAdapter1 extends ArrayListAdapter<WorkOrder> {
    private LayoutInflater inflater;
    private List<WorkOrder> listData;
    private boolean isNeedBtnViews;

    public WorkOrderListAdapter1(Context context, List<WorkOrder> listData, boolean isNeedBtnViews) {
        super(context);
        this.setList(listData);
        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
        this.isNeedBtnViews = isNeedBtnViews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        WorkOrder itemData = listData.get(position);
        String btnStr = itemData.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS);
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.lv_item_workorderlist, null);
            viewHolder = new ViewHolder(convertView);
            setNullViewHolder(viewHolder, itemData);
            convertView.setTag(R.id.tag_view, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.id.tag_view);
            setNotNullViewHolder(viewHolder, itemData);
        }
        WorkOrderGroupEnum group = WorkOrderGroupEnum.values()[0];
        setWorkOrderData(viewHolder, itemData, group);
        return convertView;
    }

    private void setEmptyContent(ViewHolder viewHolder) {
        viewHolder.tvBlankContent.setVisibility(View.VISIBLE);
        viewHolder.basicView.setVisibility(View.GONE);
        viewHolder.btnsView.setVisibility(View.GONE);
//        viewHolder.customView.setVisibility(View.GONE);
    }

    private void setNotNullViewHolder(ViewHolder viewHolder, WorkOrder data) {
        if (data.toString().isEmpty()) {
            setEmptyContent(viewHolder);
        } else {
            viewHolder.basicView.setView(data);
            viewHolder.basicView.getTag();
            if (isNeedBtnViews) {
                loadViewHolderBtns(viewHolder, data);
            } else {
                viewHolder.btnsView.setVisibility(View.GONE);
            }
        }
    }

    private void setNullViewHolder(ViewHolder viewHolder, WorkOrder data) {
        if (data.toString().isEmpty()) {
            setEmptyContent(viewHolder);
        } else {
            viewHolder.basicView.setView(data);
            viewHolder.basicView.setTag("WorkOrderListItemBasicView");
            if (isNeedBtnViews) {
                loadViewHolderBtns(viewHolder, data);
            } else {
                viewHolder.btnsView.setVisibility(View.GONE);
            }
        }
    }

    private void loadViewHolderBtns(ViewHolder viewHolder, WorkOrder data) {
        List<WorkOrderBtnModel> workOrderBtns = data.getWorkOrderBtns();
        if(ListUtil.isEmpty(workOrderBtns) && workOrderBtns.size() == 0) {
            viewHolder.btnsView.setVisibility(View.GONE);
        } else {
            viewHolder.btnsView.setVisibility(View.VISIBLE);
            viewHolder.btnsView.setView(data);
        }
        viewHolder.btnsView.setTag(data.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS));
    }

    private void setWorkOrderData(ViewHolder viewHolder, WorkOrder itemData, WorkOrderGroupEnum group) {
        viewHolder.basicView.setDisplayInfo(itemData, group);
        viewHolder.btnsView.setOnBtnClickListener(itemData);
    }

    private static class ViewHolder {
        private WorkOrderListItemBasicView basicView;
//        private WorkOrderListItemCustomView customView;
        private WorkOrderListItemBtnsView1 btnsView;
        private TextView tvBlankContent;

        public ViewHolder(View v) {
            basicView = (WorkOrderListItemBasicView) v.findViewById(R.id.view_list_item_common);
//            customView = (WorkOrderListItemCustomView) v.findViewById(R.id.view_list_item_custom);
            btnsView = (WorkOrderListItemBtnsView1) v.findViewById(R.id.view_list_item_btns);
            tvBlankContent = (TextView) v.findViewById(R.id.tv_content_blank);
        }
    }
}
