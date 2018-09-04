package com.ecity.cswatersupply.workorder.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.chart.charts.BarChart;
import com.ecity.chart.charts.PieChart;
import com.ecity.chart.components.Legend;
import com.ecity.chart.components.Legend.LegendPosition;
import com.ecity.chart.components.XAxis;
import com.ecity.chart.components.XAxis.XAxisPosition;
import com.ecity.chart.components.YAxis;
import com.ecity.chart.data.BarData;
import com.ecity.chart.data.BarDataSet;
import com.ecity.chart.data.BarEntry;
import com.ecity.chart.data.Entry;
import com.ecity.chart.data.PieData;
import com.ecity.chart.data.PieDataSet;
import com.ecity.chart.utils.ColorTemplate;
import com.ecity.chart.utils.MathHelper;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.WorkOrderSummaryTableAdapter;
import com.ecity.cswatersupply.emergency.utils.EcityConstants;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.project.activity.SelectGroupAndUserActivity;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.SelectGroupTreeActivity;
import com.ecity.cswatersupply.ui.activities.WorkOrderSummaryDetailActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.DatetimePickerCallback;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.EPickerDialogStyle;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.EPickerType;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.PercentFormatter;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderSummaryListAdapter;
import com.ecity.cswatersupply.workorder.model.PopupSummaryModel;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryBar;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryBarBean;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryPie;
import com.ecity.cswatersupply.workorder.model.WorkOrderSummaryPieBean;
import com.ecity.cswatersupply.workorder.view.PopupSummaryView.OnMenuClickListener;
import com.z3app.android.util.ScreenUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 工单汇总：汇总统计&&明细统计
 * @author JiangQiWei
 *
 */
