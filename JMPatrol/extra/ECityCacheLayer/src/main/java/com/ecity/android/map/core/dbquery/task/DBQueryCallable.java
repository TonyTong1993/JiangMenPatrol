package com.ecity.android.map.core.dbquery.task;

import android.annotation.SuppressLint;

import com.ecity.android.httpforandroid.http.HttpClientEx;
import com.ecity.android.map.core.tile.TileCallable;
import com.ecity.android.map.core.tile.TimeInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/***
 * 瓦片请求过程
 * @author ZiZhengzhuan
 *
 */
public class DBQueryCallable extends TileCallable {
	private int level;
	private int col;
	private int row;
	private String url;
	private TimeInfo timeinfo;
	public DBQueryCallable(int level, int col, int row, String url, TimeInfo timeinfo) {
		super(row, row, row, url, timeinfo);
		this.url = url;
		this.level = level;
		this.col = col;
		this.row = row;
		this.timeinfo = timeinfo;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public TimeInfo getTimeinfo() {
		return timeinfo;
	}

	public void setTimeinfo(TimeInfo timeinfo) {
		this.timeinfo = timeinfo;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public byte[] call() throws Exception {
		HttpClient httpClient = null;
		byte[] image = null;
		if (null != url) {
			String tmp = url.toLowerCase();
			if (tmp.contains("https://")) {// SSL 协议认证
				httpClient = HttpClientEx.getNewHttpClient();
			} else {
				httpClient = new DefaultHttpClient();
			}
		}
		try {
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, timeinfo.getConnectTimeout());
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, timeinfo.getSoTimeOut());

			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				image = EntityUtils.toByteArray(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null
					&& httpClient.getConnectionManager() != null) {
				httpClient.getConnectionManager().shutdown();
			}
			httpClient = null;
		}
		
		return image;
	}

}
