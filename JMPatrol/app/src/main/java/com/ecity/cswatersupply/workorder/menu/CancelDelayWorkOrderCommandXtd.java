package com.ecity.cswatersupply.workorder.menu;

import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;

/**
 * 取消延期
 * @author jonathanma
 *
 */
public class CancelDelayWorkOrderCommandXtd extends ACancelSubProcessWorkOrderCommandXtd {

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.WORKORDER_CANCEL_DELAY;
    }

    @Override
    protected WorkOrderState getWorkOrderSubState() {
        return WorkOrderState.DELAY;
    }
}
