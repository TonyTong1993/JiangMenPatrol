package com.ecity.android.map.core.meta;

import java.io.Serializable;

public class DbMetaGWFL implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2574225623750313682L;
    private String code = null;
    private String type = null;
    private String remark = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
