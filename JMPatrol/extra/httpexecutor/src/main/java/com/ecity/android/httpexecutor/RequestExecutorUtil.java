package com.ecity.android.httpexecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * @author jonathanma
 * @date 2017/4/13
 */

final class RequestExecutorUtil {

    public static JSONObject map2Json(Map<String, String> map) throws JSONException {
        JSONObject json = new JSONObject();
        if (map == null) {
            return json;
        }

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            json.putOpt(entry.getKey(), entry.getValue());
        }

        return json;
    }
}
