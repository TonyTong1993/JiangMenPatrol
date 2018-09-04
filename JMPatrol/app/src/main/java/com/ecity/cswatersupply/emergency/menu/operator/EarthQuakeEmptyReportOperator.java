package com.ecity.cswatersupply.emergency.menu.operator;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;

public class EarthQuakeEmptyReportOperator extends ACommonReportOperator1 {
    @Override
    public List<InspectItem> getDataSource() {
        return new ArrayList<InspectItem>();
    }

    @Override
    public void submit2Server(List<InspectItem> datas) {

    }

    @Override
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            activity.finish();
        }
    }
}
