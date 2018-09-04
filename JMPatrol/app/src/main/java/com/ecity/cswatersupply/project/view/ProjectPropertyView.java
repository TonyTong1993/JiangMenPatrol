package com.ecity.cswatersupply.project.view;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.model.Project;

public class ProjectPropertyView extends ABaseProjectPropertyView {

    @Override
    public void showProjectInfo(View view, Project project) {
//        showAttribute(view, project, Project.ATTR_NAME, R.id.tv_title_project_name, R.id.tv_project_name);
//        showAttribute(view, project, Project.ATTR_CODE, R.id.tv_title_project_code, R.id.tv_project_code);
//        showAttribute(view, project, Project.ATTR_TYPE, R.id.tv_title_project_type, R.id.tv_project_type);
//        showAttribute(view, project, Project.ATTR_START_DATE, R.id.tv_title_project_start_time, R.id.tv_project_start_time);
//        showAttribute(view, project, Project.ATTR_END_DATE, R.id.tv_title_project_end_time, R.id.tv_project_end_time);

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
        String typeid = project.getAttributeValue(Project.ATTR_TYPE);
        String typeStr = "";
        switch (typeid) {
            case "1":
                typeStr = "自建管道";
                break;
            case "2":
                typeStr = "重点工程";
                break;
            case "3":
                typeStr = "其他基建工程";
                break;
            case "4":
                typeStr = "管理外单位工程";
                break;
            default:
                typeStr = "";
        }
        builder2.append("项目定义").append(":").append(project.getAttributeValue(Project.ATTR_CODE)).append("\n").append("项目类型")
                .append(":").append(typeStr).append("\n").append("计划开工时间").append(":")
                .append(project.getAttributeValue(Project.ATTR_START_DATE)).append("\n").append("计划完工时间").append(":").append(project.getAttributeValue(Project.ATTR_END_DATE));

        TextView tvSecond = (TextView) view.findViewById(tvTitleProjectCode);
        tvSecond.setText(builder2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lv_common_check;
    }
}