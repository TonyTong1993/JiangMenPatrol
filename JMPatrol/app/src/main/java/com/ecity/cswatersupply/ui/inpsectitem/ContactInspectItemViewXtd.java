package com.ecity.cswatersupply.ui.inpsectitem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.android.contactmanchooser.ChooseContactManActivity;
import com.ecity.android.contactmanchooser.model.ContactMan;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderContactManChooser;
import com.z3app.android.util.StringUtil;

// TODO:一个界面有多个选人的控件，怎么处理？
public class ContactInspectItemViewXtd extends ABaseInspectItemView {
    public static final String CONTACT_USER_INFO_SEPARATOR = "--";

    private boolean isSingleChoice;
    private static Map<String, ArrayList<ContactMan>> mContactMen;
    private TextView tvContactMen;
    // TODO: 怎么得到当前工单，替换掉CustomViewInflater里的？
    public static WorkOrder mCurrentWorkOrder1;
    // TODO: 同mCurrentWorkOrder1
    public static String currentWorkOrderButtonKey1;

    @Override
    protected void setup(View view) {
        isSingleChoice = mInspectItem.getType().equals(EInspectItemType.CONTACTMEN_SINGLE);

        if (CustomViewInflater.getContactsFilter() != null) {
            mContactMen = CustomViewInflater.getContactsFilter();
        } else {
            mContactMen = new HashMap<String, ArrayList<ContactMan>>();
        }
        storeContactMen(mInspectItem);
        tvContactMen = (TextView) view.findViewById(R.id.content_choosed_main_man);
        showExistingValue(tvContactMen, mInspectItem);

        if (!mInspectItem.isEdit()) {
            return;
        }

        view.findViewById(R.id.ll_contact_men).setOnClickListener(new ContactMenOnClickListener(view, isSingleChoice, mInspectItem.getName(), R.id.content_choosed_main_man));
        ArrayList<ContactMan> contactMen = parseContactMen(mInspectItem);
        if (!ListUtil.isEmpty(contactMen)) {
            String names = getConcatenatedContactManNames(contactMen);
            tvContactMen.setText(names);
            WorkOrderContactManChooser.setContactMen(contactMen);
            mContactMen.put(mInspectItem.getName(), contactMen);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_contact_men;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseContactManActivity.REQCONTACT) {
            if (null != data) {
                int chooseMode = data.getExtras().getInt(ChooseContactManActivity.KEY_CHOOSE_MODE);
                String itemName = data.getExtras().getString("itemName");
                @SuppressWarnings("unchecked")
                List<ContactMan> datas = (List<ContactMan>) data.getExtras().getSerializable(ChooseContactManActivity.KEY_CHOOSED_MEN);
                mContactMen.put(itemName, (ArrayList<ContactMan>) datas);
                setContactValue(itemName, datas, chooseMode);
            }
        }
    }

    private static String buildChoosedMenName(String defaultValue, List<ContactMan> choosedMen2) {
        String result = defaultValue;
        if (choosedMen2.size() != 0) {
            StringBuilder name = new StringBuilder();
            for (ContactMan contactMan : choosedMen2) {
                name.append(contactMan.getName() + "、");
            }
            name.deleteCharAt(name.length() - 1);// 把最后的“、”删除
            result = name.toString();
        }
        return result;
    }

    private void setContactValue(String itemName, List<ContactMan> datas, int chooseMode) {
        String nameInfo = buildChoosedMenName(mInspectItem.getDefaultValue(), datas);
        tvContactMen.setText(nameInfo);

        String value = "";
        if (chooseMode == ChooseContactManActivity.MULTIPLE_CHOICE) {
            value = buildAssistMenIdsJsonArr(datas);
        } else if (datas.size() != 0) {
            value = String.valueOf(getUserValue(datas.get(0)));
        }
        mInspectItem.setValue(value);
    }

    private String buildAssistMenIdsJsonArr(List<ContactMan> choosedAssistMen) {
        JSONArray jsonArr = new JSONArray();
        if (choosedAssistMen != null) {
            for (ContactMan contactMan : choosedAssistMen) {
                jsonArr.put(getUserValue(contactMan));
            }
        }
        return jsonArr.toString();
    }

    private String getUserValue(ContactMan contact) {
        return contact.getUserid() + CONTACT_USER_INFO_SEPARATOR + contact.getName();
    }

    /**
     * 检查是否已有当前项的已选列表，如果没有，初始化
     */
    private void storeContactMen(InspectItem item) {
        String name = item.getName();
        if (!mContactMen.containsKey(name)) {
            mContactMen.put(name, new ArrayList<ContactMan>());
        }
    }

