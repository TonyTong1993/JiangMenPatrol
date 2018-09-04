package com.ecity.android.map.core.local;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.ecity.android.augmentedreality.data.DBWorkSpace;

public class DataTypesHelper {
    public static final Map<String, String> TYPES;
    static {
        TYPES = new HashMap<String, String>();
        TYPES.put("byte", "BYTE");
        TYPES.put("boolean", "INTEGER");
        TYPES.put("short", "SHORT");
        TYPES.put("int", "INTEGER");
        TYPES.put("long", "LONG");
        TYPES.put("string", "TEXT");
        TYPES.put("byte[]", "BLOB");
        TYPES.put("blob", "BLOB");
        TYPES.put("float", "FLOAT");
        TYPES.put("double", "DOUBLE");
        TYPES.put("date", "DATETIME");
        TYPES.put("varchar", "VARCHAR");
        TYPES.put("nvarchar", "NVARCHAR");
        TYPES.put("numerice", "NUMERIC");
    }

    @SuppressLint("DefaultLocale")
    public static String fieldDataType(String fieldType) {
        String tmp = fieldType;
        tmp = tmp.toLowerCase();
        if (tmp.contains("float"))
            tmp = "float";
        else if (tmp.contains("int"))
            tmp = "int";
        else if (tmp.contains("bool"))
            tmp = "boolean";
        else if (tmp.contains("double") || tmp.contains("real"))
            tmp = "double";
        else if (tmp.contains("date"))
            tmp = "date";
        // 这个地方一定是先判断是否为
        else if (tmp.contains("nvarchar"))
            tmp = "nvarchar";
        else if (tmp.contains("varchar"))
            tmp = "varchar";
        else if (tmp.contains("numerice"))
            tmp = "numerice";
        String type = TYPES.get(tmp);
        if (type == null)
            type = "TEXT";
        return type;
    }

    /***
     * 判断是不是有效的数据类型
     * 
     * @param type
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static boolean isValidDataType(String type) {
        if (null == type)
            return false;

        type.toUpperCase();
        if (type.equalsIgnoreCase("TEXT"))
            return true;
        else if (type.equalsIgnoreCase("INTEGER"))
            return true;
        else if (type.equalsIgnoreCase("FLOAT"))
            return true;
        else if (type.equalsIgnoreCase("BOOLEAN"))
            return true;
        else if (type.equalsIgnoreCase("CLOB"))
            return true;
        else if (type.equalsIgnoreCase("BLOB"))
            return true;
        else if (type.equalsIgnoreCase("TIMESTAMP"))
            return true;
        else if (type.equalsIgnoreCase("LONG"))
            return true;
        else if (type.equalsIgnoreCase("SHORT"))
            return true;
        else if (type.equalsIgnoreCase("DATETIME"))
            return true;
        else if (type.contains("VARCHAR"))
            return true;
        else if (type.contains("NVARCHAR"))
            return true;
        else if (type.contains("NUMERIC"))
            return true;
        else if (type.contains("VARYING CHARACTER"))
            return true;
        else if (type.contains("NATIONAL VARYING CHARACTER"))
            return true;
        else if (type.equalsIgnoreCase("REAL"))
            return true;
        return false;
    }

    /***
     * 根据表名、表类型、字段名 获得某个字段的数据类型
     * 
     * @param tableName
     * @param
     * @param fieldName
     * @return
     * @throws Exception
     */
    @SuppressLint("DefaultLocale")
    public static String fieldDataType(String tableName, String fieldName)
            throws Exception {
        String dtype = null;
        ArrayList<TableModel> tableModels = PipeDB.getInstance()
                .getTaskPlanTables();

        if (null == tableModels) {
            return "TEXT";
        }
        for (int i = 0; i < tableModels.size(); i++) {
            if (tableName.equals(tableModels.get(i).getName())) {
                for (int j = 0; j < tableModels.get(i).getFields().size(); j++) {
                    if (fieldName.equals(tableModels.get(i).getFields().get(j)
                            .getName())) {
                        String tmp = tableModels.get(i).getFields().get(j)
                                .getType();
                        tmp = tmp.toLowerCase();
                        if (tmp.contains("float"))
                            tmp = "float";
                        else if (tmp.contains("int"))
                            tmp = "int";
                        else if (tmp.contains("bool"))
                            tmp = "boolean";
                        else if (tmp.contains("double"))
                            tmp = "double";
                        else if (tmp.contains("real"))
                            tmp = "real";
                        else if (tmp.contains("date"))
                            tmp = "date";
                        // 这个地方一定是先判断是否为nvarchar
                        else if (tmp.contains("nvarchar"))
                            tmp = "nvarchar";
                        else if (tmp.contains("varchar"))
                            tmp = "varchar";
                        else if (tmp.contains("numerice"))
                            tmp = "numerice";
                        dtype = DataTypesHelper.TYPES.get(tmp);
                        if (dtype == null)
                            dtype = "TEXT";
                    }
                    if (dtype != null)
                        break;
                }
            }
        }

        return dtype;
    }

