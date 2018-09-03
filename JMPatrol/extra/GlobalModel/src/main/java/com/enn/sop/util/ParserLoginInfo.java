package com.enn.sop.util;

import com.enn.sop.global.StationInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongzhan
 * @date 2017/12/19
 */
public class ParserLoginInfo {
    public static List<StationInfoModel> parserStationInfo(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            return null;
        }
        JSONArray jsonArray = null;
        List<StationInfoModel> modelList = new ArrayList<>();
        if (jsonObject.has("datas")) {
            jsonArray = jsonObject.optJSONArray("datas");
        }
        if (jsonArray == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            if(object.has("station_type")) {
                if("B".equals(object.optString("station_type"))||"C".equals(object.optString("station_type"))) {
                    if("SITE".equals(object.optString("loc_type"))) {
                        StationInfoModel model = new StationInfoModel();
                        model.setStationId(object.optString("gid"));
                        model.setStationName(object.optString("loc_name"));
                        model.setStationEcode(object.optString("ecode"));
                        modelList.add(model);
                    }
                }
            }
        }
        return modelList;

    }
}
