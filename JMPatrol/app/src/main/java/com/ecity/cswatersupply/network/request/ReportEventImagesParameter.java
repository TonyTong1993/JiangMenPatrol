package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class ReportEventImagesParameter implements IRequestParameter {
    private String requestKey;
    private String ids;

    public ReportEventImagesParameter(String requestKey, String ids) {
        this.requestKey = requestKey;
        this.ids = ids;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("field", requestKey);
        map.put("ids", ids);

        return map;
    }
}
