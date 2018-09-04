package com.ecity.android.map.core.local;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/***
 * 
 * @author ZiZhengzhuan
 * 
 */
public class SQLiteRecordBean implements ISQLiteOper {
    private String tablename = null;
    private String tabletype = null; // 根据此变量 将RecordBean转换Graphic
    /***
     * 属性值
     */
    private Map<String, Object> attribute = new HashMap<String, Object>();

    @SuppressWarnings("unused")
    private SQLiteRecordBean() {// 不允许在外部调用

    }

    /***
     * 根据字段列表初始化属性表
     * 
     * @param tablename
     * @param tabletype
     * @param
     */
    public SQLiteRecordBean(String tablename, String tabletype) {
        this.tablename = tablename;
        this.tabletype = tabletype;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTablename() {
        return this.tablename;
    }

    public void setTabletype(String tabletype) {
        this.tabletype = tabletype;
    }

    public String getTabletype() {
        return this.tabletype;
    }

    public Map<String, Object> getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public void buildFromCursor(Cursor paramCursor) {

        try {
            String dtype = null;
            if (paramCursor.getCount() < 1)
                return;
            // 以前的做法 fields里面的字段要和paramCursor里的字段保持一致
            // 2014-05-29 现在以paramCursor为准，里面有多少个字段就往attribute中添加多少个字段
            // 游标里的字段数
            int tmpColumnCount = paramCursor.getColumnCount();
            String tmpFieldName = "";
            for (int i = 0; i < tmpColumnCount; i++) {
                tmpFieldName = paramCursor.getColumnName(i);
                if (null == tmpFieldName || tmpFieldName.equalsIgnoreCase(""))
                    continue;
                if (paramCursor.isNull(i))
                    attribute.put(tmpFieldName, null);
                else {
                    dtype = DataTypesHelper.fieldDataType(tablename,
                            tmpFieldName);
                    if (dtype.equalsIgnoreCase("INTEGER"))
                        attribute.put(tmpFieldName, paramCursor.getInt(i));
                    else if (dtype.equalsIgnoreCase("SHORT"))
                        attribute.put(tmpFieldName, paramCursor.getShort(i));
                    else if (dtype.equalsIgnoreCase("BYTE"))
                        continue;
                    else if (dtype.equalsIgnoreCase("LONG"))
                        attribute.put(tmpFieldName, paramCursor.getLong(i));
                    else if (dtype.equalsIgnoreCase("TEXT"))
                        attribute.put(tmpFieldName, paramCursor.getString(i));
                    else if (dtype.equalsIgnoreCase("BLOB"))
                        attribute.put(tmpFieldName, paramCursor.getBlob(i));
                    else if (dtype.equalsIgnoreCase("FLOAT"))
                        attribute.put(tmpFieldName, paramCursor.getFloat(i));
                    else if (dtype.equalsIgnoreCase("DOUBLE")
                            || dtype.equalsIgnoreCase("REAL"))
                        attribute.put(tmpFieldName, paramCursor.getDouble(i));
                    else if (dtype.equalsIgnoreCase("DATETIME"))
                        attribute.put(tmpFieldName, paramCursor.getString(i));
                    else
                        attribute.put(tmpFieldName, paramCursor.getString(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContentValues generateContentValues() {
        String dtype = null;
        try {
            ContentValues localContentValues = new ContentValues();
            Set<String> keys = attribute.keySet();
            if (keys != null) {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    Object value = attribute.get(key);
                    if (value != null)
                        if (!value.toString().isEmpty()
                                && !value.toString().equalsIgnoreCase("")) {

                            dtype = DataTypesHelper.fieldDataType(tablename,
                                    key);
                            if (dtype.equalsIgnoreCase("INTEGER")) {
                                localContentValues.put(key,
                                        Integer.parseInt(value.toString()));
                            } else if (dtype.equalsIgnoreCase("SHORT")) {
                                localContentValues.put(key,
                                        Integer.parseInt(value.toString()));
                            } else if (dtype.equalsIgnoreCase("BYTE"))
                                continue;
                            else if (dtype.equalsIgnoreCase("LONG")) {
                                localContentValues.put(key,
                                        Long.parseLong(value.toString()));
                            } else if (dtype.equalsIgnoreCase("TEXT")) {
                                localContentValues.put(key, value.toString());
                            } else if (dtype.equalsIgnoreCase("BLOB")) {
                                /*
                                 * ByteArrayModel byteArray =
                                 * (ByteArrayModel)attribute.get(key);
                                 */
                                byte[] bytes = (byte[]) attribute.get(key);
                                localContentValues.put(key, bytes);
                            } else if (dtype.equalsIgnoreCase("FLOAT")) {
                                localContentValues.put(key,
                                        Float.parseFloat(value.toString()));
                            } else if (dtype.equalsIgnoreCase("DOUBLE")) {
                                localContentValues.put(key,
                                        Double.parseDouble(value.toString()));
                            } else if (dtype.equalsIgnoreCase("DATETIME")) {
                                localContentValues.put(key, value.toString());
                            } else
                                localContentValues.put(key, value.toString());
                        }
                }
            }
            return localContentValues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
