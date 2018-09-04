/**
 * @company 武汉易思迪信息科技有限公司（ECity）
 * @package com.ecity.android.cityrevisionpo.graphic
 * @file PointSymbol.java
 * @author FangJianming
 * @version 2014-5-30
 */
package com.ecity.android.map.core.local;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import com.esri.core.symbol.MarkerSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * @package com.ecity.android.cityrevisionpo.graphic
 * @version 2014-5-30
 */
public class PointSymbol {

    private static PointSymbol instance;

    /**
     * @todo
     * @time 2014年12月5日 下午2:15:03
     * @return
     */
    public static PointSymbol getInstance() {
        if (instance == null)
            instance = new PointSymbol();
        return instance;
    }

    /**
     * @comments 存储点符号Map
     */
    private HashMap<Integer, MarkerSymbol> hashmap = null;
    /**
     * @comments 元数据-符号数据表
     */
    private HashMap<Integer, SQLiteRecordBean> grpdataRecordBeanMap = null;
    /**
     * @comments 元数据-线符号数据表
     */
    private HashMap<Integer, SQLiteRecordBean> psymbolRecordBeanMap = null;
    /**
     * @comments 工作空间标签
     */
    private String TAG = "PointSymbol";

    /**
     * @comments 构造函数，初始化操作
     */
    @SuppressLint("UseSparseArrays")
    private PointSymbol() {
        grpdataRecordBeanMap = new HashMap<Integer, SQLiteRecordBean>();
        psymbolRecordBeanMap = new HashMap<Integer, SQLiteRecordBean>();
        hashmap = new HashMap<Integer, MarkerSymbol>();
        initHashMap();
        Log.i(TAG, "点图元实例化！");
    }

    /**
     * @comments 构造函数，初始化操作
     */
    @SuppressLint("UseSparseArrays")
    public void setPointSymbol(HashMap<Integer, SQLiteRecordBean> grpdata,
            HashMap<Integer, SQLiteRecordBean> psymbol) {
        grpdataRecordBeanMap = grpdata;
        psymbolRecordBeanMap = psymbol;
        hashmap = new HashMap<Integer, MarkerSymbol>();
        initHashMap();
        Log.i(TAG, "点图元实例化！");
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getMarkerSymbolbyMapKey
     * @function 根据Map键值获取MarkerSymbol
     * @param key
     * @return
     */
    @SuppressLint("UseValueOf")
    public MarkerSymbol getMarkerSymbolbyMapKey(int key) {
        MarkerSymbol markaerSymbol = null;
        if (key <= 0 || key > hashmap.size())
            return hashmap.get(0);
        markaerSymbol = hashmap.get(new Integer(key - 1));
        return markaerSymbol;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method initHashMap
     * @function 初始化Map列表
     * @return
     */
    @SuppressLint({ "UseValueOf", "UseSparseArrays" })
    public boolean initHashMap() {
        if (hashmap == null)
            hashmap = new HashMap<Integer, MarkerSymbol>();
        hashmap.clear();
        // 添加默认值0
        hashmap.put(0, getMarkerSymbolbySid(0));
        // 遍历HashMap
        for (Map.Entry<Integer, SQLiteRecordBean> entry : psymbolRecordBeanMap
                .entrySet()) {
            hashmap.put(new Integer(entry.getKey()),
                    getMarkerSymbolbySid(entry.getKey()));
        }
        if (hashmap.size() <= 0)
            return false;
        return true;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getSimpleMarkerSymbolbySid
     * @function 获取点要素图元
     * @param sid
     * @return
     */
    public MarkerSymbol getMarkerSymbolbySid(int sid) {

        MarkerSymbol simpleMarkerSymbol = null;
        if (sid <= 0) {
            /* Color的第一个参数代表透明度，0完全透明，255完全不透明 */
            simpleMarkerSymbol = SymbolStyle.getInstance()
                    .getSimpleMarkerSymbol(Color.argb(255, 255, 0, 0), (int) 4,
                            0);
            /*
             * simpleMarkerSymbol = new PictureMarkerSymbol(
             * SessionManager.appResource
             * .getDrawable(R.drawable.accident_point_a));
             * simpleMarkerSymbol.setOffsetY(-2);
             */
            return simpleMarkerSymbol;
        }
        return simpleMarkerSymbol;
    }
}
