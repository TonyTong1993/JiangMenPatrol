package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.WorkOrderOperationLogBean;
import com.z3app.android.util.StringUtil;

public class WorkOrderOperationLogAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private final List<WorkOrderOperationLogBean> listData;
    private Context context;

    public WorkOrderOperationLogAdapter(List<WorkOrderOperationLogBean> listData, Context context) {
        super();
        this.listData = listData;
        this.context = context;
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
        viewHolder.nodeid.setText(listData.get(position).getNodeid());
        if (StringUtil.isEmpty(listData.get(position).getDealusername())) {
            viewHolder.username.setText(listData.get(position).getUsername());
        }else{
            viewHolder.username.setText(listData.get(position).getUsername() +"  " +context.getResources().getString(R.string.next_processing_people)+listData.get(position).getDealusername());
        }
        viewHolder.processtime.setText(listData.get(position).getProcesstime());
        viewHolder.description.setText(listData.get(position).getDescription());
        
        return convertView;
    }

    public class ViewHolder {
        private TextView nodeid, username, processtime, description;

        public ViewHolder(View v) {
            nodeid = (TextView) v.findViewById(R.id.log_date_back_node_id);
            username = (TextView) v.findViewById(R.id.log_date_back_username);
            processtime = (TextView) v.findViewById(R.id.log_date_back_processtime);
            description = (TextView) v.findViewById(R.id.log_date_back_description);
        }
    }
}
