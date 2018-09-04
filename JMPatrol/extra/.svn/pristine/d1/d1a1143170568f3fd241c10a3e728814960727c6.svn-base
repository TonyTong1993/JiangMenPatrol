package com.ecity.android.map.core.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ecity.android.map.core.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * 系统数据库 1、这个类所创建的数据库名只有一个，而且不保存在sd卡上，随着系统的卸载而删除也会通过系统
 * 应用程序清除数据功能而删除，数据库未加密，root权限的手机能看到具体内容 2、通过传入TableModel模型来创建数据库
 * 3、通过传入表名和查询条件来获得查询记录，记录以SQLiteRecordBean模型存储，使用时需要转成
 * 具体的模型（新建一个模型对象，再从SQLiteRecordBean对象里去属性值）。
 * 
 * @author ZiZhengzhuan
 * @version v1.0 <br>
 *          2014年5月29日
 */
public class PipeDBOper {
    protected SQLiteDatabase db;

    /***
     * 在指定路径创打开数据库，如果数据库不存在则建数据库
     * 
     * @deprecated 这个功能不建议用于创建应用程序的系统数据库
     * @说明 这个功能不建议用于创建应用程序的系统数据库
     * @param dbpath
     * @return
     * @throws Exception
     */
    public boolean openDatabase(String dbpath) {
        File file = new File(dbpath);
        File path = new File(file.getParent());
        if (!path.exists())
            path.mkdirs();
        if (file.exists()) {
            try {
                if (db != null && db.isOpen())
                    db.close();
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                if (db != null && db.isOpen())
                    return true;
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                e.getMessage();
            }
        } else {
        }
        return false;
    }

