package com.ecity.cswatersupply.emergency.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.EmergencyJsonUtil;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.network.request.GetEQMessageParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEQRefugeParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEQRelateParameter;
import com.ecity.cswatersupply.emergency.network.request.GetNoticeContentParameter;
import com.ecity.cswatersupply.emergency.network.request.GetNoticeListParameter;
import com.ecity.cswatersupply.emergency.network.request.GetSearchFormParameter;
import com.ecity.cswatersupply.emergency.network.request.GetUnUsualReportFormParameter;
import com.ecity.cswatersupply.emergency.network.request.UpdateManStateParameter;
import com.ecity.cswatersupply.emergency.network.response.GetNoticeListResponse;
import com.ecity.cswatersupply.emergency.test.Textwriter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.FileUtil;

/***
 * 应急相关网络请求入口，如获取地震信息，台站信息
 * @author ZiZhengzhuan
 *
 */
public class EmergencyService {
    private static EmergencyService instance;

    static {
        instance = new EmergencyService();
    }

    private EmergencyService() {

    }

    public static EmergencyService getInstance() {
        return instance;
    }

    public void getEarthQuackeList(final IRequestParameter parameter, final int requestId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEarthQuickInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseEarthQuakeInfoModels(jsonObj);
            }

            @Override
            public int getEventId() {
                return requestId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getAllEQStationList(final IRequestParameter parameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEQStationStationUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                try {
                    if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                        return EmergencyJsonUtil.parseEQMonitorStationModelsForCZ(jsonObj);
                    } else {
                        return EmergencyJsonUtil.parseEQMonitorStationModels(jsonObj);
                    }
                } catch (Exception e) {
                    return EmergencyJsonUtil.parseEQMonitorStationModelsForCZ(jsonObj);
                }
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_EQMONITORSTATION_ALL;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getAllImportEarthQuake(final IRequestParameter parameter, final int requestId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getImportEarthQuickUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseImportEarthQuakeModels(jsonObj);
            }

            @Override
            public int getEventId() {
                return requestId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getQBODatas(final IRequestParameter parameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getQBODatasUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseQBODataModels(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_QBODATDS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    //原灾情速报
//    public void getEQQuickReportInfos(final IRequestParameter parameter) {
//        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
//
//            @Override
//            public HttpRequestType getRequestType() {
//                return HttpRequestType.GET;
//            }
//
//            @Override
//            public String getUrl() {
//                return ServiceUrlManager.getEQQuickReportInfoUrl();
//            }
//
//            @Override
//            public IRequestParameter prepareParameters() {
//                return parameter;
//            }
//
//            @Override
//            protected Object customParse(JSONObject jsonObj) {
//                return EmergencyJsonUtil.parseEQQuickReportModel(jsonObj);
//            }
//
//            @Override
//            public int getEventId() {
//                return ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_INFO;
//            }
//
//            @Override
//            public Class<AServerResponse> getResponseClass() {
//                return null;
//            }
//
//            @Override
//            protected boolean getParseByGson() {
//                return false;
//            }
//        });
//    }

    public void getEQQuickReportInfos(final IRequestParameter parameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEQQuickReportInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseEQQuickReportModel(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getInvestigationInfos(final IRequestParameter parameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInvestigationInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseInvestigationModel(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_INVESGATION_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getSearchParams(final int key) {
        RequestExecutor.execute(new ARequestCallback<EventReportResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            protected boolean getParseByGson() {
                return false;
            }

            //未解析
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getSearchParamUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetSearchFormParameter(key);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_EARTHQUAKE_INFO_SEARCH_PARAMS;
            }

            @Override
            public Class<EventReportResponse> getResponseClass() {
                return null;
            }
        });
    }

    public void getEQQuickReportInspectInfos(final String url, final IRequestParameter parameter, final int eventId) {
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
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseEQQuickReportInspectInfo(jsonObj);
            }

            @Override
            public int getEventId() {
                return eventId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    /**
     * 获取需要更新的速报信息  返回zip包的服务器路径
     *
     * @param url
     * @param parameter
     * @param eventId
     */
    public void getEQQuickReportInspectInfosForUpdate(final String url, final IRequestParameter parameter, final int eventId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            public boolean isForComplexResponse() {
                return false;
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
                return parameter;
            }

            @Override
            public int getEventId() {
                return eventId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }

                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.str_emergency_get_inspect_error));
                } else {

                    JSONObject object;
                    try {
                        event = new ResponseEvent(getEventId());
                        event.setStatus(ResponseEventStatus.OK);
                        object = new JSONObject(response);
                        boolean isSuccess = object.optBoolean("success");
                        if (isSuccess) {
                            event.setData(object.opt("url"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                EventBusUtil.post(event);
            }
        });
    }

    /**
     * 上报现场调查数据  未使用
     *
     * @param parameter
     * @param eventId
     */
    public void reportEQXCDCInfos(final IRequestParameter parameter, final int eventId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.reportEQXCDCDetail();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseEQQuickReportInspectInfo(jsonObj);
            }

            @Override
            public int getEventId() {
                return eventId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getUnUsualReportForm(final String formCode) {
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
                response.setInspectItems(adaptItems);
                return response;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getUnUsualReportFormUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetUnUsualReportFormParameter(formCode);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_UNUSUAL_REPORT_FORM;
            }

            @Override
            public Class<EventReportResponse> getResponseClass() {
                return null;
            }
        });
    }


    //获取通知公告的url，对应手持端消息公告功能模块
    public void getNoticeList(final User user, final int pageNo, final int pageSize) {
        RequestExecutor.execute(new ARequestCallback<GetNoticeListResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }


            @Override
            public String getUrl() {
                return ServiceUrlManager.getNoticeList();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetNoticeListParameter(user, pageNo, pageSize);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_NOTICE_LIST;
            }

            @Override
            public Class<GetNoticeListResponse> getResponseClass() {
                return GetNoticeListResponse.class;
            }
        });
    }

    //获取通知公告的url，对应手持端消息公告功能模块
    public void getNoticeContent(final String gid) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
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
            public String getUrl() {
                return ServiceUrlManager.getNoticeContent();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetNoticeContentParameter(gid);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_NOTICE_Content;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //查询避难场所信息
    public void getRefugeList() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEQRefugeInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEQRefugeParameter();
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseEQRefugeModelForCZ(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_REFUGE_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    //查询避难场所信息（武汉地震）
    public void getRefugeforWHList() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEQRefugeInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEQRefugeParameter();
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parseEQRefugeModelForCZ(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_REFUGE_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    //查询消息公告计划任务信息（武汉地震）
    public void getEQMessageList() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEQMessageList();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEQMessageParameter();
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return EmergencyJsonUtil.parsseEQMessageModelForWH(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_MESSAGE_LIST;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void getRelateEarthquake(final EarthQuakeQuickReportModel reportModel, final EarthQuakeInfoModel localModel) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.reportExceptionEq();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetEQRelateParameter(reportModel, localModel);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_REPORT_RELATE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

        });
    }

    //查询所有联系人
    public void getEmerContact() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEmerContactUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return null;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                String path = FileUtil.getInstance(null).getMediaPathforCache() + "contacts.json";
                if (null != jsonObj) {
                    Textwriter.write(path, jsonObj.toString());
                }
                return EmergencyJsonUtil.parseContactsModel(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_GET_CONTACT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }

    public void updateManState(final boolean isLogin) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.updateManStateUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new UpdateManStateParameter(isLogin);
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.EMERGENCY_UPDATE_MAN_STATE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }
}
