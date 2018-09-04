package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.z3app.android.util.StringUtil;

public class ToggleInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected void setup(View contentView) {
        ToggleButton toggle = (ToggleButton) contentView.findViewById(R.id.toggle_value);
        RelativeLayout rlTogle = (RelativeLayout) contentView.findViewById(R.id.rl_toggle);
        if (!mInspectItem.isEdit()) {//不可编辑，则直接设置值
            TextView valueText = (TextView) contentView.findViewById(R.id.tv_item_value);
            valueText.setVisibility(View.VISIBLE);
            rlTogle.setVisibility(View.GONE);
            valueText.setText(mInspectItem.getValue());
        } else {
            toggle.setOnCheckedChangeListener(new MyOnToggleButtonClickedListener(mInspectItem));
            setToggleElementValue(mInspectItem, toggle);
        }
        toggle.setEnabled(mInspectItem.isEdit());
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_toggle;
    }

    private void setToggleElementValue(InspectItem inspectItem, ToggleButton toggleButton) {
        if (StringUtil.isBlank(inspectItem.getValue())) {
            toggleButton.setChecked(true); // 切换一下值，触发监听器设InspectItem的值
            toggleButton.setChecked(false);
            return;
        }

        List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(inspectItem);
        for (InspectItemSelectValue selection : selectValues) {
            if (inspectItem.getValue().equals(selection.gid)) {
                boolean isChecked = (selectValues.indexOf(selection) == 0); // We assume the first element means YES.
                toggleButton.setChecked(isChecked);
                break;
            }
        }
    }

    private class MyOnToggleButtonClickedListener implements CompoundButton.OnCheckedChangeListener {
        private InspectItem inspectItem;

        public MyOnToggleButtonClickedListener(InspectItem inspectItem) {
            this.inspectItem = inspectItem;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(inspectItem);
            if (ListUtil.isEmpty(selectValues)) {
                return;
            }
            int valueIndex = isChecked ? 0 : 1;
            inspectItem.setValue(selectValues.get(valueIndex).gid);
            buttonView.setBackgroundResource(isChecked ? R.drawable.btn_on : R.drawable.btn_off);
        }
    }
}
