package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.z3app.android.util.StringUtil;

public class RadioTextInspectItemViewXtd extends RadioInspectItemViewXtd {

    @Override
    protected void setup(View contentView) {
        super.setup(contentView);
        EditText etRadioValue = (EditText) contentView.findViewById(R.id.et_radio_text);
        RadioGroup rGroup = (RadioGroup) contentView.findViewById(R.id.rg_custom);
        if (!mInspectItem.isEdit()) {
            etRadioValue.setVisibility(View.GONE);
        } else {
            if (!StringUtil.isBlank(mInspectItem.getValue())) {
                String secondPart = getRadioTextSecondPartValue(mInspectItem.getValue());
                etRadioValue.setText(secondPart);
                setRadioId(rGroup);
            }
            etRadioValue.addTextChangedListener(new MyEditTextListener(mInspectItem));
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_radiogroup_text;
    }

    private class MyEditTextListener implements TextWatcher {
        private InspectItem item;

        public MyEditTextListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String newValue = s.toString();
            String firstPart = getRadioTextFirstPartValue(item.getValue());
            setRadioTextValue(firstPart, newValue);
        }
    }

    private void setRadioId(RadioGroup rGroup) {
        String firstPart = getRadioTextFirstPartValue(mInspectItem.getValue());
        List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(mInspectItem);

        for (int i = 0; i < selectValues.size(); i++) {
            InspectItemSelectValue selectValue = selectValues.get(i);
            if (firstPart.equalsIgnoreCase(selectValue.gid)) {
                RadioButton radioButton = (RadioButton) rGroup.getChildAt(i);
                radioButton.setChecked(true);
                break;
            }
        }
    }
}
