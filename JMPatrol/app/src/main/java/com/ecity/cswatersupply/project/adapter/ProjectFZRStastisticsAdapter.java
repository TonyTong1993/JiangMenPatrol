package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.network.response.ProjectFZRStastisticsModel;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ProjectFZRStastisticsAdapter extends BaseAdapter {

    private List<ProjectFZRStastisticsModel> data;
    private LayoutInflater inflater;
    private TextView name,toDeal,finished,postpone,overtime;

    public ProjectFZRStastisticsAdapter(Context context, List<ProjectFZRStastisticsModel> data) {
        this.data = data;
        setFirstRow();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ProjectFZRStastisticsModel model = data.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.item_table_fzr_summary, null);
        }
        initViews(view);
        setColor(Color.BLACK);
        name.setText(model.getUsername());
        toDeal.setText(model.getProallnums());
        finished.setText(model.getProendnums());
        postpone.setText(model.getProingnums());
        overtime.setText(model.getProovernums());
        if (position == 0) {
            view.setBackgroundResource(R.color.head_bg);
            setColor(Color.WHITE);
        } else {
            if (position % 2 != 0) {
                view.setBackgroundColor(Color.argb(250, 255, 255, 255));
            } else {
                view.setBackgroundColor(Color.argb(250, 224, 243, 250));
            }
        }

        return view;
    }

    private void initViews(View view) {
        view.setBackgroundColor(Color.WHITE);
        name = (TextView) view.findViewById(R.id.summary_table_name);
        toDeal = (TextView) view.findViewById(R.id.summary_table_to_deal);
        finished = (TextView) view.findViewById(R.id.summary_table_finished);
        postpone = (TextView) view.findViewById(R.id.summary_table_postpone);
        overtime = (TextView) view.findViewById(R.id.summary_table_overtime);
    }

    private void setColor(int black) {
        name.setTextColor(black);
        toDeal.setTextColor(black);
        finished.setTextColor(black);
        postpone.setTextColor(black);
        overtime.setTextColor(black);
    }

    private void setFirstRow() {
        ProjectFZRStastisticsModel model = new ProjectFZRStastisticsModel();
        model.setUsername("负责人");
        model.setProallnums("总数");
        model.setProendnums("已完成");
        model.setProingnums("未完成");
        model.setProovernums("超期数");
        data.add(0, model);
    }
}
