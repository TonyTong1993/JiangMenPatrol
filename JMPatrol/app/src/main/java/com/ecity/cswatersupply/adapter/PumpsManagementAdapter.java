package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;

/***  
 * Created by MaoShouBei on 2017/5/12.
 */

public class PumpsManagementAdapter extends ArrayListAdapter<PumpInsSelectValue> {
    private LayoutInflater inflater;

    public PumpsManagementAdapter(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        PumpInsSelectValue model = getList().get(position);

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.lv_item_pump, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pumpNO.setText(model.getPumpNO());
        viewHolder.pumpName.setText(model.getAlias());
        viewHolder.pumpAddress.setText(model.getPumpRoad());

        return convertView;
    }


    public class ViewHolder {
        private TextView pumpName;
        private TextView pumpNO;
        private TextView pumpAddress;

        public ViewHolder(View v) {
            pumpNO = (TextView) v.findViewById(R.id.tv_pump_no);
            pumpName = (TextView) v.findViewById(R.id.tv_pump_name);
            pumpAddress = (TextView) v.findViewById(R.id.tv_pump_address);
        }
    }
}
