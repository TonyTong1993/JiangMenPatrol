package com.ecity.cswatersupply.workorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.WorkOrderChooseMaterialActivity;
import com.ecity.cswatersupply.workorder.WorkOrderFinishActivity;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderIncreaseInspectItemAdapter;
import com.ecity.cswatersupply.workorder.model.MaterialBrief;
import com.ecity.cswatersupply.workorder.model.MaterialDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkOrderFinishFragmentMaterielInfo extends Fragment {
    private static volatile WorkOrderFinishFragmentMaterielInfo instance;

    public WorkOrderFinishFragmentMaterielInfo() {
    }

    private TextView tvExplainTitle;
    private EditText etExplain;
    private ListView mListView;
    private Button mBtnAddMaterial;
    private List<List<MaterialDetail>> materialDetailInfos;
    private List<MaterialDetail> selectedMaterials;
    private List<MaterialBrief> materialBriefs;
    private WorkOrderIncreaseInspectItemAdapter mAdapter;
    private InspectItem mInspectItem;
    private WorkOrderFinishActivity mWorkOrderFinishActivity;

    public static WorkOrderFinishFragmentMaterielInfo getInstance() {
        if (null == instance) {
            synchronized (WorkOrderFinishFragmentBaseInfo.class) {
                if (null == instance) {
                    instance = new WorkOrderFinishFragmentMaterielInfo();
                }
            }
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.custom_form_item_workorder_material_increase, null);
        initData(view);
        initUI(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (null != materialDetailInfos) {
            materialDetailInfos.clear();
            materialDetailInfos = null;
        }
        if (null != selectedMaterials) {
            selectedMaterials.clear();
            selectedMaterials = null;
        }
        if (null != materialBriefs) {
            materialBriefs.clear();
            materialBriefs = null;
        }
        super.onDestroyView();
    }

    public void onBackButtonClicked(View view) {
        this.mWorkOrderFinishActivity.finish();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.SELECT_MATERIAL:
                if (null != data) {
                    materialBriefs = (List<MaterialBrief>) data.getSerializableExtra(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_BRIEF_INFO);
                    materialDetailInfos = (List<List<MaterialDetail>>) data.getSerializableExtra(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_DETAIL_INFO);
                    refreshListView();
                }
                break;
            default:
                break;
        }
    }

    public void addMaterialJsonToInspectItems() {
        if (null == mInspectItem) {
            return;
        }
        List<InspectItem> items = mInspectItem.getChilds();
        if (ListUtil.isEmpty(items)) {
            return;
        }
        for (InspectItem itemTemp : items) {
            if ("reportfinish_deal_meterialremark".equalsIgnoreCase(itemTemp.getName())) {//表示物料备注的
                itemTemp.setValue(etExplain.getText().toString());
            }
        }

        if (ListUtil.isEmpty(selectedMaterials)) {
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < selectedMaterials.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            MaterialDetail materialDetail = selectedMaterials.get(i);
            try {
                jsonObject.put("gid", materialDetail.getParentId());
                jsonObject.put("name", materialDetail.getParentName());
                jsonObject.put("price", materialDetail.getPrice());
                jsonObject.put("unit", materialDetail.getUnit());
                jsonObject.put("diameter", materialDetail.getDiameter());
                jsonObject.put("count", materialDetail.getCount());
                jsonArray.put(i, jsonObject);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }
        for (InspectItem itemTemp : items) {
            if ("reportfinish_deal_meterial".equalsIgnoreCase(itemTemp.getName())) {
                itemTemp.setValue(jsonArray.toString());
            }
        }
    }

    private void refreshListView() {
        if (null == selectedMaterials) {
            selectedMaterials = new ArrayList<MaterialDetail>();
        } else {
            selectedMaterials.clear();
        }
        if (null != materialDetailInfos) {
            for (List<MaterialDetail> inspectItem : materialDetailInfos) {
                if (null != inspectItem) {
                    for (MaterialDetail materialDetail : inspectItem) {
                        if (materialDetail.isSelected()) {
                            selectedMaterials.add(materialDetail);
                        }
                    }
                }
            }
        }

        if (null != mAdapter) {
            mAdapter.setList(selectedMaterials);
            mAdapter.setMaterialDetailInfos(materialDetailInfos);
        }
    }

    /**
     * 提交方法
     *
     * @param view
     */
    public void onActionButtonClicked(View view) {
    }

    private void initData(View view) {
        etExplain = (EditText) view.findViewById(R.id.et_item_value);
        this.mWorkOrderFinishActivity = (WorkOrderFinishActivity) getActivity();
        if (null != mWorkOrderFinishActivity.getAllInspectItems() && mWorkOrderFinishActivity.getAllInspectItems().size() > 1) {
            mInspectItem = mWorkOrderFinishActivity.getAllInspectItems().get(1);
            if (!ListUtil.isEmpty(mInspectItem.getChilds())) {
                Iterator<InspectItem> iterator = mInspectItem.getChilds().iterator();
                while (iterator.hasNext()) {
                    InspectItem item = iterator.next();
                    if ("reportfinish_deal_meterial".equalsIgnoreCase(item.getName()) && !StringUtil.isBlank(item.getValue())) {
                        try {
                            if (null == selectedMaterials) {
                                selectedMaterials = new ArrayList<MaterialDetail>();
                            }
                            JSONArray jsonArray = new JSONArray(item.getValue());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MaterialDetail detail = new MaterialDetail();
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                detail.setParentId(jsonObj.optString("gid"));
                                detail.setUnit(jsonObj.optString("unit"));
                                detail.setDiameter(jsonObj.optString("diameter"));
                                detail.setPrice(jsonObj.optString("price"));
                                detail.setCount(jsonObj.optString("count"));
                                detail.setParentName(jsonObj.optString("name"));

                                selectedMaterials.add(detail);
                            }
                        } catch (Exception e) {
                            LogUtil.e(this, e);
                        }
                    } else if ("reportfinish_deal_meterialremark".equalsIgnoreCase(item.getName()) && !StringUtil.isBlank(item.getValue())) {
                        etExplain.setText(item.getValue());
                    }
                }
            }
        }
    }

    private void initUI(View view) {
        if (mInspectItem.getChilds().size() > 1) {
            boolean isRequired = mInspectItem.getChilds().get(1).isRequired();
            if (!isRequired) {
                TextView tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                tvStar.setVisibility(View.GONE);
            }
        }
        tvExplainTitle = (TextView) view.findViewById(R.id.tv_item_title);
        tvExplainTitle.setText(R.string.material_use_explain);
        mListView = (ListView) view.findViewById(R.id.lv_can_add_item);
        mBtnAddMaterial = (Button) view.findViewById(R.id.btn_add_material);
        mBtnAddMaterial.setOnClickListener(new MyAddMaterialBtnOnClickListener());
        if (null != mWorkOrderFinishActivity && null != mInspectItem) {
            mAdapter = new WorkOrderIncreaseInspectItemAdapter(mWorkOrderFinishActivity, mInspectItem);
            mBtnAddMaterial.setText("+" + String.format(getString(R.string.event_report_add_material), mInspectItem.getAlias()));
        }
        if (!ListUtil.isEmpty(selectedMaterials)) {
            mAdapter.setList(selectedMaterials);
        }
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
        }
    }

    private class MyAddMaterialBtnOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mWorkOrderFinishActivity, WorkOrderChooseMaterialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(WorkOrderChooseMaterialActivity.INTENT_KEY_TITLE, mWorkOrderFinishActivity.getString(R.string.workorder_finish_material_title));
            if (null != materialBriefs && null != materialDetailInfos) {
                bundle.putSerializable(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_BRIEF_INFO, (Serializable) materialBriefs);
                bundle.putSerializable(WorkOrderChooseMaterialActivity.INTENT_KEY_MATERIAL_DETAIL_INFO, (Serializable) materialDetailInfos);
                bundle.putSerializable(WorkOrderChooseMaterialActivity.INTENT_KEY_SELECTED_MATERIALS, (Serializable) selectedMaterials);
            }
            bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
            intent.putExtras(bundle);
            mWorkOrderFinishActivity.startActivityFromFragment(WorkOrderFinishFragmentMaterielInfo.getInstance(), intent, RequestCode.SELECT_MATERIAL);
        }
    }
}
