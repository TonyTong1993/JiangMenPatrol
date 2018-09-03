package com.enn.sop.model.user;

import java.io.Serializable;

/**
 * @author xiaobei
 * @date 2018/3/13
 */
public class TaskUnDealCountModel implements Serializable {
    private String functionKey;
    private int count;

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
