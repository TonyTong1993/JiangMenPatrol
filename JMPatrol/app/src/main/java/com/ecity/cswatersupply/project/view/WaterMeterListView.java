package com.ecity.cswatersupply.project.view;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.Project;

public class WaterMeterListView extends ABaseProjectPropertyView {

    @Override
    public void showProjectInfo(View view, Project project) {

        showFirstAttribute(view, project, R.id.tv_first);
        showSecondAttribute(view, project, R.id.tv_second);
    }

    private void showFirstAttribute(View view, Project project, int tvTitleProjectCode) {
        TextView tvFirst = (TextView) view.findViewById(tvTitleProjectCode);

        StringBuilder builder1 = new StringBuilder();
        builder1.append("项目名称").append(":").append(project.getAttributeValue("name"));
        tvFirst.setText(builder1);
    }

    private void showSecondAttribute(View view, Project project, int tvTitleProjectCode) {

        StringBuilder builder2 = new StringBuilder();
        builder2.append("项目编号").append(":").append(project.getAttributeValue(Project.ATTR_CODE)).append("\n");
//                .append(project.getAttributeName(Project.JS_PEDATE)).append(":").append(project.getAttributeValue(Project.JS_PEDATE)).append("\n")
//                .append(project.getAttributeName(Project.JS_PSDATE)).append(":").append(project.getAttributeValue(Project.JS_PSDATE)).append("\n")
//                .append(project.getAttributeName(Project.JS_COUNT)).append(":").append(project.getAttributeValue(Project.JS_COUNT)).append("\n")
//                .append(project.getAttributeName("state")).append(":").append(project.getAttributeValue("state"));

        TextView tvSecond = (TextView) view.findViewById(tvTitleProjectCode);
        tvSecond.setText(builder2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lv_common_check;
    }
}