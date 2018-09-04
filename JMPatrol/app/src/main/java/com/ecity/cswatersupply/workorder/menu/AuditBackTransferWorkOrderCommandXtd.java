package com.ecity.cswatersupply.workorder.menu;

import android.os.Bundle;
import android.view.View;

import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderBackTransferAuditActivity;

/**
 * 退单转办审核
 * @author jonathanma
 *
 */
public class AuditBackTransferWorkOrderCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return 0;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean executeWithExtra(View v, Bundle data) {
        if (data == null) {
            return false;
        }
        UIHelper.startActivityWithExtra(WorkOrderBackTransferAuditActivity.class, data);
        return true;
    }
}
