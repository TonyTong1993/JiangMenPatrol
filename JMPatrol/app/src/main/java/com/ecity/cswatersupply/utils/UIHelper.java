package com.ecity.cswatersupply.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.MenuFactory;
import com.ecity.cswatersupply.ui.dialog.InputDialog;
import com.ecity.cswatersupply.ui.dialog.InputDialog.IOkBtnCallback;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderDataSourceProvider;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderFormUploader;
import com.ecity.cswatersupply.workorder.view.FormActivity;
import com.ecity.cswatersupply.workorder.widght.CustomPopupBtn;
import com.ecity.cswatersupply.workorder.widght.CustomPopupMenu;
import com.ecity.cswatersupply.workorder.widght.CustomPopupMenu.OnMenuClickListener;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationType;

public class UIHelper {

    public static void startActivityWithoutExtra(Class<?> clazz) {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), clazz);
        startActivityWithItent(intent);
    }

    public static void startActivityWithExtra(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), clazz);
        intent.putExtras(bundle);
        startActivityWithItent(intent);
    }

    public static void startActivityForResultWithExtra(Class<?> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), clazz);
        intent.putExtras(bundle);
        HostApplication.getApplication().getAppManager().currentActivity().startActivityForResult(intent, requestCode);
    }
    
    public static void startActivityWithItent(Intent intent) {
        if(null == intent){
            return;
        }
        
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);
    }

    public static void showReturnDialog(Context context, final Bundle bundle) {
        Context mContext = new WeakReference<Context>(context).get();

        InputDialog returnWorkOrderDialog = new InputDialog(mContext, R.style.common_dialog, new IOkBtnCallback() {

            @Override
            public void onOk(String remark) {
                EventBusUtil.post(new UIEvent(UIEventStatus.WORKORDER_RETURNDIALOG_OK, UIEventStatus.OK, bundle, remark, null));
            }
        });
        returnWorkOrderDialog.setTitle(R.string.dialog_return);
        returnWorkOrderDialog.setEtvHint(R.string.hint_return_reason);
        returnWorkOrderDialog.show();
    }

    public static void updateNotification(Notification notification) {
        String key = getNotificationVadgeViewKey(notification.getType().toString());
        if (key != null) {
            PropertyChangeManager.getInstance().updateNotificationCountByKey(key);
        }
    }

    public static void showMoreWorkOrderOperationPopup(Activity activity, final View v, Bundle bundle) {
        Activity context = new WeakReference<Activity>(activity).get();
        final WorkOrder workOrder = (WorkOrder) bundle.getSerializable(WorkOrder.KEY_SERIAL);
        List<AppMenu> menus = getMenu(workOrder);
        final CustomPopupMenu moreBtnPopup = new CustomPopupMenu(context, menus);
        int itemCount = menus.size();
        int padding = ResourceUtil.getDimensionPixelSizeById(R.dimen.lv_item_padding_up_down_level_1);
        int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_morebtn_popmenu_item_w) + (padding << 1);
        int height = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_morebtn_popmenu_item_h) * itemCount + (itemCount + 1) * padding;
        moreBtnPopup.initPopup(width, LayoutParams.WRAP_CONTENT);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        // 在屏幕上半边
        if (moreBtnPopup.atTopHalfOfScreen(location)) {
            moreBtnPopup.setAnimation(R.style.workorder_more_btn_popup_tophalf_anim);
            moreBtnPopup.showAtLocation(v, (location[0] + v.getWidth() / 2) - width / 2, location[1] + v.getHeight() + 5, Gravity.NO_GRAVITY);
        } else { // 在屏幕下半边
            moreBtnPopup.setAnimation(R.style.workorder_more_btn_popup_bottomhalf_anim);
            moreBtnPopup.showAtLocation(v, (location[0] + v.getWidth() / 2) - width / 2, location[1] - height - 5, Gravity.NO_GRAVITY);
        }
        moreBtnPopup.setOnActionItemClickListener(new OnMenuClickListener() {

            @Override
            public void onMenuItemClick(AppMenu menu, int pos) {
                final Bundle data = new Bundle();
                data.putSerializable(WorkOrder.KEY_SERIAL, workOrder);
                data.putString(WorkOrder.INTENT_KEY_CLICKED_BUTTON, menu.getSubName());
                menu.getAMenuCommand().executeWithExtra(v, data);
            }
        });
    }

    /***
     * 新的服务下，点击工单列表的组合按钮
     * @param context
     * @param v
     * @param subBtns
     */
    public static void showMoreWorkOrderOperationPopup1(Context context, final View v, List<WorkOrderBtnModel> subBtns) {
        final CustomPopupBtn moreBtnPopup = new CustomPopupBtn(context, subBtns);
        int itemCount = subBtns.size();
        int padding = ResourceUtil.getDimensionPixelSizeById(R.dimen.lv_item_padding_up_down_level_1);
        int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_morebtn_popmenu_item_w) + (padding << 1);
        int height = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_morebtn_popmenu_item_h) * itemCount + (itemCount + 1) * padding;
        moreBtnPopup.initPopup(width, LayoutParams.WRAP_CONTENT);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        // 在屏幕上半边
        if (moreBtnPopup.atTopHalfOfScreen(location)) {
            moreBtnPopup.setAnimation(R.style.workorder_more_btn_popup_tophalf_anim);
            moreBtnPopup.showAtLocation(v, (location[0] + v.getWidth() / 2) - width / 2, location[1] + v.getHeight() + 5, Gravity.NO_GRAVITY);
        } else { // 在屏幕下半边
            moreBtnPopup.setAnimation(R.style.workorder_more_btn_popup_bottomhalf_anim);
            moreBtnPopup.showAtLocation(v, (location[0] + v.getWidth() / 2) - width / 2, location[1] - height - 5, Gravity.NO_GRAVITY);
        }
        moreBtnPopup.setOnActionItemClickListener(new CustomPopupBtn.OnButtonClickListener() {
            @Override
            public void onButtonItemClick(WorkOrderBtnModel button, int pos) {
                WorkOrderDataSourceProvider provider = new WorkOrderDataSourceProvider();
                provider.setTaskBtn(button);
                FormActivity.sourceProvider = provider;
                WorkOrderFormUploader uploader = new WorkOrderFormUploader();
                uploader.setTaskBtn(button);
                FormActivity.uploader = uploader;
                Bundle bundle = new Bundle();
                bundle.putString(CustomViewInflater.REPORT_TITLE, button.getTaskName());
                bundle.putString(FormActivity.REPORT_TITLE_RIGHT_TXT, ResourceUtil.getStringById(R.string.form_data_reporta));
                UIHelper.startActivityWithExtra(FormActivity.class, bundle);
            }
        });
    }

    private static List<AppMenu> getMenu(WorkOrder workOrder) {
        Map<String, AppMenu> allBtns = MenuFactory.getWorkOrderBtns();
        List<AppMenu> moreBtnList = new ArrayList<AppMenu>(4);
        String btnStr = workOrder.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS);
        String[] btnStrArr = btnStr.split(",");
        for (String btnName : btnStrArr) {
            if (btnName.contains("more") && allBtns.get(btnName) != null) {
                moreBtnList.add(allBtns.get(btnName));
            }
        }
        return moreBtnList;
    }
    
    /**
     * 将所有的key（包含"wo_"）转换为"workOrder"关键字
     * 
     */
    private static String getNotificationVadgeViewKey(String key) {
        String finalNotificationKey = null;
        if (key.startsWith("wo_")) {
            finalNotificationKey = NotificationType.workOrder.toString();
        } else if (key.startsWith("yh_")) {
            finalNotificationKey = NotificationType.yh_badge_view_key.toString();
        } else if (key.startsWith("xj_")) {
            finalNotificationKey = NotificationType.xj_badge_view_key.toString();
        }

        return finalNotificationKey;
    }
}
