package com.ecity.android.httpexecutor;

import com.enn.sop.global.GlobalFunctionInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author maoshoubei
 * @date 2017/10/23
 */

public abstract class AbstractRequestParameter {
    public Map<String, String> toMap() {
        return prepareParameters();
    }

    private Map<String, String> prepareParameters() {
        Map<String, String> map = new HashMap<>(16);
        setDefaultParameters(map);
        fillParameters(map);
        if (!map.containsKey("ecode")) {
            map.put("ecode", GlobalFunctionInfo.getCurrentUser().getEcode());
        }

        return map;
    }

    private void setDefaultParameters(Map<String, String> map) {
        map.put("plat", "mobile");
        map.put("sys", "android");
        map.put("f", "json");
        map.put("token", GlobalFunctionInfo.getToken());
    }

    /**
     * 请求参数构建的抽象方法
     *
     * @param map
     */
    protected abstract void fillParameters(Map<String, String> map);
}
