package com.ecity.cswatersupply.emergency.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.utils.TextUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.view.CircleTextImageView;

public class EarthQuakeQuickReportAdapter extends ArrayListAdapter<EarthQuakeQuickReportModel> {
    private LayoutInflater mInflater = null;
    private Context mContext = null;

    public EarthQuakeQuickReportAdapter(Context context) {
        super(context);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mList || position > mList.size()) {
            return convertView;
        }
        EarthQuakeQuickReportModel model = mList.get(position);

        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_earthquake_quickreport, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TextUtil.DisplayValueOfView(viewHolder.tv_report_address, model.getSurveyAddress());
        if(0 == model.getEarthQuakeId()) {
            TextUtil.DisplayValueOfView(viewHolder.tv_earthquake_id, "");
        } else {
            TextUtil.DisplayValueOfView(viewHolder.tv_earthquake_id, String.valueOf(model.getEarthQuakeId()));
        }
        TextUtil.DisplayValueOfView(viewHolder.tv_survey_time, String.valueOf(model.getSurveytTime()));
        TextUtil.DisplayValueOfView(viewHolder.tv_survey_person, String.valueOf(model.getSurveyPerson()));

        viewHolder.tv_degree.setShowBoard(true);
        viewHolder.tv_degree.setBGColor(Color.TRANSPARENT);
        viewHolder.tv_degree.setBoardColor(Color.YELLOW);
        viewHolder.tv_degree.setTextSize(0.2f);
        viewHolder.tv_degree.setBoardWidth(12);

        if(!StringUtil.isBlank(model.getEarthQuakeName())) {
            viewHolder.tv_degree.setText4CircleImage(getFirstTwoWords(model.getEarthQuakeName()));
        } else if(!StringUtil.isBlank(model.getArea())){
            viewHolder.tv_degree.setText4CircleImage(getFirstTwoWords(model.getArea()));
        } else if(0 != model.getEarthQuakeId()) {
            viewHolder.tv_degree.setText4CircleImage(String.valueOf(model.getEarthQuakeId()));
        } else {
            model.setEarthQuakeName("暂无");
        }
        return convertView;
    }

    private String getFirstTwoWords(String str) {
        int length = str.length();
        if(length > 2) {
            return str.substring(0, 2);
        } else {
            return str;
        }
    }
    
    public static class ViewHolder {
        public TextView tv_report_address, tv_earthquake_id, tv_survey_time, tv_survey_person;
        public CircleTextImageView tv_degree;

        public ViewHolder(View v) {
            tv_report_address = (TextView) v.findViewById(R.id.tv_report_address);
            tv_earthquake_id = (TextView) v.findViewById(R.id.tv_earthquake_id);
            tv_survey_time = (TextView) v.findViewById(R.id.tv_survey_time);
            tv_survey_person = (TextView) v.findViewById(R.id.tv_survey_person);
            tv_degree = (CircleTextImageView) v.findViewById(R.id.tv_degree);
        }
    }
}
