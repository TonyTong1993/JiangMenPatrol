package com.ecity.cswatersupply.project.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;

public class SafeProjectListAdapter extends ArrayListAdapter<SafeProjectListModel> {
    private LayoutInflater inflater;

    public SafeProjectListAdapter(Activity context) {
        super(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SafeProjectListModel model = getList().get(position);

        ViewHolder holder = null;
        if ((null == convertView)) {
            convertView = inflater.inflate(R.layout.lv_common_check, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvFirst.setText(model.getName());
        String typeStr = "";
        switch (model.getType()) {
            case "1":
                typeStr = "自建工程";
                break;
            case "2":
                typeStr = "重点工程";
                break;
            case "3":
                typeStr = "其他基建工程";
                break;
            case "4":
                typeStr = "管理外单位工程";
                break;
            default:
                break;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("项目编码").append(":").append(model.getCode()).append("\n").append("项目类型").append(":").append(typeStr).append("\n").append("计划开工时间").append(":").append(model.getStartdate()).append("\n").append("计划竣工时间").append(model.getEnddate());
        holder.tvSecond.setText(builder);
        return convertView;
    }

    static class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;
        private ImageView iv_right_arrow;

        public ViewHolder(View view) {
            super();
            this.tvFirst = (TextView) view.findViewById(R.id.tv_first);
            this.tvSecond = (TextView) view.findViewById(R.id.tv_second);
            this.iv_right_arrow = (ImageView) view.findViewById(R.id.iv_right_arrow);
        }
    }
}
