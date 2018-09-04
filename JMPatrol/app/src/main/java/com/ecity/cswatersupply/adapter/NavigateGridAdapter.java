package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.NaviModel;

public class NavigateGridAdapter extends BaseAdapter {
    private List<NaviModel> naviApps;
    private LayoutInflater inflater;

    public NavigateGridAdapter(Context context, List<NaviModel> naviApps) {
        this.naviApps = naviApps;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return naviApps.size();
    }

    @Override
    public Object getItem(int position) {
        return naviApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_navigate_sheet, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.appIcon.setBackground(naviApps.get(position).getIcon());
        holder.appName.setText(naviApps.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        private ImageView appIcon;
        private TextView appName;

        public ViewHolder(View view) {
            appIcon = (ImageView) view.findViewById(R.id.app_icon);
            appName = (TextView) view.findViewById(R.id.app_name);
        }
    }
}
