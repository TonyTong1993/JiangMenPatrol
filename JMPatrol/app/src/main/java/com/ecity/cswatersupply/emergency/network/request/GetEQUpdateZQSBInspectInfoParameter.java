package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
/**
 * 获取需要更新的灾情速报表单数据
 * @author ml
 *
 */
public class GetEQUpdateZQSBInspectInfoParameter implements IRequestParameter {

    private int gid;
    public GetEQUpdateZQSBInspectInfoParameter(int gid) {
        this.gid = gid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("gid", String.valueOf(gid));
        return map;
    }
}
