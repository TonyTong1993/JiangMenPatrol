package com.ecity.cswatersupply.project.view;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.Project;

public class ProspectiveMoreListView extends ABaseProjectPropertyView {

    @Override
    public void showProjectInfo(View view, Project project) {

        showFirstAttribute(view, project, R.id.tv_first);
        showSecondAttribute(view, project, R.id.tv_second);
    }

    private void showFirstAttribute(View view, Project project, int tvTitleProjectCode) {
        TextView tvFirst = (TextView) view.findViewById(tvTitleProjectCode);

        StringBuilder builder1 = new StringBuilder();
        builder1.append("审核人").append(":").append(project.getAttributeValue("username"));
        tvFirst.setText(builder1);
    }

    private void showSecondAttribute(View view, Project project, int tvTitleProjectCode) {

        StringBuilder builder2 = new StringBuilder();
        builder2.append("审核时间").append(":").append(project.getAttributeValue("createtime")).append("\n")
                .append("审核结果").append(":").append(project.getAttributeValue("other")).append("\n")
                .append("审核状态").append(":").append(project.getAttributeValue("type")).append("\n")
                .append("审核意见").append(":").append(project.getAttributeValue("remark"));

        TextView tvSecond = (TextView) view.findViewById(tvTitleProjectCode);
        tvSecond.setText(builder2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lv_common_check;
    }
}