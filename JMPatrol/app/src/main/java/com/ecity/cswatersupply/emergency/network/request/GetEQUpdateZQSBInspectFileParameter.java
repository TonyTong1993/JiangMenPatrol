package com.ecity.cswatersupply.emergency.network.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.location.Location;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.zzz.ecity.android.applibrary.service.PositionService;

/**
 * 灾情速报更新
 * @author ml
 *
 */
public class GetEQUpdateZQSBInspectFileParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;
    private int gid;

    public GetEQUpdateZQSBInspectFileParameter(int gid) {
        this.gid = gid;
    }
    @Override

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("gid", String.valueOf(gid));
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
