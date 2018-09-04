package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.project.model.StatusCountModel;

import java.util.List;

public class GetProjectWaterMeterCountResponse extends AServerResponse {
    private String totalCount;
    private List<StatusCountModel> statusCount;
    private StatusCountModel overdueCount;

    public String getTotalCount() {
        return totalCount;
    }

    public List<StatusCountModel> getStatusCount() {
        return statusCount;
    }

    public StatusCountModel getOverdueCount() {
        return overdueCount;
    }
}
