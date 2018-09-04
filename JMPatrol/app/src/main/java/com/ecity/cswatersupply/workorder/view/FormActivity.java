package com.ecity.cswatersupply.workorder.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.adpter.FormFragmentTabAdapter;
import com.ecity.cswatersupply.workorder.presenter.AFromDataSourceProvider;
import com.ecity.cswatersupply.workorder.presenter.AFromUploader;
import com.ecity.medialibrary.utils.MediaCacheManager;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GXX on 2017/4/12.
 */

public class FormActivity extends FragmentActivity {
    public static final String REPORT_TITLE_RIGHT_TXT = "REPORT_TITLE_RIGHT_TXT";
    public static AFromDataSourceProvider sourceProvider;
    public static AFromUploader uploader;
    private String mTitle;
    private LinearLayout layoutContainerView;
    private CustomTitleView mViewTitle;
    private String rightActionBtnTxtResId = ResourceUtil.getStringById(R.string.event_report_submit); // 默认是上报
    private List<InspectItem> mInspectItems;
    private CustomViewInflater mCustomViewInflater;
    private IndicatorViewPager mIndicatorViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_common);
        this.setTheme(android.R.style.Theme_Holo_Light);
        EventBusUtil.register(this);
        initData();
        initUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initData();
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CustomViewInflater.pendingViewInflater != null) {
            CustomViewInflater.pendingViewInflater.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initData(){
        if (null == getIntent()) {
            return;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        if (bundle.containsKey(CustomViewInflater.REPORT_TITLE)) {
            int titleResId = bundle.getInt(CustomViewInflater.REPORT_TITLE, -1);
            if (-1 == titleResId) {
                mTitle = bundle.getString(CustomViewInflater.REPORT_TITLE);
            } else {
                mTitle = getString(titleResId);
            }
        }

        rightActionBtnTxtResId = bundle.getString(REPORT_TITLE_RIGHT_TXT,"");

        if (null != sourceProvider) {
            sourceProvider.setActivity(this);
            sourceProvider.requestDataSource();
        }
        if(null != uploader) {
            uploader.setActivity(this);
        }
    }

    private void initUI() {
        initTitle();
        initInspectItems();
    }

    protected void initTitle() {
        mViewTitle = (CustomTitleView) findViewById(R.id.customTitleView);
        mViewTitle.setTitleText(mTitle);
        mViewTitle.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        mViewTitle.setRightActionBtnText(rightActionBtnTxtResId);
    }

    public void showItems(List<InspectItem> inspectItems) {
        if(ListUtil.isEmpty(inspectItems)) {
            submitInfo(inspectItems);
        }
        initInspectItems();
    }

    protected void initInspectItems() {
        if (ListUtil.isEmpty(mInspectItems)) {
            return;
        }

        internalFillDatas(mInspectItems);
    }

    protected void internalFillDatas(List<InspectItem> inspectItems) {
        if (null != layoutContainerView) {
            layoutContainerView.removeAllViews();
        }
        if (null == inspectItems) {
            return;
        }

        mCustomViewInflater = new CustomViewInflater(this);
        layoutContainerView = (LinearLayout) findViewById(R.id.ll_container);
        for (InspectItem item : inspectItems) {
            if (!item.isVisible() && (!item.getType().equals(EInspectItemType.GROUP))) {
                continue;
            }
            layoutContainerView.addView(mCustomViewInflater.inflate(item));
        }
    }

    public void onActionButtonClicked(View view) {
        submitInfo(view);
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    public void submitInfo(View view) {
        submitInfo(mInspectItems);
    }

    protected void submitInfo(List<InspectItem> items) {
        List<InspectItem> inspectItems = new ArrayList<InspectItem>();
        if(hasTabInspectItem(items)) {
            inspectItems = mergeInspectItems(items);
        } else {
            inspectItems = items;
        }

        if (!ListUtil.isEmpty(inspectItems) && !uploader.isInspectItemContentValid(inspectItems)) {
            Toast.makeText(this, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        uploader.submit(inspectItems);
    }

    /***
     * 合并TAB类型检查项的子项
     * @param inspectItem
     * @return
     */
    private List<InspectItem> mergeInspectItems(List<InspectItem> inspectItem) {
        List<InspectItem> allInspectItems = new ArrayList<InspectItem>();
        mergeInspectItems(inspectItem, allInspectItems);

        return allInspectItems;
    }

    private void mergeInspectItems(List<InspectItem> items, List<InspectItem> allInspectItems) {
        for (InspectItem item : items) {
            if (item.getType() == EInspectItemType.TAB) {
                mergeInspectItems(item.getChilds(), allInspectItems);
            } else {
                allInspectItems.add(item);
            }
        }
    }

    @Override
    protected void onDestroy() {
        releaseResources();
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void releaseResources() {
        if (mCustomViewInflater != null) {
            mCustomViewInflater.stopPlay();
        }
        MediaCacheManager.clearImg();
        MediaCacheManager.clearVdo();

        CustomViewInflater.releaseResources();
    }

    public void onEventMainThread(UIEvent event) {

        switch (event.getId()) {
            case UIEventStatus.GET_FORM_INSPECTITEM:
                this.mInspectItems = event.getData();
                replaceContentView(hasTabInspectItem(mInspectItems));
                break;
            default:
                break;
        }
    }

    /****
     * 根据返回的检查项中是否包含人员和材料检查项确定是否采用Fragment布局；
     * 包含两种检查项中的任何一个检查项则返回true
     * @param mInspectItems
     * @return
     */
    private boolean hasTabInspectItem(List<InspectItem> mInspectItems) {
        for(InspectItem item : mInspectItems) {
            if(item.getType() == EInspectItemType.TAB) {
                return true;
            }
        }
        return false;
    }

    private void replaceContentView(boolean isMultiPages) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        LinearLayout pageLinLayout = (LinearLayout) findViewById(R.id.view_detail_page);
        if(isMultiPages) {
            scrollView.setVisibility(View.GONE);
            pageLinLayout.setVisibility(View.VISIBLE);
            setTabPageIndicator();
        } else {
            scrollView.setVisibility(View.VISIBLE);
            pageLinLayout.setVisibility(View.GONE);
            showItems(mInspectItems);
        }
    }

    /***
     * 更新Tab分页
     */
    private void setTabPageIndicator() {
        SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        viewPager.setCanScroll(true);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new FormFragmentTabAdapter(this, mInspectItems, getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(mInspectItems.size());
    }
}
