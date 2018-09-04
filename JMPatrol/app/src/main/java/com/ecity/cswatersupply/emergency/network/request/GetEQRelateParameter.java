package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetEQRelateParameter implements IRequestParameter {
    private EarthQuakeQuickReportModel reportModel;
    private EarthQuakeInfoModel localModel;

    public GetEQRelateParameter(EarthQuakeQuickReportModel reportModel, EarthQuakeInfoModel localModel) {
        this.reportModel = reportModel;
        this.localModel = localModel;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("gid", String.valueOf(reportModel.getGid()));
        map.put("eqid", String.valueOf(localModel.getId()));
        return map;
    }
}
