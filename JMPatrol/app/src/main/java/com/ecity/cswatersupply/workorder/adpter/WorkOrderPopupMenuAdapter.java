package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.menu.AppMenu;

public class WorkOrderPopupMenuAdapter extends ArrayListAdapter<AppMenu> {
    private LayoutInflater mLayoutInflater;

    public WorkOrderPopupMenuAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        AppMenu menu = mList.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_workorder_popupmenu, null);
            holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_menu.setText(menu.getName());
        return convertView;
    }

    private static class Holder {
        TextView tv_menu;
    }
}
