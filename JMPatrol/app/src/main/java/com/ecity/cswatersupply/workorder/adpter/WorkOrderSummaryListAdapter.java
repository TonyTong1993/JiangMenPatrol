package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryPie;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryPieBean;

public class WorkOrderSummaryListAdapter extends ArrayListAdapter<WorkOrderSummaryPieBean> {
    private LayoutInflater inflater;

    public WorkOrderSummaryListAdapter(Context context, WorkOrderSummaryPie listData) {
        super(context);
        this.setList(listData.getPieBean());
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        WorkOrderSummaryPieBean bean = getList().get(position);
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_workorder_summary, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCategory.setText(bean.getPieCategory());
        viewHolder.tvAmount.setText(bean.getPieData());
       
        return convertView;
    }

    public class ViewHolder {
        private TextView tvCategory, tvAmount;

        public ViewHolder(View v) {
            tvCategory = (TextView) v.findViewById(R.id.summary_item_name);
            tvAmount = (TextView) v.findViewById(R.id.summary_item_amount);
        }
    }
}