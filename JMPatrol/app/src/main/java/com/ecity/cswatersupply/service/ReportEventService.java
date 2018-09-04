package com.ecity.cswatersupply.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.EventTypeParemeter;
import com.ecity.cswatersupply.network.request.GetAllPumpsParemeter;
import com.ecity.cswatersupply.network.request.GetCloseEventParameter;
import com.ecity.cswatersupply.network.request.GetEventListParameter;
import com.ecity.cswatersupply.network.request.GetEventReportParameter;
import com.ecity.cswatersupply.network.request.GetEventWorkOrderInfoParameter;
import com.ecity.cswatersupply.network.request.GetFZModifyFormParameter;
import com.ecity.cswatersupply.network.request.GetPumpMaintainInfoParameters;
import com.ecity.cswatersupply.network.request.GetPumpMaintainInspectItemParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderFieldParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderParameter;
import com.ecity.cswatersupply.network.request.PunishmentDetailsParameter;
import com.ecity.cswatersupply.network.request.UpdateValveStatesParameter;
import com.ecity.cswatersupply.network.request.ValveSwitchReportParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;

public class ReportEventService {
    private static ReportEventService instance;
    static {
        instance = new ReportEventService();
    }

    private ReportEventService() {

    }

    public static ReportEventService getInstance() {
        return instance;
    }

    public void getEventReportParams(final String key) {
        RequestExecutor.execute(new ARequestCallback<EventReportResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            protected boolean getParseByGson() {
                return false;
            }

            protected Object customParse(JSONObject jsonObj) {
                EventReportResponse response = new EventReportResponse();
                List<InspectItem> adaptItems = InspectItemAdapter.adaptEventReportItems(jsonObj);
                setReporterDefaultInfo(adaptItems);
                response.setInspectItems(adaptItems);
                response.setTableName(jsonObj.optString("tableName"));
                return response;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEventFormUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEventReportParameter(key);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_GET_PARAMS;
            }

            @Override
            public Class<EventReportResponse> getResponseClass() {
                return EventReportResponse.class;
            }
        });
    }

    //获得阀门开关表单
    public void getValveSwitchForm() {
        RequestExecutor.execute(new ARequestCallback<EventReportResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            protected boolean getParseByGson() {
                return false;
            }

            protected Object customParse(JSONObject jsonObj) {
                EventReportResponse response = new EventReportResponse();
                List<InspectItem> adaptItems = InspectItemAdapter.adaptEventReportItems(jsonObj);
                setReporterDefaultInfo(adaptItems);
                response.setInspectItems(adaptItems);
                response.setTableName(jsonObj.optString("tableName"));
                return response;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getValveSwitchFormUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new ValveSwitchReportParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_GET_VALVE_SWITCH;
            }

            @Override
            public Class<EventReportResponse> getResponseClass() {
                return EventReportResponse.class;
            }
        });
    }

    public void getEventList(final User user, final EventType eventType) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEventFormListUrl();
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseEvents(jsonObj, eventType);
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEventListParameter(user, eventType);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_GET_EVENT_LIST;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void closeEvent(final User user, final String EventId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getCloseEventUrl();
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseCloseEvent(jsonObj, EventId);
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetCloseEventParameter(user, EventId);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_CLOSE_EVENT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getPunishmentDetails(final String eventId, final String eventType) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEventInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new PunishmentDetailsParameter(eventId, eventType);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                EventReportResponse response = null;
                try {
                    response = new EventReportResponse();
                    String reString = jsonObj.toString().replace("GEOM", "TXT").replace("DDL", "TXT").replace("DATE", "TXT");
                    JSONObject newObject = new JSONObject(reString);
                    JSONArray items = newObject.optJSONArray("params").optJSONObject(0).getJSONArray("items");
                    List<InspectItem> adaptItems = InspectItemAdapter.adaptItems(items);
                    response.setInspectItems(adaptItems);
                } catch (JSONException e) {
                    LogUtil.e(this, e);
                }
                return response;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PUNISHMENT_DETAILS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getEventType(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEventListUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new EventTypeParemeter(user);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseEventTypeList(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_GET_TYPE_LIST;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getWorkOrderInfoWithEventId(final String eventId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getNetServerQueryUrlWithTableName("WORKORDER");
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEventWorkOrderInfoParameter(eventId);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_GET_WORK_ORDER_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getWorkOrderInfoWithPid(final String pid) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getNetServerQueryUrlWithTableName("WORKORDER");
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWorkOrderParameter(pid);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_REPORT_GET_WORK_ORDER_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getWorkOrderField() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderField();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWorkOrderFieldParameter();
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderMetas2(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_GET_WORK_ORDER_FIELDS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void queryAllPumps(final String url) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetAllPumpsParemeter();
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parsePumps(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_GET_ALL_PUMP_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getPumpRepairMaintainInfo(final String url, final String pumpGid) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_GET_PUMP_MAINTAIN_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPumpMaintainInfoParameters(pumpGid);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }
        });
    }

    public void getPumpMaintainInspectItemData(final String url, final String processInstanceId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_GET_PUMP_MAINTAIN_INSPECT_ITEM;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPumpMaintainInspectItemParameter(processInstanceId);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                EventReportResponse response = new EventReportResponse();
                response.setInspectItems(InspectItemAdapter.adaptItems(jsonObj));
                return response;
            }
        });
    }

    /**
     * 修改数据库中阀门的状态
     *
     */
    public void updateValveStates(final List<InspectItem> inspectItems) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().updateValveStatesUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new UpdateValveStatesParameter(inspectItems);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_UPDATE_VALVE_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    private void setReporterDefaultInfo(List<InspectItem> adaptItems) {
        for (int i = 0; i < adaptItems.size(); i++) {
            InspectItem itemTemp = adaptItems.get(i);

            if ("REPORTERID".equalsIgnoreCase(itemTemp.getName())) {
                adaptItems.get(i).setDefaultValue(HostApplication.getApplication().getCurrentUser().getId());
                continue;
            }
            if ("REPORTER".equalsIgnoreCase(itemTemp.getName())) {
                adaptItems.get(i).setDefaultValue(HostApplication.getApplication().getCurrentUser().getTrueName());
                continue;
            }
            if (ListUtil.isEmpty(itemTemp.getChilds())) {
                continue;
            } else {
                setReporterDefaultInfo(itemTemp.getChilds());
            }
        }
    }

    /**
     * 福州获取修改检查项上报的表单
     * @param groupid
     * @param pointid
     */
    public void getFZModifyReportForms(final String groupid, final String pointid) {
        RequestExecutor.execute(new ARequestCallback<EventReportResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getFormContent();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetFZModifyFormParameter(groupid, pointid);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EVENT_GET_FZ_MODIFY_INSPECTS;
            }

            @Override
            public Class<EventReportResponse> getResponseClass() {
                return EventReportResponse.class;
            }

            protected boolean getParseByGson() {
                return false;
            }

            protected Object customParse(JSONObject jsonObj) {
                EventReportResponse response = new EventReportResponse();
                List<InspectItem> adaptItems = InspectItemAdapter.adaptEventReportItems(jsonObj);
                setReporterDefaultInfo(adaptItems);
                response.setInspectItems(adaptItems);
                response.setTableName(jsonObj.optString("tableName"));
                return response;
            }
        });
    }
}
