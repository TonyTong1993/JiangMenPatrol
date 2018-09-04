package com.ecity.cswatersupply.workorder.network;

/**
 * Created by hxx on 2017/3/15.
 */

public class WorkorderLeaderModel {
    private String wxry; //维修人员
    private String zds; //总单数
    private String jdd;//接待单
    private String clz;//处理中
    private String ywj;//已完结
    private String yq;//延期
    private String cq;//超期
    private String zd;//转单
    private String td;//退单
    private String gs;//工时

    public String getWxry() {
        return wxry;
    }

    public void setWxry(String wxry) {
        this.wxry = wxry;
    }

    public String getZds() {
        return zds;
    }

    public void setZds(String zds) {
        this.zds = zds;
    }

    public String getJdd() {
        return jdd;
    }

    public void setJdd(String jdd) {
        this.jdd = jdd;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public String getYwj() {
        return ywj;
    }

    public void setYwj(String ywj) {
        this.ywj = ywj;
    }

    public String getYq() {
        return yq;
    }

    public void setYq(String yq) {
        this.yq = yq;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getZd() {
        return zd;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }

    public String getGs() {
        return gs;
    }

    public void setGs(String gs) {
        this.gs = gs;
    }
}
