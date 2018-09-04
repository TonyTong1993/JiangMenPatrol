package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicFilterView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBtnsView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemCustomView;

/**
 * 新工单界面适配器
 * 
 * 二级界面由公共区、自定义区和按钮区三部分组成。公共区：所有工单公用的界面；自定义区：根据分组或者工单状态不同，自定义布局；
 * 按钮区：根据工单状态，自定义组装按钮。
 * 
 * @author qiwei
 *
 */
public class CustomWorkOrderAdapter extends ArrayListAdapter<WorkOrder> {
    private LayoutInflater mInflater;

    public CustomWorkOrderAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        private WorkOrderListItemBasicFilterView basicView;
        private WorkOrderListItemCustomView customView;
        private WorkOrderListItemBtnsView btnsView;

        public ViewHolder(View v) {
            basicView = (WorkOrderListItemBasicFilterView) v.findViewById(R.id.view_list_item_common);
            customView = (WorkOrderListItemCustomView) v.findViewById(R.id.view_list_item_custom);
            btnsView = (WorkOrderListItemBtnsView) v.findViewById(R.id.view_list_item_btns);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_workorderlist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.basicView.setView(getList().get(position));
        viewHolder.customView.setView(getList().get(position), WorkOrderGroupEnum.values()[position]);
        viewHolder.btnsView.setView(getList().get(position),null);

        return convertView;
    }
}
