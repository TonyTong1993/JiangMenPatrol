package com.ecity.cswatersupply.workorder.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.data.WorkOrderRepository;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.model.WorkOrderList;
import com.ecity.cswatersupply.workorder.view.WorkOrderContract;
import com.squareup.leakcanary.watcher.Preconditions;

/**
 * 工单模块逻辑层 但是后来统一使用的检查项上报的业务逻辑在{@link WorkOrderOperator}里 因为他们用到了@author sun
 * shanai的上报页面，对外暴露的不是接口，而是虚基类，没法扩展
 * 
 * @author gaokai
 *
 */
public class WorkOrderPresenter implements WorkOrderContract.Presenter {
    private String currentGid = "";
    private int currentGroupPosition = 0;
    private boolean showNotification = true;// 默认显示工单通知
    private final WorkOrderContract.View mWorkOrderView;
    private final WorkOrderRepository mWorkOrderRepository;
    private static final String KEY_CACHE = "WORKORDER";

    public WorkOrderPresenter(String titleName, WorkOrderRepository workOrderRepository, WorkOrderContract.View workOrderView) {
        mWorkOrderRepository = Preconditions.checkNotNull(workOrderRepository, "workOrderRepository");
        mWorkOrderView = Preconditions.checkNotNull(workOrderView, "workOrderView");
        mWorkOrderView.setPresenter(this);
    }

    @Override
    public String getCurrentGid() {
        return this.currentGid;
    }

