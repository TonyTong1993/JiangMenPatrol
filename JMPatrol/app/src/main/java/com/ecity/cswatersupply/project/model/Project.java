package com.ecity.cswatersupply.project.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Project implements Serializable {
    public static final String ATTR_ID = "gid";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_CODE = "code";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_ADDRESS = "address";
    public static final String ATTR_MEMO = "memo";
    public static final String ATTR_CLASSIFY = "classify";
    public static final String ATTR_START_DATE = "startdate";
    public static final String ATTR_END_DATE = "enddate";
    public static final String ATTR_ESTIMATED_COST = "procost";
    public static final String ATTR_GEOM = "geom";
    public static final String ATTR_AREA = "area";
    public static final String ATTR_PROCESS = "process";
    public static final String ATTR_CREATE_TIME = "createtime";
    public static final String ATTR_CREATERID = "createrid";
    public static final String ATTR_GETTERID = "getterid";
    public static final String ATTR_STATE = "state";

    public static final String KANCHA_GID = "kcid";//勘察id

    public static final String KAIGONG_GID = "kgid";//开工id

    public static final String PAY_GID = "payid";//资金支付id

    public static final String JG_GID = "jgid";//竣工id

    public static final String SY_GID = "syid";//试压试验id
    public static final String SY_CODE = "sycode";//试压试验编码
    public static final String SY_DATE = "sydate";//试压试验日期
    public static final String SY_COUNT = "sycount";//试压次数
    public static final String KG_TIME = "kgtime";//开工日期

    public static final String JS_GID = "jsid";//接水id
    public static final String JS_CODE = "jscode";//接水编码
    public static final String JS_PSDATE = "psdate";//计划开始时间
    public static final String JS_PEDATE = "pedate";//计划结束时间
    public static final String JS_COUNT = "jscount";//接水次数

    private static final long serialVersionUID = -6978097419128796314L;
    private JSONObject dataSource;

    public Project(JSONObject dataSource) {
        this.dataSource = dataSource;
    }

    public String getAttributeName(String key) {
        return ProjectFieldMeta.getFieldName(key);
    }

    public String getAttributeValue(String key) {
        String value = dataSource.optString(key);
        String selectValusAlias = ProjectFieldMeta.getSelectValueAlias(key, value);
        return "".equals(selectValusAlias) ? value : selectValusAlias;
    }

    public String getAttrState(String key) {
        String value = dataSource.optString(key);
        return value;
    }
}