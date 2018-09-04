package com.ecity.cswatersupply.workorder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装的列表实体类，便于序列化
 * @author gaokai
 *
 */
public class WorkOrderList extends ListModel<List<WorkOrder>> {
    private static final long serialVersionUID = 1L;

    public WorkOrderList() {

    }

    public WorkOrderList(List<List<WorkOrder>> datas) {
        this.datas = datas;
    }

}
