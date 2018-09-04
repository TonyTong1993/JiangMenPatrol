package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.project.model.FileModel;

import java.util.List;

public class GetProjectAttachmentResponse extends AServerResponse {
    private List<FileModel> features;
    private String url;

    public List<FileModel> getFeatures() {
        return features;
    }

    public String getUrl() {
        return url;
    }
}
