package com.ecity.cswatersupply.ui.inpsectitem;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.z3app.android.util.StringUtil;

public abstract class ABaseInspectItemView {
    protected Activity context;
    protected InspectItem mInspectItem;
    protected CustomViewInflater mCustomInflater;

    public View inflate(Activity context, CustomViewInflater customInflater, InspectItem item) {
        this.context = context;
        this.mInspectItem = item;
        this.mCustomInflater = customInflater;

        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(getContentView(), null);
        setupTitle(item, contentView);

        setup(contentView);

        if (!item.isVisible()) {
            contentView.setVisibility(View.GONE);
        }

        return contentView;
    }

    protected abstract void setup(View contentView);

    protected abstract int getContentView();

    public final void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
        mCustomInflater.setPendingInspectItemView(null);
        CustomViewInflater.pendingViewInflater = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override in concrete class.
    }

    protected void startActivityForResult(Intent intent, int requestCode) {
        mCustomInflater.setPendingInspectItemView(this);
        CustomViewInflater.pendingViewInflater = mCustomInflater;
        context.startActivityForResult(intent, requestCode);
    }

    protected void setupTitle(InspectItem item, View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
        if (tvTitle != null) {
            if (!StringUtil.isBlank(item.getAlias())) {
                tvTitle.setText(item.getAlias());
            } else {
                tvTitle.setText(item.getName());
            }
        }

        TextView tvStar = (TextView) view.findViewById(R.id.tv_item_star);
        if (tvStar != null) {
            if (!item.isRequired() || !item.isEdit()) {
                tvStar.setVisibility(View.GONE);
            }
        }
    }

    public InspectItem getInspectItem() {
        return mInspectItem;
    }
}
