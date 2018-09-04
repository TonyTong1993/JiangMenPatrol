package com.ecity.cswatersupply.workorder.menu;

import java.lang.ref.WeakReference;
import java.util.List;

import android.os.Bundle;

import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.workorder.WorkOrderFinishActivity;

public abstract class AWorkOrderFinishCommonOperator {
    private WeakReference<WorkOrderFinishActivity> mActivity;

    public final void setCustomActivity(WorkOrderFinishActivity activity) {
        mActivity = new WeakReference<WorkOrderFinishActivity>(activity);
    }

    public WorkOrderFinishActivity getActivity() {
        if (null == mActivity) {
            return null;
        }
        WorkOrderFinishActivity activity = mActivity.get();
        return activity;
    }

    public List<InspectItem> getDataSource() {
        return null;
    }

    public void submit2Server(List<InspectItem> datas) {

    }

    public void notifyBackEvent(WorkOrderFinishActivity activity) {
        
    }
    
    /**
     * 自定义实现点击事件
     * @param itemName 区分item的key
     * @param data 回调数据包
     */
    public void onItemClicked(String itemName, EInspectItemType eInspectItemType, Bundle data) {
        
    }
}
