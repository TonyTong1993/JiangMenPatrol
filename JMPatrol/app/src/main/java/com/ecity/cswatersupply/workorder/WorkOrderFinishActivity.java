package com.ecity.cswatersupply.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderFinishAdapter;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderFinishFragmentMaterielInfo;
import com.ecity.cswatersupply.workorder.menu.AWorkOrderFinishCommonOperator;
import com.ecity.cswatersupply.workorder.menu.EmptyWorkOrderFinishOperator;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderContactManChooser;
import com.viewpagerindicator.TabPageIndicator;
import com.z3app.android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class WorkOrderFinishActivity extends FragmentActivity {
    private WorkOrderFinishAdapter mWorkOrderFinishAdapter;
    private CustomTitleView mCustomTitleView;
    private TabPageIndicator mTabPageIndicator;
    private FrameLayout mFrameIndicatorContainer;
    private ViewPager mViewPager;

    private List<InspectItem> mInspectItems;
    private List<String> mTitles;
    private AWorkOrderFinishCommonOperator mCommonReportOperator;
    private int titleResId;
    private CustomViewInflater customViewInflater;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setTheme(R.style.Theme_TabPageIndicatorDefaults);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_workorder_finish);
        customViewInflater = new CustomViewInflater(this);
        initData(getIntent());
        initUI();
        WorkOrderContactManChooser.setLoadContactsFromServer(false);
    }

    @Override
    protected void onDestroy() {
        WorkOrderContactManChooser.setLoadContactsFromServer(true);
        CustomViewInflater.releaseResources();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        customViewInflater.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }
        notifyBackEvent();
        return super.onKeyDown(keyCode, event);
    }

    public void onBackButtonClicked(View view) {
        notifyBackEvent();
    }

    /**
     * 提交方法
     * @param view
     */
    public void onActionButtonClicked(View view) {
        WorkOrderFinishFragmentMaterielInfo.getInstance().addMaterialJsonToInspectItems();
        mCommonReportOperator.submit2Server(mInspectItems);
    }

    public List<InspectItem> getAllInspectItems() {
        return mInspectItems;
    }

    @SuppressWarnings("unchecked")
    private void initData(Intent intent) {

        if (intent == null) {
            return;
        }
        String classReportName = null;
        titleResId = R.string.workorder_finish_title;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        if (bundle.containsKey(CustomViewInflater.REPORT_TITLE)) {
            titleResId = bundle.getInt(CustomViewInflater.REPORT_TITLE);
        }

        if (bundle.containsKey(CustomViewInflater.REPORT_COMFROM)) {
            classReportName = bundle.getString(CustomViewInflater.REPORT_COMFROM);
        }

        if (StringUtil.isBlank(classReportName)) {
            mCommonReportOperator = new EmptyWorkOrderFinishOperator();
        } else {
            try {
                mCommonReportOperator = (AWorkOrderFinishCommonOperator) Class.forName(classReportName).newInstance();
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }

        setCustomReportActivity(this);

        if (bundle.containsKey(CustomViewInflater.REPORT_CHILD_ITEMS)) {
            mInspectItems = (List<InspectItem>) bundle.getSerializable(CustomViewInflater.REPORT_CHILD_ITEMS);
        } else {
            mInspectItems = mCommonReportOperator.getDataSource();
        }
    }

    private void setCustomReportActivity(WorkOrderFinishActivity activity) {
        if (null != mCommonReportOperator) {
            mCommonReportOperator.setCustomActivity(activity);
        }
    }

    private void initUI() {
        mCustomTitleView = (CustomTitleView) findViewById(R.id.workorder_finish_customTitleView);
        mCustomTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        mCustomTitleView.setTitleText(titleResId);
        mCustomTitleView.setRightActionBtnText(R.string.workorder_finish_submit);
        if (!ListUtil.isEmpty(mInspectItems)) {
            fillDatas(mInspectItems);
        }
    }

    public void fillDatas(List<InspectItem> inspectItems) {
        this.mInspectItems = inspectItems;
        adaptTitles();
        mWorkOrderFinishAdapter = new WorkOrderFinishAdapter(getSupportFragmentManager(), mTitles);
        mFrameIndicatorContainer = (FrameLayout) findViewById(R.id.wordorder_finish_indicator_container);
        mTabPageIndicator = new TabPageIndicator(this);
        mFrameIndicatorContainer.addView(mTabPageIndicator);
        mViewPager = (ViewPager) findViewById(R.id.wordorder_finish_pager);
        mViewPager.setAdapter(mWorkOrderFinishAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabPageIndicator.setViewPager(mViewPager);
    }

    private void notifyBackEvent() {
        if (null != mCommonReportOperator) {
            mCommonReportOperator.notifyBackEvent(this);
        } else {
            finish();
        }
    }

    private void adaptTitles() {
        mTitles = new ArrayList<String>();
        if (ListUtil.isEmpty(mInspectItems)) {
            return;
        }
        for (InspectItem temp : mInspectItems) {
            mTitles.add(temp.getAlias());
        }
    }

    public CustomViewInflater getCustomViewInflater() {
        return customViewInflater;
    }
}
