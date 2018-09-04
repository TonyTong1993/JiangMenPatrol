package com.ecity.cswatersupply.menu.map;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.menu.ACommonAttrListViewOperator;
import com.ecity.cswatersupply.ui.activities.planningtask.TaskAttrListsActivity;

public class DefaultAttrListViewOperator extends ACommonAttrListViewOperator{

    public List<Object> getDataSource() {
        return new ArrayList<Object>();
    }

    public void notifyBackEvent(TaskAttrListsActivity activity) {
        if (null != activity) {
            activity.finish();
        }
    }
}
