package com.ecity.cswatersupply.ui.inpsectitem;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class TitleInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_title;
    }

    @Override
    protected void setup(View contentView) {
        if (!mInspectItem.isVisible()) {
            contentView.setVisibility(View.GONE);
        }

        TextView title = (TextView) contentView.findViewById(R.id.tv_item_title);
        TextView tvStar = (TextView) contentView.findViewById(R.id.tv_item_star);
        tvStar.setVisibility(View.GONE);
        title.setText(mInspectItem.getAlias());
        title.setPadding(20, 0, 0, 0);
        title.setTextSize(20);
    }
}
