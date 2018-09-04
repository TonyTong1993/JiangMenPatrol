package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.android.contactmanchooser.ChooseContactManActivity;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.project.activity.SelectGroupAndUserActivity;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

public class Contact4ProjectInspectItemViewXtd extends ABaseInspectItemView {
    public static final String CONTACT_USER_INFO_SEPARATOR = "|";
    public static String requestUrl;
    public static Map<String, String> parameters;
    private OrganisationSelection topOrganisation;
    private List<OrganisationSelection> selectedOrganisations;
    private TextView tvContactMen;

    @Override
    protected void setup(View view) {
        if (!mInspectItem.isEdit()) {
            return;
        }

        setupTitle(mInspectItem, view);
        tvContactMen = (TextView) view.findViewById(R.id.content_choosed_main_man);
        view.findViewById(R.id.ll_contact_men).setOnClickListener(new ContactMenOnClickListener());
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_contact_men;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseContactManActivity.REQCONTACT) {
            if (data == null) {
                return;
            }

            Bundle bundle = data.getExtras();
            selectedOrganisations = (List<OrganisationSelection>) bundle.getSerializable(SelectGroupAndUserActivity.INTENT_KEY_SELECTED_ORGANISATIONS);
            showSelectedContacts(selectedOrganisations);
        }
    }

    private void showSelectedContacts(List<OrganisationSelection> selectedOrganisations) {
        StringBuilder displayBuilder = new StringBuilder();
        JSONArray valueArray = new JSONArray();
        for (OrganisationSelection selection : selectedOrganisations) {
            if (displayBuilder.length() > 0) {
                displayBuilder.append(",");
            }
            displayBuilder.append(selection.getName());
            String value = selection.getId() + CONTACT_USER_INFO_SEPARATOR + selection.getName();
            valueArray.put(value);
        }

        tvContactMen.setText(displayBuilder);
        mInspectItem.setValue(valueArray.toString());
    }

    public void onEventMainThread(ResponseEvent event) {
        if (event.getId() == ResponseEventStatus.USER_GET_PATROL_TREE) {
            LoadingDialogUtil.dismiss();
            EventBusUtil.unregister(this);
            if (!event.isOK()) {
                ToastUtil.showLong(event.getMessage());
                return;
            }
            topOrganisation = event.getData();
            gotoSelectUserScreen();
        }
    }

    private class ContactMenOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            EventBusUtil.register(Contact4ProjectInspectItemViewXtd.this);
            LoadingDialogUtil.show(context, R.string.select_organisation_query_info);
            ProjectService.getInstance().getGroupsTrees(HostApplication.getApplication().getCurrentUser());
        }
    }

    private void gotoSelectUserScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SelectGroupAndUserActivity.INTENT_KEY_ORGANISATIONS, topOrganisation);
        bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_SELECT_PEOPLE, true);
        bundle.putBoolean(SelectGroupAndUserActivity.INTENT_KEY_IS_MULTI_SELECT, mInspectItem.getType().equals(EInspectItemType.CONTACTMEN_MULTIPLE_PROJECT));
        Intent intent = new Intent(context, SelectGroupAndUserActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ChooseContactManActivity.REQCONTACT);
    }
}