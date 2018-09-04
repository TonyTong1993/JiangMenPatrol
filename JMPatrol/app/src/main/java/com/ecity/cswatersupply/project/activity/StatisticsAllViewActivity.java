package com.ecity.cswatersupply.project.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.adapter.ProjectFZRStastisticsAdapter;
import com.ecity.cswatersupply.project.network.response.ProjectFZRStastisticsModel;
import com.ecity.cswatersupply.project.network.response.ProjectFzrStastistisResponse;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.z3app.android.util.ScreenUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单(班长)汇总详情信息Activity
 *
 * @author
 */
public class StatisticsAllViewActivity extends BaseActivity {
    private RadioGroup rGroup;
    private Button btnSelectDateStart, btnSelectDateFinish;
    private RelativeLayout re_table;
    private boolean isYearType = true;
    private ListViewForScrollView lv_table;
    private CustomTitleView titleView;
    private Map<String, String> map;
    private String fzrType = "";
    private ArrayList<String> arrayListType;
    private WorkOrderPieStaticsData pieData = new WorkOrderPieStaticsData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_view);
        EventBusUtil.register(this);
        initView();
        initData();
        setOnClickListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    private void initView() {
        map = new HashMap<>();
        map.put(getString(R.string.project_stastistics1), "sjfzr");
        map.put(getString(R.string.project_stastistics2), "sgfzr");
        map.put(getString(R.string.project_stastistics3), "jsdw");

        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        titleView.setTitleText("统计总览");
        titleView.setRightActionBtnText("查询");


        rGroup = (RadioGroup) findViewById(R.id.rg_custom);
        btnSelectDateStart = (Button) findViewById(R.id.btn_select_date_start);
        btnSelectDateFinish = (Button) findViewById(R.id.btn_select_date_finish);
        setupRadioButtons(rGroup);

        lv_table = (ListViewForScrollView) findViewById(R.id.lv_summary_table);

        LinearLayout spinner_type = (LinearLayout) findViewById(R.id.spinner_type);
        TextView tv_type = (TextView) findViewById(R.id.tv_type);

        arrayListType = getArrayListType();
        new CustomSpinner(this, spinner_type, tv_type, new MyTypeSpinnerListener(), arrayListType, 0);
    }

    private ArrayList<String> getArrayListType() {
        ArrayList<String> strList = new ArrayList<String>();
        String[] stringArray = getResources().getStringArray(R.array.project_fzr_statistics);
        for (String string : stringArray) {
            strList.add(string);
        }
        return strList;
    }

    class MyTypeSpinnerListener implements CustomSpinner.OnSpinnerListener {

        @Override
        public void back(int position) {
            fzrType = map.get(arrayListType.get(position));
        }
    }


    private void initData() {

        setStaticsData(true, null, true);
        setStaticsData(false, null, false);
        refreshDatas("sjfzr");
    }

    private void setupRadioButtons(RadioGroup radioGroup) {
        RadioButton tempButton1 = new RadioButton(this);
        RadioButton tempButton2 = new RadioButton(this);
        setTempButtonProperty(ResourceUtil.getStringById(R.string.pie_data_source_year_type), tempButton1);
        setTempButtonProperty(ResourceUtil.getStringById(R.string.pie_data_source_month_type), tempButton2);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        radioGroup.addView(tempButton1);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        radioGroup.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layoutParams.setMargins((displayMetrics.widthPixels / 2) - (radioGroup.getMeasuredWidth()) - ScreenUtil.dipTopx(this, 8), 0, 0, 0);
        radioGroup.addView(tempButton2, layoutParams);
        tempButton1.setChecked(true);
    }

    private void setTempButtonProperty(String nameItem, RadioButton tempButton) {
        tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tempButton.setTextColor(getResources().getColor(R.color.txt_black_normal));
        tempButton.setText(nameItem);
    }

    private void setOnClickListener() {
        rGroup.setOnCheckedChangeListener(new MyRadioCheckListener());
        btnSelectDateStart.setOnClickListener(new MyDateClickListener(this, true));
        btnSelectDateFinish.setOnClickListener(new MyDateClickListener(this, false));

        titleView.setMessageDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDatas(fzrType);
            }
        });
    }

    private class MyRadioCheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton tempButton = (RadioButton) findViewById(checkedId);
            if (tempButton == null) {
                return;
            }
            isYearType = tempButton.getText().equals(ResourceUtil.getStringById(R.string.pie_data_source_year_type));
            setStaticsData(false, null, true);
            setStaticsData(false, null, false);
        }
    }

    private void refreshDatas(String type) {
        type = type.isEmpty() ? "sjfzr" : type;
        LoadingDialogUtil.show(this, R.string.pie_data_loading);
        ProjectService.getInstance().getProjectStatistics(type, pieData.getStrDate(), pieData.getEndDate());
    }

    private void setStaticsData(boolean isInit, String input, boolean isStart) {
        if (input == null) {
            input = DateUtil.getCurrentDate();
        }

        if (isInit) {
            btnSelectDateStart.setText(DateUtil.getYearDate(input));
            btnSelectDateFinish.setText(DateUtil.getYearDate(input));
            input = DateUtil.getCurrentDate();
        } else {
            if (isYearType) {
                if (isStart) {
                    btnSelectDateStart.setText(DateUtil.getYearDate(input));
                } else {
                    btnSelectDateFinish.setText(DateUtil.getYearDate(input));
                }
            } else {
                if (isStart) {
                    btnSelectDateStart.setText(DateUtil.getMonthYearDate(input));
                } else {
                    btnSelectDateFinish.setText(DateUtil.getMonthYearDate(input));
                }
            }
        }
        if (isStart) {
            pieData.setStrDate(getDateParamters(input, true));
        } else {
            pieData.setEndDate(getDateParamters(input, false));
        }
    }

    private String getDateParamters(String input, boolean isStart) {
        if (input == null) {
            input = DateUtil.getCurrentDate();
        }
        String lastDay = DateUtil.getDaysOfMonth(input);
        String[] dates = input.split("-");
        String year = dates[0];
        String month = dates[1];
        String monthStart = year + ResourceUtil.getStringById(R.string.middle_tag) + month + ResourceUtil.getStringById(R.string.date_month_start_str);
        String yearStart = year + ResourceUtil.getStringById(R.string.date_year_str_start_tail);
        String monthEnd = year + ResourceUtil.getStringById(R.string.middle_tag) + month + ResourceUtil.getStringById(R.string.middle_tag) + lastDay;
        String yearEnd = year + ResourceUtil.getStringById(R.string.date_year_str_end_tail);
        if (isYearType) {
            return isStart ? (yearStart) : (yearEnd);
        } else {
            return isStart ? (monthStart) : (monthEnd);
        }
    }

    private class MyDateClickListener implements View.OnClickListener {
        private DatetimePickerDialog timeDialog;
        private Activity context;
        private boolean isStart;

        public MyDateClickListener(Activity context, boolean isStart) {
            this.context = context;
            this.isStart = isStart;
        }

        @Override
        public void onClick(View v) {
            if (isYearType) {
                timeDialog = new DatetimePickerDialog(context, new MyDateTimeCallBack(isStart), DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, DatetimePickerDialog.EPickerType.YEAR, null);
            } else {
                timeDialog = new DatetimePickerDialog(context, new MyDateTimeCallBack(isStart), DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, DatetimePickerDialog.EPickerType.YEAR_MONTH, null);
            }

            WindowManager.LayoutParams attributes = timeDialog.getWindow().getAttributes();
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            attributes.width = (int) (metrics.widthPixels * 0.9);
            attributes.height = (int) (metrics.heightPixels * 0.6);
            attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            attributes.dimAmount = 0.5f;
            timeDialog.getWindow().setAttributes(attributes);
            timeDialog.show();
        }
    }

    private class MyDateTimeCallBack implements DatetimePickerDialog.DatetimePickerCallback {
        private boolean isStart;

        public MyDateTimeCallBack(boolean isStart) {
            this.isStart = isStart;
        }

        @Override
        public void OnOK(String input) {
            setStaticsData(false, input, isStart);
        }
    }

    private void handleGetSummaryDetailInfo(ResponseEvent event) {
        ProjectFzrStastistisResponse data = event.getData();
        List<ProjectFZRStastisticsModel> list = data.getFeatures();
        ProjectFZRStastisticsAdapter adapter = new ProjectFZRStastisticsAdapter(this, list);
        lv_table.setAdapter(adapter);
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_GET_STATISTICS_VIEW:
                LoadingDialogUtil.dismiss();
                handleGetSummaryDetailInfo(event);
                break;
            default:
                break;
        }
    }
}
