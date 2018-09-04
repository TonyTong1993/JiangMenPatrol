package com.ecity.cswatersupply.menu.map;

import android.view.View;
import android.view.View.OnClickListener;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.dialog.ConditionQueryDialog;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.MapFragmentTitleView;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public class CodeRectCommandXtd extends AMapMenuCommand {
    private MapFragmentTitleView mTitleView;
    private IMapOperationContext fragment;
    private MapView mapView;
    private View mRlMapZoomView;

    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        if (null != mapView) {
            GraphicsLayer graphicsLayer = null;
            try {
                graphicsLayer = LayerTool.getAnimationLayer(mapView);
            } catch (Exception e) {
            }
            if (null == graphicsLayer) {
                return false;
            }
            this.fragment = fragment;
            this.mapView = mapView;
            mTitleView = (MapFragmentTitleView) fragment.getmTitleView();
            mTitleView.setQueryBtnGone();
            mRlMapZoomView = fragment.getmRlMapZoomView();
            setMapFragmentTitleView(R.string.map_code_rectquery);

            ConditionQueryDialog dialog = new ConditionQueryDialog(fragment.getContext(), mapView, fragment,true);
            dialog.show();
        }
        return true;
    }

    private void setMapFragmentTitleView(int resId) {
        mTitleView.setBackBtnVisible();
        mTitleView.setTitleText(resId);
        mTitleView.setQueryBtnVisible();
        mTitleView.setOnQueryAddressListener(((MapMainTabFragment)fragment).getQueryClickListener(false,true));
        mTitleView.setOperatorBtnVisible();
        mTitleView.setOnBackListener(new BackClickListener());
        mTitleView.setOperatorBtnBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.btn_mainmenu_clean));
        mTitleView.setOnOperatorListener(new CleanClickListener());
    }

    public class CleanClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            fragment.cleanMapView();
        }
    }

    public class BackClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            mTitleView.setBackBtnGone();
            mTitleView.setTitleText(R.string.fragment_map_title);
            mTitleView.setQueryBtnVisible();
            mTitleView.setOnQueryAddressListener(((MapMainTabFragment)fragment).getQueryClickListener(true));
            mTitleView.setOperatorBtnVisible();
            mTitleView.setOperatorBtnBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.selector_button_map_fragment_opretor));
            mTitleView.setOnOperatorListener(((MapMainTabFragment) fragment).getOperatorClickListener());
            mRlMapZoomView.setVisibility(View.VISIBLE);

            EmptyMapCommandXtd clean = new EmptyMapCommandXtd();
            clean.execute(mapView, fragment);
        }
    }
}
