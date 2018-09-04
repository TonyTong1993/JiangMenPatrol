package com.wjk.tableview.common;

import android.view.Gravity;

/**
 * Created by JKWANG-PC on 2017/2/13.
 */

public class TableCellData {
    private String value;
    private int row;
    private int column;
    private int rowSpan;
    private int columnSpan;
    private boolean isKey;
    private int gravity;
    private boolean isLink;

    public TableCellData(String value, int row, int column, boolean isKey) {
        this(value, row, column, 1, 1, false, Gravity.CENTER);
    }

    public TableCellData(String value, int row, int column, int rowSpan, int columnSpan, boolean isKey) {
        this(value, row, column, rowSpan, columnSpan, isKey, Gravity.CENTER);
    }

    public TableCellData(String value, int row, int column, int rowSpan, int columnSpan, boolean isKey, int gravity) {
        this.value = value;
        this.row = row;
        this.column = column;
        this.rowSpan = rowSpan;
        this.columnSpan = columnSpan;
        this.isKey = isKey;
        this.gravity = gravity;
    }

    public TableCellData(String value, int row, int column, int rowSpan, int columnSpan, boolean isKey, boolean isLink, int gravity) {
        this.value = value;
        this.row = row;
        this.column = column;
        this.rowSpan = rowSpan;
        this.columnSpan = columnSpan;
        this.isKey = isKey;
        this.gravity = gravity;
        this.isLink = isLink;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean link) {
        isLink = link;
    }
}
