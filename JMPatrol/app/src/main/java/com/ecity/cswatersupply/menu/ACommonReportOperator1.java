package com.ecity.cswatersupply.menu;

import java.lang.ref.WeakReference;
import java.util.List;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;

public abstract class ACommonReportOperator1 {
    private WeakReference<CustomReportActivity1> mActivity;

    public final void setCustomActivity(CustomReportActivity1 activity) {
        mActivity = new WeakReference<CustomReportActivity1>(activity);
    }

    public CustomReportActivity1 getActivity() {
        if (null == mActivity) {
            return null;
        }
        CustomReportActivity1 activity = mActivity.get();
        return activity;
    }

    public List<InspectItem> getDataSource() {
        return null;
    }

    public void submit2Server(List<InspectItem> datas) {

    }

    public void submit2Server(List<InspectItem> datas, boolean isAgree) {

    }

    public void notifyBackEvent(CustomReportActivity1 activity) {

    }

    public void notifyBackEventWhenFinishReport(CustomReportActivity1 activity) {

    }
}
