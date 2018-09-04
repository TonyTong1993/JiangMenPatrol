package com.ecity.cswatersupply.model.checkitem;

public enum EInspectItemType {
    TEXT("TXT"), /* 文本 */
    NUMBER("NUM"), /*数字*/
    TEXTEXT("TXTEXT"), /* 长文本 */
    DATE("DATE"), /* 时间 */
    GEOMETRY("GEOM"), /* 几何 */
    GEOMETRY_AREA("GEOMAREA"), /* 地图区域 */
    DEVICE("DEV"), /* 设备 */
    SELECTVALVE("VALVE"), /* 阀门 */
    IMAGE("IMG"), /* 图片 */
    VIDEO("VDO"), /* 视频 */
    AUDIO("ADO"), /* 音频 */
    RADIO("RDO"), /* 单选项 */
    RADIOTXT("RDOTXT"), /* 单选项附文本 */
    CHECKBOX("CHK"), /* 复选项 */
    DROPDOWNLIST("DDL"), /* 下拉项 */
    DROPDOWNLIST_NOT_TOLERANT("DDLNT"), /* 下拉项 无默认 */
    DROPDOWNLISTEXT("DDLEXT"), /* 可选可填的下拉项 */
    ATTACHMENT("ATT"), /* 附件 */
    ATTACHMENT_UPLOAD("ATTUP"), /* 附件上传 */
    QRCODE("QR"), /* 二维码 */
    GROUP("GROUP"), /* 可分组 */
    CONTACTMEN_SINGLE("CONTACTS"), /* 单选联系人 */
    CONTACTMEN_MULTIPLE("CONTACTM"), /* 多选联系人 */
    CONTACTMEN_SINGLE_PROJECT("CONTACTS1"), /* 单选联系人-江门工程 */
    CONTACTMEN_MULTIPLE_PROJECT("CONTACTM1"), /* 多选联系人-江门工程 */
    DIVIDER_THICK("DIVIDERTHICK"), /* 粗间隔，20dip */
    TEXT_READ("TXT_READ"), /* 只读文本 */
    WORKORDER_CODE("CODE"), /* 工单编号类型 */
    PHONE("PHONE"), /* 联系电话 */
    TREE("TREE"), /*树型控件*/
    TOGGLE("TOGGLE"), /*二选一控件*/
    SECTION_TITLE("SECTION_TITLE"),/*分组标题的类型*/
    NAMEGEOM("NAMEGEOM"),/*选择泵房类型*/
    CORS("CORS"),/*Cors坐标信息*/
    LINKDROPDOWNLIST("LDDL"), /*下拉项与文本联动,本地配置*/
    MATERIAL("MATERIAL"), /*完工上报，材料类型*/
    TABLE("TABLE"), /*完工上报，人员类型*/
    TAB("TAB"),/*完工上报，tab展示标记*/
    ORG("ORG"),/*组织结构*/
    ORGM("ORGM");/*组织结构*/

    private final String value;

    public String getValue() {
        return value;
    }

    EInspectItemType(String value) {
        this.value = value.toUpperCase();
    }

    public static EInspectItemType getTypeByValue(String type) {
        for (EInspectItemType e : values()) {
            if (e.getValue().equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }
}
