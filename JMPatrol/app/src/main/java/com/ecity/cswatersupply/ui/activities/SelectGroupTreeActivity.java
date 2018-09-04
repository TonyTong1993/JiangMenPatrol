package com.ecity.cswatersupply.ui.activities;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.treeview.OrganizationTreeView;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.ui.widght.treeview.util.OrganizationTreeAdapter;
import com.ecity.cswatersupply.ui.widght.treeview.util.TreeViewHelper;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.TreeViewLocalData;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class SelectGroupTreeActivity extends BaseActivity {
    public static final String TREE_DATA_SOURCE = "TREE_DATA_SOURCE";
    private static final int ADAPT_SUCCESS = 1;
    private OrganisationSelection mInspectItem;
    private CustomTitleView mCustomTitleView;
    private OrganizationTreeView<TreeViewLocalData> mTreeView;
    private MyProcessHandler mProcessHandler = new MyProcessHandler(this);
    private List<TreeViewLocalData> mHandleTypes;
    private OrganizationTreeAdapter<TreeViewLocalData> mTreeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_type_tree);
        initUI();
        initData();
    }

    public void onBackButtonClicked(View view) {
        setResult(RequestCode.SELECT_DISMISS_TYPE, getIntent());
        finish();
    }

    private void initData() {
        LoadingDialogUtil.show(this, R.string.please_wait);
        Bundle extras = getIntent().getExtras();
        mInspectItem = (OrganisationSelection) extras.getSerializable(TREE_DATA_SOURCE);
        if (null != mInspectItem) {
            mCustomTitleView.setTitleText("部门选择");
            mCustomTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
            mCustomTitleView.setRightActionBtnText(R.string.finish);
            HostApplication.getApplication().submitExecutorService(new MyAdaptDataThread());
        } else {
            ToastUtil.showShort(R.string.workorder_finish_no_data_prompt);
        }
    }

    public void onActionButtonClicked(View view) {
        if (mTreeAdapter.getSelectedNode() != null) {
            Intent intent = new Intent();
            intent.putExtra(SelectGroupTreeActivity.TREE_DATA_SOURCE, mTreeAdapter.getSelectedNode());
            setResult(SelectGroupTreeActivity.RESULT_OK, intent);
            finish();
        } else {
            ToastUtil.showShort(R.string.summary_no_department_msg);
        }
    }

    private void initTitlePopup() {
        TreeNode nodeAll = new TreeNode();
        if (null != mInspectItem) {
            nodeAll.setId("-1");
        }
        mTreeAdapter.getlMenus().add(0, nodeAll);
    }

    private void fillUIContent() {
        mTreeView.setDatas(mHandleTypes, mTreeAdapter);
    }

    private static class MyProcessHandler extends Handler {
        private WeakReference<SelectGroupTreeActivity> activityWeak;

        public MyProcessHandler(SelectGroupTreeActivity activity) {
            activityWeak = new WeakReference<SelectGroupTreeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadingDialogUtil.dismiss();
            SelectGroupTreeActivity activity = null;
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

    private void enableTitleView(boolean isEnabled) {
        mCustomTitleView.ll_title.setEnabled(isEnabled);
    }

    private class MyAdaptDataThread implements Runnable {

        @Override
        public void run() {
            mHandleTypes = TreeViewHelper.convertOrganisationToHandleType(mInspectItem);
            mTreeAdapter = new OrganizationTreeAdapter<TreeViewLocalData>(SelectGroupTreeActivity.this, mHandleTypes, 0, true);
            Message msg = Message.obtain();
            msg.what = ADAPT_SUCCESS;
            mProcessHandler.sendMessage(msg);
        }
    }

    @SuppressWarnings("unchecked")
    private void initUI() {
        mTreeView = (OrganizationTreeView<TreeViewLocalData>) findViewById(R.id.treeLvCustom);
        mCustomTitleView = (CustomTitleView) findViewById(R.id.view_title_tree);
        mCustomTitleView.ll_title.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.selector_titleview_titletxt_shape));
    }
}