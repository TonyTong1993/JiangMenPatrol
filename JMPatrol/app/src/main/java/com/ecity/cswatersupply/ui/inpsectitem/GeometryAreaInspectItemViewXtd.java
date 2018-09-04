package com.ecity.cswatersupply.ui.inpsectitem;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.SetUpMapAreaOperator;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class GeometryAreaInspectItemViewXtd extends ABaseInspectItemView {
    private String selectedAreaValues;
    private TextView tvAreaSetStatus;

    @Override
    protected void setup(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_set_area);
        tvAreaSetStatus = (TextView) view.findViewById(R.id.tv_isset);
        showAreaSetStatus(false);
        relativeLayout.setOnClickListener(new MyBtnGetInfoOnClickListener());
        mInspectItem.setValue(selectedAreaValues);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_geometry_area;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RequestCode.SELECT_GEOMETRY_AREA) {
            return;
        }

        if (null == data) {
            return;
        }

        boolean isAreaSelected = false;
        selectedAreaValues = data.getStringExtra(SetUpMapAreaOperator.INTENT_KEY_AREA);
        if (selectedAreaValues != null) {
            JSONObject obj = JsonUtil.getJsonObject(selectedAreaValues);
            if (obj != null) {
                isAreaSelected = (obj.optJSONArray("rings").length() != 0);
            }
        }
        showAreaSetStatus(isAreaSelected);
        mInspectItem.setValue(selectedAreaValues);
    }

    private class MyBtnGetInfoOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MapActivity.MAP_TITLE, ResourceUtil.getStringById(R.string.event_report_select_area_title));
            bundle.putString(MapActivity.MAP_OPERATOR, SetUpMapAreaOperator.class.getName());
                if (null != selectedAreaValues) {
                    bundle.putString(SetUpMapAreaOperator.INTENT_KEY_AREA, selectedAreaValues);
                }
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCode.SELECT_GEOMETRY_AREA);
        }
    }

    private void showAreaSetStatus(boolean isSet) {
        int textId = isSet ? R.string.event_report_construct_area_done : R.string.event_report_construct_area_undone;
        int colorId = isSet ? R.color.punish_green : R.color.red;
        tvAreaSetStatus.setText(context.getString(textId));
        tvAreaSetStatus.setTextColor(context.getResources().getColor(colorId));
    }
}
