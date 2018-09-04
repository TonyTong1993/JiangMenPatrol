package com.ecity.cswatersupply.emergency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;

public class EarthQuakeEmergencyInvestigationAdapter extends ArrayListAdapter<EarthQuakeQuickReportModel> {
    private LayoutInflater mInflater = null;
    private Context mContext = null;

    public EarthQuakeEmergencyInvestigationAdapter(Context context ) {
        super(context);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_earthquake_emergency_investigation, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }
        EarthQuakeQuickReportModel model = mList.get(position);
        viewHolder.tv_report_address.setText(model.getSurveyAddress());
        viewHolder.tv_survey_time.setText(model.getSurveytTime());
        viewHolder.tv_survey_person.setText(model.getSurveyPerson());
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_report_address, tv_survey_time, tv_survey_person;

        public ViewHolder(View v) {
            tv_report_address = (TextView) v.findViewById(R.id.tv_report_address);
            tv_survey_time = (TextView) v.findViewById(R.id.tv_survey_time);
            tv_survey_person = (TextView) v.findViewById(R.id.tv_survey_person);
        }
    }
}
