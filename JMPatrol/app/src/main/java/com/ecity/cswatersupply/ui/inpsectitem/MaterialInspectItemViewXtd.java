package com.ecity.cswatersupply.ui.inpsectitem;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.workorder.WorkOrderChooseMaterialActivity;
import com.ecity.cswatersupply.workorder.adpter.AIncreaseInspectItemAdapter;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderIncreaseInspectItemAdapter;
import com.ecity.cswatersupply.workorder.model.MaterialBrief;
import com.ecity.cswatersupply.workorder.model.MaterialDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanma on 14/4/2017.
 */
public class MaterialInspectItemViewXtd extends ABaseInspectItemView implements AIncreaseInspectItemAdapter.OnItemCountChangedListener<MaterialDetail> {
    private ListView lvMaterials;
    private Button btnAdd;
    private List<List<MaterialDetail>> materialDetailInfos;
    private List<MaterialBrief> materialBriefs;
    private List<MaterialDetail> selectedMaterials = new ArrayList<MaterialDetail>();
    private WorkOrderIncreaseInspectItemAdapter mAdapter;

    @Override
    protected void setup(View contentView) {
        lvMaterials = (ListView) contentView.findViewById(R.id.lv_items);
        btnAdd = (Button) contentView.findViewById(R.id.btn_add);
        btnAdd.setText(R.string.workorder_finish_material_title);
        btnAdd.setOnClickListener(new MyOnAddClickListener());
        parseDefaultSelectedMaterials();
        mAdapter = new WorkOrderIncreaseInspectItemAdapter(context, mInspectItem);
        mAdapter.setList(selectedMaterials);
        mAdapter.setOnItemCountChangedListener(this);
        lvMaterials.setAdapter(mAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_material;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.SELECT_MATERIAL:
                if (null != data) {
                    materialBriefs = (List<MaterialBrief>) data.getSerializableExtra(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_BRIEF_INFO);
                    materialDetailInfos = (List<List<MaterialDetail>>) data.getSerializableExtra(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_DETAIL_INFO);
                    refreshListView();
                    setItemValue();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemCountChanged(MaterialDetail item) {
        setItemValue();
    }

    @Override
    public void onItemRemoved(MaterialDetail item) {
        setItemValue();
    }

    private void refreshListView() {
        selectedMaterials.clear();
        if (null != materialDetailInfos) {
            for (List<MaterialDetail> inspectItem : materialDetailInfos) {
                for (MaterialDetail materialDetail : inspectItem) {
                    if (materialDetail.isSelected()) {
                        selectedMaterials.add(materialDetail);
                    }
                }
            }
        }

        mAdapter.setList(selectedMaterials);
        mAdapter.setMaterialDetailInfos(materialDetailInfos);
    }

    private void parseDefaultSelectedMaterials() {
        if (StringUtil.isBlank(mInspectItem.getValue())) {
            return;
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(mInspectItem.getValue());
        } catch (JSONException e) {
            LogUtil.e(this, e);
            return;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.optJSONObject(i);
            if (jsonObj == null) {
                continue;
            }

            MaterialDetail detail = new MaterialDetail();
            detail.setParentId(jsonObj.optString("gid"));
            detail.setUnit(jsonObj.optString("unit"));
            detail.setDiameter(jsonObj.optString("diameter"));
            detail.setPrice(jsonObj.optString("price"));
            detail.setCount(jsonObj.optString("count"));
            detail.setParentName(jsonObj.optString("name"));
            selectedMaterials.add(detail);
        }
    }

    private void setItemValue() {
        JSONArray jsonArray = new JSONArray();

        for (MaterialDetail material : selectedMaterials) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("gid", material.getParentId());
                jsonObj.put("name", material.getParentName());
                jsonObj.put("price", material.getPrice());
                jsonObj.put("unit", material.getUnit());
                jsonObj.put("diameter", material.getDiameter());
                jsonObj.put("count", material.getCount());
                jsonArray.put(jsonObj);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }

        mInspectItem.setValue(jsonArray.toString());
    }

    private final class MyOnAddClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            gotoAddMaterials();
        }
    }

    private void gotoAddMaterials() {
        Bundle bundle = new Bundle();
        bundle.putString(WorkOrderChooseMaterialActivity.INTENT_KEY_TITLE, context.getString(R.string.workorder_finish_material_title));
        if (null != materialBriefs && null != materialDetailInfos) {
            bundle.putSerializable(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_BRIEF_INFO, (Serializable) materialBriefs);
            bundle.putSerializable(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_DETAIL_INFO, (Serializable) materialDetailInfos);
        }
        bundle.putSerializable(WorkOrderChooseMaterialActivity.INTENT_KEY_SELECTED_MATERIALS, (Serializable) selectedMaterials);
        Intent intent = new Intent(context, WorkOrderChooseMaterialActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RequestCode.SELECT_MATERIAL);
    }
}
