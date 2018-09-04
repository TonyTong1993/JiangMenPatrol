package com.ecity.cswatersupply.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderOperator;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecity.cswatersupply.model.RequestCode.SELECT_CONTACT_MAN;

public class WorkOrderBackTransferAuditActivity extends BaseActivity {
    private WorkOrder currentWorkOrder;
    private List<InspectItem> inspectItems;
    /**
     * 主办人
     */
    private View itemViewMainMan;
    /**
     * 协办人
     */
    private View itemViewAssitMan;
    private static final String MAIN_CONTACT_ITEM_KEY = "back_assign_man";
    private static final String ASSIT_CONTACT_ITEM_KEY = "back_assist_man";
    private CustomViewInflater viewInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_backaudit);
        EventBusUtil.register(this);
        handleIntent();
        initTitle();
        getDataSource();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_CONTACT_MAN) {
            viewInflater.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        if (BuildConfig.DEBUG) {
            // 测试有无内存泄漏，打包时要删除掉
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
        params.put(WorkOrder.KEY_SUB_STATE, WorkOrderState.BACK_TRANSFER_APPLY.value);
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
        if (StringUtil.isBlank(agreementItem.getValue())) {
            return true;
        }

        boolean isAgree = "1".equals(agreementItem.getValue());
        if (isAgree) {
            InspectItem item = findItemWithKey(MAIN_CONTACT_ITEM_KEY);
            if (StringUtil.isBlank(item.getValue())) {
                return true;
            }
        }

        for (InspectItem item : inspectItems) {
            if (!isAgree) {
                break; // 选择驳回，不再检测主办人和协办人
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
            json.putOpt(WorkOrder.KEY_SUB_STATE, WorkOrderState.BACK_TRANSFER_APPLY.value);
        } catch (JSONException e) {
            LogUtil.e(this, e);
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

            String value = "";
            if ((inspectItem.getType() == EInspectItemType.CONTACTMEN_MULTIPLE) || inspectItem.getType() == EInspectItemType.CONTACTMEN_SINGLE|| inspectItem.getType() == EInspectItemType.ORG || inspectItem.getType() == EInspectItemType.ORGM) {
                if (StringUtil.isBlank(inspectItem.getValue())) {
                    continue;
                }
                value = WorkOrderOperator.getContactValue(inspectItem);
            } else {
                value = inspectItem.getValue();
            }

            try {
                json.putOpt(inspectItem.getName(), value);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }
    }

    private void initView() {
        InspectItem contactItem = new InspectItem();
        contactItem.setType(EInspectItemType.ORG);
        contactItem.setName(MAIN_CONTACT_ITEM_KEY);
        contactItem.setAlias(getString(R.string.choose_main_man));
        contactItem.setRequired(true);
        contactItem.setEdit(true);
        inspectItems.add(contactItem);

        contactItem = new InspectItem();
        contactItem.setType(EInspectItemType.ORGM);
        contactItem.setName(ASSIT_CONTACT_ITEM_KEY);
        contactItem.setAlias(getString(R.string.choose_assist_man));
        contactItem.setRequired(false);
        contactItem.setEdit(true);
        inspectItems.add(contactItem);

        View view = null;
        LinearLayout llContainer = (LinearLayout) findViewById(R.id.ll_container);
        for (InspectItem item : inspectItems) {
            switch (item.getType()) {
                case WORKORDER_CODE: {
                    viewInflater.setCurrentWorkOrder(currentWorkOrder);
                    view = viewInflater.inflate(item);
                    break;
                }
                case TEXT:
                    view = viewInflater.inflate(item);
                    break;
                case TEXTEXT:
                    view = viewInflater.inflate(item);
                    break;
                case DIVIDER_THICK:
                    view = viewInflater.inflate(item);
                    break;
                case RADIO:
                    view = initRadioView(item);
                    break;
                case ORG:
                    view = viewInflater.inflate(item);
                    itemViewMainMan = view;
                    break;
                case ORGM:
                    view = viewInflater.inflate(item);
                    itemViewAssitMan = view;
                    break;
                default:
                    break;
            }

            if (view != null) {
                llContainer.addView(view);
            }
        }
    }

    private View initRadioView(InspectItem item) {
        View view = getLayoutInflater().inflate(R.layout.custom_form_item_radiogroup, null);
        RadioGroup rgGroup = (RadioGroup) view.findViewById(R.id.rg_custom);
        if (item.getName().equals("isApproval")) { // 退单申请
            rgGroup.setOrientation(LinearLayout.HORIZONTAL);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            rgGroup.setLayoutParams(layoutParams);
        }
        TextView tvStar = (TextView) view.findViewById(R.id.tv_item_star);
        if (!item.isRequired()) {
            tvStar.setVisibility(View.GONE);
        }
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
        if (!StringUtil.isBlank(item.getAlias())) {
            tvTitle.setText(item.getAlias());
        } else {
            tvTitle.setText(item.getName());
        }
        rgGroup.setOnCheckedChangeListener(new MyRadioGroupOnClickListener(item, rgGroup));
        return view;
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

            CustomRadioType cType = CustomRadioType.HORIZONTAL;
            if (item.getAlias().equalsIgnoreCase(getResources().getString(R.string.label_after_back))) {
                cType = CustomRadioType.VERTICAL;
            }

            for (int i = 0; i < selectValueLists.size(); i++) {
                RadioButton tempButton = new RadioButton(WorkOrderBackTransferAuditActivity.this);
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

            item.setValue(String.valueOf(tempButton.getTag(R.id.tag_value)));
            Object tagKey = tempButton.getTag(R.id.tag_key);
            if (tagKey.equals("isApproval0")) { // 不同意
                itemViewMainMan.setVisibility(View.GONE);
                itemViewAssitMan.setVisibility(View.GONE);
            } else if (tagKey.equals("isApproval1")) {// 同意
                toBottom();
                itemViewMainMan.setVisibility(View.VISIBLE);
                itemViewAssitMan.setVisibility(View.VISIBLE);
            }
        }
    }

    private void toBottom() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ((ScrollView) findViewById(R.id.scroll_container)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void initTitle() {
        CustomTitleView viewTitle = (CustomTitleView) findViewById(R.id.customTitleView1);
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

        viewInflater = new CustomViewInflater(this);
        this.inspectItems = event.getData();
        initView();
    }

    private void handleReportTableDone(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            LoadingDialogUtil.dismiss();
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
}
