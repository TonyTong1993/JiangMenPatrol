package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.activity.QuickReportListActivity;
import com.ecity.cswatersupply.emergency.menu.UnUsualReportOperater;
import com.ecity.cswatersupply.emergency.model.EQMonitorStationModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.model.ILonlatProvider;
import com.ecity.cswatersupply.model.PatrolUser;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;

public class MapNavigationPopView {

    private IMapOperationContext mapOperationContext;
    private PopupWindow popupWindow = null;
    private View contentView;
    private ViewHolder holder;
    private Context context;
    private OnClickListener customClickListener;

    public MapNavigationPopView(IMapOperationContext mapOperationContext) {
        this(mapOperationContext, mapOperationContext.getNaviClickListener());
    }

    public MapNavigationPopView(IMapOperationContext mapOperationContext, OnClickListener customClickListener) {
        this.mapOperationContext = mapOperationContext;
        context = mapOperationContext.getContext();
        this.customClickListener = customClickListener;
    }

    public void initPopWindow(int rootViewId, AModel infoModel) {
        dismiss();
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.map_navigation_pop, null);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.earth_quakeinfo_detaile_pop);
        popupWindow.setOutsideTouchable(false);

        updatePopData(infoModel);

        View rootview = LayoutInflater.from(context).inflate(rootViewId, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    public void updatePopData(AModel infoModel) {
        if (null == holder) {
            return;
        }

        if (infoModel == null) {
            return;
        }

        holder.tvQuakeInfo.setText(infoModel.toString());
        holder.naviBtn.setVisibility(View.VISIBLE);
        holder.naviBtn.setOnClickListener(customClickListener);
        if ((infoModel instanceof EarthQuakeInfoModel)) {
            holder.reportBtn.setVisibility(View.VISIBLE);
            holder.reportBtn.setOnClickListener(new EarthQuakeQuickReportClickListener((EarthQuakeInfoModel) infoModel));
        } else {
            holder.reportBtn.setVisibility(View.GONE);
        }

        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                holder.reportBtn.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

        if (infoModel instanceof ILonlatProvider) {
            ILonlatProvider lonlat = (ILonlatProvider) infoModel;
            double[] lonLat = {lonlat.getLongitude(), lonlat.getLatitude()};
            mapOperationContext.setTargetLonLat(lonLat);
        }

        if (infoModel instanceof PatrolUser) {
            holder.naviBtn.setBackgroundResource(R.drawable.css_mapviewpop_track);
        }
    }

    public class ViewHolder {
        private TextView tvQuakeInfo;
        private ImageButton naviBtn;
        private Button reportBtn;

        private ViewHolder(View view) {
            this.tvQuakeInfo = (TextView) view.findViewById(R.id.tv_quake_info);
            this.naviBtn = (ImageButton) view.findViewById(R.id.location);
            this.reportBtn = (Button) view.findViewById(R.id.btn_quick_report);
        }
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void dismiss() {
        if (null != popupWindow) {
            holder = null;
            contentView = null;
            popupWindow.dismiss();
        }
    }

    private class EarthQuakeQuickReportClickListener implements OnClickListener {
        EarthQuakeInfoModel infoModel;

        public EarthQuakeQuickReportClickListener(EarthQuakeInfoModel infoModel) {
            this.infoModel = infoModel;
        }

        @Override
        public void onClick(View arg0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.EARTH_QUAKE_LIST_CLICK, infoModel);
            UIHelper.startActivityWithExtra(QuickReportListActivity.class, bundle);
        }
    }

}
