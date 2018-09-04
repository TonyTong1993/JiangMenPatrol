package com.ecity.cswatersupply.project.network.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/24.
 */

// "gid": "27",
//         "reporter": "安全办",
//         "source": "2",
//         "violation": "abc",
//         "position": "abc",
//         "evetype": "1",
//         "evedes": "abc",
//         "guid": "af1ccefd-8d08-4621-a3a2-ac1da6e59c5f",
//         "step": "2",
//         "status": "0",
//         "createtime": "2017-05-17 17:29:09",
//         "proid": "1",
//         "geom": ""

public class SafeEventListModel implements Serializable {
    private String gid;
    private String reporter;
    private String source;
    private String violation;
    private String position;
    private String evetype;
    private String evedes;
    private String guid;
    private String step;
    private String status;
    private String createtime;
    private String proid;
    private String geom;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEvetype() {
        return evetype;
    }

    public void setEvetype(String evetype) {
        this.evetype = evetype;
    }

    public String getEvedes() {
        return evedes;
    }

    public void setEvedes(String evedes) {
        this.evedes = evedes;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}
