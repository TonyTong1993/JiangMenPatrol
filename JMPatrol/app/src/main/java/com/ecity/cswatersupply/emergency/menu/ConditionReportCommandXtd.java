package com.ecity.cswatersupply.emergency.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * 异常速报菜单
 * @author Gxx 2016-12-07
 */
public class ConditionReportCommandXtd extends AMenuCommand{

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.str_emergency_condition_report);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, UnUsualReportOperater.class.getName());
        bundle.putString(CustomViewInflater.EVENTTYPE, "0006");
        UIHelper.startActivityWithExtra(CustomReportActivity1.class, bundle);
        return true;
    }
}
