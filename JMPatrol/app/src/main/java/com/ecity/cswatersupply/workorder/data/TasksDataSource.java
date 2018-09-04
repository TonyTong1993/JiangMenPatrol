package com.ecity.cswatersupply.workorder.data;

import com.ecity.cswatersupply.workorder.model.WorkOrderList;

/**
 * 数据模型接口
 * @author gaokai
 *
 */
public interface TasksDataSource {
    void getTask(String id);

    void saveData(String mTitleName,WorkOrderList data);
}
