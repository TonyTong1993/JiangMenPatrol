package com.ecity.cswatersupply.emergency.network.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.location.Location;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.utils.DateUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

/**
 * 灾情速报上报
 * @author ml
 *
 */
public class GetEQReportZQSBInspectFileParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;
    //地震id
    private String eqid;

    public GetEQReportZQSBInspectFileParameter(String eqid) {
        this.eqid = eqid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eqid", eqid);
        map.put("reporterid", HostApplication.getApplication().getCurrentUser().getId());
        map.put("reporter", HostApplication.getApplication().getCurrentUser().getTrueName());
        map.put("reportTime", DateUtil.getCurrentTime());
        double[] lonLat = getlonLat();
        map.put("lon", String.valueOf(lonLat[0]));
        map.put("lat", String.valueOf(lonLat[1]));
        return map;
    }
    
    public double[] getlonLat(){
        double[] lonlat = new double[2]; 
        Location location = PositionService.getLastLocation();
        if (null != location) {
            lonlat[0] = location.getLongitude();
            lonlat[1] = location.getLatitude();
        }else{
            lonlat[0] = 0.0;
            lonlat[1] = 0.0;
        }
        return lonlat;
    }
}
