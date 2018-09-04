package com.ecity.cswatersupply.workorder.menu;

import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.view.WorkOrderFilterActivity;

/**
 * 
 * @author gaokai 工单处置命令
 *
 */
public class WorkOrderCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(WorkOrderFilterActivity.class);
        return false;
    }
}
