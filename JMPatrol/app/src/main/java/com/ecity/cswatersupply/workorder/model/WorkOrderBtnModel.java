package com.ecity.cswatersupply.workorder.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 * 工单列表的按钮结构
 */
public class WorkOrderBtnModel implements Serializable{
    private static final long serialVersionUID = 1L;

    private String taskId;
    private int flowType;
    private int index;
    private String taskCode;
    private String taskName;
    private String taskType;
    private List<WorkOrderBtnModel> subWorkOrderBtns;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getFlowType() {
        return flowType;
    }

    public void setFlowType(int flowType) {
        this.flowType = flowType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<WorkOrderBtnModel> getSubWorkOrderBtns() {
        return subWorkOrderBtns;
    }

    public void setSubWorkOrderBtns(List<WorkOrderBtnModel> subWorkOrderBtns) {
        this.subWorkOrderBtns = subWorkOrderBtns;
    }
}
