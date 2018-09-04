package com.ecity.cswatersupply.workorder.menu;

import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;

/**
 * 取消协助
 * @author jonathanma
 *
 */
public class CancelAssistWorkOrderCommandXtd extends ACancelSubProcessWorkOrderCommandXtd {

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.WORKORDER_CANCEL_ASSIST;
    }

    @Override
    protected WorkOrderState getWorkOrderSubState() {
        return WorkOrderState.ASKFORHELP;
    }
}
