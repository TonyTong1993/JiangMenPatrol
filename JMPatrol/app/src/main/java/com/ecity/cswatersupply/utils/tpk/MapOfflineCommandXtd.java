package com.ecity.cswatersupply.utils.tpk;

import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/**
 * 
 * @author qiweijiang 离线地图下载
 *
 */
public class MapOfflineCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(MapOfflineActivity.class);
        return false;
    }
}
