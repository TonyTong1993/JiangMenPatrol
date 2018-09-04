package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.SumaryPieDataModel;

import java.util.List;

public class ProjectAnalysisAdapter extends BaseAdapter {
    private Context context;
    private List<SumaryPieDataModel> list;

    public ProjectAnalysisAdapter(Context context, List<SumaryPieDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_project_count, null);
            holder = new ViewHolder();
            holder.tvFirst = (TextView) convertView.findViewById(R.id.tv_first);
            holder.tvSecond = (TextView) convertView.findViewById(R.id.tv_second);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SumaryPieDataModel sumaryPieDataModel = list.get(position);
        holder.tvFirst.setText(sumaryPieDataModel.getPieCategory());
        holder.tvSecond.setText(sumaryPieDataModel.getPieData());

        return convertView;
    }

    class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;
    }
}
