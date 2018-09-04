package com.ecity.cswatersupply.emergency.network.request;

import android.location.Location;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

public class GetEarthQuakeInspectFileParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;
    private EarthQuakeQuickReportModel model;
    private List<InspectItem> list;

    public GetEarthQuakeInspectFileParameter(EarthQuakeQuickReportModel model, List<InspectItem> list) {
        this.model = model;
        this.list = list;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eqid", String.valueOf(model.getEarthQuakeId()));
//        map.put("gid", String.valueOf(model.getGid()));
        map.put("reporterid", HostApplication.getApplication().getCurrentUser().getId());
        map.put("reporter", HostApplication.getApplication().getCurrentUser().getTrueName());
        map.put("reportTime", DateUtil.getCurrentTime());
        map.put("tablename", "003");
        double[] lonLat = getlonLat();
        if(null == lonLat || lonLat.length < 2) {
            lonLat = getDefultlonLat();
        }
        map.put("lon", String.valueOf(lonLat[0]));
        map.put("lat", String.valueOf(lonLat[1]));

        map.put("area", SessionManager.whdzdistrict);
        return map;
    }

    public double[] getDefultlonLat() {
        double[] lonlat = new double[2];
        Location location = PositionService.getLastLocation();
        if (null != location) {
            lonlat[0] = location.getLongitude();
            lonlat[1] = location.getLatitude();
        } else {
            lonlat[0] = 0.0;
            lonlat[1] = 0.0;
        }
        return lonlat;
    }


    public double[] getlonLat() {
        List<InspectItem> items = InspectItemUtil.mergeAllItemsInline(list);
        for(InspectItem item : items) {
            if(item.getName().equals("address") && item.getAlias().equals("调查点")) {
                String value = item.getValue();
                return filterLonLat(value);
            }
        }

        return null;
    }

    private double[] filterLonLat(String value) {
        String[] strs = new String[2];
        if(value.contains(";")) {
            strs = value.split(";");
            if(null == strs || strs.length < 2) {
                return null;
            }
            String temp = strs[0];
            return splitLatLon(temp);
        } else {
            return splitLatLon(value);
        }
    }

    private double[] splitLatLon(String str) {
        double[] lonlat = new double[2];
        if(str.contains(",")) {
            String[] lonLat = str.split(",");
            if(null == lonLat || lonLat.length < 2) {
                return null;
            }
            lonlat[0] = Double.valueOf(lonLat[0]);
            lonlat[1] = Double.valueOf(lonLat[1]);
            return lonlat;
        }
        return null;
    }

}
