package com.ecity.cswatersupply.ui.inpsectitem;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.ObtainLocationOpratorXtd;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

public class GeometryInspectItemViewXtd extends ABaiduInspectItemViewXtd {
    private PatrolPosition mPatrolPosition;

    @Override
    protected int getContentView() {
        return mInspectItem.isEdit() ? R.layout.custom_form_item_geometry_select : R.layout.custom_form_item_text;
    }

    @Override
    public void setup(View contentView) {
        if (mInspectItem.isEdit()) {
            TextView tvGetInfo = (TextView) contentView.findViewById(R.id.tv_getMapLocationInfo);
            TextView tvGetAddress = (TextView) contentView.findViewById(R.id.tv_getCurrentLocationInfo);
            etLocationValue = (EditText) contentView.findViewById(R.id.et_location_value);
            etAddressValue = (EditText) contentView.findViewById(R.id.et_address_value);
            tvGetInfo.setOnClickListener(new MyBtnGetInfoOnClickListener(context)); //当前定位
            tvGetAddress.setOnClickListener(new MyBtnGetInfoOnClickListener(context)); //地图选点
            etAddressValue.addTextChangedListener(new EditChangedListener(mInspectItem));
            etLocationValue.setVisibility(View.GONE);

            if (!StringUtil.isBlank(mInspectItem.getValue())) {
                String value = mInspectItem.getValue();
                setDefultValue(value);
            } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {
                String defultValue = mInspectItem.getDefaultValue();
                setDefultValue(defultValue);
            }
            if (null != mPatrolPosition) {
                getMoreAddressInfoByBaiDu(context, mPatrolPosition);
            }
        } else {
            setReadOnlyView(contentView, mInspectItem);
        }
    }

    /***
     * 加载默认值
     * @param value
     */
    private void setDefultValue(String value) {
        if (value.contains(";")) {
            String[] xyAddress = value.split(";");
            if (null == xyAddress || xyAddress.length < 2) {
                etLocationValue.setText(value);
            } else {
                etLocationValue.setText(xyAddress[0]);
                etAddressValue.setText(xyAddress[1]);
            }
        } else {
            etLocationValue.setText(value);
        }
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

    /***
     * 检查项不可编辑时调用
     * @param contentView
     * @param item
     */
    private void setReadOnlyView(View contentView, InspectItem item) {
        EditText etValue = (EditText) contentView.findViewById(R.id.et_item_value);
        etValue.setEnabled(item.isEdit());
        etValue.setHint("");
        String value = item.getValue();
        if (value.equalsIgnoreCase(";")) {
            etValue.setText("");
        } else if (value.contains(";")) {
            String[] xyAddress = value.split(";");
            if (null == xyAddress || xyAddress.length < 2) {
                etValue.setText("");
            } else {
                etValue.setText(xyAddress[1]);
            }
        } else {
            etValue.setText(value);
        }
    }

    private class MyBtnGetInfoOnClickListener implements OnClickListener {
        private Activity context;

        public MyBtnGetInfoOnClickListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_getMapLocationInfo:
                    Intent intent = new Intent(context, MapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(MapActivity.MAP_OPERATOR, ObtainLocationOpratorXtd.class.getName());
                    bundle.putString(MapActivity.MAP_TITLE, HostApplication.getApplication().getString(R.string.map_select_title));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RequestCode.REQUEST_POSITION);
                    break;
                case R.id.tv_getCurrentLocationInfo:
                    Location location = CurrentLocationManager.getLocation();
                    if (null != location) {
                        PatrolPosition mPatrolPosition = new PatrolPosition(true, location.getLatitude(), location.getLongitude(), "");
                        getMoreAddressInfoByBaiDu(context, mPatrolPosition);
                    } else {
                        Toast.makeText(context, R.string.event_reprot_hint_address_no_current_location, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class EditChangedListener implements TextWatcher {
        private InspectItem inspectItem;

        public EditChangedListener(InspectItem inspectItem) {
            super();
            this.inspectItem = inspectItem;
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            String locationAddress = inspectItem.getValue();
            if (StringUtil.isEmpty(locationAddress)) {
                locationAddress = "0,0;No Dataset";
            }
            String location = "";
            String[] array = locationAddress.split(";");
            if (null != array && array.length >= 1) {
                location = array[0];
            }

            String newAddress = arg0.toString();
            if (StringUtil.isEmpty(newAddress)) {
                newAddress = "";
            }

            mInspectItem.setValue(location + ";" + newAddress);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }

    private void setLocationAddressValue(String location, String address) {
        if (StringUtil.isEmpty(location)) {
            location = "";
        }
        if (StringUtil.isEmpty(address)) {
            address = "";
        }

        etLocationValue.setText(location);
        etAddressValue.setText(address);
        mInspectItem.setValue(location + ";" + address);
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
        if (null ==strArrays || strArrays.length != 2) {
            return false;
        }

        String regex = "[-+]?[0-9]+.*[0-9]*";
        return !(!strArrays[0].matches(regex) || !strArrays[1].matches(regex));

    }
}
