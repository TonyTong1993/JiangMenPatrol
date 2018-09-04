package com.ecity.cswatersupply.project.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

import org.json.JSONObject;

public class ProjectWaterMeterSummaryView {
    public View inflate(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.lv_item_project_summary, null);
    }

    public void show(View view, JSONObject summary) {
        showAttribute(view, summary, "datement", R.id.tv_section);
        showAttribute(view, summary, "total", R.id.tv_total);
        showAttribute(view, summary, "doing", R.id.tv_not_started);
        showAttribute(view, summary, "finish", R.id.tv_ongoing);
        showAttribute(view, summary, "hang", R.id.tv_finished);
        showAttribute(view, summary, "disused", R.id.tv_delay);
        showAttribute(view, summary, "overdue", R.id.tv_delay1);
    }

    private void showAttribute(View view, JSONObject summary, String key, int valueViewId) {
        TextView tv_wwg = (TextView) view.findViewById(R.id.tv_title_not_started);
        TextView tv_ywg = (TextView) view.findViewById(R.id.tv_title_ongoing);
        TextView tv_gq = (TextView) view.findViewById(R.id.tv_title_finished);
        TextView tv_fj = (TextView) view.findViewById(R.id.tv_title_delay);
        TextView tv_cq = (TextView) view.findViewById(R.id.tv_title_delay1);
        tv_wwg.setText("未完工");
        tv_ywg.setText("已完工");
        tv_gq.setText("挂起");
        tv_fj.setText("废件");
        tv_cq.setText("超期");


        TextView tvValue = (TextView) view.findViewById(valueViewId);
        if (key.equals("datement")) {
            String str = summary.optString(key);
            String newStr = str.substring(str.length() - 2, str.length());
            tvValue.setText(newStr);
        }else{
            tvValue.setText(summary.optString(key));
        }
    }
}
