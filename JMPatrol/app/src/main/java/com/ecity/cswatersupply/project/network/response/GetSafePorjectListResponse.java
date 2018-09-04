package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GetSafePorjectListResponse extends AServerResponse {
    private List<SafeProjectListModel> features;
    private String count;

    public List<SafeProjectListModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<SafeProjectListModel> features) {
        this.features = features;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
