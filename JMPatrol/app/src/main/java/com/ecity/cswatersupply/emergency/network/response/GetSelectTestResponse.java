package com.ecity.cswatersupply.emergency.network.response;

import com.ecity.cswatersupply.network.response.AServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class GetSelectTestResponse extends AServerResponse {
    private List<SelectModel> resultList;

    public void setResultList(List<SelectModel> resultList) {
        this.resultList = resultList;
    }

    public List<SelectModel> getResultList() {
        return resultList;
    }
}
