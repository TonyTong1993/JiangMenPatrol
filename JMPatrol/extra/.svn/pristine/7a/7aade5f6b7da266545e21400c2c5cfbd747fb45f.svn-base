package com.ecity.android.map.core.local;

import java.util.ArrayList;
import java.util.List;

public class TableModel {
    // 编号类型
    private int noType;
    // 表名
    private String name;
    // 存储数据类型
    private String type;
    // 别名
    private String alias;
    // 字段列表
    private List<FieldModel> fields = new ArrayList<FieldModel>();

    public int getNoType() {
        return noType;
    }

    public void setNoType(int noType) {
        this.noType = noType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }

    public void addField(FieldModel field) {
        if (fields != null)
            fields.add(field);
    }

    public List<FieldModel> getFields() {
        return fields;
    }

    // 判断是否包含某个字段
    public boolean isContainField(String fieldname) {
        boolean flg = false;
        if (fields == null)
            return flg;
        for (int i = 0; i < fields.size(); i++) {
            if (fieldname.equalsIgnoreCase(fields.get(i).getName())) {
                flg = true;
                break;
            }
        }
        return flg;
    }

    @Override
    public String toString() {
        return "TableModel [noType=" + noType + ", name=" + name + ", type="
                + type + ", alias=" + alias + "]";
    }
}