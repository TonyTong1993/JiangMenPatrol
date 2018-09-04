package com.ecity.cswatersupply.workorder.model;

import android.content.Context;

import com.ecity.cswatersupply.model.IIncreaseInspectItemModel;
import com.ecity.cswatersupply.model.OrganisationSelection;

/**
 * Created by jonathanma on 17/4/2017.
 */

public class UserWorkload implements IIncreaseInspectItemModel {
    private OrganisationSelection userInfo;
    private String count;

    public UserWorkload(OrganisationSelection userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String generateTitle(Context context) {
        return userInfo.getName();
    }

    @Override
    public String generateDetail(Context context) {
        return "";
    }

    @Override
    public String getCount() {
        return count;
    }

    @Override
    public void setCount(String count) {
        this.count = count;
    }

    public OrganisationSelection getUserInfo() {
        return userInfo;
    }
}
