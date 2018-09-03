package com.ecity.android.httpexecutor;

/**
 * @author maoshoubei
 * @date 2017/10/2
 */

public interface IRequestFinishCallback {
    /**
     * 请求完成后回调方法
     * @param o
     */
    void onRequestProcessed(Object o);
}
