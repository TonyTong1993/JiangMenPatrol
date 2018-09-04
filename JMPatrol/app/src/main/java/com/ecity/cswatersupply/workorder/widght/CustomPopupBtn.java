package com.ecity.cswatersupply.workorder.widght;

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
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderPopupButtonAdapter;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderPopupMenuAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;

import java.util.ArrayList;
import java.util.List;

/***
 *  点击组合按钮的PopWindow
 */
public class CustomPopupBtn {
    private Context context;
    private ListView btnListView;
    private List<WorkOrderBtnModel> buttons;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow = null;
    private WorkOrderPopupButtonAdapter adapter;
    private OnButtonClickListener popupButtonClickListener;
    private OnPopupMenuDismissListener mOnPopupButtonDismissListener;

    public CustomPopupBtn(Context context) {
        this(context, new ArrayList<WorkOrderBtnModel>());
    }

    public CustomPopupBtn(Context context, List<WorkOrderBtnModel> buttons) {
        this.context = context;
        this.buttons = buttons;
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
            if (null != popupWindow) {
                popupWindow.dismiss();
            } else {
                return initPopupWindow(width, height);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void setBackGround(int resId) {
        btnListView.setBackgroundResource(resId);
    }

    private View initPopupWindow(int width, int height) {

        View rootView = mInflater.inflate(R.layout.panel_workorder_popupmenu, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(rootView, width, height);
        }

        btnListView = (ListView) rootView.findViewById(R.id.ltv_menu);
        this.adapter = new WorkOrderPopupButtonAdapter(context);
        this.adapter.setList(buttons);
        btnListView.setAdapter(adapter);
        btnListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (null != buttons && arg2 < buttons.size()) {
                    if (popupButtonClickListener != null) {
                        popupButtonClickListener.onButtonItemClick(getBtnItem(arg2), arg2);
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
        return rootView;
    }

    public void showAtLocation(View anchor, int xoff, int yoff, int gravity) {
        popupWindow.update();
        popupWindow.showAtLocation(anchor, gravity, xoff, yoff);
    }

    public void showAsDropDown(View anchor, int x, int y, int gravity) {
        popupWindow.showAsDropDown(anchor, x, y, gravity);
        popupWindow.update();
    }

    OnDismissListener popupOnDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            if (mOnPopupButtonDismissListener != null) {
                mOnPopupButtonDismissListener.onDismiss();
            }
        }
    };

    public void setList(List<WorkOrderBtnModel> datas) {
        adapter.setList(datas);
    }


    public WorkOrderBtnModel getBtnItem(int index) {
        if (null == buttons)
            return null;
        if (buttons.size() < index)
            return null;
        return buttons.get(index);
    }

    public void setOnActionItemClickListener(OnButtonClickListener listener) {
        popupButtonClickListener = listener;
    }


    public void setOnPopupMenuDismissListener(OnPopupMenuDismissListener listener) {
        mOnPopupButtonDismissListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonItemClick(WorkOrderBtnModel button, int pos);
    }

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
