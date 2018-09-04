package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GetSafeDetailStepResponse extends AServerResponse {
    private List<SafeDetailStepModel> features;

    public List<SafeDetailStepModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<SafeDetailStepModel> features) {
        this.features = features;
    }
}
