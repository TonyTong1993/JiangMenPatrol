package com.ecity.cswatersupply.service;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.Event2TaskParameter;
import com.ecity.cswatersupply.network.request.Event2WorkOrderParameter;
import com.ecity.cswatersupply.network.request.ExtraDetailInfoParameter;
import com.ecity.cswatersupply.network.request.GetTaskFormDataParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderDetailTabParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderFlowDetailInfoParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderGroupParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderListParameter;
import com.ecity.cswatersupply.network.request.GetWorkOrderLogInfoParameter;
import com.ecity.cswatersupply.network.request.WorkOrderCategoryAmountParameter;
import com.ecity.cswatersupply.network.request.WorkOrderCategoryParameter;
import com.ecity.cswatersupply.network.request.WorkOrderCommonParameter;
import com.ecity.cswatersupply.network.request.WorkOrderDetailBasicInfoParameter;
import com.ecity.cswatersupply.network.request.WorkOrderDetailFlowInfoDetailParameter;
import com.ecity.cswatersupply.network.request.WorkOrderDetailFlowInfoParameter;
import com.ecity.cswatersupply.network.request.WorkOrderEvent2TaskParameter;
import com.ecity.cswatersupply.network.request.WorkOrderFilterParameter;
import com.ecity.cswatersupply.network.request.WorkOrderParameter;
import com.ecity.cswatersupply.network.request.WorkOrderSearchResultParameter;
import com.ecity.cswatersupply.network.request.WorkOrderSummaryItemParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.workorder.model.ExtraInfo;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsParamter;
import com.ecity.cswatersupply.workorder.network.ContactManResponse;
import com.ecity.cswatersupply.workorder.network.MaterialInfoResponse;
import com.ecity.cswatersupply.workorder.network.WorkLeaderStaticsParamter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单网络请求操作
 *
 * @author gaokai
 */
public enum WorkOrderService {
    // 单例的最简洁模式：枚举单例
    instance;

    WorkOrderService() {
    }

