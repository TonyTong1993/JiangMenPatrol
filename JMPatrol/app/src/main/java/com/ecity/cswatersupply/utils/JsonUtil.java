package com.ecity.cswatersupply.utils;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.map.AMapMenu;
import com.ecity.cswatersupply.model.AddressInfoModel;
import com.ecity.cswatersupply.model.BaseResponse;
import com.ecity.cswatersupply.model.FlowInfoBean;
import com.ecity.cswatersupply.model.ImageModel;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.model.PatrolBusInfo;
import com.ecity.cswatersupply.model.PatrolUser;
import com.ecity.cswatersupply.model.PumpRepairAndMaintainInfoModel;
import com.ecity.cswatersupply.model.SignInStateBean;
import com.ecity.cswatersupply.model.SummaryDetailModel;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.model.WorkOrderOperationLogBean;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;
import com.ecity.cswatersupply.model.event.ConstructionEvent;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.model.event.GISReportEvent;
import com.ecity.cswatersupply.model.event.MeasureEvent;
import com.ecity.cswatersupply.model.event.PatrolEvent;
import com.ecity.cswatersupply.model.event.PointLeakageEvent;
import com.ecity.cswatersupply.model.event.PumpEvent;
import com.ecity.cswatersupply.model.event.PunishmentEvent;
import com.ecity.cswatersupply.model.event.RepairementEvent;
import com.ecity.cswatersupply.network.response.loginresponse.LoginResponse;
import com.ecity.cswatersupply.network.response.loginresponse.LoginTokenResponse;
import com.ecity.cswatersupply.project.model.ProjectLogBean;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.ecity.cswatersupply.workorder.model.UnFinishWorkOrderAddressInfo;
import com.ecity.cswatersupply.workorder.model.UnFinishWorkOrderInfo;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.ecity.cswatersupply.workorder.model.WorkOrderMTField;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryBar;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryBarBean;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryPie;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryPieBean;
import com.ecity.cswatersupply.workorder.network.WorkorderLeaderModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.z3app.android.util.JSONUtils;
import com.z3app.android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class JsonUtil {

    /**
     * JSONObject。若出现异常，返回null。
     *
     * @param json
     * @return JSONArray对象。若出现异常，返回null。
     */
    private static JSONObject paraseJSONToBaseResponse(JSONObject json) {
        JSONObject data = null;
        try {
            data = json.getJSONObject("data");
        } catch (Exception e) {
            LogUtil.e("JsonUtil", e);
        }

        return data;
    }
    /**
     * LoginTokenResponse。若出现异常，返回null。
     *
     * @param json
     * @return LoginTokenResponse对象。若出现异常，返回null。
     */
    public static LoginTokenResponse paraseJSONTOLoginTokenResponse(JSONObject json) {
        String token = null;
        JSONObject data = paraseJSONToBaseResponse(json);
        try {
            if (data.has("token")) {
                token = data.getString("token");
            }
        } catch (Exception e) {
            LogUtil.e("JsonUtil", e);
        }
        LoginTokenResponse tokenResponse = new LoginTokenResponse(token);
        return tokenResponse;
    }

    /*
     * mapcfg:地图配置相关
     * role:角色分类
     * group:部门
     * org:组织机构
     * user:用户信息
     * */
    public static LoginResponse paraseJSONObjectToLoginResponse(JSONObject json) {
        JSONObject data = paraseJSONToBaseResponse(json);
        LoginResponse response = null;
        try {
            String mapConfig = data.getString("mapcfg");
            JSONArray role = data.getJSONArray("role");
            JSONArray group = data.getJSONArray("group");
            JSONObject org = data.getJSONObject("org");
            JSONObject userInfo = data.getJSONObject("user");
            JSONArray menus = data.getJSONArray("menu");
            User user = parserUserInfoToUserModel(userInfo);
            ArrayList<AppMenu> appMenus = getAppMenusWithMenusJSONArray(menus);

            response = new LoginResponse();
            response.setMenus(appMenus);
            response.setUser(user);
        } catch (Exception e) {
            System.out.print(e);
            LogUtil.e("JsonUtil", e);
        }

        return response;
    }
    /**
     * 将json字符串转换为JSONArray对象。若出现异常，返回null。
     *
     * @param userInfo
     * @return User对象。若出现异常，返回null。
     */
    private static User parserUserInfoToUserModel(JSONObject userInfo) {
        User user = null;
        try {
          String trueName =  userInfo.getString("username");
          String loginname =  userInfo.getString("loginname");
          String sss= URLDecoder.decode(trueName,"gbk");
          String phone =  userInfo.getString("phone");
          String email =  userInfo.getString("email");
          String oid =  userInfo.getString("oid");
          String isadmin =  userInfo.getString("isadmin");
          user = new User();
          user.setTrueName(trueName);
          user.setLoginName(loginname);
          user.setPhone(phone);
          user.setEmail(email);
          user.setOid(oid);
          user.setIsadmin(isadmin);
        }catch (Exception e) {
            LogUtil.e("JsonUtil", e);
        }

        return user;
    }

    /**
     * 将json字符串转换为JSONArray对象。若出现异常，返回null。
     *
     * @param menus
     * @return ArrayList<AppMenu>。若出现异常，返回空的集合。
     */
    private static  ArrayList<AppMenu> getAppMenusWithMenusJSONArray(JSONArray menus) {
        ArrayList<AppMenu> appMenus = new ArrayList<>();

        for (int i = 0;i < menus.length();i++) {
            try {
                JSONObject menu = menus.getJSONObject(i);
                String paramsJSONString = menu.getString("params");
                String menuType = null;
                String gid = null;

                if (!StringUtil.isBlank(paramsJSONString)) {
                    JSONObject params = new  JSONObject(paramsJSONString);
                    if (params.has("menuType")) {
                        menuType = params.getString("menuType");
                    }
                    if (params.has("identity")) {
                        gid = params.getString("identity");
                    }
                }

                String menuName = menu.getString("name");
                ArrayList<AppMenu> childMenus = null;
                if (menu.has("children")) {
                    JSONArray children = menu.getJSONArray("children");
                    if (children.length() > 0) {
                        childMenus = getAppMenusWithMenusJSONArray(children);
                    }
                }
                AppMenu appMenu = new AppMenu(menuName,childMenus,menuType);
                appMenu.setGid(gid);
                appMenu.setName(menuName);
                appMenus.add(appMenu);

            }catch (Exception e) {
                LogUtil.e("JsonUtil", e);
            }
        }
        return appMenus;
    }


    /**
     * 将json字符串转换为JSONArray对象。若出现异常，返回null。
     *
     * @param json
     * @return JSONArray对象。若出现异常，返回null。
     */
    public static JSONArray getJsonArray(String json) {
        if (StringUtil.isBlank(json)) {
            return null;
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            LogUtil.e("JsonUtil", e);
        }

        return jsonArray;
    }

    /**
     * 将json字符串转换为JSONObject对象。若出现异常，返回null。
     *
     * @param json
     * @return JSONObject对象。若出现异常，返回null。
     */
    public static JSONObject getJsonObject(String json) {
        if (StringUtil.isBlank(json)) {
            return null;
        }

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            LogUtil.e("JsonUtil", e);
        }

        return jsonObj;
    }

    public static Map<String, String> json2Map(JSONObject json) {
        Map<String, String> map = new HashMap<String, String>();

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, json.optString(key));
        }

        return map;
    }

    public static Map<String, String> paraseUserInfoToOldDataFormatter(JsonObject json) {

        return null;
    }

    /**
     * 解析工单 工单2.0解析方法
     *
     * @param jsonObj
     * @return
     */

    public static String parseWorkOrderCategory(JSONObject jsonObj) {
        String category = jsonObj.optString("name");
        return category;
    }

    public static Map<String, String> parseWorkOrderCategoryAmount(JSONObject jsonObject) {
        Map<String, String> categoryAmount = new HashMap<String, String>();
        String name = "waitamount";
        String count = jsonObject.optString(name);
        categoryAmount.put(name, count);
        name = "executeamount";
        count = jsonObject.optString(name);
        categoryAmount.put(name, count);
        name = "finishamount";
        count = jsonObject.optString(name);
        categoryAmount.put(name, count);

        return categoryAmount;
    }

    public static Map<String, List<WorkOrder>> parseWorkOrderSearchResult(JSONObject jsonObject) {
        Map<String, List<WorkOrder>> workOrderMap = new HashMap<String, List<WorkOrder>>();
        List<WorkOrder> workOrderCategories = new ArrayList<WorkOrder>();
        Map<String, JSONArray> groupMap = new HashMap<String, JSONArray>();
        List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
        workOrders = parseWorkOrders(jsonObject);
        groupMap = parseSearchCategory(jsonObject);
        Iterator<String> keies = groupMap.keySet().iterator();
        while (keies.hasNext()) {
            String keyStr = keies.next();
            JSONArray currentIds = groupMap.get(keyStr);
            if (currentIds.length() != 0) {
                for (WorkOrder workOrder : workOrders) {
                    for (int j = 0; j < currentIds.length(); j++) {
                        if (StringUtil.isEquals(workOrder.getAttribute(WorkOrder.KEY_CODE), currentIds.optString(j).toString())) {
                            workOrderCategories.add(workOrder);
                        }
                    }
                }
                workOrderMap.put(keyStr, workOrderCategories);
                workOrderCategories = new ArrayList<WorkOrder>();
            } else {
                List<WorkOrder> cateory = new ArrayList<WorkOrder>();
                workOrderMap.put(keyStr, cateory);
            }
        }

        return workOrderMap;
    }

    private static Map<String, JSONArray> parseSearchCategory(JSONObject jsonObject) {
        Map<String, JSONArray> categorys = new HashMap<String, JSONArray>();
        JSONArray fieldAliases = jsonObject.optJSONArray("group");
        for (int i = 0; i < fieldAliases.length(); i++) {
            JSONObject jsn = fieldAliases.optJSONObject(i);
            String alias = jsn.optString("alias");
            JSONArray ids = jsn.optJSONArray("ids");
            categorys.put(alias, ids);
        }
        return categorys;
    }

    /**
     * 解析汇总的详情列表信息
     *
     * @param jsonObj
     * @return
     */
    public static ArrayList<SummaryDetailModel> parseSummaryDetails(JSONObject jsonObj, WorkOrderPieStaticsData pieData) {
        ArrayList<SummaryDetailModel> summaryDetails = new ArrayList<SummaryDetailModel>();
        String[] names = {"已完成", "未完成", "超期"};
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                SummaryDetailModel detailModel = new SummaryDetailModel();
                JSONObject summaryItem = jsonArray.optJSONObject(i);
                detailModel.setTotal(JSONUtils.getString(summaryItem, "total", ""));
                String date = JSONUtils.getString(summaryItem, "date", "");
                if (StringUtil.isBlank(date)) {
                    return summaryDetails;
                }
                String dateStr = pieData.isYearType() ? date.split("-")[1] : date.split("-")[2];
                detailModel.setDate(dateStr);
                detailModel.setDateStr(date);
                JSONArray datas = summaryItem.optJSONArray("data");
                for (int j = 0; j < datas.length(); j++) {
                    JSONObject details = datas.optJSONObject(j);
                    if (details.toString().contains(names[0])) {
                        detailModel.setFinishedAmount(JSONUtils.getString(details, names[0], ""));
                    } else if (details.toString().contains(names[1])) {
                        detailModel.setDealingAmount(JSONUtils.getString(details, names[1], ""));
                    } else if (details.toString().contains(names[2])) {
                        detailModel.setOverSceduleAmount(JSONUtils.getString(details, names[2], ""));
                    }
                }
                summaryDetails.add(detailModel);
            }
        }
        //降序排序
        ListUtil.sort(summaryDetails, false);

        return summaryDetails;
    }

    /**
     * 解析工单 工单2.0解析方法
     *
     * @param jsonObj
     * @return
     */
    public static List<WorkOrder> parseWorkOrders(JSONObject jsonObj) {
        List<WorkOrder> workOrders;
        List<WorkOrderMTField> WorkOrderMetas = null;// 用于解析工单属性数据

        JSONArray fieldAliases = jsonObj.optJSONArray("fieldAliases");
        if (fieldAliases.length() != 0) {
        }

        WorkOrderMetas = parseWorkOrderMetas2(jsonObj);
        String total = jsonObj.optString("total");
        if (total.equals("0")) {
            return new ArrayList<WorkOrder>(1);
        } else {
            workOrders = new ArrayList<WorkOrder>(Integer.valueOf(total));
        }
        int featuresLength = 0;
        JSONArray jsonArrFeatures = jsonObj.optJSONArray("features");
        if (jsonArrFeatures != null) {
            featuresLength = jsonArrFeatures.length();// 工单数量
        }
        if (featuresLength != 0) {
            for (int i = 0; i < featuresLength; i++) {
                JSONObject currentFeature = jsonArrFeatures.optJSONObject(i);
                WorkOrder workOrder = new WorkOrder();
                workOrder.setGeomString(currentFeature.optString("geometry"));
                JSONObject attJsonObj = currentFeature.optJSONObject("attributes");
                Map<String, String> attributes = new LinkedHashMap<String, String>();
                if (attJsonObj != null && SessionManager.workOrderMetasHaveParsed == true) {
                    for (WorkOrderMTField workOrderMTField : WorkOrderMetas) {
                        String name = workOrderMTField.name;
                        if (attJsonObj.has(name)) {
                            attributes.put(name, attJsonObj.optString(name));
                        }
                    }
                } else {
                    ToastUtil.showShort("解析工单数据失败，请联系管理员确认元数据是否配置");
                }
                if (attJsonObj.has(WorkOrder.KEY_FROM_ALIAS)) {
                    attributes.put(WorkOrder.KEY_FROM_ALIAS, attJsonObj.optString(WorkOrder.KEY_FROM_ALIAS));
                }
                workOrder.setAttributes(attributes);
                workOrders.add(workOrder);
            }
        }
        return workOrders;
    }

    public static WorkOrder parseSingleWorkOrder(JSONObject workOrderJson) {
        if (!SessionManager.workOrderMetasHaveParsed) {
            ToastUtil.showShort("解析工单数据失败，请先到管网维修功能查看工单，初始化工单元数据。");
            return null;
        }

        JSONObject attJsonObj = workOrderJson.optJSONObject("attributes");
        if (attJsonObj == null) {
            return null;
        }

        Map<String, String> attributes = new LinkedHashMap<String, String>();
        Set<String> keys = SessionManager.workOrderMetas.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            attributes.put(key, attJsonObj.optString(key));
        }
        attributes.put(WorkOrder.KEY_FROM_ALIAS, attJsonObj.optString(WorkOrder.KEY_FROM_ALIAS));

        WorkOrder workOrder = new WorkOrder();
        workOrder.setGeomString(workOrderJson.optString("geometry"));
        workOrder.setAttributes(attributes);

        return workOrder;
    }

    /**
     * 解析并排序工单元数据（新）
     *
     * @param jsonObj
     * @return
     */
    public static List<WorkOrderMTField> parseWorkOrderMetas2(JSONObject jsonObj) {
        List<WorkOrderMTField> WorkOrderMetas = null;// 现在是无序的

        JSONArray jsonArrFields = jsonObj.optJSONArray("fields");
        int fieldsLength = jsonArrFields.length();
        if (fieldsLength != 0 && SessionManager.workOrderMetasHaveParsed == false) {// 工单元数据只用解析一遍
            SessionManager.workOrderMetas = new LinkedHashMap<String, WorkOrderMTField>((int) (fieldsLength / 0.75) + 1);// HashMap内部达到容量的0.75便会扩容
            WorkOrderMetas = new ArrayList<WorkOrderMTField>(fieldsLength);
            for (int i = 0; i < fieldsLength; i++) {
                WorkOrderMTField field = new WorkOrderMTField();
                JSONObject currentField = jsonArrFields.optJSONObject(i);
                field.name = currentField.optString("name");
                field.type = currentField.optString("type");
                field.alias = currentField.optString("alias");
                field.findex = currentField.optInt("findex");
                field.visible = currentField.optBoolean("visible");
                WorkOrderMetas.add(field);
                // 这里以后要保存到本地
                JSONArray jsonArrValues = currentField.optJSONArray("values");
                if (jsonArrValues == null) {
                    continue;
                }
                int valuesLength = jsonArrValues.length();
                field.values = new HashMap<String, String>((int) (valuesLength / 0.75) + 1);
                for (int j = 0; j < valuesLength; j++) {
                    JSONObject temp = jsonArrValues.optJSONObject(j);
                    String name = temp.optString("name");
                    String alias = temp.optString("alias");
                    field.values.put(name, alias);
                }
            }
            // 根据findex升序排序
            Collections.sort(WorkOrderMetas, new AttributeKeysComparator());
            for (WorkOrderMTField workOrderMTField : WorkOrderMetas) {
                SessionManager.workOrderMetas.put(workOrderMTField.name, workOrderMTField);
            }
            SessionManager.workOrderMetasHaveParsed = true;
            // 解析完元数据，用元数据别名替换工单按钮
            WorkOrderUtil.replaceAllBtnAlias();
        } else {
            if (SessionManager.workOrderMetas == null) {
                return null;
            }
            Set<String> keySets = SessionManager.workOrderMetas.keySet();
            WorkOrderMetas = new ArrayList<WorkOrderMTField>(keySets.size());
            for (String key : keySets) {
                WorkOrderMetas.add(SessionManager.workOrderMetas.get(key));
            }
        }
        return WorkOrderMetas;
    }

    public static ArrayList<ImageModel> parseImageModels(JSONObject jsonObj) {
        ArrayList<ImageModel> imageModels = new ArrayList<ImageModel>();
        try {
            if (null != jsonObj) {
                JSONArray jsonArray = jsonObj.getJSONArray("attachmentInfos");
                if (null != jsonArray) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject attcachment = jsonArray.optJSONObject(i);
                        ImageModel imgageModel = new ImageModel();
                        String addDate = JSONUtils.getString(attcachment, "adddate", "");
                        String remark = JSONUtils.getString(attcachment, "remark", "");
                        String filePath = JSONUtils.getString(attcachment, "virurl", "");
                        imgageModel.addDate = addDate;
                        imgageModel.remark = remark;
                        imgageModel.filePath = filePath;
                        imageModels.add(imgageModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageModels;
    }

    public static Map<String, String> parseWatchResult(JSONObject json) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("isWatch", json.optString("isWatch"));
        result.put("canSign", json.optString("canSign"));
        result.put("signOutTime", json.optString("signOutTime"));
        return result;
    }

    public static SignInStateBean parseWatchStateResult(JSONObject json) {
        SignInStateBean signInResult = new SignInStateBean();
        signInResult.setHasSignIn(json.optString("hasSignIn").equals("true"));
        signInResult.setMsg(json.optString("msg"));
        return signInResult;
    }

    public static ArrayList<InspectItem> parseInspectItems(JSONObject jsonObj) {
        ArrayList<InspectItem> inspectItems = new ArrayList<InspectItem>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("params");
            if (null != jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject inspectJson = jsonArray.optJSONObject(i);
                    boolean required = JSONUtils.getBoolean(inspectJson, "required", false);
                    boolean visible = JSONUtils.getBoolean(inspectJson, "visible", false);
                    String alias = JSONUtils.getString(inspectJson, "alias", "");
                    String type = JSONUtils.getString(inspectJson, "type", "");
                    String name = JSONUtils.getString(inspectJson, "name", "");
                    String value = JSONUtils.getString(inspectJson, "value", "");
                    String defaultValue = JSONUtils.getString(inspectJson, "defaultValue", "");
                    String selectValues = inspectJson.optJSONArray("selectValues").toString();
                    EInspectItemType enum_type = EInspectItemType.getTypeByValue(type);
                    InspectItem inspectItem = new InspectItem(visible, required, enum_type, name, alias, value, defaultValue, selectValues);
                    inspectItems.add(inspectItem);
                }
            }
        }
        return inspectItems;
    }

    /**
     * 解析请求的数据，返回带有两个key的Map，status存储事件的几种状态及其对应的状态码；events存储所有的事件。
     *
     * @param jsonObj
     * @param eventType 事件类型
     * @return Map<String ,   Object>
     */
    public static Map<String, Object> parseEvents(JSONObject jsonObj, EventType eventType) {
        List<Event> events = new ArrayList<Event>();
        Map<String, Object> eventsAndStatus = new HashMap<String, Object>();

        if (jsonObj == null) {
            return eventsAndStatus;
        }

        JSONArray jsonArrayFields = jsonObj.optJSONArray("fields");
        Map<String, String> eventState = new HashMap<String, String>();
        for (int i = 0; i < jsonArrayFields.length(); i++) {
            JSONObject json = jsonArrayFields.optJSONObject(i);
            if (("chulizhuangtai").equalsIgnoreCase(json.optString("name"))) {
                JSONArray jsonArraySelectValue = json.optJSONArray("selectValues");
                for (int j = 0; j < jsonArraySelectValue.length(); j++) {
                    JSONObject temp = jsonArraySelectValue.optJSONObject(j);
                    eventState.put(temp.optString("name"), temp.optString("alias"));
                }
            }
        }
        eventsAndStatus.put("status", eventState);

        JSONArray jsonArrayFeatures = jsonObj.optJSONArray("features");
        for (int i = 0; i < jsonArrayFeatures.length(); i++) {
            Event event = null;
            JSONObject json = jsonArrayFeatures.optJSONObject(i);
            Map<String, String> workOrderData = json2Map(json);
            if (eventType == EventType.REPAIRE) {
                event = new RepairementEvent(workOrderData);
            } else if (eventType == EventType.POINT_LEAK) {
                event = new PointLeakageEvent(workOrderData);
            } else if (eventType == EventType.PUNISHMENT) {
                event = new PunishmentEvent(workOrderData);
            } else if (eventType == EventType.CONSTRUCTION) {
                event = new ConstructionEvent(workOrderData);
            } else if (eventType == EventType.PUMP) {
                event = new PumpEvent(workOrderData);
            } else if (eventType == EventType.MEASURE) {
                event = new MeasureEvent(workOrderData);
            } else if (eventType == EventType.PATROL) {
                event = new PatrolEvent(workOrderData);
            } else if (eventType == EventType.GIS) {
                event = new GISReportEvent(workOrderData);
            }

            events.add(event);
        }

        eventsAndStatus.put("events", events);

        return eventsAndStatus;
    }

    public static boolean parseCloseEvent(JSONObject jsonObj, String eventId) {
        boolean closeSuccessed = false;
        if (jsonObj == null) {
            return false;
        }
        if (jsonObj.has("isSuccess")) {
            closeSuccessed = jsonObj.optBoolean("isSuccess");
        }
        return closeSuccessed;
    }

    //工单详情－流程信息
    public static ArrayList<FlowInfoBean> parseFlowInfoList(JSONObject jsonObj) {
        ArrayList<FlowInfoBean> flowInfoListItems = new ArrayList<FlowInfoBean>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject flowItem = jsonArray.optJSONObject(i);
                String type = flowItem.optString("type");
                String state = flowItem.optString("state");
                String addman = flowItem.optString("addman");
                String addtime = dateFormateUtil(flowItem.optString("addtime"));
                String processinstanceid = flowItem.optString("processinstanceid");
                FlowInfoBean flowInfo = new FlowInfoBean(addtime, addman, type, state, processinstanceid);
                flowInfoListItems.add(flowInfo);
            }
        }
        return flowInfoListItems;
    }

    public static ArrayList<WorkOrderOperationLogBean> parseLogList(JSONObject jsonObj) {
        ArrayList<WorkOrderOperationLogBean> logListItems = new ArrayList<WorkOrderOperationLogBean>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject logItem = jsonArray.optJSONObject(i);
                String logNodeId = logItem.optString("nodeid", "");
                String logProcesstime = logItem.optString("processtime", "");
                String logUsername = logItem.optString("username", "");
                String logDescription = logItem.optString("description", "");
                String logDealusername = logItem.optString("dealusername", "");
                WorkOrderOperationLogBean logBean = new WorkOrderOperationLogBean(logNodeId, logUsername, logProcesstime, logDescription, logDealusername);
                logListItems.add(logBean);
            }
        }
        //降序排序
        ListUtil.sort(logListItems, false);
        return logListItems;
    }

    public static String dateFormateUtil(String string) {
        String result = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            result = format.format(format.parse(string));
        } catch (ParseException e) {
            LogUtil.e("JsonUtil_DateFormate_error", e.toString());
        }
        return result;
    }

    public static String parsePunishStateChange(JSONObject jsonObject) {
        String result = null;
        try {
            result = jsonObject.getString("isSuccess");
        } catch (JSONException e) {
            LogUtil.e("JsonUtilPunishStateChange", e);
        }
        return result;
    }

    public static Map<String, List<String>> parseEventTypeList(JSONObject jsonObject) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        JSONArray eventMenu = jsonObject.optJSONArray("eventmenu");
        if (eventMenu != null) {
            List<String> eventType = new ArrayList<String>();
            List<String> eventName = new ArrayList<String>();
            for (int i = 0; i < eventMenu.length(); i++) {
                JSONObject event = eventMenu.optJSONObject(i);
                if (event != null) {
                    eventType.add(event.optString("eventtype"));
                    eventName.add(event.optString("eventname"));
                }
            }
            result.put("eventtype", eventType);
            result.put("eventname", eventName);
        }
        return result;
    }

    public static List<PatrolUser> parseAllPatroMan(JSONObject jsonObj) {
        List<PatrolUser> patrolMen = new ArrayList<PatrolUser>();
        JSONArray jsonArray = jsonObj.optJSONArray("items");
        if (null == jsonArray) {
            return patrolMen;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            PatrolUser user = new PatrolUser();
            user.setId(jsonObject.optString("userid"));
            user.setTrueName(jsonObject.optString("truename"));
            user.setState(jsonObject.optString("state"));
            user.setType(jsonObject.optString("type"));
            user.setGroupId(jsonObject.optString("groupid"));
            user.setGroupName(jsonObject.optString("groupname"));
            user.setPatroltime(jsonObject.optString("patroltime"));
            if (isNum(jsonObject.optString("lon"))) {
                user.setLongitude(Double.valueOf(jsonObject.optString("lon")));
            } else {
                user.setLongitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("lat"))) {
                user.setLatitude(Double.valueOf(jsonObject.optString("lat")));
            } else {
                user.setLatitude(Double.NaN);
            }
            user.setSpeed(jsonObject.optString("speed"));
            user.setAvrspeed(jsonObject.optString("avrspeed"));
            patrolMen.add(user);
        }
        return patrolMen;
    }

    public static List<PatrolBusInfo> parsePatrolBusInfo(JSONObject jsonObj) {
        List<PatrolBusInfo> patrolBusInfos = new ArrayList<>();
        JSONArray jsonArray = jsonObj.optJSONArray("rows");
        if (null == jsonArray) {
            return patrolBusInfos;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            PatrolBusInfo patrolBusInfo = new PatrolBusInfo();
            patrolBusInfo.setGid(jsonObject.optString("gid"));
            patrolBusInfo.setUserid(jsonObject.optString("userid"));
            patrolBusInfo.setBusno(jsonObject.optString("busno"));
            patrolBusInfo.setUsername(jsonObject.optString("username"));
            patrolBusInfo.setGroupname(jsonObject.optString("groupname"));
            if (jsonObject.has("device_info") && jsonObject.optString("device_info").equals("0")) {
                patrolBusInfo.setDeviceInfo("1");
            } else {
                patrolBusInfo.setDeviceInfo("0");
            }
            if (isNum(jsonObject.optString("lng"))) {
                patrolBusInfo.setLng(Double.valueOf(jsonObject.optString("lng")));
            } else {
                patrolBusInfo.setLng(Double.NaN);
            }
            if (isNum(jsonObject.optString("lat"))) {
                patrolBusInfo.setLat(Double.valueOf(jsonObject.optString("lat")));
            } else {
                patrolBusInfo.setLat(Double.NaN);
            }
            patrolBusInfos.add(patrolBusInfo);
        }
        return patrolBusInfos;
    }

    public static boolean isNum(String str) {
        return StringUtil.isNum(str) && !("".equals(str));
    }

    public static ArrayList<ProjectLogBean> parseProjectLogList(JSONObject jsonObj) {
        ArrayList<ProjectLogBean> logListItems = new ArrayList<ProjectLogBean>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject logItem = jsonArray.optJSONObject(i);
                String logNodeId = JSONUtils.getString(logItem, "type", "");
                String logProcesstime = JSONUtils.getString(logItem, "createtime", "");
                String logUsername = JSONUtils.getString(logItem, "username", "");
                String logDescription = JSONUtils.getString(logItem, "remark", "");
                ProjectLogBean logBean = new ProjectLogBean(logNodeId, logUsername, logProcesstime, logDescription);
                logListItems.add(logBean);
            }
        }
        //降序排序
        ListUtil.sort(logListItems, false);
        return logListItems;
    }

    /**
     * "id": "u975",
     * "value": "975",
     * "text": "蒋永江",
     * "code": "1-1-6-13",
     * "showcheck": true,
     * "checkstate": 1,
     * "isexpand": false,
     * "hasChildren": false,
     * "complete": true
     *
     * @author jonathanma
     */
    public static OrganisationSelection parseOrganisationSelection(JSONObject json) {
        OrganisationSelection organisation = new OrganisationSelection();
        if (json.has("value")) {
            organisation.setId(json.optInt("value"));
        } else if (json.has("id")) {
            String idValue = json.optString("id");
            if (idValue.startsWith("id")) {
                String idStr = idValue.substring(2);
                organisation.setId(Integer.valueOf(idStr));
            } else if (idValue.startsWith("d") || idValue.startsWith("u")) {
                String idStr = idValue.substring(1);
                organisation.setId(Integer.valueOf(idStr));
            }
        }
        organisation.setName(json.optString("text"));
        organisation.setCode(json.optString("code"));
        organisation.setUser(json.optString("id").startsWith("u"));
        organisation.setPhone(json.optString("phone"));
        organisation.setEmail(json.optString("email"));
        organisation.setAvatar(json.optString("avatar"));

        JSONArray childrenJson = json.optJSONArray("ChildNodes");
        if (childrenJson == null) {
            childrenJson = json.optJSONArray("children");
        }
        if ((childrenJson != null) && (childrenJson.length() > 0)) {
            parseOrganisationSelectionChildren(organisation, childrenJson);
        }

        return organisation;
    }

    private static void parseOrganisationSelectionChildren(OrganisationSelection parent, JSONArray childrenJson) {
        for (int i = 0; i < childrenJson.length(); i++) {
            JSONObject childJson = childrenJson.optJSONObject(i);
            OrganisationSelection child = parseOrganisationSelection(childJson);
            parent.addChild(child);
        }
    }

    public static Object parsePlanAndPipeAddress(JSONObject jsonObj) {
        Map<String, Object> addresses = new HashMap<String, Object>();
        List<AddressInfoModel> planAddrInfos = new ArrayList<AddressInfoModel>();
        List<AddressInfoModel> PipeaddrInfos = new ArrayList<AddressInfoModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("results");
        if (null == jsonArray) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            AddressInfoModel infoModel = new AddressInfoModel();
            String type = jsonObject.optString("type");
            infoModel.setType(type);

            JSONObject json_1 = jsonObject.optJSONObject("geometry");
            infoModel.setX(json_1.optDouble("x"));
            infoModel.setY(json_1.optDouble("y"));

            JSONObject json_2 = jsonObject.optJSONObject("attributes");
            if (ResourceUtil.getStringById(R.string.map_plan_address).equals(type)) {
                infoModel.setName(json_2.optString("PNAME"));
                planAddrInfos.add(infoModel);
            } else if (ResourceUtil.getStringById(R.string.map_pipe_address).equals(type)) {
                infoModel.setName(json_2.optString(ResourceUtil.getStringById(R.string.map_pipe_address_name)));
                infoModel.setAddress(json_2.optString(ResourceUtil.getStringById(R.string.map_pipe_address_road)));
                PipeaddrInfos.add(infoModel);
            }
        }
        addresses.put(ResourceUtil.getStringById(R.string.map_plan_address), planAddrInfos);
        addresses.put(ResourceUtil.getStringById(R.string.map_pipe_address), PipeaddrInfos);
        return addresses;
    }

    /**
     * 工单饼状图汇总
     *
     * @param jsonObj
     * @return
     */
    public static WorkOrderSummaryPie parseWorkOrderSummaryPieStatics(JSONObject jsonObj) {
        WorkOrderSummaryPie pie = new WorkOrderSummaryPie();
        List<WorkOrderSummaryPieBean> beanList = new ArrayList<WorkOrderSummaryPieBean>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("items");
            JSONObject flowItem = jsonArray.optJSONObject(0);
            JSONArray items = flowItem.optJSONArray("data");
            String total = flowItem.optString("total");
            pie.setTotal(total);
            for (int i = 0; i < items.length(); i++) {
                WorkOrderSummaryPieBean pieBean = new WorkOrderSummaryPieBean();
                pieBean.setPieCategory(items.optJSONObject(i).optString("name"));
                pieBean.setPieData(items.optJSONObject(i).optString("value"));
                beanList.add(pieBean);
            }
            pie.setPieBean(beanList);
        }
        return pie;
    }

    /**
     * 工单条形图汇总
     *
     * @param jsonObj
     * @return
     */
    public static List<WorkOrderSummaryBar> parseWorkOrderSummaryBarStatics(JSONObject jsonObj) {
        List<WorkOrderSummaryBar> barDatas = new ArrayList<WorkOrderSummaryBar>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("items");
            JSONObject flowItem = jsonArray.optJSONObject(0);
            JSONArray items = flowItem.optJSONArray("data");
            for (int i = 0; i < items.length(); i++) {
                JSONObject beanItem = items.optJSONObject(i);
                WorkOrderSummaryBarBean bean = new WorkOrderSummaryBarBean();
                WorkOrderSummaryBar summaryBar = new WorkOrderSummaryBar();
                bean.setToDealAmount(beanItem.optString("wjdNum"));
                bean.setDealingAmount(beanItem.optString("clzNum"));
                bean.setFinishedAmount(beanItem.optString("ywcNum"));
                bean.setPostponeAmount(beanItem.optString("yqNum"));
                bean.setOvertimeAmount(beanItem.optString("cqNum"));
                summaryBar.setPersonName(beanItem.optString("userName"));
                summaryBar.setBarDatas(bean);
                barDatas.add(summaryBar);
            }
        }
        return barDatas;
    }

    /**
     * 工单汇总(班长)姐希
     *
     * @param jsonObj
     * @return
     */
    public static List<WorkorderLeaderModel> parseWorkOrderLeader(JSONObject jsonObj) {
        List<WorkorderLeaderModel> barDatas = new ArrayList<WorkorderLeaderModel>();
        if (null != jsonObj) {
            JSONArray items = jsonObj.optJSONArray("resultist");
            for (int i = 0; i < items.length(); i++) {
                JSONObject beanItem = items.optJSONObject(i);
                WorkorderLeaderModel model = new WorkorderLeaderModel();
                model.setWxry(beanItem.optString("userName"));
                model.setZds(beanItem.optString("total"));
                model.setJdd(beanItem.optString("djdNum"));
                model.setClz(beanItem.optString("clzNum"));
                model.setYwj(beanItem.optString("wjNum"));
                model.setYq(beanItem.optString("yqNum"));
                model.setCq(beanItem.optString("cqNum"));
                model.setZd(beanItem.optString("zdNum"));
                model.setTd(beanItem.optString("tdNum"));
                model.setGs(beanItem.optString("worktime"));
                barDatas.add(model);
            }
        }
        return barDatas;
    }

    public static List<UnFinishWorkOrderInfo> parseWorkOrderUnfinishedInfos(JSONObject jsonObj) {
        List<UnFinishWorkOrderInfo> infoList = new ArrayList<UnFinishWorkOrderInfo>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("resultist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                UnFinishWorkOrderInfo info = new UnFinishWorkOrderInfo();
                info.setPatrolName(object.optString("username"));
                info.setTodayWorkOrderNumber(String.valueOf(object.optInt("todayCnt")));
                info.setWaitProcessWorkOrderNumber(String.valueOf(object.optInt("dealCnt")));

                List<UnFinishWorkOrderAddressInfo> addressInfos = new ArrayList<UnFinishWorkOrderAddressInfo>();
                JSONArray jsonArrays = object.optJSONArray("address");
                for (int j = 0; j < jsonArrays.length(); j++) {
                    UnFinishWorkOrderAddressInfo addressInfo = new UnFinishWorkOrderAddressInfo();
                    JSONObject jsonObject = jsonArrays.optJSONObject(j);
                    boolean isHost = 0 != jsonObject.optInt("isHost");
                    addressInfo.setHost(isHost);
                    addressInfo.setAddresses(jsonObject.optString("address"));
                    addressInfos.add(addressInfo);
                }
                info.setAddressInfoList(addressInfos);
                infoList.add(info);
            }
        }
        return infoList;
    }

    public static List<PumpInsSelectValue> parsePumps(JSONObject jsonObj) {
        List<PumpInsSelectValue> pumps = new ArrayList<PumpInsSelectValue>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                PumpInsSelectValue pump = new PumpInsSelectValue();
                JSONObject attrObject = object.optJSONObject("attributes");
                pump.setGid(attrObject.optString("gid"));
                pump.setName(attrObject.optString(ResourceUtil.getStringById(R.string.event_pump_name)));
                pump.setAlias(attrObject.optString(ResourceUtil.getStringById(R.string.event_pump_name)));
                pump.setPumpRoad(attrObject.optString(ResourceUtil.getStringById(R.string.event_pump_address)));
                pump.setX(attrObject.optString(ResourceUtil.getStringById(R.string.event_pump_x)));
                pump.setY(attrObject.optString(ResourceUtil.getStringById(R.string.event_pump_y)));
                pump.setPumpNO(attrObject.optString(ResourceUtil.getStringById(R.string.event_pump_no)));
                pumps.add(pump);
            }
        }
        return pumps;
    }

    public static List<PumpRepairAndMaintainInfoModel> parsePumpRepairMaintainInfo(JSONObject jsonObject) {
        List<PumpRepairAndMaintainInfoModel> pumpInfos = new ArrayList<PumpRepairAndMaintainInfoModel>();
        if (null != jsonObject) {
            JSONArray jsonArray = jsonObject.optJSONArray("resultist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                PumpRepairAndMaintainInfoModel pumpInfo = new PumpRepairAndMaintainInfoModel();
                pumpInfo.setBusinessKey(object.optString("businesskey"));
                pumpInfo.setMaintainContent(object.optString("content"));
                pumpInfo.setMaintainTime(object.optString("time"));
                pumpInfo.setMaintainType(object.optString("type"));
                pumpInfo.setMaintainUserName(object.optString("username"));
                pumpInfos.add(pumpInfo);
            }
        }
        return pumpInfos;
    }

    //------------------------------------------------------------------------
    //------------------------------------------------------------------------

    /**
     * 新的服务下，工单解析
     *
     * @param jsonObj
     * @return
     */
    public static List<WorkOrder> parseWorkOrders1(JSONObject jsonObj) {
        List<WorkOrder> workOrders = new ArrayList<>();
        List<WorkOrderMTField> WorkOrderMetas = parseWorkOrderMetas(jsonObj); // 用于解析工单属性数据

        String total = jsonObj.optString("total");
        if (total.equals("0")) {
            return new ArrayList<WorkOrder>(1);
        } else {
            workOrders = new ArrayList<WorkOrder>(Integer.valueOf(total));
        }

        JSONArray jsonArrFeatures = jsonObj.optJSONArray("features");
        int featuresLength = jsonArrFeatures.length();
        if (jsonArrFeatures == null) {
            return workOrders;
        }
        if (0 == featuresLength) {
            return workOrders;
        }

        List<WorkOrderBtnModel> workOrderBtns = new ArrayList<>();
        for (int i = 0; i < featuresLength; i++) {
            JSONObject currentFeature = jsonArrFeatures.optJSONObject(i);
            WorkOrder workOrder = new WorkOrder();
            workOrder.setGeomString(currentFeature.optString("geometry"));
            JSONObject attJsonObj = currentFeature.optJSONObject("attributes");
            Map<String, String> attributes = new LinkedHashMap<String, String>();
            if (attJsonObj != null && SessionManager.workOrderMetasHaveParsed == true) {
                for (WorkOrderMTField workOrderMTField : WorkOrderMetas) {
                    String name = workOrderMTField.name;
                    if (attJsonObj.has(name)) {
                        attributes.put(name, attJsonObj.optString(name));
                    }
                }
            } else {
                ToastUtil.showShort("解析工单数据失败，请联系管理员确认元数据是否配置");
            }
            workOrderBtns = parseWorkOrderBtns(attJsonObj);

            if (attJsonObj.has(WorkOrder.KEY_FROM_ALIAS)) {
                attributes.put(WorkOrder.KEY_FROM_ALIAS, attJsonObj.optString(WorkOrder.KEY_FROM_ALIAS));
            }

            workOrder.setAttributes(attributes);
            workOrder.setWorkOrderBtns(workOrderBtns);
            workOrders.add(workOrder);
        }

        return workOrders;
    }

    /***
     * 新的服务下，解析工单列表的所有buttons
     * @param attJsonObj
     * @return
     */
    private static List<WorkOrderBtnModel> parseWorkOrderBtns(JSONObject attJsonObj) {
        List<WorkOrderBtnModel> workOrderBtns = new ArrayList<>();
        if (null == attJsonObj) {
            return workOrderBtns;
        }
        JSONArray btnJSONArray = attJsonObj.optJSONArray(WorkOrder.KEY_OPERATE_BTNS);
        if (null == btnJSONArray) {
            return workOrderBtns;
        }

        for (int i = 0; i < btnJSONArray.length(); i++) {
            WorkOrderBtnModel workOrderBtn = new WorkOrderBtnModel();
            JSONObject object = btnJSONArray.optJSONObject(i);
            workOrderBtn.setTaskName(object.optString("taskName"));
            if (!object.has("children")) {
                workOrderBtn.setTaskId(object.optString("taskId"));
                workOrderBtn.setTaskName(object.optString("taskName"));
                workOrderBtn.setTaskCode(object.optString("taskCode"));
                workOrderBtn.setTaskType(object.optString("taskType"));
                workOrderBtn.setIndex(object.optInt("index"));
                workOrderBtn.setFlowType(object.optInt("flowType"));
            }

            JSONArray subBtnJSONArray = object.optJSONArray("children");
            if (null != subBtnJSONArray) {
                List<WorkOrderBtnModel> subWorkOrderBtns = new ArrayList<>();
                for (int j = 0; j < subBtnJSONArray.length(); j++) {
                    JSONObject subObject = subBtnJSONArray.optJSONObject(j);

                    WorkOrderBtnModel subWorkOrderBtn = new WorkOrderBtnModel();
                    subWorkOrderBtn.setTaskId(subObject.optString("taskId"));
                    subWorkOrderBtn.setTaskName(subObject.optString("taskName"));
                    subWorkOrderBtn.setTaskCode(subObject.optString("taskCode"));
                    subWorkOrderBtn.setTaskType(subObject.optString("taskType"));
                    subWorkOrderBtn.setIndex(subObject.optInt("index"));
                    subWorkOrderBtn.setFlowType(subObject.optInt("flowType"));

                    subWorkOrderBtns.add(subWorkOrderBtn);
                }
                workOrderBtn.setSubWorkOrderBtns(subWorkOrderBtns);
            }
            workOrderBtns.add(workOrderBtn);
        }
        return workOrderBtns;
    }

    /**
     * 新的服务下，解析并排序工单元数据
     *
     * @param jsonObj
     * @return
     */
    private static List<WorkOrderMTField> parseWorkOrderMetas(JSONObject jsonObj) {
        List<WorkOrderMTField> WorkOrderMetas = null;// 现在是无序的

        JSONArray jsonArrFields = jsonObj.optJSONArray("fields");
        int fieldsLength = jsonArrFields.length();
        if (fieldsLength != 0 && SessionManager.workOrderMetasHaveParsed == false) {// 工单元数据只用解析一遍
            SessionManager.workOrderMetas = new LinkedHashMap<String, WorkOrderMTField>((int) (fieldsLength / 0.75) + 1);// HashMap内部达到容量的0.75便会扩容
            WorkOrderMetas = new ArrayList<WorkOrderMTField>(fieldsLength);
            for (int i = 0; i < fieldsLength; i++) {
                WorkOrderMTField field = new WorkOrderMTField();
                JSONObject currentField = jsonArrFields.optJSONObject(i);
                field.name = currentField.optString("name");
                field.type = currentField.optString("type");
                field.alias = currentField.optString("alias");
                field.findex = currentField.optInt("findex");
                field.visible = currentField.optBoolean("visible");
                WorkOrderMetas.add(field);
                // 这里以后要保存到本地
                JSONArray jsonArrValues = currentField.optJSONArray("values");
                if (jsonArrValues == null) {
                    continue;
                }
                int valuesLength = jsonArrValues.length();
                field.values = new HashMap<String, String>((int) (valuesLength / 0.75) + 1);
                for (int j = 0; j < valuesLength; j++) {
                    JSONObject temp = jsonArrValues.optJSONObject(j);
                    String name = temp.optString("name");
                    String alias = temp.optString("alias");
                    field.values.put(name, alias);
                }
            }
            // 根据findex升序排序
            Collections.sort(WorkOrderMetas, new AttributeKeysComparator());
            for (WorkOrderMTField workOrderMTField : WorkOrderMetas) {
                SessionManager.workOrderMetas.put(workOrderMTField.name, workOrderMTField);
            }
            SessionManager.workOrderMetasHaveParsed = true;
        } else {
            if (SessionManager.workOrderMetas == null) {
                return null;
            }
            Set<String> keySets = SessionManager.workOrderMetas.keySet();
            WorkOrderMetas = new ArrayList<WorkOrderMTField>(keySets.size());
            for (String key : keySets) {
                WorkOrderMetas.add(SessionManager.workOrderMetas.get(key));
            }
        }
        return WorkOrderMetas;
    }

    /**
     * 新的服务下，工单详情tab解析
     *
     * @param jsonObj
     * @return
     */
    public static List<WorkOrderDetailTabModel> parseWorkOrdersDetailTabs(JSONObject jsonObj) {
        List<WorkOrderDetailTabModel> tabs = new ArrayList<WorkOrderDetailTabModel>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("tab");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                WorkOrderDetailTabModel tab = new WorkOrderDetailTabModel();
                tab.setName(object.optString("alias"));
                tab.setAlias(object.optString("name"));
                tab.setGroupid(object.optString("groupid"));
                tab.setUrl(object.optString("url"));
                tabs.add(tab);
            }
        }
        return tabs;
    }

    /**
     * 工单流程信息解析
     *
     * @param jsonObj
     * @return
     */
    public static List<FlowInfoBean> parseWorkOrdersFlowInfos(JSONObject jsonObj) {
        List<FlowInfoBean> flowInfos = new ArrayList<FlowInfoBean>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject flowItem = jsonArray.optJSONObject(i);
                JSONObject attributes = flowItem.optJSONObject("attributes");
                String type = attributes.optString("type");
                String state = attributes.optString("state");
                String addman = attributes.optString("addman");
                String addtime = dateFormateUtil(attributes.optString("addtime"));
                String gid = attributes.optString("gid");
                FlowInfoBean flowInfo = new FlowInfoBean(addtime, addman, type, state, gid);
                flowInfos.add(flowInfo);
            }
        }
        return flowInfos;
    }

    /**
     * 工单流程详细信息解析
     *
     * @param jsonObj
     * @return
     */
    public static List<InspectItem> parseWorkOrdersFlowDetailInfos(JSONObject jsonObj) {
        List<InspectItem> flowInfoDetailItems = new ArrayList<>();
        Map<String, String> fieldMap = new HashMap<String, String>();
        JSONArray fieldArray = jsonObj.optJSONArray("fields");
        if (null == fieldArray) {
            return flowInfoDetailItems;
        }

        for (int i = 0; i < fieldArray.length(); i++) {
            JSONObject jsonObject = fieldArray.optJSONObject(i);
            String name = jsonObject.optString("name");
            String alias = jsonObject.optString("alias");
            fieldMap.put(name, alias);
        }

        JSONArray featuresArray = jsonObj.optJSONArray("features");
        if (null == featuresArray) {
            return flowInfoDetailItems;
        }

        Set<String> keySets = fieldMap.keySet();
        for (int i = 0; i < featuresArray.length(); i++) {
            JSONObject jsonObject = featuresArray.optJSONObject(i);
            JSONObject attrObject = jsonObject.optJSONObject("attributes");
            for (String key : keySets) {
                String value = attrObject.optString(key);
                String alias = fieldMap.get(key);
                InspectItem item = new InspectItem();
                item.setName(key);
                item.setAlias(alias);
                item.setVisible(true);
                item.setValue(value);
                item.setType(EInspectItemType.TEXT);
                flowInfoDetailItems.add(item);
            }
        }

        return flowInfoDetailItems;
    }

    /*****
     * 工单日志回溯信息解析
     * @param jsonObj
     * @return
     */
    public static ArrayList<WorkOrderOperationLogBean> parseLogList1(JSONObject jsonObj) {
        ArrayList<WorkOrderOperationLogBean> logListItems = new ArrayList<WorkOrderOperationLogBean>();
        if (null != jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject logItem = jsonArray.optJSONObject(i);
                String logNodeId = logItem.optString("nodeid", "");
                String logProcesstime = logItem.optString("processtime", "");
                String logUsername = logItem.optString("username", "");
                String logDescription = logItem.optString("description", "");
                String logDealusername = logItem.optString("dealusername", "");
                WorkOrderOperationLogBean logBean = new WorkOrderOperationLogBean(logNodeId, logUsername, logProcesstime, logDescription, logDealusername);
                logListItems.add(logBean);
            }
        }

        return logListItems;
    }

    /****
     * 新的服务下，解析查询到的工单结果
     * @param jsonObject
     * @return
     */
    public static Map<String, List<WorkOrder>> parseWorkOrderSearchResult1(JSONObject jsonObject) {
        Map<String, List<WorkOrder>> workOrderMap = new HashMap<String, List<WorkOrder>>();
        List<WorkOrder> workOrderCategories = new ArrayList<WorkOrder>();
        Map<String, JSONArray> groupMap = new HashMap<String, JSONArray>();
        List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
        workOrders = parseWorkOrders1(jsonObject);
        groupMap = parseSearchCategory(jsonObject);
        Iterator<String> keies = groupMap.keySet().iterator();
        while (keies.hasNext()) {
            String keyStr = keies.next();
            JSONArray currentIds = groupMap.get(keyStr);
            if (currentIds.length() != 0) {
                for (WorkOrder workOrder : workOrders) {
                    for (int j = 0; j < currentIds.length(); j++) {
                        if (StringUtil.isEquals(workOrder.getAttribute(WorkOrder.KEY_CODE), currentIds.optString(j).toString())) {
                            workOrderCategories.add(workOrder);
                        }
                    }
                }
                workOrderMap.put(keyStr, workOrderCategories);
                workOrderCategories = new ArrayList<WorkOrder>();
            } else {
                List<WorkOrder> cateory = new ArrayList<WorkOrder>();
                workOrderMap.put(keyStr, cateory);
            }
        }

        return workOrderMap;
    }
}
