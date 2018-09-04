package com.ecity.cswatersupply.emergency.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.activity.EarthquakeLocalInfoActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

//常州地震  灾情速报菜单
//武汉地震  灾情详报菜单
public class EarthQuakeQuickReportCommand extends AMenuCommand {
    public static final String FLAG_FROM_EARTHQUAKE_ZQSB = "FLAG_FROM_EARTHQUAKE_ZQSB";

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                Bundle bundle = new Bundle();
                bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.quick_report_title);
                bundle.putString(CustomViewInflater.REPORT_COMFROM, UnUsualReportOperater.class.getName());
                bundle.putString(CustomViewInflater.EVENTTYPE, "0005");
                UIHelper.startActivityWithExtra(CustomReportActivity1.class, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(FLAG_FROM_EARTHQUAKE_ZQSB, FLAG_FROM_EARTHQUAKE_ZQSB);
                UIHelper.startActivityWithExtra(EarthquakeLocalInfoActivity.class,bundle);
            }
            return true;
        } catch (Exception e) {
            return false;
        }


//        UIHelper.startActivityWithoutExtra(QuickReportListActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.quick_report_title);
//        bundle.putString(CustomViewInflater.REPORT_COMFROM, UnUsualReportOperater.class.getName());
//        bundle.putString(CustomViewInflater.EVENTTYPE, "0005");
//        UIHelper.startActivityWithExtra(CustomReportActivity1.class, bundle);
//        return true;
    }

}
