package com.ecity.cswatersupply.ui.inpsectitem;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.z3app.android.util.StringUtil;

public class TextInspectItemViewXtd extends ABaseInspectItemView {
    private static final int TEXT_MAX_LENGTH = 80;
    private static final int LONG_TEXT_MAX_LENGTH = 125;

    @Override
    protected int getContentView() {
        boolean isTextNormal = mInspectItem.getType().equals(EInspectItemType.TEXT);
        return isTextNormal ? R.layout.custom_form_item_text : R.layout.custom_form_item_text_long;
    }

    @Override
    protected void setup(View contentView) {
        if (!mInspectItem.isVisible()) {
            contentView.setVisibility(View.GONE);
        }

        setupTitle(mInspectItem, contentView);
        EditText etValue = (EditText) contentView.findViewById(R.id.et_item_value);
        if (mInspectItem.isEdit()) {
            boolean isTextNormal = mInspectItem.getType().equals(EInspectItemType.TEXT);
            int maxLength = isTextNormal ? TEXT_MAX_LENGTH : LONG_TEXT_MAX_LENGTH;
            InputFilter[] filters = { new InputFilter.LengthFilter(maxLength) };
            etValue.setFilters(filters);
        } else {
            etValue.setText(" ");
        }

        if (!StringUtil.isBlank(mInspectItem.getValue())) {
            etValue.setText(mInspectItem.getValue());
        } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {
            etValue.setText(mInspectItem.getDefaultValue());
        }
        etValue.addTextChangedListener(new MyEditTextListener(mInspectItem));
        etValue.setEnabled(mInspectItem.isEdit());
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
            // no logic to do.
        }

        @Override
        public void afterTextChanged(Editable s) {
            item.setValue(s.toString());
        }
    }
}
