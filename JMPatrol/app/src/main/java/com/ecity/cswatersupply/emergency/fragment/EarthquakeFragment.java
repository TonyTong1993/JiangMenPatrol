package com.ecity.cswatersupply.emergency.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.activity.DetailWebActivity;
import com.ecity.cswatersupply.emergency.activity.EarthQuakeInfoBaseListActivity;
import com.ecity.cswatersupply.emergency.adapter.ExpertAdapter;
import com.ecity.cswatersupply.emergency.adapter.QuakeInfoListAdapter;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoDistributionOperaterXtd;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoSearchOperater;
import com.ecity.cswatersupply.emergency.menu.UnUsualReportOperater;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EmergencyItem;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.model.SearchType;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.emergency.utils.CommonTool;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.network.RequestParameter;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.shizhefei.fragment.LazyFragment;

/**
 * 本地震情
 *
 * @author 49136
 */
public class EarthquakeFragment extends LazyFragment {

    public final static String QUAKE_ITEM_CLICK_RESULT = "QUAKE_ITEM_CLICK_RESULT";
    private final static int INTERVAL = 1000 * 60 * 2;

    public List<EarthQuakeInfoModel> quakeInfoLists = new ArrayList<EarthQuakeInfoModel>();
//    private MapActivityTitleView titleView;
    private ListView mListView;
    private LinearLayout searchLayout;
    private PullToRefreshListView pullRefreshListView;
    private ArrayListAdapter<EarthQuakeInfoModel> mAdapter;
    private RequestParameter.IRequestParameter searchParameter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.activity_earthquake_information);
        EventBusUtil.register(this);
//        initTitleView();
        initView();
        initLisenter();
        requestQuakeInfoList();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
//        if (validate()) {
//            requestQuakeInfoList();
//        } else {
//            mAdapter.setList(SessionManager.quakeInfoList);
//            mAdapter.notifyDataSetChanged();
//        }
    }

//    private void initTitleView() {
//        titleView = (MapActivityTitleView) findViewById(R.id.view_title);
//        if (null != getActivity().getIntent().getExtras()) {//TODO
//            titleView.setBtnStyle(MapActivityTitleView.BtnStyle.ONLY_BACK);
//        } else {
//            titleView.setBtnStyle(MapActivityTitleView.BtnStyle.RIGHT_ACTION);
//            titleView.setLegendBackground(getResources().getDrawable(R.drawable.icon_map_unfocus));
//        }
//        titleView.setTitleText(getResources().getString(R.string.earth_quake_information_title_quick));
//    }

    private void initView() {
        MapActivityTitleView titleView = (MapActivityTitleView)findViewById(R.id.view_title);
        titleView.setVisibility(View.GONE);

        searchLayout = (LinearLayout) findViewById(R.id.ll_searchview);
        pullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_listview);
        pullRefreshListView.setPullLoadEnabled(false);
        pullRefreshListView.setPullRefreshEnabled(true);

        mListView = pullRefreshListView.getRefreshableView();
        mListView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mListView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_4));

        mAdapter = new QuakeInfoListAdapter(getActivity());
        mAdapter.setList(quakeInfoLists);
        mListView.setAdapter(mAdapter);
    }

    private void initLisenter() {
//        titleView.setOnLegendListener(new EarthquakeFragment.RigthActionClickListener());
        searchLayout.setOnClickListener(new EarthquakeFragment.SearchViewOnClickListener());
        pullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestQuakeInfoList();
                pullRefreshListView.onPullDownRefreshComplete();
                pullRefreshListView.setLastUpdateTime();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mListView.setOnItemClickListener(new EarthquakeFragment.OnListItemClickListener());
    }

