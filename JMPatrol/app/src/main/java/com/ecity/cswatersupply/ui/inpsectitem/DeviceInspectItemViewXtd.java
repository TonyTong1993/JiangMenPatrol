package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.ObtainDeviceOpratorXtd;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.dialog.QueryLayerControlDialog;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.esri.core.map.Graphic;
import com.z3app.android.util.StringUtil;

public class DeviceInspectItemViewXtd extends ABaseInspectItemView {
    private Graphic selectedDevice;
    private TextView tvDeviceValue;
    private String alias;
    private DialogBtnClickLstener listener;
    private QueryLayerControlDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_device_select;
    }

    @Override
    protected void setup(View contentView) {
        TextView tvGetInfo = (TextView) contentView.findViewById(R.id.tv_getDeviceInfo);
        tvDeviceValue = (TextView) contentView.findViewById(R.id.tv_device_value);
        tvGetInfo.setOnClickListener(new MyBtnGetInfoOnClickListener(context));
        alias = mInspectItem.getAlias();

        if (alias.equals(ResourceUtil.getStringById(R.string.map_select_valve_alias))) {
            tvGetInfo.setText(ResourceUtil.getStringById(R.string.map_select_valve));
        }
        if (!StringUtil.isBlank(mInspectItem.getValue())) {
            tvDeviceValue.setText(R.string.map_select_device_done);
        } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {
            tvDeviceValue.setText(mInspectItem.getDefaultValue());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RequestCode.REQUEST_DEVICE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            selectedDevice = (Graphic) data.getSerializableExtra("device");
            if (selectedDevice == null) {
                tvDeviceValue.setText("");
                setDeviceItemValue(null);
            } else {
                String dname = getDnameByGraphic(selectedDevice);
                tvDeviceValue.setText(ResourceUtil.getStringById(R.string.map_select_device_done) + dname);
                setDeviceItemValue(selectedDevice);
            }
        } else {
//            selectedDevice = null;
//            tvDeviceValue.setText("");
//            setDeviceItemValue(null);
        }
    }

    public String getDnameByGraphic(Graphic selectedDevice) {
        Map<String, Object> attributes = selectedDevice.getAttributes();
        String hiddenAttrPrefix = ResourceUtil.getStringById(R.string.map_device_hidden_attr_prefix);
        String key = hiddenAttrPrefix + "layerId";
        int layerId = (Integer) attributes.get(key);
        return QueryLayerIDs.getDnamebyLayerId(layerId);
    }

    private class MyBtnGetInfoOnClickListener implements OnClickListener {
        private Activity context;

        public MyBtnGetInfoOnClickListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (alias.equals(ResourceUtil.getStringById(R.string.map_select_valve_alias))) {
                Intent intent = new Intent(context, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MapActivity.MAP_TITLE, ResourceUtil.getStringById(R.string.map_select_valve_title));
                bundle.putString(MapActivity.DEVICE_OPERATOR, ObtainDeviceOpratorXtd.class.getName());
                putDeviceTypeInfo(v, bundle);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCode.REQUEST_DEVICE);
            } else {
                listener = new DialogBtnClickLstener();
                dialog = new QueryLayerControlDialog(context, listener);
                dialog.show();
            }
        }
    }

    private void putDeviceTypeInfo(View v, Bundle bundle) {
        String deviceType = ((TextView) v).getText().toString();
        ArrayList<String> deviceTypes = new ArrayList<String>();
        if (deviceType.equals(Constants.KEY_VALVE_TYPE_NAME)) {
            deviceTypes.add(Constants.VALUE_VALVE_TYPE_NAME);
            bundle.putStringArrayList(MapActivity.DEVICE_VALVE_OPERATOR, deviceTypes);
        }
    }

    private void setDeviceItemValue(Graphic deviceGraphic) {
        String value = "";
        if (deviceGraphic != null) {
            String hiddenAttrPrefix = HostApplication.getApplication().getString(R.string.map_device_hidden_attr_prefix);
            Map<String, Object> attributes = deviceGraphic.getAttributes();
            JSONObject deviceAttrJson = new JSONObject();
            try {
                String key = "layerId";
                int layerId = (Integer) attributes.get(hiddenAttrPrefix + key);
                deviceAttrJson.put(key, layerId);
                String displayAttrs = getReportAttrs(deviceGraphic);
                String dname = QueryLayerIDs.getDnamebyLayerId(layerId);
                if(!StringUtil.isBlank(displayAttrs)) {
                    dname = dname + " " + displayAttrs;
                }
                deviceAttrJson.put("dname", dname);
                key = "layerName";
                deviceAttrJson.put(key, attributes.get(hiddenAttrPrefix + key));
                key = "queryUrl";
                deviceAttrJson.put(key, attributes.get(hiddenAttrPrefix + key));
                deviceAttrJson.put("gid", attributes.get("gid"));
                //阀门编号
                deviceAttrJson.put(ResourceUtil.getStringById(R.string.valve_number), attributes.get(ResourceUtil.getStringById(R.string.valve_number)));
            } catch (JSONException e) {
                LogUtil.e("CustomViewInflater", e);
            }
            value = deviceAttrJson.toString();
        }

        mInspectItem.setValue(value);
    }

    public String getReportAttrs(Graphic graphic) {
        String[] arrays = ResourceUtil.getArrayById(R.array.gis_event_report_attrs);
        Map<String, Object> attributes = graphic.getAttributes();
        if( null == arrays || 0 == arrays.length || null == attributes || attributes.size() == 0 ) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arrays.length; i++) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                if (!StringUtil.isBlank(entry.getKey()) || null != entry.getValue()) {
                    String value = String.valueOf(entry.getValue());
                    if (entry.getKey().toLowerCase().contains(arrays[i].toLowerCase()) && !StringUtil.isBlank(value)) {
                        buffer.append(entry.getKey() + ":" + value+" ");
                    }
                }
            }
        }

        return buffer.toString();
    }

    private class DialogBtnClickLstener implements QueryLayerControlDialog.OnDialogBtnClickListener {

        @Override
        public void onback(boolean result, final ArrayList<Integer> ids) {
            if (result) {
                Intent intent = new Intent(context, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MapActivity.MAP_TITLE, ResourceUtil.getStringById(R.string.map_select_device_title));
                bundle.putString(MapActivity.DEVICE_OPERATOR, ObtainDeviceOpratorXtd.class.getName());
                bundle.putSerializable(Constants.QUERY_DEVICE_LAYER_IDS, ids);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCode.REQUEST_DEVICE);
            }
        }
    }
}
