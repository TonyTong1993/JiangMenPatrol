package com.ecity.cswatersupply.emergency.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.activity.EarthQuakeEmergencyInvestigationActivity;
import com.ecity.cswatersupply.emergency.activity.EarthquakeLocalInfoActivity;
import com.ecity.cswatersupply.emergency.activity.EmergencyConditionDetailActivity;
import com.ecity.cswatersupply.emergency.activity.ImportEarthQuakeListActivity;
import com.ecity.cswatersupply.emergency.activity.ReportSearchActivity;
import com.ecity.cswatersupply.emergency.adapter.EarthQuakeQuickReportAdapter;
import com.ecity.cswatersupply.emergency.menu.operator.EarthQuakeDZXCYJDCReportOperator;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.network.request.GetEQZQSBListParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeQuickReportInfosParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.emergency.view.CustomAlertDialog;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.network.RequestParameter;
import com.ecity.cswatersupply.project.network.request.SearchProjectParameter;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.shizhefei.fragment.LazyFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class ReportQueryInfoFragment extends LazyFragment {
    public static final String TAB_POSITION = "TAB_POSITION";
    public static final String INTENT_RELATE_GOING = "INTENT_RELATE_GOING";
    private int position;

    protected ListView mListView;
    protected LinearLayout ll_earthquake_searchview;
    protected PullToRefreshListView lv_earthquake;
    private List<EarthQuakeQuickReportModel> reportLists;//灾情速报列表信息
    private List<EarthQuakeQuickReportModel> investigationLists;//现场调查列表信息
    private RequestParameter.IRequestParameter searchParameter;
    private EarthQuakeQuickReportAdapter adapter;
    private EarthQuakeInfoModel quakeInfoModel;
    private boolean hasBeenResumed = false;
    private EarthQuakeInfoModel clickModel;

    public ReportQueryInfoFragment() {
    }

    public ReportQueryInfoFragment(int position) {
        this.position = position;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.activity_earthquake_listview);
        EventBusUtil.register(this);
        initData();
        initView();
        request();
        EmergencyService.getInstance().getEarthQuackeList(new GetEarthQuakeParameter(), ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL);
        initLisenter();
    }

    private void request() {
        //获取地震列表信息
        LoadingDialogUtil.show(getActivity(), R.string.str_emergency_get_quickreportinfos);
        if (position == 0) {
            EmergencyService.getInstance().getEQQuickReportInfos(new GetEQZQSBListParameter());
        } else {
            EmergencyService.getInstance().getInvestigationInfos(new GetEarthQuakeQuickReportInfosParameter(null, null));
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        EventBusUtil.unregister(this);
        super.onDestroyViewLazy();
    }

    private void initData() {
        reportLists = new ArrayList<EarthQuakeQuickReportModel>();
        investigationLists = new ArrayList<EarthQuakeQuickReportModel>();
    }

    private void initView() {
        MapActivityTitleView title_view = (MapActivityTitleView) findViewById(R.id.title_view);
        title_view.setVisibility(View.GONE);

        ll_earthquake_searchview = (LinearLayout) findViewById(R.id.ll_earthquake_searchview);
        lv_earthquake = (PullToRefreshListView) findViewById(R.id.lv_earthquake);
        lv_earthquake.setPullLoadEnabled(false);
        lv_earthquake.setPullRefreshEnabled(true);
        mListView = lv_earthquake.getRefreshableView();
        mListView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mListView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_10));
        adapter = new EarthQuakeQuickReportAdapter(HostApplication.getApplication());
        mListView.setAdapter(adapter);
    }

    private void initLisenter() {
        lv_earthquake.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                request();
                lv_earthquake.onPullDownRefreshComplete();
                lv_earthquake.setLastUpdateTime();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        ll_earthquake_searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportQueryInfoFragment.this.getActivity(), ReportSearchActivity.class);
                intent.putExtra(TAB_POSITION, position);
                startActivity(intent);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionItem, long id) {
                SessionManager.isImageNotEdit = true;
                if (position == 1) {
                    Intent intent = new Intent(getActivity(), CustomReportActivity1.class);
                    intent.putExtra(CustomViewInflater.REPORT_TITLE, R.string.emergency_investigation_title);
                    intent.putExtra(CustomViewInflater.REPORT_COMFROM, EarthQuakeDZXCYJDCReportOperator.class.getName());
                    intent.putExtra("report_item", investigationLists.get(positionItem));
                    intent.putExtra("new_or_update", "update");
                    intent.putExtra("edit_flag", "edit_flag");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), EmergencyConditionDetailActivity.class);
                    intent.putExtra("report_item", reportLists.get(positionItem));
                    startActivity(intent);
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int positionitem, long id) {
                if (position == 0) {
                    EarthQuakeQuickReportModel model = reportLists.get(positionitem);
                    CustomAlertDialog dialog = new CustomAlertDialog(ReportQueryInfoFragment.this.getActivity(), "提示", "是否进行关联操作", new MyCustomDialogListener(model), CustomAlertDialog.DialogStyle.YESNO);
                    dialog.setNegativeButtonTitle("确定");
                    dialog.setPositiveButtonTitle("取消");
                    dialog.show();
                }

                return true;
            }
        });
    }

    class MyCustomDialogListener implements CustomAlertDialog.OnCustomDialogListener {
        private EarthQuakeQuickReportModel model;

        MyCustomDialogListener(EarthQuakeQuickReportModel model) {
            this.model = model;
        }

        @Override
        public void back(boolean result) {
            if (!result) {
                //进行关联操作
//                Intent intent = new Intent(getActivity(), EarthquakeLocalInfoActivity.class);
                Intent intent = new Intent(getActivity(), ImportEarthQuakeListActivity.class);
                intent.putExtra(INTENT_RELATE_GOING, model);
                startActivity(intent);
            }
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.EMERGENCY_REPORT_QUERY_SEARCH:
                SearchProjectParameter searchData = event.getData();
                String proName = searchData.getProName();
                String proCode = searchData.getProCode();
                int positionFrom = searchData.getProTypeid();

                if (position == positionFrom) {
                    List<EarthQuakeQuickReportModel> searchList = new ArrayList<>();
                    List<EarthQuakeQuickReportModel> list = new ArrayList<>();
                    list = position == 0 ? reportLists : investigationLists;

                    for (EarthQuakeQuickReportModel i : list) {
                        if (proCode.equals("全部")) {
                            if (i.getSurveyPerson().contains(proName)) {
                                searchList.add(i);
                            }
                        } else {
                            if (i.getSurveyPerson().contains(proName) && i.getEarthQuakeName() != null && i.getEarthQuakeName().equals(proCode)) {
                                searchList.add(i);
                            }
                        }
                    }
                    adapter.setList(searchList);
                }
                break;

            case UIEventStatus.EMERGENCY_RELATE_FINISH:
                request();
                break;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        lv_earthquake.onPullUpRefreshComplete();
        lv_earthquake.onPullDownRefreshComplete();
        lv_earthquake.setLastUpdateTime();
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_EARTHQUAKE_ALL:
                // 得到所有地震信息(范围)
                handleGetEarthQuakeInfoList(event);
                break;
            case ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_INFO:
                LoadingDialogUtil.dismiss();
                // 得到所有灾情速报信息
                if(0 == position) {
                    reportLists = event.getData();
                    if (ListUtil.isEmpty(reportLists) || reportLists.size() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.no_quick_report_infos, Toast.LENGTH_LONG).show();
                        return;
                    }
                    initListView(reportLists);
                }
                break;
            case ResponseEventStatus.EMERGENCY_GET_INVESGATION_INFO:
                LoadingDialogUtil.dismiss();
                // 得到所有现场调查信息
                if(1 == position) {
                    investigationLists = event.getData();
                    if (ListUtil.isEmpty(investigationLists) || investigationLists.size() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.no_investigation_infos, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    initListView(investigationLists);
                }
                break;
            default:
                break;
        }
    }

    private void initListView(List<EarthQuakeQuickReportModel> reportLists) {
        for (EarthQuakeInfoModel i : SessionManager.quakeInfoList) {
            for (EarthQuakeQuickReportModel j : reportLists) {
                if (i.getId() == j.getEarthQuakeId()) {
                    j.setEarthQuakeName(i.getRegion());
                }
            }
        }
        listSort(reportLists);
        adapter.setList(reportLists);
        adapter.notifyDataSetChanged();
    }

    private void listSort(List<EarthQuakeQuickReportModel> list) {
        Collections.sort(list, new Comparator<EarthQuakeQuickReportModel>() {
            @Override
            public int compare(EarthQuakeQuickReportModel o1, EarthQuakeQuickReportModel o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getSurveytTime());
                    Date dt2 = format.parse(o2.getSurveytTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    private void handleGetEarthQuakeInfoList(ResponseEvent event) {
        List<EarthQuakeInfoModel> quakeInfoLists = event.getData();
        SessionManager.quakeInfoList = quakeInfoLists;
        if(0 == position) {
            initListView(reportLists);
        } else {
            initListView(investigationLists);
        }
    }
}
