package com.ecity.cswatersupply.menu.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class MapPopupMenuAdapter extends ArrayListAdapter<AMapMenu> {
    private LayoutInflater mLayoutInflater;

    public MapPopupMenuAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        AMapMenu menu = mList.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_list_popupmenu, null);
            holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
            holder.iv_menu = (ImageView) convertView.findViewById(R.id.iv_menu);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_menu.setText(menu.getName());
        holder.iv_menu.setImageResource(ResourceUtil.getDrawableResourceId(menu.getIconName()));
        return convertView;
    }

    private static class Holder {
        ImageView iv_menu;
        TextView tv_menu;
    }
}
