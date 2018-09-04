package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.ColorTextView;
import com.ecity.cswatersupply.workorder.model.UnFinishWorkOrderInfo;

import java.util.List;

public class WorkOrderDispatchTableAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<UnFinishWorkOrderInfo> lists;
    private TextView patrolName, todayNumber, waitNumber;
    private ColorTextView waitAddresses;

    public WorkOrderDispatchTableAdapter(Context context, List<UnFinishWorkOrderInfo> lists) {
        super();
        this.lists = lists;
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
        UnFinishWorkOrderInfo infos = lists.get(index);
        if (view == null) {
            view = inflater.inflate(R.layout.item_table_unfinish_workorder_list, null);
        }
        initViews(view);
        patrolName.setText(infos.getPatrolName());
        todayNumber.setText(infos.getTodayWorkOrderNumber());
        waitNumber.setText(infos.getWaitProcessWorkOrderNumber());
        String str = infos.toString();
        waitAddresses.setSpecifiedTextsColor(str, "!", Color.RED);

        return view;
    }

    private void initViews(View view) {
        view.setBackgroundColor(Color.WHITE);
        patrolName = (TextView) view.findViewById(R.id.patrol_name);
        todayNumber = (TextView) view.findViewById(R.id.today_number);
        waitNumber = (TextView) view.findViewById(R.id.wait_number);
        waitAddresses = (ColorTextView) view.findViewById(R.id.wait_addresses);

        waitAddresses.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        patrolName.setTextSize(12);
        todayNumber.setTextSize(12);
        waitNumber.setTextSize(12);
        waitAddresses.setTextSize(12);
    }
}
