package com.ecity.cswatersupply.adapter.planningtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo.Attr;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo.FieldAliases;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.utils.PlanTaskUtils;
import com.esri.core.map.Graphic;

public class PointPartAttrAdapter {
    private static PointPartAttrAdapter instance;

    private PointPartAttrAdapter() {
    }

    static {
        instance = new PointPartAttrAdapter();
    }

    public static PointPartAttrAdapter getInstance() {
        return instance;
    }

    public PointPartAttrInfo getPointPartAttrAdapter(String jsonString) {
        try {
            PointPartAttrInfo info = new PointPartAttrInfo();
            JSONObject attrsJson = new JSONObject(jsonString);

            JSONObject fieldAliasesjson = attrsJson.getJSONObject("fieldAliases");
            List<FieldAliases> fAliasesList = new ArrayList<FieldAliases>();
            Iterator<String> iterator = fieldAliasesjson.keys();
            while (iterator.hasNext()) {
                String fid = iterator.next().toString();
                String fname = fieldAliasesjson.getString(fid);
//                if (!PlanTaskUtils.isAbcStr(fid)) {
                    FieldAliases faliases = info.new FieldAliases(fid, fname);
                    fAliasesList.add(faliases);
//                }
            }
            info.setfAliasesList(fAliasesList);

            JSONArray features = attrsJson.getJSONArray("features");
            if (!(features.length() > 0)) {
                return info;
            }
            List<List<Attr>> attrLists = new ArrayList<List<Attr>>();
            for (int i = 0; i < features.length(); i++) {
                List<Attr> attrList = new ArrayList<PointPartAttrInfo.Attr>();
                JSONObject feature = new JSONObject(features.get(i).toString());
                if (feature.has("attributes")) {
                    JSONObject attributes = feature.getJSONObject("attributes");
                    Iterator<String> attrIterator = attributes.keys();
                    while (attrIterator.hasNext()) {
                        String attrId = attrIterator.next().toString();
                        String attrName = attributes.getString(attrId);
//                        if (!PlanTaskUtils.isAbcStr(attrId)) {
                            for (int j = 0; j < fAliasesList.size(); j++) {
                                if (fAliasesList.get(j).fid.equalsIgnoreCase(attrId)) {
                                    Attr attr = info.new Attr();
                                    attr.setAttrKey(attrId);
                                    attr.setAttrValue(attrName);
                                    attrList.add(attr);
                                }
                            }
//                        }
                    }
                }
                if (feature.has("geometry")) {
                    JSONObject geometry = feature.getJSONObject("geometry");
                    if (geometry.has("x")){
                        info.setX(geometry.optString("x"));
                    }
                    if (geometry.has("y")){
                        info.setY(geometry.optString("y"));
                    }
                }
                attrLists.add(attrList);
            }
            info.setAttrList(attrLists);
            return info;
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        return null;
    }

    public PointPartAttrInfo getGraphicAttrAdapter(Graphic graphic) {
        PointPartAttrInfo info = new PointPartAttrInfo();
        List<List<Attr>> attrLists = new ArrayList<List<Attr>>();
        List<Attr> attrList = new ArrayList<PointPartAttrInfo.Attr>();
        String[] attrsName = graphic.getAttributeNames();
        HashMap<String, Object> attrsMap = (HashMap<String, Object>) graphic.getAttributes();
        String hiddenAttrPrefix = HostApplication.getApplication().getString(R.string.map_device_hidden_attr_prefix);
        for (int i = 0; i < attrsName.length; i++) {
            String name = attrsName[i];
            if (name.startsWith(hiddenAttrPrefix)) { // 在拉框查询和关联设备功能中，我们在graphic里放了其他属性，但是不需要显示在属性详情界面，这些属性的key都有"hidden_"前缀。
                continue;
            }
            try {
                if(!FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                    if (PlanTaskUtils.isAbcStr(name)) {
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (attrsMap.containsKey(name)) {
                String value = attrsMap.get(name).toString();
                if (("").equalsIgnoreCase(value)) {
                    value = HostApplication.getApplication().getAppManager().currentActivity().getString(R.string.map_query_text);
                }
                Attr attr = info.new Attr();
                attr.setAttrKey(name);
                attr.setAttrValue(value);
                attrList.add(attr);
            }
        }
        attrLists.add(attrList);
        info.setAttrList(attrLists);
        return info;
    }
}
