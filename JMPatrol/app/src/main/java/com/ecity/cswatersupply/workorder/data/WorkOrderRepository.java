package com.ecity.cswatersupply.workorder.data;

import java.util.ArrayList;
import java.util.List;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.model.WorkOrderList;

/**
 * 工单模块数据层 ，负责增删改查工单
 * 
 * @author gaokai
 *
 */
public class WorkOrderRepository {
    private boolean mCacheIsDirty = false;
    private TasksDataSource mTasksLocalDataSource;
    private TasksDataSource mTasksRemoteDataSource;
    private List<List<WorkOrder>> currentWorkOrders;
    private volatile static WorkOrderRepository INSTANCE;

    public WorkOrderRepository(TasksDataSource tasksRemoteDataSource, TasksDataSource tasksLocalDataSource) {
        this.mTasksRemoteDataSource = tasksRemoteDataSource;
        this.mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static WorkOrderRepository getInstance(TasksDataSource tasksRemoteDataSource, TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (WorkOrderRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WorkOrderRepository(tasksRemoteDataSource, tasksLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public List<List<WorkOrder>> getCurrentWorkOrders() {
        return currentWorkOrders;
    }

    public void setCurrentWorkOrders(List<List<WorkOrder>> currentWorkOrders) {
        this.currentWorkOrders = currentWorkOrders;
    }

    public void saveData(String keyCache) {
        mTasksLocalDataSource.saveData(keyCache, new WorkOrderList(currentWorkOrders));
    }

    public void readCache(String key) {
        // 内存中有缓存且可用时，读内存，否则读本地
        if (currentWorkOrders != null && !mCacheIsDirty) {
            WorkOrderList listEntity = new WorkOrderList(currentWorkOrders);
            EventBusUtil.post(new UIEvent(UIEventStatus.ON_LOAD_DATA_SUCCESS, listEntity));
        } else {
            mTasksLocalDataSource.getTask(key);
        }
    }

    public void getTask(String id) {
        if (NetWorkUtil.isNetworkAvailable(HostApplication.getApplication())) {
            mTasksRemoteDataSource.getTask(id);
        } else {
            readCache(id);
        }
    }

    public void moveWorkOrderTo(WorkOrder workOrder, int position, WorkOrderGroupEnum group) {
        if (currentWorkOrders == null || position == -1) {
            return;
        }
        deleteWorkOrder(position, workOrder);
        addWorkOrder(group.ordinal(), workOrder);
    }

    public void deleteWorkOrder(int position, WorkOrder currentWorkOrder) {
        if (currentWorkOrders == null || position == -1) {
            return;
        }
        ArrayList<WorkOrder> workOrders = (ArrayList<WorkOrder>) currentWorkOrders.get(position);
        workOrders.remove(currentWorkOrder);
    }

    public void changeWorkOrderState(WorkOrder currentWorkOrder, String state, int currentGroupPosition) {
        if (currentWorkOrder != null) {
            currentWorkOrder.getAttributes().put(WorkOrder.KEY_STATE, state);
            toTop(currentGroupPosition, currentWorkOrder);
        }
    }

    /**
     * 改变分支流程状态
     */
    public void changeSubState(WorkOrder currentWorkOrder, String subWorkFlowState, int currentGroupPosition) {
        if (currentWorkOrder != null) {
            currentWorkOrder.getAttributes().put(WorkOrder.KEY_SUB_STATE, subWorkFlowState);
            toTop(currentGroupPosition, currentWorkOrder);
        }
    }

    public WorkOrder getWorkOrderByGid(int position, String gid) {
        if (currentWorkOrders == null || position == -1) {
            return null;
        }
        List<WorkOrder> currentList = currentWorkOrders.get(position);
        for (WorkOrder workOrder : currentList) {
            if (gid.equals(workOrder.getAttributes().get(WorkOrder.KEY_ID))) {
                return workOrder;
            }
        }

        return null;
    }

    /**
     * 过滤出新工单
     * 
     * @param lists
     *            分组后的下载的工单数据
     * @return
     */
    public int checkNewData(ArrayList<List<WorkOrder>> downloadedData) {
        if (this.currentWorkOrders == null) {
            this.currentWorkOrders = downloadedData;
            return getCount(downloadedData);
        }
        int newCount = 0;
        for (int i = 0; i < downloadedData.size(); i++) {
            if (downloadedData.get(i).size() == 0) {
                currentWorkOrders.get(i).clear();
                WorkOrderGroupEnum.values()[i].setHasNew(false);// 如果下载的工单为0，要重置工单指示器图标，不为0的情况在过滤后判断
                continue;
            } else {
                newCount += checkNew(currentWorkOrders.get(i), downloadedData.get(i));
            }
        }
        this.currentWorkOrders = downloadedData;
        return newCount;
    }
    
    private int getCount(List<List<WorkOrder>> newDatas) {
        int count = 0;
        for (int i = 0; i < newDatas.size(); i++) {
            List<WorkOrder> list = newDatas.get(i);
            if (list.size() != 0) {
                WorkOrderGroupEnum.values()[i].setHasNew(true);
            }
            count += list.size();
        }
        return count;
    }

    private int checkNew(List<WorkOrder> currentList, List<WorkOrder> downloadedList) {
        int count = 0;
        for (WorkOrder workOrder : downloadedList) {
            if (!currentList.contains(workOrder)) {
                count++;
            }
        }
        return count;
    }

    private void addWorkOrder(int position, WorkOrder workOrder) {
        if (currentWorkOrders == null || position == -1) {
            return;
        }
        currentWorkOrders.get(position).add(0, workOrder);// 置顶
        WorkOrderGroupEnum.values()[position].setHasNew(true);
    }

    /**
     * 置顶工单
     */
    private void toTop(int position, WorkOrder target) {
        if (currentWorkOrders == null || position == -1) {
            return;
        }
        ArrayList<WorkOrder> workOrders = (ArrayList<WorkOrder>) currentWorkOrders.get(position);
        workOrders.remove(target);
        workOrders.add(0, target);
    }
}
