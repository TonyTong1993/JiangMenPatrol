package com.ecity.cswatersupply.project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.project.activity.LogBackActivity;
import com.ecity.cswatersupply.project.network.response.SafeEventListModel;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;

public class SafeEventListAdapter extends ArrayListAdapter<SafeEventListModel> {
    private LayoutInflater inflater;
    private SafeProjectListModel clickModel;
    private Activity activity;

    public SafeEventListAdapter(Activity context, SafeProjectListModel clickModel) {
        super(context);
        this.clickModel = clickModel;
        this.activity = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SafeEventListModel model = getList().get(position);

        ViewHolder holder = null;
        if ((null == convertView)) {
            convertView = inflater.inflate(R.layout.lv_common_safe_event_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvFirst.setText(clickModel.getName());
        StringBuilder builder1 = new StringBuilder();
        builder1.append("上报人").append(":").append(model.getReporter()).append("\n").append("上报时间").append(":").append(model.getCreatetime()).append("\n").append("事件描述").append(":").append(model.getEvedes()).append("\n").append("所处位置").append(":").append(model.getPosition());
        StringBuilder builder2 = new StringBuilder();
        builder2.append("状态").append(":").append(model.getEvetype());
        holder.tvSecond.setText(builder1);
        holder.tvThird.setText(builder2);

        holder.btLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LogBackActivity.class);
                intent.putExtra(LogBackActivity.INTENT_KEY_LOG_BACK_TYPE, "SAFE_FLAG");
                intent.putExtra(LogBackActivity.INTENT_KEY_LOG_BACK_PROJECTID, model.getGid());
                intent.putExtra(LogBackActivity.INTENT_KEY_LOG_BACK_TITLE, "安全日志");
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;
        private TextView tvThird;
        private ImageView iv_right_arrow;
        private Button btLog;

        public ViewHolder(View view) {
            super();
            this.tvFirst = (TextView) view.findViewById(R.id.tv_first);
            this.tvSecond = (TextView) view.findViewById(R.id.tv_second);
            this.tvThird = (TextView) view.findViewById(R.id.tv_third);
            this.iv_right_arrow = (ImageView) view.findViewById(R.id.iv_right_arrow);
            this.btLog = (Button) view.findViewById(R.id.bt_log);
        }
    }
}
