package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryBar;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryBarBean;

public class WorkOrderSummaryTableAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<WorkOrderSummaryBar> lists;
    private TextView name, toDeal, dealing, finished, postpone, overtime;

    public WorkOrderSummaryTableAdapter(Context context, List<WorkOrderSummaryBar> lists) {
        super();
        this.lists = lists;
        setFirstRowData();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        WorkOrderSummaryBar summaryBar = lists.get(index);
        if (view == null) {
            view = inflater.inflate(R.layout.item_table_summary_list, null);
        }
        initViews(view);
        setColor(Color.BLACK);
        name.setText(summaryBar.getPersonName());
        toDeal.setText(summaryBar.getBarDatas().getToDealAmount());
        dealing.setText(summaryBar.getBarDatas().getDealingAmount());
        finished.setText(summaryBar.getBarDatas().getFinishedAmount());
        postpone.setText(summaryBar.getBarDatas().getPostponeAmount());
        overtime.setText(summaryBar.getBarDatas().getOvertimeAmount());
        if (index == 0) {
            view.setBackgroundResource(R.color.head_bg);
            setColor(Color.WHITE);
        } else {
            if (index % 2 != 0) {
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
        dealing = (TextView) view.findViewById(R.id.summary_table_dealing);
        finished = (TextView) view.findViewById(R.id.summary_table_finished);
        postpone = (TextView) view.findViewById(R.id.summary_table_postpone);
        overtime = (TextView) view.findViewById(R.id.summary_table_overtime);
    }

    private void setColor(int black) {
        name.setTextColor(black);
        toDeal.setTextColor(black);
        dealing.setTextColor(black);
        finished.setTextColor(black);
        postpone.setTextColor(black);
        overtime.setTextColor(black);
    }

    private void setFirstRowData() {
        WorkOrderSummaryBar firstData = new WorkOrderSummaryBar();
        firstData.setPersonName(ResourceUtil.getStringById(R.string.summary_fix_people));
        WorkOrderSummaryBarBean barDatas = new WorkOrderSummaryBarBean();
        barDatas.setToDealAmount(ResourceUtil.getStringById(R.string.summary_to_deal));
        barDatas.setDealingAmount(ResourceUtil.getStringById(R.string.summary_dealing));
        barDatas.setFinishedAmount(ResourceUtil.getStringById(R.string.summary_finished));
        barDatas.setPostponeAmount(ResourceUtil.getStringById(R.string.summary_postpone));
        barDatas.setOvertimeAmount(ResourceUtil.getStringById(R.string.summary_over_schedule));
        firstData.setBarDatas(barDatas);
        lists.add(0, firstData);
    }
}
