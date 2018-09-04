package com.ecity.cswatersupply.task;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.utils.CoordTransfer;

/**
 * 
 * @author: wangliu94@163.com
 */

public class TransparamsLoader {
    private static TransparamsLoader instance = null;

    public static TransparamsLoader getInstance() {
        if (instance == null) {
            instance = new TransparamsLoader();
        }
        return instance;

    }

    /**
     * 
     * @author: wangliu94@163.com
     */

    public void loadFromWeb(final String url) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    CoordTransfer.coordinateConvertor.LoadTransParamsFromWeb(url);
                } catch (Exception e) {
                    LogUtil.e(this, e);
                }
            }
        }).start();

    }

}
