package com.ecity.cswatersupply.model.event;

public enum EventType {
    REPAIRE(0),
    POINT_LEAK(1),
    PUNISHMENT(2),
    CONSTRUCTION(3),
    PUMP(4),
    MEASURE(5),
    PATROL(6),
    GIS(30);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EventType valueOf(int eventType) {
        EventType[] values = EventType.values();
        for (EventType value : values) {
            if (value.value == eventType) {
                return value;
            }
        }

        return null;
    }
}
