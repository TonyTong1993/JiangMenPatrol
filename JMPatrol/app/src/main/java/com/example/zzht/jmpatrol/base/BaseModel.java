package com.example.zzht.jmpatrol.base;

import com.example.zzht.jmpatrol.utils.GsonUtil;

import java.io.Serializable;


/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/3.
 * PS: Not easy to write code, please indicate.
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 6833942478177021532L;

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
