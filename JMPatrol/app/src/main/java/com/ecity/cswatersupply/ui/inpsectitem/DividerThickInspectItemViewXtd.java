package com.ecity.cswatersupply.ui.inpsectitem;

import android.view.View;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;

public class DividerThickInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected void setup(View contentView) {

    }

    @Override
    protected void setupTitle(InspectItem item, View view) {
        // Do nothing. No title in this type of item.
    }

    @Override
    protected int getContentView() {
        return R.layout.item_divider_thick;
    }
}
