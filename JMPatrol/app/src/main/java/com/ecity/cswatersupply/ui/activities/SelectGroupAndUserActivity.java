package com.ecity.cswatersupply.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.OrganisationListAdapter;
import com.ecity.cswatersupply.adapter.OrganisationListAdapter.IOrganisationListAdapter;
import com.ecity.cswatersupply.contact.TakePhoneActivity;
import com.ecity.cswatersupply.contact.model.Contact;
import com.ecity.cswatersupply.contact.widght.PathTextView;
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
    private PathTextView pathTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation_selection);
        initData();
        initUI();
        setListeners();
    }

    private void initPathTextView() {
        pathTextView = (PathTextView) findViewById(R.id.tv_path);
        pathTextView.setOnItemClickListener(new OnPathTextViewClickListener());
        pathTextView.clearRoot();
        pathTextView.initRoot(topOrganisation.getName(), Color.parseColor("#48a0c7"));

    }

    public void onBackButtonClicked(View view) {
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
            showChildOrganisations(item, false);
        } else {
            Contact contact = convertModel2Contact(item);
            Intent intent = new Intent(SelectGroupAndUserActivity.this, TakePhoneActivity.class);
            intent.putExtra(TakePhoneActivity.EMERENCY_CONTACT, contact);
            startActivity(intent);
        }
    }

    private void initUI() {
        initPathTextView();
        titleView = (CustomTitleView) findViewById(R.id.view_title);
        titleView.setTitleText(R.string.select_organisation_title);
        titleView.setRightActionBtnText(R.string.ok);
        lvRecords = (ListView) findViewById(R.id.lv_records);
        adapter = new OrganisationListAdapter(this, isSelectingUser, isMultiSelection, this);
        lvRecords.setAdapter(adapter);
        showChildOrganisations(topOrganisation, false);
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
                if (item.isUser()) {
                    Contact contact = convertModel2Contact(item);
                    Intent intent = new Intent(SelectGroupAndUserActivity.this, TakePhoneActivity.class);
                    intent.putExtra(TakePhoneActivity.EMERENCY_CONTACT, contact);
                    startActivity(intent);
                } else if (!ListUtil.isEmpty(item.getChildren())) {
                    showChildOrganisations(item, false);
                }
            }
        });
    }

    private void showChildOrganisations(OrganisationSelection parentOrganisation, boolean isGoingBack) {
        if (queuedOrganisations.size() == 0) {
            queuedOrganisations.add(parentOrganisation);
        } else if (queuedOrganisations.get(queuedOrganisations.size() - 1).getId() != parentOrganisation.getId()) {
            queuedOrganisations.add(parentOrganisation);
        }

        adapter.setList(getCurrentDataSource());
        resetPathTextView();
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


        pathTextView.backParent();
        queuedOrganisations.remove(queuedOrganisations.size() - 1);
        OrganisationSelection parentOrganisation = queuedOrganisations.get(queuedOrganisations.size() - 1);
        showChildOrganisations(parentOrganisation, true);
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

    private class OnPathTextViewClickListener implements PathTextView.OnItemClickListener {

        @Override
        public void onClick(long currentId, int backCount) {
            for (int i = 0; i < backCount; i++) {
                queuedOrganisations.remove(queuedOrganisations.size() - 1);
            }

            try {
                pathTextView.backParent();
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
            OrganisationSelection parentOrganisation;
            if (queuedOrganisations.size() == 0) {
                parentOrganisation = topOrganisation;
            } else {
                parentOrganisation = queuedOrganisations.get(queuedOrganisations.size() - 1);
            }
            showChildOrganisations(parentOrganisation, true);
        }
    }

    private Contact convertModel2Contact(OrganisationSelection item) {
        OrganisationSelection lastOrgnisation = queuedOrganisations.get(queuedOrganisations.size() - 1);
        Contact contact = new Contact();
        contact.setId(String.valueOf(item.getId()));
        contact.setType("");
        contact.setSex("");
        contact.setName(item.getName());
        contact.setDepartment(lastOrgnisation.getName());
        contact.setPosition("");
        contact.setTel(item.getPhone());
        contact.setMobile(item.getPhone());
        contact.setCity("");
        contact.setRegion("");
        contact.setCommunity("");

        return contact;
    }

    private void resetPathTextView() {
        pathTextView.clearRoot();
        pathTextView.initRoot(topOrganisation.getName(), Color.parseColor("#48a0c7"));

        for (int i = 1; i < queuedOrganisations.size(); i ++) {
            OrganisationSelection currentOrganisation = queuedOrganisations.get(i);
            if (com.ecity.cswatersupply.utils.ListUtil.isEmpty(currentOrganisation.getChildren())) {
                pathTextView.append(i, Color.parseColor("#cccccc"), currentOrganisation.getName());
            } else {
                pathTextView.append(i, Color.parseColor("#48a0c7"), currentOrganisation.getName());
            }
        }
    }
}