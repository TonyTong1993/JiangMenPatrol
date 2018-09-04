package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class HistoryRecordListAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<String> items;

    public HistoryRecordListAdapter(Context context, List<String> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.activity_poi_search_history_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.addressname.setText(items.get(position).toString());
        return convertView;
    }

    private class ViewHolder {
        private TextView addressname;

        public ViewHolder(View v) {
            addressname = (TextView)v.findViewById(R.id.addressname);
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

}
