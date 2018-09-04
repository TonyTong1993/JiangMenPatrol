package com.ecity.cswatersupply.workorder.view;

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
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.adpter.PopupSummaryAdapter;
import com.ecity.cswatersupply.workorder.model.PopupSummaryModel;

/**
 * 工单汇总弹出分类：汇总统计／明细统计
 * 
 * @author qiwei
 *
 */
public class PopupSummaryView {
    private Context context;
    private ListView ltv_menu;
    public boolean isshowing = false;
    private ArrayList<PopupSummaryModel> popupMenus;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow = null;
    private PopupSummaryAdapter adapter;
    private OnMenuClickListener popupMenuClickListener;
    private OnPopupMenuDismissListener mOnPopupMenuDismissListener;

    public PopupSummaryView(Context context) {
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
        adapter = new PopupSummaryAdapter(context);
        ltv_menu.setAdapter(adapter);
        ltv_menu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (null != popupMenus && arg2 < popupMenus.size()) {

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
        popupWindow.setFocusable(false);
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

    public void setMenu(int resId) {
        String[] menus = ResourceUtil.getArrayById(resId);
        popupMenus = getMenusByNames(menus);
        adapter.setList(popupMenus);
    }

    private ArrayList<PopupSummaryModel> getMenusByNames(String[] menus) {
        ArrayList<PopupSummaryModel> temp = new ArrayList<PopupSummaryModel>();
        for (String name : menus) {
            PopupSummaryModel menu = PopupSummaryModel.getPageByName(name);
            if (menu != null) {
                temp.add(menu);
            }
        }
        return temp;
    }

    public PopupSummaryModel getMenuItem(int index) {
        if (null == popupMenus)
            return null;
        if (popupMenus.size() < index)
            return null;
        return popupMenus.get(index);
    }

    public void setOnActionItemClickListener(OnMenuClickListener listener) {
        popupMenuClickListener = listener;
    }

    public void setOnPopupMenuDismissListener(OnPopupMenuDismissListener listener) {
        mOnPopupMenuDismissListener = listener;
    }

    public interface OnMenuClickListener {
        void onMenuItemClick(PopupSummaryModel menu, int pos);
    }

    public interface OnPopupMenuDismissListener {
        void onDismiss();
    }
}
