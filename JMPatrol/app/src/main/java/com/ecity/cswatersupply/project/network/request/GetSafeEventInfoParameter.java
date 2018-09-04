package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.project.network.response.SafeDetailStepModel;
import com.ecity.cswatersupply.project.network.response.SafeEventListModel;

import java.util.HashMap;
import java.util.Map;


public class GetSafeEventInfoParameter implements IRequestParameter {
    private User user;
    SafeEventListModel model;
    SafeDetailStepModel stepdata;

    public GetSafeEventInfoParameter(SafeEventListModel model, User user,SafeDetailStepModel stepdata) {
        this.model = model;
        this.user = user;
        this.stepdata = stepdata;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eveId", model.getGid());
        map.put("userid", user.getId());
        map.put("istodo",String.valueOf(stepdata.istodo()));
        return map;
    }

}
