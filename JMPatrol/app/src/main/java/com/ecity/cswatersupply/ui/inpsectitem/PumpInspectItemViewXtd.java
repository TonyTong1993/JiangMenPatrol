package com.ecity.cswatersupply.ui.inpsectitem;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;
import com.ecity.cswatersupply.ui.activities.PumpSelectActivity;
import com.z3app.android.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class PumpInspectItemViewXtd extends ABaseInspectItemView {

    public static final String INTENT_FROM_PUMP_INSPECT = "isFromPumpInspectItem";
    private RelativeLayout pumpSelect;
    private TextView tvPump, tvPumpRoad;

    @Override
    protected void setup(View view) {
        pumpSelect = (RelativeLayout) view.findViewById(R.id.rl_pump_select);
        tvPump = (TextView) view.findViewById(R.id.tv_pump);
        tvPumpRoad = (TextView) view.findViewById(R.id.tv_pump_road);


        if (mInspectItem.isEdit()) {
            pumpSelect.setOnClickListener(new PumpSelectOnClickListener());
        } else {
            tvPump.setText(" ");
            tvPumpRoad.setText(" ");
        }

        PumpInsSelectValue selectValue = null;
        if (!StringUtil.isBlank(mInspectItem.getValue())) {

        } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {

        }
        if (null == selectValue) {
            return;
        }
        tvPump.setText(selectValue.getAlias());
        tvPumpRoad.setText(selectValue.getPumpRoad());
        tvPump.setEnabled(mInspectItem.isEdit());
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_select_pump;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PumpSelectActivity.REQPUMP) {
            if (null != data) {
                PumpInsSelectValue selectValue = (PumpInsSelectValue) data.getSerializableExtra(PumpSelectActivity.SELECT_PUMP);
                tvPump.setText(selectValue.getAlias());
                if (isblank(selectValue.getPumpRoad())) {
                    tvPumpRoad.setText("无地址");
                }
                tvPumpRoad.setText(selectValue.getPumpRoad());
                setPumpValue(selectValue);
            }
        }
    }

    private void setPumpValue(PumpInsSelectValue selectValue) {
        if (null == selectValue) {
            return;
        }

        if (StringUtil.isBlank(selectValue.getName())) {
            mInspectItem.setValue("");
        } else {
            String value = "";
            if (isblank(selectValue.getX()) || isblank(selectValue.getY())) {
                value = selectValue.getName() + Constants.SEPARATOR + selectValue.getPumpRoad() + Constants.SEPARATOR + selectValue.getGid() + Constants.SEPARATOR + selectValue.getPumpNO();
            } else {
                value = selectValue.getX() + "," + selectValue.getY() + ";" + selectValue.getName() + Constants.SEPARATOR + selectValue.getPumpRoad() + Constants.SEPARATOR + selectValue.getGid() + Constants.SEPARATOR + selectValue.getPumpNO();
            }

            mInspectItem.setValue(value);
        }
    }

    private class PumpSelectOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PumpSelectActivity.class);
            intent.putExtra(INTENT_FROM_PUMP_INSPECT, true);
            startActivityForResult(intent, PumpSelectActivity.REQPUMP);
        }
    }

    private boolean isblank(String str) {
        return (StringUtil.isBlank(str) || "".equals(str));
    }


    public static String[] constructPumpParams(JSONObject json, String str) {
        String[] str1 = {"pump_name", "pump_address", "pump_gid", "pump_no"};

        if (StringUtil.isBlank(str)) {
            return null;
        }

        String fixStr = str;
        int firstIndex = str.indexOf(";");

        if (firstIndex > 0) {
            String position = str.substring(0, firstIndex);
            if (isValidLocationData(position)) {
                try {
                    fixStr = str.substring(firstIndex + 1, str.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String strArray[] = fixStr.split(Constants.SEPARATOR);

        try {
            if (strArray.length >= 4) {
                json.putOpt(str1[0], strArray[0]);
                json.putOpt(str1[1], strArray[1]);
                json.putOpt(str1[2], strArray[2]);
                json.putOpt(str1[3], strArray[3]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strArray;
    }

    private static boolean isValidLocationData(String location) {
        if (null == location || !location.contains(",")) {
            return false;
        }
        String[] strArrays = location.split(",");
        if (null == strArrays || strArrays.length != 2) {
            return false;
        }

        String regex = "[-+]?[0-9]+.*[0-9]*";
        return !(!strArrays[0].matches(regex) || !strArrays[1].matches(regex));

    }

}