    public void downLoadAllWorkOrders() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getAllWorkOrdersUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DOWN_ALL_WORKORDER;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrders(jsonObj);
            }
        });
    }

    public void getExtraDetailInfo(final String gid) {
        RequestExecutor.execute(new ARequestCallback<ExtraInfo>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getExtraDetailInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new ExtraDetailInfoParameter(gid);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DETAIL;
            }

            @Override
            public Class<ExtraInfo> getResponseClass() {
                return ExtraInfo.class;
            }
        });
    }

    public void getMainWorkFlowFormData(final Map<String, String> params) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getFormDataUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderCommonParameter(params);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_WORKORDER_INSPECT_ITEMS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptItems(jsonObj);
            }
        });
    }

    public void getSubWorkFlowFormData(final Map<String, String> params) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderSubFormDataUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderCommonParameter(params);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_WORKORDER_INSPECT_ITEMS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptItems(jsonObj);
            }
        });
    }

    /**
     * 工单处理，工单一切流转操作
     */
    public void handleWorkOrder(final String url, final int eventId, final Map<String, String> params) {
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
                return new WorkOrderCommonParameter(params);
            }

            @Override
            public int getEventId() {
                return eventId;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                jsonObj.remove("success");
                try {
                    jsonObj.put("processInstanceId", params.get("processInstanceId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObj;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getContactMan(final Map<String, String> params) {
        RequestExecutor.execute(new ARequestCallback<ContactManResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getContactManUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderCommonParameter(params);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_CONTACT_MAN;
            }

            @Override
            public Class<ContactManResponse> getResponseClass() {
                return ContactManResponse.class;
            }
        });
    }

    public void getEvent2TaskContactMan() {
        RequestExecutor.execute(new ARequestCallback<ContactManResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEvent2TaskContactManUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderEvent2TaskParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_CONTACT_MAN;
            }

            @Override
            public Class<ContactManResponse> getResponseClass() {
                return ContactManResponse.class;
            }
        });
    }

    public void getMaterialInfo() {
        RequestExecutor.execute(new ARequestCallback<MaterialInfoResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getMaterialInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new EmptyRequestParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_MATERIAL_INFO;
            }

            @Override
            public Class<MaterialInfoResponse> getResponseClass() {
                return MaterialInfoResponse.class;
            }
        });
    }

    //工单详情－基本信息、勘察信息、维修信息。
    public void getDetailInfo(final WorkOrder workOrder) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getDetailBasicInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderDetailBasicInfoParameter(workOrder);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptDetailItems(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_DETAIL_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //工单详情-流程信息。
    public void getWorkOrderDetailFlowInfo(final WorkOrder workOrder) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {

                return ServiceUrlManager.getInstance().getWorkOrderDetailFlowInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderDetailFlowInfoParameter(workOrder);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseFlowInfoList(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_FLOW_DETAIL_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //工单详情－流程信息-详情信息
    public void getFlowInfoDetails(final String processinstanceid, final String type) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderDetailFlowInfoDetailUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderDetailFlowInfoDetailParameter(processinstanceid, type);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptItems(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DETAIL_FLOW_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 获取事件转工单需要填写的表单
     */
    public void getEvent2WorkOrderForm(final String eventId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEvent2WorkOrderFormUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new IRequestParameter() {

                    @Override
                    public Map<String, String> toMap() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("eventId", eventId);
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
                return InspectItemAdapter.adaptItems(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_EVENT_2_WORK_ORDER_FORM;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 获取事件转任务需要填写的表单
     */
    public void getEvent2TaskForm(final String eventId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getEvent2TaskFormUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new IRequestParameter() {

                    @Override
                    public Map<String, String> toMap() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("eventId", eventId);
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
                return InspectItemAdapter.adaptTaskItems(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_EVENT_2_TASK_FORM;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 上传事件转工单需要填写的表单
     */
    public void submitEvent2WorkOrderForm(final User reporter, final Event event, final List<InspectItem> items) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getSubmitEvent2WorkOrderUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new Event2WorkOrderParameter(reporter, event, items);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_SUBMIT_EVENT_2_WORK_ORDER_FORM;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 上传事件转任务需要填写的表单
     */
    public void submitEvent2TaskForm(final User reporter, final Event event, final List<InspectItem> items) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getSubmitEvent2TaskUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new Event2TaskParameter(reporter, event, items);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_SUBMIT_EVENT_2_TASK_FORM;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void downLoadWorkOrdersByFilter(final String filters, final int pageNum, final String orderBy) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrdersFilterUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderFilterParameter(filters, pageNum, orderBy,null);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DOWN_FILTER_WORKORDER;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrders(jsonObj);
            }
        });
    }

    public void downLoadWorkOrdersByWorkOderId(final String filters, final int pageNum, final String orderBy,final String condition) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrdersFilterUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderFilterParameter(filters, pageNum, orderBy,condition);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DOWN_By_WORKORDER_ID;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrders(jsonObj);
            }
        });
    }

    /**
     * 获取工单汇总功能的详情信息
     *
     * @param pieData   部门
     * @param pieData 开始时间
     * @param pieData 结束时间
     * @param pieData 统计类型
     */
    public void getSummaryDetailWorkOrders(final WorkOrderPieStaticsData pieData, final boolean isTotal) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getDetailSummaryUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderSummaryItemParameter(pieData);
            }

            @Override
            public int getEventId() {
                return isTotal ? ResponseEventStatus.WORKORDER_DOWN_WORKORER_SUMMARY_DETAIL_FROM_TOTAL : ResponseEventStatus.WORKORDER_DOWN_WORKORER_SUMMARY_DETAIL;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                if (isTotal) {
                    return JsonUtil.parseSummaryDetails(jsonObj, pieData);
                } else {
                    return JsonUtil.parseWorkOrders(jsonObj);
                }
            }
        });
    }

    public void getWorkOrderCategoryById(final String id) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderCategoryUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderCategoryParameter(id);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DOWN_CATEGORY_WORKORDER;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderCategory(jsonObj);
            }
        });
    }

    public void getWorkOrderCategoryFilter(final String id) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderCategoryUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderCategoryParameter(id);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DOWN_CATEGORY__FILTER_WORKORDER;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderCategory(jsonObj);
            }
        });
    }

    public void getWorkOrderCategoryAmount() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderCategoryAmountUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderCategoryAmountParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DOWN_CATEGORY_AMOUNT_WORKORDER;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderCategoryAmount(jsonObj);
            }
        });
    }

    public void getWorkOrderSearchResult(final String searchParameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrdersFilterUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderSearchResultParameter(searchParameter);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_SEARCH_WORKORDER_RESULT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderSearchResult(jsonObj);
            }
        });
    }

    /**
     * 刷新图表数据
     *
     * @param pieData
     * @param isGetPieChart 是否为饼状图
     */
    public void getWorkOrderSummaryDataStatics(final WorkOrderPieStaticsData pieData, final boolean isGetPieChart) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                if (isGetPieChart) {
                    return ServiceUrlManager.getInstance().getWorkOrderPieStaticsUrl();
                } else {
                    return ServiceUrlManager.getInstance().getWorkOrderBarStaticsUrl();
                }
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderPieStaticsParamter(pieData, isGetPieChart);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                if (isGetPieChart) {
                    return JsonUtil.parseWorkOrderSummaryPieStatics(jsonObj);
                } else {
                    return JsonUtil.parseWorkOrderSummaryBarStatics(jsonObj);
                }
            }

            @Override
            public int getEventId() {
                if (isGetPieChart) {
                    return ResponseEventStatus.WORKORDER_GET_PIE_DATA_STATIC;
                } else {
                    return ResponseEventStatus.WORKORDER_GET_BAR_DATA_STATIC;
                }
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /**
     * 请求班长表数据
     *
     * @param pieData
     */
    public void getGroupWorkorderStatistics(final WorkOrderPieStaticsData pieData) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getGroupWorkorderStatistics();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkLeaderStaticsParamter(pieData);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderLeader(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_BAR_DATA_STATIC_Leader;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void getUnfinishedWorkOrderInfos(final String userid, final String groupid) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getGroupWorkOrderInfoUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWorkOrderGroupParameter(userid, groupid);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_UNFINISHED_INFOS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderUnfinishedInfos(jsonObj);
            }
        });
    }


