package com.ecity.cswatersupply.workorder.menu;

import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;

/**
 * 取消退单转办申请
 * @author jonathanma
 *
 */
public class CancelBackTransferWorkOrderCommandXtd extends ACancelSubProcessWorkOrderCommandXtd {

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.WORKORDER_BACK_TRANSFER_CANCEL;
    }

    @Override
    protected WorkOrderState getWorkOrderSubState() {
        return WorkOrderState.BACK_TRANSFER_APPLY;
    }
}
