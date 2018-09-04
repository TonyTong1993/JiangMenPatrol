package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;

import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.z3app.android.util.StringUtil;

public class CheckBoxInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected void setup(View contentView) {
        LinearLayout llCbContainer = (LinearLayout) contentView.findViewById(R.id.ll_cb_container);

        List<InspectItemSelectValue> selectLists = InspectItemUtil.parseSelectValues(mInspectItem);
        for (int i = 0; i < selectLists.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            checkBox.setTextColor(context.getResources().getColor(R.color.txt_black_normal));
            checkBox.setText(selectLists.get(i).name);
            checkBox.setTag(selectLists.get(i).gid);
            if (!StringUtil.isBlank(mInspectItem.getValue()) && mInspectItem.getValue().contains(selectLists.get(i).gid)) {
                checkBox.setChecked(true);
            }
            checkBox.setOnCheckedChangeListener(new MyCheckBoxOnClickListener(mInspectItem, checkBox));
            llCbContainer.addView(checkBox);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_checkbox;
    }

    private class MyCheckBoxOnClickListener implements android.widget.CompoundButton.OnCheckedChangeListener {
        private InspectItem item;
        private CheckBox checkBox;

        public MyCheckBoxOnClickListener(InspectItem item, CheckBox checkBox) {
            this.item = item;
            this.checkBox = checkBox;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String valueTemp = item.getValue();
            String cbTag = String.valueOf(checkBox.getTag());
            if (isChecked) {
                if (!StringUtil.isBlank(valueTemp) && valueTemp.contains(cbTag)) {
                    return;
                } else {
                    if (StringUtil.isBlank(valueTemp)) {
                        item.setValue(valueTemp + cbTag + ",");
                    } else {
                        item.setValue(valueTemp + "," + cbTag + ",");
                    }
                }
            } else {
                if (!StringUtil.isBlank(valueTemp) && valueTemp.contains(cbTag)) {
                    item.setValue(valueTemp.replace(cbTag + ",", ""));
                }
            }
            if (item.getValue().endsWith(",")) {
                item.setValue(item.getValue().substring(0, item.getValue().length() - 1));
            }
        }
    }
}
