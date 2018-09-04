package com.ecity.cswatersupply.workorder.menu;

import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;

/**
 * 取消转办
 * @author jonathanma
 *
 */
public class CancelTransferWorkOrderCommandXtd extends ACancelSubProcessWorkOrderCommandXtd {

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.WORKORDER_CANCEL_TRANSFER;
    }

    @Override
    protected WorkOrderState getWorkOrderSubState() {
        return WorkOrderState.TRANSFER;
    }
}
