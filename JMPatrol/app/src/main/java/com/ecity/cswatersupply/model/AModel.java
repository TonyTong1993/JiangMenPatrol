package com.ecity.cswatersupply.model;

import java.io.Serializable;

import com.ecity.cswatersupply.utils.GsonUtil;

public abstract class AModel implements Serializable {
    private static final long serialVersionUID = 6833942478177021532L;

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
