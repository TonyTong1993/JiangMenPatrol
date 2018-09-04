package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class WorkOrderFilterParameter implements IRequestParameter {
    private String filters;
    private int pageNum;
    private String orderBy;
    private String condition;

    public WorkOrderFilterParameter(String filters, int pageNum, String orderBy, String condition) {
        this.filters = filters;
        this.pageNum = pageNum;
        this.orderBy = orderBy;
        this.condition = condition;
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        if (Constants.FINISH_PROCESS.contains(filters)) {
            map.put("queryOneMonth", String.valueOf(true));
        } else {
            map.put("queryOneMonth", String.valueOf(false));
        }

        if (filters.equals("myTeam")) {
            map.put("myTeam", String.valueOf(true));
            map.put("categorys", Constants.WAIT_PROCESS);
        } else if (filters.equals("notMyTeam")) {
            map.put("myTeam", String.valueOf(false));
            map.put("categorys", Constants.WAIT_PROCESS);
        } else {
            map.put("categorys", filters);
        }
        map.put("userid", mUser.getGid());
        map.put("orderBy", orderBy);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", "30");
        map.put("condition", condition);

        return map;
    }
}
