package com.ecity.cswatersupply.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/5.
 * PS: Not easy to write code, please indicate.
 */
public class BaseResponse {
    private String msgCode;
    private String msg;
    private JSONObject data;

    public BaseResponse(String msgCode, String msg, JSONObject data) {
        this.msgCode = msgCode;
        this.msg = msg;
        this.data = data;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getData() {
        return data;
    }
}
