package com.ecity.cswatersupply.ui.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class GpsStateActivity extends BaseActivity {
    private CustomTitleView mViewTitle;
    private TextView mValueLnt;
    private TextView mValueLat;
    private TextView mValueAccuracy;
    private TextView mValueProvider;
    private TextView mValueCoordX;
    private TextView mValueCoordY;
    private TextView mValueRecentUpdate;
    private TextView mValueReportSuccessCount;
    private double[] xy = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_state);
        initUI();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onViewAllRecords(View view) {
        Intent intent = new Intent(this, GpsHistoryActivity.class);
        intent.putExtra(GpsHistoryActivity.FILTER, GpsHistoryActivity.ACTIVITY_FILTER_ALL);
        startActivity(intent);
    }

    public void onViewNotSuccessRecords(View view) {
        Intent intent = new Intent(this, GpsHistoryActivity.class);
        intent.putExtra(GpsHistoryActivity.FILTER, GpsHistoryActivity.ACTIVITY_FILTER_NOT_UPLOAD);
        startActivity(intent);
    }

    private void initUI() {
        mViewTitle = (CustomTitleView) findViewById(R.id.view_title_gps);
        mValueLnt = (TextView) findViewById(R.id.value_lnt);
        mValueLat = (TextView) findViewById(R.id.value_lat);
        mValueAccuracy = (TextView) findViewById(R.id.value_accuracy);
        mValueProvider = (TextView) findViewById(R.id.value_PROVIDER);
        mValueCoordX = (TextView) findViewById(R.id.value_coordX);
        mValueCoordY = (TextView) findViewById(R.id.value_coordY);
        mValueRecentUpdate = (TextView) findViewById(R.id.value_recentUpdate);
        mValueReportSuccessCount = (TextView) findViewById(R.id.value_successReportCount);

        mViewTitle.setTitleText(R.string.my_profile_gps_state);
        Location currentLocation = CurrentLocationManager.getLocation();

        if (null != currentLocation) {
            mValueLnt.setText(String.valueOf(currentLocation.getLongitude()));
            mValueLat.setText(String.valueOf(currentLocation.getLatitude()));
            mValueAccuracy.setText(String.valueOf(currentLocation.getAccuracy()));
            mValueProvider.setText(String.valueOf(currentLocation.getProvider()));
            convertLntLat2XY(currentLocation);
            mValueCoordX.setText(String.valueOf(xy[0]));
            mValueCoordY.setText(String.valueOf(xy[1]));
            mValueRecentUpdate.setText(SessionManager.LastTimeString);
            mValueReportSuccessCount.setText(String.valueOf(SessionManager.reportSuccessCount));
        }
    }

    private void convertLntLat2XY(Location location) {
        if(null == location) {
            Toast.makeText(getApplicationContext(), R.string.my_profile_gps_no_location, Toast.LENGTH_LONG).show();
            return;
        }
        if (null != CoordTransfer.coordinateConvertor || null != CoordTransfer.coordinateConvertor.gpsTransFull) {
            xy = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude());
        }
    }
}
