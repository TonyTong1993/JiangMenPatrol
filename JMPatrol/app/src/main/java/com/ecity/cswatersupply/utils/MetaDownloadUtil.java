package com.ecity.cswatersupply.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.ecity.cswatersupply.model.metaconfig.DbMetaSetConfig;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;

public class MetaDownloadUtil {
	public static boolean isMetasLoaded = false;
	private static DbMetaSetConfig dbMetaSetConfig;
	public static int getAndroidSDKVersion() {
		int version = 8;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			Log.e("TAG", e.toString());
			version = 14;
		}
		return version;
	}

	public static void LoadMapMetas(final String spatialQueryServiceUrl) {
		if (getAndroidSDKVersion() >= 14){
			loadMetasFor14(spatialQueryServiceUrl);
		} else {
			loadMetasFor8(spatialQueryServiceUrl);
		}
	}

	private static void loadMetasFor14(final String spatialQueryServiceUrl) {
		new Thread() {
			public void run() {
				try {

					HttpClient httpclient = new DefaultHttpClient();
					String metasUrl = spatialQueryServiceUrl + "/metas?f=json";
					
					HttpGet httpRequest = new HttpGet(metasUrl);
					HttpResponse httpResponse = httpclient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						dbMetaSetConfig = DbMetaSetConfig.fromJsonInputStream(httpResponse.getEntity().getContent());
					}
					if (dbMetaSetConfig != null) {
						QueryLayerIDs.getInstance().init(
								dbMetaSetConfig.getDbMetaSet());
						if (QueryLayerIDs.getQueryNetlayerIDs().length == 0)
							throw new Exception("获取元数据失败！");
						isMetasLoaded = true;
					} else{
						Log.v("异常：", "获取元数据失败！");
						isMetasLoaded = false;
					}
					// 元数据加载成功后，提示消失

				} catch (Exception e) {
					Log.v("异常：", "元数据请求异常！");
					isMetasLoaded = false;
				}
			}
		}.start();
	}

	private static void loadMetasFor8(final String spatialQueryServiceUrl) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String metasUrl = spatialQueryServiceUrl
						+ "/metas?f=json";
			HttpGet httpRequest = new HttpGet(metasUrl);
			HttpResponse httpResponse = httpclient.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				dbMetaSetConfig = DbMetaSetConfig
						.fromJsonInputStream(httpResponse.getEntity()
								.getContent());
			}
			if (dbMetaSetConfig != null) {
				QueryLayerIDs.getInstance().init(
						dbMetaSetConfig.getDbMetaSet());
				if (QueryLayerIDs.getQueryNetlayerIDs().length == 0)
					throw new Exception("获取元数据失败！");
				
				isMetasLoaded = true;
			} else {
				isMetasLoaded = false;
			}
		} catch (Exception e) {
			Log.v("异常：", e.getMessage());
			isMetasLoaded = false;
			e.printStackTrace();
		}
	}
}
