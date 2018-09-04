package com.ecity.cswatersupply.emergency.model;

/**
 * 速报总览数据模型
 */
public class QBOModel {
    //地震id
    private String eqid;
    //死亡人数
    private String pdead;
    //受伤人数
    private String pss;
    //受灾人数
    private String ptotal;
    //地震烈度
    private String liedu;
    //房屋破坏
    private String bnum;
    //财产损失
    private String money;
    public String getEqid() {
        return eqid;
    }
    public void setEqid(String eqid) {
        this.eqid = eqid;
    }
    public String getPdead() {
        return pdead;
    }
    public void setPdead(String pdead) {
        this.pdead = pdead;
    }
    public String getPss() {
        return pss;
    }
    public void setPss(String pss) {
        this.pss = pss;
    }
    public String getPtotal() {
        return ptotal;
    }
    public void setPtotal(String ptotal) {
        this.ptotal = ptotal;
    }
    public String getLiedu() {
        return liedu;
    }
    public void setLiedu(String liedu) {
        this.liedu = liedu;
    }
    public String getBnum() {
        return bnum;
    }
    public void setBnum(String bnum) {
        this.bnum = bnum;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }


}
