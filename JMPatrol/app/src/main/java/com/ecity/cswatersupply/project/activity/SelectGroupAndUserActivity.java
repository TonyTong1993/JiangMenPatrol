package com.ecity.cswatersupply.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.OrganisationListAdapter;
import com.ecity.cswatersupply.adapter.OrganisationListAdapter.IOrganisationListAdapter;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectGroupAndUserActivity extends Activity implements IOrganisationListAdapter {
    public static final String INTENT_KEY_ORGANISATIONS = "INTENT_KEY_ORGANISATIONS";
    public static final String INTENT_KEY_IS_SELECT_PEOPLE = "INTENT_KEY_IS_SELECT_PEOPLE";
    public static final String INTENT_KEY_IS_MULTI_SELECT = "INTENT_KEY_IS_MULTI_SELECT";
    /**
     * 选择结果。在onActivityResult中，用这个key或去选中结果。
     */
    public static final String INTENT_KEY_SELECTED_ORGANISATIONS = "INTENT_KEY_SELECTED_ORGANISATIONS";
    private CustomTitleView titleView;
    private ListView lvRecords;
    /**
     * 所有的组织和人员
     */
    private OrganisationSelection topOrganisation;
    /**
     * 被点击的组织，点一个组织，先把该组织存起来，再显示它的children。用于回退的时候。
     */
    private List<OrganisationSelection> queuedOrganisations = new ArrayList<OrganisationSelection>();

    /**
     * true：选人，false：选机构
     */
    private boolean isSelectingUser;
    /**
     * true：多选，false：单选
     */
    private boolean isMultiSelection;
    private OrganisationListAdapter adapter;
    private List<OrganisationSelection> selectedOrganisations = new ArrayList<OrganisationSelection>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_selection);
        initData();
        initUI();
        setListeners();
    }

    public void onBackButtonClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_KEY_SELECTED_ORGANISATIONS, (Serializable) selectedOrganisations);
        setResult(RESULT_OK, new Intent().putExtras(bundle));
        notifyBackEvent();
    }

    public void onActionButtonClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_KEY_SELECTED_ORGANISATIONS, (Serializable) selectedOrganisations);
        setResult(RESULT_OK, new Intent().putExtras(bundle));
        finish();
    }

    @Override
    public void disselectAllItems() {
        topOrganisation.setChildOrganisationsSelected(false);
        topOrganisation.setChildUsersSelected(false);
    }

    @Override
    public void onItemCheckStatusChanged(OrganisationSelection item) {
        updateSelectedOrganisations(item);
    }

    @Override
    public void onItemClickListener(OrganisationSelection item) {
        if (!item.isUser()) {
            showChildOrganisations(item);
        }
    }

    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_title);
        titleView.setTitleText(R.string.select_organisation_title);
        titleView.setRightActionBtnText(R.string.ok);
        lvRecords = (ListView) findViewById(R.id.lv_records);
        adapter = new OrganisationListAdapter(this, isSelectingUser, isMultiSelection, this);
        lvRecords.setAdapter(adapter);
        showChildOrganisations(topOrganisation);
    }

    private void initData() {
        topOrganisation = (OrganisationSelection) getIntent().getSerializableExtra(INTENT_KEY_ORGANISATIONS);
        isSelectingUser = getIntent().getBooleanExtra(INTENT_KEY_IS_SELECT_PEOPLE, false);
        isMultiSelection = getIntent().getBooleanExtra(INTENT_KEY_IS_MULTI_SELECT, false);
    }

    private void setListeners() {
        lvRecords.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<OrganisationSelection> dataSource = getCurrentDataSource();
                OrganisationSelection item = dataSource.get(position);
                if (!item.isUser() && !ListUtil.isEmpty(item.getChildren())) {
                    showChildOrganisations(item);
                }
            }
        });
    }

    private void showChildOrganisations(OrganisationSelection parentOrganisation) {
        queuedOrganisations.add(parentOrganisation);
        adapter.setList(getCurrentDataSource());
    }

    private void notifyBackEvent() {
        if ((queuedOrganisations.size() <= 1)) { // 已退到最顶级
            if (selectedOrganisations.size() > 0) {
                confirmExit();
            } else {
                finish();
            }
            return;
        }

        queuedOrganisations.remove(queuedOrganisations.size() - 1);
        OrganisationSelection parentOrganisation = queuedOrganisations.get(queuedOrganisations.size() - 1);
        queuedOrganisations.remove(queuedOrganisations.size() - 1);
        showChildOrganisations(parentOrganisation);
    }

    private List<OrganisationSelection> getCurrentDataSource() {
        return queuedOrganisations.get(queuedOrganisations.size() - 1).getChildren();
    }

    private void updateSelectedOrganisations(OrganisationSelection item) {
        if (item.isSelected() && !isMultiSelection) {
            selectedOrganisations.clear();
        }

        if (item.isSelected()) {
            if (!selectedOrganisations.contains(item)) {
                selectedOrganisations.add(item);
            }
        } else {
            if (selectedOrganisations.contains(item)) {
                selectedOrganisations.remove(item);
            }
        }
    }

    private void confirmExit() {
        AlertView.OnAlertViewListener listener = new OnAlertViewListener() {

            @Override
            public void back(boolean result) {
                if (result) {
                    finish();
                }
            }
        };

        String msg = getString(R.string.select_organisation_confirm_exit);
        AlertView dialog = new AlertView(this, null, msg, listener, AlertView.AlertStyle.OK_CANCEL);
        dialog.show();
    }
}