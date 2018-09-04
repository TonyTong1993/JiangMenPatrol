package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.ListViewMenuItem;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.utils.ScreenUtil;

public class MyProfileMainTabAdapter extends ArrayListAdapter<ListViewMenuItem> {
    private LayoutInflater mInflater;

    public MyProfileMainTabAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyProfileTabItemViewHolder holder = null;
        if (null != convertView) {
            holder = (MyProfileTabItemViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.lv_item_my_profile_menu, null);
            holder = new MyProfileTabItemViewHolder(convertView);
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

    private void adjustFirstItemLayout(View view) {
        int padding20 = ScreenUtil.pxTodip(mContext, 20);
        int padding60 = ScreenUtil.pxTodip(mContext, 60);
        view.setPadding(padding20, padding60, padding20, padding60);
        ImageView imgView = (ImageView) view.findViewById(R.id.iv_icon);
        imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        imgView.setPadding(padding60, padding60, padding60, padding60);
    }

    private static class MyProfileTabItemViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDetail;

        public MyProfileTabItemViewHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        }
    }
}
