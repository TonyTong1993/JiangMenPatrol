package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;

import com.z3app.android.util.StringUtil;

public class DateItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;
    private String defaultValue;
    private String selectValues;


    public DateItem() {
        defaultValue = null;
        value = defaultValue;
        selectValues = null;
    }

    public DateItem( String value, String defaultValue, String selectValues) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.selectValues = selectValues;
    }

    /**
     * 默认等于defaultValue
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    /**
     * 默认值 可以为空
     * 
     * @return
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 可选值 可以为空
     * 
     * @return
     */
    public String getSelectValues() {
        return selectValues;
    }

    public void setSelectValues(String selectValues) {
        this.selectValues = selectValues;
    }

    public boolean buildFromJSON(String json) {
        boolean flag = false;
        if (StringUtil.isEmpty(json)) {
            return flag;
        }

        return flag;
    }
}
