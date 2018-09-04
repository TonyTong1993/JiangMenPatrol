package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;

public class ProjectListAdapter extends ArrayListAdapter<Project> {
    private ABaseProjectPropertyView projectPropertyView;

    public ProjectListAdapter(Context context, ABaseProjectPropertyView projectPropertyView) {
        super(context);
        this.projectPropertyView = projectPropertyView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Project project = getList().get(position);

        if (null == convertView) {
            convertView = projectPropertyView.inflate(getContext());
        }

        projectPropertyView.showProjectInfo(convertView, project);

        return convertView;
    }
}
