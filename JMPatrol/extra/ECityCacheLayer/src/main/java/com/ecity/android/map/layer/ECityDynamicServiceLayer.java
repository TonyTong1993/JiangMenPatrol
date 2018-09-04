package com.ecity.android.map.layer;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

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
import org.codehaus.jackson.JsonParser;

import android.util.Log;

import com.ecity.android.httpforandroid.http.HttpClientEx;
import com.ecity.android.map.layer.ECityDynamicServiceLayerInfo.OnCompleteListener;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.internal.value.r;
import com.esri.core.io.EsriSecurityException;
import com.esri.core.io.EsriServiceException;

public class ECityDynamicServiceLayer extends ArcGISDynamicMapServiceLayer implements
		OnCompleteListener {
	private final static int CON_TIMEOUT = 1000 * 10;
	private final static int READ_TIMEOUT = 1000 * 60;
	private ExecutorService pool = Executors.newCachedThreadPool();
	private String cachePath;// 缓存目录
	private ECityDynamicServiceLayerInfo layerInfoParser = null;
	private String layerName = "";

	public ECityDynamicServiceLayer(String cachePath, String url,
			String layerName) {
		super(url);
		this.layerName = layerName;
		this.cachePath = cachePath;
		setName(layerName);

		// 读取文件
		layerInfoParser = new ECityDynamicServiceLayerInfo(cachePath, url,layerName);

	    setCredentials(credentials);
	   
	    initCachData();
		if (layerInfoParser.isMapserverFileExist() && layerInfoParser.parserMapServer(layerName)) {
			initLayer();
		} else {
			layerInfoParser.requestMapServerInfo(this);
		}
	}

	// 初始化缓存路径
	private void initCachData() {
		File file = new File(getMapPathforCache());
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * 判断文件夹是否存在，如果不存在就创建
	 * 
	 * @param fileName
	 *            完整的文件路径
	 */
	private boolean hasFileDir(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.exists();
	}

	private String getMapPathforCache() {
		String lastStr = cachePath.substring(cachePath.length() - 2);
		String fixedcachePath = "";
		if (lastStr.contains("/")) {
			fixedcachePath = cachePath + layerName;
		} else {
			fixedcachePath = cachePath + "//" + layerName;
		}
		hasFileDir(fixedcachePath);
		return fixedcachePath;
	}

	/***
	 * 请求图片
	 */
	@Override
	protected byte[] getImage(int width, int height, Envelope extent)
			throws Exception {
		return requestTileImage(width, height, extent, getAllLayers());
	}

	/***
	 * 元数据请求监听
	 */
	@Override
	public void onCompleteListener(boolean flg, String msg) {
		if (flg) {
		    try
		    {
		      getServiceExecutor().submit(new Runnable() {
		        public void run() {
	    			ECityDynamicServiceLayer.this.initLayer();
		        }
		      });
		    }
		    catch (RejectedExecutionException e) {
		    }
		}
	}

	/***
	 * 初始化
	 */
	@Override
	protected void initLayer() {
		if (getID() == 0) {
			this.nativeHandle = create();
		}
		if (this.nativeHandle == 0L) {
			changeStatus(OnStatusChangedListener.STATUS.fromInt(-1000));
			Log.e("ArcGIS", "initialize fail : "
					+ OnStatusChangedListener.STATUS.fromInt(-1000));
		} else {
			try {
				if (!layerInfoParser.parserMapServer(layerName))
					throw new Exception("init layer failed");
				
				JsonParser tjsonParser = parserMetas(getUrl(),layerInfoParser.getMetas());
				serviceInfo = r.a(tjsonParser,layerInfoParser.getMetas());
				
				super.initLayer();
			} catch (Exception localException) {
				Log.e("ArcGIS", "url =" + getUrl(), localException);
				if ((localException instanceof EsriSecurityException))
					changeStatus(OnStatusChangedListener.STATUS
							.fromInt(((EsriSecurityException) localException)
									.getCode()));
				else
					changeStatus(OnStatusChangedListener.STATUS.fromInt(-1002));
			}
		}
		super.initLayer();
	}

	private static final JsonParser parserMetas(String url,String metas) throws Exception
	  {
	    JsonParser localJsonParser = null;
	    try {
	      localJsonParser = com.esri.core.internal.util.c.c(metas);
	      localJsonParser.nextToken();
	      EsriSecurityException[] arrayOfEsriSecurityException = new EsriSecurityException[1];
	      //if (com.esri.core.internal.io.handler.c.a(metas, localJsonParser, url, arrayOfEsriSecurityException, paramInt, parami))
	     // {
	    //	  return a(url, paramMap, true, paramInt + 1, parami);
	      //}
	      if (arrayOfEsriSecurityException[0] != null)
	        throw arrayOfEsriSecurityException[0];
	    } catch (EsriServiceException localEsriServiceException) {
	      if ((localEsriServiceException.getCode() == 401) || (localEsriServiceException.getMessage().trim().contains("Unauthorized")))
	        throw new EsriSecurityException(-10001, "Invalid or missing user credentials", localEsriServiceException);
	      throw localEsriServiceException;
	    }
	    return localJsonParser;
	  }
	
	/***
	 * 检查与服务器的连接状态
	 */
	public byte[] requestTileImage(int width, int height, Envelope extent,
			ArcGISLayerInfo[] layerInfo) throws Exception {
		String url = getUrl();
		url += "/export?";
		url += "dpi=" + getDpi();
		url += "&transparent=true";
		url += "&format=PNG24";
		url += "&bbox=" + extent.getXMin() + "%2C" + extent.getXMin() + "%2C"
				+ extent.getXMin() + "%2C" + extent.getXMin();
		url += "&bboxSR=" + getSpatialReference().getID();
		url += "&imageSR=" + getSpatialReference().getID();
		url += "&size=" + width + "%2C" + height;
		url += "&f=image";

		byte[] buffer = null;
		try {
			ImageRequestWorkHandler runable = new ImageRequestWorkHandler(url);
			pool.execute(runable);
			while (!runable.getFlg()) {
				try {
					Thread.sleep(150);
				} catch (Exception e) {
				}

			}
			buffer = runable.getImage();
		} catch (Exception e) {
		}
		return buffer;
	}

	/***
	 * 图片请求
	 * 
	 * @author ZiZhengzhuan
	 *
	 */
	public class ImageRequestWorkHandler implements Runnable {
		private String httpUrl = "";
		private String message = "";
		private boolean flg = false;
		private boolean connectOK = false;
		private byte[] image;

		public byte[] getImage() {
			return image;
		}

		public ImageRequestWorkHandler(String httpUrl) {
			this.httpUrl = httpUrl;
			flg = false;
			connectOK = false;
		}

		public boolean getFlg() {
			return flg;
		}

		public boolean isConnectOK() {
			return connectOK;
		}

		public String getMessage() {
			return message;
		}

		@Override
		public void run() {
			HttpClient httpClient = null;
			if (null != httpUrl) {
				String tmp = httpUrl.toLowerCase();
				if (tmp.contains("https://")) {// SSL 协议认证
					httpClient = HttpClientEx.getNewHttpClient();
				} else {
					httpClient = new DefaultHttpClient();
				}
			}
			try {
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, CON_TIMEOUT);
				httpClient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);

				HttpGet httpGet = new HttpGet(httpUrl);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					connectOK = true;
					HttpEntity entity = httpResponse.getEntity();
					image = EntityUtils.toByteArray(entity);
				}
				flg = true;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				e.printStackTrace();
				message += "\n";
				message += e.getMessage();
				flg = true;
				connectOK = false;
			} catch (ConnectTimeoutException e) {
				e.printStackTrace();
				e.printStackTrace();
				message += "\n";
				message += e.getMessage();
				flg = true;
				connectOK = false;
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace();
				message += "\n";
				message += e.getMessage();
				flg = true;
				connectOK = false;
			} finally {
				if (httpClient != null
						&& httpClient.getConnectionManager() != null) {
					httpClient.getConnectionManager().shutdown();
				}
			}
		}
	}
}
