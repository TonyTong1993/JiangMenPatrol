package com.ecity.cswatersupply.ui.inpsectitem;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.DatetimePickerCallback;
import com.z3app.android.util.StringUtil;

public class DateInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_select_date;
    }

    @Override
    protected void setup(View contentView) {
        Button btnSelect = (Button) contentView.findViewById(R.id.view_value);
        if (!StringUtil.isBlank(mInspectItem.getValue())) {
            btnSelect.setText(mInspectItem.getValue());
        } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {
            btnSelect.setText(mInspectItem.getDefaultValue());
        }
        btnSelect.setOnClickListener(new MyBtnSelectOnClickListener(context, mInspectItem, btnSelect));
    }

    private class MyBtnSelectOnClickListener implements OnClickListener {
        private Button btn;
        private InspectItem item;
        private DatetimePickerDialog timeDialog;
        private Activity context;

        public MyBtnSelectOnClickListener(Activity context, InspectItem item, Button btn) {
            this.context = context;
            this.item = item;
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            timeDialog = new DatetimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new MyDateTimeCallBack(item, btn));
            LayoutParams attributes = timeDialog.getWindow().getAttributes();
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            attributes.width = (int) (metrics.widthPixels * 0.9);
            attributes.height = (int) (metrics.heightPixels * 0.6);
            attributes.flags = LayoutParams.FLAG_DIM_BEHIND;
            attributes.dimAmount = 0.5f;
            timeDialog.getWindow().setAttributes(attributes);
            timeDialog.show();
        }
    }

    private class MyDateTimeCallBack implements DatetimePickerCallback {
        private InspectItem item;
        private Button btn;

        public MyDateTimeCallBack(InspectItem item, Button btn) {
            this.item = item;
            this.btn = btn;
        }

        @Override
        public void OnOK(String input) {
            item.setValue(input);
            btn.setText(item.getValue());
        }
    }
}