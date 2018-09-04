package com.ecity.cswatersupply.utils;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.z3app.android.util.StringUtil;

public class TextUtil {
    public static void DisplayValueOfView(View v, String value) {
        if (v == null) {
            return;
        }
        if (StringUtil.isBlank(value)) {
            value = ResourceUtil.getStringById(R.string.item_empty);
        }
        if (v instanceof TextView) {
            ((TextView) v).setText(value);
        }
    }


}