//    public void onBackButtonClicked(View v) {//TODO
//        gefinish();
//    }

    private void requestQuakeInfoList() {
        SessionManager.lastUpdateTime = System.currentTimeMillis();
        LoadingDialogUtil.show(getActivity(), R.string.getting_earth_quake_infos);
        EmergencyService.getInstance().getEarthQuackeList(new GetEarthQuakeParameter(), ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL);
    }

    private boolean validate() {
        //最多2分钟更新一次
        if ((System.currentTimeMillis() - SessionManager.lastUpdateTime) > INTERVAL) {
            SessionManager.lastUpdateTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (resultCode) {
//            case ResultCode.EARTH_QUAKE_INFO_OK:
//                getSearchResult(data);
//                break;
//            case ResultCode.EARTH_QUAKE_INFO_SEARCH_CANCEL:
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBusUtil.unregister(this);
    }


    private void getSearchResult(Intent data) {
        searchParameter = (RequestParameter.IRequestParameter) data.getSerializableExtra(Constants.EARTH_QUAKE_INFO_PARAM);
        if (null == searchParameter) {
            Toast.makeText(getApplicationContext(), R.string.no_earth_quake_infos, Toast.LENGTH_LONG).show();
            return;
        }
        //调用网络
        EmergencyService.getInstance().getEarthQuackeList(searchParameter, ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL);
        LoadingDialogUtil.show(getActivity(), R.string.str_searching);
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.EMERGENCY_ANNONOUN_SEARCH:
                Intent data = event.getData();
                getSearchResult(data);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        pullRefreshListView.onPullUpRefreshComplete();
        pullRefreshListView.onPullDownRefreshComplete();
        pullRefreshListView.setLastUpdateTime();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL:
                // 得到所有地震信息
                handleGetEarthQuakeInfoList(event);
                break;
            default:
                break;
        }
    }

    private void handleGetEarthQuakeInfoList(ResponseEvent event) {
        quakeInfoLists = event.getData();
        SessionManager.quakeInfoList = quakeInfoLists;
        if (null == quakeInfoLists) {
            Toast.makeText(getApplicationContext(), R.string.no_earth_quake_infos, Toast.LENGTH_LONG).show();
            return;
        }
        mAdapter.setList(quakeInfoLists);
        mAdapter.notifyDataSetChanged();
    }

    //设置查询框点击事件
    private class SearchViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CustomReportActivity1.class);
            Bundle bundle = new Bundle();
            bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.earth_quake_infor_search_title);
            bundle.putString(CustomViewInflater.REPORT_COMFROM, QuakeInfoSearchOperater.class.getName());
            bundle.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.ok);
            //SearchType.EARTH_QUAKE为地震信息查询；SearchType.QUICK_REPORT为速报信息查询
            bundle.putInt(Constants.EARTH_QUAKE_INFO_SEARCH_TYPE, SearchType.EARTH_QUAKE.getValue());
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCode.EARTH_QUAKE_INFO_SEARCH, bundle);
        }
    }

    private class RigthActionClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra(MapActivity.MAP_OPERATOR, QuakeInfoDistributionOperaterXtd.class.getName());
            intent.putExtra(MapActivity.MAP_TITLE, getString(R.string.earth_quake_information_title));
            startActivity(intent);
        }
    }

    private class OnListItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            if (null != getActivity().getIntent().getExtras()) {
                if (getActivity().getIntent().getExtras().containsKey(Constants.REQUEST_CODE_FLAG)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(QUAKE_ITEM_CLICK_RESULT, quakeInfoLists.get(position));
                    getActivity().setResult(getActivity().RESULT_OK, new Intent().putExtras(bundle));
                    getActivity().finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.emergency_destruction_report_title);
                    bundle.putString(CustomViewInflater.REPORT_COMFROM, UnUsualReportOperater.class.getName());
                    bundle.putString(CustomViewInflater.EVENTTYPE, "0004");
                    bundle.putString(UnUsualReportOperater.EARTHQUAKEID, String.valueOf(quakeInfoLists.get(position).getId()));
                    UIHelper.startActivityWithExtra(CustomReportActivity1.class, bundle);
                }
            } else {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra(Constants.EARTH_QUAKE_LIST_CLICK, quakeInfoLists.get(position));
                intent.putExtra(MapActivity.MAP_OPERATOR, QuakeInfoDistributionOperaterXtd.class.getName());
                intent.putExtra(MapActivity.MAP_TITLE, getString(R.string.earth_quake_information_title));
                startActivity(intent);
            }
        }
    }


}
