package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetMessageParameter implements IRequestParameter {
    private User user;
    private int pageNo;
    private int pageSize;
    private boolean isProcessed;

    public GetMessageParameter(User user, int pageNo, int pageSize, boolean isProcessed) {
        this.user = user;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.isProcessed = isProcessed;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getGid());
        map.put("pageno", String.valueOf(pageNo));
        map.put("pagesize", String.valueOf(pageSize));

        return map;
    }
}
