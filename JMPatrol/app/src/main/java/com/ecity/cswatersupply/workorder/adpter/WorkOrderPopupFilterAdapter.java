package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderPopupMenuModel;

/**
 * 
 * @author qiwei
 *
 */
public class WorkOrderPopupFilterAdapter extends ArrayListAdapter<WorkOrderPopupMenuModel> {
    private LayoutInflater mLayoutInflater;

    public WorkOrderPopupFilterAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        WorkOrderPopupMenuModel menu = mList.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_workorder_list_popupmenu, null);
            holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
            holder.notificationView = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_menu.setText(menu.getName());
        holder.notificationView.setVisibility(View.GONE);
        return convertView;
    }

    private static class Holder {
        TextView tv_menu;
        TextView notificationView;
    }
}
