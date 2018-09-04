/**
 * @company 武汉易思迪信息科技有限公司（ECity）
 * @package com.ecity.android.cityrevisionpo.graphic
 * @file SymbolStyle.java
 * @author FangJianming
 * @version 2014-5-30
 */
package com.ecity.android.map.core.local;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * @package com.ecity.android.cityrevisionpo.graphic
 * @version 2014-5-30
 */
public class SymbolStyle {

    private static SymbolStyle instance = null;

    public static SymbolStyle getInstance() {
        if (instance == null)
            instance = new SymbolStyle();
        return instance;
    }

    /**
     * @comments 构造函数，初始化操作
     */
    public SymbolStyle() {
        instance = this;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getSimpleMarkerSymbol
     * @function 获取点要素显示类型
     * @param color
     * @param size
     * @param type
     * @return
     */
    public SimpleMarkerSymbol getSimpleMarkerSymbol(int color, int size,
            int type) {
        SimpleMarkerSymbol pntSimpleMarkerSymbol = null;
        switch (type) {
        case 0:
            pntSimpleMarkerSymbol = new SimpleMarkerSymbol(color, size,
                    SimpleMarkerSymbol.STYLE.CIRCLE);
            break;
        case 1:
            pntSimpleMarkerSymbol = new SimpleMarkerSymbol(color, size,
                    SimpleMarkerSymbol.STYLE.CIRCLE);
            break;
        case 2:
            pntSimpleMarkerSymbol = new SimpleMarkerSymbol(color, size,
                    SimpleMarkerSymbol.STYLE.CROSS);
            break;
        case 3:
            pntSimpleMarkerSymbol = new SimpleMarkerSymbol(color, size,
                    SimpleMarkerSymbol.STYLE.DIAMOND);
            break;
        case 4:
            pntSimpleMarkerSymbol = new SimpleMarkerSymbol(color, size,
                    SimpleMarkerSymbol.STYLE.SQUARE);
            break;
        case 5:
            pntSimpleMarkerSymbol = new SimpleMarkerSymbol(color, size,
                    SimpleMarkerSymbol.STYLE.X);
            break;
        default:
            break;
        }
        return pntSimpleMarkerSymbol;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getPictureMarkerSymbol
     * @function 图片标记
     * @param bytes
     * @return
     */
    public PictureMarkerSymbol getPictureMarkerSymbol(byte[] bytes) {
        if (bytes == null)
            return null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                null);
        @SuppressWarnings("deprecation")
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(
                bitmapDrawable);
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    /**
     * @package com.ecity.android.cityrevisionpo.graphic
     * @method getSolidSimpleLineSymbolbySid
     * @function 实线标记
     * @param color
     * @param linw
     * @return
     */
    public SimpleLineSymbol getSolidSimpleLineSymbolbySid(int color, int linw) {
        SimpleLineSymbol LINE_SOLID = new SimpleLineSymbol(color, linw,
                SimpleLineSymbol.STYLE.SOLID);
        return LINE_SOLID;
    }
}
