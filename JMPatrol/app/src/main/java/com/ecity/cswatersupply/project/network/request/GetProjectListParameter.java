package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectListParameter implements IRequestParameter {
    private int pageNo;
    private int pageSize;
    private String type;
    private String startTime;
    private String endTime;
    private String status;

    public GetProjectListParameter(String type, int pageNo, int pageSize, String startTime, String endTime, String status) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("type", type);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("status", status);

        return map;
    }

    /*
    <param name="f" style="query" default="json" type="xs:string"/>
    <param name="returnFields" style="query" default="true" type="xs:boolean"/>
    <param name="code" style="query" default="" type="xs:string"/>
    <param name="name" style="query" default="" type="xs:string"/>
    <param name="type" style="query" default="0" type="xs:int"/>
    <param name="classify" style="query" default="0" type="xs:int"/>
    <param name="process" style="query" default="0" type="xs:int"/>
    <param name="startime0" style="query" default="" type="xs:string"/>
    <param name="startime1" style="query" default="" type="xs:string"/>
    <param name="orderField" style="query" default="" type="xs:string"/>
    <param name="onlyCount" style="query" default="false" type="xs:boolean"/>
    <param name="pageno" style="query" default="1" type="xs:int"/>
    <param name="pagesize" style="query" default="1000" type="xs:int"/>
    */
}
