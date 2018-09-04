package com.ecity.cswatersupply.project.view;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.model.WorkLoadProject;

public class WorkLoadProjectPropertyView extends ABaseProjectPropertyView {

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
        appendAttribute(builder, project, WorkLoadProject.ATTR_WL_BIAN_ZHI_DATE, true);
        appendAttribute(builder, project, WorkLoadProject.ATTR_WL_RATE, true);
        appendAttribute(builder, project, WorkLoadProject.ATTR_WL_COUNT, true);
        appendAttribute(builder, project, WorkLoadProject.ATTR_WL_STATE, false);

        TextView tvSecond = (TextView) view.findViewById(tvTitleProjectCode);
        tvSecond.setText(builder);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lv_common_check;
    }
}