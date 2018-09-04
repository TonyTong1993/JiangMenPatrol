package com.ecity.cswatersupply.workorder.network;

import java.util.List;

import com.ecity.android.contactmanchooser.model.ContactMan;
import com.ecity.cswatersupply.network.response.AServerResponse;

public class ContactManResponse extends AServerResponse {
    private List<ContactMan> patrolManList;

    public List<ContactMan> getPatrolManList() {
        return patrolManList;
    }

    public void setPatrolManList(List<ContactMan> patrolManList) {
        this.patrolManList = patrolManList;
    }

}