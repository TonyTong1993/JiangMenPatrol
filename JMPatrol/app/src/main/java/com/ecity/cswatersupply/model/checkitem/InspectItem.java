package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;
import java.util.List;

import com.z3app.android.util.StringUtil;

public class InspectItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean required;
    private boolean visible;
    private boolean edit;
    private boolean increase;
    private EInspectItemType type;
    private String name;
    private String alias;
    private String value;
    private boolean isLongText;
    private String defaultValue;
    private String selectValues;
    private List<InspectItem> childs;
    /**
     * Part的类型。0：点，1：线，2：区。
     */
    private int geoType;

    /****
     * 用于福州外勤本地控制两个检查项的联动
     * groupName 检查项分组的组名
     * linkName  联动检查项的name
     * linkAlias 联动检查项的别名
     */
    private String groupName;
    private String linkName;
    private String linkAlias;

    /***
     * 两个DDL类型检查项的联动标志
     * cascadeGroupName 联动分组名
     * cascadeGroupSn  标识联动项是父类还是子类
     */
    private String cascadeGroupName;
    private String cascadeGroupSn;

    private boolean imageEditFlag;//用来标识武汉地震中图片的编辑状态

    public InspectItem() {
        required = false;
        increase = false;
        visible = false;
        type = EInspectItemType.valueOf("TEXT");
        name = null;
        alias = null;
        defaultValue = null;
        value = defaultValue;
        selectValues = null;
        childs = null;
    }

    public InspectItem(boolean visible, boolean required, EInspectItemType type, String name, String alias, String value, String defaultValue, String selectValues) {
        this.required = required;
        this.visible = visible;
        this.type = type;
        this.name = name;
        this.alias = alias;
        this.value = value;
        this.defaultValue = defaultValue;
        this.selectValues = selectValues;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    public boolean isIncrease() {
        return increase;
    }

    public void setIncrease(boolean increase) {
        this.increase = increase;
    }

    /**
     * 类型
     * 
     * @return
     */
    public EInspectItemType getType() {
        return type;
    }

    public void setType(EInspectItemType type) {
        this.type = type;
    }

    /***
     * 名称
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 别名
     * 
     * @return
     */
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
     * 是否是长文本 1是0否
     * 
     * @return
     */
    public boolean isLongText() {
        return isLongText;
    }

    public void setLongText(boolean isLongText) {
        this.isLongText = isLongText;
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

    public List<InspectItem> getChilds() {
        return childs;
    }

    public void setChilds(List<InspectItem> childs) {
        this.childs = childs;
    }

    public int getGeoType() {
        return geoType;
    }

    public void setGeoType(int geoType) {
        this.geoType = geoType;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkAlias() {
        return linkAlias;
    }

    public void setLinkAlias(String linkAlias) {
        this.linkAlias = linkAlias;
    }

    public String getCascadeGroupName() {
        return cascadeGroupName;
    }

    public void setCascadeGroupName(String cascadeGroupName) {
        this.cascadeGroupName = cascadeGroupName;
    }

    public String getCascadeGroupSn() {
        return cascadeGroupSn;
    }

    public void setCascadeGroupSn(String cascadeGroupSn) {
        this.cascadeGroupSn = cascadeGroupSn;
    }

    public boolean buildFromJSON(String json) {
        boolean flag = false;
        if (StringUtil.isEmpty(json)) {
            return flag;
        }

        return flag;
    }

    public boolean isImageEditFlag() {
        return imageEditFlag;
    }

    public void setImageEditFlag(boolean imageEditFlag) {
        this.imageEditFlag = imageEditFlag;
    }
}
