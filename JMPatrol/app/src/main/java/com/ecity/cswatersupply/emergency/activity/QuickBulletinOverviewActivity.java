package com.ecity.cswatersupply.emergency.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.chart.charts.BarChart;
import com.ecity.chart.data.BarData;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.adapter.QBOGridAdapter;
import com.ecity.cswatersupply.emergency.fragment.EarthquakeFragment;
import com.ecity.cswatersupply.emergency.model.BarCharEntry;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.QBOGridModel;
import com.ecity.cswatersupply.emergency.model.QBOModel;
import com.ecity.cswatersupply.emergency.network.request.GetQBODatasParameter;
import com.ecity.cswatersupply.emergency.network.request.GetSBZLParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.emergency.utils.BarCharUtils;
import com.ecity.cswatersupply.emergency.utils.DataConvertUtil;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/***
 * 速报总览界面
 * @author Gxx 2016-11-21
 */
public class QuickBulletinOverviewActivity extends BaseActivity{
    private final static int TIME_INTERVAL= 5;

    private CustomTitleView titleView;
    private RelativeLayout tvReLayout;
    private TextView tvQuakeRegion;
    private TextView tvQuakeTime;
    private GridView gridView;
    private BarChart barChart;
    private List<QBOGridModel> items;
    private QBOGridAdapter qboGridAdapter;
    private List<BarCharEntry> entryList;
    private QBOModel qboModel;
    private Map<String,Object> map;
    private ScheduledExecutorService executorService;
    private EarthQuakeInfoModel quakeInfoModel;
    private int eqid;
    private boolean isStop = true;

    /**
     * 设置柱状图数据
     */
    private BarData barData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_bulletin_overview);
        EventBusUtil.register(this);
        initUI();
        initData();
    }

    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_tile);
        titleView.setTitleText(R.string.quick_bulletin_overview_title);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);

        tvReLayout = (RelativeLayout) findViewById(R.id.rl_quake_info);
        tvQuakeRegion = (TextView) findViewById(R.id.tv_quake_region);
        tvQuakeTime = (TextView) findViewById(R.id.tv_quake_time);
        gridView = (GridView) findViewById(R.id.gridview);
        barChart = (BarChart) findViewById(R.id.barchart);

        qboGridAdapter = new QBOGridAdapter(this);
        gridView.setAdapter(qboGridAdapter);
        tvReLayout.setOnClickListener(new QuakeInfoClickListener());
        map = new HashMap<String, Object>();
    }

    private void initData() {
        if(ListUtil.isEmpty(SessionManager.quakeInfoList)) {
            Intent intent = new Intent(QuickBulletinOverviewActivity.this, NewsAnnounActivity.class);
            intent.putExtra(Constants.REQUEST_CODE_FLAG, RequestCode.REQUEST_TO_EARTH_QUAKE_LIST);
            startActivityForResult(intent, RequestCode.REQUEST_TO_EARTH_QUAKE_LIST);
            return;
        }
        quakeInfoModel = SessionManager.quakeInfoList.get(0);
        eqid = quakeInfoModel.getId();
        tvQuakeRegion.setText(quakeInfoModel.getLocation());
        tvQuakeTime.setText(quakeInfoModel.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStop == true) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(new TimerIncreasedRunnable(),
                    0, 1000 * TIME_INTERVAL, TimeUnit.MILLISECONDS);
            isStop = false;
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
        if (executorService != null){
            executorService.shutdown();
        }
        executorService = null;
        EventBusUtil.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                quakeInfoModel = (EarthQuakeInfoModel) data.getSerializableExtra(EarthquakeFragment.QUAKE_ITEM_CLICK_RESULT);
                if(null == quakeInfoModel) {
                    return;
                }
                tvQuakeRegion.setText(quakeInfoModel.getLocation());
                tvQuakeTime.setText(quakeInfoModel.getTime());
                requestQBODatas();
                break;
            default:
                break;
        }
    }

    private void requestQBODatas() {
        if(ListUtil.isEmpty(SessionManager.quakeInfoList)) {
            return;
        }
        eqid = quakeInfoModel.getId();
        EmergencyService.getInstance().getQBODatas(new GetSBZLParameter(eqid));
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_QBODATDS:
                // 获取一条地震总览信息数据
                handleGetQBODatas(event);
                break;
            default:
                break;
        }
    }

    private void handleGetQBODatas(ResponseEvent event) {
        map = event.getData();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if("qboData".equals(entry.getKey())) {
                qboModel = (QBOModel) entry.getValue();
            } else if("barCharData".equals(entry.getKey())) {
                entryList = (List<BarCharEntry>) entry.getValue();
            }
        }
        items = buildGridSourceData(qboModel);
        qboGridAdapter.setList(items);
        initCalBarChart();
    }

    /**
     * 初始化条形图
     */
    public void initCalBarChart() {
        barData = BarCharUtils.getInstance().generateCalData(entryList);
        BarCharUtils.getInstance().initBarChart(barData, barChart);
    }

    /**
     * @param qboModel
     * @return
     */
    private List<QBOGridModel> buildGridSourceData(QBOModel qboModel) {
        List<QBOGridModel> list = new ArrayList<QBOGridModel>();
        list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_pdead), DataConvertUtil.normalize(qboModel.getPdead()), "", R.color.red));
        list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_pss), DataConvertUtil.normalize(qboModel.getPss()), "", R.color.red));
        list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_ptotal), DataConvertUtil.normalize(qboModel.getPtotal()), "", R.color.red));
        if (StringUtil.isEmpty(qboModel.getLiedu())){
            list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_liedu), getResources().getString(R.string.no_data) , "", R.color.col_app_theme));
        }else{
            list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_liedu), qboModel.getLiedu() , "", R.color.col_app_theme));
        }
        if (StringUtil.isEmpty(qboModel.getLiedu())){
            list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_bnum), getResources().getString(R.string.no_data) , "", R.color.slateblue));
        }else{
            list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_bnum), qboModel.getBnum() , "", R.color.slateblue));
        }
        list.add(new QBOGridModel(ResourceUtil.getStringById(R.string.earth_quake_money),DataConvertUtil.normalize(qboModel.getMoney()), "", R.color.moccasin));

        return list;
    }

    private class QuakeInfoClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(QuickBulletinOverviewActivity.this, NewsAnnounActivity.class);
            intent.putExtra(Constants.REQUEST_CODE_FLAG, RequestCode.REQUEST_TO_EARTH_QUAKE_LIST);
            startActivityForResult(intent, RequestCode.REQUEST_TO_EARTH_QUAKE_LIST);
        }
    }

    private class TimerIncreasedRunnable implements Runnable {

        @Override
        public void run() {
            try {
                if (isStop){
                    return;
                }
                requestQBODatas();
            } catch (Throwable t) {
                
            }
        }
    }

}
