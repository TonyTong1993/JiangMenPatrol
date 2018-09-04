package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.project.view.ProjectSummaryView;
import com.ecity.cswatersupply.project.view.ProjectWaterMeterSummaryView;

import org.json.JSONObject;

public class ProjectSummaryListAdapter extends ArrayListAdapter<JSONObject> {
    private ProjectSummaryView summaryViewHolder;
    private ProjectWaterMeterSummaryView summaryWaterViewHolder;

    public ProjectSummaryListAdapter(Context context, ProjectSummaryView summaryViewHolder) {
        super(context);
        this.summaryViewHolder = summaryViewHolder;
    }

    public ProjectSummaryListAdapter(Context context, ProjectWaterMeterSummaryView summaryWaterViewHolder) {
        super(context);
        this.summaryWaterViewHolder = summaryWaterViewHolder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = summaryViewHolder != null ? summaryViewHolder.inflate(getContext()) : summaryWaterViewHolder.inflate(getContext());
        }

        JSONObject item = getList().get(position);
        if (summaryViewHolder!=null){
            summaryViewHolder.show(convertView, item);
        }else{
            summaryWaterViewHolder.show(convertView, item);
        }

        return convertView;
    }
}
