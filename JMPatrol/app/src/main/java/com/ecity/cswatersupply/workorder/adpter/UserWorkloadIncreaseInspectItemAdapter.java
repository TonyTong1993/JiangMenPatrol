package com.ecity.cswatersupply.workorder.adpter;

import android.app.Activity;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.workorder.model.UserWorkload;

import java.util.List;

public class UserWorkloadIncreaseInspectItemAdapter extends AIncreaseInspectItemAdapter<UserWorkload>  {
    private List<List<UserWorkload>> mMaterialDetailInfos;

    public UserWorkloadIncreaseInspectItemAdapter(Activity activity, InspectItem parentItem) {
        super(activity, parentItem);
    }

    @Override
    protected void deselectItem(int position) {

    }

    @Override
    protected int getItemMinCount() {
        return 1;
    }
}
