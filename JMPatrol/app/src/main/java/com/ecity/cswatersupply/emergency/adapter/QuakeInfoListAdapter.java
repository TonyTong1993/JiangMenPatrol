package com.ecity.cswatersupply.emergency.adapter;

import java.text.ParseException;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.activity.EarthquakeLocalInfoActivity;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoDistributionOperaterXtd;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.TextUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.view.CircleTextImageView;

/***
 * 地震信息列表适配器
 * @author Gxx 2016-11-23
 */
public class QuakeInfoListAdapter extends ArrayListAdapter<EarthQuakeInfoModel>{
    private Context context;
    private LayoutInflater layoutInflater;

    public QuakeInfoListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_list_quakeinfo, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }

        EarthQuakeInfoModel infoModel = mList.get(position);
        holder.degree.setShowBoard(true);
        holder.degree.setBGColor(Color.TRANSPARENT);
        holder.degree.setTextSize(0.25f);
        holder.degree.setBoardWidth(12);
        double degree = infoModel.getML();
        if(0.0 <= degree && degree < 2.0){
            holder.degree.setBoardColor(Color.parseColor("#dbf103"));
            holder.degree.setCircleTextColor(Color.parseColor("#dbf103"));
        } else if(2.0 <= degree && degree < 4.0){
            holder.degree.setBoardColor(Color.parseColor("#f1a003"));
            holder.degree.setCircleTextColor(Color.parseColor("#f1a003"));
        } else if(4.0 <= degree && degree < 7.0){
            holder.degree.setBoardColor(Color.parseColor("#f16503"));
            holder.degree.setCircleTextColor(Color.parseColor("#f16503"));
        } else {
            holder.degree.setBoardColor(Color.parseColor("#ff0000"));
            holder.degree.setCircleTextColor(Color.parseColor("#ff0000"));
        }
        holder.degree.setText4CircleImage(String.valueOf(infoModel.getML()));
        if(!StringUtil.isBlank(infoModel.getRegion())) {
            holder.region.setText(infoModel.getRegion());
        } else if(!StringUtil.isBlank(infoModel.getLocation())) {
            holder.region.setText(infoModel.getLocation());
        } else {
            TextUtil.DisplayValueOfView(holder.region,infoModel.getRegion());
        }
        holder.time.setText(infoModel.getTime());
//        Date startDate;
//        try {
//            startDate = DateUtil.stringToDate(infoModel.getTime());
//            holder.difftime.setText(DateUtil.twoDateDistance(startDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        holder.region.setOnClickListener(new AddressOnClickListener(infoModel));
        return convertView;
    }
    
    private class ViewHolder {
        private CircleTextImageView degree;
        private TextView region;
        private TextView time;
        private TextView difftime;

        private ViewHolder(View view) {
            this.degree = (CircleTextImageView) view.findViewById(R.id.tv_degree);
            this.region = (TextView) view.findViewById(R.id.tv_region);
            this.time = (TextView) view.findViewById(R.id.tv_time);
            this.difftime = (TextView) view.findViewById(R.id.tv_difftime);
        }
    }

    private class AddressOnClickListener implements View.OnClickListener {
        private EarthQuakeInfoModel infoModel;

        public AddressOnClickListener(EarthQuakeInfoModel infoModel) {
            this.infoModel = infoModel;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra(Constants.EARTH_QUAKE_LIST_CLICK, infoModel);
            intent.putExtra(MapActivity.MAP_OPERATOR, QuakeInfoDistributionOperaterXtd.class.getName());
            intent.putExtra(MapActivity.MAP_TITLE, ResourceUtil.getStringById(R.string.earth_quake_information_title));
            context.startActivity(intent);
        }
    }

}
