package com.zzz.ecity.android.applibrary.model;

import java.io.Serializable;

/**
 * 用于PropertyChangeListener 因为有时候Value要结合tag才能识别更新对象
 * 比如九宫格的工单更新，必须找到工单处置，只更新它的气泡显示 而不能把计划任务的也更新了
 *
 * @author gaokai
 */
public class PropertyChangeModel implements Serializable {
    public PropertyChangeModel() {
    }

    public PropertyChangeModel(String key, int value) {
        this.key = key;
        this.value = value;
    }

    private static final long serialVersionUID = 1L;
    public String key;
    public Integer value;

    @Override
    public boolean equals(Object other) {
        // 先检查是否其自反性，后比较other是否为空。这样效率高
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof PropertyChangeModel)) {
            return false;
        }

        final PropertyChangeModel propertyChangeModel = (PropertyChangeModel) other;

        if (!key.equals(propertyChangeModel.key)) {
            return false;
        }
        return value.equals(propertyChangeModel.value);
    }

    @Override
    public int hashCode() {// hashCode主要是用来提高hash系统的查询效率。当hashCode中不进行任何操作时，可以直接让其返回
        // 一常数，或者不进行重写。
        int result = key.hashCode();
        result = 29 * result + value.hashCode();
        return result;
    }

}
