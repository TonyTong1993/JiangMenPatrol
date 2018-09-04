package com.ecity.cswatersupply.project.network.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/8.
 */

// "gid": "1",
//         "code": "丑的",
//         "name": "吉喜林项目测试1",
//         "type": "1",
//         "classify": "2",
//         "startdate": "2016-12-01 18:04:48",
//         "enddate": "2016-12-01 18:04:51",
//         "area": "2",
//         "address": "新奥大学",
//         "memo": "么末",
//         "geom": "",
//         "process": "5",
//         "procost": "23.00",
//         "createtime": "2016-11-28 15:06:11",
//         "finishrate": "",
//         "cnuserid": "2",
//         "cnusername": "大厦的",
//         "finishtime": "2016-12-15 15:53:03",
//         "processtime": "",
//         "kgtime": "2016-12-12 19:29:50",
//         "buildself": "1",
//         "ispre": "0",
//         "ishide": "0",
//         "designo": "12",
//         "sgfzrid": "",
//         "sgfzr": "",
//         "jsdwname": "",
//         "total": "3",
//         "doing": "1",
//         "doing_total": "1/3"

public class SafeProjectListModel implements Serializable{
    private String gid;
    private String code;
    private String name;
    private String type;
    private String classify;
    private String startdate;
    private String enddate;
    private String area;
    private String address;
    private String memo;
    private String geom;
    private String process;
    private String procost;
    private String createtime;
    private String finishrate;
    private String cnuserid;
    private String cnusername;
    private String finishtime;
    private String processtime;
    private String kgtime;
    private String buildself;
    private String ispre;
    private String ishide;
    private String designo;
    private String sgfzrid;
    private String sgfzr;
    private String jsdwname;
    private String total;
    private String doing;
    private String doing_total;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcost() {
        return procost;
    }

    public void setProcost(String procost) {
        this.procost = procost;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFinishrate() {
        return finishrate;
    }

    public void setFinishrate(String finishrate) {
        this.finishrate = finishrate;
    }

    public String getCnuserid() {
        return cnuserid;
    }

    public void setCnuserid(String cnuserid) {
        this.cnuserid = cnuserid;
    }

    public String getCnusername() {
        return cnusername;
    }

    public void setCnusername(String cnusername) {
        this.cnusername = cnusername;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getProcesstime() {
        return processtime;
    }

    public void setProcesstime(String processtime) {
        this.processtime = processtime;
    }

    public String getKgtime() {
        return kgtime;
    }

    public void setKgtime(String kgtime) {
        this.kgtime = kgtime;
    }

    public String getBuildself() {
        return buildself;
    }

    public void setBuildself(String buildself) {
        this.buildself = buildself;
    }

    public String getIspre() {
        return ispre;
    }

    public void setIspre(String ispre) {
        this.ispre = ispre;
    }

    public String getIshide() {
        return ishide;
    }

    public void setIshide(String ishide) {
        this.ishide = ishide;
    }

    public String getDesigno() {
        return designo;
    }

    public void setDesigno(String designo) {
        this.designo = designo;
    }

    public String getSgfzrid() {
        return sgfzrid;
    }

    public void setSgfzrid(String sgfzrid) {
        this.sgfzrid = sgfzrid;
    }

    public String getSgfzr() {
        return sgfzr;
    }

    public void setSgfzr(String sgfzr) {
        this.sgfzr = sgfzr;
    }

    public String getJsdwname() {
        return jsdwname;
    }

    public void setJsdwname(String jsdwname) {
        this.jsdwname = jsdwname;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDoing() {
        return doing;
    }

    public void setDoing(String doing) {
        this.doing = doing;
    }

    public String getDoing_total() {
        return doing_total;
    }

    public void setDoing_total(String doing_total) {
        this.doing_total = doing_total;
    }
}
