package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.enrique.bluetooth4falcon.CORSStateActivity;

public class CORSCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(CORSStateActivity.class);
        return true;
    }

}
