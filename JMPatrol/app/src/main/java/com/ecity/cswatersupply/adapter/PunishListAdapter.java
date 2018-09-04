package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.model.event.PunishmentEvent;
import com.z3app.android.util.StringUtil;

public class PunishListAdapter extends ArrayListAdapter<PunishmentEvent> {
    private LayoutInflater inflater;

    public PunishListAdapter(Context context, List<PunishmentEvent> listData) {
        super(context);
        this.setList(listData);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        PunishmentEvent event = getList().get(position);
        int punishState = Integer.parseInt(event.getAttribute(PunishmentEvent.ATTR_KEY_STATE));
        String punishId = event.getAttribute(PunishmentEvent.ATTR_KEY_ID);
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_punishlist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String eventCode = event.getAttribute(PunishmentEvent.ATTR_KEY_CODE);
        if(StringUtil.isBlank(eventCode)) {
            viewHolder.punishEventCode.setText(R.string.event_with_no_code);
        } else {
            viewHolder.punishEventCode.setText(eventCode);
        }
        viewHolder.punishTime.setText(event.getAttribute(PunishmentEvent.ATTR_KEY_CREATED_TIME));
        viewHolder.punishMatter.setText(event.getAttribute(PunishmentEvent.ATTR_KEY_REASON));
        viewHolder.punishContact.setText(event.getAttribute(PunishmentEvent.ATTR_KEY_ACCEPTER));
//        viewHolder.punishPhone.setText(event.getAttribute(PunishmentEvent.ATTR_KEY_PHONE));
        viewHolder.punishLocation.setText(event.getAttribute(PunishmentEvent.ATTR_KEY_ADDRESS));
        viewHolder.tvLeft.setTag(position);
        switch (punishState) {
            case PunishmentEvent.STATUS_REPORTING:
                viewHolder.punishState.setBackgroundResource(R.drawable.shape_punish_state_reporting);
                viewHolder.punishState.setText(R.string.punishment_state_reporting);
                viewHolder.tvLeft.setVisibility(View.VISIBLE);
                viewHolder.tvRight.setVisibility(View.VISIBLE);
                if (("false").equalsIgnoreCase(event.getAttribute(PunishmentEvent.ATTR_KEY_CAN_EDIT))) {
                    viewHolder.tvLeft.setVisibility(View.GONE);
                } else {
                    viewHolder.tvLeft.setText(R.string.punishment_button_reporting);
                }
                viewHolder.tvRight.setText(R.string.event_management_close);
                viewHolder.tvLeft.setOnClickListener(new TvListenner(punishState, punishId, position, false));
                viewHolder.tvRight.setOnClickListener(new TvListenner(punishState, punishId, position, true));
                break;
            case PunishmentEvent.STATUS_PRINTING:
                viewHolder.tvLeft.setVisibility(View.VISIBLE);
                viewHolder.tvRight.setVisibility(View.GONE);
                viewHolder.punishState.setBackgroundResource(R.drawable.shape_punish_state_printing);
                viewHolder.punishState.setText(R.string.punishment_state_printing);
                viewHolder.tvLeft.setText(R.string.punishment_button_printing);
                viewHolder.tvLeft.setOnClickListener(new TvListenner(punishState, punishId, position, false));
                break;
            case PunishmentEvent.STATUS_END:
                viewHolder.punishState.setBackgroundResource(R.drawable.shape_punish_state_ended);
                viewHolder.punishState.setText(R.string.punishment_state_ended);
                viewHolder.tvLeft.setVisibility(View.GONE);
                viewHolder.tvRight.setVisibility(View.GONE);
                break;
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView punishTime, punishState, punishMatter, punishContact, punishPhone, punishLocation, tvLeft, tvRight;
        private TextView punishEventCode;

        public ViewHolder(View v) {
            punishEventCode = (TextView) v.findViewById(R.id.tv_event_code);
            punishTime = (TextView) v.findViewById(R.id.tv_punish_time);
            punishState = (TextView) v.findViewById(R.id.tv_punish_state);
            punishMatter = (TextView) v.findViewById(R.id.tv_punish_matter);
            punishContact = (TextView) v.findViewById(R.id.tv_punish_contact);
//            punishPhone = (TextView) v.findViewById(R.id.tv_punish_number);
            punishLocation = (TextView) v.findViewById(R.id.tv_punish_location);
            tvLeft = (TextView) v.findViewById(R.id.tv_bottom_left);
            tvRight = (TextView) v.findViewById(R.id.tv_bottom_right);
        }
    }

    private class TvListenner implements OnClickListener {
        int punishState;
        String punishId;
        int position;
        TextView tv_title;
        EditText et_amount;
        boolean isRightButton;

        public TvListenner(int punishState, String punishId, int position, boolean isRightButton) {
            this.punishState = punishState;
            this.punishId = punishId;
            this.position = position;
            this.isRightButton = isRightButton;
        }

        @Override
        public void onClick(View v) {
            switch (punishState) {
                case PunishmentEvent.STATUS_REPORTING:
                    if (isRightButton) {
                        EventBusUtil.post(new UIEvent(UIEventStatus.PUNISH_EVENT_CLOSE, getList().get(position)));
                    } else {
                        UIEvent reportEvent = new UIEvent(UIEventStatus.PUNISHSTATE_STATUS_REPORTING);
                        reportEvent.setData(position);
                        EventBusUtil.post(reportEvent);
                    }
                    break;
                case PunishmentEvent.STATUS_PRINTING:
                    if (isRightButton) {
                        EventBusUtil.post(new UIEvent(UIEventStatus.PUNISH_EVENT_CLOSE, getList().get(position)));
                    } else {
                        UIEvent printEvent = new UIEvent(UIEventStatus.PUNISHSTATE_STATUS_PRINTING);
                        printEvent.setData(position);
                        EventBusUtil.post(printEvent);
                    }
                    break;
                default:
                    break;
            }
        }

    }
}