package com.example.zzht.jmpatrol.network.request;

import com.ecity.android.httpforandroid.http.HttpBaseService;
import com.ecity.android.httpforandroid.http.HttpBaseServiceListener;
import com.ecity.android.httpforandroid.http.HttpBaseServiceParameters;
import com.ecity.android.httpforandroid.http.HttpRequestJsonParse;
import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.example.zzht.jmpatrol.utils.StringUtil;

/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/3.
 * PS: Not easy to write code, please indicate.
 */
public class StringBaseRequest extends HttpBaseService<String> {
    private HttpRequestType type;

    public StringBaseRequest(HttpBaseServiceParameters paramTaskParameters,
                                      HttpBaseServiceListener<String> paramTaskListener,
                                      HttpRequestType type) {
        super(paramTaskParameters, paramTaskListener);
        this.type = type;
    }

    public StringBaseRequest(HttpBaseServiceParameters paramTaskParameters) {
        super(paramTaskParameters);
    }

    @Override
    protected String execute() throws Exception {
        String msg = "";
        try {
            String response = HttpRequestJsonParse.executeFromMapToStringNew(
                    this.actionInput.getUrl(),
                    this.actionInput.generateRequestParams(), type);
            if (!StringUtil.isBlank(response)) {
                msg = response;
            }
        } catch (Exception e) {
//            LogUtil.e(this, e);
            throw e;
        }

        return msg;
    }
}
