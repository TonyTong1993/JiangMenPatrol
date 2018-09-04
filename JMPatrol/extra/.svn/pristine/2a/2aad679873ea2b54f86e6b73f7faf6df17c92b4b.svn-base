package com.ecity.mobile.android.bdlbslibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.tools.CoordinateConverter;
import com.baidu.lbsapi.tools.Point;

/**
 * Created by zhengzhuanzi on 2017/7/3.
 */

public class BDPanoActivity extends Activity {

    public static final String PANO_COORD_TYPE = "PANO_COORD_TYPE";
    public static final String PANO_COORD_TYPE_BD = "PANO_COORD_TYPE_BD";
    public static final String PANO_COORD_TYPE_WGS84 = "PANO_COORD_TYPE_WGS84";
    public static final String PANO_COORD_LON = "PANO_COORD_LON";
    public static final String PANO_COORD_LAT = "PANO_COORD_LAT";

    private PanoramaView mPanoView;
    private static LocationPluginMain locationPluginMain;
    private ImageButton ibtn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBMapManager();
        setContentView(R.layout.activity_bd_pano);

        initView();

        Intent intent = getIntent();
        Point resultPointLL = null;
        if (intent != null) {
            String coordType = PANO_COORD_TYPE_WGS84;
            if (intent.hasExtra(PANO_COORD_TYPE)) {
                coordType = intent.getStringExtra(PANO_COORD_TYPE);
            }

            if (intent.hasExtra(PANO_COORD_LON) && intent.hasExtra(PANO_COORD_LAT)) {
                try {
                    Point sourcePoint =
                            new Point(Double.valueOf(intent.getStringExtra(PANO_COORD_LON)), Double.valueOf(intent.getStringExtra(PANO_COORD_LAT)));
                    if (PANO_COORD_TYPE_WGS84.equalsIgnoreCase(coordType)) {
                        resultPointLL = CoordinateConverter.converter(CoordinateConverter.COOR_TYPE.COOR_TYPE_WGS84, sourcePoint);
                        if (null != resultPointLL) {
                            showPanoAtLocation(resultPointLL.x, resultPointLL.y);
                        }
                    } else {
                        showPanoAtLocation(sourcePoint.x, sourcePoint.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showPanoAtLocation(double lon, double lat) {
        mPanoView.setShowTopoLink(true);
        mPanoView.setPoiMarkerVisibility(true);
        mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        mPanoView.setPanorama(lon, lat);

    }

    private void initBMapManager() {
        if (null == locationPluginMain) {
            locationPluginMain = new LocationPluginMain(getApplication());
        }
        if (locationPluginMain.mBMapManager == null) {
            locationPluginMain.mBMapManager = new BMapManager(getApplication());
            locationPluginMain.mBMapManager.init(new LocationPluginMain.MyGeneralListener());
        }
    }

    private void initView() {
        mPanoView = (PanoramaView) findViewById(R.id.panorama);
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }
}
