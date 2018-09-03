package com.ecity.android.httpexecutor.uploadrecord;


import com.ecity.android.httpexecutor.AbstractRequestCallback;
import com.ecity.android.httpexecutor.AbstractRequestParameter;
import com.ecity.android.httpexecutor.RequestExecutor;
import com.ecity.android.log.LogUtil;

import java.util.Map;

/**
 * Created by jonathanma on 20/4/2017.
 */

class RequestRecordUploader {
    private static final String TAG = "RequestRecordUploader";
    private static RequestRecordUploader instance;

    static {
        instance = new RequestRecordUploader();
    }

    public static RequestRecordUploader getInstance() {
        return instance;
    }

    public void upload(final UploadRecord record) {
        RequestExecutor.execute(new AbstractRequestCallback() {
            @Override
            public boolean isFile() {
                return false;
            }

            @Override
            public String getFilePath() {
                return null;
            }

            @Override
            public boolean isPost() {
                return record.isPost();
            }

            @Override
            public String getUrl() {
                LogUtil.v(TAG, "url=" + record.getUrl());
                return record.getUrl();
            }

            @Override
            public Map<String, String> getParameter() throws Exception {
                return new AbstractRequestParameter() {
                    @Override
                    protected void fillParameters(Map<String, String> map) {
                        map.putAll(record.getUploadParameter());
                        LogUtil.v(TAG, "params=" + record.getUploadParameter().toString());
                    }
                }.toMap();
            }

            @Override
            public int getEventId() {
                return record.getRequestId();
            }
        });
    }
}
