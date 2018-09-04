package com.ecity.cswatersupply.emergency.network.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetEarthQuakeQuickReportInfosParameter implements IRequestParameter, Serializable {

    private static final long serialVersionUID = 1L;
    private List<InspectItem> items;
    private String reporter;
    private String eqid;
    private EarthQuakeInfoModel clickModel;

    public GetEarthQuakeQuickReportInfosParameter() {
    }

    public GetEarthQuakeQuickReportInfosParameter(List<InspectItem> items, EarthQuakeInfoModel clickModel) {
        this.items = items;
        this.clickModel = clickModel;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (null == items) {
            map.put("reporterid", "");
            if (clickModel == null) {
                map.put("eqid", "");
            } else {
                map.put("eqid", String.valueOf(clickModel.getId()));
            }
        } else {
            getSearchParams();
            map.put("reporterid", reporter);
            map.put("eqid", eqid);
        }
        return map;
    }

    private void getSearchParams() {
        for (InspectItem inspectItem : items) {
            if ("shangbaoren".equals(inspectItem.getName())) {
                reporter = inspectItem.getValue();
            } else if ("eqid".equals(inspectItem.getName())) {
                eqid = inspectItem.getValue();
            }
        }
    }
}
