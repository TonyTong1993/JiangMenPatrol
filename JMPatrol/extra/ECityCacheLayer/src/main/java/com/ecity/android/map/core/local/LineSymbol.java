/**
 * @company 武汉易思迪信息科技有限公司（ECity）
 * @package com.ecity.android.cityrevisionpo.graphic
 * @file LineSymbol.java
 * @author FangJianming
 * @version 2014-6-3
 */
package com.ecity.android.map.core.local;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import com.esri.core.symbol.SimpleLineSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * @package com.ecity.android.cityrevisionpo.graphic
 * @version 2014-6-3
 */
public class LineSymbol {

    private static LineSymbol instance;

    /**
     * @todo
     * @time 2014年12月5日 下午2:15:03
     * @return
     */
    public static LineSymbol getInstance() {
        if (instance == null)
            instance = new LineSymbol();
        return instance;
    }

    /**
     * @comments 存储线符号Map
     */
    private HashMap<Integer, SimpleLineSymbol> hashmap = null;
    /**
     * @comments 元数据-线符号数据表
     */
    private HashMap<Integer, SQLiteRecordBean> lsymbolRecordBeanList = null;
    /**
     * @comments 工作空间标签
     */
    private String TAG = "PointSymbol";

    /**
     * @comments 构造函数，初始化操作
     */
    @SuppressLint("UseSparseArrays")
    private LineSymbol() {
        lsymbolRecordBeanList = new HashMap<Integer, SQLiteRecordBean>();
        hashmap = new HashMap<Integer, SimpleLineSymbol>();
        Log.i(TAG, "线图元实例化！");
    }

    /**
     * @comments 构造函数，初始化操作
     */
    @SuppressLint("UseSparseArrays")
    public void setLineSymbol(HashMap<Integer, SQLiteRecordBean> lsymbol) {
        lsymbolRecordBeanList = lsymbol;
        hashmap = new HashMap<Integer, SimpleLineSymbol>();
        initHashMap();
        Log.i(TAG, "线图元实例化！");
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getSimpleLineSymbolbyMapKey
     * @function 根据Map键值获取线图元
     * @param key
     * @return
     */
    @SuppressLint("UseValueOf")
    public SimpleLineSymbol getSimpleLineSymbolbyMapKey(int key) {
        SimpleLineSymbol simpleLineSymbol = null;
        if (key <= 0 || key > hashmap.size())
            return hashmap.get(new Integer(0));
        simpleLineSymbol = hashmap.get(new Integer(key - 1));
        return simpleLineSymbol;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method initHashMap
     * @function 初始化Map列表
     * @return
     */
    @SuppressLint({ "UseSparseArrays", "UseValueOf" })
    public boolean initHashMap() {
        if (hashmap == null)
            hashmap = new HashMap<Integer, SimpleLineSymbol>();
        hashmap.clear();
        // 添加默认值0
        hashmap.put(0, getSimpleLineSymbolbySid(0));
        // 遍历HashMap
        for (Map.Entry<Integer, SQLiteRecordBean> entry : lsymbolRecordBeanList
                .entrySet()) {
            hashmap.put(new Integer(entry.getKey()),
                    getSimpleLineSymbolbySid(entry.getKey()));
        }
        if (hashmap.size() <= 0)
            return false;
        return true;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getSimpleLineSymbolbySid
     * @function 根据线号获取线图元
     * @param sid
     * @return
     */
    public SimpleLineSymbol getSimpleLineSymbolbySid(int sid) {
        SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(Color.BLUE,
                (int) 3, SimpleLineSymbol.STYLE.SOLID);

        if (sid <= 0)
            return simpleLineSymbol;
        return simpleLineSymbol;
    }
}
