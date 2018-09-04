package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.ListViewMenuItem;
import com.z3app.android.util.StringUtil;

public class InboxMainTabAdapter extends ArrayListAdapter<ListViewMenuItem> {
    private LayoutInflater mInflater;

    public InboxMainTabAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InboxMainTabItemViewHolder holder = null;
        if (null != convertView) {
            holder = (InboxMainTabItemViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.lv_item_inbox_menu, null);
            holder = new InboxMainTabItemViewHolder(convertView);
            convertView.setTag(holder);
        }

        ListViewMenuItem item = getList().get(position);
        holder.ivIcon.setImageResource(item.getIconId());
        holder.tvTitle.setText(item.getTitle());
        if (StringUtil.isBlank(item.getDetail())) {
            holder.tvDetail.setVisibility(View.GONE);
        } else {
            holder.tvDetail.setText(item.getDetail());
        }

        return convertView;
    }

    private static class InboxMainTabItemViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDetail;

        public InboxMainTabItemViewHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        }
    }
}
