package com.ecity.cswatersupply.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.CustomMainReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/**
 * 数据采集菜单
 * 2017-3-21
 */
public class DataCollectionCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        bundle.putString(CustomViewInflater.REPORT_TITLE, ResourceUtil.getStringById(R.string.event_data_collection_title));
        bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
        bundle.putInt(CustomViewInflater.EVENTTYPE, 4);
        UIHelper.startActivityWithExtra(CustomMainReportActivity1.class, bundle);
        return true;
    }

}
