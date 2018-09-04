package com.ecity.cswatersupply.ui.inpsectitem;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.ui.activities.SelectGroupAndUserActivity;
import com.ecity.cswatersupply.workorder.adpter.AIncreaseInspectItemAdapter;
import com.ecity.cswatersupply.workorder.adpter.UserWorkloadIncreaseInspectItemAdapter;
import com.ecity.cswatersupply.workorder.model.UserWorkload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ecity.cswatersupply.model.RequestCode.SELECT_CONTACT_MAN;

/**
 * Created by jonathanma on 14/4/2017.
 */
public class UserWorkloadInspectItemViewXtd extends ABaseInspectItemView implements AIncreaseInspectItemAdapter.OnItemCountChangedListener<UserWorkload> {
    private ListView lvItems;
    private Button btnAdd;
    private UserWorkloadIncreaseInspectItemAdapter mAdapter;
    private List<UserWorkload> dataSource = new ArrayList<UserWorkload>();
    private OrganisationSelection mTopOrganisationSelection;

    @Override
    protected void setup(View contentView) {
        dataSource = new ArrayList<UserWorkload>();
        lvItems = (ListView) contentView.findViewById(R.id.lv_items);
        btnAdd = (Button) contentView.findViewById(R.id.btn_add);
        btnAdd.setText(R.string.workorder_form_add_repairman);
        btnAdd.setOnClickListener(new MyOnAddClickListener());
        mAdapter = new UserWorkloadIncreaseInspectItemAdapter(context, mInspectItem);
        mAdapter.setOnItemCountChangedListener(this);
        mAdapter.setList(dataSource);
        lvItems.setAdapter(mAdapter);
        parseContactMen();
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_material;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_CONTACT_MAN:
                if (null != data) {
                    List<OrganisationSelection> selections = (List<OrganisationSelection>) data.getSerializableExtra(SelectGroupAndUserActivity.INTENT_KEY_SELECTED_ORGANISATIONS);
                    refreshListView(selections);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemCountChanged(UserWorkload item) {
        setItemValue();
    }

    @Override
    public void onItemRemoved(UserWorkload item) {
        setItemValue();
    }

    private void setItemValue() {
        JSONArray jsonArray = new JSONArray();

        for (UserWorkload workload : dataSource) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("name", workload.getUserInfo().getId());
                jsonObj.put("alias", workload.getUserInfo().getName());
                jsonObj.put("count", workload.getCount());
                jsonArray.put(jsonObj);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }

        mInspectItem.setValue(jsonArray.toString());
    }
    private void parseContactMen() {
        if (!StringUtil.isBlank(mInspectItem.getAlias())) {
            mTopOrganisationSelection = new OrganisationSelection();
        }

        if (StringUtil.isBlank(mInspectItem.getSelectValues())) {
            return;
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(mInspectItem.getSelectValues());
        } catch (JSONException e) {
            LogUtil.e(this, e);
            return;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.optJSONObject(i);
            if (jsonObj == null) {
                continue;
            }

            OrganisationSelection selection = new OrganisationSelection();
            selection.setName(jsonObj.optString("alias"));
            selection.setId(jsonObj.optInt("name"));
            selection.setUser(true);
            mTopOrganisationSelection.addChild(selection);
        }
    }

    private void refreshListView(List<OrganisationSelection> selections) {
        dataSource.clear();
        for (OrganisationSelection selection: selections) {
            UserWorkload workload = new UserWorkload(selection);
            dataSource.add(workload);
        }
        mAdapter.setList(dataSource);
    }

    private final class MyOnAddClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            gotoSelectUser();
        }
    }

    private void gotoSelectUser() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SelectGroupAndUserActivity.INTENT_KEY_ORGANISATIONS, mTopOrganisationSelection);
        bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_MULTI_SELECT, true);
        bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_SELECT_PEOPLE, true);
        Intent intent = new Intent(context, SelectGroupAndUserActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SELECT_CONTACT_MAN);
    }
}
