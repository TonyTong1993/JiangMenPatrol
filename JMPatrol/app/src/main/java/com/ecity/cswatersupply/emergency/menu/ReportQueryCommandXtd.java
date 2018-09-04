package com.ecity.cswatersupply.emergency.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.activity.ReportQueryActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * 上报查询菜单
 * @author Gxx 2016-12-07
 */
public class ReportQueryCommandXtd extends AMenuCommand{

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        UIHelper.startActivityWithExtra(ReportQueryActivity.class, bundle);
        return true;
    }
}
