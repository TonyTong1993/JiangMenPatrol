package com.zzz.ecity.android.applibrary.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;

import com.ecity.android.db.utils.StringUtil;
import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.model.EPositionMessageType;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.model.PositionConfig;
import com.zzz.ecity.android.applibrary.utils.GPSPositionPackageTool;

public class GPSPositionReporter {
    private boolean isRuning = false;
    private static GPSPositionReporter instance;

    public static GPSPositionReporter getInstance() {
        if (null == instance) {
            instance = new GPSPositionReporter();
        }
        return instance;
    }

    private GPSPositionReporter() {
    	
    }

    public boolean isTaskRuning() {
        return isRuning;
    }

    public void reportPositions(List<GPSPositionBean> reportBeanList) {
        reportPositions(reportBeanList, null);
    }

    public synchronized void reportPositions(
            List<GPSPositionBean> reportBeanList, Handler posReportHandler) {
        if (null == MyApplication.getApplication()) {
            return;
        }

        String url = PositionConfig.getReportServerURL();
        if (null == reportBeanList || reportBeanList.size() < 1) {
            if (null != posReportHandler) {
                posReportHandler.sendEmptyMessage(0);
            }
            return;
        }

        // ReportData data = new ReportData();
        long userid = 0;
        if (null != reportBeanList && reportBeanList.size() > 0) {
            GPSPositionBean bean = reportBeanList
                    .get(reportBeanList.size() - 1);
            userid = bean.getUserid();
            // data.setUserId(bean.getid());
        } else {
        	if (null != posReportHandler) {
                posReportHandler.sendEmptyMessage(0);
            }
        	
            return;
        }
        isRuning = true;
        report(userid, url, reportBeanList);
        isRuning = false;
        if (null != posReportHandler) {
            posReportHandler.sendEmptyMessage(1);
        }
    }
    