    @Override
    public void onLoadingNewData(boolean showNotification) {
        this.showNotification = showNotification;
        mWorkOrderRepository.getTask(KEY_CACHE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleDownloadedWorkOrder(int messageResId, ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            // 网络获取出错时，从缓存读取
            mWorkOrderRepository.readCache(KEY_CACHE);
            return;
        }

        Object data = event.getData();
        if (data != null && data instanceof ArrayList) {
            ArrayList<WorkOrder> workOrders = null;
            try {
                workOrders = (ArrayList<WorkOrder>) data;
            } catch (Exception e) {
                LogUtil.e(this, e);
                workOrders = new ArrayList<WorkOrder>();
            }
            List<List<WorkOrder>> groupedWorkOrders = groupWorkOrders(workOrders);
            WorkOrderList workOrderListEntity = new WorkOrderList();
            workOrderListEntity.setDatas(groupedWorkOrders);
            updateUI(messageResId, workOrderListEntity);
            mWorkOrderRepository.saveData(KEY_CACHE);
            showNotification = true;
        }
    }

    @Override
    public void updateUI(int messageResId, Object obj) {
        if (obj != null) {
            WorkOrderList aModelList = (WorkOrderList) obj;
            ArrayList<List<WorkOrder>> list = (ArrayList<List<WorkOrder>>) (aModelList.getDatas() == null ? new ArrayList<List<WorkOrder>>() : aModelList.getDatas());
            int newDataCount = mWorkOrderRepository.checkNewData(list);// 得到所有分组中的新工单
            if (newDataCount != 0 && showNotification) {
                mWorkOrderView.showStatusBar(ResourceUtil.getStringById(messageResId, newDataCount));
            }
            mWorkOrderView.updateList(mWorkOrderRepository.getCurrentWorkOrders());
        } else {
            mWorkOrderView.updateList(initWorkOrders());
        }
        mWorkOrderView.showCompletionStatus();
    }

    @Override
    public void applyAcceptWorkOrder(String gid) {
        this.currentGid = gid;
        mWorkOrderView.showLoadingDialog(R.string.accept_workorder);
        WorkOrder currentWorkOrder = mWorkOrderRepository.getWorkOrderByGid(currentGroupPosition, gid);
        Map<String, String> params = new HashMap<String, String>();
        params.put(WorkOrder.KEY_ID, gid);
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put("userid", HostApplication.getApplication().getCurrentUser().getId());
            paramJson.putOpt(WorkOrder.KEY_STATE, currentWorkOrder.getAttributes().get(WorkOrder.KEY_STATE));
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        params.put("properties", paramJson.toString());
        WorkOrderService.instance.handleWorkOrder(ServiceUrlManager.getInstance().getSubmitFormDataUrl(), ResponseEventStatus.WORKORDER_ACCEPT_EVENT, params);
    }

    private List<List<WorkOrder>> groupWorkOrders(ArrayList<WorkOrder> workOrders) {
        List<List<WorkOrder>> groupedWorkOrders = initWorkOrders();
        String mainState;
        String subState;
        for (WorkOrder workOrder : workOrders) {
            mainState = workOrder.getAttributes().get(WorkOrder.KEY_STATE);
            subState = workOrder.getAttributes().get(WorkOrder.KEY_SUB_STATE);
            groupSingleWorkOrder(workOrder, mainState, subState, groupedWorkOrders);
        }
        return groupedWorkOrders;
    }

    private void groupSingleWorkOrder(WorkOrder workOrder, String mainState, String subState, List<List<WorkOrder>> groupedWorkOrders) {
        if (mainState == null) {
            return;
        }
        WorkOrderGroupEnum mainTargetGroup = checkMainStateInWhichGroup(mainState);
        if (mainTargetGroup == null) {
            return;
        }
        //快速返回
        if (mainTargetGroup == WorkOrderGroupEnum.COMLETE) {
            groupedWorkOrders.get(WorkOrderGroupEnum.COMLETE.ordinal()).add(workOrder);
            return;
        }

        //没有分支状态，判断是否能操作，如果不能操作，放在处理中分组，此条规则用于过滤组长分派给组员，而组员尚未接单的情况
        if (subState.isEmpty()) {
            if (workOrder.getAttributes().get(WorkOrder.KEY_CAN_OPERATOR) != null && workOrder.getAttributes().get(WorkOrder.KEY_CAN_OPERATOR).equalsIgnoreCase("false")) {
                groupedWorkOrders.get(WorkOrderGroupEnum.HANDLLING.ordinal()).add(workOrder);
            } else {
                groupedWorkOrders.get(mainTargetGroup.ordinal()).add(workOrder);
            }
        }
        //有分支状态
        else {
            WorkOrderGroupEnum subTargetGroup = checkSubStateInWhichGroup(subState);
            if (subTargetGroup == WorkOrderGroupEnum.NOHANDLE) {
                //如果是组长，放到待处理分组，例如：延期申请
                if (HostApplication.getApplication().getCurrentUser().isLeader()) {
                    groupedWorkOrders.get(WorkOrderGroupEnum.NOHANDLE.ordinal()).add(workOrder);
                }
                //如果是组员，放到处理中分组
                else {
                    groupedWorkOrders.get(WorkOrderGroupEnum.HANDLLING.ordinal()).add(workOrder);
                }
            } else if (subTargetGroup == WorkOrderGroupEnum.HANDLLING) {//此处用于过滤转办申请
                String acceptId = workOrder.getAttributes().get(WorkOrder.KEY_TRANSFER_ACCEPTER);
                //此条工单的转单接收人是当前用户，该单子分到待处理分组
                if (acceptId != null && acceptId.equals(HostApplication.getApplication().getCurrentUser().getId())) {
                    groupedWorkOrders.get(WorkOrderGroupEnum.NOHANDLE.ordinal()).add(workOrder);
                } else {
                    groupedWorkOrders.get(WorkOrderGroupEnum.HANDLLING.ordinal()).add(workOrder);
                }
            } else {
                groupedWorkOrders.get(mainTargetGroup.ordinal()).add(workOrder);
            }
        }
    }

    private WorkOrderGroupEnum checkSubStateInWhichGroup(String subState) {
        for (WorkOrderGroupEnum group : WorkOrderGroupEnum.values()) {
            String[] leaderStates = group.getLeaderState().split(",");
            String[] states = subState.split(",");
            for (String state : states) {
                for (String leaderState : leaderStates)
                    if (state.equals(leaderState)) {
                        return group;
                    }
            }
        }

        return null;
    }

    private WorkOrderGroupEnum checkMainStateInWhichGroup(String mainState) {
        for (WorkOrderGroupEnum group : WorkOrderGroupEnum.values()) {
            String[] states = group.getContainState().split(",");
            for (String state : states) {
                if (state.equals(mainState)) {
                    return group;
                }
            }
        }

        return null;
    }

    /**
     * 检查subState集合中的元素是否在subStateCollection中
     */
    private boolean checkIn(String subState, String subStateCollection) {
        String[] states = subState.split(",");
        for (String state : states) {
            if (subStateCollection.contains(state)) {
                return true;
            }
        }
        return false;
    }

    private List<List<WorkOrder>> initWorkOrders() {
        int length = WorkOrderGroupEnum.values().length;
        List<List<WorkOrder>> groupedWorkOrders = new ArrayList<List<WorkOrder>>(length);
        for (int i = 0; i < length; i++) {
            groupedWorkOrders.add(new ArrayList<WorkOrder>());
        }
        return groupedWorkOrders;
    }

    @Override
    public void onLoadingMoreData() {
        // 暂时未用
    }

    @Override
    public void setGroupPosition(int position) {
        this.currentGroupPosition = position;
    }
}
