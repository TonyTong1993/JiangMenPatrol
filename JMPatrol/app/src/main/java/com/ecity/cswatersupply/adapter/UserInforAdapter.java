package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class UserInforAdapter extends BaseAdapter {
    private List<String> title;
    private List<String> infor;
    private LayoutInflater inflater;

    public UserInforAdapter(List<String> title, List<String> infor, Context context) {
        super();
        this.title = title;
        this.infor = infor;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        return title.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_user_information, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userInforTitle.setText(title.get(position));
        if (title.get(position).equalsIgnoreCase("头像")) {
            Drawable drawable = convertView.getResources().getDrawable(R.drawable.defaultphoto);
            drawable.setBounds(0, 0, 100, 100);
            viewHolder.userInfor.setCompoundDrawables(drawable, null, null, null);
        } else {
            viewHolder.userInfor.setText(infor.get(position));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView userInforTitle;
        private TextView userInfor;

        public ViewHolder(View v) {
            userInforTitle = (TextView)v.findViewById(R.id.item_user_title);
            userInfor = (TextView)v.findViewById(R.id.item_user_information);
        }
    }
}