    private void report(long userid, String url, List<GPSPositionBean> beanList) {
        boolean needUpdate = false;
        List<GPSPositionBean> resultList = null;
        String pospackage = GPSPositionPackageTool.toJSONString(beanList);
        String userID = "";
        Map<String, String> paramMap = new HashMap<String, String>();
        try {
            if (!StringUtil.isEmpty(String.valueOf(userid))) {
                userID = String.valueOf(userid);
            } else {
                userID = "-1";
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        paramMap.put("userID", userID);

        List<NameValuePair> nameValuePairArray = new ArrayList<NameValuePair>();
        mapToList(url, paramMap, nameValuePairArray);
        url = url
                + (nameValuePairArray.isEmpty() ? "" : new StringBuilder()
                        .append("?")
                        .append(URLEncodedUtils.format(nameValuePairArray,
                                "UTF-8")).toString());
        HttpPost httpRequest = new HttpPost(url);
        if (pospackage != null) {
            try {
                // 构造最简单的字符串数据
                List<NameValuePair> posArray = new ArrayList<NameValuePair>();
                posArray.add(new BasicNameValuePair("pospackage", pospackage));
                posArray.add(new BasicNameValuePair("userID", userID));
                StringEntity reqEntity = new StringEntity(
                        URLEncodedUtils.format(posArray, "UTF-8"));

                reqEntity.setContentType("application/x-www-form-urlencoded");
                httpRequest.setEntity(reqEntity);
            } catch (UnsupportedEncodingException e) {
            	sendPositionBroadcast(EPositionMessageType.POSITIONREPORTFAIL,e.getMessage());
            }
        }

        HttpClient httpclient = new DefaultHttpClient();
        JSONObject jsonObject = null;
        HttpParams params = null;
        params = httpclient.getParams();
        // set timeout
        HttpConnectionParams.setConnectionTimeout(params, 15 * 1000);
        HttpConnectionParams.setSoTimeout(params, 40 * 1000);
        boolean isSuccess = false;
        try {
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            String result = "";
            if (httpResponse.getEntity() != null) {
                result = (EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8));
            }
            try {
                jsonObject = new JSONObject(result);
                try {

                    if (null == jsonObject || beanList == null) {
                        isSuccess = false;
                    } else {
                        if (!jsonObject.has("success")
                                && !jsonObject.has("isSuccess")) {
                            isSuccess = false;
                        } else {
                            boolean firstException = false;
                            try {
                                isSuccess = jsonObject.getBoolean("success");
                                firstException = false;
                            } catch (Exception e) {
                                firstException = true;
                            }

                            if (firstException) {
                                try {
                                    isSuccess = jsonObject
                                            .getBoolean("isSuccess");
                                } catch (Exception e) {
                                    isSuccess = false;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    resultList = beanList;
                }
            } catch (JSONException e) {
                resultList = beanList;
                sendPositionBroadcast(EPositionMessageType.POSITIONREPORTFAIL,e.getMessage());
            }
        } catch (ClientProtocolException e) {
            resultList = beanList;
            needUpdate = true;
            if (null != e) { 
            	sendPositionBroadcast(EPositionMessageType.POSITIONREPORTFAIL,e.getMessage());
            }
        } catch (ConnectTimeoutException e) {
            needUpdate = true;
            if (null != e) { 
            	sendPositionBroadcast(EPositionMessageType.POSITIONREPORTFAIL,e.getMessage());
            }
        } catch (SocketTimeoutException e) {
            resultList = beanList;
            needUpdate = true;
            if (null != e) { 
            	sendPositionBroadcast(EPositionMessageType.POSITIONREPORTFAIL,e.getMessage());
            }
        } catch (IOException e) {
            resultList = beanList;
            if (null != e) { 
            	sendPositionBroadcast(EPositionMessageType.POSITIONREPORTFAIL,e.getMessage());
            }
        }
        
        if (null != beanList) {
            resultList = new ArrayList<GPSPositionBean>();
            for (int i = 0; i < beanList.size(); i++) {
                GPSPositionBean gpsPositionBean = beanList.get(i);
                GPSPositionBean tmpBean = new GPSPositionBean();
                tmpBean.setacu(gpsPositionBean.getacu());
                tmpBean.setbattery(gpsPositionBean.getbattery());
                tmpBean.setId(gpsPositionBean.getId());
                tmpBean.setlat(gpsPositionBean.getlat());
                tmpBean.setlon(gpsPositionBean.getlon());
                tmpBean.setSpeed(gpsPositionBean.getSpeed());
                tmpBean.setStatus(gpsPositionBean.getStatus());

                tmpBean.setOverspeed(gpsPositionBean.isOverspeed()?1:0);
                tmpBean.setRepay(gpsPositionBean.isRepay()?1:0);
                tmpBean.setNightWatch(gpsPositionBean.isNightWatch()?1:0);
                tmpBean.setPlanTaskId(gpsPositionBean.getPlanTaskId());
                tmpBean.setInDetourArea(gpsPositionBean.isInDetourArea()?1:0);

                if (isSuccess) {
                    tmpBean.setStatus(1);
                    if (!tmpBean.isRepay()) {
                        tmpBean.setRepay(0);
                    }
                    needUpdate = true;
                } else {
                    if (0 == tmpBean.getStatus()) {
                        tmpBean.setStatus(0);
                    }
                    if (!tmpBean.isRepay()) {
                        needUpdate = true;
                    }
                    tmpBean.setRepay(1);
                }

                tmpBean.setTime(gpsPositionBean.getTime());
                tmpBean.setUserid(gpsPositionBean.getUserid());
                tmpBean.setx(gpsPositionBean.getx());
                tmpBean.sety(gpsPositionBean.gety());
                resultList.add(tmpBean);
            }
        }
        
        if(isSuccess){
        	sendPositionBroadcast(EPositionMessageType.POSITIONREPORTSUCCESS,"");
        }
        
        if (needUpdate) {
        	try {
				GPSPositionDao.getInstance().updateGPSPositions(resultList);
			} catch (Exception e2) {
				sendPositionBroadcast(EPositionMessageType.POSITIONBEANSAVEFAIL,e2.getMessage());
			}
        }
    }
    
	/***
	 * 参数列表Map转为List <br/>
	 * 在之前版本中，如果字符串长度小于2000 使用GET 否则使用POST
	 * @param url
	 * @param paramters 参数，仅支持字符串类型的数据
	 * @param nameValuePairList
	 * @return
	 */
	private final boolean mapToList(String url, Map<String, String> paramters, List<NameValuePair> nameValuePairList) {
		if ((paramters == null) || (paramters.isEmpty())) {
			return true;
		}
		int length = 0;
		for (Map.Entry<String, String> entry : paramters.entrySet()) {
			String str1 = entry.getKey();
			String str2 = entry.getValue();
			if (!StringUtil.isBlank(str1)) {
				if (str2 == null) {
					str2 = "";
				}
				length += str1.length() + str2.length() + 2;
				nameValuePairList.add(new BasicNameValuePair(str1, str2));
			}
		}
		
		return length <= 2000;
	}
	
	private void sendPositionBroadcast(EPositionMessageType type, String msg) {
		Intent intent = new Intent(PositionConfig.ACTION_POSITION_NAME);
		intent.putExtra(PositionConfig.ACTION_POSITION_MSG_TYPE,
				type.getValue());
		intent.putExtra(PositionConfig.ACTION_POSITION_MSG_CONTENT, msg);
		MyApplication.getApplication().sendBroadcast(intent);
	}
}
