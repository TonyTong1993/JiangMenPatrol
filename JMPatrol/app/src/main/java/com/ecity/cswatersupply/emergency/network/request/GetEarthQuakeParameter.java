package com.ecity.cswatersupply.emergency.network.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetEarthQuakeParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;

    private List<InspectItem> items;
    private String[] mL = new String[2];
    private String[] time = new String[2];
    private String keywords = "";

    public GetEarthQuakeParameter() {
        
    }

    public GetEarthQuakeParameter(List<InspectItem> items) {
        this.items = items;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if(null == items) {
            map.put("StartTime", "");
            map.put("EndTime", "");
            map.put("StartML", "");
            map.put("EndML", "");
            map.put("StartLongitude", "");
            map.put("EndLongitude","");
            map.put("Keyword","");
            map.put("type", "");
        } else {
            getSearchParams();
            if(null != time) {
                map.put("StartTime", time[0] + "-01-01");
                map.put("EndTime", time[1] + "-01-01");
            } else {
                map.put("StartTime", "");
                map.put("EndTime", "");
            }

            if(null != mL) {
                map.put("StartML", mL[0]);
                map.put("EndML", mL[1]);
            } else {
                map.put("StartML", "");
                map.put("EndML", "");
            }
            map.put("StartLongitude", "");
            map.put("EndLongitude", "");
            map.put("StartLatitude", "");
            map.put("EndLatitude", "");
            if(null == keywords) {
                map.put("Keyword", "");
            } else {
                map.put("Keyword", keywords);
            }
            map.put("type", "");
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
            } else if("address".equals(inspectItem.getName())) {
                keywords = inspectItem.getValue();
            }
        }
    }

}
