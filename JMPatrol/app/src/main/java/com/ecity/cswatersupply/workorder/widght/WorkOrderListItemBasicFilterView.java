/**
 * 文件名：WorkOrderListCommonView.java
 *
 * 版本信息：
 * 日期：2016年2月25日
 * Copyright Ecity 2016 
 * 版权所有
 *
 */

package com.ecity.cswatersupply.workorder.widght;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.PhoneUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;

/**
 * 此类描述的是：工单列表基本信息区布局
 * 
 * @author: gaokai
 * @version: 2016年2月25日 上午11:20:30
 */

public class WorkOrderListItemBasicFilterView extends RelativeLayout {
    private Context context;

    public WorkOrderListItemBasicFilterView(Context context) {
        this(context, null);
    }

    public WorkOrderListItemBasicFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    // 根据分组显示布局
    public void setView(WorkOrder data) {
        removeAllViews();
        initView(R.layout.view_workordergroup_basic);
    }

    public void setDisplayInfo(WorkOrder data, WorkOrderGroupEnum group) {
        Map<String, String> attributes = data.getAttributes();
        setBasicInfo(attributes, group);
    }

    private void initView(int layoutResId) {
        LayoutInflater.from(context).inflate(layoutResId, this, true);// 最后一个参数为true：则把该View添加到RootView中，如果为false，需要手动addView
        int dp_8 = getResources().getDimensionPixelSize(R.dimen.lv_item_padding_up_down_level_3);
        int dp_10 = getResources().getDimensionPixelSize(R.dimen.padding_spacing_level_5);
        this.setPadding(dp_10, dp_8, dp_10, 0);
    }

    private void setBasicInfo(Map<String, String> attributes, WorkOrderGroupEnum group) {
        findViewById(R.id.tv_phoneNum).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PhoneUtil.call(context, ((TextView) v).getText().toString());
            }
        });

        DisplayValueOfView(R.id.tv_code, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_CODE));
        DisplayValueOfView(R.id.iv_workorder_from, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_FROM_ALIAS));
        DisplayValueOfView(R.id.tv_content, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_CONTENT));
        DisplayValueOfView(R.id.tv_address, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_ADDRESS));
        DisplayValueOfView(R.id.tv_phoneNum, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_PHONE));
        DisplayValueOfView(R.id.content_workorder_state, WorkOrderUtil.getWorkOrderStateString(attributes));

        TextView tvRemarkLabel = (TextView) findViewById(R.id.label_remark);
        TextView tvRemark = (TextView) findViewById(R.id.tv_remark);
        switch (group) {
            case NOHANDLE:
                tvRemarkLabel.setVisibility(View.VISIBLE);
                tvRemark.setVisibility(View.VISIBLE);
                DisplayValueOfView(R.id.tv_remark, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_REMARK));
                break;
            default:
                tvRemarkLabel.setVisibility(View.GONE);
                tvRemark.setVisibility(View.GONE);
                break;
        }

        TextView ivWorkOrderFrom = (TextView) findViewById(R.id.iv_workorder_from);
        WorkOrderUtil.setBackGroundOfWorkOrderFrom(ivWorkOrderFrom);

        TextView ivWorkOrderIsCooperate = (TextView) findViewById(R.id.iv_workorder_iscooperate);
        if (isCooperate(WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_ASSIST_MAN))) {
            ivWorkOrderIsCooperate.setVisibility(View.VISIBLE);
        } else {
            ivWorkOrderIsCooperate.setVisibility(View.GONE);
        }
    }

    private boolean isCooperate(String aliasOfValue) {
        String[] cooperaters = aliasOfValue.split(",");
        List<String> cooperaterList = new ArrayList<String>();
        Collections.addAll(cooperaterList, cooperaters);
        return cooperaterList.contains(HostApplication.getApplication().getCurrentUser().getTrueName());
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
}
