package com.ecity.cswatersupply.network.request;

import com.ecity.android.httpforandroid.http.HttpBaseService;
import com.ecity.android.httpforandroid.http.HttpBaseServiceListener;
import com.ecity.android.httpforandroid.http.HttpBaseServiceParameters;
import com.ecity.android.httpforandroid.http.HttpRequestJsonParse;
import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.android.log.LogUtil;
import com.z3app.android.util.StringUtil;

public class CSWaterSupplyStringRequest extends HttpBaseService<String> {
    private HttpRequestType type;

    public CSWaterSupplyStringRequest(HttpBaseServiceParameters paramTaskParameters,
            HttpBaseServiceListener<String> paramTaskListener,
            HttpRequestType type) {
        super(paramTaskParameters, paramTaskListener);
        this.type = type;
    }

    public CSWaterSupplyStringRequest(HttpBaseServiceParameters paramTaskParameters) {
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
