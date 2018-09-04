package com.ecity.cswatersupply.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.PumpsManagementAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.inpsectitem.PumpInspectItemViewXtd;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/***
 * Created by MaoShouBei on 2017/5/12.
 */

public class PumpManagementActivity extends BaseActivity {

    private CustomTitleView titleView;
    private List<PumpInsSelectValue> selectValueList;
    private PumpsManagementAdapter mPumpsManagementAdapter;
    private PullToRefreshListView refreshPunishListView;
    private ListView lvRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_management);
        EventBusUtil.register(this);
        initUI();
        initTitle();
        bindEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPumpsList();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        refreshPunishListView = (PullToRefreshListView) findViewById(R.id.lv_pumps);
        refreshPunishListView.setPullLoadEnabled(true);
        refreshPunishListView.setPullRefreshEnabled(true);
        lvRecords = refreshPunishListView.getRefreshableView();
        lvRecords.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mPumpsManagementAdapter = new PumpsManagementAdapter(this);
    }

    private void requestPumpsList() {
        LoadingDialogUtil.show(this, R.string.str_searching);
        String url = ServiceUrlManager.getInstance().getSpacialSearchUrl() + "/11/query";
        ReportEventService.getInstance().queryAllPumps(url);
    }

    private void initTitle() {
        titleView.setTitleText(ResourceUtil.getStringById(R.string.pump_management_title));
        titleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
    }

    private void bindEvents() {
        findViewById(R.id.ll_searchview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchPumps();
            }
        });

        lvRecords.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(PumpManagementActivity.this, PumpDetailActivity.class);
                intent.putExtra(PumpSelectActivity.SELECT_PUMP, selectValueList.get(i));
                startActivity(intent);
            }
        });

        refreshPunishListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestPumpsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestPumpsList();
            }
        });
    }

    private void gotoSearchPumps() {
        Intent intent = new Intent(this, PumpSelectActivity.class);
        intent.putExtra(PumpInspectItemViewXtd.INTENT_FROM_PUMP_INSPECT, false);
        startActivityForResult(intent, PumpSelectActivity.REQPUMP);
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EVENT_GET_ALL_PUMP_INFO:
                handleAllPumps(event);
                break;
            default:
                break;
        }
    }

    private void handleAllPumps(ResponseEvent event) {
        selectValueList = event.getData();
        MyComparator comparator = new MyComparator();
        Collections.sort(selectValueList, comparator);
        List<PumpInsSelectValue> temp = new ArrayList<>();
        for (PumpInsSelectValue pump : selectValueList) {
            if (StringUtil.isBlank(pump.getName()) || "".equals(pump.getName())) {
                temp.add(pump);
            }
        }
        selectValueList.removeAll(temp);
        temp.clear();
        mPumpsManagementAdapter.setList(selectValueList);
        lvRecords.setAdapter(mPumpsManagementAdapter);
        mPumpsManagementAdapter.notifyDataSetChanged();
        refreshPunishListView.onPullDownRefreshComplete();
        refreshPunishListView.onPullUpRefreshComplete();
        refreshPunishListView.setLastUpdateTime();
    }

    private class MyComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            PumpInsSelectValue pump0 = (PumpInsSelectValue) arg0;
            PumpInsSelectValue pump1 = (PumpInsSelectValue) arg1;
            return pump0.getName().compareTo(pump1.getName());
        }
    }
}