    /***
     * 在指定路径创打开数据库，如果数据库不存在则建数据库
     * 
     * @deprecated 这个功能不建议用于创建应用程序的系统数据库
     * @说明 这个功能不建议用于创建应用程序的系统数据库
     * @param dbpath
     * @return
     * @throws Exception
     */
    public boolean createDatabase(String dbpath) {
        File file = new File(dbpath);
        File path = new File(file.getParent());
        if (!path.exists())
            path.mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                e.getMessage();
            }
            try {
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                if (db != null && db.isOpen()) {
                    db.close();
                    return true;
                }
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                e.getMessage();
            }
        } else {
        }
        return true;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.data
     * @method getDatabaseTablesInfo
     * @function 获取数据库表结构
     * @return
     */
    public ArrayList<SQLiteRecordBean> getDatabaseTablesInfo() {
        ArrayList<SQLiteRecordBean> beanList = new ArrayList<SQLiteRecordBean>();
        if (!db.isOpen())
            return beanList;
        String basicSQL = "select name,type,sql from sqlite_master where type='table' and (name ='grpdata' or name ='lsymbol' or name ='psymbol' "
                + "or name ='metainfo' or name like '%_lin%' or name like '%_nod%' or name like '%_mt%' or name like '%_updatelog%') order by name";
        Cursor localCursor = db.rawQuery(basicSQL, null);
        // 没有查询到记录
        if (localCursor.getCount() <= 0) {
            localCursor.close();
            return beanList;
        }
        localCursor.moveToFirst();
        while (!localCursor.isAfterLast()) {
            if (localCursor.getString(0) == null
                    || localCursor.getString(0).equalsIgnoreCase(""))
                continue;
            SQLiteRecordBean bean = new SQLiteRecordBean(
                    localCursor.getString(0), localCursor.getString(1));
            String sql = localCursor.getString(2);
            // 去除回车和换行符
            sql = sql.replace("\r\n", "");
            // 去除[
            sql = sql.replace("[", "");
            // 去除]
            sql = sql.replace("]", "");
            bean.getAttribute().put("sql", sql);
            beanList.add(bean);
            localCursor.moveToNext();
        }
        localCursor.close();
        return beanList;
    }

    // /***
    // * 创建一个表
    // *
    // * @return
    // */
    // public boolean creatTable(TableModel table) {
    // if (null == table) {
    // return false;
    // }
    // try {
    // db.execSQL(CreatTableHelper.ConstructsCreateTableString(table));
    // } catch (SQLException e) {
    // e.printStackTrace();
    // return false;
    // }
    // return true;
    // }

    /***
     * 删除一个表
     * 
     * @return
     */
    public boolean deleteTable(String tableName) throws Exception {
        if (null == tableName || tableName.equalsIgnoreCase(""))
            return false;
        try {
            db.execSQL("drop table " + tableName);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /***
     * 向表格插入一条记录
     * 
     * @param tableName
     *            表名
     * @param bean
     *            记录
     * @return
     */
    public boolean insertNewRecord(String tableName, SQLiteRecordBean bean)
            throws Exception {
        if (null == bean || null == tableName || tableName.equalsIgnoreCase(""))
            return false;
        try {
            ContentValues localContentValues = bean.generateContentValues();
            if (localContentValues == null)
                return false;
            db.insert(tableName, null, localContentValues);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /***
     * 更新某个表格的一条记录
     * 
     * @param tableName
     *            表名
     * @param bean
     *            记录值
     * @param where
     *            条件 注意：当条件为空时所有记录都会被更新为相同值
     * @return
     */
    public boolean updateRecord(String tableName, SQLiteRecordBean bean,
            String where) throws Exception {
        if (null == bean || null == tableName || tableName.equalsIgnoreCase(""))
            return false;
        try {
            db.update(tableName, bean.generateContentValues(), where, null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /*
     * public boolean insertOrUpdate(String tableName,SQLiteRecordBean
     * bean)throws Exception{ if ( null==bean ||null ==
     * tableName||tableName.equalsIgnoreCase("")) return false; try {
     * ContentValues localContentValues = bean.generateContentValues();
     * localContentValues = if(localContentValues == null) return false;
     * db.replaceOrThrow(tableName, null,localContentValues); return true; }
     * catch (SQLException e) { e.printStackTrace(); throw new
     * Exception(e.getMessage()); }
     * 
     * 
     * }
     */
    /***
     * 删除符合条件的记录
     * 
     * @param tableName
     * @param where
     *            当条件为空时，会删除整个表
     * @return
     */
    public boolean deleteRecord(String tableName, String where)
            throws Exception {
        if (null == tableName)
            return false;
        if (StringUtil.isEmpty(where))
            deleteAllRecord(tableName);
        try {
            db.delete(tableName, where, null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /***
     * 删除所有记录
     * 
     * @param tableName
     * @return
     */
    public boolean deleteAllRecord(String tableName) throws Exception {
        if (null == tableName || tableName.equalsIgnoreCase(""))
            return false;
        try {
            db.execSQL("delete from " + tableName);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /***
     * 执行一条SQL语句
     * 
     * @param sql
     * @return
     */
    public boolean execSQL(String sql) throws Exception {
        if (null == sql)
            return false;
        try {
            db.execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /***
     * 查询数据库
     * 
     * @param tableName
     * @param where
     *            查询条件 当条件为空时，查询整个表的所有记录
     * @return
     * @throws Exception
     */
    public List<SQLiteRecordBean> queryWithWhere(String tableName, String where)
            throws Exception {
        if (null == tableName || tableName.equalsIgnoreCase(""))
            return null;
        List<SQLiteRecordBean> rlist = new ArrayList<SQLiteRecordBean>();
        try {
            String sql = "";
            if (null == where)
                sql = "select * from " + tableName;
            else
                sql = "SELECT  * FROM " + tableName + " WHERE " + where;

            Cursor localCursor = db.rawQuery(sql, null);
            localCursor.moveToFirst();
            if (localCursor.getCount() < 1) {
                localCursor.close();
                return rlist;
            }
            while (true) {
                SQLiteRecordBean bean = new SQLiteRecordBean(tableName, "table");
                bean.buildFromCursor(localCursor);
                rlist.add(bean);
                if (!localCursor.moveToNext()) {
                    localCursor.close();
                    return rlist;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @package com.ecity.android.cityrevisionpo.data
     * @method queryWithWhere
     * @function 查询表的所有记录
     * @param tableName
     * @param where
     * @param tableType
     * @return
     * @throws Exception
     */
    public List<SQLiteRecordBean> queryWithWhere(String tableName,
            String where, String tableType) throws Exception {
        if (null == tableName || tableName.equalsIgnoreCase(""))
            return null;
        List<SQLiteRecordBean> rlist = new ArrayList<SQLiteRecordBean>();
        try {
            String sql = "";
            if (null == where)
                sql = "select * from " + tableName;
            else
                sql = "SELECT  * FROM " + tableName + " WHERE " + where;

            Cursor localCursor = db.rawQuery(sql, null);
            localCursor.moveToFirst();
            if (localCursor.getCount() < 1) {
                localCursor.close();
                return rlist;
            }
            while (true) {
                SQLiteRecordBean bean = new SQLiteRecordBean(tableName,
                        tableType);
                bean.buildFromCursor(localCursor);
                rlist.add(bean);
                if (!localCursor.moveToNext()) {
                    localCursor.close();
                    return rlist;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /***
     * 根据SQL语句进行查询
     * 
     * @param sql
     * @return
     * @throws Exception
     */
    public List<SQLiteRecordBean> execQuerySQL(String sql, String tableType)
            throws Exception {
        if (null == sql || sql.equalsIgnoreCase(""))
            return null;
        String tableName = "";
        String tmp = sql.replaceAll(" ", ";");
        String[] arr = tmp.split(";");

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equalsIgnoreCase("FROM"))
                if (i + 1 < arr.length) {
                    if (StringUtil.isEmpty(arr[i + 1]))
                        for (int k = i + 1; k < arr.length; k++) {
                            if (!arr[k].equalsIgnoreCase("")) {
                                tableName = arr[k];
                                break;
                            }
                        }
                    else
                        tableName = arr[i + 1];

                    if (!StringUtil.isEmpty(tableName))
                        break;
                }
        }
        if (tableName.equalsIgnoreCase(""))
            return null;

        List<SQLiteRecordBean> rlist = new ArrayList<SQLiteRecordBean>();
        try {
            Cursor localCursor = db.rawQuery(sql, null);
            localCursor.moveToFirst();
            if (localCursor.getCount() < 1) {
                localCursor.close();
                return rlist;
            }

            while (true) {
                SQLiteRecordBean bean = new SQLiteRecordBean(tableName,
                        tableType);
                bean.buildFromCursor(localCursor);
                rlist.add(bean);
                if (!localCursor.moveToNext()) {
                    localCursor.close();
                    return rlist;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @package com.ecity.android.cityrevisionpo.data
     * @method queryTableMaxGIDbyNameandWhere
     * @function 获取初始时点表和线表最大GID
     * @param table
     * @param where
     * @return
     * @throws Exception
     */
    public int queryTableMaxGIDbyNameandWhere(String table, String where)
            throws Exception {
        int maxGID = 0;
        if (null == table || table.equalsIgnoreCase(""))
            return maxGID;
        String sql = "";
        if (where == null || where.equalsIgnoreCase(""))
            sql = "select MAX(gid) from " + table;
        else
            sql = "select MAX(gid) from " + table + " " + where;
        try {
            Cursor localCursor = db.rawQuery(sql, null);
            localCursor.moveToFirst();
            if (localCursor.getCount() < 1) {
                localCursor.close();
                return maxGID;
            }
            maxGID = localCursor.getInt(0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return maxGID;
    }

    /***
     * 升级数据库
     * 
     * @return
     */
    public boolean upgradeDataBase() throws Exception {
        return true;
    }

    /***
     * 关闭已经打开的数据库
     */
    public void close() {
        try {
            if (db != null && db.isOpen())
                db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
