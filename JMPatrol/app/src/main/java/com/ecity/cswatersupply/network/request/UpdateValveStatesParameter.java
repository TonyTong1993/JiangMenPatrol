package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.network.RequestParameter;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateValveStatesParameter implements RequestParameter.IRequestParameter {

    private List<InspectItem> items;

    public UpdateValveStatesParameter(List<InspectItem> items) {
        this.items = items;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();

        InspectItem selectVavle = getInspectItem(EInspectItemType.SELECTVALVE);
        JSONObject attributes = new JSONObject();
        if(null == selectVavle) {
            return map;
        }
        String selectVavleValue = selectVavle.getValue();
        String gid = null;
        String layerId = null;
        String valveNumber = null;

        try {
            JSONObject json = new JSONObject(selectVavleValue);
            gid = json.optString("gid");
            layerId = json.optString("layerId");
            valveNumber = json.optString(ResourceUtil.getStringById(R.string.valve_number));
            attributes.put(ResourceUtil.getStringById(R.string.valve_number), valveNumber);
        } catch (JSONException e) {
        }

        InspectItem item = getInspectItem(EInspectItemType.DROPDOWNLIST);
        List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(item);
        try {
            for(InspectItemSelectValue value : selectValues) {
                if (value.gid.equals(item.getValue())) {
                    attributes.put(ResourceUtil.getStringById(R.string.valve_switch_state), value.name);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("f", "html");
        map.put("attributes",attributes.toString());
        map.put("layerId",layerId);
        map.put("objectId", gid);
        map.put("gdbVersion", "SDE.DEFAULT");
        map.put("rollbackOnFailure", "false");
        return map;
    }

    private InspectItem getInspectItem(EInspectItemType type) {
        for(InspectItem item : items) {
            if(item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
