package com.ecity.cswatersupply.project.service;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.DeleteMessageParameter;
import com.ecity.cswatersupply.network.request.GetMessageParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.project.network.request.GetFZRStastisticsParameter;
import com.ecity.cswatersupply.project.network.request.GetKCSJViewParameter;
import com.ecity.cswatersupply.project.network.request.GetPatrolTreeParameter;
import com.ecity.cswatersupply.project.network.request.GetProjectAttachmentParameter;
import com.ecity.cswatersupply.project.network.request.GetProjectForMapParameter;
import com.ecity.cswatersupply.project.network.request.GetProjectSummaryInfoParameter;
import com.ecity.cswatersupply.project.network.request.GetProjectSummaryWaterMeterInfoParameter;
import com.ecity.cswatersupply.project.network.request.GetProjectTypeCountParameter;
import com.ecity.cswatersupply.project.network.request.GetSafeCreatePageParameter;
import com.ecity.cswatersupply.project.network.request.GetSafeDetailTabParameter;
import com.ecity.cswatersupply.project.network.request.GetSafeEventInfoParameter;
import com.ecity.cswatersupply.project.network.request.GetSafeEventListParameter;
import com.ecity.cswatersupply.project.network.request.GetSafeProjectListParameter;
import com.ecity.cswatersupply.project.network.request.GetWaterMeterInfoParameter;
import com.ecity.cswatersupply.project.network.request.GetWaterMeterListParameter;
import com.ecity.cswatersupply.project.network.request.ProjectLogBackParameter;
import com.ecity.cswatersupply.project.network.request.SearchProjectParameter;
import com.ecity.cswatersupply.project.network.response.GetProjectAttachmentResponse;
import com.ecity.cswatersupply.project.network.response.GetProjectTypeCountResponse;
import com.ecity.cswatersupply.project.network.response.GetProjectWaterMeterCountResponse;
import com.ecity.cswatersupply.project.network.response.GetSafeDetailStepResponse;
import com.ecity.cswatersupply.project.network.response.GetSafeEventListResponse;
import com.ecity.cswatersupply.project.network.response.GetSafePorjectListResponse;
import com.ecity.cswatersupply.project.network.response.ProjectFzrStastistisResponse;
import com.ecity.cswatersupply.project.network.response.SafeDetailStepModel;
import com.ecity.cswatersupply.project.network.response.SafeEventListModel;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;
import com.ecity.cswatersupply.project.network.response.adapter.GetProjectForMapResponse;
import com.ecity.cswatersupply.utils.JsonUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectService {
    private static ProjectService instance;

    static {
        instance = new ProjectService();
    }

    private ProjectService() {
    }

    public static ProjectService getInstance() {
        return instance;
    }

    public void getMessages(final User user, final int pageNo, final int pageSize, final boolean isProcessed) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getUserMsg();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetMessageParameter(user, pageNo, pageSize, isProcessed);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.MESSAGE_QUERY;
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
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void deleteMessages(final List<String> messageIds, final boolean isDeleteAll) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().deleteUserMsg();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new DeleteMessageParameter(messageIds, isDeleteAll);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.MESSAGE_DELETE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getGroupsTrees(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getGroupsTreeUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPatrolTreeParameter(user);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseOrganisationSelection(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.USER_GET_PATROL_TREE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /***
     * 江门工程 项目总览 获得所有项目信息各类数量
     *
     * @param type
     * @param
     * @param
     * @param
     */
    public void queryProCount4Analysis(final String type, final String startTime, final String endTime) {
        RequestExecutor.execute(new ARequestCallback<GetProjectTypeCountResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().queryProCount4Analysis();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetProjectTypeCountParameter(type, startTime, endTime);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_PROJECT_TYPE_COUNT;
            }

            @Override
            public Class<GetProjectTypeCountResponse> getResponseClass() {
                return GetProjectTypeCountResponse.class;
            }
        });
    }

    /**
     * 查询所有月份，或所有天的工程信息
     *
     * @param type
     * @param startTime
     * @param endTime
     */
    public void querySummaryInfo(final int type, final int year, final int month, final String startTime, final String endTime) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getSummaryProCount4AnalysisUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetProjectSummaryInfoParameter(type, year, month, startTime, endTime);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_PROJECT_SUMMARY_COUNT;
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
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 查询所有月份，或所有天的工程信息（水表报装）
     *
     * @param year
     */
    public void queryWaterMeterSummaryInfo(final int year, final int month) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().statProByTimeAndStatus();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetProjectSummaryWaterMeterInfoParameter(year, month);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_WATERMETER_SUMMARY;
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
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 发送一个Get请求。
     *
     * @param url        请求地址
     * @param requestId  请求id
     * @param parameters 请求参数
     */
    public void queryDetail(final String url, final int requestId, final Map<String, String> parameters) {
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
                return new IRequestParameter() {

                    @Override
                    public Map<String, String> toMap() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.putAll(parameters);

                        return map;
                    }
                };
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptProjectDetailItems(jsonObj);
            }

            @Override
            public int getEventId() {
                return requestId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //江门工程获取附件信息
    public void getAttachment(final String guid) {
        RequestExecutor.execute(new ARequestCallback<GetProjectAttachmentResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getAttachment();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetProjectAttachmentParameter(guid);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_PROJECT_ATTACHMENT;
            }

            @Override
            public Class<GetProjectAttachmentResponse> getResponseClass() {
                return GetProjectAttachmentResponse.class;
            }
        });
    }

    //江门工程勘察设计设计部查看工程部委托
    public void getViewKcsjProject(final String kcid, final User user, final boolean isFromMsg) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getDesignLook();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetKCSJViewParameter(kcid, user);
            }

            @Override
            public int getEventId() {
                if (!isFromMsg) {
                    return ResponseEventStatus.PROJECT_GET_PROSPECTIVE_VIEW;
                } else {
                    return ResponseEventStatus.PROJECT_GET_PROSPECTIVE_VIEW_FROM_MSG;
                }
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //水表报装工程统计信息
    public void statProStatusCountByTime(final String type, final String startTime, final String endTime) {
        RequestExecutor.execute(new ARequestCallback<GetProjectWaterMeterCountResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().statProStatusCountByTime();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWaterMeterInfoParameter(type, startTime, endTime);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_WATERMETER_INFO;
            }

            @Override
            public Class<GetProjectWaterMeterCountResponse> getResponseClass() {
                return GetProjectWaterMeterCountResponse.class;
            }
        });
    }

    //水表报装列表信息
    public void getWaterMeterProList(final int pageNo, final int pageSize, final String type, final String status, final String startTime, final String endTime) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getWaterMeterProList();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWaterMeterListParameter(pageNo, pageSize, type, status, startTime, endTime);
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
                int eventId = -1;
                switch (status) {
                    case "1"://未完成
                        eventId = ResponseEventStatus.PROJECT_GET_WATERMETER_LIST1;
                        break;
                    case "2"://挂起
                        eventId = ResponseEventStatus.PROJECT_GET_WATERMETER_LIST2;
                        break;
                    case "3"://完成
                        eventId = ResponseEventStatus.PROJECT_GET_WATERMETER_LIST3;
                        break;
                    case "4"://废件
                        eventId = ResponseEventStatus.PROJECT_GET_WATERMETER_LIST4;
                        break;
                    case "-1"://代表查询超期
                        eventId = ResponseEventStatus.PROJECT_GET_WATERMETER_LIST5;
                        break;
                }
                return eventId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getLogDateBacks(final String logBackType, final String projectId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                if (logBackType.equals("SAFE_FLAG")) {
                    return ProjectServiceUrlManager.getInstance().getSafeEventLogBackUrl();
                } else {
                    return ProjectServiceUrlManager.getInstance().getProjectsLogBackUrl(logBackType);
                }
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new ProjectLogBackParameter(projectId, logBackType);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseProjectLogList(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_PROJECTS_LOG_BACK;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //江门工程公共列表请求
    public void getCommonProList(final int position, final String url, final Map<String, String> parameters) {
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
                return new IRequestParameter() {

                    @Override
                    public Map<String, String> toMap() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.putAll(parameters);

                        return map;
                    }
                };
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
                if (position == 0) {
                    return ResponseEventStatus.PROJECT_GET_PROSPECTIVE_LIST_NEW;
                } else {
                    return ResponseEventStatus.PROJECT_GET_PROSPECTIVE_LIST_OLD;
                }
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //江门工程提交公共
    public void postCommonCheck(final String url, final int enventId, final AReportInspectItemParameter checkKCSJParameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return checkKCSJParameter;
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
                return enventId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void sendGetRequest(final String url, final int requestId, final Map<String, String> parameters) {
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
                return new IRequestParameter() {

                    @Override
                    public Map<String, String> toMap() {
                        return parameters;
                    }
                };
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
                return requestId;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //工程一张图 获取江门工程地图上的工程信息
    public void getProjectListForMap(final String type) {
        RequestExecutor.execute(new ARequestCallback<GetProjectForMapResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getProjectForMap();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetProjectForMapParameter(type);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_INFO_FOR_MAP;
            }


            @Override
            public Class<GetProjectForMapResponse> getResponseClass() {
                return GetProjectForMapResponse.class;
            }
        });
    }

    //江门工程 统计汇总功能获取相关信息
    public void getProjectStatistics(final String fzrType, final String startTime, final String endTime) {
        RequestExecutor.execute(new ARequestCallback<ProjectFzrStastistisResponse>() {


            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getFzrStastictis();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetFZRStastisticsParameter(fzrType, startTime, endTime);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_GET_STATISTICS_VIEW;
            }


            @Override
            public Class<ProjectFzrStastistisResponse> getResponseClass() {
                return ProjectFzrStastistisResponse.class;
            }
        });
    }

    //江门工程 安全管理 获取项目列表
    public void querySafeProjects(final User user, final int pageNo, final int pageSize, final SearchProjectParameter searchProjectparameter) {
        RequestExecutor.execute(new ARequestCallback<GetSafePorjectListResponse>() {


            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().querySafeProList();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetSafeProjectListParameter(user, pageNo, pageSize, searchProjectparameter);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_PROJECT;
            }


            @Override
            public Class<GetSafePorjectListResponse> getResponseClass() {
                return GetSafePorjectListResponse.class;
            }
        });
    }

    //江门工程 安全管理 获取问题列表（点击项目列表）
    public void querySafeEventList(final User user, final SafeProjectListModel model) {
        RequestExecutor.execute(new ARequestCallback<GetSafeEventListResponse>() {


            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().querySafeList();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetSafeEventListParameter(user, model);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_EVENT_LIST;
            }


            @Override
            public Class<GetSafeEventListResponse> getResponseClass() {
                return GetSafeEventListResponse.class;
            }
        });
    }

    //江门工程 安全管理 获取问题详情的相关Tab页信息
    public void getEventStep(final SafeEventListModel model) {
        RequestExecutor.execute(new ARequestCallback<GetSafeDetailStepResponse>() {


            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getEventStep();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetSafeDetailTabParameter(model);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_TAB;
            }


            @Override
            public Class<GetSafeDetailStepResponse> getResponseClass() {
                return GetSafeDetailStepResponse.class;
            }
        });
    }

    //江门工程 安全管理 获取问题详情的内容
    public void getEventInfo(final int requestId, final String step, final SafeEventListModel model, final User user, final SafeDetailStepModel stepdata) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {


            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getEventInfo(step);
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetSafeEventInfoParameter(model, user,stepdata);
            }

            @Override
            public int getEventId() {
                return requestId;
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
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //江门工程 安全管理 获得创建页内容
    public void getCreatePage(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {


            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ProjectServiceUrlManager.getInstance().getCreatePage();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetSafeCreatePageParameter(user);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PROJECT_SAFE_CREATE_PAGE_INFO;
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
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

}
