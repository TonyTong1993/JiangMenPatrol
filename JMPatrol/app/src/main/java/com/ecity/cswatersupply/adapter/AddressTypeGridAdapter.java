package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class AddressTypeGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> types;

    public AddressTypeGridAdapter(Context context, List<String> types) {
        this.inflater = LayoutInflater.from(context);
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_grid_address_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.addressType.setText(types.get(position));
        holder.addressType.setBackgroundColor(ResourceUtil.getColorById(R.color.ics_blue));
        return convertView;
    }

    static class ViewHolder {
        private TextView addressType;

        public ViewHolder(View view) {
            addressType = (TextView) view.findViewById(R.id.address_type);
        }
    }

}
