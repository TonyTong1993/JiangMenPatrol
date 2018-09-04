package com.ecity.cswatersupply.workorder.data;

import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.workorder.model.WorkOrderList;

/**
 * 远程数据资源
 * @author gaokai
 *
 */
public class TasksRemoteDataSource implements TasksDataSource{
    private static TasksRemoteDataSource INSTANCE;

    @Override
    public void getTask(String id) {
        WorkOrderService.instance.downLoadAllWorkOrders();
    }

    public static TasksDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void saveData(String mTitleName, WorkOrderList data) {
        
    }
}
