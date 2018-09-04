package com.ecity.cswatersupply.workorder.model;

/**
 * 工单来源
 * 
 * @author gaokai
 *
 */
public enum WorkOrderSource {
    /**
     * 热线
     */
    REXIAN("热线"),
    /**
     * 巡检
     */
    PATROL("巡检"),
    /**
     * 检漏
     */
    JIANLOU("检漏"),
    /**
     * 自报
     */
    ZIBAO("自报");
    public String value;

    WorkOrderSource(String value) {
        this.value = value;
    }
}
