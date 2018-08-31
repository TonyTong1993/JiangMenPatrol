package com.ecity.android.httpforandroid.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestBytes extends HttpRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(String url, Map<String, String> parameters) throws Exception {
		return getBytes(url,parameters,HttpRequestType.AUTO);
	}
	/***
	 * 
	 * @param url
	 * @param parameters
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(String url, Map<String, String> parameters,HttpRequestType type) throws Exception {
		HttpUriRequest httpRequest = null;
		try {
			ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
			boolean bool = HttpRequest.mapToList(url, parameters, localArrayList);
			
			switch (type) {
			case GET:
				httpRequest = HttpRequest.getHttpGet(url, localArrayList);
				break;
			case POST:
				httpRequest = HttpRequest.getHttpPost(url, localArrayList);
				break;
				
			default:
				httpRequest = bool ? HttpRequest
						.getHttpGet(url, localArrayList) : HttpRequest
						.getHttpPost(url, localArrayList);
				break;
			}
			return getBytes(httpRequest);
		} catch (Exception localException) {
			if (httpRequest != null) {
				httpRequest.abort();
			}
			throw localException;
		}
	}
	/***
	 * 
	 * @param httpUriRequest
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static final byte[] getBytes(HttpUriRequest httpUriRequest) throws ClientProtocolException, IOException {
		byte[] arrayOfByte = null;
		HttpEntity localHttpEntity = null;
		ByteArrayOutputStream localByteArrayOutputStream = null;
		try {
			HttpResponse localHttpResponse = httpClient.execute(httpUriRequest);
			arrayOfByte = readInputStream(localHttpResponse.getEntity().getContent());
		} finally {
			if (localByteArrayOutputStream != null) {
				localByteArrayOutputStream.close();
			}
			if (localHttpEntity != null) {
				localHttpEntity.consumeContent();
			}
		}

		return arrayOfByte;
	}
	/***
	 * 从URL地址获取图片
	 * @param urlPath
	 * @return
	 */
	public static byte[] getImageFromURL(String urlPath) {
		byte[] data = null;
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("content-type", "text/plain; charset=utf-8");
			conn.setDoInput(true);
			// conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10 * 1000);
			is = conn.getInputStream();
			if (conn.getResponseCode() == 200) {
				data = readInputStream(is);
			} else {
				data = null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return data;
	}
	/***
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 10];
		int length = -1;

		while ((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}

		baos.flush();

		byte[] data = baos.toByteArray();

		is.close();
		baos.close();

		return data;
	}
}
