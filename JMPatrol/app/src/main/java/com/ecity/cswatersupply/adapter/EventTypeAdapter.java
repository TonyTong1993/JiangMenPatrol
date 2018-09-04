package com.ecity.cswatersupply.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class EventTypeAdapter extends BaseAdapter {
    private List<String> menuList;
    private ArrayList<Integer> menuImageList;
    private LayoutInflater gridInflater;

    public EventTypeAdapter(List<String> menuList, ArrayList<Integer> menuImageList, Context context) {
        super();
        this.menuList = menuList;
        this.menuImageList = menuImageList;
        gridInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = gridInflater.inflate(R.layout.item_eventreport_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvListItem.setText(menuList.get(position));
        int imageIndex = position%4;
        holder.ivListItem.setImageResource(menuImageList.get(imageIndex));
        return convertView;
    }

    private class ViewHolder {
        public TextView tvListItem;
        public ImageView ivListItem;

        public ViewHolder(View v) {
            tvListItem = (TextView) v.findViewById(R.id.iv_eventype_text);
            ivListItem = (ImageView) v.findViewById(R.id.iv_eventype_image);
        }
    }
}
