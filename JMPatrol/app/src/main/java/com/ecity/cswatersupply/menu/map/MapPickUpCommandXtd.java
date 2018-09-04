package com.ecity.cswatersupply.menu.map;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.MapFragmentTitleView;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.zzz.ecity.android.applibrary.utils.ScreenUtil;

/**
 * 地图拾取菜单
 * @author qiwei
 *
 */
public class MapPickUpCommandXtd extends AMapMenuCommand {
    private MapFragmentTitleView mTitleView;
    private MapView mapView;
    private IMapOperationContext fragment;
    private GraphicsLayer graphicsLayer = null;

    @Override
    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        this.fragment = fragment;
        this.mapView = mapView;
        graphicsLayer = LayerTool.getGraphicsLayer(mapView);
        mTitleView = (MapFragmentTitleView) fragment.getmTitleView();
        mTitleView.setQueryBtnGone();
        if ((mapView == null) || (graphicsLayer == null)) {
            ToastUtil.showLong(fragment.getContext().getResources().getString(R.string.planningtask_not_load_mapview));
            return false;
        }
        if (mapView.isLoaded()) {
            resetValues();
            setMapFragmentTitleView(R.string.pickup);
            mapView.setOnSingleTapListener(m_OnSingleTapListener);
        }
        return true;
    }

    OnSingleTapListener m_OnSingleTapListener = new OnSingleTapListener() {

        private static final long serialVersionUID = 1L;

        public void onSingleTap(float x, float y) {
            resetValues();
            Point position = mapView.toMapPoint(x, y);
            setCalloutByPoint(position);
        }
    };

    private void setMapFragmentTitleView(int resId) {
        mTitleView.setBackBtnVisible();
        mTitleView.setTitleText(resId);
        mTitleView.setOperatorBtnVisible();
        mTitleView.setOnBackListener(new BackClickListener());
        mTitleView.setOperatorBtnBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.btn_mainmenu_clean));
        mTitleView.setOnOperatorListener(new CleanClickListener());
    }

    private class CleanClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            resetValues();
        }
    }

    private void resetValues() {
        graphicsLayer.removeAll();
        if (mapView.getCallout().isShowing()) {
            mapView.getCallout().hide();
        }
    }

    private class BackClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            resetValues();
            mTitleView.setBackBtnGone();
            mTitleView.setTitleText(R.string.fragment_map_title);
            mTitleView.setQueryBtnVisible();
            mTitleView.setOperatorBtnVisible();
            mTitleView.setOperatorBtnBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.selector_button_map_fragment_opretor));
            mTitleView.setOnOperatorListener(((MapMainTabFragment) fragment).getOperatorClickListener());

            EmptyMapCommandXtd clean = new EmptyMapCommandXtd();
            clean.execute(mapView, fragment);
        }
    }

    private void setCalloutByPoint(Point position) {
        LayoutInflater inflater = LayoutInflater.from(fragment.getContext());
        View view = inflater.inflate(R.layout.callout_layout, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        String location = String.format("%.3f,%.3f", position.getX(), position.getY());
        title.setText(location);
        Graphic g1 = new Graphic(position, new SimpleMarkerSymbol(Color.BLUE, 8, STYLE.CIRCLE));
        graphicsLayer.addGraphic(g1);
        Callout callout = mapView.getCallout();
        callout.setStyle(R.layout.callout);
        callout.setOffset(0, 5);
        callout.show(position, view);
        callout.setMaxWidth(ResourceUtil.getDimensionPixelSizeById(R.dimen.callout_w));
        callout.setMaxHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.callout_h));
    }
}
