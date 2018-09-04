package com.ecity.cswatersupply.model.event;

import java.util.Map;

public class ConstructionEvent extends Event {
    private static final long serialVersionUID = -3393456062159888943L;
    public static final String ATTR_KEY_CONSTRUCTION_DEPARMENT = "shigongdanwei";
    public static final String ATTR_KEY_NAME = "gongchengmingcheng";
    public static final String ATTR_KEY_BULID_DEPARMENT = "jianshedanwei";
    public static final String ATTR_KEY_BAOGUAN = "shifoubaoguan";
    public static final String ATTR_KEY_MASSAGER = "xiangmujingli";
    public static final String ATTR_KEY_CONTACTS = "lianxifangshi";

    public ConstructionEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public EventType getType() {
        return EventType.CONSTRUCTION;
    }

    @Override
    public String getId() {
        return getAttribute(ATTR_KEY_ID);
    }

    @Override
    public String getReportTime() {
        return null;
    }
}
