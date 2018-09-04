package com.ecity.cswatersupply.ui.inpsectitem;

import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class SectionTitleInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_section_title;
    }

    @Override
    protected void setup(View contentView) {
        if (!mInspectItem.isVisible()) {
            contentView.setVisibility(View.GONE);
        }

        setupTitle(mInspectItem, contentView);
        TextView tvTitle = (TextView) contentView.findViewById(R.id.tv_item_title);
        tvTitle.setTextColor(context.getResources().getColor(R.color.white));
    }
}
