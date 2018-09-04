package com.ecity.cswatersupply.workorder.adpter;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.model.MaterialBrief;
import com.ecity.cswatersupply.workorder.model.MaterialDetail;
import com.ecity.cswatersupply.workorder.model.MaterialServerInfo;

public class MaterialInfoAdapter {
    public static void adapt(List<MaterialServerInfo> materialServerInfo, List<List<MaterialDetail>> materialDetailAllInfos, List<MaterialBrief> materialBriefs) {
        if (ListUtil.isEmpty(materialServerInfo) || null == materialDetailAllInfos || null == materialBriefs) {
            return;
        }

        if (materialBriefs.size() == 0) {
            addOneMaterialBrief(materialBriefs, materialServerInfo.get(0));
        }

        String nextId = materialBriefs.get(0).getId();
        for (MaterialServerInfo materialServerInfoTemp : materialServerInfo) {
            if (nextId.equalsIgnoreCase(materialServerInfoTemp.getGid())) {
                continue;
            }

            addOneMaterialBrief(materialBriefs, materialServerInfoTemp);

            nextId = materialServerInfoTemp.getGid();
        }

        for (MaterialBrief materialBrief : materialBriefs) {
            List<MaterialDetail> materialDetails = new ArrayList<MaterialDetail>();
            for (MaterialServerInfo materialSerInfoTemp : materialServerInfo) {
                if (materialBrief.getId().equalsIgnoreCase(materialSerInfoTemp.getGid())) {
                    MaterialDetail materialDetail = new MaterialDetail();
                    materialDetail.setParentId(materialBrief.getId());
                    materialDetail.setParentName(materialBrief.getName());
                    materialDetail.setUnit(materialSerInfoTemp.getUnit());
                    materialDetail.setPrice(materialSerInfoTemp.getPrice());
                    materialDetail.setDiameter(materialSerInfoTemp.getDiameter());

                    materialDetails.add(materialDetail);
                }
            }

            materialDetailAllInfos.add(materialDetails);
        }

    }

    private static void addOneMaterialBrief(List<MaterialBrief> materialBriefs, MaterialServerInfo materialServerInfoTemp) {
        MaterialBrief materialBrief = new MaterialBrief();
        materialBrief.setId(materialServerInfoTemp.getGid());
        materialBrief.setName(materialServerInfoTemp.getName());
        materialBrief.setUnit(materialServerInfoTemp.getUnit());
        materialBriefs.add(materialBrief);
    }

}
