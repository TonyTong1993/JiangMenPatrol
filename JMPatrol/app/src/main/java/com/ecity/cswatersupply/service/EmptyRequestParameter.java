package com.ecity.cswatersupply.service;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;


public class EmptyRequestParameter implements IRequestParameter {

    public EmptyRequestParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();

        return map;
    }
}

