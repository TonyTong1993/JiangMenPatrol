package com.ecity.cswatersupply.adapter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.SummaryDetailModel;
import com.ecity.cswatersupply.ui.activities.WorkOrderSummaryDetailActivity;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.ecity.cswatersupply.workorder.view.CustomWorkOrderActivity;

public class WorkOrderSummaryDetailAdapter extends ArrayListAdapter<SummaryDetailModel> {
    private LayoutInflater mInflater = null;
    private WorkOrderSummaryDetailActivity mActivity = null;
    public static final String TIELE_FORMAT_SUMMARY_DETAIL = "TIELE_FORMAT_SUMMARY_DETAIL";

    public WorkOrderSummaryDetailAdapter(WorkOrderSummaryDetailActivity context) {
        super(context);
        this.mActivity = context;
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_summary_details, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }
        SummaryDetailModel model = mList.get(position);
        String finishedText = mActivity.getString(R.string.summary_finished);
        String dealingText = mActivity.getString(R.string.workorder_dealing);
        String postponeText = mActivity.getString(R.string.summary_over_schedule);

        viewHolder.summary_detail_str_date.setText(model.getDate());
        viewHolder.tv_workorder_amount.setText(model.getTotal());
        viewHolder.tv_workorder_finished.setText(finishedText + "(" + model.getFinishedAmount() + ")");
        viewHolder.tv_workorder_dealing.setText(dealingText + "(" + model.getDealingAmount() + ")");
        viewHolder.tv_workorder_postpone.setText(postponeText + "(" + model.getOverSceduleAmount() + ")");
        viewHolder.tv_workorder_amount.setOnClickListener(new AmountClickListener(position, mActivity.getString(R.string.menu_summary_details_category), model.getTotal()));
        viewHolder.tv_workorder_finished.setOnClickListener(new AmountClickListener(position, finishedText, model.getFinishedAmount()));
        viewHolder.tv_workorder_dealing.setOnClickListener(new AmountClickListener(position, dealingText, model.getDealingAmount()));
        viewHolder.tv_workorder_postpone.setOnClickListener(new AmountClickListener(position, postponeText, model.getOverSceduleAmount()));
        return convertView;
    }

    private class AmountClickListener implements OnClickListener {
        private int position;
        private String categoryName;
        private String amount;

        public AmountClickListener(int position, String categoryName, String amount) {
            this.position = position;
            this.categoryName = categoryName;
            this.amount = amount;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            WorkOrderPieStaticsData newPieData = new WorkOrderPieStaticsData();
            newPieData.setCategory(categoryName);
            newPieData.setYearType(mActivity.getPieData().isYearType());
            newPieData.setGroup(mActivity.getPieData().getGroup());
            newPieData.setStrDate(getFormatDate(mActivity.getDetailDatas().get(position).getDateStr(), true, position));
            newPieData.setEndDate(getFormatDate(mActivity.getDetailDatas().get(position).getDateStr(), false, position));
            newPieData.setGroupTrueName(mActivity.getPieData().getGroupTrueName());
            bundle.putSerializable(Constants.WORKORDER_SUMMARY_DETAIL, newPieData);
            bundle.putString(TIELE_FORMAT_SUMMARY_DETAIL, getFormatTitle(newPieData, amount));
            UIHelper.startActivityWithExtra(CustomWorkOrderActivity.class, bundle);
        }
    }

    private String getFormatTitle(WorkOrderPieStaticsData newPieData, String amount) {
        String title;
        amount = "(" + amount + ")";
        if (newPieData.isYearType()) {
            title = newPieData.getGroupTrueName() + DateUtil.getMonthYearDate(newPieData.getStrDate()) + newPieData.getCategory() + amount;
        } else {
            title = newPieData.getGroupTrueName() + DateUtil.getDate(newPieData.getStrDate()) + newPieData.getCategory() + amount;
        }

        return title;

    }

    private String getFormatDate(String input, boolean isStart, int position) {
        if (input == null) {
            input = DateUtil.getCurrentDate();
        } else {
            boolean convertSuccess = true;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                format.setLenient(false);
                format.parse(input);
            } catch (ParseException e) {
                convertSuccess = false;
            }

            if(!convertSuccess){
                String[] dates = input.split("-");
                String year = dates[0];
                String month = dates[1];
                input = year + "-" + month + "-01";
            }
        }

        String day = "";
        String lastDay = DateUtil.getDaysOfMonth(input);
        String[] dates = input.split("-");
        String year = dates[0];
        String month = dates[1];
        String dayStart, monthStart, dayEnd, monthEnd;
        if (mActivity.getPieData().isYearType()) {//按所点击的月查询
            month = getList().get(position).getDate();
            monthStart = year + "-" + month + mActivity.getString(R.string.date_month_start_str);
            monthEnd = year + "-" + month + "-" + lastDay;
            return isStart ? (monthStart) : (monthEnd);
        } else {//按所点击的天查询
            day = getList().get(position).getDate();
            dayStart = year + "-" + month + "-" + day;
            DecimalFormat df = new DecimalFormat("00");
            String lateDay = df.format(Integer.valueOf(day) + 1);
            int result = lateDay.compareTo(lastDay);
            if (result > 0) {//大于当月天数
                month = df.format(Integer.valueOf(month) + 1);
                dayEnd = year + "-" + month + "-" + mActivity.getString(R.string.date_month_start_str);
            } else {
                dayEnd = year + "-" + month + "-" + lateDay;
            }
            return isStart ? (dayStart) : (dayEnd);
        }
    }

    public static class ViewHolder {
        public TextView summary_detail_str_date, tv_workorder_amount, tv_workorder_finished, tv_workorder_dealing, tv_workorder_postpone;

        public ViewHolder(View v) {
            summary_detail_str_date = (TextView) v.findViewById(R.id.summary_detail_str_date);
            tv_workorder_amount = (TextView) v.findViewById(R.id.tv_workorder_amount);
            tv_workorder_finished = (TextView) v.findViewById(R.id.tv_workorder_finished);
            tv_workorder_dealing = (TextView) v.findViewById(R.id.tv_workorder_dealing);
            tv_workorder_postpone = (TextView) v.findViewById(R.id.tv_workorder_postpone);
        }
    }
}
