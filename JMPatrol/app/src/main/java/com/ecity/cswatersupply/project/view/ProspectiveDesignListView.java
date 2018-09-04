package com.ecity.cswatersupply.project.view;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.Project;

public class ProspectiveDesignListView extends ABaseProjectPropertyView {

    @Override
    public void showProjectInfo(View view, Project project) {

        showFirstAttribute(view, project, R.id.tv_first);
        showSecondAttribute(view, project, R.id.tv_second);
    }

    private void showFirstAttribute(View view, Project project, int tvTitleProjectCode) {
        StringBuilder builder = new StringBuilder();
        appendAttribute(builder, project, "name", false);
        TextView tvFirst = (TextView) view.findViewById(tvTitleProjectCode);
        tvFirst.setText(builder);
    }

    private void showSecondAttribute(View view, Project project, int tvTitleProjectCode) {
        StringBuilder builder = new StringBuilder();
        appendAttribute(builder, project, "uptime", true);
        appendAttribute(builder, project, "tuptime", true);
        appendAttribute(builder, project, "designer", true);
        appendAttribute(builder, project, "state", false);

        TextView tvSecond = (TextView) view.findViewById(tvTitleProjectCode);
        tvSecond.setText(builder);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lv_common_check;
    }
}