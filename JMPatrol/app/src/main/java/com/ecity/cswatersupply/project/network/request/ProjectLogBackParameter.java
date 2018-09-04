package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class ProjectLogBackParameter implements IRequestParameter {
    private String projectId;
    private String logBackType;

    public ProjectLogBackParameter(String projectId,String logBackType) {
        this.projectId = projectId;
        this.logBackType = logBackType;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (logBackType.equals("SAFE_FLAG")){
            map.put("eveid",projectId);
        }else{
            map.put("proid", projectId);
        }
        return map;
    }
}
