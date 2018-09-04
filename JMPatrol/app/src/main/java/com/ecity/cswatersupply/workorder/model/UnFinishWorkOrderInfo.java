package com.ecity.cswatersupply.workorder.model;


import com.ecity.android.db.utils.StringUtil;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.List;

public class UnFinishWorkOrderInfo {
    private String patrolName;
    private String todayWorkOrderNumber;
    private String waitProcessWorkOrderNumber;
    private List<UnFinishWorkOrderAddressInfo> addressInfoList;

    public String getPatrolName() {
        return patrolName;
    }

    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getTodayWorkOrderNumber() {
        return todayWorkOrderNumber;
    }

    public void setTodayWorkOrderNumber(String todayWorkOrderNumber) {
        this.todayWorkOrderNumber = todayWorkOrderNumber;
    }

    public String getWaitProcessWorkOrderNumber() {
        return waitProcessWorkOrderNumber;
    }

    public void setWaitProcessWorkOrderNumber(String waitProcessWorkOrderNumber) {
        this.waitProcessWorkOrderNumber = waitProcessWorkOrderNumber;
    }

    public List getAddressInfoList() {
        return addressInfoList;
    }

    public void setAddressInfoList(List<UnFinishWorkOrderAddressInfo> addressInfoList) {
        this.addressInfoList = addressInfoList;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(ListUtil.isEmpty(addressInfoList)) {
            return sb.toString();
        }
        int size = addressInfoList.size();
        for(int i = 0; i < size; i++ ) {
            UnFinishWorkOrderAddressInfo info = addressInfoList.get(i);

            if(info.isHost()) {
                sb.append("! ");
            }
            sb.append(info.getAddresses()).append("\n");
        }

        return sb.toString().trim();
    }

}