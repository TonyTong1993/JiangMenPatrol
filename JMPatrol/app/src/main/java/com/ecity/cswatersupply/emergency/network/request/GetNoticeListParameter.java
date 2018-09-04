package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetNoticeListParameter implements IRequestParameter{

    private User user;
    private int pageNo;
    private int pageSize;
    public GetNoticeListParameter(User user, int pageNo, int pageSize) {
        this.user = user;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", user.getId());//不做限制，所有人均可看到消息
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }

}
