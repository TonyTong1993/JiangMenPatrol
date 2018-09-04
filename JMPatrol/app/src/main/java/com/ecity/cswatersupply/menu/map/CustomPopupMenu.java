package com.ecity.cswatersupply.menu.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AMenu;

public class CustomPopupMenu {
    private Context context;
    private ListView ltv_menu;
    public boolean isshowing = false;
    private List<AMapMenu> menus;
    private List<AEventMenu> eventMenus;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow = null;
    private MapPopupMenuAdapter mapMenuAdapter;
    private EventPopupMenuAdapter eventMenuAdapter;
    private OnMapMenuClickListener popupMenuClickListener;
    private OnEventMenuClickListener eventMenuClickListener;
    private OnPopupMenuDismissListener mOnPopupMenuDismissListener;

    public <T extends AMenu> CustomPopupMenu(Context context, List<T> menus, Class<T> menuClass) {
        this.context = context;
        if (AMapMenu.class.isAssignableFrom(menuClass)) {
            this.menus = (ArrayList<AMapMenu>) menus;
        } else {
            this.eventMenus = (ArrayList<AEventMenu>) menus;
        }
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void dismiss() {
        if (null == popupWindow) {
            return;
        }
        context = null;
        popupWindow.dismiss();
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public View initPopup(int width, int height) {
        try {
            if (isshowing) {
                popupWindow.dismiss();
            } else {
                return initPopupWindow(width, height);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void setBackGround(int resId) {
        ltv_menu.setBackgroundResource(resId);
    }

    private View initPopupWindow(int width, int height) {

        View rootView = mInflater.inflate(R.layout.panel_list_popupmenu, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(rootView, width, height);
        }
        ltv_menu = (ListView) rootView.findViewById(R.id.ltv_menu);

        if(null != menus) {
            this.mapMenuAdapter = new MapPopupMenuAdapter(context);
            this.mapMenuAdapter.setList(menus);
            ltv_menu.setAdapter(mapMenuAdapter);
            ltv_menu.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (null != menus && arg2 < menus.size()) {
                        if (popupMenuClickListener != null) {
                            popupMenuClickListener.onMenuItemClick(getMenuItem(arg2), arg2);
                        }
                    }
                    dismiss();
                }
            });
        } else if (null != eventMenus) {
            this.eventMenuAdapter = new EventPopupMenuAdapter(context);
            this.eventMenuAdapter.setList(eventMenus);
            ltv_menu.setAdapter(eventMenuAdapter);
            ltv_menu.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (null != eventMenus && arg2 < eventMenus.size()) {
                        if (eventMenuClickListener != null) {
                            eventMenuClickListener.onMenuItemClick(getEventMenuItem(arg2), arg2);
                        }
                    }
                    dismiss();
                }
            });
        }
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(popupOnDismissListener);
        return rootView;
    }

    public void showAtLocation(View anchor, int xoff, int yoff, int gravity) {
        popupWindow.update();
        isshowing = true;
        popupWindow.showAtLocation(anchor, gravity, xoff, yoff);
    }

    public void showAsDropDown(View anchor, int x, int y, int gravity) {
        popupWindow.showAsDropDown(anchor, x, y, gravity);
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

    public void setList(List<AMapMenu> datas) {
        mapMenuAdapter.setList(datas);
    }

    public AMapMenu getMenuItem(int index) {
        if (null == menus)
            return null;
        if (menus.size() < index)
            return null;
        return menus.get(index);
    }

    public AEventMenu getEventMenuItem(int index) {
        if (null == eventMenus)
            return null;
        if (eventMenus.size() < index)
            return null;
        return eventMenus.get(index);
    }

    public void setOnActionItemClickListener(OnMapMenuClickListener listener) {
        popupMenuClickListener = listener;
    }

    public void setOnEventMenuClickListener(OnEventMenuClickListener listener) {
        eventMenuClickListener = listener;
    }

    public void setOnPopupMenuDismissListener(OnPopupMenuDismissListener listener) {
        mOnPopupMenuDismissListener = listener;
    }

    /**
     * Listener for item click
     */
    public interface OnMapMenuClickListener {
        void onMenuItemClick(AMapMenu menu, int pos);
    }

    public interface OnEventMenuClickListener {
        void onMenuItemClick(AEventMenu menu, int pos);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnPopupMenuDismissListener {
        void onDismiss();
    }

    public void setBackgroundAlpha(final float bgAlpha) {
        new Handler(context.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = bgAlpha; // 0.0-1.0
                ((Activity) context).getWindow().setAttributes(lp);
            }
        }, 200);

    }

    public void setAnimation(int animationResId) {
        if (popupWindow != null) {
            popupWindow.setAnimationStyle(animationResId);
        }
    }

    /**
     * 是否在屏幕上半边
     */
    public boolean atTopHalfOfScreen(int[] location) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return location[1] <= (screenHeight >> 1);
    }
}
