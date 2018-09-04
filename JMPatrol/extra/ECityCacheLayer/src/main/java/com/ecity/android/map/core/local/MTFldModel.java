/**   
 * 文件名：MTFldModel.java   
 * 包名:com.ecity.android.cityrevisionpo.data
 * @Author:wangliu94@163.com
 * @Description:TODO
 * 版本信息：V1.0 
 * 日期：2014-7-1   
 * Copyright Ecity(Wuhan) Corporation 2014    
 * 版权所有   
 *   
 */

package com.ecity.android.map.core.local;

/**
 * @类名：MTFldModel
 * @description:
 * @author : wangliu94@163.com
 * @version : 2014-7-1 下午08:30:54
 */

public class MTFldModel {
    private String name;
    private String alias;
    // 1 TextBox 2 日期 3 下拉框
    private int disptype;

    private String defval;
    private String fldval;// 提供的选项
    private String prop;
    private int visible;
    private int editable;

    /**
     * name
     * 
     * @return the name
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * alias
     * 
     * @return the alias
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */

    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * disptype
     * 
     * @return the disptype
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getDisptype() {
        return disptype;
    }

    /**
     * @param disptype
     *            the disptype to set
     */

    public void setDisptype(int disptype) {
        this.disptype = disptype;
    }

    /**
     * defval
     * 
     * @return the defval
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getDefval() {
        return defval;
    }

    /**
     * @param defval
     *            the defval to set
     */

    public void setDefval(String defval) {
        this.defval = defval;
    }

    /**
     * fldval
     * 
     * @return the fldval
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getFldval() {
        return fldval;
    }

    /**
     * @param fldval
     *            the fldval to set
     */

    public void setFldval(String fldval) {
        this.fldval = fldval;
    }

    /**
     * prop
     * 
     * @return the prop
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getProp() {
        return prop;
    }

    /**
     * @param prop
     *            the prop to set
     */

    public void setProp(String prop) {
        this.prop = prop;
    }

    /**
     * visible
     * 
     * @return the visible
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getVisible() {
        return visible;
    }

    /**
     * @param visible
     *            the visible to set
     */

    public void setVisible(int visible) {
        this.visible = visible;
    }

    /**
     * editable
     * 
     * @return the editable
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getEditable() {
        return editable;
    }

    /**
     * @param editable
     *            the editable to set
     */

    public void setEditable(int editable) {
        this.editable = editable;
    }

}
