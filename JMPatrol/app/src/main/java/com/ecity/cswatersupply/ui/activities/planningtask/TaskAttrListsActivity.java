package com.ecity.cswatersupply.ui.activities.planningtask;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.planningtask.AttrListAdapter;
import com.ecity.cswatersupply.menu.ACommonAttrListViewOperator;
import com.ecity.cswatersupply.menu.map.DefaultAttrListViewOperator;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.ScrollListView;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class TaskAttrListsActivity extends BaseActivity {
    public static final String ATTR_TITLE = "ATTR_TITLE";
    public static final String ATTRS_LIST = "ATTRS_LIST";
    public static final String ATTR_LISTVIEW_COMEFROM = "ATTR_LISTVIEW_COMEFROM";
    private ScrollListView mListView;
    private CustomTitleView title;
    private ACommonAttrListViewOperator mCommonAttrListViewOperator;
    private List<Object> attrinfos;
    private String mTitle;
    private AttrListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointpart_attr);
        initDataSource(getIntent());
        initView();
    }

    private void initView() {
        title = (CustomTitleView) findViewById(R.id.customTitleView);
        title.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        title.setTitleText(mTitle);

        mListView = (ScrollListView) findViewById(R.id.lv_attr);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initDataSource(intent);
    }

    public void onBackButtonClicked(View v) {
        notifyBackEvent();
    }

    private void notifyBackEvent() {
        if (null != mCommonAttrListViewOperator) {
            mCommonAttrListViewOperator.notifyBackEvent(this);
        } else {
            finish();
        }
    }

    @SuppressWarnings("unchecked")
    private void initDataSource(Intent intent) {
        if (intent == null) {
            return;
        }
        String AttrListViewClassName = null;
        int titleResId = R.string.planningtask_pointpart_attr;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(ATTR_TITLE)) {
            titleResId = bundle.getInt(ATTR_TITLE);
            mTitle = getString(titleResId);
        }
        if (bundle.containsKey(ATTR_LISTVIEW_COMEFROM)) {
            AttrListViewClassName = bundle.getString(ATTR_LISTVIEW_COMEFROM);
        }

        if (StringUtil.isBlank(AttrListViewClassName)) {
            mCommonAttrListViewOperator = new DefaultAttrListViewOperator();
        } else {
            try {
                mCommonAttrListViewOperator = (ACommonAttrListViewOperator) Class.forName(AttrListViewClassName).newInstance();
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
        if (null != mCommonAttrListViewOperator) {
            mCommonAttrListViewOperator.setCustomActivity(this);
        }
        attrinfos = (List<Object>) mCommonAttrListViewOperator.getDataSource();
        mAdapter = new AttrListAdapter(this.getApplicationContext());
        mAdapter.setList(attrinfos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 测试有无内存泄漏
        RefWatcher refWatcher = HostApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
