package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.chart.charts.PieChart;
import com.ecity.chart.components.Legend;
import com.ecity.chart.data.Entry;
import com.ecity.chart.data.PieData;
import com.ecity.chart.data.PieDataSet;
import com.ecity.chart.utils.ColorTemplate;
import com.ecity.chart.utils.MathHelper;
import com.ecity.chart.utils.PercentFormatter;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.utils.EcityConstants;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.adapter.ProjectAnalysisAdapter;
import com.ecity.cswatersupply.project.model.SumaryPieDataModel;
import com.ecity.cswatersupply.project.network.response.GetProjectWaterMeterCountResponse;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.MapFragmentTitleView;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */

public class WaterMeterAllInfoActivity extends BaseActivity {
    public static final String INTENT_KEY_WATER_METER_FLAG = "INTENT_KEY_WATER_METER_FLAG";

    private TextView tv_selected_date;
    private TextView tv_project_sum;
    private TextView tv_project_all_info_title;
    private PieChart mPieView;
    private ListView lv_project_all_info;

    private String projectType = "0";
    private boolean isMonthChecked;
    private String defaultTime;
    private String firstDate = DateUtil.getYearFirstDay(DateUtil.getCurrentDate());
    private String lastDate = DateUtil.getYearLastDay(DateUtil.getCurrentDate());
    private String selectTime;
    private List<SumaryPieDataModel> sumaryPieDatas;
    private double sumLength;
    private GetProjectWaterMeterCountResponse countData;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_make_main);
        EventBusUtil.register(this);
        initView();

        requestProjectInfoCount();
        setOnClickListener();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void initView() {
        tv_selected_date = (TextView) findViewById(R.id.tv_selected_date);
        tv_project_sum = (TextView) findViewById(R.id.tv_project_sum);

        mPieView = (PieChart) findViewById(R.id.view_workorder_pie_chart);
        tv_project_all_info_title = (TextView) findViewById(R.id.tv_project_all_info_title);
        lv_project_all_info = (ListView) findViewById(R.id.lv_project_all_info);

        LinearLayout spinner_type = (LinearLayout) findViewById(R.id.spinner_type);
        TextView tv_type = (TextView) findViewById(R.id.tv_type);

        ArrayList<String> arrayListDate = getArrayListDate();
        new CustomSpinner(this, spinner_type, tv_type, new MyTypeSpinnerListener(), arrayListDate, 0);

        RadioGroup rg_year_month = (RadioGroup) findViewById(R.id.rg_year_month);
        rg_year_month.setOnCheckedChangeListener(new MyRadioGroupOnClickListener());

        rg_year_month.setOrientation(RadioGroup.HORIZONTAL);
        RadioButton radioButton1 = new RadioButton(this);
        RadioButton radioButton2 = new RadioButton(this);
        radioButton1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        radioButton1.setTextColor(getResources().getColor(R.color.txt_black_normal));
        radioButton1.setTag("0");
        radioButton1.setText("年");
        radioButton2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        radioButton2.setTextColor(getResources().getColor(R.color.txt_black_normal));
        radioButton2.setText("月");
        radioButton2.setTag("1");

        rg_year_month.addView(radioButton1);
        rg_year_month.addView(radioButton2);
        RadioButton childAt = (RadioButton) rg_year_month.getChildAt(0);
        childAt.setChecked(true);

        MapFragmentTitleView titleView = (MapFragmentTitleView) findViewById(R.id.view_title_map);
        titleView.setTitleText(R.string.second_project_all_info);
        titleView.setOperatorBtnGone();
        titleView.setQueryBtnGone();
        titleView.setBackBtnVisible();
        titleView.setOnBackListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WaterMeterAllInfoActivity.this.finish();
            }
        });
    }

    private ArrayList<String> getArrayListDate() {
        ArrayList<String> strList = new ArrayList<String>();
        String[] stringArray = getResources().getStringArray(R.array.project_water_meter_type);
        for (String string : stringArray) {
            strList.add(string);
        }
        return strList;
    }

    class MyTypeSpinnerListener implements CustomSpinner.OnSpinnerListener {

        @Override
        public void back(int position) {
            projectType = String.valueOf(position);
            requestProjectInfoCount();
        }
    }

    private class MyRadioGroupOnClickListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempButton = (RadioButton) findViewById(checkedId);
            String tag = (String) tempButton.getTag();
            isMonthChecked = tag.equals("1");
        }
    }

    private void requestProjectInfoCount() {
        String currentDate = DateUtil.getCurrentDate();
        if (selectTime == null) {
            selectTime = currentDate;
        }

        ProjectService.getInstance().statProStatusCountByTime(projectType, firstDate, lastDate);
    }

    public void onClickSelectDate(View view) {
        DatetimePickerDialog.EPickerType type = null;
        if (isMonthChecked) {
            type = DatetimePickerDialog.EPickerType.YEAR_MONTH;
        } else {
            type = DatetimePickerDialog.EPickerType.YEAR;
        }

        DatetimePickerDialog datetimePickerDialog = new DatetimePickerDialog(this, new DatetimePickerDialog.DatetimePickerCallback() {

            @Override
            public void OnOK(String input) {
                defaultTime = input;
                String date = DateUtil.getDate(input);
                selectTime = date;

                if (!isMonthChecked) {
                    firstDate = DateUtil.getYearFirstDay(date);
                    lastDate = DateUtil.getYearLastDay(date);
                    tv_selected_date.setText(firstDate.subSequence(0, 4) + "年");
                } else {
                    firstDate = DateUtil.getMonthFirstDay(date);
                    lastDate = DateUtil.getMonthLastDay(date);
                    tv_selected_date.setText(firstDate.subSequence(0, 7) + "月");
                }

                requestProjectInfoCount();
            }
        }, DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, type, defaultTime);
        datetimePickerDialog.show();
    }

    private void handleGetProjectCountResponse(ResponseEvent event) {
        countData = event.getData();

        sumaryPieDatas = new ArrayList<SumaryPieDataModel>();
        SumaryPieDataModel model1 = new SumaryPieDataModel();
        SumaryPieDataModel model2 = new SumaryPieDataModel();
        SumaryPieDataModel model3 = new SumaryPieDataModel();
        SumaryPieDataModel model4 = new SumaryPieDataModel();
        model1.setPieCategory(countData.getStatusCount().get(0).getStatus_alias());
        model1.setPieData(String.valueOf(countData.getStatusCount().get(0).getCount()));
        model2.setPieCategory(countData.getStatusCount().get(1).getStatus_alias());
        model2.setPieData(countData.getStatusCount().get(1).getCount());
        model3.setPieCategory(countData.getStatusCount().get(2).getStatus_alias());
        model3.setPieData(String.valueOf(countData.getStatusCount().get(2).getCount()));
        model4.setPieCategory(countData.getStatusCount().get(3).getStatus_alias());
        model4.setPieData(String.valueOf(countData.getStatusCount().get(3).getCount()));
        sumaryPieDatas.add(model1);
        sumaryPieDatas.add(model2);
        sumaryPieDatas.add(model3);
        sumaryPieDatas.add(model4);

        tv_project_sum.setText(countData.getTotalCount());

        initPieChart();
        initListView();
    }

    private void initPieChart() {
        for (int i = 0; i < sumaryPieDatas.size(); i++) {
            String length = sumaryPieDatas.get(i).getPieData();
            double newLength;
            if (TextUtils.isEmpty(length)) {
                newLength = 0;
            } else {
                newLength = Double.parseDouble(length);
            }
            sumLength += newLength;
        }
        PieData mPieData = getPieData(EcityConstants.PIE_RANGE);
        showChart(mPieView, mPieData, "");
    }

    private void initListView() {
        List<SumaryPieDataModel> list = new ArrayList<SumaryPieDataModel>();
        SumaryPieDataModel model1 = new SumaryPieDataModel();
        SumaryPieDataModel model2 = new SumaryPieDataModel();
        SumaryPieDataModel model3 = new SumaryPieDataModel();
        SumaryPieDataModel model4 = new SumaryPieDataModel();
        SumaryPieDataModel model5 = new SumaryPieDataModel();
        model1.setPieCategory(countData.getStatusCount().get(0).getStatus_alias());
        model1.setPieData(countData.getStatusCount().get(0).getCount());
        model2.setPieCategory(countData.getStatusCount().get(1).getStatus_alias());
        model2.setPieData(countData.getStatusCount().get(1).getCount());
        model3.setPieCategory(countData.getStatusCount().get(2).getStatus_alias());
        model3.setPieData(countData.getStatusCount().get(2).getCount());
        model4.setPieCategory(countData.getStatusCount().get(3).getStatus_alias());
        model4.setPieData(countData.getStatusCount().get(3).getCount());
        model5.setPieCategory(countData.getOverdueCount().getCount());
        model5.setPieData(countData.getOverdueCount().getCount());
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        list.add(model5);

        lv_project_all_info.setAdapter(new ProjectAnalysisAdapter(this, list));
    }

    private PieData getPieData(float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < sumaryPieDatas.size(); i++) {
            xValues.add(sumaryPieDatas.get(i).getPieCategory());
        }
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < sumaryPieDatas.size(); i++) {
            MathHelper mathHelper = new MathHelper();
            String length = sumaryPieDatas.get(i).getPieData();
            double newLength;
            if (TextUtils.isEmpty(length)) {
                newLength = 0;
            } else {
                newLength = Double.parseDouble(length);
            }
            double per = mathHelper.div(newLength, sumLength);
            float roundPer = (float) mathHelper.round(per * 100, 1);
            //            percents[i] = String.valueOf(newLength) + "?";
            yValues.add(new Entry(roundPer, i));
        }
        PieDataSet pieDataSet = new PieDataSet(yValues, EcityConstants.NULL_VALUE);
        pieDataSet.setSliceSpace(EcityConstants.SLICE_SPACE);
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px);
        PieData pieData = new PieData(xValues, pieDataSet);
        pieData.setDrawValues(true);
        return pieData;
    }

    private void showChart(PieChart pieChart, PieData pieData, String description) {
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleRadius(EcityConstants.ZERO);
        pieChart.setHoleRadius(EcityConstants.ZERO);
        pieChart.setDescription(description);
        //        pieChart.setNoDataTextDescription(EcityConstants.NO_DATA_DESCR
        //        IPTION);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawSliceText(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(EcityConstants.ROTATION_ANGLE);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER); //?????
        legend.setTextColor(ResourceUtil.getColorById(R.color.darkgray));
        legend.setTextSize(12);
        legend.setXEntrySpace(8f);
        legend.setYEntrySpace(8f);
        pieChart.animateXY(EcityConstants.ANIMATE_X, EcityConstants.ANIMATE_Y);
    }

    private void setOnClickListener() {
        tv_project_all_info_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date endDate = DateUtil.fromDate(lastDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                int year = calendar.get(Calendar.YEAR);
                int month = 0;
                if (isMonthChecked) {
                    month = calendar.get(Calendar.MONTH) + 1;
                }

                Intent intent = new Intent(WaterMeterAllInfoActivity.this, ProjectWaterMeterSummaryListActivity.class);
                intent.putExtra(ProjectSummaryListActivity.INTENT_KEY_TYPE, Integer.valueOf(projectType));
                intent.putExtra(ProjectSummaryListActivity.INTENT_KEY_YEAR, year);
                intent.putExtra(ProjectSummaryListActivity.INTENT_KEY_MONTH, month);
                intent.putExtra(ProjectSummaryListActivity.INTENT_KEY_START_TIME, firstDate);
                intent.putExtra(ProjectSummaryListActivity.INTENT_KEY_END_TIME, lastDate);
                startActivity(intent);
            }
        });

        lv_project_all_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WaterMeterAllInfoActivity.this, WaterMeterListActivity.class);
                intent.putExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_TYPE, projectType);
                intent.putExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_START_TIME, firstDate);
                intent.putExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_END_TIME, lastDate);
                intent.putExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_STATE, position);
                startActivity(intent);
            }
        });

    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_GET_WATERMETER_INFO:
                LoadingDialogUtil.dismiss();
                handleGetProjectCountResponse(event);
                break;
            default:
                break;
        }
    }
}
