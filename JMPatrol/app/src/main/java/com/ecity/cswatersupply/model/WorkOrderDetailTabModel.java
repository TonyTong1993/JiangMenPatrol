package com.ecity.cswatersupply.model;

/***
 * 工单详情tab项模型
 * @author Gxx 2017-04-06
 *
 */
public class WorkOrderDetailTabModel extends AModel {
    private static final long serialVersionUID = 1L;
    private String alias;
    private String name;
    private String groupid;
    private String url;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
