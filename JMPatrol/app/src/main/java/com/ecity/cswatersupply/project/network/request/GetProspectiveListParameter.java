package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProspectiveListParameter implements IRequestParameter {
    private User user;
    private int pageNo;
    private int pageSize;
    private int position;

    public GetProspectiveListParameter(User user, int pageNo, int pageSize, int position) {
        this.user = user;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.position = position;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        map.put("pageno", String.valueOf(pageNo));
        map.put("pagesize", String.valueOf(pageSize));
        if (position == 0) {
            map.put("state", "0,1,2,3,4,6,7,9");
        } else {
            map.put("state", "5,8");
        }

        return map;
    }

}
