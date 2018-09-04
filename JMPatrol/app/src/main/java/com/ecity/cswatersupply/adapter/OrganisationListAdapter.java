package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.view.CircleTextImageView;

public class OrganisationListAdapter extends ArrayListAdapter<OrganisationSelection> {
    public interface IOrganisationListAdapter {
        void disselectAllItems();

        void onItemCheckStatusChanged(OrganisationSelection item);

        void onItemClickListener(OrganisationSelection item);
    }

    private LayoutInflater mInflater;
    private boolean isSelectEnabled;
    private boolean isMultiSelection;
    private boolean isSelectUser;
    private IOrganisationListAdapter callback;

    public OrganisationListAdapter(Context context, boolean isSelectUser, boolean isMultiSelection, IOrganisationListAdapter callback) {
        super(context);
        mInflater = LayoutInflater.from(context);
        this.isMultiSelection = isMultiSelection;
        this.isSelectUser = isSelectUser;
        this.callback = callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrganisationSelection item = getList().get(position);
        NotificationSelectionViewHolder holder = null;

        if ((null != convertView) && (item.equals(convertView.getTag(R.string.select_organisation_title)))) {
            holder = (NotificationSelectionViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.lv_item_organisation, null);
            holder = new NotificationSelectionViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setTag(R.string.select_organisation_title, item);
        }

        String name = item.getName();
        holder.tvTitle.setText(name);
        if (item.isUser()) {
            holder.ivName.setVisibility(View.VISIBLE);
            holder.ivName.setShowBoard(false);
            holder.ivName.setBGColor(R.color.lightblue);
            holder.ivName.setTextSize(0.25f);
        } else {
            holder.ivName.setVisibility(View.GONE);
        }
        if (!StringUtil.isBlank(name)) {
            String text = "";
            if (name.length() > 2) {
                text = name.substring(name.length() - 2, name.length());
            } else {
                text = name.substring(1, name.length());
            }
            holder.ivName.setText4CircleImage(text);
        }
        MyOnItemClickedListener listener = new MyOnItemClickedListener(item);
        if (item.isUser()) {
            holder.ivIndicator.setVisibility(View.GONE);
            holder.tvTitle.setOnClickListener(listener);
        } else {
            holder.tvTitle.setOnClickListener(listener);
            holder.ivIndicator.setOnClickListener(listener);
        }

        if (shouldShowCheckBox(item)) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(item.isSelected());
            holder.checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener(item));
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setEnabled(false);
        }

        return convertView;
    }

    private boolean shouldShowCheckBox(OrganisationSelection item) {
        return (isSelectUser && item.isUser()) || (!isSelectUser && !item.isUser());
    }

    private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
        private OrganisationSelection item;

        public MyOnCheckedChangeListener(OrganisationSelection item) {
            this.item = item;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked && !isMultiSelection) {
                callback.disselectAllItems();
            }
            item.setSelected(isChecked);
            callback.onItemCheckStatusChanged(item);
            notifyDataSetChanged();
        }
    }

    private class MyOnItemClickedListener implements View.OnClickListener {
        private OrganisationSelection item;

        public MyOnItemClickedListener(OrganisationSelection item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            callback.onItemClickListener(item);
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
        TextView tvTitle;
        ImageView ivIndicator;
        CircleTextImageView ivName;

        public NotificationSelectionViewHolder(View view) {
            checkBox = (CheckBox) view.findViewById(R.id.cb_status);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ivIndicator = (ImageView) view.findViewById(R.id.iv_indicator);
            ivName = (CircleTextImageView) view.findViewById(R.id.iv_name);
        }
    }
}
