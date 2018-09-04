package com.ecity.cswatersupply.workorder.model;

import java.util.List;

public class WorkOrderSummaryPie {
    private String total;
    private List<WorkOrderSummaryPieBean> pieBean;

    public List<WorkOrderSummaryPieBean> getPieBean() {
        return pieBean;
    }

    public void setPieBean(List<WorkOrderSummaryPieBean> pieBean) {
        this.pieBean = pieBean;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}