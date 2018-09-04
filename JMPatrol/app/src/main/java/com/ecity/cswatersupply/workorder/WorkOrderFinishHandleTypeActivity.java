package com.ecity.cswatersupply.workorder;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.treeview.TreeView;
import com.ecity.cswatersupply.ui.widght.treeview.TreeView.OnTreeNodeClickListener;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.ui.widght.treeview.util.TreeListViewAdapter;
import com.ecity.cswatersupply.ui.widght.treeview.util.TreeListViewAdapter.ETreeChooseMode;
import com.ecity.cswatersupply.ui.widght.treeview.util.TreeViewHelper;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.TreeViewLocalData;
import com.ecity.cswatersupply.workorder.widght.Tree3TitlePopupMenu;
import com.ecity.cswatersupply.workorder.widght.Tree3TitlePopupMenu.OnMenuClickListener;
import com.ecity.cswatersupply.workorder.widght.Tree3TitlePopupMenu.OnPopupMenuDismissListener;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class WorkOrderFinishHandleTypeActivity extends BaseActivity {
    public static final String TREE_DATA_SOURCE = "TREE_DATA_SOURCE";
    private static final int ADAPT_SUCCESS = 1;
    private List<TreeViewLocalData> mHandleTypes;
    private InspectItem mInspectItem;
    private MyProcessHandler mProcessHandler = new MyProcessHandler(this);
    private TreeView<TreeViewLocalData> mTreeView;
    private TreeListViewAdapter<TreeViewLocalData> mTreeAdapter;
    private CustomTitleView mCustomTitleView;
    private Tree3TitlePopupMenu mTree3TitlePopupMenu;
    private Drawable drawableLeftUp;
    private Drawable drawableLeftDown;
    private boolean isArrowDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workder_handle_type_tree);
        initUI();
        initData();
    }

    @Override
    protected void onDestroy() {
        TreeViewHelper.setIs3TreeViewFalse();
        TreeViewHelper.setMenu(null);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onActionButtonClicked(View view) {
        setHandleTypeValue();
        Intent intent = new Intent();
        intent.putExtra(TREE_DATA_SOURCE, (Serializable) mHandleTypes);
        setResult(RESULT_OK, intent);
        finish();
    }

    @SuppressWarnings("unchecked")
    private void initUI() {
        mTreeView = (TreeView<TreeViewLocalData>) findViewById(R.id.treeLvCustom);
        mCustomTitleView = (CustomTitleView) findViewById(R.id.view_title_tree);
        mCustomTitleView.ll_title.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.selector_titleview_titletxt_shape));
        mCustomTitleView.ll_title.setOnClickListener(onTitleClick);
    }

    private void enableTitleView(boolean isEnabled) {
        mCustomTitleView.ll_title.setEnabled(isEnabled);
    }

    private void setHandleTypeValue() {
        List<TreeNode> treeNode = mTreeAdapter.getlNodes();
        if (ListUtil.isEmpty(treeNode) || ListUtil.isEmpty(mHandleTypes)) {
            return;
        }

        for (TreeViewLocalData dataTemp : mHandleTypes) {
            dataTemp.setHasSelected(false);
            dataTemp.setSelected(false);
        }

        for (TreeNode nodeTemp : treeNode) {
            if (nodeTemp.isSelected()) {
                for (TreeViewLocalData handleTypeTemp : mHandleTypes) {
                    if (null != nodeTemp.getParent() && handleTypeTemp.getId().equalsIgnoreCase(nodeTemp.getParent().getId())) {
                        handleTypeTemp.setHasSelected(true);
                    }
                    if (null != nodeTemp.getParent() && null != nodeTemp.getParent().getParent()
                            && handleTypeTemp.getId().equalsIgnoreCase(nodeTemp.getParent().getParent().getId())) {
                        handleTypeTemp.setHasSelected(true);
                    }
                    if (handleTypeTemp.getId().equalsIgnoreCase(nodeTemp.getId())) {
                        handleTypeTemp.setSelected(true);
                    }
                }
            }
        }
    }

    private void initData() {
        LoadingDialogUtil.show(this, R.string.please_wait);
        Bundle extras = getIntent().getExtras();
        mInspectItem = (InspectItem) extras.getSerializable(TREE_DATA_SOURCE);
        if (null != mInspectItem) {
            mCustomTitleView.setTitleText(mInspectItem.getAlias());
            mCustomTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
            mCustomTitleView.setRightActionBtnText(R.string.finish);
            HostApplication.getApplication().submitExecutorService(new MyAdaptDataThread());
        } else {
            ToastUtil.showShort(R.string.workorder_finish_no_data_prompt);
        }
    }

    private void initTitlePopup() {
        drawableLeftUp = getResources().getDrawable(R.drawable.icon_navbar_drop_up);
        drawableLeftUp.setBounds(0, 0, drawableLeftUp.getMinimumWidth(), drawableLeftUp.getMinimumHeight());
        drawableLeftDown = getResources().getDrawable(R.drawable.icon_navbar_drop_down);
        drawableLeftDown.setBounds(0, 0, drawableLeftDown.getMinimumWidth(), drawableLeftDown.getMinimumHeight());
        mTree3TitlePopupMenu = new Tree3TitlePopupMenu(this);
        int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_popmenu_w);
        mTree3TitlePopupMenu.initPopup(mCustomTitleView, width, LayoutParams.WRAP_CONTENT);
        mCustomTitleView.tv_title.setCompoundDrawables(null, null, drawableLeftUp, null);
        isArrowDown = false;
        TreeNode nodeAll = new TreeNode();
        if (null != mInspectItem) {
            nodeAll.setName(mInspectItem.getAlias());
            nodeAll.setId("-1");
        }
        mTreeAdapter.getlMenus().add(0, nodeAll);
        mTree3TitlePopupMenu.setMenus(mTreeAdapter.getlMenus());
        mTree3TitlePopupMenu.setOnActionItemClickListener(new OnMenuClickListener() {

            @Override
            public void onMenuItemClick(TreeNode menu, int pos) {
                mCustomTitleView.setTitleText(menu.getName());
                mTreeAdapter.changeDataSourcesByTreeNode(menu);
                mTreeAdapter.notifyDataSetChangedOnly();
                TreeViewHelper.setMenu(menu);
            }
        });
        mTree3TitlePopupMenu.setOnPopupMenuDismissListener(new OnPopupMenuDismissListener() {

            @Override
            public void onDismiss() {
                if (null != drawableLeftUp) {
                    mCustomTitleView.tv_title.setCompoundDrawables(null, null, drawableLeftUp, null);
                    isArrowDown = false;
                }
            }
        });
    }

    private void fillUIContent() {
        mTreeView.setDatas(mHandleTypes, mTreeAdapter);
        mTreeView.setOnTreeNodeClickListener(new MyTreeViewOnItemClickListener());
    }

    private class MyAdaptDataThread implements Runnable {

        @Override
        public void run() {
            mHandleTypes = TreeViewHelper.convertInspectItemToHandleType(mInspectItem);
            //由于生成Adapter过程中会解析数据,故放在一个线程中
            mTreeAdapter = new TreeListViewAdapter<TreeViewLocalData>(WorkOrderFinishHandleTypeActivity.this, mHandleTypes, 0);
            mTreeAdapter.setTreeChooseMode(ETreeChooseMode.ONLY_ONE_IN_ONE_NODE);
            Message msg = Message.obtain();
            msg.what = ADAPT_SUCCESS;
            mProcessHandler.sendMessage(msg);
        }
    }

    private static class MyProcessHandler extends Handler {
        private WeakReference<WorkOrderFinishHandleTypeActivity> activityWeak;

        public MyProcessHandler(WorkOrderFinishHandleTypeActivity activity) {
            activityWeak = new WeakReference<WorkOrderFinishHandleTypeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadingDialogUtil.dismiss();
            WorkOrderFinishHandleTypeActivity activity = null;
            if (null != activityWeak) {
                activity = activityWeak.get();
            }
            if (null == activity) {
                return;
            }
            switch (msg.what) {
                case ADAPT_SUCCESS:
                    if (TreeViewHelper.isIs3TreeView()) {
                        activity.initTitlePopup();
                    }
                    activity.enableTitleView(TreeViewHelper.isIs3TreeView());
                    activity.fillUIContent();
                    break;
                default:
                    break;
            }

        }

    }

    private class MyTreeViewOnItemClickListener implements OnTreeNodeClickListener {

        @Override
        public void onTreeNodeClick(TreeNode node, int position) {
            if (node.isLeaf()) {
                mTreeAdapter.changeAssociatedNodeValue(node, !node.isSelected());
            }
            if (node.isSelected()) {
                mTreeAdapter.setOtherParallelNodeFalse(node);
            }
        }
    }

    OnClickListener onTitleClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mTree3TitlePopupMenu == null) {
                return;
            }

            if (!isArrowDown && (null != drawableLeftDown)) {
                mCustomTitleView.tv_title.setCompoundDrawables(null, null, drawableLeftDown, null);
                isArrowDown = true;
            }
            int xPos = mCustomTitleView.getWidth() / 2 - mTree3TitlePopupMenu.getPopupWindow().getWidth() / 2;
            mTree3TitlePopupMenu.showAdDropDown(mCustomTitleView, xPos, 0);
        }
    };
}
