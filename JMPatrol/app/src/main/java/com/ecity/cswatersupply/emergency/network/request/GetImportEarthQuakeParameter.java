package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetImportEarthQuakeParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;

    private List<InspectItem> items;
    private String[] mL = new String[2];
    private String[] time = new String[2];
    private String keywords = "";

    public GetImportEarthQuakeParameter() {

    }

    public GetImportEarthQuakeParameter(List<InspectItem> items) {
        this.items = items;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if(null == items) {
            map.put("minLevel", "");
            map.put("maxLevel", "");
            map.put("sTime", "");
            map.put("eTime", "");
            map.put("place", "");
            map.put("status","");
            map.put("type", "1");
        } else {
            getSearchParams();
            if(null != time) {
                map.put("sTime", time[0] + "-01-01");
                map.put("eTime", time[1] + "-01-01");
            } else {
                map.put("sTime", "");
                map.put("eTime", "");
            }

            if(null != mL) {
                map.put("minLevel", mL[0]);
                map.put("maxLevel", mL[1]);
            } else {
                map.put("minLevel", "");
                map.put("maxLevel", "");
            }
            map.put("place", "");
            map.put("status","");
            map.put("type", "1");
        }
        return map;
    }

    private void getSearchParams() {
        for(InspectItem inspectItem : items) {
            if("nianfen".equals(inspectItem.getName())) {
                String value = inspectItem.getValue();
                if(value.contains("-")) {
                    time = inspectItem.getValue().split("-");
                } else {
                    time = null;
                }
            } else if("zhenji".equals(inspectItem.getName())) {
                String value = inspectItem.getValue();
                if(value.contains("-")) {
                    mL = inspectItem.getValue().split("-");
                } else {
                    mL = null;
                }
            }
        }
    }

}
