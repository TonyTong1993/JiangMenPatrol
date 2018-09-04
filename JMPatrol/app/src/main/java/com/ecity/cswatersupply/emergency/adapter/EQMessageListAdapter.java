package com.ecity.cswatersupply.emergency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.network.response.NoticeModel;

/***
 * 计划任务列表适配器
 * @author
 */
public class EQMessageListAdapter extends ArrayListAdapter<NoticeModel> {
    private Context context;
    private LayoutInflater layoutInflater;

    public EQMessageListAdapter(Context context) {
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
        holder.ivRight.setVisibility(View.GONE);
        NoticeModel infoModel = mList.get(position);
        String userName = StringUtil.isBlank(infoModel.getCreater())? "无":infoModel.getCreater();
        holder.tvFirst.setText("发布人:" + userName);
        StringBuilder builder = new StringBuilder();

        builder.append("发布时间").append(":").append(infoModel.getCreatetime()).append("\n").append("任务内容").append(":").append(infoModel.getContent());
        holder.tvSecond.setText(builder);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;
        private ImageView ivRight;

        private ViewHolder(View view) {
            this.tvFirst = (TextView) view.findViewById(R.id.tv_first);
            this.tvSecond = (TextView) view.findViewById(R.id.tv_second);
            this.ivRight = (ImageView) view.findViewById(R.id.iv_right_arrow);
        }
    }

}
