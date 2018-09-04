package com.ecity.cswatersupply.network;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public interface IRequestCallback {
    /**
     * 
     * @return 
     *   true, if server will return a response in json format. <br/>
     *   Subscriber needs to override onCompletion(short flag, JSONObject response)<br/><br/>
     *   
     *   false, if response is a simple string. <br/>
     *   Subscriber needs to override onCompletion(short flag, String response)
     */
    boolean isForComplexResponse();

    /**
     * @return
     * Request type, Get or Post or Auto.
     */
    HttpRequestType getRequestType();

    String getUrl();

    IRequestParameter prepareParameters();

    void onCompletion(short flag, JSONObject response);

    void onCompletion(short flag, String response);

    void onError(Throwable e);
}