//    ----------------------------------------------------------------------------------------
//    ----------------------------------------------------------------------------------------

    /***
     * 在新的服务下查询工单
     * @param pageNum 请求页数
     * @param pageSize  每页请求条数
     * @param strWhere  查询条件
     * @param workorderState 工单状态
     */
    public void queryWorkOrders(final int pageNum, final int pageSize, final String strWhere, final String workorderState) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().queryWorkOrdersUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWorkOrderListParameter(pageNum, pageSize, strWhere, workorderState);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_QUERY_WORKORDER_LIST;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrders1(jsonObj);
            }
        });
    }

    /****
     * 获取工单表单数据
     * @param taskBtn
     */
    public void getTaskFormData(final WorkOrderBtnModel taskBtn) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getTaskFormDataUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetTaskFormDataParameter(taskBtn);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.FORM_GET_FORM_INSPECTITEMS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptFormReportItems(jsonObj);
            }
        });
    }

    /****
     * 新的服务下，获取工单详情的tab选项
     * @param processInstanceId 工单的唯一流程id
     */
    public void getWorkOrderDetailTab(final String processInstanceId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderDetailTabsUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWorkOrderDetailTabParameter(processInstanceId);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_DETAIL_TAB;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrdersDetailTabs(jsonObj);
            }
        });
    }

    /****
     * 新的服务下，获取工单详情的基本信息、勘察信息、材料信息、维修信息
     * @param url
     * @param params
     * @param eventId
     */
    public void getWorkOrderInfo(final String url, final Map<String, String> params, final int eventId) {
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
                        return params;
                    }
                };
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

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return InspectItemAdapter.adaptDetailItems(jsonObj);
            }
        });
    }

    /****
     * 新的服务下，获取工单详情的审批流程信息
     * @param url
     * @param params
     * @param eventId
     */
    public void getWorkOrderAduitInfo(final String url, final Map<String, String> params, final int eventId) {
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
                        return params;
                    }
                };
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

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrdersFlowInfos(jsonObj);
            }
        });
    }

    /****
     * 新的服务下，获取工单详情的详细审批信息
     * @param gid
     * @param type
     */
    public void getWorkOrderFlowDetailInfo(final String gid, final String type) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderFlowDetailUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetWorkOrderFlowDetailInfoParameter(gid, type);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrdersFlowDetailInfos(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_DETAIL_FLOW_INFO;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /****
     * 新的服务下，获取工单详情的日志信息
     * @param url
     * @param processInstanceId
     */
    public void getLogDateBacks(final String url, final String processInstanceId) {
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
                return new GetWorkOrderLogInfoParameter(processInstanceId);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseLogList1(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_GET_DETAIL_LOG;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    /****
     * 新的服务下，搜索工单数据
     * @param searchParameter
     */
    public void getWorkOrderSearchResult1(final String searchParameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderSearchUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderSearchResultParameter(searchParameter);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_SEARCH_WORKORDER_RESULT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWorkOrderSearchResult1(jsonObj);
            }
        });
    }

}
