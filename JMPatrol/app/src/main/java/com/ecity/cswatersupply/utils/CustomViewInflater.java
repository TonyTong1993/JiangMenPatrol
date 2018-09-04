package com.ecity.cswatersupply.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.ecity.android.contactmanchooser.model.ContactMan;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.ui.inpsectitem.ABaseInspectItemView;
import com.ecity.cswatersupply.ui.inpsectitem.AudioInspectItemViewXtd;
import com.ecity.cswatersupply.ui.inpsectitem.InspectItemViewFactory;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.utils.MediaCacheManager;

public class CustomViewInflater {
    public static final String INTENT_KEY_POINT_PART = "INTENT_KEY_POINT_PART";//要上报检查项的点的id
    public static final String INTENT_KEY_LINE_PART = "INTENT_KEY_LINE_PART";//要上报检查项的线的id
    public static final String REPORT_TITLE = "REPORT_TITLE";
    public static final String REPORT_TITLE_PARENT = "REPORT_TITLE_PARENT";
    public static final String BOTTOM_TOOLBAR_MODE = "BOTTOM_TOOLBAR_MODE"; // 底部按钮模式
    public static final String BOTTOM_SINLEBTN_TXT = "REPORT_BOTTOM_SINLEBTN_TXT"; // 单按钮文字
    public static final String BOTTOM_TWOBTN_NEGATIVE_TXT = "negative"; // 双按钮模式下，消极按钮文字
    public static final String BOTTOM_TWOBTN_POSITIVE_TXT = "positive";// 双按钮模式下，积极按钮文字
    public static final String REPORT_CHILD_ITEMS = "REPORT_CHILD_ITEMS";
    public static final String SIGN_IN_DATE_ITEM = "SIGN_IN_DATE_ITEM";//签到
    public static final String REPORT_COMFROM = "REPORT_COMFROM";
    public static final String REPORT_COORDINATE = "REPORT_COORDINATE";
    public static final String REPORT_MULTI_CHILD_IDENTIFY = "REPORT_MULTI_CHILD_IDENTIFY";
    public static final String EVENTTYPE = "EVENTTYPE";
    public static final String EVENTID = "EVENTID";
    public static final String EVENT_LEAK_CURRENT_SELECT_DEVICE = "EVENT_LEAK_CURRENT_SELECT_DEVICE";
    public static final String KEY_CONTACT_MAN_FILTER_MAIN_ASSIGNEE = "KEY_CONTACT_MAN_FILTER_MAIN_ASSIGNER";
    public static final String KEY_CONTACT_MAN_FILTER_ASSISTANT_ASSIGNEES = "KEY_CONTACT_MAN_FILTER_ASSISTANT_ASSIGNERS";
    public static final String KEY_IS_EARTHQUAKE_INSPECTITEMS = "KEY_IS_EARTHQUAKE_INSPECTITEMS";
    public static final String PATHS = "paths";

    public static final int SINGLE_BTN = 10000; // 单按钮模式
    public static final int TWO_BTNS = 20000;// 双按钮模式
    public static final int NO_BTN = 30000;//无底部上传按钮
    public static final int GROUPS = 40000;
    public static final String INTENT_GROUPS = "INTENT_GROUPS";//筛选返回

    private Activity mCurrentActivity;
    private WorkOrder currentWorkOrder;
    private static String currentWorkOrderButtonKey;
    private static Map<String, ArrayList<ContactMan>> contactMen = new HashMap<String, ArrayList<ContactMan>>();

    public static PatrolPosition mPatrolPosition;
    private static List<ABaseInspectItemView> inspectItemViews = new ArrayList<ABaseInspectItemView>();
    /**
     * 调用了startActivityForResult的项，等待接收onActivityResult的返回结果
     */
    private ABaseInspectItemView pendingInspectItem;
    /**
     * 调用startActivityForResult的CustomViewInflater。在多个tab的情况下，用于区分不同tab下的实例。
     */
    public static CustomViewInflater pendingViewInflater;

    public CustomViewInflater(Activity activity) {
        mCurrentActivity = activity;
        inspectItemViews.clear();
    }

    public static void releaseResources() {
        mPatrolPosition = null;
        currentWorkOrderButtonKey = null;
        inspectItemViews.clear();
        if (null != contactMen) {
            contactMen.clear();
        }

        if (null != MediaCacheManager.imgdrr) {
            MediaCacheManager.imgdrr.clear();
        }
        if (null != MediaCacheManager.imgbmp) {
            MediaCacheManager.imgbmp.clear();
        }
    }

    public ArrayList<AudioModel> getAudiomodels() {
        AudioInspectItemViewXtd itemView = getAudioInspectItemView();
        return (itemView == null) ? null : itemView.getAudioModels();
    }

    public View inflate(InspectItem item) {
        if (item.getType().equals(EInspectItemType.ATTACHMENT)&&item.isEdit()){
            item.setType(EInspectItemType.ATTACHMENT_UPLOAD);
        }
        ABaseInspectItemView itemView = InspectItemViewFactory.getInstance().getInspectItemView(item.getType());
        if (itemView == null) {
            return null;
        }

        inspectItemViews.add(itemView);

        return itemView.inflate(mCurrentActivity, this, item);
    }

    public void stopPlay() {
        AudioInspectItemViewXtd itemView = getAudioInspectItemView();
        if (itemView != null) {
            itemView.stopPlay();
        }
    }

    /**
     * 在使用的activity的onAcitivyResult中调用此方法
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (pendingInspectItem != null) {
            pendingInspectItem.handleOnActivityResult(requestCode, resultCode, data);
        } 
    }

    /**
     * 设置选人界面需要过滤的人。
     * @param filterKey 过滤人的key
     * @param filterContacts 需要被过滤掉的人
     */
    public static void setContactsFilter(String filterKey, ArrayList<ContactMan> filterContacts) {
        contactMen.put(filterKey, filterContacts);
    }

    public static Map<String, ArrayList<ContactMan>> getContactsFilter() {
        return contactMen;
    }

    public static void setCurrentWorkOrderButtonKey(String currentWorkOrderButtonKey) {
        CustomViewInflater.currentWorkOrderButtonKey = currentWorkOrderButtonKey;
    }

    public static String getCurrentWorkOrderButtonKey() {
        return currentWorkOrderButtonKey;
    }

    private AudioInspectItemViewXtd getAudioInspectItemView() {
        for (ABaseInspectItemView itemView : inspectItemViews) {
            if (itemView.getInspectItem().getType().equals(EInspectItemType.AUDIO)) {
                return (AudioInspectItemViewXtd) itemView;
            }
        }

        return null;
    }

    public void setPendingInspectItemView(ABaseInspectItemView itemView) {
        this.pendingInspectItem = itemView;
    }

    public WorkOrder getCurrentWorkOrder() {
        return currentWorkOrder;
    }

    public void setCurrentWorkOrder(WorkOrder currentWorkOrder) {
        this.currentWorkOrder = currentWorkOrder;
    }
}
