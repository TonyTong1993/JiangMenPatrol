package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.ProjectLogBean;

import java.util.List;

public class ProjectLogAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private final List<ProjectLogBean> listData;

    public ProjectLogAdapter(List<ProjectLogBean> listData, Context context) {
        super();
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_workorder_operation_log, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nodeName.setText(listData.get(position).getNodeName());
        viewHolder.username.setText(listData.get(position).getUsername());
        viewHolder.processtime.setText(listData.get(position).getProcesstime());
        viewHolder.remark.setText(listData.get(position).getRemark());
        
        return convertView;
    }

    public class ViewHolder {
        private TextView nodeName, username, processtime, remark;

        public ViewHolder(View v) {
            nodeName = (TextView) v.findViewById(R.id.log_date_back_node_id);
            username = (TextView) v.findViewById(R.id.log_date_back_username);
            processtime = (TextView) v.findViewById(R.id.log_date_back_processtime);
            remark = (TextView) v.findViewById(R.id.log_date_back_description);
        }
    }
}
