package com.ecity.android.httpexecutor;

/**
 * @author maoshoubei
 * @date 2017/10/23
 */

public abstract class AbstractBaseRequest {

    /**
     * 获取请求回调接口
     *
     * @return
     */
    protected abstract AbstractRequestCallback getAbstractRequestCallback();

    /**
     * 执行请求
     */
    public void execute() {
        RequestExecutor.execute(getAbstractRequestCallback());
    }
}
