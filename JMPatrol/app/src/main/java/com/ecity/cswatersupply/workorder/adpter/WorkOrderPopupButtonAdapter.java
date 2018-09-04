package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;

public class WorkOrderPopupButtonAdapter extends ArrayListAdapter<WorkOrderBtnModel> {
    private LayoutInflater mLayoutInflater;

    public WorkOrderPopupButtonAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        WorkOrderBtnModel button = mList.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_workorder_popupmenu, null);
            holder.tv_button = (TextView) convertView.findViewById(R.id.tv_menu);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_button.setText(button.getTaskName());
        return convertView;
    }

    private static class Holder {
        TextView tv_button;
    }
}
