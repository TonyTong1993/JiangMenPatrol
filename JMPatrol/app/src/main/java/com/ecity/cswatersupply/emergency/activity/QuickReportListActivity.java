package com.ecity.cswatersupply.emergency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.adapter.EarthQuakeQuickReportAdapter;
import com.ecity.cswatersupply.emergency.fragment.EarthquakeFragment;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoSearchOperater;
import com.ecity.cswatersupply.emergency.menu.operator.EarthQuakeZQSBReportOperator;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.model.SearchType;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeQuickReportInfosParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 灾情速报列表Activity
 *
 * @author Administrator
 */
public class QuickReportListActivity extends BaseActivity {
    public final static String QUICK_REPORT_ITEM_CLICK_RESULT = "QUICK_REPORT_ITEM_CLICK_RESULT";
    public final static String START_ACTION_NAME = "START_ACTION_NAME";
    public final static String START_ACTION_REPORT_NEW = "START_ACTION_REPORT_NEW";

    private MapActivityTitleView title_view;
    protected ListView mListView;
    protected LinearLayout ll_earthquake_searchview;
    protected PullToRefreshListView lv_earthquake;
    private List<EarthQuakeQuickReportModel> reportLists;
    private IRequestParameter searchParameter;
    private EarthQuakeQuickReportAdapter adapter;
    private EarthQuakeInfoModel quakeInfoModel;
    private boolean hasBeenResumed = false;
    private EarthQuakeInfoModel clickModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_listview);
        EventBusUtil.register(this);
        initData();
        initTitleView();
        initView();
        initLisenter();
        hasBeenResumed = false;
        requestListSourceDatas();
    }

    @Override
    protected void onResume() {
        if (isReportNew()) {
            if (hasBeenResumed) {
                finish();
            }
        }
        hasBeenResumed = true;
        super.onResume();
    }

    private void initData() {
        clickModel = (EarthQuakeInfoModel) getIntent().getSerializableExtra(Constants.EARTH_QUAKE_LIST_CLICK);
    }

    private void initTitleView() {
        title_view = (MapActivityTitleView) findViewById(R.id.title_view);
        if (null != getIntent().getExtras()) {
            title_view.setBtnStyle(BtnStyle.RIGHT_ACTION);
        } else {
            title_view.setLegendBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.btn_add_hl));
        }
        title_view.setTitleText(getString(R.string.quick_report_title));
    }

    private void initView() {
        ll_earthquake_searchview = (LinearLayout) findViewById(R.id.ll_earthquake_searchview);
        lv_earthquake = (PullToRefreshListView) findViewById(R.id.lv_earthquake);
        lv_earthquake.setPullLoadEnabled(false);
        lv_earthquake.setPullRefreshEnabled(true);
        mListView = lv_earthquake.getRefreshableView();
        mListView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mListView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_10));
        adapter = new EarthQuakeQuickReportAdapter(HostApplication.getApplication());
        mListView.setAdapter(adapter);
        reportLists = new ArrayList<EarthQuakeQuickReportModel>();
    }

    private void initLisenter() {
        title_view.setOnLegendListener(new QuickReportRightOnClickListener());
        ll_earthquake_searchview.setOnClickListener(new SearchViewOnClickListener());
        lv_earthquake.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestListSourceDatas();
                lv_earthquake.onPullDownRefreshComplete();
                lv_earthquake.setLastUpdateTime();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        mListView.setOnItemClickListener(new QuickReportListItemClick());
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ResultCode.EARTH_QUAKE_QUICK_REPORT_INFO_OK:
                getSearchResult(data);
                break;
            case ResultCode.EARTH_QUAKE_INFO_SEARCH_CANCEL:
                break;
            default:
                break;
        }
    }

    private void showNewZQSBOporator(Intent data) {
        quakeInfoModel = (EarthQuakeInfoModel) data.getSerializableExtra(EarthquakeFragment.QUAKE_ITEM_CLICK_RESULT);
        if (null == quakeInfoModel) {
            return;
        }
        Intent intent = new Intent(QuickReportListActivity.this, CustomReportActivity1.class);
        intent.putExtra(CustomViewInflater.REPORT_TITLE, R.string.quick_report_title);
        intent.putExtra(CustomViewInflater.REPORT_COMFROM, EarthQuakeZQSBReportOperator.class.getName());
        intent.putExtra(EarthQuakeZQSBReportOperator.REPORT_TYPE, EarthQuakeZQSBReportOperator.REPORT_TYPE_NEW);
        intent.putExtra("gid", quakeInfoModel.getId());
        startActivity(intent);
    }

    public void requestListSourceDatas() {
        LoadingDialogUtil.show(this, R.string.str_emergency_get_quickreportinfos);
        EmergencyService.getInstance().getEQQuickReportInfos(new GetEarthQuakeQuickReportInfosParameter(null, clickModel));
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void getSearchResult(Intent data) {
        searchParameter = (IRequestParameter) data.getSerializableExtra(Constants.EARTH_QUAKE_QUICK_REPORT_INFO_PARAM);
        if (null == searchParameter) {
            return;
        }
        LoadingDialogUtil.show(this, R.string.str_searching);
        //查询灾情速报信息
        EmergencyService.getInstance().getEQQuickReportInfos(searchParameter);
    }

    private boolean isReportNew() {
        if (getIntent().hasExtra(START_ACTION_NAME)) {
            if (START_ACTION_REPORT_NEW.equalsIgnoreCase(getIntent().getStringExtra(START_ACTION_NAME))) {
                return true;
            }
        }

        return false;
    }


    //设置查询框点击事件
    private class SearchViewOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.quick_report_search_title);
            bundle.putString(CustomViewInflater.REPORT_COMFROM, QuakeInfoSearchOperater.class.getName());
            bundle.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.info_search);
            // EARTH_QUAKE代表地震信息查询；QUICK_REPORT代表速报查询界面
            bundle.putInt(Constants.EARTH_QUAKE_INFO_SEARCH_TYPE, SearchType.QUICK_REPORT.getValue());
            Intent intent = new Intent(QuickReportListActivity.this, CustomReportActivity1.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCode.EARTH_QUAKE_QUICK_REPORT_INFO_SEARCH, bundle);
        }
    }

    //title_view 右边按钮点击事件
    private class QuickReportRightOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(QuickReportListActivity.this, CustomReportActivity1.class);
            intent.putExtra(CustomViewInflater.REPORT_TITLE, R.string.quick_report_title);
            intent.putExtra(CustomViewInflater.REPORT_COMFROM, EarthQuakeZQSBReportOperator.class.getName());
            intent.putExtra(EarthQuakeZQSBReportOperator.REPORT_TYPE, EarthQuakeZQSBReportOperator.REPORT_TYPE_NEW);
            intent.putExtra("gid", clickModel.getId());
            startActivity(intent);
        }
    }

    private class QuickReportListItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            if (null == reportLists || position > reportLists.size()) {
                return;
            }
            if (null != getIntent().getExtras()) {
                Intent intent = new Intent(QuickReportListActivity.this, CustomReportActivity1.class);
                intent.putExtra(CustomViewInflater.REPORT_TITLE, R.string.quick_report_title);
                intent.putExtra(CustomViewInflater.REPORT_COMFROM, EarthQuakeZQSBReportOperator.class.getName());
                intent.putExtra(EarthQuakeZQSBReportOperator.REPORT_TYPE, EarthQuakeZQSBReportOperator.REPORT_TYPE_UPDATE);
                intent.putExtra("report_item", reportLists.get(position));
                startActivity(intent);
            }
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        lv_earthquake.onPullUpRefreshComplete();
        lv_earthquake.onPullDownRefreshComplete();
        lv_earthquake.setLastUpdateTime();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_INFO:
                // 得到所有地震信息
                handleGetEarthQuakeQuickReportInfo(event);
                break;
            default:
                break;
        }
    }

    private void handleGetEarthQuakeQuickReportInfo(ResponseEvent event) {
        reportLists = event.getData();
        if (ListUtil.isEmpty(reportLists) || reportLists.size() == 0) {
            Toast.makeText(getApplicationContext(), R.string.no_quick_report_infos, Toast.LENGTH_LONG).show();
            return;
        }

        for (EarthQuakeInfoModel i : SessionManager.quakeInfoList) {
            for (EarthQuakeQuickReportModel j : reportLists) {
                if (i.getId() == j.getEarthQuakeId()) {
                    j.setEarthQuakeName(i.getRegion());
                }
            }
        }

        adapter.setList(reportLists);
        adapter.notifyDataSetChanged();
    }
}
