package com.ecity.cswatersupply.adapter.checkitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.inpsectitem.ContactInspectItemViewXtd;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;

public class InspectItemAdapter {

    public static Map<String, List<InspectItem>> adaptWorkOrderDetailInfo(JSONObject jsonObject) {
        return null;
    }

    public static List<InspectItem> adaptItems(JSONObject jsonObject) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONArray serverItems = jsonObject.optJSONArray("params");
        if (serverItems != null) {
            items = adaptItems(serverItems);
        }

        return items;
    }

    public static List<InspectItem> adaptTaskItems(JSONObject jsonObject) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONArray paramItems = jsonObject.optJSONArray("params");
        JSONObject serverItem = paramItems.optJSONObject(0);
        JSONArray serverItems = serverItem.optJSONArray("items");
        if (serverItems != null) {
            items = adaptItems(serverItems);
        }

        return items;
    }

    public static List<InspectItem> adaptEventReportItems(JSONObject jsonObject) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONArray serverGroups = jsonObject.optJSONArray("params");
        if (serverGroups != null) {
            JSONObject serverItems = serverGroups.optJSONObject(0);
            if (serverItems != null) {
                items = adaptItems(serverItems.optJSONArray("items"));
            }
        }
        return items;
    }

    /****
     * 南昌外勤，解析工单检查项
     * @param jsonObject
     * @return
     */
    public static List<InspectItem> adaptFormReportItems(JSONObject jsonObject) {
        JSONArray serverGroups = jsonObject.optJSONArray("params");
        if(null == serverGroups) {
            return new ArrayList<InspectItem>();
        }

        return adaptItems(serverGroups);
    }

    public static List<InspectItem> adaptValveReportItems(JSONObject jsonObject) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONArray serverGroups = jsonObject.optJSONArray("params");
        if (serverGroups != null) {
            items = adaptItems(serverGroups);
        }
        return items;
    }

    /**
     * 从服务获取这个灾情速报的表单
     * @param jsonObject
     * @return
     */
    public static InspectItem adaptEQZQSBItems(JSONObject jsonObject) {
        return adaptItem(jsonObject);
    }

    public static InspectItem adaptItem(JSONObject serverItem) {
        InspectItem item = new InspectItem();
        String defaultValue = serverItem.optString("defaultValue");
        item.setAlias(serverItem.optString("alias"));
        item.setDefaultValue(defaultValue);
        item.setName(serverItem.optString("name"));
        item.setVisible(serverItem.optString("visible").equalsIgnoreCase("1"));
        item.setRequired(serverItem.optString("required").equalsIgnoreCase("1"));
        item.setLongText(serverItem.optString("len").equalsIgnoreCase("1"));
        item.setIncrease(serverItem.optString("increase").equalsIgnoreCase("1"));
        item.setEdit(serverItem.optString("edit").equalsIgnoreCase("1"));
        item.setType(getItemType(serverItem.optString("type")));
        if ((!StringUtil.isBlank(defaultValue)) && (serverItem.optString("alias").equalsIgnoreCase(ResourceUtil.getStringById(R.string.inspect_item_start_time)))) {
            item.setValue(defaultValue);
        } else {
            item.setValue(serverItem.optString("value"));
        }
        item.setGeoType(serverItem.optInt("gtype"));

        JSONArray selectValuesArray = serverItem.optJSONArray("selectValues");
        if (null != selectValuesArray) {
            item.setSelectValues(selectValuesArray.toString());
            if (EInspectItemType.CONTACTMEN_MULTIPLE == item.getType()) {
                setDefaultSelectedContacts(item, selectValuesArray);
            }
        } else {
            item.setSelectValues(serverItem.optString("selectValues"));
        }

        JSONArray childrenItems = serverItem.optJSONArray("childs");
        if (null == childrenItems) {
            childrenItems = serverItem.optJSONArray("items");
        }

        if ((item.getType() == EInspectItemType.GROUP || item.getType() == EInspectItemType.TAB) && (null != childrenItems)) {
            item.setChilds(adaptItems(childrenItems));
        }

        if (EInspectItemType.DEVICE == item.getType()) {
            if (!item.isEdit()) {
                item.setType(EInspectItemType.TEXT);
            }
        }

        JSONObject cascadeGroup = serverItem.optJSONObject("cascadeGroup");
        if(null != cascadeGroup) {
            item.setCascadeGroupName(cascadeGroup.optString("group"));
            item.setCascadeGroupSn(cascadeGroup.optString("sn"));
        }

        return item;
    }

    public static InspectItem adaptIncreaseItem(JSONObject serverItem) {
        InspectItem item = new InspectItem();
        String defaultValue = serverItem.optString("defaultValue");
        item.setAlias(serverItem.optString("alias"));
        item.setDefaultValue(defaultValue);
        item.setName(serverItem.optString("name"));
        item.setVisible(serverItem.optString("visible").equalsIgnoreCase("1"));
        item.setRequired(serverItem.optString("required").equalsIgnoreCase("1"));
        item.setLongText(serverItem.optString("len").equalsIgnoreCase("1"));
        item.setIncrease(serverItem.optString("increase").equalsIgnoreCase("1"));
        item.setEdit(serverItem.optString("edit").equalsIgnoreCase("1"));
        item.setType(getItemType(serverItem.optString("type")));
        if ((!StringUtil.isBlank(defaultValue)) && (serverItem.optString("alias").equalsIgnoreCase(ResourceUtil.getStringById(R.string.inspect_item_start_time)))) {
            item.setValue(defaultValue);
        } else {
            item.setValue(serverItem.optString("value"));
        }
        item.setGeoType(serverItem.optInt("gtype"));

        JSONArray selectValuesArray = serverItem.optJSONArray("selectValues");
        if (null != selectValuesArray) {
            item.setSelectValues(selectValuesArray.toString());
            if (EInspectItemType.CONTACTMEN_MULTIPLE == item.getType()) {
                setDefaultSelectedContacts(item, selectValuesArray);
            }
        } else {
            item.setSelectValues(serverItem.optString("selectValues"));
        }
        //
        String childrenString = serverItem.optString("childs");
        //

        JSONArray childrenItems = null;
        try {
            childrenItems = new JSONArray(childrenString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ((item.getType() == EInspectItemType.GROUP) && (null != childrenItems)) {
            item.setChilds(adaptItems(childrenItems));
        }

        if (EInspectItemType.DEVICE == item.getType()) {
            if (!item.isEdit()) {
                item.setType(EInspectItemType.TEXT);
            }
        }

        return item;
    }

    public static List<InspectItem> adaptItems(JSONArray serverItems) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        if (serverItems == null) {
            return items;
        }

        for (int i = 0; i < serverItems.length(); i++) {
            JSONObject serverItem = serverItems.optJSONObject(i);
            if (serverItem == null) {
                continue;
            }

            InspectItem item = adaptItem(serverItem);
            items.add(item);
        }

        return items;
    }

    private static EInspectItemType getItemType(String type) {
        for (EInspectItemType e : EInspectItemType.values()) {
            if (e.getValue().equalsIgnoreCase(type) || e.name().equals(type)) {
                return e;
            }
        }

        return EInspectItemType.TEXT;// 类型缺省，返回TEXT
    }

    private static void setDefaultSelectedContacts(InspectItem item, JSONArray selectValuesArray) {
        JSONArray contactIds = new JSONArray();
        for (int i = 0; i < selectValuesArray.length(); i++) {
            JSONObject selectValue = selectValuesArray.optJSONObject(i);
            if (selectValue == null) {
                continue;
            }

            Iterator<String> iterator = selectValue.keys();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                contactIds.put(key + ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR + selectValue.optString(key));
                break; // For each selectValue, the format is ""9": "蓬江东区004"", only one key. And the key is user id.
            }
        }

        if (contactIds.length() > 0) {
            item.setValue(contactIds.toString());
        }
    }

    public static Map<String, List<InspectItem>> adaptDetailItems(JSONObject jsonObject) {
        Map<String, List<InspectItem>> workOrderDetailInfo = new HashMap<String, List<InspectItem>>();
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONArray paramsItems = jsonObject.optJSONArray("params");
        if (paramsItems != null) {
            for (int i = 0; i < paramsItems.length(); i++) {
                JSONObject jsonObj = paramsItems.optJSONObject(i);
                if (jsonObj == null) {
                    continue;
                }

                String aliasStr = jsonObj.optString("alias");
                items = adaptDetailItems(paramsItems, i);
                workOrderDetailInfo.put(aliasStr, items);
            }
        }

        return workOrderDetailInfo;
    }

    public static String getFlowUrl(JSONObject jsonObject) {
        String flowUrl = null;
        JSONArray paramsItems = jsonObject.optJSONArray("params");
        if (paramsItems != null) {
            for (int i = 0; i < paramsItems.length(); i++) {
                JSONObject jsonObj = paramsItems.optJSONObject(i);
                if (jsonObj == null) {
                    continue;
                }

                String aliasStr = jsonObj.optString("alias");
                if (aliasStr == "流程信息") {
                    flowUrl = jsonObj.optString("url");
                }
            }
        }

        return flowUrl;
    }

    private static List<InspectItem> adaptDetailItems(JSONArray params, int position) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONObject serverItem = params.optJSONObject(position);
        JSONArray infoArr = serverItem.optJSONArray("items");
        for (int j = 0; j < infoArr.length(); j++) {
            JSONObject itemObj = infoArr.optJSONObject(j);
            InspectItem item = adaptItem(itemObj);

            if (!item.isEdit() && (item.getType() != EInspectItemType.IMAGE) && (item.getType() != EInspectItemType.GEOMETRY) && (item.getType() != EInspectItemType.RADIO)) {
                item.setType(EInspectItemType.TEXT);
            }
            items.add(item);

        }
        return items;
    }

    public static List<InspectItem> adaptProjectDetailItems(JSONObject jsonObject) {
        List<InspectItem> items = new ArrayList<InspectItem>();
        JSONArray paramsItems = jsonObject.optJSONArray("params");
        if (paramsItems != null) {
            for (int i = 0; i < paramsItems.length(); i++) {
                JSONObject jsonObj = paramsItems.optJSONObject(i);
                InspectItem item = adaptItem(jsonObj);

                if (!item.isEdit() && (item.getType() != EInspectItemType.IMAGE) && (item.getType() != EInspectItemType.GEOMETRY) && (item.getType() != EInspectItemType.RADIO)
                        && item.getType() != EInspectItemType.ATTACHMENT && item.getType() != EInspectItemType.SECTION_TITLE) {
                    item.setType(EInspectItemType.TEXT);
                }
                items.add(item);
            }
        }

        return items;
    }
}
