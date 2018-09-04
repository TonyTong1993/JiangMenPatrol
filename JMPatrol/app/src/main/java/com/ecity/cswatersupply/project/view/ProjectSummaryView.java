package com.ecity.cswatersupply.project.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

import org.json.JSONObject;

public class ProjectSummaryView {
    public View inflate(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.lv_item_project_summary, null);
    }

    public void show(View view, JSONObject summary) {
        showAttribute(view, summary, "section", R.id.tv_section);
        showAttribute(view, summary, "total", R.id.tv_total);
        showAttribute(view, summary, "notStarted", R.id.tv_not_started);
        showAttribute(view, summary, "ongoing", R.id.tv_ongoing);
        showAttribute(view, summary, "finished", R.id.tv_finished  );
        showAttribute(view, summary, "delay", R.id.tv_delay);
    }

    private void showAttribute(View view, JSONObject summary, String key, int valueViewId) {
        TextView tvValue = (TextView) view.findViewById(valueViewId);
        tvValue.setText(summary.optString(key));
    }
}
