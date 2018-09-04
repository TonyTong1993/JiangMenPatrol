package com.wjk.tableview;

import android.content.Context;
import android.widget.GridLayout;

import com.wjk.tableview.common.TableCellData;
import com.wjk.tableview.model.TableDataConfig;

import java.util.List;

/**
 * Created by JKWANG-PC on 2017/2/13.
 */

public abstract class TableDataAdapter {

    protected final Context context;
    private final List<TableCellData> data;

    protected GridLayout tableDataView;

    protected int tableDataViewWidth = 0;
    protected int columnCount;

    protected final TableDataConfig config;

    public TableDataAdapter(Context context, List<TableCellData> data,int columnCount,final TableDataConfig config) {
        this.context = context;
        this.data = data;
        this.columnCount = columnCount;
        if(null == config) {
            this.config = new TableDataConfig();
        } else {
            this.config = config;
        }
    }

    /***
     * 表格数据基础配置
     * @return
     */
    public TableDataConfig getConfig() {
        return config;
    }

    protected void setTableDataViewWidth(int tableDataViewWidth) {
        this.tableDataViewWidth = tableDataViewWidth;
    }

    protected void setTableDataView(GridLayout tableDataView) {
        this.tableDataView = tableDataView;
        addGridLayoutView(context,data);
    }

    protected abstract void addGridLayoutView(Context context, List<TableCellData> cellDatas);
}
