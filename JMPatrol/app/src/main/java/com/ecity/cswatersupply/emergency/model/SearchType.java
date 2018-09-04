package com.ecity.cswatersupply.emergency.model;

/***
 * the type of search
 * EARTH_QUAKE 地震信息查询
 * QUICK_REPORT 速报信息查询
 * IMPORT_EARTH_QUAKE 重要地震信息查询
 * @author Administrator
 */
public enum SearchType {
    EARTH_QUAKE(1),
    QUICK_REPORT(2),
    IMPORT_EARTH_QUAKE(3);

    private int value;

    SearchType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SearchType valueOf(int eventType) {
        SearchType[] values = SearchType.values();
        for (SearchType value : values) {
            if (value.value == eventType) {
                return value;
            }
        }

        return null;
    }
}
