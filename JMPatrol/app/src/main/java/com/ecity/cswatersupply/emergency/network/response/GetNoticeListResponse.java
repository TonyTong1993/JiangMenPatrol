package com.ecity.cswatersupply.emergency.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.xg.model.Notification;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class GetNoticeListResponse extends AServerResponse{
    private String count;
    private List<NoticeModel> features;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<NoticeModel> getFeatures() {
        return features;
    }

    public void setFeatures(List<NoticeModel> features) {
        this.features = features;
    }
}
