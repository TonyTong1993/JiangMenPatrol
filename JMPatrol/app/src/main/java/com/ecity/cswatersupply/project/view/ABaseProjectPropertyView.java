package com.ecity.cswatersupply.project.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.project.model.Project;

public abstract class ABaseProjectPropertyView {

    public abstract void showProjectInfo(View view, Project project);

    protected abstract int getLayoutId();

    public View inflate(Context context) {
        return LayoutInflater.from(context).inflate(getLayoutId(), null);
    }

    protected void showAttribute(View view, Project project, String key, int titleViewId, int valueViewId) {
        TextView tvTitle = (TextView) view.findViewById(titleViewId);
        TextView tvValue = (TextView) view.findViewById(valueViewId);
//        tvTitle.setText(project.getAttributeName(key));
        tvValue.setText(project.getAttributeValue(key));
    }

    protected void appendAttribute(StringBuilder builder, Project project, String key, boolean withNewLine) {
        String name = project.getAttributeName(key);
        String value = project.getAttributeValue(key);
        builder.append(name).append(":").append(value);
        if (withNewLine) {
            builder.append("\n");
        }
    }

    protected void appendAttribute1(StringBuilder builder, Project project, String key, boolean withNewLine) {
        String name = project.getAttributeName(key);
        String value = project.getAttributeValue(key);
        builder.append(name).append(":").append(value);
        if (withNewLine) {
            builder.append("\n");
        }
    }
}
