package com.ecity.cswatersupply.emergency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.model.QBOGridModel;

/**
 * 速报总览界面GirdView的Adapter
 * @author Gxx 2016-11-21
 *
 */
public class QBOGridAdapter extends ArrayListAdapter<QBOGridModel> {
    private Context mContext;
    private LayoutInflater mInflater;

    public QBOGridAdapter(Context context) {
        super(context);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_grid_quickbulletinoverview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }
        QBOGridModel qboGridModel = mList.get(position);
        holder.cricleText.setTextColor(mContext.getResources().getColor(qboGridModel.getColor()));
        holder.cricleText.setText(qboGridModel.getValue());
        holder.name.setText(qboGridModel.getName());
//        holder.unit.setText("("+qboGridModel.getUnit()+")");

        return convertView;
    }

    private class ViewHolder{
        private TextView cricleText;
        private TextView name;
        private TextView unit;

        private ViewHolder(View view) {
            this.cricleText = (TextView) view.findViewById(R.id.tv_cricle_text);
            this.name = (TextView) view.findViewById(R.id.tv_name);
            this.unit = (TextView) view.findViewById(R.id.tv_unit);
        }
    }

}
