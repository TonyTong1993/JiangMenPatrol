package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;
import android.widget.Toast;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

/**
 * 异常上报参数
 * @author Gxx 2016-12-8
 */
public class ReportUnUsualFormParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private String code;
    private String eqid;
    
    public ReportUnUsualFormParameter(List<InspectItem> items) {
        this.items = items;
    }
    public void setCode(String code) {
        this.code = code;
    }
   
    public void setEqid(String eqid) {
        this.eqid = eqid;
    }
    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        map.put("reporterid", HostApplication.getApplication().getCurrentUser().getId());
        map.put("reporter", HostApplication.getApplication().getCurrentUser().getTrueName());
        map.put("reporterTime", DateUtil.getCurrentTime());
        map.put("lat", getCurrentLocation().get("lat"));
        map.put("lon", getCurrentLocation().get("lon"));
        if(!StringUtil.isBlank(code)) {
            map.put("code", code);
        }
        if(!StringUtil.isBlank(eqid)) {
            map.put("eqid", eqid);
        }
    }

    private Map<String, String> getCurrentLocation() {
        Map<String, String> location = new HashMap<String, String>();
        for (InspectItem inspectItem : items) {
            if (inspectItem.getType().toString().equalsIgnoreCase("GEOMETRY")) {
                String[] locate = inspectItem.getValue().split(";")[0].split(",");
                try {
                    location.put("lat", String.valueOf(locate[1]));
                    location.put("lon", String.valueOf(locate[0]));
                    break;
                } catch (NumberFormatException e) {
                    LogUtil.e(this, e);
                }
            }
        }
        if (location.isEmpty()) {
            String longitude = null;
            String latitude = null;
            double[] eventLoctation = null;
            try {
                Location currentLocation = null;
                currentLocation = PositionService.getLastLocation();
                if (null != currentLocation){
                    longitude = String.valueOf(currentLocation.getLongitude());
                    latitude = String.valueOf(currentLocation.getLatitude());
                    eventLoctation = CoordTransfer.transToLocal(Double.parseDouble(latitude), Double.parseDouble(longitude));
                }else{
                    ToastUtil.showMessage(HostApplication.getApplication(), R.string.event_reprot_hint_address_no_current_location, Toast.LENGTH_LONG);
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
                ToastUtil.showMessage(HostApplication.getApplication(), R.string.event_reprot_hint_address_no_current_location, Toast.LENGTH_LONG);
            }
            location.put("lat", String.valueOf(eventLoctation[1]));
            location.put("lon", String.valueOf(eventLoctation[0]));
        }
        return location;
    }

    @Override
    protected String getInspectItemsKey() {
        return "properties";
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return items;
    }
}
