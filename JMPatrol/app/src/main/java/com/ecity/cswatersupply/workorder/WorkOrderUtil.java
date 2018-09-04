package com.ecity.cswatersupply.workorder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.MenuFactory;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderMTField;
import com.ecity.cswatersupply.workorder.model.WorkOrderSource;
import com.z3app.android.util.StringUtil;

public class WorkOrderUtil {
    /**
     * 根据工单元数据，获取字段别名
     */
    public static String getAliasFromMetas(String key, String name) {
        if (SessionManager.workOrderMetasHaveParsed && SessionManager.workOrderMetas != null) {
            WorkOrderMTField meta = SessionManager.workOrderMetas.get(key);
            if (meta != null && meta.values != null && meta.values.containsKey(name)) {
                return meta.values.get(name);
            }
        }
        return name;
    }

    public static String getWorkOrderStateString(Map<String, String> attributes) {
        StringBuilder displayState = new StringBuilder();
        String subState = attributes.get(WorkOrder.KEY_SUB_STATE);
        // 有子状态时，显示子状态，没有则显示主状态
        if (!StringUtil.isBlank(subState)) {
            String[] states = subState.split(",");
            for (String state : states) {
                String stateAlias = WorkOrderUtil.getAliasFromMetas(WorkOrder.KEY_SUB_STATE, state);
                displayState.append(stateAlias);
                displayState.append("，");
            }
            displayState.deleteCharAt(displayState.length() - 1);//去掉结尾的“，”
            return displayState.toString();
        } else {
            return WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_STATE);
        }
    }

    /**
     * 获取工单字段属性值的别名，例如，key为source，取到当前工单的source为hotLine，返回别名：热线
     * 
     * @param key
     *            工单属性中的key
     */
    public static String getAliasOfValue(Map<String, String> attributes, String key) {
        String value = attributes.get(key);
        if (value != null) {
            return getAliasFromMetas(key, value);
        }
        return "";
    }

    /**
     * 获取工单属性中Key的别名，例如：key为source，返回别名：工单来源
     */
    public static String getAliasOfKey(String key) {
        if (SessionManager.workOrderMetasHaveParsed && SessionManager.workOrderMetas != null) {
            WorkOrderMTField meta = SessionManager.workOrderMetas.get(key);
            if (meta != null) {
                return meta.alias;
            }
        }
        return key;
    }

    /**
     * 字段是否为空
     */
    public static boolean isEmptyStr(View v, String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 设置工单来源背景
     */
    public static void setBackGroundOfWorkOrderFrom(TextView from) {
        String workOrderFrom = from.getText().toString();
        Drawable bg = null;
        if (workOrderFrom.equals(WorkOrderSource.ZIBAO.value)) {
            bg = ResourceUtil.getDrawableResourceById(R.drawable.css_bg_blue_corner);
        } else if (workOrderFrom.equals(WorkOrderSource.REXIAN.value)) {
            bg = ResourceUtil.getDrawableResourceById(R.drawable.css_bg_orange_corner);
        } else if (workOrderFrom.equals(WorkOrderSource.PATROL.value)) {
            bg = ResourceUtil.getDrawableResourceById(R.drawable.css_bg_purple_corner);
        } else if (workOrderFrom.equals(WorkOrderSource.JIANLOU.value)) {
            bg = ResourceUtil.getDrawableResourceById(R.drawable.css_bg_green_corner);
        }
        if (bg != null) {
//            from.setBackground(bg);
        }
    }

    public static void replaceAllBtnAlias() {
        Map<String, AppMenu> allBtns = MenuFactory.getWorkOrderBtns();
        Iterator<String> keies = allBtns.keySet().iterator();
        while (keies.hasNext()) {
            AppMenu tempMenu = allBtns.get(keies.next());
            tempMenu.setSubName(tempMenu.getName()); // 记录按钮的key，如transfer, cancelTransfer
            tempMenu.setName(getAliasFromMetas(WorkOrder.KEY_OPERATE_BTNS, tempMenu.getName()));
        }
    }

    public static String getWorkOrderDetailUrl(WorkOrderDetailTabModel tabModel) {
        String serverUrl = tabModel.getUrl();
        return getSplitUrl(serverUrl);
    }

    public static Map<String, String> getWorkOrderDetailParam(WorkOrderDetailTabModel tabModel, String processInstanceId) {
        String serverUrl = tabModel.getUrl();
        String[] requestInfos = serverUrl.split("\\?");

        String serverParamName = requestInfos[1].substring(0, requestInfos[1].indexOf("="));
        String serverParamValue = requestInfos[1].substring(requestInfos[1].indexOf("=") + 1, requestInfos[1].length());
        try {
            serverParamValue = URLDecoder.decode(serverParamValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(serverParamName, serverParamValue);
        map.put("processinstanceid", processInstanceId);
        return map;
    }

    public static String getSplitUrl(String serverUrl) {
        String[] requestInfos = serverUrl.split("\\?");

        String url = requestInfos[0].replace("WORKFLOW_SVR|", ServiceUrlManager.getInstance().getFieldWorkServiceUrl());
        return url;
    }

    public static Map<String, String> getServerParam(String serverUrl) {
        String[] requestInfos = serverUrl.split("\\?");

        String serverParamName = requestInfos[1].substring(0, requestInfos[1].indexOf("="));
        String serverParamValue = requestInfos[1].substring(requestInfos[1].indexOf("=") + 1, requestInfos[1].length());
        try {
            serverParamValue = URLDecoder.decode(serverParamValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(serverParamName, serverParamValue);
        return map;
    }

}
