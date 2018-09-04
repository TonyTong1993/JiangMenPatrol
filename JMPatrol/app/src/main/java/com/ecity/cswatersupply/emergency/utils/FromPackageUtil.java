package com.ecity.cswatersupply.emergency.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.z3app.android.util.StringUtil;

public class FromPackageUtil{
    
    /***
     * 将JSON对象解析成InspectItem 对象列表
     * @param objJson
     * @param item
     * @param list
     * @return
     */
    public static List<InspectItem> analysisJson(Object objJson,InspectItem item,List<InspectItem> list) {
        if (objJson instanceof JSONArray) {
            JSONArray objArray = (JSONArray) objJson;
            for (int i = 0; i < objArray.length(); i++) {
                try {
                    Object object = objArray.get(i);
                    analysisJson(object,item,list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            InspectItem tempItem = adaptItem(jsonObject);
            List<InspectItem> items = null;
            JSONArray childrenItems = jsonObject.optJSONArray("childs");
            if (null == childrenItems) {
                childrenItems = jsonObject.optJSONArray("items");
            }
            
            if (null != childrenItems && childrenItems.length()>0) {
                items = new ArrayList<InspectItem>();
                analysisJson(childrenItems,tempItem,items);
            }
            
            list.add(tempItem);
            item.setChilds(list);
        }
        
        return list;
    }
    /***
     * 将检查项组织成JSON对象
     * @param item
     * @return
     */
    public static JSONObject buildJson(InspectItem item) {
        JSONObject jsonObj = buildJsonWithOutChild(item);
        JSONArray objArray = new JSONArray();
        if ((item.getChilds() != null) 
                && (item.getChilds().size() != 0)) {
            List<InspectItem> childs = item.getChilds();
            int size = childs.size();
            for (int i = 0; i < size; i++) {
                JSONObject tmpObj = buildJson(childs.get(i));
                objArray.put(tmpObj);
            }
        }
        
        try {
            jsonObj.put("items", objArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return jsonObj;
    }

    private static InspectItem adaptItem(JSONObject serverItem) {
        InspectItem item = new InspectItem();
        String defaultValue = serverItem.optString("defaultValue");
        item.setAlias(serverItem.optString("alias"));
        item.setDefaultValue(defaultValue);
        item.setName(serverItem.optString("name"));
        item.setVisible(serverItem.optString("visible").equalsIgnoreCase("1"));
        item.setRequired(serverItem.optString("required").equalsIgnoreCase("1"));
        item.setLongText(serverItem.optString("len").equalsIgnoreCase("1"));
        item.setIncrease(serverItem.optString("increase").equalsIgnoreCase("1"));
        item.setEdit(serverItem.optString("edit").equalsIgnoreCase("1"));
        item.setType(getItemType(serverItem.optString("type")));
        if ((!StringUtil.isBlank(defaultValue))) {
            item.setValue(defaultValue);
        } else {
            item.setValue(serverItem.optString("value"));
        }
        item.setGeoType(serverItem.optInt("gtype"));
        item.setSelectValues(serverItem.optString("selectValues"));
        return item;
    }
    
    private static  JSONObject buildJsonWithOutChild(InspectItem serverItem) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("defaultValue", serverItem.getDefaultValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("type", serverItem.getType().getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("alias", serverItem.getAlias());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObj.put("name", serverItem.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       
        try {
            jsonObj.put("visible", serverItem.isVisible()?1:0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("required", serverItem.isRequired()?1:0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("len", serverItem.isLongText()?1:0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("increase", serverItem.isIncrease()?1:0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("edit", serverItem.isEdit()?1:0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
     
        try {
            jsonObj.put("value", serverItem.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            jsonObj.put("gtype", serverItem.getGeoType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       
        try {
            JSONArray jsonArr = new JSONArray();
            try {
                JSONArray tmp = new JSONArray(serverItem.getSelectValues());
                jsonArr = tmp;
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonObj.put("selectValues", jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
    
    private static EInspectItemType getItemType(String type) {
        for (EInspectItemType e : EInspectItemType.values()) {
            if (e.getValue().equalsIgnoreCase(type)) {
                return e;
            }
        }
        
        return EInspectItemType.TEXT;// 类型缺省，返回TEXT
    }
}
