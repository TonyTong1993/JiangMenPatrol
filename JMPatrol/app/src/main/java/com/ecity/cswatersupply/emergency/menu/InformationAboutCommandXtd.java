package com.ecity.cswatersupply.emergency.menu;

import com.ecity.cswatersupply.emergency.activity.NewsAnnounActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

public class InformationAboutCommandXtd extends AMenuCommand{


    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(NewsAnnounActivity.class);
        return true;
    }

}
