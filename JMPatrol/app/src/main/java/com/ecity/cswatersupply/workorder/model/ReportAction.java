package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.event.UIEventStatus;

public enum ReportAction {
    RETURN (-1),
    ACCEPT(-1),
    DELAYAPPLY(UIEventStatus.WORKORDER_DELAYAPPLY),//value的作用：延期、进度、完工这几个按钮，用到了customReportActivity，
                                                          //业务逻辑在Operator里，需要与WorkOrdersActivity通信
    POCESS(UIEventStatus.WORKORDER_POCESS),
    ASKFINISH(UIEventStatus.WORKORDER_ASKFINISH);
    
    int value;
    ReportAction(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static ReportAction getReportActionByValue(int serviceAction) {
        for (ReportAction r : values()) {
            if(r.getValue() == serviceAction){
                return r;
            }
        }
        return null;
    }
}
