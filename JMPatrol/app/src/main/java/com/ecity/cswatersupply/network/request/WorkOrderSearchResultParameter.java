package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class WorkOrderSearchResultParameter implements IRequestParameter {
    private String searchParameter;
    public WorkOrderSearchResultParameter(String searchParameter) {
        this.searchParameter=searchParameter;
        
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("condition", searchParameter);
        map.put("userid", mUser.getGid());
        map.put("categorys", Constants.WORKORDER_ALL_CATEGORIES);
        return map;
    }
}
