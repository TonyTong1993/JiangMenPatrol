package com.ecity.cswatersupply.menu;

import java.lang.ref.WeakReference;

import com.ecity.cswatersupply.ui.activities.planningtask.TaskAttrListsActivity;

public abstract class ACommonAttrListViewOperator {
    private WeakReference<TaskAttrListsActivity> mActivity;

    public final void setCustomActivity(TaskAttrListsActivity activity) {
        mActivity = new WeakReference<TaskAttrListsActivity>(activity);
    }

    public TaskAttrListsActivity getActivity() {
        if (null == mActivity) {
            return null;
        }
        TaskAttrListsActivity activity = mActivity.get();
        return activity;
    }

    public Object getDataSource() {
        return null;
    }

    public void notifyBackEvent(TaskAttrListsActivity activity) {

    }
}
