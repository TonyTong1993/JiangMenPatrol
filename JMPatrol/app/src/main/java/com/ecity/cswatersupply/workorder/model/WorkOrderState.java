package com.ecity.cswatersupply.workorder.model;

/**
 * 工单状态
 * 
 * @author gaokai
 *
 */
public enum WorkOrderState {
    /**
     * 待分派
     */
    NEW("2"),
    /**
     * 待接单
     */
    RECEIVED("3"),
    /**
     * 已接单，待勘察
     */
    ACCEPTED("4"),
    /**
     * 已勘察，待上门维修
     */
    EXPLORED("5"),
    /**
     * 已签到，待完工
     */
    SIGNINED("6"),
    /**
     * 已完工上报，审核
     */
    FINISH("7"),
    // ////////////// 以下为分支状态
    /**
     * 退回申请
     */
    RETURN("25"),
    /**
     * 延期申请
     */
    DELAY("22"),
    /**
     * 协助申请
     */
    ASKFORHELP("23"),
    /**
     * 转办申请
     */
    TRANSFER("24"),
    /**
     * 进度
     */
    POCESS("26"),
    /**
     * 延期通过
     */
    DELAY_PASS("14"),
    /**
     * 延期驳回
     */
    DELAY_NOT_PASS("15"),
    /**
     * 协助通过
     */
    ASSIS_PASS("17"),
    /**
     * 协助驳回
     */
    ASSIS_NOT_PASS("18"),
    /**
     * 转办接收
     */
    TRANSFER_PASS("27"),
    /**
     * 转办驳回
     */
    TRANSFER_NOT_PASS("28"),
    /**
     * 退单通过
     */
    RETURN_PASS("29"),
    /**
     * 退单驳回
     */
    RETURN_NOT_PASS("30"),
    /**
     * 协助取消
     */
    ASSIST_CANCEL("32"),
    /**
     * 转办取消
     */
    TRANSFER_CANCEL("33"),
    /**
     * 退单取消
     */
    RETURN_CANCEL("34"),
    /**
     * 退单转办申请
     */
    BACK_TRANSFER_APPLY("38"),
    /**
     * 退单转办通过
     */
    BACK_TRANSFER_PASS("39"),
    /**
     * 退单转办驳回
     */
    BACK_TRANSFER_NOT_PASS("40"),
    /**
     * 退单转办取消
     */
    BACK_TRANSFER_CANCEL("41");

    public String value;

    WorkOrderState(String value) {
        this.value = value;
    }
}
