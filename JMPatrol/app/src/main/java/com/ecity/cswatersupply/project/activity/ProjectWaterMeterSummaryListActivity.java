package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.adapter.ProjectSummaryListAdapter;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.view.ProjectWaterMeterSummaryView;
import com.ecity.cswatersupply.ui.activities.ABasePullToRefreshActivity;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectWaterMeterSummaryListActivity extends ABasePullToRefreshActivity<JSONObject> {
    public static final String INTENT_KEY_TYPE = "INTENT_KEY_TYPE";
    public static final String INTENT_KEY_YEAR = "INTENT_KEY_YEAR";
    public static final String INTENT_KEY_MONTH = "INTENT_KEY_MONTH";
    public static final String INTENT_KEY_START_TIME = "INTENT_KEY_START_TIME";
    public static final String INTENT_KEY_END_TIME = "INTENT_KEY_END_TIME";
    public static final String INTENT_KEY_STATE = "INTENT_KEY_STATE";
    private String type;
    private int year;
    private int month;
    private String startTime;
    private String endTime;
    private List<JSONObject> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        initDateSource();
        initUI();
        requestSummaryInfo();
    }

    @Override
    protected String getScreenTitle() {
        return null;
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected ArrayListAdapter<JSONObject> prepareListViewAdapter() {
        return new ProjectSummaryListAdapter(this, new ProjectWaterMeterSummaryView());
    }

    @Override
    protected List<JSONObject> prepareDataSource() {
        return null;
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return false;
    }

    @Override
    protected boolean isPullLoadEnabled() {
        return false;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String section = dataSource.get(position).optString("datement");
                String[] dates;
                String substring = section.substring(section.length() - 2, section.length());
                if (month < 1) {
                    dates = getDateRangeByMonth(year, Integer.valueOf(substring));
                } else {
                    dates = getDateRangeByDay(year, month, Integer.valueOf(substring));
                }

                Intent intent = new Intent(ProjectWaterMeterSummaryListActivity.this, WaterMeterListActivity.class);
                intent.putExtra(INTENT_KEY_TYPE, type);
                intent.putExtra(INTENT_KEY_START_TIME, dates[0]);
                intent.putExtra(INTENT_KEY_END_TIME, dates[1]);
                startActivity(intent);
            }
        };
    }

    private String[] getDateRangeByMonth(int year, int month) {
        String startTime = new StringBuilder().append(year).append("-").append(month).append("-01 00:00:00").toString();
        List<String> days = DateUtil.getAllDaysInMonth(year, month, false);
        String day = days.get(0);
        String endTime = new StringBuilder().append(year).append("-").append(month).append("-").append(day).append(" 00:00:00").toString();

        return new String[]{startTime, endTime};
    }

    private String[] getDateRangeByDay(int year, int month, int day) {
        String startTime = new StringBuilder().append(year).append("-").append(month).append("-").append(day).append(" 00:00:00").toString();
        String endTime = new StringBuilder().append(year).append("-").append(month).append("-").append(day).append(" 23:59:59").toString();

        return new String[]{startTime, endTime};
    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return null;
    }

    private void initUI() {
        String title;
        if (month < 1) {
            title = String.format(getString(R.string.project_title_summary_by_year), year);
        } else {
            title = String.format(getString(R.string.project_title_summary_by_month), year, month);
        }
        getTitleView().setTitleText(title);
    }

    private void initDateSource() {
        type = String.valueOf(getIntent().getIntExtra(INTENT_KEY_TYPE, -1));
        year = getIntent().getIntExtra(INTENT_KEY_YEAR, 0);
        month = getIntent().getIntExtra(INTENT_KEY_MONTH, 0);
        startTime = getIntent().getStringExtra(INTENT_KEY_START_TIME);
        endTime = getIntent().getStringExtra(INTENT_KEY_END_TIME);
    }

    private void requestSummaryInfo() {

        ProjectService.getInstance().queryWaterMeterSummaryInfo(year, month);
    }

    private void handleGetSummaryInfoResponse(ResponseEvent event) {
        JSONObject response = event.getData();
        JSONArray summaryInfo = response.optJSONArray("features");
        dataSource = new ArrayList<JSONObject>();
        for (int i = 0; i < summaryInfo.length(); i++) {
            dataSource.add(summaryInfo.optJSONObject(i));
        }
        updateDataSource(dataSource);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_GET_WATERMETER_SUMMARY:
                LoadingDialogUtil.dismiss();
                handleGetSummaryInfoResponse(event);
                break;
            default:
                break;
        }
    }
}
