package com.ecity.cswatersupply.ui.inpsectitem;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;

public class NumberInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_number;
    }

    @Override
    protected void setup(View contentView) {
        TextView tvUnit = (TextView) contentView.findViewById(R.id.tv_unit);
        tvUnit.setVisibility(View.VISIBLE);

        if (!StringUtil.isBlank(mInspectItem.getDefaultValue()) && mInspectItem.getDefaultValue().contains(":")) {
            tvUnit = (TextView) contentView.findViewById(R.id.tv_unit);
            tvUnit.setText(mInspectItem.getDefaultValue().substring(mInspectItem.getDefaultValue().indexOf(":") + 1, mInspectItem.getDefaultValue().length()));
            tvUnit.setVisibility(View.VISIBLE);
        }

        EditText etValueNumber = (EditText) contentView.findViewById(R.id.et_item_value_number);
        if (!mInspectItem.isEdit()) {
            etValueNumber.setText(mInspectItem.getValue());
        } else {
            etValueNumber.addTextChangedListener(new MyNumberEditTextListener(context, mInspectItem));
            etValueNumber.setHint(ResourceUtil.getStringById(R.string.str_clickinput));
            String defaultValue = mInspectItem.getDefaultValue();
            String value = "";
            if (!StringUtil.isBlank(mInspectItem.getValue())) {
                value = mInspectItem.getValue();
            } else if (!StringUtil.isBlank(defaultValue)) {
                if (defaultValue.contains(":")) {
                    value = defaultValue.substring(0, defaultValue.indexOf(":"));
                } else {
                    value = defaultValue;
                }
            }
            etValueNumber.setText(value);
        }
        etValueNumber.setEnabled(mInspectItem.isEdit());
    }

    private class MyNumberEditTextListener implements TextWatcher {
        private InspectItem item;
        private CharSequence temp;
        private final int charMaxNum = 10;
        private Context context;

        public MyNumberEditTextListener(Context context, InspectItem item) {
            this.item = item;
            this.context = context;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no logic to do.
        }

        @Override
        public void afterTextChanged(Editable s) {
            setLimitDigital(s);
            if (temp.length() >= charMaxNum) {
                Toast.makeText(context, ResourceUtil.getStringById(R.string.over_input_length), Toast.LENGTH_LONG).show();
            }
        }

        private void setLimitDigital(Editable s) {
            int len = s.toString().length();
            if (len == 1 && s.toString().equals("0")) {
                s.clear();
            }
            item.setValue(s.toString());
        }
    }
}
