package com.wjk.tableview.toolkits;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjk.tableview.TableHeaderAdapter;
import com.wjk.tableview.common.TableHeaderColumnModel;
import com.wjk.tableview.model.TableHeaderConfig;
import com.wjk.tableview.widget.BorderTextView;


/**
 * Simple implementation of the {@link TableHeaderAdapter}. This adapter will render the given header
 * Strings as {@link TextView}.
 */
public final class SimpleTableHeaderAdapter extends TableHeaderAdapter {


    public SimpleTableHeaderAdapter(Context context, TableHeaderColumnModel columnModel, final TableHeaderConfig config) {
        super(context, columnModel, config);
    }

    @Override
    public View getHeaderView(final int columnIndex, final ViewGroup parentView) {
        final BorderTextView textView = new BorderTextView(getContext());

        if (columnIndex < columnModel.getColumnCount()) {
            textView.setText(columnModel.getColumnValue(columnIndex));
        }

        textView.setPadding(getConfig().paddingLeft, getConfig().paddingTop, getConfig().paddingRight, getConfig().paddingBottom);
        textView.setTypeface(textView.getTypeface(), getConfig().typeface);
        textView.setTextSize(getConfig().textSize);
        textView.setTextColor(getConfig().textColor);
        textView.setBorderColor(getConfig().borderColor);
        textView.setSrokeWidth(getConfig().srokeWidth);
        textView.setGravity(getConfig().gravity);

        return textView;
    }
}
