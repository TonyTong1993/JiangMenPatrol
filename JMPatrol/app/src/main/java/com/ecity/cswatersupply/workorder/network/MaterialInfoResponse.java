package com.ecity.cswatersupply.workorder.network;

import java.util.List;

import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.workorder.model.MaterialServerInfo;
import com.google.gson.annotations.SerializedName;

public class MaterialInfoResponse extends AServerResponse {
    @SerializedName("features")
    private List<MaterialServerInfo> materialServerInfos;

    public List<MaterialServerInfo> getMaterialServerInfos() {
        return materialServerInfos;
    }
}
