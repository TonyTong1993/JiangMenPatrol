package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.project.network.response.SafeEventListModel;

import java.util.HashMap;
import java.util.Map;

public class GetSafeDetailTabParameter implements IRequestParameter {
    SafeEventListModel model;

    public GetSafeDetailTabParameter(SafeEventListModel model) {
        this.model = model;
    }

    @Override
    public Map<String, String> toMap() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("eveid", model.getGid());
        map.put("userid",currentUser.getId());
        return map;
    }
}
