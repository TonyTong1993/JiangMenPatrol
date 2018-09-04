package com.ecity.cswatersupply.ui.inpsectitem;

import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class CorsInspectItemViewXtd extends ABaseInspectItemView {

    private Button refreshBtn;
    private TextView date,accuracy,x,y,lon,lat;

    @Override
    protected void setup(View view) {
        init(view);
        initData();
        setCorsValue();
        refreshBtn.setOnClickListener(new CorsRefreshOnClickListener());
    }

    private void init(View view) {
        refreshBtn = (Button) view.findViewById(R.id.refresh);
        setItemView(view,R.id.ll_date,R.string.event_data_collection_time);
        setItemView(view,R.id.ll_accuracy,R.string.event_data_collection_hdop);
        setItemView(view,R.id.ll_x,R.string.event_data_collection_x);
        setItemView(view,R.id.ll_y,R.string.event_data_collection_y);
        setItemView(view,R.id.ll_lon,R.string.event_data_collection_lon);
        setItemView(view,R.id.ll_lat,R.string.event_data_collection_lat);

        date = (TextView) view.findViewById(R.id.ll_date).findViewById(R.id.value);
        accuracy = (TextView) view.findViewById(R.id.ll_accuracy).findViewById(R.id.value);
        x = (TextView) view.findViewById(R.id.ll_x).findViewById(R.id.value);
        y = (TextView) view.findViewById(R.id.ll_y).findViewById(R.id.value);
        lon = (TextView) view.findViewById(R.id.ll_lon).findViewById(R.id.value);
        lat = (TextView) view.findViewById(R.id.ll_lat).findViewById(R.id.value);
    }

    private void setItemView(View view, int LinLayoutId, int labelId) {
        LinearLayout linLayout = (LinearLayout) view.findViewById(LinLayoutId);
        TextView lableTxt = (TextView) linLayout.findViewById(R.id.label);
        lableTxt.setText(ResourceUtil.getStringById(labelId));
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_info_location;
    }


    private void setCorsValue() {
        JSONObject json = new JSONObject();
        try {
            json.put("x",x.getText().toString());
            json.put("y",y.getText().toString());
            json.put("lat",lat.getText().toString());
            json.put("lon",lon.getText().toString());
            json.put("hdop",accuracy.getText().toString());
            json.put("time",date.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mInspectItem.setValue(json.toString());
    }

    private class CorsRefreshOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            initData();
            setCorsValue();
        }
    }

    private void initData() {
        Location location = CurrentLocationManager.getLocation();
        String currentTime = DateUtil.getCurrentTime();
        date.setText(currentTime);
        if(null != location) {
            accuracy.setText(String.valueOf(location.getAccuracy()));
            lon.setText(String.format("%.6f", location.getLongitude()));
            lat.setText(String.format("%.6f", location.getLatitude()));
            double[] xy = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude());
            if(null != xy && xy.length == 2) {
                x.setText(String.format("%.6f", xy[0]));
                y.setText(String.format("%.6f", xy[1]));
            } else {
                ToastUtil.showShort(R.string.event_tansfer_location_error);
            }
        }
    }

}
