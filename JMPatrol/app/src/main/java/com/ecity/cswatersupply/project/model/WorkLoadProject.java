package com.ecity.cswatersupply.project.model;

import org.json.JSONObject;

public class WorkLoadProject extends Project {
    public static final String ATTR_WL_BIAN_ZHI_DATE = "bzdate";
    public static final String ATTR_WL_RATE = "rate";
    public static final String ATTR_WL_COUNT = "count";
    public static final String ATTR_WL_STATE = "checkstate";
    public static final String ATTR_WL_CRETA_TIME = "createtime";

    private static final long serialVersionUID = 4450755005741074380L;

    public WorkLoadProject(JSONObject dataSource) {
        super(dataSource);
    }
}