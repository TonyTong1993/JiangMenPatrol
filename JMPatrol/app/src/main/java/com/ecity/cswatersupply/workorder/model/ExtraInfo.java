package com.ecity.cswatersupply.workorder.model;

import java.util.ArrayList;

import com.ecity.cswatersupply.network.response.AServerResponse;
/**
 * 完结工单额外信息
 * 用于详情展示
 * @author gaokai
 *
 */
public class ExtraInfo extends AServerResponse {
    public ArrayList<RepairMaterial> repairMaterials;
    public ArrayList<RepairMan> repairMen;
    public ArrayList<ProcessLog> processLogs;
}
