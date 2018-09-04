package com.ecity.cswatersupply.workorder.adpter;

import android.app.Activity;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.workorder.model.MaterialDetail;

import java.util.List;

public class WorkOrderIncreaseInspectItemAdapter extends AIncreaseInspectItemAdapter<MaterialDetail>  {
    private List<List<MaterialDetail>> mMaterialDetailInfos;

    public WorkOrderIncreaseInspectItemAdapter(Activity activity, InspectItem parentItem) {
        super(activity, parentItem);
    }

    public void setMaterialDetailInfos(List<List<MaterialDetail>> mMaterialDetailInfos) {
        this.mMaterialDetailInfos = mMaterialDetailInfos;
    }


    @Override
    protected void deselectItem(int position) {
        if (null == mMaterialDetailInfos) {
            return;
        }

        MaterialDetail material = mList.get(position);
        for (List<MaterialDetail> materialDetail : mMaterialDetailInfos) {
            for (MaterialDetail materialDetailTemp : materialDetail) {
                if (material.getParentId().equalsIgnoreCase(materialDetailTemp.getParentId()) && material.getDiameter().equalsIgnoreCase(materialDetailTemp.getDiameter())) {
                    materialDetailTemp.setSelected(false);
                }
            }
        }
    }

    @Override
    protected int getItemMinCount() {
        return 1;
    }
}
