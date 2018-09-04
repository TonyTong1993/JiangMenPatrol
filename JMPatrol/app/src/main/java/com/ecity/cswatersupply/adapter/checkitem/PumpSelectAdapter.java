package com.ecity.cswatersupply.adapter.checkitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;

public class PumpSelectAdapter extends ArrayListAdapter<PumpInsSelectValue> {
    private LayoutInflater inflater;

    public PumpSelectAdapter(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        PumpInsSelectValue model = getList().get(position);

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_pump_select, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pumpName.setText(model.getAlias());

        return convertView;
    }

    public class ViewHolder {
        private TextView pumpName;

        public ViewHolder(View v) {
            pumpName = (TextView) v.findViewById(R.id.pump_name);
        }
    }
}