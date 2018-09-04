package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GetSafeEventListResponse extends AServerResponse {
    private List<SafeEventListModel> features;

    public List<SafeEventListModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<SafeEventListModel> features) {
        this.features = features;
    }
}
