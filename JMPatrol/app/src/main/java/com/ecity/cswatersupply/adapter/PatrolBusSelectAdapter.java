package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;


public class PatrolBusSelectAdapter extends ArrayListAdapter<String> {

    private LayoutInflater inflater;

    public PatrolBusSelectAdapter(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String model = getList().get(position);
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_patrol_bus_select, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.patrolBusUserName.setText(model);

        return convertView;
    }

    public class ViewHolder {
        private TextView patrolBusUserName;

        public ViewHolder(View v) {
            patrolBusUserName = (TextView) v.findViewById(R.id.patrol_bus_username);
        }
    }
}
