package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.model.GPSPositionBean;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.utils.CurrentLocationManager;

public class PostPositionsParameter implements IRequestParameter {

    private List<GPSPositionBean> posList;

    public PostPositionsParameter(List<GPSPositionBean> posList) {
        this.posList = posList;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pospackage", getPosPackage());

        return map;
    }

    public String getPosPackage() {
        return CurrentLocationManager.toJSONString(posList);
        //return GsonUtil.toJson(posList);
    }

}
