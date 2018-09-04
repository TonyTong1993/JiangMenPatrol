package com.ecity.cswatersupply.workorder.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderLeaderTableAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.ecity.cswatersupply.workorder.network.WorkorderLeaderModel;
import com.z3app.android.util.ScreenUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.List;

/**
 * 工单(班长)汇总详情信息Activity
 *
 * @author
 */
public class WorkLeaderDetailActivity extends BaseActivity {
    private RadioGroup rGroup;
    private Button btnSelectDate;
    private RelativeLayout re_table;
    private boolean isDayType = true;
    private ListViewForScrollView lv_table;
    private WorkOrderPieStaticsData pieData = new WorkOrderPieStaticsData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_leader_summary);
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
        CustomTitleView titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setTitleText(R.string.summary_over_work_leader_title);

        rGroup = (RadioGroup) findViewById(R.id.rg_custom);
        btnSelectDate = (Button) findViewById(R.id.btn_select_date);
        setupRadioButtons(rGroup);

        re_table = (RelativeLayout) findViewById(R.id.head);
        lv_table = (ListViewForScrollView) findViewById(R.id.lv_summary_table);
        setHeadStr();

        re_table.setFocusable(true);
        re_table.setClickable(true);
        re_table.setBackgroundColor(getResources().getColor(R.color.head_bg));
        re_table.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
        lv_table.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
    }

    private void setHeadStr() {
        TextView tv_head1 = (TextView) findViewById(R.id.text_view1);
        TextView tv_head2 = (TextView) findViewById(R.id.text_view2);
        TextView tv_head3 = (TextView) findViewById(R.id.text_view3);
        TextView tv_head4 = (TextView) findViewById(R.id.text_view4);
        TextView tv_head5 = (TextView) findViewById(R.id.text_view5);
        TextView tv_head6 = (TextView) findViewById(R.id.text_view6);
        TextView tv_head7 = (TextView) findViewById(R.id.text_view7);
        TextView tv_head8 = (TextView) findViewById(R.id.text_view8);
        TextView tv_head9 = (TextView) findViewById(R.id.text_view9);
        TextView tv_head10 = (TextView) findViewById(R.id.text_view10);
        tv_head1.setText(R.string.summary_fix_people);
        tv_head2.setText(R.string.summary_total_workorder);
        tv_head3.setText(R.string.summary_to_deal);
        tv_head4.setText(R.string.summary_dealing);
        tv_head5.setText(R.string.summary_finished);
        tv_head6.setText(R.string.summary_postpone);
        tv_head7.setText(R.string.summary_over_schedule);
        tv_head8.setText(R.string.summary_over_transfer_order);
        tv_head9.setText(R.string.summary_over_back_order);
        tv_head10.setText(R.string.summary_over_total_worktime);
    }

    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) re_table
                    .findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);
            return false;
        }
    }

    private void initData() {
        refreshDatas(true, null);
    }

    private void setupRadioButtons(RadioGroup radioGroup) {
        RadioButton tempButton1 = new RadioButton(this);
        RadioButton tempButton2 = new RadioButton(this);
        setTempButtonProperty(ResourceUtil.getStringById(R.string.pie_data_source_day_type), tempButton1);
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
        btnSelectDate.setOnClickListener(new MyDateClickListener(this));
    }

    private class MyRadioCheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton tempButton = (RadioButton) findViewById(checkedId);
            if (tempButton == null) {
                return;
            }
            isDayType = tempButton.getText().equals(ResourceUtil.getStringById(R.string.pie_data_source_day_type));
            refreshDatas(false, null);
        }
    }

    private void refreshDatas(boolean isInit, String input) {
        setStaticsData(isInit, input);
        LoadingDialogUtil.show(this, R.string.pie_data_loading);
        WorkOrderService.instance.getGroupWorkorderStatistics(pieData);
    }

    private void setStaticsData(boolean isInit, String input) {
        if (input == null) {
            input = DateUtil.getCurrentDate();
        }

        if (isInit) {
            btnSelectDate.setText(DateUtil.getCurrentDate());
            input = DateUtil.getCurrentDate();
        } else {
            if (isDayType) {
                btnSelectDate.setText(DateUtil.getDate(input));
            } else {
                btnSelectDate.setText(DateUtil.getMonthYearDate(input));
            }
        }
        pieData.setStrDate(getDateParamters(input, true)+" 00:00:00");
        pieData.setEndDate(getDateParamters(input, false)+" 23:59:59");
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
        String monthEnd = year + ResourceUtil.getStringById(R.string.middle_tag) + month + ResourceUtil.getStringById(R.string.middle_tag) + lastDay;
        if (isDayType) {
            return DateUtil.getDate(input);
        } else {
            return isStart ? (monthStart) : (monthEnd);
        }
    }

    private class MyDateClickListener implements View.OnClickListener {
        private DatetimePickerDialog timeDialog;
        private Activity context;

        public MyDateClickListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (isDayType) {
                timeDialog = new DatetimePickerDialog(context, new MyDateTimeCallBack(), DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, DatetimePickerDialog.EPickerType.YEAR_MONTH_DATE, null);
            } else {
                timeDialog = new DatetimePickerDialog(context, new MyDateTimeCallBack(), DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, DatetimePickerDialog.EPickerType.YEAR_MONTH, null);
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

        public MyDateTimeCallBack() {
        }

        @Override
        public void OnOK(String input) {
            refreshDatas(false, input);
        }
    }

    private void handleGetSummaryDetailInfo(ResponseEvent event) {
        List<WorkorderLeaderModel> data = event.getData();
        WorkOrderLeaderTableAdapter adapter = new WorkOrderLeaderTableAdapter(this, data, re_table);
        lv_table.setAdapter(adapter);
    }


    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_GET_BAR_DATA_STATIC_Leader:
                LoadingDialogUtil.dismiss();
                handleGetSummaryDetailInfo(event);
                break;
            default:
                break;
        }
    }
}
