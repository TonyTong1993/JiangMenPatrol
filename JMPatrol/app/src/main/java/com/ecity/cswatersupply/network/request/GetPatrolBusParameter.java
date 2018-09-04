package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/***  
 * Created by MaoShouBei on 2017/5/25.
 */

public class GetPatrolBusParameter implements RequestParameter.IRequestParameter {
    private String mGroupcode;
    private String mBusNo;
    private boolean mIsMonitor;

    public GetPatrolBusParameter(String groupCode, String busNo, boolean isMonitor) {
        mGroupcode = groupCode;
        mBusNo = busNo;
        mIsMonitor = isMonitor;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (null == mBusNo) {
            map.put("busno", "");
        } else {
            map.put("busno", mBusNo);
        }
        map.put("code", mGroupcode);
        map.put("ismonitor", Boolean.toString(mIsMonitor));
        map.put("f", "json");
        return map;
    }
}
