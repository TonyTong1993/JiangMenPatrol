package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.mobile.android.bdlbslibrary.BDGeoLocator;
import com.ecity.mobile.android.bdlbslibrary.BDGeoLocator.OnNotifyAddressInfo;
import com.ecity.mobile.android.bdlbslibrary.model.AddressInfo;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

public abstract class ABaiduInspectItemViewXtd extends ABaseInspectItemView {
    private PatrolPosition mPatrolPosition;
    protected EditText etAddressValue;
    protected EditText etLocationValue;
    private BDGeoLocator mBDGeoLocator;

    @Override
    public View inflate(Activity context, CustomViewInflater customInflater, InspectItem item) {
        View view = super.inflate(context, customInflater, item);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.REQUEST_POSITION) {
            if (null != data) {
                String location = data.getStringExtra(Constants.FORM_MAP_GET_CURRENT_POSITION);
                if (isValidLocationData(location)) {
                    setPatrolPosition(location);
                    setLocationAddressValue(location, null);
                }
            }
        }
    }

    protected void getMoreAddressInfoByBaiDu(Activity context, PatrolPosition mPatrolPosition) {
        etAddressValue.setEnabled(true);
        if (null == mPatrolPosition) {
            return;
        }

        LoadingDialogUtil.show(context, R.string.event_report_hint_address_loading);

        CountDownHandler mCountDownHandler = new CountDownHandler();
        try {
            if (mPatrolPosition.isLatLon) {
                double[] mercatorxy = null;
                mercatorxy = CoordTransfer.transToLocal(mPatrolPosition.x, mPatrolPosition.y);
                if (null != mercatorxy && 2 == mercatorxy.length) {
                    etLocationValue.setText(mercatorxy[0] + "," + mercatorxy[1]);
                    etAddressValue.setText("");
                }
                if (!StringUtil.isBlank(mPatrolPosition.placeName)) {
                    etAddressValue.setText(mPatrolPosition.placeName);
                } else {
                    getBDGeoLocator().startGetAddressInfoByXY(mPatrolPosition.y, mPatrolPosition.x, new MyOnNotifyAddressInfoCallback(context));
                    mCountDownHandler.postDelayed(new CountDownThread(context), 10000);
                }
            } else {
                double[] lngLat = null;
                etLocationValue.setText(mPatrolPosition.y + "," + mPatrolPosition.x);
                etAddressValue.setText("");
                lngLat = CoordTransfer.transToLatlon(mPatrolPosition.x, mPatrolPosition.y);
                if (null != lngLat && 2 == lngLat.length) {
                    if (!StringUtil.isBlank(mPatrolPosition.placeName)) {
                        etAddressValue.setText(mPatrolPosition.placeName);
                    } else {
                        getBDGeoLocator().startGetAddressInfoByXY(lngLat[0], lngLat[1], new MyOnNotifyAddressInfoCallback(context));
                        mCountDownHandler.postDelayed(new CountDownThread(context), 10000);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(etLocationValue, e);
        }
    }

    private static class CountDownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class CountDownThread implements Runnable {
        private Activity context;

        public CountDownThread(Activity context) {
            this.context = context;
        }

        @Override
        public void run() {
            LoadingDialogUtil.dismiss();
            Location location = CurrentLocationManager.getLocation();
            
            if ((etAddressValue != null) && StringUtil.isBlank(etAddressValue.getText().toString()) && location == null) {
                Toast.makeText(context, R.string.event_report_hint_address_loading_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyOnNotifyAddressInfoCallback implements OnNotifyAddressInfo {
        private Activity context;

        public MyOnNotifyAddressInfoCallback(Activity context) {
            this.context = context;
        }

        @Override
        public void getAddressInfo(boolean arg0, int arg1, String arg2, List<AddressInfo> arg3) {
            LoadingDialogUtil.dismiss();
            if (null != etAddressValue && null != etLocationValue && !ListUtil.isEmpty(arg3)&&arg3.get(0).getAddrStr()!=null) {
                setLocationAddressValue(etLocationValue.getText().toString(), arg3.get(0).getAddrStr());
            } else {
                ToastUtil.showMessage(context, R.string.event_report_hint_address_loading_fail, 0);
            }
        }
    }

    private void setLocationAddressValue(String location, String address) {
        if (!StringUtil.isBlank(address)) {
            mInspectItem.setValue(location + ";" + address);
            if (location != null) {
                etLocationValue.setText(location);
            }
            etAddressValue.setText(address);
        } else {
            mInspectItem.setValue(location);
            etLocationValue.setText(location);
        }
    }

    private void setPatrolPosition(String location) {
        if (StringUtil.isBlank(location) || null == etAddressValue) {
            return;
        }

        String[] xy = location.split(",");
        if (null != xy && 2 == xy.length) {
            if (null == mPatrolPosition) {
                mPatrolPosition = new PatrolPosition();
            }
            mPatrolPosition.x = Double.valueOf(xy[0]);
            mPatrolPosition.y = Double.valueOf(xy[1]);
            mPatrolPosition.isLatLon = false;
            getMoreAddressInfoByBaiDu(context, mPatrolPosition);
        }
    }

    private boolean isValidLocationData(String location) {
        if (!location.contains(",")) {
            return false;
        }
        String[] strArrays = location.split(",");
        if (strArrays.length != 2) {
            return false;
        }

        String regex = "[-+]?[0-9]+.*[0-9]*";
        return !(!strArrays[0].matches(regex) || !strArrays[1].matches(regex));

    }

    private BDGeoLocator getBDGeoLocator() {
        if (mBDGeoLocator == null) {
            mBDGeoLocator = new BDGeoLocator(context.getApplication());
        }

        return mBDGeoLocator;
    }

}
