package com.ecity.cswatersupply.workorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.contactmanchooser.ChooseContactManActivity;
import com.ecity.android.contactmanchooser.model.ContactMan;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.BuildConfig;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemSelectValueAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderContactManChooser;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class WorkOrderBackAuditActivity extends BaseActivity {
    private static final int TRANSFER = 100;
    private static final int DISPATCH = 200;
    private static final int BACK2SYSTEM = 300;
    private static final int TEXT_MAX_LENGTH = 80;

    private int nextOperation = -1;
    private boolean isContactRequired = false;
    private WorkOrder currentWorkOrder;
    private CustomTitleView viewTitle;
    private List<InspectItem> inspectItems;
    /**
     * 后续处理
     */
    private View itemViewAfterDo;
    /**
     * 选择联系人
     */
    private View itemViewChooseMen;
    private ArrayList<ContactMan> choosedMen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_backaudit);
        EventBusUtil.register(this);
        handleIntent();
        initTitle();
        getDataSource();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ChooseContactManActivity.REQCONTACT:
                if (null != data) {
                    int chooseMode = data.getExtras().getInt(ChooseContactManActivity.KEY_CHOOSE_MODE);
                    String itemName = data.getExtras().getString("itemName");
                    this.choosedMen = (ArrayList<ContactMan>) data.getExtras().getSerializable(ChooseContactManActivity.KEY_CHOOSED_MEN);
                    setContactValue(itemName, choosedMen, chooseMode);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        if (BuildConfig.DEBUG) {
            RefWatcher refWatcher = HostApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onActionButtonClicked(View v) {
        if (hasEmptyItem()) {
            Toast.makeText(this, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        submit2Server();
    }

    private void getDataSource() {
        LoadingDialogUtil.show(this, R.string.page_loading);
        Map<String, String> params = new HashMap<String, String>();
        params.put("assignee", HostApplication.getApplication().getCurrentUser().getId());
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID));
        params.put(WorkOrder.KEY_SUB_STATE, WorkOrderState.RETURN.value);
        WorkOrderService.instance.getMainWorkFlowFormData(params);
    }

    private void submit2Server() {
        LoadingDialogUtil.show(this, ResourceUtil.getStringById(R.string.please_wait));
        LoadingDialogUtil.setCancelable(false);
        Map<String, String> params = new HashMap<String, String>();
        JSONObject json = buildAuditJson(inspectItems);
        params.put("properties", json.toString());
        params.put("assignee", HostApplication.getApplication().getCurrentUser().getId());
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID));
        WorkOrderService.instance.handleWorkOrder(ServiceUrlManager.getInstance().getSubmitFormDataUrl(), ResponseEventStatus.WORKORDER_REPORT_TABLE, params);
    }

    private boolean hasEmptyItem() {
        if (ListUtil.isEmpty(inspectItems)) {
            return false;
        }

        InspectItem agreementItem = findItemWithKey("isApproval");
        boolean isAgree = "1".equals(agreementItem.getValue());
        if (StringUtil.isBlank(agreementItem.getValue())) {
            return true;
        }
        if (isContactRequired && isAgree) {
            InspectItem backPerson = findItemWithKey("back_person");
            if (StringUtil.isBlank(backPerson.getValue())) {
                return true;
            }
        }

        InspectItem followingProcessItem = findItemWithKey("back_next");
        if (isAgree && StringUtil.isBlank(followingProcessItem.getValue())) {
            return true;
        }

        for (InspectItem item : inspectItems) {
            if ("back_next".equals(item.getName())) {
                continue;
            }

            if (item.isRequired() && (StringUtil.isBlank(item.getValue()) || item.getValue().equals("[]"))) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return “同意”、“不同意”检查项
     */
    private InspectItem findItemWithKey(String alias) {
        for (InspectItem item : inspectItems) {
            if (alias.equals(item.getName())) {
                return item;
            }
        }

        return null;
    }

    private JSONObject buildAuditJson(List<InspectItem> inspectItems2) {
        JSONObject json = new JSONObject();
        try {
            json.putOpt(WorkOrder.KEY_SUB_STATE, WorkOrderState.RETURN.value);
        } catch (JSONException e) {
            LogUtil.e("WorkOrderOperator", e);
        }
        putOptInspectItemData(json, inspectItems2);
        return json;
    }

    private void putOptInspectItemData(JSONObject json, List<InspectItem> inspectItems2) {
        for (InspectItem inspectItem : inspectItems2) {
            if (inspectItem.getType() == EInspectItemType.IMAGE) {
                continue;
            }

            if (!inspectItem.isEdit()) {
                continue;
            }

            try {
                json.putOpt(inspectItem.getName(), inspectItem.getValue());
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }
    }

    private void setContactValue(String itemName, ArrayList<ContactMan> choosedMen, int chooseMode) {
        Iterator<InspectItem> iterator = inspectItems.iterator();
        String value = "";
        while (iterator.hasNext()) {
            InspectItem item = iterator.next();
            if (!item.getName().equalsIgnoreCase(itemName)) {
                continue;
            }
            String nameInfo = buildChoosedMenName(item.getDefaultValue(), choosedMen);
            ((TextView) itemViewChooseMen.findViewById(R.id.content_choosed_main_man)).setText(nameInfo);

            if (choosedMen.size() != 0) {
                value = String.valueOf(choosedMen.get(0).getUserid());
            }
            item.setValue(value);
        }
    }

    private String buildChoosedMenName(String defaultValue, ArrayList<ContactMan> choosedMen2) {
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

    private void initView() {
        View view = null;
        TextView tvTitle = null;
        LinearLayout llContainer = (LinearLayout) findViewById(R.id.ll_container);
        for (InspectItem item : inspectItems) {
            switch (item.getType()) {
                case WORKORDER_CODE:
                    view = initWorkOrderCodeView(view, tvTitle, item);
                case TEXT:
                    view = initTextView(view, tvTitle, item);
                    break;
                case TEXTEXT:
                    view = initTextView(view, tvTitle, item);
                    break;
                case DIVIDER_THICK:
                    view = getLayoutInflater().inflate(R.layout.item_divider_thick, null);
                    break;
                case RADIO:
                    view = initRadioView(view, tvTitle, item);
                    break;
                case CONTACTMEN_SINGLE:
                    view = initContactMenView(tvTitle, item, ChooseContactManActivity.SINGLE_CHOICE);
                    break;
                default:
                    break;
            }
            if (view != null) {
                llContainer.addView(view);
            }
        }
    }

    private View initContactMenView(TextView tvTitle, InspectItem item, int singleChoice) {
        this.choosedMen = new ArrayList<ContactMan>();
        itemViewChooseMen = getLayoutInflater().inflate(R.layout.custom_form_item_contact_men_1, null);
        itemViewChooseMen.setVisibility(View.GONE);
        TextView tvStar = (TextView) itemViewChooseMen.findViewById(R.id.tv_item_star);
        if (!item.isRequired()) {
            tvStar.setVisibility(View.GONE);
        }
        tvTitle = (TextView) itemViewChooseMen.findViewById(R.id.tv_item_title);
        setText(tvTitle, item.getAlias(), item.getName());
        TextView tvContactMen = (TextView) itemViewChooseMen.findViewById(R.id.content_choosed_main_man);
        setText(tvContactMen, item.getValue(), item.getDefaultValue());
        itemViewChooseMen.findViewById(R.id.ll_contact_men).setOnClickListener(new ContactMenOnClickListener(singleChoice, item.getName(), R.id.content_choosed_main_man));
        return itemViewChooseMen;
    }

    private class ContactMenOnClickListener implements OnClickListener {

        private int choiceType;
        private String itemName;
        private int targetResId;

        public ContactMenOnClickListener(int choiceType, String itemName, int targetResId) {
            this.choiceType = choiceType;
            this.itemName = itemName;
            this.targetResId = targetResId;
        }

        @Override
        public void onClick(View v) {
            findViewById(targetResId).setTag(itemName);// 用检查项名字区分多个联系人Type
            Bundle bundle = makeContactBundle(choiceType, targetResId, itemName, choosedMen);
            Intent intent = new Intent(WorkOrderBackAuditActivity.this, ChooseContactManActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, ChooseContactManActivity.REQCONTACT);
        }
    }

    private View initRadioView(View view, TextView tvTitle, InspectItem item) {
        view = getLayoutInflater().inflate(R.layout.custom_form_item_radiogroup, null);
        RadioGroup rgGroup = (RadioGroup) view.findViewById(R.id.rg_custom);
        rgGroup.setOrientation(RadioGroup.VERTICAL);
        if (item.getName().equals("isApproval")) { // 退单申请
            rgGroup.setOrientation(LinearLayout.HORIZONTAL);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            rgGroup.setLayoutParams(layoutParams);
        } else if (item.getName().equals("back_next")) {// 后续处理
            itemViewAfterDo = view;
            itemViewAfterDo.setVisibility(View.GONE);
        }
        TextView tvStar = (TextView) view.findViewById(R.id.tv_item_star);
        if (!item.isRequired()) {
            tvStar.setVisibility(View.GONE);
        }
        tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
        if (!StringUtil.isBlank(item.getAlias())) {
            tvTitle.setText(item.getAlias());
        } else {
            tvTitle.setText(item.getName());
        }
        rgGroup.setOnCheckedChangeListener(new MyRadioGroupOnClickListener(item, rgGroup));
        return view;
    }

    private Bundle makeContactBundle(int choiceType, int targetResId, String itemName, ArrayList<ContactMan> choosedMen2) {
        Bundle bundle = new Bundle();
        bundle.putInt(ChooseContactManActivity.KEY_TITLE, R.string.title_workorder_choosemen);
        bundle.putString("itemName", itemName);
        bundle.putInt(ChooseContactManActivity.KEY_CHOOSE_MODE, choiceType);
        bundle.putSerializable(ChooseContactManActivity.KEY_CHOOSED_MEN, choosedMen2);
        bundle.putString(ChooseContactManActivity.KEY_CORE_NAME, WorkOrderContactManChooser.class.getName());
        if (nextOperation == TRANSFER) {// 转给别家班长
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("groupid", HostApplication.getApplication().getCurrentUser().getGroupId());
            params.put("userid", HostApplication.getApplication().getCurrentUser().getId());
            params.put("getMonitor", String.valueOf(true));
            bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_RULE, params);
        } else if (nextOperation == DISPATCH) {// 派给自家兄弟
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("groupid", HostApplication.getApplication().getCurrentUser().getGroupId());
            params.put("userid", HostApplication.getApplication().getCurrentUser().getId());
            bundle.putSerializable(ChooseContactManActivity.KEY_FILTER_RULE, params);
        }
        return bundle;
    }

    enum CustomRadioType {
        VERTICAL, HORIZONTAL
    }

    private class MyRadioGroupOnClickListener implements OnCheckedChangeListener {
        private InspectItem item;

        public MyRadioGroupOnClickListener(InspectItem item, RadioGroup radioGroup) {
            this.item = item;
            JSONArray jsonArray = JsonUtil.getJsonArray(item.getSelectValues());
            if (jsonArray == null) {
                return;
            }

            List<InspectItemSelectValue> selectValueLists = InspectItemSelectValueAdapter.adapt(jsonArray);
            if (ListUtil.isEmpty(selectValueLists)) {
                return;
            }

            CustomRadioType cType = CustomRadioType.VERTICAL;
            if (item.getAlias().equalsIgnoreCase(getResources().getString(R.string.label_after_back))) {
                cType = CustomRadioType.VERTICAL;
            }

            for (int i = 0; i < selectValueLists.size(); i++) {
                RadioButton tempButton = new RadioButton(WorkOrderBackAuditActivity.this);
                tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tempButton.setTextColor(ResourceUtil.getColorById(R.color.txt_black_normal));
                tempButton.setText(selectValueLists.get(i).name);
                tempButton.setTag(R.id.tag_key, item.getName() + selectValueLists.get(i).gid);
                tempButton.setTag(R.id.tag_value, selectValueLists.get(i).gid);
                // tempButton.setButtonDrawable(R.drawable.selecor_radio_button);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (selectValueLists.size() == 2 && cType == CustomRadioType.HORIZONTAL) {
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    tempButton.measure(w, h);
                    int width = tempButton.getMeasuredWidth();
                    int marginEdge = (displayMetrics.widthPixels >> 2) - (width >> 1) - radioGroup.getPaddingLeft(); // 到屏幕边界的距离
                    int marginCenter = (displayMetrics.widthPixels >> 2) - (width >> 1);// 到屏幕中心线的距离
                    if (i == 0) {
                        layoutParams.setMargins(marginEdge, 0, marginCenter, 0); // 使每个radioButton位于屏幕的四分之一处
                    } else {
                        layoutParams.setMargins(marginCenter, 0, marginEdge, 0);
                    }
                    radioGroup.addView(tempButton, layoutParams);
                } else {
                    layoutParams.setMargins(0, 20, 0, 20);
                    radioGroup.addView(tempButton, layoutParams);
                }
                if (item.getValue().equalsIgnoreCase(selectValueLists.get(i).gid)) {
                    radioGroup.check(tempButton.getId());
                }
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempButton = (RadioButton) findViewById(checkedId);
            //            if (tempButton.isChecked()) {
            //                return;
            //            }

            item.setValue(String.valueOf(tempButton.getTag(R.id.tag_value)));
            Object tagKey = tempButton.getTag(R.id.tag_key);
            if (tagKey.equals("isApproval0")) { // 不同意
                itemViewAfterDo.setVisibility(View.GONE);
                itemViewChooseMen.setVisibility(View.GONE);
            } else if (tagKey.equals("isApproval1")) {// 同意
                itemViewAfterDo.setVisibility(View.VISIBLE);
                toBottom();
                if (nextOperation == TRANSFER || nextOperation == DISPATCH) {
                    itemViewChooseMen.setVisibility(View.VISIBLE);
                } else {
                    itemViewChooseMen.setVisibility(View.GONE);
                }
            } else if (tagKey.equals("back_next0")) {// 转单
                isContactRequired = true;
                nextOperation = TRANSFER;
                itemViewChooseMen.setVisibility(View.VISIBLE);
                toBottom();
            } else if (tagKey.equals("back_next1")) {// 分派
                isContactRequired = true;
                nextOperation = DISPATCH;
                itemViewChooseMen.setVisibility(View.VISIBLE);
                toBottom();
            } else if (tagKey.equals("back_next2")) {// 退单
                isContactRequired = false;
                nextOperation = BACK2SYSTEM;
                itemViewChooseMen.setVisibility(View.GONE);
            }
        }
    }

    private View initTextView(View view, TextView tvTitle, InspectItem item) {
        view = getLayoutInflater().inflate(R.layout.custom_form_item_text, null);
        TextView tvStar = (TextView) view.findViewById(R.id.tv_item_star);
        if (!item.isRequired()) {
            tvStar.setVisibility(View.GONE);
        }
        tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
        if (!StringUtil.isBlank(item.getAlias())) {
            tvTitle.setText(item.getAlias());
        } else {
            tvTitle.setText(item.getName());
        }
        EditText etValue = (EditText) view.findViewById(R.id.et_item_value);
        InputFilter[] filters = { new InputFilter.LengthFilter(TEXT_MAX_LENGTH) };
        etValue.setFilters(filters);

        if (!StringUtil.isBlank(item.getValue())) {
            etValue.setText(item.getValue());
        } else if (!StringUtil.isBlank(item.getDefaultValue())) {
            etValue.setText(item.getDefaultValue());
        } else {
            if (!item.isEdit()) {
                etValue.setHint("");
            }
        }
        etValue.addTextChangedListener(new MyEditTextListener(item));
        etValue.setEnabled(item.isEdit());
        return view;
    }

    private void toBottom() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ((ScrollView) findViewById(R.id.scroll_container)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private View initWorkOrderCodeView(View view, TextView tvTitle, InspectItem item) {
        view = getLayoutInflater().inflate(R.layout.item_code, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
        setText(tvTitle, item.getAlias(), item.getName());
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        setText(tvContent, item.getValue(), item.getDefaultValue());
        view.findViewById(R.id.rl_body).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle workOrder = new Bundle();
                workOrder.putSerializable(WorkOrder.KEY_SERIAL, currentWorkOrder);
                UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity.class, workOrder);
            }
        });
        return view;
    }

    private void setText(TextView v, String value, String defaultValue) {
        if (!StringUtil.isBlank(value)) {
            v.setText(value);
        } else {
            v.setText(defaultValue);
        }
    }

    private void initTitle() {
        viewTitle = (CustomTitleView) findViewById(R.id.customTitleView1);
        viewTitle.setTitleText(ResourceUtil.getStringById(R.string.workorder_backaudit_title));
        viewTitle.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        viewTitle.setRightActionBtnText(R.string.form_data_reporta);
    }

    private void handleIntent() {
        currentWorkOrder = (WorkOrder) getIntent().getExtras().getSerializable(WorkOrder.KEY_SERIAL);
    }

    private void handleInspectItems(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            finish();
            return;
        }
        this.inspectItems = event.getData();
        initView();
    }

    private void handleReportTableDone(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            LoadingDialogUtil.dismiss();
            return;
        } else {
            afterReport(null);
        }
    }

    private void afterReport(Message msg) {
        LoadingDialogUtil.setCancelable(true);
        LoadingDialogUtil.dismiss();
        if (msg != null) {
            Toast.makeText(this, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
        String successMsg = ResourceUtil.getStringById(R.string.audit_return_workorder_status);
        EventBusUtil.post(new UIEvent(UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT, UIEventStatus.OK, currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID), successMsg, null));
        finish();
    }

    public void onEventMainThread(ResponseEvent event) {
        switch (event.getId()) {
            case ResponseEventStatus.GET_WORKORDER_INSPECT_ITEMS:
                handleInspectItems(event);
                break;
            case ResponseEventStatus.WORKORDER_REPORT_TABLE:
                handleReportTableDone(event);
                break;
            default:
                break;
        }
    }

    private class MyEditTextListener implements TextWatcher {
        private InspectItem item;

        public MyEditTextListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no logic to do.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no logic to do.
        }

        @Override
        public void afterTextChanged(Editable s) {
            item.setValue(s.toString());
        }
    }
}
