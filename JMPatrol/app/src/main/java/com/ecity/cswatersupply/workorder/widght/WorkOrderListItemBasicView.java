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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.PhoneUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.z3app.android.util.StringUtil;

import static com.ecity.cswatersupply.utils.DateUtil.convertTimeString2Date;
import static com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum.COMLETE;
import static com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum.NOHANDLE;

/**
 * 此类描述的是：工单列表基本信息区布局
 * 
 * @author: gaokai
 * @version: 2016年2月25日 上午11:20:30
 */

public class WorkOrderListItemBasicView extends RelativeLayout {
    private Context context;
    public static final int MAX_TIME_GAP = 6 * 60 *60;
    public static final int WORK_ORDER_GOING_TO_BEYOND_DEADLINE = 0;
    public static final int WORK_ORDER_BEYONDED_DEADLINE = 1;
    public static final int WORK_ORDER_NOT_BEYOND_DEADLINE = 2;

    public WorkOrderListItemBasicView(Context context) {
        this(context, null);
    }

    public WorkOrderListItemBasicView(Context context, AttributeSet attrs) {
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
                String phoneTxts = ((TextView) v).getText().toString();
                List<String> phones = PhoneUtil.getPhoneNumbers(phoneTxts);
                List<String> filterPhones = PhoneUtil.filterBlankStr(phones);
                if(null == filterPhones || 0 == filterPhones.size()) {
                    ToastUtil.showShort(R.string.error_phone_format);
                    return;
                }
                String[] phoneItems = new String[filterPhones.size()];
                phoneItems = ListUtil.toArray(filterPhones, phoneItems);
                if(null == phoneItems || 0 == phoneItems.length) {
                    return;
                }
                if(1 == phoneItems.length) {
                    PhoneUtil.call(context, phoneItems[0]);
                } else {
                    showListDialog(phoneItems);
                }
            }
        });

        DisplayValueOfView(R.id.tv_code, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_CODE));
        DisplayValueOfView(R.id.iv_workorder_from, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_FROM_ALIAS));
        DisplayValueOfView(R.id.tv_content, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_CONTENT));
        DisplayValueOfView(R.id.tv_address, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_ADDRESS));
        DisplayValueOfView(R.id.tv_phoneNum, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_PHONE));
        DisplayValueOfView(R.id.content_workorder_state, WorkOrderUtil.getWorkOrderStateString(attributes));
        //约期
        DisplayValueOfView(R.id.content_workorder_fixtime, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_FIXTIME));
        DisplayValueOfView(R.id.tv_remark, WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_REMARK));

        TextView tvRemarkLabel = (TextView) findViewById(R.id.label_remark);
        TextView tvRemark = (TextView) findViewById(R.id.tv_remark);
        TextView tvPhoneNumLabel = (TextView) findViewById(R.id.label_phoneNum);
        TextView tvPhoneNum = (TextView) findViewById(R.id.tv_phoneNum);
        TextView tvFixTimeLabel = (TextView) findViewById(R.id.label_workorder_fixtime);
        TextView tvFixTime = (TextView) findViewById(R.id.content_workorder_fixtime);
        tvFixTimeLabel.setVisibility(View.GONE);
        tvFixTime.setVisibility(View.GONE);
        switch (group) {
            case NOHANDLE:
                tvRemarkLabel.setVisibility(View.VISIBLE);
                tvRemark.setVisibility(View.VISIBLE);
                break;
            case COMLETE:
                tvPhoneNumLabel.setVisibility(View.GONE);
                tvPhoneNum.setVisibility(View.GONE);
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

        TextView tvBeyondDeadlineLabel = (TextView) findViewById(R.id.iv_workorder_is_beyound_deadline);
        String deadLineTime = WorkOrderUtil.getAliasOfValue(attributes, WorkOrder.KEY_ASKFINISH_TIME);
        Drawable drawable;
        int jugdgeState = judgeGapOfTime(deadLineTime);
        if (group == NOHANDLE || group == COMLETE || jugdgeState == WORK_ORDER_NOT_BEYOND_DEADLINE || -1 == jugdgeState) {
            tvBeyondDeadlineLabel.setVisibility(View.GONE);
        } else if (jugdgeState == WORK_ORDER_GOING_TO_BEYOND_DEADLINE) {
            drawable = context.getResources().getDrawable(R.drawable.css_bg_orange_corner);
            tvBeyondDeadlineLabel.setVisibility(View.VISIBLE);
            tvBeyondDeadlineLabel.setText(ResourceUtil.getStringById(R.string.label_work_order_going_to_beyond_deadline));
            tvBeyondDeadlineLabel.setBackground(drawable);
        } else if (jugdgeState == WORK_ORDER_BEYONDED_DEADLINE) {
            drawable = context.getResources().getDrawable(R.drawable.css_bg_red_corner);
            tvBeyondDeadlineLabel.setVisibility(View.VISIBLE);
            tvBeyondDeadlineLabel.setText(ResourceUtil.getStringById(R.string.label_work_order_beyond_deadline));
            tvBeyondDeadlineLabel.setBackground(drawable);
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

    private void showListDialog(final String[] items) {
        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        listDialog.setTitle(R.string.link_num);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneUtil.call(context, items[which]);
            }
        });
        listDialog.show();
    }

    private int judgeGapOfTime(String deadlineTime) {
        String currentTime = DateUtil.getCurrentTime();
        if (StringUtil.isBlank(deadlineTime)) {
            return -1;
        }
        int secondsBetween = DateUtil.secondsBetween(convertTimeString2Date(currentTime), convertTimeString2Date(deadlineTime));
        if (secondsBetween > 0 && secondsBetween < MAX_TIME_GAP) {
            return WORK_ORDER_GOING_TO_BEYOND_DEADLINE;//即将超期
        } else if (secondsBetween < 0 || secondsBetween == 0) {
            return WORK_ORDER_BEYONDED_DEADLINE;//已超期
        } else if (secondsBetween > MAX_TIME_GAP || secondsBetween == MAX_TIME_GAP) {
            return WORK_ORDER_NOT_BEYOND_DEADLINE;//未超期
        }
        return -1;
    }
}
