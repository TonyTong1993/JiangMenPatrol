package com.ecity.cswatersupply.emergency.utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.ecity.chart.charts.BarChart;
import com.ecity.chart.components.XAxis;
import com.ecity.chart.components.XAxis.XAxisPosition;
import com.ecity.chart.components.YAxis;
import com.ecity.chart.data.BarData;
import com.ecity.chart.data.BarDataSet;
import com.ecity.chart.data.BarEntry;
import com.ecity.chart.utils.ColorTemplate;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.emergency.model.BarCharEntry;

public class BarCharUtils {
    private final static Typeface MTF = Typeface.createFromAsset(HostApplication.getApplication().getApplicationContext().getAssets(), EcityConstants.MTF_PATH);
    private static BarCharUtils instance;
    static {
        instance = new BarCharUtils();
    }

    private BarCharUtils() {

    }

    public static BarCharUtils getInstance() {
        return instance;
    }

    /**
     * 通用的初始化柱图
     */
    public void initBarChart(BarData data, BarChart barChart) {
        // apply styling
        data.setValueTypeface(MTF);
        data.setValueTextColor(Color.BLACK);
        barChart.setDescription(EcityConstants.NULL_VALUE);
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription(EcityConstants.NO_DATA_DESCRIPTION);
        barChart.setDrawGridBackground(false);
        barChart.setScaleYEnabled(false);// 设置不允许Y轴收缩

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(MTF);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(MTF);
        leftAxis.setLabelCount(EcityConstants.LABEL_COUNT, false);
        leftAxis.setSpaceTop(EcityConstants.SPACE_TOP);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTypeface(MTF);
        rightAxis.setLabelCount(EcityConstants.LABEL_COUNT, false);
        rightAxis.setSpaceTop(EcityConstants.SPACE_TOP);
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        // set data
        for (int i = 0; i < data.getDataSets().size(); i++) {
            data.getDataSets().get(i).setBarSpacePercent(70f);
        }
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(5);
        // 设置图例不可见
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
    }

    /**
     * 得到Barchart需要的数据类型BarData
     */
    public BarData generateCalData(List<BarCharEntry> entryList) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 0; i < entryList.size(); i++) {
            String value = entryList.get(i).getValue();
            float newValue;
            if (TextUtils.isEmpty(value)) {
                newValue = 0;
            } else {
                newValue = Float.parseFloat(value);
            }
            entries.add(new BarEntry(newValue, i));
        }

        BarDataSet d = new BarDataSet(entries, EcityConstants.CAL_STRING);
        d.setBarSpacePercent(EcityConstants.BAR_SPACE_PERCENT);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setBarShadowColor(EcityConstants.BAR_SHADOW_COLOR);

        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        sets.add(d);

        BarData cd = new BarData(getCalXlables(entryList), sets);
        return cd;
    }

    /**
     * 得到X轴标签
     */
    public ArrayList<String> getCalXlables(List<BarCharEntry> entryList) {

        ArrayList<String> xLables = new ArrayList<String>();
        for (int i = 0; i < entryList.size(); i++) {
            xLables.add(entryList.get(i).getStructure());
        }
        return xLables;
    }
}
