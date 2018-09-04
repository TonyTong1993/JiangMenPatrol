package com.ecity.cswatersupply.ui.inpsectitem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.PhoneUtil;
import com.z3app.android.util.StringUtil;

public class PhoneInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected void setup(View contentView) {
        final TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        setText(tvContent, mInspectItem.getValue(), mInspectItem.getDefaultValue());
        contentView.findViewById(R.id.rl_body).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PhoneUtil.call(context, (String) tvContent.getText());
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.item_phone;
    }

    private void setText(TextView v, String value, String defaultValue) {
        if (!StringUtil.isBlank(value)) {
            v.setText(value);
        } else {
            v.setText(defaultValue);
        }
    }
}
