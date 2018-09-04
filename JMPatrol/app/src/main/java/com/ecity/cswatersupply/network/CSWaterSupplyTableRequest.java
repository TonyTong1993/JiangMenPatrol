package com.ecity.cswatersupply.network;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpBaseService;
import com.ecity.android.httpforandroid.http.HttpBaseServiceListener;
import com.ecity.android.httpforandroid.http.HttpBaseServiceParameters;
import com.ecity.android.httpforandroid.http.HttpRequestJsonParse;
import com.ecity.android.httpforandroid.http.HttpRequestType;

public class CSWaterSupplyTableRequest extends HttpBaseService<JSONObject> {
    private JSONObject jsonObj;
    private HttpRequestType type;

    public CSWaterSupplyTableRequest(HttpBaseServiceParameters paramTaskParameters, HttpBaseServiceListener<JSONObject> paramTaskListener, HttpRequestType type) {
        super(paramTaskParameters, paramTaskListener);
        this.type = type;
    }

    public CSWaterSupplyTableRequest(HttpBaseServiceParameters paramTaskParameters) {
        super(paramTaskParameters);
    }

    @Override
    protected JSONObject execute() throws Exception {
        try {
            String jsonstr = HttpRequestJsonParse.executeFromMapToStringNew(this.actionInput.getUrl(), this.actionInput.generateRequestParams(), type);
            jsonObj = new JSONObject(jsonstr);
        } catch (Exception e) {
            throw e;
        }

        return jsonObj;
    }
}
