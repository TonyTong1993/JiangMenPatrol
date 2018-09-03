package com.zzz.ecity.android.applibrary.model;

public enum EPositionMessageType {
    RECEIVENEWLOCATION("RECEIVENEWLOCATION"), /* 接收到新坐标 */
    FILTERNEWLOCATION("FILTERNEWLOCATION"), /* 筛选新坐标 */
    POSITIONREPORTSUCCESS("POSITIONREPORTSUCCESS"),/*坐标上报成功*/
    POSITIONREPORTFAIL("POSITIONREPORTFAIL"),/*坐标上报失败*/
    POSITIONBEANBUILDERNOTFOUND("POSITIONBEANBUILDERNOTFOUND"),/*坐标构建器未设置*/
    POSITIONBEANSAVEFAIL("POSITIONBEANSAVEFAIL"),/*轨迹存储失败*/
    BUILDPOSITIONBEANERROR("BUILDPOSITIONBEANERROR");/*创建轨迹点失败*/

    private final String value;

    public String getValue() {
        return value;
    }

    EPositionMessageType(String value) {
        this.value = value.toUpperCase();
    }

    public static EPositionMessageType getTypeByValue(String type) {
        for (EPositionMessageType e : values()) {
            if (e.getValue().equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }
}