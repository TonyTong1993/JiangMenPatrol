package com.ecity.cswatersupply.project.view;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.Project;

public class CompleteAcceptListView extends ABaseProjectPropertyView {

    @Override
    public void showProjectInfo(View view, Project project) {

        showFirstAttribute(view, project, R.id.tv_first);
        showSecondAttribute(view, project, R.id.tv_second);
    }

    private void showFirstAttribute(View view, Project project, int tvTitleProjectCode) {
        TextView tvFirst = (TextView) view.findViewById(tvTitleProjectCode);

        StringBuilder builder1 = new StringBuilder();
        builder1.append(project.getAttributeName("name")).append(":").append(project.getAttributeValue("name"));
        tvFirst.setText(builder1);
    }

    private void showSecondAttribute(View view, Project project, int tvTitleProjectCode) {

        StringBuilder builder2 = new StringBuilder();
        builder2.append(project.getAttributeName("checker")).append(":").append(project.getAttributeValue("checker")).append("\n").append(project.getAttributeName("startdate"))
                .append(":").append(project.getAttributeValue("startdate")).append("\n").append(project.getAttributeName("finishtime")).append(":")
                .append(project.getAttributeValue("finishtime")).append("\n").append(project.getAttributeName("state")).append(":").append(project.getAttributeValue("state"));

        TextView tvSecond = (TextView) view.findViewById(tvTitleProjectCode);
        tvSecond.setText(builder2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lv_common_check;
    }
}