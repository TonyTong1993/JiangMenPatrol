package com.ecity.cswatersupply.workorder.widght;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.MenuFactory;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.xg.util.NotificationUtil;

/**
 * 工单列表按钮展示区
 * 
 * @author gaokai
 *
 */
public class WorkOrderListItemBtnsView extends LinearLayout {
    private Context context;
    private boolean hasMeasured;
    private WorkOrder currentWorkOrder;
    private List<AppMenu> btns = new ArrayList<AppMenu>();
    public static final int MARGIN_BTN = ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_6);
    private String nextStepId;//来自某一消息的nextStepId

    public WorkOrderListItemBtnsView(Context context) {
        this(context, null);
    }

    public WorkOrderListItemBtnsView(Context context, AttributeSet attrs) {
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

    public void setView(WorkOrder data, String nextStepId) {
        this.currentWorkOrder = data;
        this.nextStepId = nextStepId;
        initView();
        if (data != null && null != data.getAttributes()) {
            Map<String, String> attributes = data.getAttributes();
            if (attributes.containsKey(WorkOrder.KEY_STATE)) {
                parseRealBtns(attributes);
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

    private void parseRealBtns(Map<String, String> attributes) {
        this.btns.clear();
        Map<String, AppMenu> allBtns = MenuFactory.getWorkOrderBtns();
        String btnStr = attributes.get(WorkOrder.KEY_OPERATE_BTNS);
        if (btnStr == null || btnStr.isEmpty()) {
            return;
        }
        String[] btns = btnStr.split(",");
        boolean hasMoreBtn = false;
        for (String btnName : btns) {
            if (allBtns.containsKey(btnName)) {
                if (btnName.contains("more") && !hasMoreBtn) { // 如果出现更多系按钮，在首项放入更多按钮，待点击更多按钮时，再解析更多按钮
                    this.btns.add(0, allBtns.get("more"));
                    hasMoreBtn = true;
                } else if (btnName.contains("more") && hasMoreBtn) {
                    continue;
                } else {
                    AppMenu menu = allBtns.get(btnName);
                    this.btns.add(menu);
                }
            }
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
        for (final AppMenu btn : btns) {
            View btnView = LayoutInflater.from(getContext()).inflate(R.layout.view_workorderlist_item_btn, null);
            TextView tv_btn = (TextView) btnView.findViewById(R.id.tv_btn);
            tv_btn.setText(btn.getName());

            int height = getResources().getDimensionPixelSize(R.dimen.activity_title_height);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthSize, height);
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
            final Bundle data = getBunldeData(currentWorkOrder);
            setListener(btnView, btn, data);
            i++;
        }
    }

    public void setOnBtnClickListener(WorkOrder itemData, WorkOrderGroupEnum group) {
        final Bundle data = getBunldeData(itemData);
        resetOnClickListener(this.btns, data);
    }

    private void resetOnClickListener(List<AppMenu> btns2, final Bundle data) {
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
                final AppMenu menu = btns2.get(btnIndex % btnCount);
                setListener(childView, menu, data);
                btnIndex++;
            }
        }
    }

    private void setListener(final View view, final AppMenu menu, final Bundle bundle) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                NotificationUtil.clearNotificationById(context, NotificationUtil.SPECIAL_NOTIFICATION);
                //clearNotification(currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID));
                bundle.putString(WorkOrder.INTENT_KEY_CLICKED_BUTTON, menu.getSubName());
                menu.getAMenuCommand().executeWithExtra(v, bundle);
            }
        });
    }

    private Bundle getBunldeData(WorkOrder workOrder) {
        final Bundle data = new Bundle();
        data.putSerializable(WorkOrder.KEY_SERIAL, workOrder);
        data.putString(WorkOrder.KEY_ID, workOrder.getAttribute(WorkOrder.KEY_ID));

        return data;
    }

    private void clearNotification(String id) {
        if (null != nextStepId && nextStepId.equals(id)) {
            NotificationUtil.clearNotification(context);
        }
    }
}
