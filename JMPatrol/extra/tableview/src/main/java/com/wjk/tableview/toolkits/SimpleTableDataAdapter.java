package com.wjk.tableview.toolkits;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.wjk.tableview.R;
import com.wjk.tableview.TableDataAdapter;
import com.wjk.tableview.common.TableCellData;
import com.wjk.tableview.model.TableDataConfig;

import java.util.List;

/**
 * Created by JKWANG-PC on 2017/2/16.
 */

public final class SimpleTableDataAdapter extends TableDataAdapter {
    private ITableViewRowClickListener mITableViewRowClickListener;

    public SimpleTableDataAdapter(Context context, List<TableCellData> data, int columnCount, final TableDataConfig config) {
        super(context, data, columnCount, config);
    }

    @Override
    protected void addGridLayoutView(Context context, List<TableCellData> cellDatas) {

        for (int i = 0, n = cellDatas.size(); i < n; ++i) {
            final TableCellData cellData = cellDatas.get(i);
            TextView view = new TextView(context);
            view.setText(cellData.getValue());
            GridLayout.Spec rowSpec = GridLayout.spec(cellData.getRow(), cellData.getRowSpan());
            GridLayout.Spec colomnSpce = GridLayout.spec(cellData.getColumn(), cellData.getColumnSpan());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colomnSpce);
            params.setGravity(Gravity.FILL);
            params.width = (int) (tableDataViewWidth * cellData.getColumnSpan() / columnCount);
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;

            if (getConfig().showCrossRow) {
                if (cellData.getRow() % 2 == 0) {
                    if (cellData.getRow() > 0) {
                        if (getConfig().showTableGrid) {
                            view.setBackgroundResource(R.drawable.tv_border_even);
                        } else {
                            view.setBackgroundResource(R.drawable.tv_border_even_nogrid);
                        }
                    } else {
                        if (getConfig().showTableGrid) {
                            view.setBackgroundResource(R.drawable.tv_border_first_row);
                        } else {
                            view.setBackgroundResource(R.drawable.tv_border_first_row_nogrid);
                        }
                    }
                } else {
                    if (getConfig().showTableGrid) {
                        view.setBackgroundResource(R.drawable.tv_border_odd);
                    } else {
                        view.setBackgroundResource(R.drawable.tv_border_odd_nogrid);
                    }
                }
            } else {
                if ( getConfig().showTableGrid ) {
                    view.setBackgroundResource(R.drawable.tv_border_odd);
                } else {
                    view.setBackgroundResource(R.drawable.tv_border_odd_nogrid);
                }
            }

            view.setGravity(cellData.getGravity());
            view.setTextSize(getConfig().textSize);
            view.setPadding(getConfig().paddingLeft, getConfig().paddingTop, getConfig().paddingRight, getConfig().paddingBottom);
            view.setTypeface(view.getTypeface(), getConfig().typeface);
            if (cellData.isLink()) {
                view.setTextColor(getConfig().linkColor);
            } else if (cellData.isKey()) {
                view.setTextColor(getConfig().keyColor);
            } else {
                view.setTextColor(getConfig().textColor);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(null != mITableViewRowClickListener ) {
                    mITableViewRowClickListener.OnRowClicked(cellData);
                }
                }
            });

            tableDataView.addView(view, params);
        }
    }

    public void setITableViewRowClickListener(ITableViewRowClickListener iTableViewRowClickListener) {
        mITableViewRowClickListener = iTableViewRowClickListener;
    }

    public interface ITableViewRowClickListener {
        void OnRowClicked(TableCellData cellData);
    }
}
