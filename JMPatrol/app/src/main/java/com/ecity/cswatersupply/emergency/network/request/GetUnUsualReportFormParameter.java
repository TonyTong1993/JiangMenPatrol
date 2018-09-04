package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetUnUsualReportFormParameter implements IRequestParameter {
    private String formCode;
    
    public GetUnUsualReportFormParameter(String formCode) {
        super();
        this.formCode = formCode;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", formCode);
        return map;
    }
}
