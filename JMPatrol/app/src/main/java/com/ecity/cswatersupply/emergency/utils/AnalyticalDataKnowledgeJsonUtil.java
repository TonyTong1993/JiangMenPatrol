package com.ecity.cswatersupply.emergency.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;

public class AnalyticalDataKnowledgeJsonUtil {

    /**
     * 解析知识库相关数据工具类
     * EmergencyPlanModel
     */
    public static List<EmergencyPlanModel> emergencyResponseData(JSONObject jsonObj) {
        List<EmergencyPlanModel> eqInfoModels = new ArrayList<EmergencyPlanModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("resultList");
        if (jsonArray == null) {
            return eqInfoModels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject == null) {
                continue;
            }
            EmergencyPlanModel infoModel = new EmergencyPlanModel();
            infoModel.setId(jsonObject.optString("id"));
            infoModel.setType(jsonObject.optString("type"));
            infoModel.setTypeName(jsonObject.optString("typeName"));
            infoModel.setDoc(jsonObject.optString("doc"));

            String docUrl = jsonObject.optString("docUrl");
            String url = "";
            for (int j = 0; j < docUrl.length(); j++) {
                if (isChinese(docUrl.charAt(j)))
                    try {
                        url = url + URLEncoder.encode(docUrl.substring(j, j + 1), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                else {
                    url = url + docUrl.substring(j, j + 1);
                }
            }

            url = url.replaceAll(" ", "%20");


            infoModel.setDocUrl(url);
            infoModel.setCreatetime(jsonObject.optString("createtime"));
            eqInfoModels.add(infoModel);
        }
        return eqInfoModels;
    }

    public static boolean isChinese(char c) {
        boolean ischinese = false;
        if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
            ischinese = true;
        }
        return ischinese;
    }
}
