package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

import android.content.Intent;
import android.provider.Contacts;

public class ContactsCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Intent intent= new Intent(); 
        intent.setClassName("com.android.contacts","com.android.contacts.activities.PeopleActivity");
        UIHelper.startActivityWithItent(intent);
        return true;
    }

}
