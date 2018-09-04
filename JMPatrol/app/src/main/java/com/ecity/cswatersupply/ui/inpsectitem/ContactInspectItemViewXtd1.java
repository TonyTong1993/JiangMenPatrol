package com.ecity.cswatersupply.ui.inpsectitem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.ui.activities.SelectGroupAndUserActivity;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.ecity.cswatersupply.model.RequestCode.SELECT_CONTACT_MAN;


/**
 * 南昌外勤使用的选人项
 */
public class ContactInspectItemViewXtd1 extends ABaseInspectItemView {
    private OrganisationSelection mTopOrganisationSelection;
    private boolean isSingleChoice;
    private TextView mItemView;

    @Override
    protected void setup(View view) {
        parseContactMen();
        isSingleChoice = mInspectItem.getType().equals(EInspectItemType.CONTACTMEN_SINGLE);
        view.findViewById(R.id.ll_contact_men).setOnClickListener(new ContactMenOnClickListener());
        mItemView = (TextView) view.findViewById(R.id.content_choosed_main_man);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_CONTACT_MAN:
                if (null != data) {
                    List<OrganisationSelection> selections = (List<OrganisationSelection>) data.getSerializableExtra(SelectGroupAndUserActivity.INTENT_KEY_SELECTED_ORGANISATIONS);
                    setItemValue(selections);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_contact_men;
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

    private void setItemValue(List<OrganisationSelection> selections) {
        if (ListUtil.isEmpty(selections)) {
            mItemView.setText("");
            mInspectItem.setValue("");
            return;
        }

        StringBuilder userNames = new StringBuilder();
        JSONArray userIds = new JSONArray();
        for (OrganisationSelection selection : selections) {
            if (userNames.length() > 0) {
                userNames.append(",");
            }
            userNames.append(selection.getName());
            userIds.put(selection.getId());
        }
        mItemView.setText(userNames.toString());

        String value = isSingleChoice ? String.valueOf(userIds.optInt(0)) : userIds.toString();
        mInspectItem.setValue(value);
    }

    private class ContactMenOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(SelectGroupAndUserActivity.INTENT_KEY_ORGANISATIONS, mTopOrganisationSelection);
            bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_MULTI_SELECT, false);
            bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_SELECT_PEOPLE, true);
            Intent intent = new Intent(context, SelectGroupAndUserActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, SELECT_CONTACT_MAN);
        }
    }
}

