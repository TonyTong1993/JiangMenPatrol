package com.ecity.cswatersupply.workorder.widght;

import java.util.List;

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
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.ui.widght.treeview.util.Tree3TitleAdapter;

/**
 * 当树为三级时,第一级显示为弹出式的PopupWindow
 *
 */
public class Tree3TitlePopupMenu {
    private Context mContext;
    private ListView mLvmenu;
    public boolean isshowing = false;
    private List<TreeNode> mTreeNode3Indexs;
    private LayoutInflater mInflater;
    private PopupWindow mPopupWindow = null;
    private Tree3TitleAdapter mAdapter;
    private OnMenuClickListener mPopupMenuClickListener;
    private OnPopupMenuDismissListener mOnPopupMenuDismissListener;

    public Tree3TitlePopupMenu(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void dismiss() {
        if (null == mPopupWindow) {
            return;
        }
        mPopupWindow.dismiss();
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public void initPopup(View anchor, int width, int height) {
        try {
            if (isshowing) {
                mPopupWindow.dismiss();
            } else {
                initPopupWindow(anchor, width, height);
            }
        } catch (Exception e) {
        }
    }

    private void initPopupWindow(View anchor, int width, int height) {

        ViewGroup rootView = (ViewGroup) mInflater.inflate(R.layout.panel_workorder_popupmenu, null);
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(rootView, width, height);
        }

        mLvmenu = (ListView) rootView.findViewById(R.id.ltv_menu);
        mAdapter = new Tree3TitleAdapter(mContext);

        mLvmenu.setAdapter(mAdapter);
        mLvmenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (null != mTreeNode3Indexs && arg2 < mTreeNode3Indexs.size()) {

                    if (mPopupMenuClickListener != null) {
                        mPopupMenuClickListener.onMenuItemClick(getMenuItem(arg2), arg2);
                    }
                }
                dismiss();
            }
        });

        mPopupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOnDismissListener(popupOnDismissListener);
    }

    public void showAdDropDown(View anchor, int x, int y) {
        mPopupWindow.showAsDropDown(anchor, x, y);
        mPopupWindow.update();
        isshowing = true;
    }

    public void setMenus(List<TreeNode> nodes) {
        mTreeNode3Indexs = nodes;
        if (null != mAdapter) {
            mAdapter.setDatas(nodes);
            mAdapter.notifyDataSetChanged();
        }
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

    /**
     * Get action item at an index
     *
     * @param index Index of item (position from callback)
     * @return Action Item at the position
     */
    public TreeNode getMenuItem(int index) {
        if (null == mTreeNode3Indexs)
            return null;
        if (mTreeNode3Indexs.size() < index)
            return null;
        return mTreeNode3Indexs.get(index);
    }

    /**
     * Set listener for action item clicked.
     *
     * @param listener  Listener
     */
    public void setOnActionItemClickListener(OnMenuClickListener listener) {
        mPopupMenuClickListener = listener;
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
        void onMenuItemClick(TreeNode menu, int pos);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnPopupMenuDismissListener {
        void onDismiss();
    }
}
