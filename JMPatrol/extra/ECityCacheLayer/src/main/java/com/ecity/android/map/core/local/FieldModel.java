package com.ecity.android.map.core.local;

public class FieldModel {
    /*
     * =================================== 部分地理数据默认字段名称
     * ===================================
     */
    /***
     * 上点号
     */
    final public static String STARTPOINT = "stnod";
    /***
     * 本点号
     */
    final public static String ENDPOINT = "ednod";
    /***
     * 点号,记录号
     */
    final public static String GID = "gid";
    /***
     * x坐标
     */
    final public static String POS_X = "x";
    /***
     * y坐标
     */
    final public static String POS_Y = "y";

    final public static String POS_X2 = "横座标";

    final public static String POS_Y2 = "纵座标";

    final public static String LINE_LENGTH = "长度";

    // 字段名
    private String name;
    // 字段类型
    private String type;
    // 字段别名
    private String alias;
    // 字段长度
    private int length;
    // 默认值
    private String defaultvalue;
    // 可选值
    private String SelValue;

    /***
     * 字段名
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /***
     * 字段类型
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /***
     * 字段别名
     * 
     * @return
     */
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    /***
     * 字段长度
     * 
     * @return
     */
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /***
     * 默认值
     * 
     * @return
     */
    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    /***
     * 可选值
     * 
     * @return
     */
    public String getSelValue() {
        return SelValue;
    }

    public void setSelValue(String SelValue) {
        this.SelValue = SelValue;
    }

    @Override
    public String toString() {
        return "Field [name=" + name + ", type=" + type + ", alias=" + alias
                + ", length=" + length + ", defaultvalue=" + defaultvalue
                + ", SelValue=" + SelValue + "]";
    }

}