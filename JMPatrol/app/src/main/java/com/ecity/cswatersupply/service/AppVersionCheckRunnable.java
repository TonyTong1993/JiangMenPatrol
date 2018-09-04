package com.ecity.cswatersupply.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.ecity.android.httpforandroid.http.Config;
import com.ecity.cswatersupply.model.AppVerionInfo;

public class AppVersionCheckRunnable implements Runnable {
    public static final int CHECKAPPUPDATE_COMPLETE = 0x98;
    
    private Handler handler;
    private String configURL;

    public AppVersionCheckRunnable(Handler handler, String configURL) {
        this.handler = handler;
        this.configURL = configURL;
    }

    /**
     * @return 从服务器端下载得到地图服务配置
     */
    @Override
    public void run() {
        AppVerionInfo appVerionInfo = new AppVerionInfo();
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpParams params = null;
            params = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, Config.httpConnectionTimeout);
            HttpConnectionParams.setSoTimeout(params, Config.httpSoTimeout);
            httpclient.setParams(params);
            HttpGet httpRequest = new HttpGet(configURL);
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 在线读取地图配置文件
                InputStream stream = httpResponse.getEntity().getContent();
                try {
                    String vstr = inputInputStream2StringReader(stream, null);
                    JSONObject jsonObj = new JSONObject(vstr);
                    Iterator<String> iterator = jsonObj.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        String value = jsonObj.optString(key);
                        if (key.equalsIgnoreCase("versionCode")) {
                            appVerionInfo.setVersionCode(Integer.parseInt(value));
                        } else if (key.equalsIgnoreCase("versionName")) {
                            appVerionInfo.setVersionName(value);
                        } else if (key.equalsIgnoreCase("APKName")) {
                            appVerionInfo.setPakage(value);
                        } else if (key.equalsIgnoreCase("msg")) {
                            appVerionInfo.setDescription(value);
                        } else if (key.equalsIgnoreCase("type")) {
                            appVerionInfo.setType(Integer.parseInt(value));
                        }
                    }
                    appVerionInfo.setSuccess(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    appVerionInfo.setSuccess(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            appVerionInfo.setSuccess(false);
        }
        if (null != handler) {
            Message msg = Message.obtain();
            msg.what = CHECKAPPUPDATE_COMPLETE;
            msg.obj = appVerionInfo;
            handler.sendMessage(msg);
        }
    }

    /**
     * 利用BufferedReader实现Inputstream转换成String <功能详细描述>
     * 
     * @param in
     * @return String
     */
    public static String inputInputStream2StringReader(InputStream in, String encode) {
        String str = "";
        try {
            if (encode == null || encode.equals("")) {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();
            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
