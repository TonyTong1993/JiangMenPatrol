package com.ecity.android.map.core.local;

import android.database.SQLException;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ECityPipeTransUtil {

    // /***
    // * 将SQLite数据记录转换为Graphic对象
    // *
    // * @param bean
    // * @return
    // * @throws Exception
    // */
    // public static ECityGraphic recordBeantoGraphic(SQLiteRecordBean bean)
    // throws Exception {
    // if (null == bean || null == bean.getTablename()
    // || null == bean.getAttribute())
    // return null;
    // Map<String, Object> att = new HashMap<String, Object>();
    // // 添加表名
    // att.put(ECityPipeConstant.String_Key_TableName, bean.getTablename());
    //
    // // ====================================================
    //
    // ECityGraphic graphic = null;
    //
    // if (bean.getTabletype().equalsIgnoreCase(DBTableType.DBTableType_LINE)) {
    //
    // graphic = new ECityGraphic();
    // att.put(ECityGraphic.Graphic_Type_Key, ECityDataId.type_pipe);
    //
    // } else if (bean.getTabletype().equalsIgnoreCase(
    // DBTableType.DBTableType_POINT)) {
    //
    // graphic = new ECityGraphic();
    // att.put(ECityGraphic.Graphic_Type_Key, ECityDataId.type_node);
    //
    // }
    //
    //
    // if (graphic != null) {
    // // 完成属性建立
    // att.putAll(bean.getAttribute());
    // graphic.setAttributes(att);
    // }
    // return graphic;
    // }

    /**
     * @method blobToDoublePoint
     * @TODO 二进制转换为Point数组
     * @param bytes
     * @return
     * @throws SQLException
     */
    public static ArrayList<Point> blobToDoublePoint(byte[] bytes) {

        int length = bytes.length / 16;
        ArrayList<Point> points = new ArrayList<Point>();
        // 先进行byte到double的转换，再进行点的组合
        double doub[] = new double[2 * length];
        for (int i = 0, j = 0; i < bytes.length; i = i + 8, j = j + 1) {
            int cnt = 0;
            int len = 8;
            byte[] tmp = new byte[8];
            for (int ti = i; ti < (i + len); ti++) {
                tmp[cnt] = bytes[ti];
                cnt++;
            }
            long accum = 0;
            int tj = 0;
            for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
                accum |= ((long) (tmp[tj] & 0xff)) << shiftBy;
                tj++;
            }
            doub[j] = Double.longBitsToDouble(accum);
        }
        for (int i = 0, j = 0; i < length; i++, j = j + 2) {
            points.add(new Point(doub[j], doub[j + 1], 0));
        }
        return points;
    }

    /***
     * 将SQLite数据记录转换为Graphic对象
     * 
     * @param bean
     * @return
     * @throws Exception
     */
    public static Graphic recordBeantoGraphic(SQLiteRecordBean bean)
            throws Exception {
        if (null == bean || null == bean.getTablename()
                || null == bean.getAttribute())
            return null;
        Map<String, Object> att = new HashMap<String, Object>();
        // 添加表名
        att.put("tableName", bean.getTablename());
        // 完成属性建立
        att.putAll(bean.getAttribute());
        // ====================================================
        Symbol symbol = null;
        Graphic graphic = null;
        // sid 可能存在错误
        int sid = -1;
        if (!(att.get("sid") == null))
            sid = (Integer) att.get("sid");
        else
            sid = 0;

        if (bean.getTabletype().equalsIgnoreCase(DBTableType.DBTableType_LINE)) {

            symbol = LineSymbol.getInstance().getSimpleLineSymbolbyMapKey(sid);
            byte[] bytes = (byte[]) att.get("geom");
            ArrayList<Point> points = blobToDoublePoint(bytes);
            Polyline poly = new Polyline();
            poly.startPath(points.get(0));
            for (int i = 1; i < points.size(); i++) {
                poly.lineTo(points.get(i));
            }
            // graphic = new Graphic(poly, symbol, att, null);
            graphic = new Graphic(poly, symbol, att, 0);
        } else if (bean.getTabletype().equalsIgnoreCase(
                DBTableType.DBTableType_POINT)) {
            symbol = PointSymbol.getInstance().getMarkerSymbolbyMapKey(sid);
            Point point = new Point((Double) att.get("x"),
                    (Double) att.get("y"));
            // graphic = new Graphic(point, symbol, att, null);
            graphic = new Graphic(point, symbol, att, 1);
        }

        return graphic;
    }

    //
    // /**
    // * @method blobToDoublePoint
    // * @TODO 二进制转换为Point数组
    // * @param bytes
    // * @return
    // * @throws SQLException
    // */
    // public static ArrayList<Point> blobToDoublePoint(byte[] bytes) {
    // int length = bytes.length / 16;
    // ArrayList<Point> points = new ArrayList<Point>();
    // // 先进行byte到double的转换，再进行点的组合
    // double doub[] = new double[2 * length];
    // for (int i = 0, j = 0; i < bytes.length; i = i + 8, j = j + 1) {
    // int cnt = 0;
    // int len = 8;
    // byte[] tmp = new byte[8];
    // for (int ti = i; ti < (i + len); ti++) {
    // tmp[cnt] = bytes[ti];
    // cnt++;
    // }
    // long accum = 0;
    // int tj = 0;
    // for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
    // accum |= ((long) (tmp[tj] & 0xff)) << shiftBy;
    // tj++;
    // }
    // doub[j] = Double.longBitsToDouble(accum);
    // }
    // for (int i = 0, j = 0; i < length; i++, j = j + 2) {
    // points.add(new Point(doub[j], doub[j + 1]));
    // }
    // return points;
    // }

    /***
     * 将Graphic对象转换为SQLite数据记录
     * 
     * @param
     * @return
     */
    public static SQLiteRecordBean graphictoRecordBean(Graphic graphic) {

        if (null == graphic || null == graphic.getAttributes())
            return null;
        Map<String, Object> att = new HashMap<String, Object>();
        String tableName = "";
        if (graphic.getAttributes().containsKey("tableName"))
            tableName = String
                    .valueOf(graphic.getAttributes().get("tableName"));
        else
            return null;
        att.putAll(graphic.getAttributes());
        att.remove("tableName");
        SQLiteRecordBean bean = null;
        if (graphic.getGeometry().getType() == Geometry.Type.POINT) {
            bean = new SQLiteRecordBean(tableName,
                    DBTableType.DBTableType_POINT);
            Point pnt = (Point) graphic.getGeometry();
            att.put("x", pnt.getX());
            att.put("y", pnt.getY());
        } else if (graphic.getGeometry().getType() == Geometry.Type.LINE
                || graphic.getGeometry().getType() == Geometry.Type.POLYLINE)
            bean = new SQLiteRecordBean(tableName, DBTableType.DBTableType_LINE);
        bean.setAttribute(att);

        return bean;
    }
    // /**
    // * @package com.ecity.android.cityrevisionpo.util
    // * @method recordBeantoLinSymbol
    // * @function 将Bean转换成线符号模型
    // * @param bean
    // * @return
    // */
    // public static DBlsymbol recordBeantoLinSymbol(SQLiteRecordBean bean) {
    // if (null == bean || null == bean.getTablename()
    // || null == bean.getAttribute())
    // return null;
    // DBlsymbol lsymbol = new DBlsymbol();
    // Map<String, Object> beanAtt = bean.getAttribute();
    // lsymbol.sid = ((Integer) beanAtt.get("sid")).intValue();
    // lsymbol.linw = ((Double) beanAtt.get("linw")).doubleValue();
    // lsymbol.red = ((Integer) beanAtt.get("r")).intValue();
    // lsymbol.green = ((Integer) beanAtt.get("g")).intValue();
    // lsymbol.blue = ((Integer) beanAtt.get("b")).intValue();
    // return lsymbol;
    // }
    //
    // /**
    // * @package com.ecity.android.cityrevisionpo.util
    // * @method recordBeantoPntSymbol
    // * @function 将Bean转换成点符号模型
    // * @param bean
    // * @return
    // */
    // public static DBpsymbol recordBeantoPntSymbol(SQLiteRecordBean bean) {
    // if (null == bean || null == bean.getTablename()
    // || null == bean.getAttribute())
    // return null;
    // DBpsymbol psymbol = new DBpsymbol();
    // Map<String, Object> beanAtt = bean.getAttribute();
    // psymbol.sid = ((Integer) beanAtt.get("sid")).intValue();
    // psymbol.size = ((Double) beanAtt.get("size")).doubleValue();
    // psymbol.red = ((Integer) beanAtt.get("r")).intValue();
    // psymbol.green = ((Integer) beanAtt.get("g")).intValue();
    // psymbol.blue = ((Integer) beanAtt.get("b")).intValue();
    // psymbol.grpid = ((Integer) beanAtt.get("grpid")).intValue();
    //
    // return psymbol;
    // }
    // /**
    // * @package com.ecity.android.cityrevisionpo.util
    // * @method recordBeantoDBgrpdata
    // * @function 将Bean转换成点图元符号模型
    // * @param bean
    // * @return
    // */
    // public static DBgrpdata recordBeantoDBgrpdata(SQLiteRecordBean bean) {
    // if (null == bean || null == bean.getTablename()
    // || null == bean.getAttribute())
    // return null;
    // DBgrpdata grpdata = new DBgrpdata();
    // Map<String, Object> beanAtt = bean.getAttribute();
    // grpdata.grpid = ((Integer) beanAtt.get("grpid")).intValue();
    // if (beanAtt.get("type") == null)
    // grpdata.type = 1;// 默认设置
    // else
    // grpdata.type = ((Integer) beanAtt.get("type")).intValue();
    // if (beanAtt.get("grpdata") == null)
    // grpdata.grpdata = null;
    // else
    // grpdata.grpdata = (byte[]) beanAtt.get("grpdata");
    //
    // return grpdata;
    // }

}
