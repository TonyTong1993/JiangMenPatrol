package com.ecity.cswatersupply.network.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.DateUtil;

public class GisErrorReportParameter extends AReportInspectItemParameter {

    private List<InspectItem> items;

    public GisErrorReportParameter(List<InspectItem> items) {
        this.items = items;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("attributes", getAttributes());
            jsonObj.put("geometry", getGeometry());
            jsonArray.put(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("adds", jsonArray.toString());
        map.put("f", "json");
        return map;
    }

    private String getAttributes() {
        JSONObject attribute = new JSONObject();
        try {
            for (InspectItem item : items) {
                if (item.getType() == EInspectItemType.TEXT) {
                    attribute.put("remark", item.getValue());
                } else if (item.getType() == EInspectItemType.DROPDOWNLIST) {
                    attribute.put("errorType", item.getValue());
                }
            }
            attribute.put("reporttime", DateUtil.getCurrentTime());
            attribute.put("reportman", HostApplication.getApplication().getCurrentUser().getId());
            attribute.put("guid", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attribute.toString();
    }

    private String getGeometry() {
        String geometry = "";
        for (InspectItem item : items) {
            if (item.getType() == EInspectItemType.GEOMETRY_AREA) {
                geometry = item.getValue();
            }
        }
        return geometry;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        //no logic to do
    }

    @Override
    protected String getInspectItemsKey() {
        return "";
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return new ArrayList<InspectItem>();
    }
}
