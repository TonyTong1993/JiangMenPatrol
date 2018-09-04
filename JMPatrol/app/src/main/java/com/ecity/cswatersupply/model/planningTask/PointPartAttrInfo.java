package com.ecity.cswatersupply.model.planningTask;

import java.io.Serializable;
import java.util.List;

public class PointPartAttrInfo extends Z3PlanTaskPart implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<FieldAliases> fAliasesList;
    public List<List<Attr>> attrList;
    //本地坐标
    public String x;
    public String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getY() {
        return y;
    }

    public List<FieldAliases> getfAliasesList() {
        return fAliasesList;
    }

    public void setfAliasesList(List<FieldAliases> fAliasesList) {
        this.fAliasesList = fAliasesList;
    }

    public List<List<Attr>> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<List<Attr>> attrList) {
        this.attrList = attrList;
    }

    public class Attr implements Serializable {
        private static final long serialVersionUID = -5633337982664281068L;
        public String attrKey;
        public String attrValue;

        public String getAttrKey() {
            return attrKey;
        }

        public void setAttrKey(String attrKey) {
            this.attrKey = attrKey;
        }

        public String getAttrValue() {
            return attrValue;
        }

        public void setAttrValue(String attrValue) {
            this.attrValue = attrValue;
        }

    }

    public class FieldAliases implements Serializable {
        private static final long serialVersionUID = -1966396266747111218L;
        public String fid;
        public String fname;

        public FieldAliases(String fid, String fname) {
            this.fid = fid;
            this.fname = fname;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }
    }
}
