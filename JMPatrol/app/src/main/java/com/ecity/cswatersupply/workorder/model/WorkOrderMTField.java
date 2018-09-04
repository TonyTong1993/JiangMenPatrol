package com.ecity.cswatersupply.workorder.model;

import java.util.Map;

import com.ecity.cswatersupply.model.AModel;

/**
 * 工单元数据模型
 * 
 * @author gaokai
 *
 */
public class WorkOrderMTField extends AModel {
    private static final long serialVersionUID = 1L;
    public String name;
    public String type;
    public String alias;
    public int findex;
    public Map<String, String> values;
    public boolean visible;
}
