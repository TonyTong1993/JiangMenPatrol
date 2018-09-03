package com.example.zzht.jmpatrol.network.request;

import com.ecity.android.httpforandroid.http.HttpBaseService;
import com.ecity.android.httpforandroid.http.HttpBaseServiceListener;
import com.ecity.android.httpforandroid.http.HttpBaseServiceParameters;
import com.ecity.android.httpforandroid.http.HttpRequestJsonParse;
import com.ecity.android.httpforandroid.http.HttpRequestType;

import org.json.JSONObject;

public class JsonBaseRequest extends HttpBaseService<JSONObject> {
    private JSONObject jsonObj;
    private HttpRequestType type;

    public JsonBaseRequest(HttpBaseServiceParameters paramTaskParameters, HttpBaseServiceListener<JSONObject> paramTaskListener, HttpRequestType type) {
        super(paramTaskParameters, paramTaskListener);
        this.type = type;
    }

    public JsonBaseRequest(HttpBaseServiceParameters paramTaskParameters) {
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
