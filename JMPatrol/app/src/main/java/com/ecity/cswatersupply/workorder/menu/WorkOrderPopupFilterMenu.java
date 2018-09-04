package com.ecity.cswatersupply.workorder.menu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderPopupFilterAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderPopupMenuModel;

/**
 * 工单快捷菜单
 * 
 * @author qiwei
 *
 */
public class WorkOrderPopupFilterMenu {
    private Context context;
    private ListView ltv_menu;
    public boolean isshowing = false;
    private ArrayList<WorkOrderPopupMenuModel> workOrderMenus;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow = null;
    private WorkOrderPopupFilterAdapter adapter;
    private OnMenuClickListener popupMenuClickListener;
    private OnPopupMenuDismissListener mOnPopupMenuDismissListener;

    /***
     * constructor allowing orientation override
     * 
     * @param context
     *            Context
     * @param actionItems
     *            List AppMenu
     */
    public WorkOrderPopupFilterMenu(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void dismiss() {
        if (null == popupWindow) {
            return;
        }
        popupWindow.dismiss();
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void initPopup(View anchor, int width, int height) {
        try {
            if (isshowing) {
                popupWindow.dismiss();
            } else {
                initPopupWindow(anchor, width, height);
            }
        } catch (Exception e) {
        }
    }

    private void initPopupWindow(View anchor, int width, int height) {
        ViewGroup rootView = (ViewGroup) mInflater.inflate(R.layout.panel_workorder_list_popupmenu, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(rootView, width, height);
        }
        ltv_menu = (ListView) rootView.findViewById(R.id.ltv_menu);
        adapter = new WorkOrderPopupFilterAdapter(context);
        ltv_menu.setAdapter(adapter);
        ltv_menu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (null != workOrderMenus && arg2 < workOrderMenus.size()) {

                    if (popupMenuClickListener != null) {
                        popupMenuClickListener.onMenuItemClick(getMenuItem(arg2), arg2);
                    }
                }
                dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(popupOnDismissListener);
    }

    public void showAdDropDown(View anchor, int x, int y) {
        popupWindow.showAsDropDown(anchor, x, y);
        popupWindow.update();
        isshowing = true;
    }

    OnDismissListener popupOnDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            isshowing = false;
            if (mOnPopupMenuDismissListener != null) {
                mOnPopupMenuDismissListener.onDismiss();
            }
        }
    };

    public void setMenu(int resId,int position) {
        String[] menus = ResourceUtil.getArrayById(resId);
        workOrderMenus = getMenusByNames(menus,position);
        adapter.setList(workOrderMenus);
    }

    private ArrayList<WorkOrderPopupMenuModel> getMenusByNames(String[] menus, int position) {
        ArrayList<WorkOrderPopupMenuModel> temp = new ArrayList<WorkOrderPopupMenuModel>();
        for (String name : menus) {
            WorkOrderPopupMenuModel menu = WorkOrderPopupMenuModel.getPageByName(name,position);
            if (menu != null) {
                temp.add(menu);
            }
        }
        return temp;
    }

    /**
     * Get action item at an index
     *
     * @param index
     *            Index of item (position from callback)
     * @return Action Item at the position
     */
    public WorkOrderPopupMenuModel getMenuItem(int index) {
        if (null == workOrderMenus)
            return null;
        if (workOrderMenus.size() < index)
            return null;
        return workOrderMenus.get(index);
    }

    /**
     * Set listener for action item clicked.
     *
     * @param listener
     *            Listener
     */
    public void setOnActionItemClickListener(OnMenuClickListener listener) {
        popupMenuClickListener = listener;
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if
     * the quicakction dialog is dismissed by clicking outside the dialog or
     * clicking on sticky item.
     */
    public void setOnPopupMenuDismissListener(OnPopupMenuDismissListener listener) {
        mOnPopupMenuDismissListener = listener;
    }

    /**
     * Listener for item click
     */
    public interface OnMenuClickListener {
        void onMenuItemClick(WorkOrderPopupMenuModel menu, int pos);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnPopupMenuDismissListener {
        void onDismiss();
    }
}
