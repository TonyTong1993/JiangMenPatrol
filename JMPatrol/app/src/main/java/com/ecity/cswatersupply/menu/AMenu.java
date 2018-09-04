package com.ecity.cswatersupply.menu;

import java.io.Serializable;

import com.ecity.cswatersupply.utils.GsonUtil;
import com.google.gson.annotations.SerializedName;

/**
 * 菜单基类，只用于显示
 * 
 * @author gaokai
 *
 */
public abstract class AMenu implements Serializable ,Cloneable{
    public enum EMenuType{
        NATIVE("NATIVE"), /*本地菜单*/
        WEBURL("WEBURL"); /*网页菜单*/
       
        private final String value;

        public String getValue() {
            return value;
        }

        EMenuType(String value) {
            this.value = value.toUpperCase();
        }

        public static EMenuType getTypeByValue(String type) {
            for (EMenuType e : values()) {
                if(e.getValue().equalsIgnoreCase(type)){
                    return e;
                }
            }
            return null;
        }
    }
    /**
     * 
     */
    private static final long serialVersionUID = 1L; 
    @SerializedName("id")
    protected String gid;                   //唯一编号
    @SerializedName("username")
    protected String iconName;              //菜单图标名称
    @SerializedName("imgUrl")
    protected String iconURL;               //菜单图标URL

    protected EMenuType type;               //菜单类型：本地固定菜单或者远程动态URL菜单
    
    private String configUrl;
    
    @SerializedName("pageUrl")
    protected String url;                   //远程动态URL菜单对应的URL地址
    @SerializedName("nodeName")
    protected String name;                  //菜单名称
    @SerializedName("orderID")
    protected String description;           //菜单描述
   
    protected int iconId;                   //菜单资源id
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public EMenuType getType() {
        return type;
    }

    public void setType(EMenuType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = EMenuType.getTypeByValue(type);
    }
    
    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }
    public String getConfigUrl() {
        return configUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