public class WorkOrderSummaryActivity extends BaseActivity {
    private TextView mOverSchedule1, mOverSchedule2;
    private TextView mPostponeSchedule1, mPostponeSchedule2;
    private TextView mTvTotal;
    private PieChart mPieView;
    private BarChart mBarChartView;
    private Button btnSelectDate;
    private Button selectPerson;
    private RadioGroup rGroup;
    private PopupSummaryView popupView;
    private CustomTitleView titleView;
    private TextView tvAction;
    private LinearLayout llPieChartView, llBarChartView;
    private RelativeLayout rlSummaryAmount;
    private ListViewForScrollView mSummaryListView;
    private ListViewForScrollView lvSummaryTable;
    private ScrollView scrollView;
    private int[] colors;
    private boolean isYearType;
    private static String SUMMARY_CATEGORY = "汇总";
    public static final String WORKORDER_SUMMARY_DETAIL_TITLE = "WORKORDER_SUMMARY_DETAIL_TITLE";
    private static String TIME_PRE = " 00:00:00";
    private static String TIME_SUF = " 23:59:59";
    private OrganisationSelection topOrganisation;
    private WorkOrderSummaryPie sumaryPieDatas;
    private List<WorkOrderSummaryBar> sumaryBarDatas;
    private WorkOrderPieStaticsData pieData = new WorkOrderPieStaticsData();
    private List<OrganisationSelection> selectedOrganisations = new ArrayList<OrganisationSelection>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_summary);
        EventBusUtil.register(this);
        initData();
        initUI();
        bindListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                setSelectGroupValue(data);
                refreshDatas(false, null);
                break;
            case RequestCode.SELECT_HANDLE_TYPE:
                if (resultCode == RequestCode.SELECT_DISMISS_TYPE) {
                    refreshDatas(false, null);
                } else {
                    TreeNode group = (TreeNode) data.getSerializableExtra(SelectGroupTreeActivity.TREE_DATA_SOURCE);
                    pieData.setGroup(group.getGroup());
                    pieData.setGroupTrueName(group.getName());
                    selectPerson.setText(pieData.getGroupTrueName());
                    refreshDatas(false, null);
                }
                break;
        }
    }

    private void showPopupMenu(View view) {
        int width = (int) getResources().getDimension(R.dimen.workorder_popmenu_w_s);
        int xPos = ScreenUtil.getScreenWidth(this) - width;
        popupView.showAdDropDown(titleView.imgv_tag, xPos, (int) getResources().getDimension(R.dimen.activity_title_height));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    private void showChartPartView() {
        showChartView();
        showChartListView();
    }

    private void showChartView() {
        setPieData(getPieData(EcityConstants.PIE_RANGE));
        setLegend();
        setOtherPieValue();

    }

    private void showChartListView() {
        final WorkOrderSummaryListAdapter pieAdapter = new WorkOrderSummaryListAdapter(this, sumaryPieDatas);
        mSummaryListView.setAdapter(pieAdapter);
        mSummaryListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pieData.setCategory(pieAdapter.getList().get(position).getPieCategory());
                pieData.setYearType(isYearType);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.WORKORDER_SUMMARY_DETAIL, (Serializable) pieData);
                UIHelper.startActivityWithExtra(CustomWorkOrderActivity.class, bundle);
            }
        });
    }

    /**
     * 获取饼状图所需的内容项（未处理／已处理／处理中）
     * @param range
     * @return
     */
    private PieData getPieData(float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        String overScedule = getResources().getString(R.string.workorder_over_schedule);
        String postponeScedule = getResources().getString(R.string.workorder_postpone_schedule);
        String sumLenth = sumaryPieDatas.getTotal();
        for (int i = 0; i < sumaryPieDatas.getPieBean().size(); i++) {
            String category = sumaryPieDatas.getPieBean().get(i).getPieCategory();
            String length = sumaryPieDatas.getPieBean().get(i).getPieData();
            MathHelper mathHelper = new MathHelper();
            double per = mathHelper.div(Double.parseDouble(length), Double.parseDouble(sumLenth));
            float roundPer = (float) mathHelper.round(per * 100, 1);
            if ((!category.equalsIgnoreCase(overScedule)) && (!category.equalsIgnoreCase(postponeScedule))) {
                xValues.add(category);
                yValues.add(new Entry(roundPer, i));
            }
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

    private void initViews() {
        scrollView = (ScrollView) findViewById(R.id.sv_summary);
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        titleView.tv_title.setText(getString(R.string.workorder_pie_data_title));
        tvAction = (TextView) titleView.findViewById(R.id.tv_action);
        selectPerson = (Button) findViewById(R.id.btn_select_department);
        btnSelectDate = (Button) findViewById(R.id.btn_select_date);
        rGroup = (RadioGroup) findViewById(R.id.rg_custom);
        mSummaryListView = (ListViewForScrollView) findViewById(R.id.lv_workorder_summary_data);
        mTvTotal = (TextView) findViewById(R.id.tv_item_summary_amount);
        rlSummaryAmount = (RelativeLayout) findViewById(R.id.rl_item_summary_amount);
        setupRadioButtons(rGroup);
        mPieView = (PieChart) findViewById(R.id.view_workorder_pie_chart);
        llPieChartView = (LinearLayout) findViewById(R.id.ll_pie_chart_views);
        llBarChartView = (LinearLayout) findViewById(R.id.ll_bar_chart_views);
        lvSummaryTable = (ListViewForScrollView) findViewById(R.id.lv_summary_table);
        mBarChartView = (BarChart) findViewById(R.id.view_bar_chart);
        mOverSchedule1 = (TextView) findViewById(R.id.tv_item_pie_over_schedule1);
        mOverSchedule2 = (TextView) findViewById(R.id.tv_item_pie_over_schedule2);
        mPostponeSchedule1 = (TextView) findViewById(R.id.tv_item_pie_postpone_schedule1);
        mPostponeSchedule2 = (TextView) findViewById(R.id.tv_item_pie_postpone_schedule2);
    }

    private void setPieChart() {
        mPieView.setHoleColorTransparent(true);
        mPieView.setTransparentCircleRadius(EcityConstants.ZERO);
        mPieView.setHoleRadius(EcityConstants.ZERO);
        mPieView.setNoDataTextDescription(EcityConstants.NO_DATA_DESCRIPTION);
        mPieView.setDrawCenterText(false);
        mPieView.setDrawSliceText(false);
        mPieView.setDrawHoleEnabled(true);
        mPieView.setRotationEnabled(true);
        mPieView.setUsePercentValues(true);
        mPieView.setDescription("");
    }

    private void setPieData(PieData pieData) {
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        mPieView.setData(pieData);
    }

    private void setLegend() {
        Legend mLegend = mPieView.getLegend();
        mLegend.setPosition(LegendPosition.RIGHT_OF_CHART);
        mLegend.setTextColor(ResourceUtil.getColorById(R.color.darkgray));
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        mPieView.animateXY(EcityConstants.ANIMATE_X, EcityConstants.ANIMATE_Y);
        colors = new int[sumaryPieDatas.getPieBean().size()];
        for (int i = 0; i < sumaryPieDatas.getPieBean().size(); i++) {
            if (sumaryPieDatas.getPieBean().size() < ColorTemplate.VORDIPLOM_COLORS.length) {
                colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
            } else {
                for (int j = 0; j < colors.length; j++) {
                    colors[j] = ColorTemplate.VORDIPLOM_COLORS[j % ColorTemplate.VORDIPLOM_COLORS.length];
                }
            }
        }
    }

    private void setOtherPieValue() {
        String overScedule = getResources().getString(R.string.workorder_over_schedule);
        String postponeScedule = getResources().getString(R.string.workorder_postpone_schedule);
        for (int i = 0; i < sumaryPieDatas.getPieBean().size(); i++) {
            WorkOrderSummaryPieBean pieData = sumaryPieDatas.getPieBean().get(i);
            String category = sumaryPieDatas.getPieBean().get(i).getPieCategory();
            if (category.equalsIgnoreCase(overScedule)) {
                setScheduleText(mOverSchedule1, mOverSchedule2, pieData, category);
            } else if (category.equalsIgnoreCase(postponeScedule)) {
                setScheduleText(mPostponeSchedule1, mPostponeSchedule2, pieData, category);
            }
        }
        mTvTotal.setText(sumaryPieDatas.getTotal());
    }

    private void setScheduleText(TextView textView, TextView textView2, WorkOrderSummaryPieBean pieData, String category) {
        String amount = pieData.getPieData();
        float percentValue = Float.valueOf(amount) / Float.valueOf(sumaryPieDatas.getTotal());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String p = decimalFormat.format(((float) ((int) (percentValue * 10000)) / 10000) * 100);
        String percentStr = p + "%";
        String begin = isYearType ? getString(R.string.pie_data_format_year) : getString(R.string.pie_data_format_month);
        String text = begin + category + getString(R.string.pie_data_format_amount) + amount + getString(R.string.pie_data_format2);

        textView.setText(text);
        textView2.setText(category + getString(R.string.pie_data_format3) + percentStr);
    }

    private void initData() {
        getGroupTree();
    }

    private void getGroupTree() {
        LoadingDialogUtil.show(this, R.string.pie_data_loading);
        UserService.getInstance().getGroupsTrees(null);
    }

    private void initUI() {
        initViews();
        initPopView();
        setPieChart();
    }

    private void showBarPartView() {
        showBarView();
        showBarTable();
        scrollView.scrollTo(10, 10);
    }

    private void showBarTable() {
        WorkOrderSummaryTableAdapter tableAdapter = new WorkOrderSummaryTableAdapter(WorkOrderSummaryActivity.this, sumaryBarDatas);
        lvSummaryTable.setAdapter(tableAdapter);
    }

    private void showBarView() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        List<String> xValues = new ArrayList<String>();
        if (!ListUtil.isEmpty(sumaryBarDatas)) {
            for (int i = 0; i < sumaryBarDatas.size(); i++) {
                WorkOrderSummaryBar barData = sumaryBarDatas.get(i);
                xValues.add(barData.getPersonName());
            }
        }
        colors = new int[5];
        colors = setColor(colors);
        for (int j = 0; j < sumaryBarDatas.size(); j++) {
            setSummaryBarDataSet(getString(R.string.summary_to_deal), yVals1, j, getBean(j).getToDealAmount(), dataSets, 0);
        }
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        for (int j = 0; j < sumaryBarDatas.size(); j++) {
            setSummaryBarDataSet(getString(R.string.summary_dealing), yVals2, j, getBean(j).getDealingAmount(), dataSets, 1);
        }
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        for (int j = 0; j < sumaryBarDatas.size(); j++) {
            setSummaryBarDataSet(getString(R.string.summary_finished), yVals3, j, getBean(j).getFinishedAmount(), dataSets, 2);
        }
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
        for (int j = 0; j < sumaryBarDatas.size(); j++) {
            setSummaryBarDataSet(getString(R.string.summary_postpone), yVals4, j, getBean(j).getPostponeAmount(), dataSets, 3);
        }
        ArrayList<BarEntry> yVals5 = new ArrayList<BarEntry>();
        for (int j = 0; j < sumaryBarDatas.size(); j++) {
            setSummaryBarDataSet(getString(R.string.summary_over_schedule), yVals5, j, getBean(j).getOvertimeAmount(), dataSets, 4);
        }
        BarData data = new BarData(xValues, dataSets);
        initBarView(data);
    }

    private void setSummaryBarDataSet(String categoryName, ArrayList<BarEntry> yVals, int index, String yValue, ArrayList<BarDataSet> dataSets, int colorIndex) {
        yVals.add(new BarEntry(translateYvalue(yValue), index));
        BarDataSet set = new BarDataSet(yVals, categoryName);
        if (index == sumaryBarDatas.size() - 1) {
            set.setColor(colors[colorIndex]);
            set.setBarSpacePercent(50f);
            dataSets.add(set);
        }
    }

    private WorkOrderSummaryBarBean getBean(int index) {
        return sumaryBarDatas.get(index).getBarDatas();
    }

    public int[] setColor(int[] colors) {
        for (int j = 0; j < colors.length; j++) {
            colors[j] = ColorTemplate.VORDIPLOM_COLORS[j % ColorTemplate.VORDIPLOM_COLORS.length];
        }
        return colors;
    }

    public static float translateYvalue(String yvalue) {
        float y = 0;
        if (StringUtil.isEmpty(yvalue)) {
            y = 0;
        } else {
            y = Float.parseFloat(yvalue);
        }
        return y;
    }

    public void initBarView(BarData data) {
        XAxis xAxis = mBarChartView.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        mBarChartView.getAxisRight().setEnabled(false);
        YAxis leftAxis = mBarChartView.getAxisLeft();
        leftAxis.setSpaceTop(30f);
        mBarChartView.setPinchZoom(false);
        mBarChartView.setDrawBarShadow(false);
        mBarChartView.setDrawGridBackground(false);
        mBarChartView.setNoDataTextDescription(EcityConstants.NO_DATA_DESCRIPTION);
        mBarChartView.setDrawGridBackground(false);
        mBarChartView.setScaleYEnabled(false);
        mBarChartView.setDescription(EcityConstants.NULL_VALUE);
        data.setGroupSpace(80f);
        mBarChartView.setData(data);
        mBarChartView.setVisibleXRangeMaximum(15);
        mBarChartView.getLegend().setPosition(LegendPosition.BELOW_CHART_CENTER);
        mBarChartView.getLegend().setFormToTextSpace(10);
        mBarChartView.invalidate();
    }

    private void initPopView() {
        Drawable nav_up = getResources().getDrawable(R.drawable.arrow_down_white);
        nav_up.setBounds(0, 3, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tvAction.setCompoundDrawables(null, null, nav_up, null);
        tvAction.setCompoundDrawablePadding(5);
        popupView = new PopupSummaryView(this);
        int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_popmenu_w);
        popupView.initPopup(tvAction, width, LayoutParams.WRAP_CONTENT);
        popupView.setMenu(R.array.workorder_pop_menu_summary);
        tvAction.setText(getString(R.string.menu_summary_static));
        llBarChartView.setVisibility(View.GONE);
        llPieChartView.setVisibility(View.VISIBLE);
        tvAction.setOnClickListener(new OnActionBtnClickListener());
        popupView.setOnActionItemClickListener(new OnMenuClickListener() {

            @Override
            public void onMenuItemClick(PopupSummaryModel menu, int pos) {
                tvAction.setText(menu.getName());
                if (menu.getName().equals(getString(R.string.menu_summary_details))) {
                    llBarChartView.setVisibility(View.VISIBLE);
                    llPieChartView.setVisibility(View.GONE);
                } else if (menu.getName().equals(getString(R.string.menu_summary_static))) {
                    llBarChartView.setVisibility(View.GONE);
                    llPieChartView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void bindListener() {
        selectPerson.setOnClickListener(new SelectGroupBtnClickListener());
        rGroup.setOnCheckedChangeListener(new MyRadioGroupOnClickListener());
        btnSelectDate.setOnClickListener(new MyBtnSelectDateOnClickListener(WorkOrderSummaryActivity.this));
        rlSummaryAmount.setOnClickListener(new TotalDetailClickListener());
    }

    private class MyBtnSelectDateOnClickListener implements OnClickListener {
        private DatetimePickerDialog timeDialog;
        private Activity context;

        public MyBtnSelectDateOnClickListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (isYearType) {
                timeDialog = new DatetimePickerDialog(context, new MyDateTimeCallBack(), EPickerDialogStyle.OK_CANCEL, EPickerType.YEAR, null);
            } else {
                timeDialog = new DatetimePickerDialog(context, new MyDateTimeCallBack(), EPickerDialogStyle.OK_CANCEL, EPickerType.YEAR_MONTH, null);
            }

            LayoutParams attributes = timeDialog.getWindow().getAttributes();
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            attributes.width = (int) (metrics.widthPixels * 0.9);
            attributes.height = (int) (metrics.heightPixels * 0.6);
            attributes.flags = LayoutParams.FLAG_DIM_BEHIND;
            attributes.dimAmount = 0.5f;
            timeDialog.getWindow().setAttributes(attributes);
            timeDialog.show();
        }
    }

    private class MyRadioGroupOnClickListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempButton = (RadioButton) findViewById(checkedId);
            if (tempButton == null) {
                return;
            }
            if (tempButton.getText().equals(ResourceUtil.getStringById(R.string.pie_data_source_month_type))) {
                isYearType = false;
            } else {
                isYearType = true;
            }
            refreshDatas(false, null);
        }
    }

    private class SelectGroupBtnClickListener implements OnClickListener {
        public SelectGroupBtnClickListener() {
        }

        @Override
        public void onClick(View v) {
            if (topOrganisation == null) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(SelectGroupTreeActivity.TREE_DATA_SOURCE, topOrganisation);
            Intent intent = new Intent(WorkOrderSummaryActivity.this, SelectGroupTreeActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCode.SELECT_HANDLE_TYPE);
        }
    }

    private class OnActionBtnClickListener implements OnClickListener {
        public OnActionBtnClickListener() {
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
    }

    private class TotalDetailClickListener implements OnClickListener {
        public TotalDetailClickListener() {
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            pieData.setCategory(SUMMARY_CATEGORY);
            bundle.putSerializable(Constants.WORKORDER_SUMMARY_DETAIL, (Serializable) pieData);
            UIHelper.startActivityWithExtra(WorkOrderSummaryDetailActivity.class, bundle);
        }
    }

    private class MyDateTimeCallBack implements DatetimePickerCallback {

        public MyDateTimeCallBack() {
        }

        @Override
        public void OnOK(String input) {
            String currentDate;
            String formatDate;
            if (isYearType) {
                currentDate = DateUtil.getYearDate();
                formatDate = DateUtil.getYearDate(input);
            } else {
                currentDate = DateUtil.getMonthDate();
                formatDate = DateUtil.getMonthYearDate(input);
            }
            if (currentDate.compareTo(formatDate) < 0) {
                LoadingDialogUtil.dismiss();
                showAlertDialog(getString(R.string.summary_date_invalid_title), getString(R.string.summary_date_invalid_msg));
            } else {
                refreshDatas(false, input);
            }

        }
    }

    @SuppressWarnings("unchecked")
    private void setSelectGroupValue(Intent data) {
        Bundle bundle = data.getExtras();
        selectedOrganisations = (List<OrganisationSelection>) bundle.getSerializable(SelectGroupAndUserActivity.INTENT_KEY_SELECTED_ORGANISATIONS);
        if (ListUtil.isEmpty(selectedOrganisations)) {
            selectPerson.setText("");
            pieData.setGroup("1-1");
            pieData.setGroupTrueName(getString(R.string.summary_member_default));
        } else {
            pieData.setGroup(selectedOrganisations.get(0).getCode());
            pieData.setGroupTrueName(selectedOrganisations.get(0).getName());
            selectPerson.setText(pieData.getGroupTrueName());
        }
    }

    private void setStaticsData(boolean isInit, String input) {
        if (input == null) {
            input = DateUtil.getCurrentDate();
        }

        if (isInit) {
            String groupCode = findGroupCode(topOrganisation, getString(R.string.summary_member_default));
            pieData.setGroup(groupCode);
            pieData.setGroupTrueName(getString(R.string.summary_member_default));
            btnSelectDate.setText(DateUtil.getMonthDate());
            input = DateUtil.getCurrentDate();
            selectPerson.setText(getString(R.string.summary_member_default));

        } else {
            if (isYearType) {
                btnSelectDate.setText(DateUtil.getYearDate(input));
            } else {
                btnSelectDate.setText(DateUtil.getMonthYearDate(input));
            }
        }
        pieData.setYearType(isYearType);
        pieData.setStrDate(getDateParamters(input, true));
        pieData.setEndDate(getDateParamters(input, false));
    }

    private String findGroupCode(OrganisationSelection parent, String targetGroupName) {
        String code = "";

        if (targetGroupName.equals(parent.getName())) {
            return parent.getCode();
        }

        if (!ListUtil.isEmpty(topOrganisation.getChildren())) {
            for (OrganisationSelection org : topOrganisation.getChildren()) {
                return findGroupCode(org, targetGroupName);
            }
        }

        return code;
    }

    private void setTempButtonProperty(String nameItem, RadioButton tempButton) {
        tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tempButton.setTextColor(getResources().getColor(R.color.txt_black_normal));
        tempButton.setText(nameItem);
    }

    private void setupRadioButtons(RadioGroup radioGroup) {
        RadioButton tempButton1 = new RadioButton(this);
        RadioButton tempButton2 = new RadioButton(this);
        setTempButtonProperty(ResourceUtil.getStringById(R.string.pie_data_source_month_type), tempButton1);
        setTempButtonProperty(ResourceUtil.getStringById(R.string.pie_data_source_year_type), tempButton2);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        radioGroup.addView(tempButton1);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        radioGroup.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layoutParams.setMargins((displayMetrics.widthPixels / 2) - (radioGroup.getMeasuredWidth()) - ScreenUtil.dipTopx(this, 8), 0, 0, 0);
        radioGroup.addView(tempButton2, layoutParams);
        tempButton1.setChecked(true);
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
            return isStart ? (yearStart + TIME_PRE) : (yearEnd + TIME_SUF);
        } else {
            return isStart ? (monthStart + TIME_PRE) : (monthEnd + TIME_SUF);
        }
    }

    private void refreshDatas(boolean isInit, String input) {
        setStaticsData(isInit, input);
        LoadingDialogUtil.show(this, R.string.pie_data_loading);
        WorkOrderService.instance.getWorkOrderSummaryDataStatics(pieData, true);
    }

    public void onEventMainThread(ResponseEvent event) {
        switch (event.getId()) {
            case ResponseEventStatus.USER_GET_PATROL_TREE:
                handleGetPatrolGroup(event);
                break;
            case ResponseEventStatus.WORKORDER_GET_PIE_DATA_STATIC:
                handleGetPieDataStatics(event);
                break;
            case ResponseEventStatus.WORKORDER_GET_BAR_DATA_STATIC:
                handleGetBarDataStatics(event);
                break;
            default:
                break;
        }
    }

    private void handleGetPatrolGroup(ResponseEvent event) {
        topOrganisation = event.getData();
        refreshDatas(true, null);
    }

    private void handleGetPieDataStatics(ResponseEvent event) {
        sumaryPieDatas = event.getData();
        if (sumaryPieDatas.getTotal().equals("0")) {
            LoadingDialogUtil.dismiss();
            showAlertDialog(getString(R.string.summary_data_blank_title), getString(R.string.summary_data_blank_msg));
            return;
        }
        showChartPartView();
        WorkOrderService.instance.getWorkOrderSummaryDataStatics(pieData, false);
    }

    private void handleGetBarDataStatics(ResponseEvent event) {
        sumaryBarDatas = event.getData();
        showBarPartView();
        LoadingDialogUtil.dismiss();
    }

    private void showAlertDialog(String title, String msg) {
        AlertView dialog = new AlertView(this, title, msg, new OnAlertViewListener() {
            @Override
            public void back(boolean result) {
                if (result) {
                }
            }
        }, AlertView.AlertStyle.OK);
        dialog.show();
    }
}
