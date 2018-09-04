package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.ui.inpsectitem.ContactInspectItemViewXtd;
import com.ecity.cswatersupply.utils.DateUtil;
import com.z3app.android.util.StringUtil;

public abstract class AReportInspectItemParameter implements IRequestParameter {

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();

        fillSimpleFields(map);
        fillItems(map);

        return map;
    }

    protected abstract void fillSimpleFields(Map<String, String> map);

    protected abstract String getInspectItemsKey();

    protected abstract List<InspectItem> getInspectItems();

    protected final void fillItems(Map<String, String> map) {
        List<InspectItem> inspectItems = getInspectItems();
        Map<String, String> itemsMap = new HashMap<String, String>();
        for (InspectItem item : inspectItems) {
            fillItem(itemsMap, item);
        }

        if (StringUtil.isBlank(getInspectItemsKey())) {
            map.putAll(itemsMap);
        } else {
            JSONObject jsonObject = new JSONObject(itemsMap);
            if (null != jsonObject) {
                map.put(getInspectItemsKey(), jsonObject.toString());
            }
        }
    }

    private void fillItem(Map<String, String> map, InspectItem item) {
        String selectVavleValue = null;
        switch (item.getType()) {
            case TEXT: // TEXT 、NUMBER and TEXTEXT use same logic
                map.put("pumpname","");
            case TEXTEXT:
            case NUMBER:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case GEOMETRY_AREA:
                map.put(item.getName(), item.getValue());
                break;
            case GEOMETRY:
                String addr = ""+String.valueOf(item.getValue());
                String[] loaction = addr.split(";");
                if ( null!= loaction &&  loaction.length > 1 ) {
                    map.put(item.getName(), loaction[1]);
                } else {
                    map.put(item.getName(), addr);
                }
                break;
            case DEVICE: // SELECTVALVE and DEVICE use same logic
                map.put(item.getName(), item.getValue());
                break;
            case SELECTVALVE:DEVICE: // SELECTVALVE and DEVICE use same logic
                selectVavleValue = item.getValue();
                try {
                    JSONObject json = new JSONObject(selectVavleValue);
                    String gid = json.optString("gid");
                    String dname = json.optString("dname");
                    String valveNumber = json.optString("阀门编号");
                    User currentUser = HostApplication.getApplication().getCurrentUser();
                    map.put("VALVEID", gid);
                    map.put("TYPE", dname);
                    map.put("VALVENUM", valveNumber);
                    map.put("REPORTERID", currentUser.getId());
                    map.put("REPORTERNAME", currentUser.getTrueName());
                    map.put("REPORTTIME", DateUtil.getCurrentTime());
                } catch (JSONException e) {
                }
                break;
            case RADIO:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case RADIOTXT:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case CHECKBOX:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case DROPDOWNLIST:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case TOGGLE:
                map.put(item.getName(), item.getValue());
                break;
            case CONTACTMEN_MULTIPLE:
                if (!StringUtil.isBlank(item.getValue())) {
                    String stringResult = item.getValue().substring(1, item.getValue().length()-1);
                    String[] stringArray = stringResult.split(",");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < stringArray.length; i++) {
                        String xunjMen = stringArray[i].substring(1, stringArray[i].length()-1);
                        String[] array = xunjMen.split(ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR);
                        if (i!=stringArray.length-1) {
                            sb.append(array[0]+",");
                        }else{
                            sb.append(array[0]);
                        }
                    }
                    map.put(item.getName(), sb.toString());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case CONTACTMEN_SINGLE:
                if (!StringUtil.isBlank(item.getValue())) {
                    String[] array = item.getValue().split(ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR);
                    if (array.length > 1) {
                        map.put(item.getName(), array[0]);
                    } else {
                        map.put(item.getName(), item.getValue());
                    }
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case CONTACTMEN_SINGLE_PROJECT:
            case CONTACTMEN_MULTIPLE_PROJECT:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case TREE:
                if (!StringUtil.isBlank(item.getValue())) {
                    String values[] = item.getValue().split(",");
                    map.put(item.getName(), values[values.length - 1]);
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case DATE:
                if (!StringUtil.isBlank(item.getValue())) {
                    map.put(item.getName(), item.getValue());
                } else {
                    map.put(item.getName(), item.getDefaultValue());
                }
                break;
            case IMAGE:
                break;
            case VIDEO:
                break;
            case AUDIO:
                break;
            case ATTACHMENT:
                break;
            default:
                break;
        }
    }
}
