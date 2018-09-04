package com.ecity.cswatersupply.emergency.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.network.response.NoticeModel;
import com.ecity.cswatersupply.utils.DateUtil;
import com.zzz.ecity.android.applibrary.view.CircleTextImageView;

import java.text.ParseException;
import java.util.Date;

/***
 * 公告消息列表适配器
 * @author
 */
public class NoticeListAdapter extends ArrayListAdapter<NoticeModel> {
    private Context context;
    private LayoutInflater layoutInflater;

    public NoticeListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.lv_common_check, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }

        NoticeModel infoModel = mList.get(position);
        holder.tvFirst.setText(infoModel.getTitle());
        StringBuilder builder = new StringBuilder();

        builder.append("公告人").append(":").append(infoModel.getCreater()).append("\n").append("公告时间").append(":").append(infoModel.getCreatetime());
        holder.tvSecond.setText(builder);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;

        private ViewHolder(View view) {
            this.tvFirst = (TextView) view.findViewById(R.id.tv_first);
            this.tvSecond = (TextView) view.findViewById(R.id.tv_second);
        }
    }

}
