package com.ecity.cswatersupply.project.network.response.adapter;

import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.project.network.response.ProjectAndGeoModel;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GetProjectForMapResponse extends AServerResponse {
    private List<ProjectAndGeoModel> features;

    public List<ProjectAndGeoModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<ProjectAndGeoModel> features) {
        this.features = features;
    }
}
