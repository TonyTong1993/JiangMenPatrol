package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;

import java.util.HashMap;
import java.util.Map;

//  * 获取某一项目对应的整改列表
//          * @param reporter 上报人
//        * @param source 来源
//        * @param evetype 事件类型
//        * @param createtime0 开始时间
//        * @param createtime1 结束时间
//        * @param proid 项目名称

public class GetSafeEventListParameter implements IRequestParameter {
    private User user;
    SafeProjectListModel model;

    public GetSafeEventListParameter(User user, SafeProjectListModel model) {
        this.user = user;
        this.model = model;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", user.getId());
        map.put("proid", model.getGid());
        return map;
    }

}
