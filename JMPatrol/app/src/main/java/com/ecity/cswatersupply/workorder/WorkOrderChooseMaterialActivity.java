package com.ecity.cswatersupply.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.task.SaveCacheTask;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CacheManager;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.adpter.MaterialInfoAdapter;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderChooseMaterialAdapter;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderFinishFragmentMaterielInfo;
import com.ecity.cswatersupply.workorder.model.MaterialBrief;
import com.ecity.cswatersupply.workorder.model.MaterialDetail;
import com.ecity.cswatersupply.workorder.model.MaterialServerInfo;
import com.ecity.cswatersupply.workorder.network.MaterialInfoResponse;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkOrderChooseMaterialActivity extends BaseActivity {
    public static final String INTENT_KEY_TITLE = "INTENT_KEY_TITLE";
    public static final String INTENT_KEY_MATERIAL_BRIEF_INFO = "INTENT_KEY_MATERIAL_BRIEF_INFO";
    public static final String INTENT_KEY_MATERIAL_DETAIL_INFO = "INTENT_KEY_MATERIAL_DETAIL_INFO";
    public static final String INTENT_KEY_SELECTED_MATERIALS = "INTENT_KEY_SELECTED_MATERIALS";
    private static final String MATERIAL_CACHE_KEY = "MATERIAL_CACHE_KEY";
    private CustomTitleView mCustomTitleView;
    private ExpandableListView mExpandableListView;
    private WorkOrderChooseMaterialAdapter mAdapter;
    private List<List<MaterialDetail>> materialDetails;
    private List<MaterialBrief> materialBriefs;
    private List<MaterialDetail> selectedMaterials;
    private EditText mSearchEditText;
    private String mTitle;
    private IExecuteAfterTaskDo iExecuteAfterTaskDo = new IExecuteAfterTaskDo() {

        @Override
        public void executeTaskError() {
        }

        @Override
        public void executeOnTaskSuccess(Serializable result) {
            @SuppressWarnings("unchecked")
            List<MaterialServerInfo> cacheMaterial = (List<MaterialServerInfo>) result;
            materialDetails = new ArrayList<List<MaterialDetail>>();
            materialBriefs = new ArrayList<MaterialBrief>();
            MaterialInfoAdapter.adapt(cacheMaterial, materialDetails, materialBriefs);
            mAdapter.setGroupList(materialBriefs);
            if (!ListUtil.isEmpty(materialBriefs) && !ListUtil.isEmpty(materialDetails)) {
                setSelectedMaterialStatus();
            }
            mAdapter.setChildrenList(materialDetails);
            mExpandableListView.setAdapter(mAdapter);
        }

        @Override
        public void executeOnTaskFinish() {
            LoadingDialogUtil.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        setContentView(R.layout.activity_workorder_choosematerial);
        requestData();
        initUI();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public void onActionButtonClicked(View v) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_MATERIAL_BRIEF_INFO, (Serializable) materialBriefs);
        intent.putExtra(INTENT_KEY_MATERIAL_DETAIL_INFO, (Serializable) materialDetails);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressWarnings("unchecked")
    private void requestData() {
        Bundle bundle = getIntent().getExtras();
        mTitle = getIntent().getStringExtra(INTENT_KEY_TITLE);
        if (mTitle == null) {
            mTitle = getString(R.string.workorder_finish_material_title);
        }

        materialBriefs = (List<MaterialBrief>) bundle.getSerializable(INTENT_KEY_MATERIAL_BRIEF_INFO);
        materialDetails = (List<List<MaterialDetail>>) bundle.getSerializable(INTENT_KEY_MATERIAL_DETAIL_INFO);
        selectedMaterials = (List<MaterialDetail>) bundle.getSerializable(INTENT_KEY_SELECTED_MATERIALS);

        if (CacheManager.isReadCacheData(this, false, MATERIAL_CACHE_KEY) && (null == materialBriefs || null == materialDetails)) {
            LoadingDialogUtil.show(this, R.string.hold_on_please);
            new ReadCacheTask(this, iExecuteAfterTaskDo).execute(MATERIAL_CACHE_KEY);
        } else if (null == materialBriefs || null == materialDetails) {
            LoadingDialogUtil.show(this, R.string.hold_on_please);
            WorkOrderService.instance.getMaterialInfo();
        } else {
            // no logic to do.
        }
    }

    private void initUI() {
        mCustomTitleView = (CustomTitleView) findViewById(R.id.view_title_material);
        mCustomTitleView.setTitleText(mTitle);
        mCustomTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        mCustomTitleView.setRightActionBtnText(R.string.ok);
        mSearchEditText = (EditText) findViewById(R.id.edit_search);
        mSearchEditText.addTextChangedListener(new MyEditTextSearchWatcher());
        mExpandableListView = (ExpandableListView) findViewById(R.id.lvext_material);
        mAdapter = new WorkOrderChooseMaterialAdapter(this);
        if (!ListUtil.isEmpty(materialBriefs) && !ListUtil.isEmpty(materialDetails)) {
            mAdapter.setGroupList(materialBriefs);
            mAdapter.setChildrenList(materialDetails);
            mExpandableListView.setAdapter(mAdapter);
        }
        mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        mExpandableListView.collapseGroup(i);
                    }
                }
                mExpandableListView.setSelectedGroup(groupPosition);
            }
        });
    }

    private void setSelectedMaterialStatus() {
        if (ListUtil.isEmpty(selectedMaterials)) {
            return;
        }

        for (MaterialDetail selectedMaterial : selectedMaterials) {
            for (List<MaterialDetail> materialDetail : materialDetails) {
                for (MaterialDetail tempItem : materialDetail) {
                    if (!selectedMaterial.getParentId().equalsIgnoreCase(tempItem.getParentId())) {
                        break;
                    }

                    if (selectedMaterial.getDiameter().equalsIgnoreCase(tempItem.getDiameter())) {
                        tempItem.setSelected(true);
                        tempItem.setCount(selectedMaterial.getCount());
                    }
                }
            }
        }
    }

    private void processGetMaterialsResponse(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        MaterialInfoResponse response = event.getData();
        if (!ListUtil.isEmpty(response.getMaterialServerInfos())) {
            materialDetails = new ArrayList<List<MaterialDetail>>();
            materialBriefs = new ArrayList<MaterialBrief>();
            new SaveCacheTask(this, (Serializable) response.getMaterialServerInfos(), MATERIAL_CACHE_KEY).execute();
            MaterialInfoAdapter.adapt(response.getMaterialServerInfos(), materialDetails, materialBriefs);
        }
        mAdapter.setGroupList(materialBriefs);
        mAdapter.setChildrenList(materialDetails);
        mExpandableListView.setAdapter(mAdapter);
    }


    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_GET_MATERIAL_INFO:
                processGetMaterialsResponse(event);
                break;
            default:
                break;
        }
    }

    private class MyEditTextSearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            filterMaterials(s.toString());
        }
    }

    private void filterMaterials(String string) {
        List<List<MaterialDetail>> searchMaterialDetails = new ArrayList<List<MaterialDetail>>();
        List<MaterialBrief> searchMaterialBriefs = new ArrayList<MaterialBrief>();
        for (int i = 0; i < materialBriefs.size(); i++) {
            if (materialBriefs.get(i).getName().toUpperCase().contains(string)) {
                searchMaterialBriefs.add(materialBriefs.get(i));
                searchMaterialDetails.add(materialDetails.get(i));
            }
        }
        mAdapter.setGroupList(searchMaterialBriefs);
        mAdapter.setChildrenList(searchMaterialDetails);
        mExpandableListView.setAdapter(mAdapter);
    }
}
