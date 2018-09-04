package com.ecity.cswatersupply.emergency.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.ecity.cswatersupply.emergency.adapter.EarthQuakeEmergencyInvestigationAdapter;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoSearchOperater;
import com.ecity.cswatersupply.emergency.menu.operator.EarthQuakeDZXCYJDCReportOperator;
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
import com.ecity.cswatersupply.utils.TextUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 地震现场应急列表调查Activity
 *
 * @author Administrator
 */
public class EarthQuakeEmergencyInvestigationActivity extends BaseActivity {

    private List<EarthQuakeQuickReportModel> investigationLists;
    private MapActivityTitleView title_view;
    protected ListView mListView;
    protected LinearLayout ll_earthquake_searchview;
    protected PullToRefreshListView lv_earthquake;
    private EarthQuakeEmergencyInvestigationAdapter adapter;
    private EarthQuakeQuickReportModel quickReportInfoModel;
    private IRequestParameter searchParameter;
    private EarthQuakeInfoModel clickModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_listview);
        EventBusUtil.register(this);
        initData();
        initTitleView();
        initView();
        requestListSourceDatas();
        initLisenter();
    }

    private void initData() {
        clickModel = (EarthQuakeInfoModel) getIntent().getSerializableExtra(Constants.EARTH_QUAKE_LIST_CLICK);
    }

    private void initTitleView() {
        title_view = (MapActivityTitleView) findViewById(R.id.title_view);
        title_view.setBtnStyle(BtnStyle.RIGHT_ACTION);
        title_view.setLegendBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.btn_add_hl));
        title_view.setTitleText(getString(R.string.emergency_investigation_title));

    }

    private void initView() {
        ll_earthquake_searchview = (LinearLayout) findViewById(R.id.ll_earthquake_searchview);
        lv_earthquake = (PullToRefreshListView) findViewById(R.id.lv_earthquake);
        lv_earthquake.setPullLoadEnabled(false);
        lv_earthquake.setPullRefreshEnabled(true);
        mListView = lv_earthquake.getRefreshableView();
        mListView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mListView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_10));
        investigationLists = new ArrayList<EarthQuakeQuickReportModel>();
        adapter = new EarthQuakeEmergencyInvestigationAdapter(HostApplication.getApplication());
        mListView.setAdapter(adapter);
    }

    private void initLisenter() {
        title_view.setOnLegendListener(new RightOnClickListener());
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

        mListView.setOnItemClickListener(new ItemClickListener());
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public void requestListSourceDatas() {
        LoadingDialogUtil.show(this, R.string.str_emergency_get_investigationinfos);
        EmergencyService.getInstance().getInvestigationInfos(new GetEarthQuakeQuickReportInfosParameter(null, clickModel));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ResultCode.EARTH_QUAKE_QUICK_REPORT_INFO_OK:
//                getSearchResult(data);
                getLocalSearchResult(data);
                break;
            case ResultCode.EARTH_QUAKE_INFO_SEARCH_CANCEL:
                break;
            case RESULT_OK:
                addInvestigationInfo(data);
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    private void getSearchResult(Intent data) {
        searchParameter = (IRequestParameter) data.getSerializableExtra(Constants.EARTH_QUAKE_QUICK_REPORT_INFO_PARAM);
        if (null == searchParameter) {
            return;
        }
        LoadingDialogUtil.show(this, R.string.str_searching);
        //查询现场调查信息
        EmergencyService.getInstance().getInvestigationInfos(searchParameter);
    }

    private void getLocalSearchResult(Intent data) {
        searchParameter = (IRequestParameter) data.getSerializableExtra(Constants.EARTH_QUAKE_QUICK_REPORT_INFO_PARAM);
        if (null == searchParameter) {
            return;
        }
        String reporter = searchParameter.toMap().get("reporterid");
        List<EarthQuakeQuickReportModel> results = new ArrayList<>();
        for(EarthQuakeQuickReportModel model : investigationLists) {
            if(!StringUtil.isEmpty(model.getSurveyPerson()) && model.getSurveyPerson().equalsIgnoreCase(reporter)) {
                results.add(model);
            }
        }
        investigationLists = results;
        initInvestigationInfo();
    }

    private void addInvestigationInfo(Intent data) {
        quickReportInfoModel = (EarthQuakeQuickReportModel) data.getSerializableExtra(QuickReportListActivity.QUICK_REPORT_ITEM_CLICK_RESULT);
        if (null == quickReportInfoModel) {
            return;
        }
        investigationLists.add(quickReportInfoModel);

        startXcdc(quickReportInfoModel);
    }

    private void startXcdc(EarthQuakeQuickReportModel model) {
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.emergency_investigation_title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, EarthQuakeDZXCYJDCReportOperator.class.getName());
        bundle.putString(CustomViewInflater.REPORT_COMFROM, EarthQuakeDZXCYJDCReportOperator.class.getName());
        bundle.putSerializable("report_item", model);
        UIHelper.startActivityWithExtra(CustomReportActivity1.class, bundle);
    }

    //title_view 右边按钮点击事件
    private class RightOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            SessionManager.isImageNotEdit = false;
            Intent intent = new Intent(EarthQuakeEmergencyInvestigationActivity.this, CustomReportActivity1.class);
            intent.putExtra(CustomViewInflater.REPORT_TITLE, R.string.emergency_investigation_title);
            intent.putExtra(CustomViewInflater.REPORT_COMFROM, EarthQuakeDZXCYJDCReportOperator.class.getName());
            EarthQuakeQuickReportModel model = new EarthQuakeQuickReportModel();
            model.setGid(0);
            model.setEarthQuakeId(clickModel.getId());
            model.setEarthQuakeName(clickModel.getRegion());
            model.setSurveyPerson(HostApplication.getApplication().getCurrentUser().getLoginName());
            intent.putExtra("report_item", model);
            intent.putExtra("new_or_update", "new");
            startActivity(intent);
        }
    }

    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            Intent intent = new Intent(EarthQuakeEmergencyInvestigationActivity.this, CustomReportActivity1.class);
            intent.putExtra(CustomViewInflater.REPORT_TITLE, R.string.emergency_investigation_title);
            intent.putExtra(CustomViewInflater.REPORT_COMFROM, EarthQuakeDZXCYJDCReportOperator.class.getName());
            intent.putExtra("report_item", investigationLists.get(position));
            intent.putExtra("new_or_update", "update");
            startActivity(intent);
        }
    }

    //设置查询框点击事件
    private class SearchViewOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(ListUtil.isEmpty(investigationLists)) {
                ToastUtil.showShort(R.string.str_emergency_without_investigationinfos);
                return;
            }
            ArrayList<String> reporters = getAllReporters();
            Bundle bundle = new Bundle();
            bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.quick_report_search_title);
            bundle.putString(CustomViewInflater.REPORT_COMFROM, QuakeInfoSearchOperater.class.getName());
            bundle.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.info_search);
            bundle.putInt("eqid", clickModel.getId());
            bundle.putSerializable("reporters", reporters);
            // EARTH_QUAKE代表地震信息查询；QUICK_REPORT代表速报查询界面
            bundle.putInt(Constants.EARTH_QUAKE_INFO_SEARCH_TYPE, SearchType.QUICK_REPORT.getValue());
            Intent intent = new Intent(EarthQuakeEmergencyInvestigationActivity.this, CustomReportActivity1.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCode.EARTH_QUAKE_QUICK_REPORT_INFO_SEARCH, bundle);
        }
    }

    public ArrayList<String> getAllReporters() {
        ArrayList<String> reporters = new ArrayList<>();
        if(ListUtil.isEmpty(investigationLists)) {
            return reporters;
        }
        for(EarthQuakeQuickReportModel model : investigationLists) {
            if(!StringUtil.isEmpty(model.getSurveyPerson()) && !reporters.contains(model.getSurveyPerson())) {
                reporters.add(model.getSurveyPerson());
            }
        }
        return reporters;
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
            case ResponseEventStatus.EMERGENCY_GET_INVESGATION_INFO:
                // 得到所有地震信息
                investigationLists = event.getData();
                initInvestigationInfo();
                break;
            default:
                break;
        }
    }

    private void initInvestigationInfo() {
        if (ListUtil.isEmpty(investigationLists) || investigationLists.size() == 0) {
            Toast.makeText(getApplicationContext(), R.string.no_investigation_infos, Toast.LENGTH_SHORT).show();
        }
        adapter.setList(investigationLists);
        adapter.notifyDataSetChanged();
    }

}
