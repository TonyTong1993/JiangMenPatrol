package com.ecity.cswatersupply.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.map.LocationOperatorXtd;
import com.ecity.cswatersupply.model.event.ConstructionEvent;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.model.event.RepairementEvent;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.z3app.android.util.StringUtil;

public class EventListAdapter<T extends Event> extends ArrayListAdapter<T> {

    public interface IEventOperationActionListener {
        void onLeftButtonClicked(Event event, int position);

        void onRightButtonClicked(Event event, int position);
    }

    private LayoutInflater inflater;
    private Map<String, String> statusMap;
    private IEventOperationActionListener operationListener;

    public EventListAdapter(Context context, List<T> listData, Map<String, String> status, IEventOperationActionListener operationListener) {
        super(context);
        this.setList(listData);
        this.inflater = LayoutInflater.from(context);
        this.statusMap = status;
        this.operationListener = operationListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int eventStatus = -1;
        T event = getList().get(position);
        try {
            if (!StringUtil.isEmpty(event.getAttribute(Event.ATTR_KEY_STATE))) {
                eventStatus = Integer.parseInt(event.getAttribute(Event.ATTR_KEY_STATE));
            }
        } catch (Exception e) {
            LogUtil.i("EventListAdapter", e.toString());
        }

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.lv_item_event, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String eventCode = event.getAttribute(Event.ATTR_KEY_CODE);
        if(StringUtil.isBlank(eventCode)) {
            viewHolder.tvEventCode.setText(R.string.event_with_no_code);
        } else {
            viewHolder.tvEventCode.setText(eventCode);
        }
        viewHolder.tvReportTime.setText(event.getAttribute(Event.ATTR_KEY_REPORT_TIME));
        viewHolder.tvDescription.setText(event.getAttribute(Event.ATTR_KEY_DESCRIPTION));
        viewHolder.tvReporter.setText(event.getAttribute(Event.ATTR_KEY_REPORTER));
        viewHolder.tvAddress.setText(event.getAttribute(Event.ATTR_KEY_ADDRESS));
        viewHolder.tvLeft.setOnClickListener(new MyOnClickListener(event, false, position));
        viewHolder.tvRight.setOnClickListener(new MyOnClickListener(event, true, position));
        if (event instanceof ConstructionEvent) {
            viewHolder.layoutLocation.setVisibility(View.GONE);
        } else {
            viewHolder.tvAddress.setOnClickListener(new MyLoctaionClickListener(event,position));
        }
        if (statusMap.containsKey(event.getAttribute(Event.ATTR_KEY_STATE))) {
            viewHolder.tvState.setText(statusMap.get(event.getAttribute(Event.ATTR_KEY_STATE)));
        }

        switch (eventStatus) {
            case RepairementEvent.STATUS_UNPROCESSING:
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_punish_state_reporting);
                int eventType = Integer.parseInt(event.getAttribute(Event.ATTR_KEY_EVENTTYPE));
                if (eventType == EventType.CONSTRUCTION.getValue()) {
                    viewHolder.tvLeft.setVisibility(View.VISIBLE);
                    viewHolder.tvLeft.setText(R.string.event_management_to_task);
                    viewHolder.tvRight.setVisibility(View.VISIBLE);
                    viewHolder.tvRight.setText(R.string.event_management_close);
                } else {
                    viewHolder.tvLeft.setVisibility(View.VISIBLE);
                    viewHolder.tvLeft.setText(R.string.event_management_to_work_order);
                    viewHolder.tvRight.setVisibility(View.VISIBLE);
                    viewHolder.tvRight.setText(R.string.event_management_close);
                }
                break;
            case RepairementEvent.STATUS_PROCESSED:
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_punish_state_printing);
                break;
            case RepairementEvent.STATUS_END:
                viewHolder.tvState.setBackgroundResource(R.drawable.shape_punish_state_ended);
                break;
        }

        //事件转任务权限判断
        String canEdit = event.getAttribute(Event.ATTR_KEY_CAN_EDIT);
        int eventType = Integer.parseInt(event.getAttribute(Event.ATTR_KEY_EVENTTYPE));
        if (eventStatus == Event.STATUS_END) {
            viewHolder.tvLeft.setVisibility(View.GONE);
            viewHolder.tvRight.setVisibility(View.GONE);
        } else if (eventStatus == Event.STATUS_PROCESSED) {
            viewHolder.tvRight.setVisibility(View.GONE);
            if (eventType == EventType.CONSTRUCTION.getValue()) {
                viewHolder.tvLeft.setVisibility(View.GONE);
            } else {
                viewHolder.tvLeft.setVisibility(View.VISIBLE);
                viewHolder.tvLeft.setText(R.string.event_management_view_work_order);
            }
        } else {
            if (canEdit.equalsIgnoreCase("false")) {
                viewHolder.tvLeft.setVisibility(View.GONE);
                viewHolder.tvRight.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView tvReportTime, tvState, tvDescription, tvReporter, tvAddress, tvLeft, tvRight;
        private TextView tvEventCode;
        private LinearLayout layoutLocation;

        public ViewHolder(View v) {
            tvEventCode = (TextView) v.findViewById(R.id.tv_event_code);
            tvReportTime = (TextView) v.findViewById(R.id.tv_punish_time);
            tvState = (TextView) v.findViewById(R.id.tv_punish_state);
            tvDescription = (TextView) v.findViewById(R.id.tv_punish_matter);
            tvReporter = (TextView) v.findViewById(R.id.tv_punish_contact);
            tvAddress = (TextView) v.findViewById(R.id.tv_punish_location);
            tvLeft = (TextView) v.findViewById(R.id.tv_bottom_left);
            tvRight = (TextView) v.findViewById(R.id.tv_bottom_right);
            layoutLocation = (LinearLayout) v.findViewById(R.id.lineLayout_location);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private T event;
        private boolean isRightButton;
        private int position;

        public MyOnClickListener(T event, boolean isRightButton, int position) {
            this.event = event;
            this.isRightButton = isRightButton;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (operationListener != null) {
                if (isRightButton) {
                    operationListener.onRightButtonClicked(event, position);
                } else {
                    operationListener.onLeftButtonClicked(event, position);
                }
            }
        }
    }

    private class MyLoctaionClickListener implements View.OnClickListener {
        private T event;
        private int position;

        public MyLoctaionClickListener(T event, int position) {
            this.event = event;
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            EventBusUtil.post(new UIEvent(UIEventStatus.EVENT_ADDRESS_ON_CLICKED,position));

            Bundle data = new Bundle();
            data.putString(MapActivity.EVENT_LLOCATION_LAT, event.getAttribute(Event.ATTR_KEY_Y));
            data.putString(MapActivity.EVENT_LLOCATION_LON, event.getAttribute(Event.ATTR_KEY_X));
            data.putString(MapActivity.LOCATION_OPERATOR, LocationOperatorXtd.class.getName());
            data.putString(MapActivity.MAP_TITLE, ResourceUtil.getStringById(R.string.event_map_location));
            UIHelper.startActivityWithExtra(MapActivity.class, data);
        }
    }
}
