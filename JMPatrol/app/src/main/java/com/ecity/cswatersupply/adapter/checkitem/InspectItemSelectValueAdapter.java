package com.ecity.cswatersupply.adapter.checkitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.z3app.android.util.StringUtil;

/**
 * 将可选值解析成对象
 * @author zhengzhuanzi
 *
 */
public class InspectItemSelectValueAdapter {
    public static List<InspectItemSelectValue> adapt(JSONObject jsonObj) {
        if (null == jsonObj || jsonObj.length() == 0) {
            return null;
        }

        ArrayList<InspectItemSelectValue> resultArr = new ArrayList<InspectItemSelectValue>();

        @SuppressWarnings("unchecked")
        Iterator<String> iterator = jsonObj.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            Object value = null;
            try {
                value = jsonObj.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (null != value) {
                InspectItemSelectValue inspectItemSelectValue = new InspectItemSelectValue(key, value.toString());
                resultArr.add(inspectItemSelectValue);
            }
        }

        return resultArr;
    }

    public static List<InspectItemSelectValue> adapt(JSONArray jsonArray) {
        List<InspectItemSelectValue> resultArr = new ArrayList<InspectItemSelectValue>();

        if (null == jsonArray || jsonArray.length() == 0) {
            return resultArr;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArray.getJSONObject(i);
                @SuppressWarnings("unchecked")
                Iterator<String> iterator = jsonObj.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    if ("name".equalsIgnoreCase(key) || "alias".equalsIgnoreCase(key) || "logmark".equalsIgnoreCase(key)) {
                        if ("alias".equalsIgnoreCase(key)) {
                            continue;
                        } else if ("logmark".equalsIgnoreCase(key)) {
                            continue;
                        }
                        String gid = jsonObj.optString("name");
                        String name = jsonObj.optString("alias");
                        if (null == gid) {
                            gid = "";
                        }
                        if (null == name) {
                            name = "";
                        }
                        InspectItemSelectValue inspectItemSelectValue = new InspectItemSelectValue(gid, name);
                        resultArr.add(inspectItemSelectValue);
                    } else {
                        Object value = null;
                        try {
                            value = jsonObj.get(key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (null != value) {
                            InspectItemSelectValue inspectItemSelectValue = new InspectItemSelectValue(key, value.toString());
                            resultArr.add(inspectItemSelectValue);
                        }
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                LogUtil.e("InspectItemSelectValueAdapter", e.getMessage());
            }

        }

        return resultArr;
    }

    public static List<InspectItemSelectValue> adapt1(JSONArray jsonArray) {
        List<InspectItemSelectValue> resultArr = new ArrayList<InspectItemSelectValue>();

        if (null == jsonArray || jsonArray.length() == 0) {
            return resultArr;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArray.getJSONObject(i);
                InspectItemSelectValue inspectItemSelectValue = new InspectItemSelectValue();
                inspectItemSelectValue.setGid(jsonObj.optString("name"));
                inspectItemSelectValue.setName(jsonObj.optString("alias"));

                JSONArray array = jsonObj.optJSONArray("selectValues");
                if (null != array) {
                    inspectItemSelectValue.setChildSelectValue(adapt1(array));
                }
                resultArr.add(inspectItemSelectValue);
            } catch (Exception e) {
                LogUtil.e("InspectItemSelectValueAdapter", e.getMessage());
            }
        }

        return resultArr;
    }

    public static List<InspectItemSelectValue> adapt(String str) {
        if (StringUtil.isBlank(str)) {
            return null;
        }

        ArrayList<InspectItemSelectValue> resultArr = new ArrayList<InspectItemSelectValue>();

        str = str.substring(1, str.length() - 1).replace(" ", "");
        String[] strArray = str.split(",");
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = strArray[i].substring(1, strArray[i].length() - 1);
            String[] strTempArray = strArray[i].split("=");
            if (strTempArray.length < 2) {
                return null;
            }
            InspectItemSelectValue inspectItemSelectValue = new InspectItemSelectValue(strTempArray[0], strTempArray[1]);
            resultArr.add(inspectItemSelectValue);
        }

        return resultArr;
    }
}