    private void showExistingValue(TextView textView, InspectItem inspectItem) {
        if (StringUtil.isBlank(inspectItem.getValue())) {
            textView.setText(inspectItem.getDefaultValue());
            return;
        }

        if (inspectItem.getType() == EInspectItemType.CONTACTMEN_SINGLE) {
            textView.setText(getSingleContactName(inspectItem.getValue()));
            return;
        }

        JSONArray jsonArray = JsonUtil.getJsonArray(inspectItem.getValue());
        if (jsonArray == null) {
            return;
        }

        String text = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.optString(i);
            if (text.length() > 0) {
                text += ",";
            }
            text += getSingleContactName(value);
        }
        textView.setText(text);
    }

    private String getSingleContactName(String value) {
        String[] array = value.split(CONTACT_USER_INFO_SEPARATOR);
        return (array.length > 1) ? array[1] : value;
    }

    private class ContactMenOnClickListener implements OnClickListener {

        private View view;
        private boolean isSingleChoice;
        private String itemName;
        private int targetResId;

        public ContactMenOnClickListener(View view, boolean isSingleChoice, String itemName, int targetResId) {
            this.view = view;
            this.isSingleChoice = isSingleChoice;
            this.itemName = itemName;
            this.targetResId = targetResId;
        }

        @Override
        public void onClick(View v) {
            view.findViewById(targetResId).setTag(itemName);// 用检查项名字区分多个联系人Type
            Bundle bundle = makeContactBundle(isSingleChoice, targetResId, itemName, mContactMen.get(itemName));
            Intent intent = new Intent(context, ChooseContactManActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, ChooseContactManActivity.REQCONTACT);
        }
    }

    private Bundle makeContactBundle(boolean isSingleChoice, int targetResId, String itemName, ArrayList<ContactMan> choosedMen) {
        Bundle bundle = new Bundle();
        int titleId = setContactTitle(itemName);
        Map<String, String> contactFilterParams = null;
        putBundleData(bundle, titleId, itemName, isSingleChoice, choosedMen);
        makeLoginUserFilterMen(HostApplication.getApplication().getCurrentUser());
        setContactManFilter(contactFilterParams, bundle, titleId, itemName, choosedMen);

        return bundle;
    }

    private int setContactTitle(String itemName) {
        int titleId = -1;
        if ("assign_main_workman".equalsIgnoreCase(itemName)) {
            titleId = R.string.title_workorder_dispatch_mainworker;
        } else if ("assign_assistances".equalsIgnoreCase(itemName)) {
            titleId = R.string.title_workorder_dispatch_assistances;
        } else if ("transfer_receiver".equalsIgnoreCase(itemName)) {
            titleId = R.string.title_workorder_transfer_selectpeople;
        } else if ("reportfinish_deal_person".equalsIgnoreCase(itemName)) {
            titleId = R.string.title_workorder_select_workers;
        } else if (itemName.equalsIgnoreCase("audit_userid")) {
            titleId = R.string.title_workorder_aduit;
        } else if (itemName.equalsIgnoreCase("TASKPERSON")) {
            titleId = R.string.title_workorder_event_to_task;
        } else if (itemName.equalsIgnoreCase("XUNJMAN")) {
            titleId = R.string.planningtask_person;
        } else {
            titleId = R.string.title_workorder_dispatch;
        }

        return titleId;
    }

    private void putBundleData(Bundle bundle, int titleId, String itemName, boolean isSingleChoice, ArrayList<ContactMan> choosedMen) {
        bundle.putInt(ChooseContactManActivity.KEY_TITLE, titleId);
        bundle.putString("itemName", itemName);
        int choiceType = isSingleChoice ? ChooseContactManActivity.SINGLE_CHOICE : ChooseContactManActivity.MULTIPLE_CHOICE;
        bundle.putInt(ChooseContactManActivity.KEY_CHOOSE_MODE, choiceType);
        bundle.putSerializable(ChooseContactManActivity.KEY_CHOOSED_MEN, choosedMen);
        bundle.putString(ChooseContactManActivity.KEY_CORE_NAME, WorkOrderContactManChooser.class.getName());
        bundle.putString(ChooseContactManActivity.KEY_CORE_NAME, WorkOrderContactManChooser.class.getName());
    }

    private void makeLoginUserFilterMen(User user) {
        if (user.getGid() == null) {
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(user.getGid());
        } catch (NumberFormatException e) {
            LogUtil.e(this, e);
            return;
        }
        ContactMan contactModel = new ContactMan();
        contactModel.setUserid(userId);
        ArrayList<ContactMan> loginFilter = new ArrayList<ContactMan>();
        loginFilter.add(contactModel);
        mContactMen.put("filter_login", loginFilter);
    }

    private void setContactManFilter(Map<String, String> contactFilterParams, Bundle bundle, int titleId, String itemName, ArrayList<ContactMan> choosedMen) {
        if (itemName.equalsIgnoreCase("transfer_receiver")) {// 转办:是班长，转给别家班长;不是班长，转给本组其他人员
            if (HostApplication.getApplication().getCurrentUser().isLeader()) {
                bundle.putBoolean(ChooseContactManActivity.KEY_ONLY_MONITOR, true);
                contactFilterParams = getLeaderFilterRule();
                bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_MEN, mContactMen.get("filter_login"));
            } else {
                contactFilterParams = getFilterRule();
                bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_MEN, mContactMen.get("filter_login"));
            }
        } else if (itemName.equalsIgnoreCase("assign_main_workman")) {// 分派:派给本组其他人员 
            contactFilterParams = getFilterRule();// 选主办人时，过滤已选协办人
            bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_MEN, mContactMen.get("assign_assistances"));
        } else if (itemName.equalsIgnoreCase("assign_assistances")) {
            contactFilterParams = getFilterRule();
            ArrayList<ContactMan> filterContacts = new ArrayList<ContactMan>();
            ArrayList<ContactMan> workOrderRelevantContacts = getWorkOrderRelevantContacts();
            ArrayList<ContactMan> mainAssignContact = mContactMen.get("assign_main_workman");
            filterContacts.addAll(workOrderRelevantContacts); // 班长审核协助申请时，选协助人，需要过滤掉工单的主办人和协办人
            if (mainAssignContact != null) {
                filterContacts.addAll(mainAssignContact); // 班长分派工单，选协助人时，要过滤掉主办人
            }
            bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_MEN, filterContacts);
        } else if (itemName.equalsIgnoreCase("audit_userid")) {//延期／协助／退单
            contactFilterParams = getAuditFilterRule();
            bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_MEN, mContactMen.get("assign_assistances"));
        } else if (itemName.equalsIgnoreCase("TASKPERSON")) {
            bundle.putSerializable(ChooseContactManActivity.KEY_CHOOSE_MEN_REQUEST_TYPE, ChooseContactManActivity.KEY_CHOOSE_MEN_REQUEST_TYPE);
        }else if (itemName.equalsIgnoreCase("XUNJMAN")) {
            bundle.putSerializable(ChooseContactManActivity.KEY_CHOOSE_MEN_REQUEST_TYPE, ChooseContactManActivity.KEY_CHOOSE_MEN_REQUEST_TYPE);
        }
        if (contactFilterParams != null) {
            if (CustomViewInflater.getCurrentWorkOrderButtonKey() != null) {
                contactFilterParams.put("btnname", CustomViewInflater.getCurrentWorkOrderButtonKey());
            }
            bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_RULE, (Serializable) contactFilterParams);
        }
    }

    private Map<String, String> getLeaderFilterRule() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("processinstanceid", mCustomInflater.getCurrentWorkOrder().getAttribute(WorkOrder.KEY_ID));
        params.put("userid", HostApplication.getApplication().getCurrentUser().getId());

        return params;
    }

    private Map<String, String> getFilterRule() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("groupid", HostApplication.getApplication().getCurrentUser().getGroupId());
        params.put("userid", HostApplication.getApplication().getCurrentUser().getId());
        params.put("processinstanceid", mCustomInflater.getCurrentWorkOrder().getAttribute(WorkOrder.KEY_ID));

        return params;
    }

    /**
     * 获取当前操作工单的主办人和协办人。
     * @return
     */
    private ArrayList<ContactMan> getWorkOrderRelevantContacts() {
        ArrayList<ContactMan> contacts = new ArrayList<ContactMan>();
        ArrayList<ContactMan> mainAssignee = mContactMen.get(CustomViewInflater.KEY_CONTACT_MAN_FILTER_MAIN_ASSIGNEE);
        ArrayList<ContactMan> assistantAssignees = mContactMen.get(CustomViewInflater.KEY_CONTACT_MAN_FILTER_ASSISTANT_ASSIGNEES);
        if (mainAssignee != null) {
            contacts.addAll(mainAssignee);
        }
        if (assistantAssignees != null) {
            contacts.addAll(assistantAssignees);
        }

        return contacts;
    }

    private Map<String, String> getAuditFilterRule() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("groupid", "audit");
        params.put("userid", HostApplication.getApplication().getCurrentUser().getId());
        params.put("processinstanceid", mCustomInflater.getCurrentWorkOrder().getAttribute(WorkOrder.KEY_ID));

        return params;
    }

    private ArrayList<ContactMan> parseContactMen(InspectItem item) {
        ArrayList<ContactMan> contactMen = new ArrayList<ContactMan>();
        List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(item);
        for (InspectItemSelectValue selectedValue : selectValues) {
            ContactMan man = new ContactMan();
            man.setUserid(Integer.valueOf(selectedValue.gid));
            man.setName(selectedValue.name);
            man.setPhone("");
            contactMen.add(man);
        }

        return contactMen;
    }

    private String getConcatenatedContactManNames(List<ContactMan> men) {
        if (ListUtil.isEmpty(men)) {
            return "";
        }

        StringBuilder strBuilder = new StringBuilder();
        for (ContactMan man : men) {
            if (strBuilder.length() > 0) {
                strBuilder.append(context.getString(R.string.delimeter1));
            }
            strBuilder.append(man.getName());
        }

        return strBuilder.toString();
    }
}
