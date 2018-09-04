package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.z3app.android.util.ScreenUtil;
import com.z3app.android.util.StringUtil;

public class RadioInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected void setup(View contentView) {
        RadioGroup rGroup = (RadioGroup) contentView.findViewById(R.id.rg_custom);
        rGroup.setEnabled(mInspectItem.isEdit());

        if (!mInspectItem.isEdit()) {
            TextView valueText = (TextView) contentView.findViewById(R.id.tv_item_value);
            valueText.setVisibility(View.VISIBLE);
            rGroup.setVisibility(View.GONE);
            valueText.setText(mInspectItem.getValue());
        } else {
            setupRadioButtons(rGroup);
            rGroup.setOnCheckedChangeListener(new MyRadioGroupOnClickListener());
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_radiogroup;
    }

    private class MyRadioGroupOnClickListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempButton = (RadioButton) context.findViewById(checkedId);
            if (tempButton == null) {
                return;
            }

            String oldValue = mInspectItem.getValue();
            String secondPart = getRadioTextSecondPartValue(oldValue);
            String newValue = String.valueOf(tempButton.getTag());
            if (mInspectItem.getType() == EInspectItemType.RADIO) {
                mInspectItem.setValue(newValue);
            } else {
                setRadioTextValue(newValue, secondPart);
            }
        }
    }

    private void setupRadioButtons(RadioGroup radioGroup) {
        List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(mInspectItem);
        int size = selectValues.size();
        for (int i = 0; i < size; i++) {
            InspectItemSelectValue selectValue = selectValues.get(i);
            RadioButton tempButton = new RadioButton(context);
            tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tempButton.setTextColor(context.getResources().getColor(R.color.txt_black_normal));
            tempButton.setText(selectValue.name);
            tempButton.setTag(selectValue.gid);

            if (size == 2) {
                radioGroup.setOrientation(RadioGroup.HORIZONTAL);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i % 2 == 0) {
                    radioGroup.addView(tempButton);
                } else {
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    radioGroup.measure(w, h);
                    int measuredWidth = radioGroup.getMeasuredWidth();
                    int left = (displayMetrics.widthPixels / 2) - measuredWidth - ScreenUtil.dipTopx(context, 8);
                    layoutParams.setMargins(left, 0, 0, 0);
                    radioGroup.addView(tempButton, layoutParams);
                }
            } else {
                radioGroup.addView(tempButton);
            }

            if (mInspectItem.getValue().equalsIgnoreCase(selectValue.gid)) {
                radioGroup.check(tempButton.getId());
            }
        }
    }

    protected void setRadioTextValue(String firstPart, String secondPart) {
        mInspectItem.setValue(firstPart + "&" + secondPart);
    }

    protected String getRadioTextFirstPartValue(String oldValue) {
        if (StringUtil.isEmpty(oldValue)) {
            return "";
        }
        int index = oldValue.indexOf("&");
        return (index == -1) ? oldValue : oldValue.substring(0, index);
    }

    protected String getRadioTextSecondPartValue(String oldValue) {
        if (StringUtil.isEmpty(oldValue)) {
            return "";
        }
        int index = oldValue.indexOf("&");
        return (index == -1) ? "" : oldValue.substring(index + 1);
    }
}
