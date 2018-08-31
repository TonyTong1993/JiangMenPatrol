/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.ecity.android.httpforandroid.httpext;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Used to intercept and handle the responses from requests made using 
 * {@link AsyncHttpClient}. The {@link #onSuccess(String)} method is 
 * designed to be anonymously overridden with your own response handling code.
 * <p>
 * Additionally, you can override the {@link #onFailure(Throwable, String)},
 * {@link #onStart()}, and {@link #onFinish()} methods as required.
 * <p>
 * For example:
 * <p>
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get("http://www.google.com", new AsyncHttpResponseHandler() {
 *     &#064;Override
 *     public void onStart() {
 *         // Initiated the request
 *     }
 *
 *     &#064;Override
 *     public void onSuccess(String response) {
 *         // Successfully got a response
 *     }
 * 
 *     &#064;Override
 *     public void onFailure(Throwable e, String response) {
 *         // Response failed :(
 *     }
 *
 *     &#064;Override
 *     public void onFinish() {
 *         // Completed the request (either success or failure)
 *     }
 * });
 * </pre>
 */
public class AsyncHttpResponseHandler {
    private static final int SUCCESS_MESSAGE = 0;
    private static final int FAILURE_MESSAGE = 1;
    private static final int START_MESSAGE = 2;
    private static final int FINISH_MESSAGE = 3;

    private Handler handler;

    /**
     * Creates a new AsyncHttpResponseHandler
     */
    public AsyncHttpResponseHandler() {
        // Set up a handler to post events back to the correct thread if possible
        if(Looper.myLooper() != null) {
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    AsyncHttpResponseHandler.this.handleMessage(msg);
                }
            };
        }
    }


    //
    // Callbacks to be overridden, typically anonymously
    //

    /**
     * Fired when the request is started, override to handle in your own code
     */
    public void onStart() {}

    /**
     * Fired in all cases when the request is finished, after both success and failure, override to handle in your own code
     */
    public void onFinish() {}

    /**
     * Fired when a request returns successfully, override to handle in your own code
     * @param content the body of the HTTP response from the server
     */
    public void onSuccess(String content) {}

    /**
     * Fired when a request fails to complete, override to handle in your own code
     * @param error the underlying cause of the failure
     * @deprecated use {@link #onFailure(Throwable, String)}
     */
    public void onFailure(Throwable error) {}

    /**
     * Fired when a request fails to complete, override to handle in your own code
     * @param error the underlying cause of the failure
     * @param content the response body, if any
     */
    public void onFailure(Throwable error, String content) {
        // By default, call the deprecated onFailure(Throwable) for compatibility
        onFailure(error);
    }


    //
    // Pre-processing of messages (executes in background threadpool thread)
    //

    protected void sendSuccessMessage(String responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, responseBody));
    }

    protected void sendFailureMessage(Throwable e, String responseBody) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, responseBody}));
    }

    protected void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    protected void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }


    //
    // Pre-processing of messages (in original calling thread, typically the UI thread)
    //

    protected void handleSuccessMessage(String responseBody) {
        onSuccess(responseBody);
    }

    protected void handleFailureMessage(Throwable e, String responseBody) {
        onFailure(e, responseBody);
    }



    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {
        switch(msg.what) {
            case SUCCESS_MESSAGE:
                handleSuccessMessage((String)msg.obj);
                break;
            case FAILURE_MESSAGE:
                Object[] repsonse = (Object[])msg.obj;
                handleFailureMessage((Throwable)repsonse[0], (String)repsonse[1]);
                break;
            case START_MESSAGE:
                onStart();
                break;
            case FINISH_MESSAGE:
                onFinish();
                break;
        }
    }

    protected void sendMessage(Message msg) {
        if(handler != null){
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, response);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }


    // Interface to AsyncHttpRequest
    void sendResponseMessage(HttpResponse response) {
        StatusLine status = response.getStatusLine();
        String responseBody = null;
        try {
            HttpEntity entity = null;
            HttpEntity temp = response.getEntity();
            if(temp != null) {
                entity = new BufferedHttpEntity(temp);
                InputStream inputStream = entity.getContent();
                responseBody = sparseResponse(inputStream);
//                responseBody = EntityUtils.toString(entity);
            }
        } catch(IOException e) {
            sendFailureMessage(e, null);
        }

        if(status.getStatusCode() >= 300) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
        } else {
            sendSuccessMessage(responseBody);
        }
    }
    
    @SuppressWarnings("hiding")
	private String sparseResponse(InputStream responseBody){
    	//将返回的值存入到String中
    	String str = "";
	    BufferedReader brd = null;
		try {
			brd = new BufferedReader(new InputStreamReader(responseBody,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    StringBuilder  sb  = new StringBuilder();
	    String line;
	    try {
			while((line = brd.readLine()) != null){
			    sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			brd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	    str = new String(sb.toString());
	    
		if (str.startsWith("\"") && str.endsWith("\"") && str.length() > 1 && (str.charAt(1) == '[' || str.charAt(1) == '{')) {
			str = str.substring(1, str.length() - 1);
		}
		// zizhengzhuan 2015-11-18 替换了会影响json字符串中的值的引号
		//str = str.replace("\\", "");
		return str;

    }
}