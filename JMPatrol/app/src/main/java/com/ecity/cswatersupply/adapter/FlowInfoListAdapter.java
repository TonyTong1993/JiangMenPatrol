package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.FlowInfoBean;

public class FlowInfoListAdapter extends ArrayListAdapter<FlowInfoBean> {
    private LayoutInflater inflater;
    
    public FlowInfoListAdapter(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_flow_info, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.addTime.setText(getList().get(position).getAddTime());
        viewHolder.addMan.setText(getList().get(position).getAddMan());
        viewHolder.type.setText(getList().get(position).getType());
        viewHolder.state.setText(getList().get(position).getState());
        return convertView;
    }

    public class ViewHolder {
        private TextView addTime, addMan, type, state;

        public ViewHolder(View v) {
            addTime = (TextView) v.findViewById(R.id.tv_flow_addtime);
            addMan = (TextView) v.findViewById(R.id.tv_flow_addman_detail);
            type = (TextView) v.findViewById(R.id.tv_flow_type);
            state = (TextView) v.findViewById(R.id.tv_flow_state_detail);
        }
    }
}
