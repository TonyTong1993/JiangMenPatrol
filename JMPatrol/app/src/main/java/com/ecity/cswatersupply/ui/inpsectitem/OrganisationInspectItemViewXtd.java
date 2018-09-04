package com.ecity.cswatersupply.ui.inpsectitem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.ui.activities.SelectGroupAndUserActivity;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ecity.cswatersupply.model.RequestCode.SELECT_CONTACT_MAN;

public class OrganisationInspectItemViewXtd extends ABaseInspectItemView {
    private static OrganisationSelection topOrganisation;
    private TextView tvValue;
    private List<OrganisationSelection> selectedItems;
    private Map<String, OrganisationSelection> id2Users;

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_organisation;
    }

    @Override
    protected void setup(View contentView) {
        tvValue = (TextView) contentView.findViewById(R.id.tv_value);
        tvValue.setOnClickListener(new MyOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != SELECT_CONTACT_MAN) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (data != null) {
            handleSelectPeopleResult(data);
        }
    }

    protected boolean isMultiSelection() {
        return false;
    }

    protected void handleSelectPeopleResult(Intent data) {
        selectedItems = (List<OrganisationSelection>) data.getSerializableExtra(SelectGroupAndUserActivity.INTENT_KEY_SELECTED_ORGANISATIONS);
        setContactValue(selectedItems);
//        updateSelectionStatus(selectedItems);
    }

    private void updateSelectionStatus(List<OrganisationSelection> selectedItems) {
        Iterator<String> iterator = id2Users.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            id2Users.get(key).setSelected(false);
        }

        for (OrganisationSelection item : selectedItems) {
            String key  = String.valueOf(item.getId());
            id2Users.get(key).setSelected(true);
        }
    }

    private void setContactValue(List<OrganisationSelection> data) {
        String nameInfo = buildChoosedMenName(mInspectItem.getDefaultValue(), data);
        tvValue.setText(nameInfo);

        if (ListUtil.isEmpty(data)) {
            mInspectItem.setValue("");
        } else {
            String value;
            if (isMultiSelection()) {
                value = buildAssistMenIdsJsonArr(data);
            } else {
                value = String.valueOf(getUserValue(data.get(0)));
            }
            mInspectItem.setValue(value);
        }
    }

    private String buildAssistMenIdsJsonArr(List<OrganisationSelection> choosedAssistMen) {
        JSONArray jsonArr = new JSONArray();
        if (choosedAssistMen != null) {
            for (OrganisationSelection contactMan : choosedAssistMen) {
                jsonArr.put(getUserValue(contactMan));
            }
        }
        return jsonArr.toString();
    }

    private String getUserValue(OrganisationSelection contact) {
        return contact.getId() + ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR + contact.getName();
    }

    private static String buildChoosedMenName(String defaultValue, List<OrganisationSelection> choosedMen2) {
        String result = defaultValue;
        if (choosedMen2.size() != 0) {
            StringBuilder name = new StringBuilder();
            for (OrganisationSelection contactMan : choosedMen2) {
                name.append(contactMan.getName() + "、");
            }
            name.deleteCharAt(name.length() - 1);// 把最后的“、”删除
            result = name.toString();
        }
        return result;
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EventBusUtil.register(OrganisationInspectItemViewXtd.this);
            LoadingDialogUtil.show(context, R.string.select_organisation_query_info);
            UserService.getInstance().getGroupsTree(HostApplication.getApplication().getCurrentUser());

        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (event.getId() == ResponseEventStatus.USER_GET_PATROL_TREE) {
            LoadingDialogUtil.dismiss();
            EventBusUtil.unregister(this);
            if (event.isOK()) {
                topOrganisation = event.getData();
                topOrganisation = topOrganisation.getChildren().get(0);
//                cacheAllUsers(topOrganisation);
                Bundle bundle = new Bundle();
                bundle.putSerializable(SelectGroupAndUserActivity.INTENT_KEY_ORGANISATIONS, topOrganisation);
                bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_MULTI_SELECT, isMultiSelection());
                bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_SELECT_PEOPLE, true);
                Intent intent = new Intent(context, SelectGroupAndUserActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, SELECT_CONTACT_MAN);
            } else {
                ToastUtil.showLong(event.getMessage());
            }
        }
    }

    private void cacheAllUsers(OrganisationSelection topOrganisation) {
        id2Users = new HashMap<String, OrganisationSelection>();
        for (OrganisationSelection group : topOrganisation.getChildren()) {
            if (!group.isUser()) {
                cacheUsersInGroup(group);
            }
        }
    }

    private void cacheUsersInGroup(OrganisationSelection group) {
        List<OrganisationSelection> children = group.getChildren();
        if (ListUtil.isEmpty(children)) {
            return;
        }

        for (OrganisationSelection child : children) {
            if (child.isUser()) {
                id2Users.put(String.valueOf(child.getId()), child);
            } else {
                cacheUsersInGroup(child);
            }
        }
    }
}
