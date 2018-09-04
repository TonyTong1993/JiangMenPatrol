package com.ecity.cswatersupply.workorder.widght;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;
import com.z3app.android.util.StringUtil;

/**
 * 此类描述的是：工单列表自定义布局
 * 
 * @author: gaokai
 * @version: 2016年2月25日 上午11:20:30
 */

public class WorkOrderListItemCustomView extends RelativeLayout {
    private Context context;

    public WorkOrderListItemCustomView(Context context) {
        this(context, null);
    }

    public WorkOrderListItemCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    // 根据分组显示布局
    public void setView(WorkOrder data, WorkOrderGroupEnum group) {
        removeAllViews();
        switch (group) {
            case NOHANDLE:
                initView(R.layout.view_workordergroup_no_handle);
                break;
            case HANDLLING:
                initView(R.layout.view_workordergroup_handlling);
                break;
            case COMLETE:
//                initView(R.layout.view_workordergroup_complete);
                break;
            default:
                break;
        }
    }

    public void setDisplayInfo(WorkOrder data, WorkOrderGroupEnum group) {
        Map<String, String> attributes = data.getAttributes();
        switch (group) {
            case NOHANDLE:
                setInfoOfGroupNoHandle(attributes);
                break;
            case HANDLLING:
                setInfoOfGroupHandled(attributes);
                break;
            case COMLETE:
//                setInfoOfGroupComplete(attributes);
                break;
            default:
                break;
        }
    }

    private void initView(int layoutResId) {
        LayoutInflater.from(context).inflate(layoutResId, this, true);// 最后一个参数为true：则把该View添加到RootView中，如果为false，需要手动addView
        int dp_10 = getResources().getDimensionPixelSize(R.dimen.padding_spacing_level_5);
        //        int dp_20 = getResources().getDimensionPixelSize(R.dimen.padding_spacing_level_3);
        this.setPadding(dp_10, 0, dp_10, dp_10);
    }

    private void setInfoOfGroupNoHandle(Map<String, String> attributes) {
//        DisplayValueOfView(R.id.tv_fanying_region,WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_FANYING_REGION));
        DisplayValueOfView(R.id.content_time_for_dispatching, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_DISPATCH_TIME));
        DisplayValueOfView(R.id.content_man_who_dispatched, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_DISPATCHER));
        showCheckOpinion(attributes);
    }

    private void setInfoOfGroupHandled(Map<String, String> attributes) {
        setInfoOfGroupNoHandle(attributes);
        DisplayValueOfView(R.id.content_time_deadline, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_ASKFINISH_TIME));
    }

    private void setInfoOfGroupComplete(Map<String, String> attributes) {
        setInfoOfGroupHandled(attributes);
        DisplayValueOfView(R.id.content_time_complete, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_COMLETE_TIME));
    }

    private void DisplayValueOfView(int resId, String value) {
        View v = findViewById(resId);
        if (v == null) {
            return;
        }
        if (WorkOrderUtil.isEmptyStr(v, value)) {
            value = ResourceUtil.getStringById(R.string.item_empty);
        }
        if (v instanceof TextView) {
            ((TextView) v).setText(value);
        }
    }

    private void showCheckOpinion(Map<String, String> attributes) {
        TextView tvOpinionTitle = (TextView) findViewById(R.id.label_check_opinion);
        TextView tvOpinionContent = (TextView) findViewById(R.id.content_check_opinion);
        if ((tvOpinionTitle == null) || (tvOpinionContent == null)) {
            return;
        }

        String subState = attributes.get(WorkOrder.KEY_SUB_STATE);
        String opinionKey = getCheckOpinionKey(subState);
        if (StringUtil.isEmpty(opinionKey)) {
            tvOpinionTitle.setVisibility(View.GONE);
            tvOpinionContent.setVisibility(View.GONE);
        } else {

            String value = ResourceUtil.getStringById(R.string.item_empty);

            if( ! StringUtil.isBlank(attributes.get(opinionKey))) {
                value = attributes.get(opinionKey);
            }
            tvOpinionContent.setText(value);
        }
    }

    private String getCheckOpinionKey(String status) {
        String key = "";

        if (StringUtil.isBlank(status)) {
            return key;
        }

        if (status.equals(WorkOrderState.ASSIS_PASS.value) || status.equals(WorkOrderState.ASSIS_NOT_PASS.value)) {
            return WorkOrder.KEY_ASSIST_CHECK_OPINION;
        }
        if (status.equals(WorkOrderState.TRANSFER_PASS.value) || status.equals(WorkOrderState.TRANSFER_NOT_PASS.value)) {
            return WorkOrder.KEY_TRANSFER_CHECK_OPINION;
        }
        if (status.equals(WorkOrderState.RETURN_PASS.value) || status.equals(WorkOrderState.RETURN_NOT_PASS.value)) {
            return WorkOrder.KEY_RETURN_CHECK_OPINION;
        }
        if (status.equals(WorkOrderState.DELAY_PASS.value) || status.equals(WorkOrderState.DELAY_NOT_PASS.value)) {
            return WorkOrder.KEY_DELAY_CHECK_OPINION;
        }

        return key;
    }
}
