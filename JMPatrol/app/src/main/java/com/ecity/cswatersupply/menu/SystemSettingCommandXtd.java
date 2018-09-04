package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

public class SystemSettingCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        ToastUtil.showShort("系统设置");
        return false;
    }
}
