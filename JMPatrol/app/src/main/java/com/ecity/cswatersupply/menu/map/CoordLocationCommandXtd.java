package com.ecity.cswatersupply.menu.map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.MapFragmentTitleView;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

/**
 * 地图界面的坐标定位
 * @author GXX
 *
 */
public class CoordLocationCommandXtd extends AMapMenuCommand{

    private MapFragmentTitleView mTitleView;
    private IMapOperationContext fragment;
    private MapView mapView;
    private View mRlMapZoomView;
    private LinearLayout locLayout;
    private EditText etX,etY;
    private Button okBtn;
    private GraphicsLayer graphicsLayer = null;
    private PictureMarkerSymbol myCoordLocationSymbol;

    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        if (null != mapView) {
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
            setMapFragmentTitleView(R.string.map_location);

            initLocationView();

            mapView.setOnTouchListener(new MapOnTouchListener(fragment.getContext(), mapView));
        }
        return true;
    }

    private void initLocationView() {
        locLayout = (LinearLayout) fragment.getMapLocationLayout();
        locLayout.setVisibility(View.VISIBLE);
        etX = (EditText) locLayout.findViewById(R.id.location_x);
        etY = (EditText) locLayout.findViewById(R.id.location_y);
        okBtn = (Button) locLayout.findViewById(R.id.ok);
        myCoordLocationSymbol = new PictureMarkerSymbol(fragment.getContext().getResources().getDrawable(R.drawable.marker_point_night_red));

        okBtn.setOnClickListener(new MyOkButtonOnClickListener());
    }

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
            etX.setText("");
            etY.setText("");
            fragment.cleanMapView();
        }
    }

    private class MyOkButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            String xStr = etX.getText().toString();
            String yStr = etY.getText().toString();
            if(xStr.equals("") || yStr.equals("")) {
                Toast.makeText(fragment.getContext(), R.string.empty_location, Toast.LENGTH_SHORT).show();
                return;
            }
            Point point = new Point(Double.valueOf(xStr),Double.valueOf(yStr));
            mapView.centerAt(point, true);
            Graphic graphic = new Graphic(point, myCoordLocationSymbol, null, null);
            graphicsLayer.removeAll();
            graphicsLayer.addGraphic(graphic);
        }
    }

    private class BackClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            mTitleView.setBackBtnGone();
            mTitleView.setTitleText(R.string.fragment_map_title);
            mTitleView.setQueryBtnVisible();
            mTitleView.setOperatorBtnVisible();
            mTitleView.setOperatorBtnBackground(HostApplication.getApplication().getResources().getDrawable(R.drawable.selector_button_map_fragment_opretor));
            mTitleView.setOnOperatorListener(((MapMainTabFragment) fragment).getOperatorClickListener());
            locLayout.setVisibility(View.GONE);
            mRlMapZoomView.setVisibility(View.VISIBLE);

            EmptyMapCommandXtd clean = new EmptyMapCommandXtd();
            clean.execute(mapView, fragment);
        }
    }
}
