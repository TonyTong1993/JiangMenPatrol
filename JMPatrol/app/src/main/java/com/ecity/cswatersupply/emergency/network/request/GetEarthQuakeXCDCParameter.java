package com.ecity.cswatersupply.emergency.network.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.utils.DateUtil;

public class GetEarthQuakeXCDCParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;
    //地震id
    private int eqid;
    //灾情速报id
    private int did;

    public GetEarthQuakeXCDCParameter(int eqid, int did) {
        this.eqid = eqid;
        this.did = did;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(); 
        map.put("eqid", eqid+"");
        map.put("did", "2");
        map.put("reporterid", HostApplication.getApplication().getCurrentUser().getId());
        map.put("reporter", HostApplication.getApplication().getCurrentUser().getTrueName());
        map.put("reportTime", DateUtil.getCurrentTime());
        return map;
    }
}
