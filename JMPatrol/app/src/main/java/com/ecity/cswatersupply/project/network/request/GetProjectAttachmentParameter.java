package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectAttachmentParameter implements IRequestParameter {
    private String guid;

    public GetProjectAttachmentParameter(String guid) {
        this.guid = guid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("guid", guid);

        return map;
    }

}
