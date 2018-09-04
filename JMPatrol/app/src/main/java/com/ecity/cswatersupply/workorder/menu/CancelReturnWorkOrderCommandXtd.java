package com.ecity.cswatersupply.workorder.menu;

import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;

/**
 * 取消退单
 * @author jonathanma
 *
 */
public class CancelReturnWorkOrderCommandXtd extends ACancelSubProcessWorkOrderCommandXtd {

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.WORKORDER_CANCEL_RETURN;
    }

    @Override
    protected WorkOrderState getWorkOrderSubState() {
        return WorkOrderState.RETURN;
    }
}
