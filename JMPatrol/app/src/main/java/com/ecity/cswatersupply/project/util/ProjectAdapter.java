package com.ecity.cswatersupply.project.util;

import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.model.ProjectFieldMeta;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter {
    public static List<Project> parseProjects(JSONObject response) {
        JSONArray fields = response.optJSONArray("features");
        if ((fields == null) || (fields.length() == 0)) {
            return null;
        }

        List<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < fields.length(); i++) {
            JSONObject json = fields.optJSONObject(i);
            if (json == null) {
                continue;
            }

            projects.add(new Project(json));
        }

        return projects;
    }

    public static void parseProjectFieldMeta(JSONObject response) {
        JSONArray fields = response.optJSONArray("fields");
        if ((fields == null) || (fields.length() == 0)) {
            return;
        }

        for (int i = 0; i < fields.length(); i++) {
            JSONObject json = fields.optJSONObject(i);
            if (json == null) {
                continue;
            }

            String key = json.optString("name");
            ProjectFieldMeta.put(key, json);
        }
    }
}
