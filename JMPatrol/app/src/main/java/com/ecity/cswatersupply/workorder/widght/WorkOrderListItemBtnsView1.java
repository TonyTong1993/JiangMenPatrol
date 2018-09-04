package com.ecity.cswatersupply.workorder.widght;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.MenuFactory;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderDataSourceProvider;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderFormUploader;
import com.ecity.cswatersupply.workorder.view.FormActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工单列表按钮展示区
 */
public class WorkOrderListItemBtnsView1 extends LinearLayout {
    private Context context;
    private boolean hasMeasured;
    private WorkOrder currentWorkOrder;
    private List<WorkOrderBtnModel> btns = new ArrayList<>();
    public static final int MARGIN_BTN = ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_6);

    public WorkOrderListItemBtnsView1(Context context) {
        this(context, null);
    }

    public WorkOrderListItemBtnsView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(HORIZONTAL);
    }

    private void initView() {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorderlist_item_btns, this, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!hasMeasured) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            hasMeasured = true;
            if (0 == btns.size()) {
                return;
            }
            int btnwidth = getSingleBtnWidth(width);
            // 根据测量出的宽度计算每个按钮的宽度
            addBtnView(btnwidth);
        }
    }

    private int getSingleBtnWidth(int width) {
        int btnCount = btns.size();
        if (0 == btnCount) {
            return 0;
        }
        width -= (btnCount + 1) * MARGIN_BTN;// 每个Btn两个margin
        int btnwidth = 0;
        if (btnCount == 1) {
            btnwidth = width >> 1; // 一个按钮时，让按钮宽度约为屏幕二分之一
        } else {
            btnwidth = width / btnCount;
        }
        return btnwidth;
    }

    public void setView(WorkOrder data) {
        this.currentWorkOrder = data;
        initView();
        if (data != null && null != data.getAttributes()) {
            Map<String, String> attributes = data.getAttributes();
            if (attributes.containsKey(WorkOrder.KEY_STATE)) {
                parseRealBtns(currentWorkOrder);
                if (0 == btns.size()) {
                    setVisibility(View.GONE);
                }
            }
            if (hasMeasured) {// 初次渲染，没有测量，会进入onMeasure()
                int btnwidth = getSingleBtnWidth(getWidth());
                addBtnView(btnwidth);
            }
        } else {
            setVisibility(View.GONE); // 如果工单属性有错，不显示按钮
        }
    }

    private void parseRealBtns(WorkOrder currentWorkOrder) {
        this.btns.clear();
        List<WorkOrderBtnModel> workOrderBtns = currentWorkOrder.getWorkOrderBtns();
        if (workOrderBtns == null || 0 == workOrderBtns.size()) {
            return;
        }
        for (WorkOrderBtnModel btn : workOrderBtns) {
            this.btns.add(btn);
        }
    }

    /**
     * 动态添加按钮跟分割线
     *
     * @param widthSize
     */
    @SuppressLint("InflateParams")
    private void addBtnView(int widthSize) {
        int btnCount = btns.size();
        int i = 0;
        if (widthSize == 0 || btnCount == 0) {
            return;
        }
        for (final WorkOrderBtnModel btn : btns) {
            View btnView = LayoutInflater.from(getContext()).inflate(R.layout.view_workorderlist_item_btn, null);
            TextView tv_btn = (TextView) btnView.findViewById(R.id.tv_btn);
            tv_btn.setText(btn.getTaskName());

            int height = getResources().getDimensionPixelSize(R.dimen.activity_title_height);
            LayoutParams params = new LayoutParams(widthSize, height);
            // 一个按钮时，让按钮变瘦些
            if (btnCount == 1) {
                DisplayMetrics dm = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int margin = (screenWidth - widthSize) >> 1;
                params.setMargins(margin, 0, margin, 0);// 当一个按钮时，让按钮变得瘦些
            }
            // 为了让按钮左右间隔一样
            else if (i == 0) {
                params.setMargins(MARGIN_BTN, 0, MARGIN_BTN >> 1, 0);
            } else if (i == btnCount - 1) {
                params.setMargins(MARGIN_BTN >> 1, 0, MARGIN_BTN, 0);
            } else {
                params.setMargins(MARGIN_BTN >> 1, 0, MARGIN_BTN >> 1, 0);
            }
            tv_btn.setLayoutParams(params);

            addView(btnView);
            // 第一次开始渲染按钮的时候就要加事件监听
            setListener(btnView, btn);
            i++;
        }
    }

    public void setOnBtnClickListener(WorkOrder data) {
        resetOnClickListener(this.btns);
    }

    private void resetOnClickListener(List<WorkOrderBtnModel> btns2) {
        if (btns2.size() == 0) {
            return;
        }
        // 这里要注意，第一次要进入该分组的时候，childCount是为0的，因为此时还没有开始渲染按钮，所以要在addBtnView()中加事件监听
        int childCount = getChildCount();
        int btnIndex = 0;
        int btnCount = btns2.size();
        for (int i = 0; i < childCount; i++) {
            // 重设按钮监听事件
            View childView = getChildAt(i);
            if (childView instanceof LinearLayout) {
                // 因为前面设定过，如果复用，一定是工单状态相同的情况，则按钮数量肯定相同，所以这里不会出现超界
                final WorkOrderBtnModel workOrderBtn = btns2.get(btnIndex % btnCount);
                setListener(childView, workOrderBtn);
                btnIndex++;
            }
        }
    }

    private void setListener(final View view, final WorkOrderBtnModel workOrderBtn) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                List<WorkOrderBtnModel> subWorkOrderBtns = workOrderBtn.getSubWorkOrderBtns();
                if (null != subWorkOrderBtns && 0 != subWorkOrderBtns.size()) {
                    UIHelper.showMoreWorkOrderOperationPopup1(context, view, subWorkOrderBtns);
                } else {
                    WorkOrderDataSourceProvider provider = new WorkOrderDataSourceProvider();
                    provider.setTaskBtn(workOrderBtn);
                    FormActivity.sourceProvider = provider;
                    WorkOrderFormUploader uploader = new WorkOrderFormUploader();
                    uploader.setTaskBtn(workOrderBtn);
                    FormActivity.uploader = uploader;
                    Bundle bundle = new Bundle();
                    bundle.putString(CustomViewInflater.REPORT_TITLE, workOrderBtn.getTaskName());
                    bundle.putString(FormActivity.REPORT_TITLE_RIGHT_TXT, ResourceUtil.getStringById(R.string.form_data_reporta));
                    UIHelper.startActivityWithExtra(FormActivity.class, bundle);
                }
            }
        });
    }

}
