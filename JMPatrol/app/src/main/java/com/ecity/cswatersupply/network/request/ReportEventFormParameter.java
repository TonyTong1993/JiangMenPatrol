package com.ecity.cswatersupply.network.request;

import android.location.Location;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.inpsectitem.PumpInspectItemViewXtd;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportEventFormParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private String eventTableName;
    private String key;
    //巡检和养护任务事件上报时，需要上报任务id
    private int taskid;

    public ReportEventFormParameter(List<InspectItem> items, String eventTableName, String key) {
        this.items = items;
        this.eventTableName = eventTableName;
        this.key = key;
        setUnVisibleInspectItemValue();
    }

    /***
     * 从设备类型检查项的值中取出设备编号(gid)给设备编号检查项复制
     */
    private void setUnVisibleInspectItemValue() {
        String value = "";
        for(InspectItem item : items) {
            if("shebeileixing".equals(item.getName())) {
                value = item.getValue();
            }
        }

        for(InspectItem item : items) {
            if("shebeibianhao".equals(item.getName()) && !StringUtil.isBlank(value)) {
                try {
                    JSONObject json = new JSONObject(value);
                    item.setValue(json.optString("gid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ReportEventFormParameter(List<InspectItem> items, String eventTableName, String key,int taskid) {
        this(items, eventTableName, key);
        this.taskid = taskid;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        map.put("action", "");
        map.put("token", "");
        map.put("tableName", eventTableName);
        map.put("eventtype", String.valueOf(key));
        map.put("properties", getInspectItemsKey());
        map.put("userid", HostApplication.getApplication().getCurrentUser().getId());
        map.put("username", HostApplication.getApplication().getCurrentUser().getTrueName());
        if(0 != taskid) {
            map.put("taskid", String.valueOf(taskid));
        }

        Map<String, String> locationInfo = getSelectedLocation();
        if (locationInfo == null) {
            locationInfo = getCurrentLocation();
        }

        if (locationInfo != null) {
            map.putAll(locationInfo);
        }
    }

    @Override
    protected String getInspectItemsKey() {
        List<InspectItem> inspectItems = getInspectItems();
        Map<String, String> parameterMap = new HashMap<String, String>();
        for (int i = 0; i < inspectItems.size(); i++) {
            if (inspectItems.get(i).getType().name().equalsIgnoreCase(EInspectItemType.IMAGE.name())) {
                continue;
            }
            if (inspectItems.get(i).getType().name().equalsIgnoreCase(EInspectItemType.AUDIO.name())) {
                continue;
            }
            if (inspectItems.get(i).getType().name().equalsIgnoreCase(EInspectItemType.VIDEO.name())) {
                continue;
            }
            if (items.get(i).getType().name().equalsIgnoreCase(EInspectItemType.GEOMETRY.name())) {
                String addr = ""+String.valueOf(items.get(i).getValue());
                String[] loaction = addr.split(";");
                if ( null!= loaction &&  loaction.length > 1 ) {
                    parameterMap.put(inspectItems.get(i).getName(), loaction[1]);
                } else {
                    parameterMap.put(inspectItems.get(i).getName(), addr);
                }

                continue;
            }
            if (items.get(i).getType().name().equalsIgnoreCase(EInspectItemType.NAMEGEOM.name())) {
                JSONObject json = new JSONObject();
                String[] pumpValues = PumpInspectItemViewXtd.constructPumpParams(json, items.get(i).getValue());
                if(null != pumpValues && pumpValues.length >= 4 ) {
                    parameterMap.put(inspectItems.get(i).getName(), pumpValues[0] + "," + pumpValues[1]);
                    parameterMap.put("pumpgid", pumpValues[2]);
                }
                json = null;
                continue;
            }

            parameterMap.put(inspectItems.get(i).getName(), inspectItems.get(i).getValue());
            parameterMap.put("userid", HostApplication.getApplication().getCurrentUser().getId());
            parameterMap.put("pumpname","");
        }
        Gson gson = new Gson();
        String parameters = gson.toJson(parameterMap);
        return parameters;
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return items;
    }

    private Map<String, String> getSelectedLocation() {
        InspectItem inspectItem = getGeometryItem();
        if (inspectItem == null) {
            return null;
        }

        Map<String, String> location = new HashMap<String, String>();
        if(!inspectItem.getValue().contains(";")) {
            return null;
        }
        String[] values = inspectItem.getValue().split(";");
        if ((values == null) || (values.length < 2)) {
            return null;
        }

        String[] xy = values[0].split(",");
        if ((xy == null) || (values.length < 2)) {
            return null;
        }
        double[] lonLat = CoordTransfer.transToLatlon(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
        location.put("lon", String.valueOf(lonLat[0]));
        location.put("lat", String.valueOf(lonLat[1]));
        location.put("x", xy[0]);
        location.put("y", xy[1]);

        return location;
    }

    private Map<String, String> getCurrentLocation() {
        Map<String, String> map = new HashMap<String, String>();
        Location location = CurrentLocationManager.getLocation();
        if (location == null) {
            return null;
        }
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());
        double[] xy = CoordTransfer.transToLocal(Double.parseDouble(latitude), Double.parseDouble(longitude));
        map.put("lon", longitude);
        map.put("lat", latitude);
        map.put("x", String.valueOf(xy[0]));
        map.put("y", String.valueOf(xy[1]));

        return map;
    }

    private InspectItem getGeometryItem() {
        for (InspectItem item : items) {
            if (item.getType() == EInspectItemType.GEOMETRY || item.getType() == EInspectItemType.NAMEGEOM) {
                return item;
            }
        }

        return null;
    }
}