    /**
     * 此方法描述的是： 根据一直表结构获取 字段类型
     * 
     * @param tableModel
     * @param fieldName
     * @return
     * @throws Exception
     *             return String
     * @author : wangliu94@163.com
     * @version : 2014-7-17 下午12:45:59
     */

    public static String fieldDataType(TableModel tableModel, String fieldName)
            throws Exception {
        String dtype = null;
        if (null == PipeDB.getInstance().getTaskPlanTables())
            return "TEXT";

        for (int j = 0; j < tableModel.getFields().size(); j++) {
            if (fieldName.equals(tableModel.getFields().get(j).getName())) {
                String tmp = tableModel.getFields().get(j).getType();
                tmp = tmp.toLowerCase();
                if (tmp.contains("float"))
                    tmp = "float";
                else if (tmp.contains("int"))
                    tmp = "int";
                else if (tmp.contains("bool"))
                    tmp = "boolean";
                else if (tmp.contains("double"))
                    tmp = "double";
                else if (tmp.contains("real"))
                    tmp = "real";
                else if (tmp.contains("date"))
                    tmp = "date";
                // 这个地方一定是先判断是否为nvarchar
                else if (tmp.contains("nvarchar"))
                    tmp = "nvarchar";
                else if (tmp.contains("varchar"))
                    tmp = "varchar";
                else if (tmp.contains("numerice"))
                    tmp = "numerice";
                dtype = DataTypesHelper.TYPES.get(tmp);
                if (dtype == null)
                    dtype = "TEXT";
            }
            if (dtype != null)
                break;

        }

        return dtype;
    }

    /***
     * 从字段对象获得字段数据类型
     * 
     * @param field
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String fieldDataType(FieldModel field) {
        if (null == field)
            return "";
        String dtype = null;
        String tmp = field.getType();
        tmp = tmp.toLowerCase();
        if (tmp.contains("float"))
            tmp = "float";
        else if (tmp.contains("int"))
            tmp = "int";
        else if (tmp.contains("bool"))
            tmp = "boolean";
        else if (tmp.contains("double"))
            tmp = "double";
        else if (tmp.contains("date"))
            tmp = "date";
        // 这个地方一定是先判断是否为nvarchar
        else if (tmp.contains("nvarchar"))
            tmp = "nvarchar";
        else if (tmp.contains("varchar"))
            tmp = "varchar";
        else if (tmp.contains("numerice"))
            tmp = "numerice";
        dtype = DataTypesHelper.TYPES.get(tmp);
        if (dtype == null)
            dtype = "TEXT";
        return dtype;
    }

    /***
     * 根据表名、表类型、字段名 获得一个字段对象
     * 
     * @param tableName
     * @param tabletype
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static FieldModel getField(String tableName, String tabletype,
            String fieldName) throws Exception {
        FieldModel field = null;
        ArrayList<TableModel> tableModels = PipeDB.getInstance()
                .getTaskPlanTables();

        if (null == tableModels) {
            return null;
        }
        for (int i = 0; i < tableModels.size(); i++) {
            if (tableName.equals(tableModels.get(i).getName())
            /*
             * &&tabletype.equals(DBWorkSpace.getInstance().getTaskPlanTables().get
             * (i).getType())
             */) {
                for (int j = 0; j < tableModels.get(i).getFields().size(); j++) {
                    if (fieldName.equals(tableModels.get(i).getFields().get(j)
                            .getName())) {
                        field = tableModels.get(i).getFields().get(j);
                        break;
                    }
                }
                if (field != null)
                    break;
            }
        }
        return field;
    }
}
