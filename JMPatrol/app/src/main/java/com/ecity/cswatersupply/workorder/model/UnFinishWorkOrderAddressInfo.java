package com.ecity.cswatersupply.workorder.model;


import com.ecity.android.db.utils.StringUtil;

public class UnFinishWorkOrderAddressInfo {
    private String addresses;
    private boolean isHost;

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getAddresses() {
        return addresses;
    }

}