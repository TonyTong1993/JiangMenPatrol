package com.ecity.cswatersupply.project.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class ProjectFieldMeta {
    private static final String KEY_NAME = "name";
    private static final String KEY_ALIAS = "alias";
    private static final String KEY_SELECT_VALUES = "values";

    private static Map<String, JSONObject> fieldsMata;

    static {
        fieldsMata = new HashMap<String, JSONObject>();
    }

    public static void put(String key, JSONObject field) {
        fieldsMata.put(key, field);
    }

    public static String getFieldName(String key) {


        return getValue(KEY_ALIAS, fieldsMata.get(key));
    }

    public static String getSelectValueAlias(String key, String value) {
        if ((value == null) || (fieldsMata.get(key) == null)) {
            return "";
        }

        JSONArray values = fieldsMata.get(key).optJSONArray(KEY_SELECT_VALUES);
        if ((values == null) || (values.length() == 0)) {
            return "";
        }

        for (int i = 0; i < values.length(); i++) {
            JSONObject tmpValue = values.optJSONObject(i);
            if (tmpValue == null) {
                continue;
            }

            if (value.equals(tmpValue.optString(KEY_NAME))) {
                return tmpValue.optString(KEY_ALIAS);
            }
        }

        return "";
    }

    private static String getValue(String key, JSONObject json) {
        return (json == null) ? "" : json.optString(key);
    }
}