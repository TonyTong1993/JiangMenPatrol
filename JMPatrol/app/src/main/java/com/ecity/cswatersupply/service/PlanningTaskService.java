package com.ecity.cswatersupply.service;

import java.util.List;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.planningTask.ArriveLineInfo;
import com.ecity.cswatersupply.model.planningTask.ArrivePointInfo;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.GetPointPartAttrsParameter;
import com.ecity.cswatersupply.network.request.PlanTaskGetContentsParameter;
import com.ecity.cswatersupply.network.request.PlanningTaskParameter;
import com.ecity.cswatersupply.network.request.RepoterArrivedLinePartParameter;
import com.ecity.cswatersupply.network.request.RepoterArrivedPointPartParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class PlanningTaskService {

    private volatile static PlanningTaskService instance;

    private PlanningTaskService() {
    }

    public static PlanningTaskService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new PlanningTaskService();
                }
            }
        }
        return instance;
    }

    public void getPlanningTasks(final String url, final User user,final String type) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            public boolean isForComplexResponse() {
                return false;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new PlanningTaskParameter(user,type);
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PLANNINGTASK_LIST;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }
                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.get_planningtask_error));
                } else {
                    event = new ResponseEvent(getEventId());
                    event.setStatus(ResponseEventStatus.OK);
                    event.setData(response);
                }
                EventBusUtil.post(event);
            }

        });
    }

    public void getPlanningTasksInMainActivity(final String url, final User user) {
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
                return new PlanningTaskParameter(user,null);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PLANNINGTASK_LIST_MAIN;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }
                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.get_planningtask_error));
                } else {
                    event = new ResponseEvent(getEventId());
                    event.setStatus(ResponseEventStatus.OK);
                    event.setData(response);
                }
                EventBusUtil.post(event);
            }

        });
    }

    public void repoterArrivedPointPart(final String url, final List<ArrivePointInfo> infos) {
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
                return new RepoterArrivedPointPartParameter(infos);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PLANNING_POINT_ARRIVED;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

        });
    }

    public void repoterArrivedLinePart(final String url, final List<ArriveLineInfo> infos) {
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
                return new RepoterArrivedLinePartParameter(infos);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PLANNING_LINE_ARRIVED;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

        });
    }

    public void getReportInspectItems(final String url) {
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
                return new PlanTaskGetContentsParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PLANNING_REPORTINSPECTITEMS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }
                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.get_content_error));
                } else {
                    event = new ResponseEvent(getEventId());
                    event.setStatus(ResponseEventStatus.OK);
                    event.setData(response);
                }
                EventBusUtil.post(event);
            }

        });
    }

    public void getAttrsForGeometry(final String url, final String objectIds,final String flag) {
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
                return new GetPointPartAttrsParameter(objectIds);
            }

            @Override
            public int getEventId() {
                if (flag.equalsIgnoreCase("pointservice")){
                    return ResponseEventStatus.PLANNING_ATTRS_POINTSERVICE;
                }else{
                    return ResponseEventStatus.PLANNING_ATTRS;
                }
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }
                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.get_point_attrs_error));
                } else {
                    event = new ResponseEvent(getEventId());
                    event.setStatus(ResponseEventStatus.OK);
                    event.setData(response);
                }
                EventBusUtil.post(event);
            }

        });
    }
}
