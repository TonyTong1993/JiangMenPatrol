package com.ecity.cswatersupply.menu.map;

import java.util.List;

import android.content.Intent;

import com.ecity.cswatersupply.menu.ACommonAttrListViewOperator;
import com.ecity.cswatersupply.ui.activities.planningtask.TaskAttrListsActivity;

public class PointPartAttrListViewOperator extends ACommonAttrListViewOperator{
    public static final String ATTRS_LIST = "ATTRS_LIST";

    @SuppressWarnings("unchecked")
    public List<Object> getDataSource() {
        Intent intent = getActivity().getIntent();
        return  (List<Object>) intent.getExtras().getSerializable(ATTRS_LIST);
    }

    public void notifyBackEvent(TaskAttrListsActivity activity) {
        if (null != activity) {
            activity.finish();
        }
    }
}
