package com.ecity.cswatersupply.project.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;

public class GetProjectTypeCountResponse extends AServerResponse {
    private String notStarted;
    private String ongoing;
    private String finished;
    private String delay;

    public String getNotStarted() {
        return notStarted;
    }

    public String getOngoing() {
        return ongoing;
    }

    public String getFinished() {
        return finished;
    }

    public String getDelay() {
        return delay;
    }
}
