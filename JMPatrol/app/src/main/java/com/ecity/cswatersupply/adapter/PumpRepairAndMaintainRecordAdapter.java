package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.PumpRepairAndMaintainInfoModel;

/***
 * Created by MaoShouBei on 2017/5/12.
 */

public class PumpRepairAndMaintainRecordAdapter extends ArrayListAdapter<PumpRepairAndMaintainInfoModel> {
    private LayoutInflater mLayoutInflater;

    public PumpRepairAndMaintainRecordAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        PumpRepairAndMaintainInfoModel model = getList().get(position);
        int operateType = Integer.valueOf(model.getMaintainType());
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.lv_item_pump_detail, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvRecordTime.setText(model.getMaintainTime());
        switch (operateType) {
            case PumpRepairAndMaintainInfoModel.OPERATE_TYPE_REPAIR:
                viewHolder.tvRecordContentLabel.setText(R.string.pump_repair_content_str);
                viewHolder.tvRecordType.setText(R.string.pump_operate_type_repair_str);
                viewHolder.tvRecordManLabel.setText(R.string.pump_repair_man_str);
                viewHolder.tvRecordMan.setText(model.getMaintainUserName());
                viewHolder.tvRecordContent.setText(model.getMaintainContent());
                break;
            case PumpRepairAndMaintainInfoModel.OPERATE_TYPE_EVENT_REPORT:
                viewHolder.tvRecordContentLabel.setText(R.string.pump_report_content_str);
                viewHolder.tvRecordType.setText(R.string.pump_operate_type_event_report_str);
                viewHolder.tvRecordManLabel.setText(R.string.pump_report_man_str);
                viewHolder.tvRecordMan.setText(model.getMaintainUserName());
                viewHolder.tvRecordContent.setText(model.getMaintainContent());
                break;
            case PumpRepairAndMaintainInfoModel.OPERATE_TYPE_MAINTAIN_REPORT:
                viewHolder.tvRecordContentLabel.setVisibility(View.GONE);
                viewHolder.tvRecordType.setText(R.string.pump_operate_type_maintain_report_str);
                viewHolder.tvRecordManLabel.setText(R.string.pump_maintain_man_str);
                viewHolder.tvRecordMan.setText(model.getMaintainUserName());
                viewHolder.tvRecordContent.setVisibility(View.GONE);
                break;
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView tvRecordTime;
        private TextView tvRecordManLabel;
        private TextView tvRecordMan;
        private TextView tvRecordType;
        private TextView tvRecordContentLabel;
        private TextView tvRecordContent;

        public ViewHolder(View v) {
            tvRecordTime = (TextView) v.findViewById(R.id.tv_time);
            tvRecordManLabel = (TextView) v.findViewById(R.id.tv_record_man_label);
            tvRecordMan = (TextView) v.findViewById(R.id.tv_record_man);
            tvRecordType = (TextView) v.findViewById(R.id.tv_record_type);
            tvRecordContentLabel = (TextView) v.findViewById(R.id.tv_record_content_label);
            tvRecordContent = (TextView) v.findViewById(R.id.tv_record_content);
        }
    }
}
