package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ProjectFzrStastistisResponse extends AServerResponse {
    private List<ProjectFZRStastisticsModel> features;

    public List<ProjectFZRStastisticsModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<ProjectFZRStastisticsModel> features) {
        this.features = features;
    }
}
