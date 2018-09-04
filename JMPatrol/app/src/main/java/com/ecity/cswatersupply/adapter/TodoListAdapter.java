package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.NotificationSelection;

public class TodoListAdapter extends ArrayListAdapter<NotificationSelection> {
    private LayoutInflater mInflater;
    private boolean isSelectEnabled;
    private OnNotificationCheckStatusChangedListener listener;

    public interface OnNotificationCheckStatusChangedListener {
        /**
         * @param selection 被操作的通知。选中状态已经被更新了，调用isSelected方法，获得的是最新的选中状态。
         */
        void onNotificationCheckStatusChanged(NotificationSelection selection);
    }

    public TodoListAdapter(Context context, OnNotificationCheckStatusChangedListener listener) {
        super(context);
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationSelection item = getList().get(position);
        NotificationSelectionViewHolder holder = null;

        if ((null != convertView) && (item.getNotification().equals(convertView.getTag(R.string.lv_todo_item_tag)))) {
            holder = (NotificationSelectionViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.lv_item_todo, null);
            holder = new NotificationSelectionViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setTag(R.string.lv_todo_item_tag, item.getNotification());
        }

        holder.ivIcon.setImageResource(R.drawable.message_icon_work);
        holder.tvTitle.setText(item.getNotification().getSentTime());
        holder.tvDetail.setText(item.getNotification().getContent());

        if (isSelectEnabled) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(item.isSelected());
            holder.checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener(item));
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        if (item.getNotification().isNew()) {
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.tips_red_no_text);
            holder.ivIndicator.setImageDrawable(drawable);
        } else {
            holder.ivIndicator.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
        private NotificationSelection item;

        public MyOnCheckedChangeListener(NotificationSelection item) {
            this.item = item;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            item.setSelected(isChecked);
            if (listener != null) {
                listener.onNotificationCheckStatusChanged(item);
            }
        }
    }

    public boolean isSelectEnabled() {
        return isSelectEnabled;
    }

    public void setSelectEnabled(boolean isSelectEnabled) {
        this.isSelectEnabled = isSelectEnabled;
    }

    private static class NotificationSelectionViewHolder {
        CheckBox checkBox;
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDetail;
        ImageView ivIndicator;

        public NotificationSelectionViewHolder(View view) {
            checkBox = (CheckBox) view.findViewById(R.id.cb_status);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDetail = (TextView) view.findViewById(R.id.tv_detail);
            ivIndicator = (ImageView) view.findViewById(R.id.iv_indicator);
        }
    }
}
