package com.ecity.android.map.layer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpProtocolParams;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.SSLSocketFactoryEx;
import com.ecity.android.httpforandroid.httpext.AsyncHttpClient;
import com.ecity.android.httpforandroid.httpext.JsonHttpResponseHandler;
import com.ecity.android.httpforandroid.httpext.RequestParams;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.TimeInfo;

public class ECityDynamicServiceLayerInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	public String serviceDescription;
	public String mapName;
	public String description;
	public String copyrightText;
	public String layerInfos;
	public String tables;
	public SpatialReference spatialReference;
	public boolean singleFusedMapCache;
	public String tileInfo;
	public Envelope initialExtent;
	public SpatialReference defualtSpatialreference;
	public MapGeometry fullExtent;
	public String units;
	public String supportedImageFormatTypes;
	public String metas;
	public String capabilities;
	int maxRecordCount;
	boolean supportsDynamicLayers;
	TimeInfo timeInfo;
	double minScale = (0.0D / 0.0D);
	double maxScale = (0.0D / 0.0D);

	private String mapURL;// 服务路径
	private String layerName;// 图层名称
	private String cachePath;// 缓存目录
	
	public interface OnCompleteListener {
		public void onCompleteListener(boolean flg, String msg);
	};

	/***
	 * 
	 * @param cachePath
	 *            eg: ..//ECity//cache//map//
	 * @param mapURL
	 * @param layerName
	 */
	public ECityDynamicServiceLayerInfo(String cachePath, String mapURL,
			String layerName) {
		this.mapURL = mapURL;
		this.layerName = layerName;
		this.cachePath = cachePath;
	}
	
	public boolean isMapserverFileExist() {
		File file = new File(getMapserverPath());
		if (file.isDirectory()) {
			return false;
		}
		return file.exists();
	}

	
	public boolean parserMapServer(String layerName) {
    	RandomAccessFile raf = null;
    	File file = null;
    	file = new File(getMapserverPath());
		
		if(null == file || !file.exists())
			return false;
		
		boolean flg = false;
        try {
			raf = new RandomAccessFile(file, "rwd");
			raf.seek(0);
			byte[] buffer = new byte[(int) raf.length()];
			raf.read(buffer);
			String contentStr = new String(buffer);
			metas = contentStr;
			JsonParser tjsonParser = null;
			JsonFactory jpf = new JsonFactory();
			try {
				tjsonParser = jpf.createJsonParser(metas);
				tjsonParser.nextToken();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (tjsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
					return false;
				}
				while (tjsonParser.nextToken() != JsonToken.END_OBJECT) {
					String str = tjsonParser.getCurrentName();

					tjsonParser.nextToken();
					if ("serviceDescription".equals(str)) {
						serviceDescription = tjsonParser.getText();
					} else if ("mapName".equals(str)) {
						mapName = tjsonParser.getText();
					} else if ("description".equals(str)) {
						description = tjsonParser.getText();
					} else if ("copyrightText".equals(str)) {
						copyrightText = tjsonParser.getText();
					} else if ("spatialReference".equals(str)) {
						spatialReference = SpatialReference.fromJson(tjsonParser);
					} else if ("singleFusedMapCache".equals(str)) {
						singleFusedMapCache = tjsonParser.getBooleanValue();
					} else if ("units".equals(str)) {
						units = tjsonParser.getText();
					} else if ("supportedImageFormatTypes".equals(str)) {
						supportedImageFormatTypes = tjsonParser.getText();
					} else if ("fullExtent".equals(str)) {
						fullExtent = GeometryEngine.jsonToGeometry(tjsonParser);
					} else if ("initialExtent".equals(str)) {
						MapGeometry localMapGeometry = GeometryEngine.jsonToGeometry(tjsonParser);
						if (localMapGeometry != null) {
							initialExtent = ((Envelope) localMapGeometry.getGeometry());
							defualtSpatialreference = localMapGeometry.getSpatialReference();
						}
					} else if ("layers".equals(str)) {
						if (tjsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
							layerInfos = tjsonParser.nextTextValue();
						}
					} else if ("tables".equals(str)) {
						if (tjsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
							tables = tjsonParser.getText();
						}
					} else if ("tileInfo".equals(str)) {
						tileInfo = tjsonParser.nextTextValue();
					} else if ("capabilities".equals(str)) {
						capabilities = tjsonParser.getText();
					} else if ("maxRecordCount".equals(str)) {
						maxRecordCount = tjsonParser.getIntValue();
					} else if ("supportsDynamicLayers".equals(str)) {
						supportsDynamicLayers = tjsonParser.getBooleanValue();
					} else if ("timeInfo".equals(str)) {
						timeInfo = TimeInfo.fromJson(tjsonParser);
					} else if ("minScale".equals(str)) {
						if (tjsonParser.getCurrentToken() != JsonToken.VALUE_NULL)
							minScale = tjsonParser.getDoubleValue();
					} else if ("maxScale".equals(str)) {
						if (tjsonParser.getCurrentToken() != JsonToken.VALUE_NULL)
							maxScale = tjsonParser.getDoubleValue();
					} else {
						tjsonParser.skipChildren();
					}
				}
				flg = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch (Exception e) {
      	 e.printStackTrace();
      	 flg = false;
       }finally{
			try {
				if(raf!=null)
					raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
       }
        
        return flg;
	}
	
	/***
	 * 服务缓缓文件路径
	 * 
	 * @return
	 */
	private String getMapserverPath() {
		String lastStr = cachePath.substring(cachePath.length() - 2);
		String mapServerPath = "";
		if (lastStr.contains("//")) {
			mapServerPath = cachePath + layerName + "//mapserver.json";
		} else {
			mapServerPath = cachePath + "//" + layerName + "//mapserver.json";
		}
		return mapServerPath;
	}

	/***
	 * 请求地图图层元数据
	 * 
	 * @param onCompleteListeners
	 */
	public void requestMapServerInfo(OnCompleteListener onCompleteListeners) {
		if (client == null)
			client = new AsyncHttpClient();

		if (null != mapURL) {
			String tmp = mapURL.toLowerCase();
			if (tmp.contains("https://")) {
				SSLSocketFactory sf = createSSLSocketFactory();
				if (sf != null) {
					client.setSSLSocketFactory(sf);
				}
				HttpProtocolParams.setUseExpectContinue(client.getHttpClient()
						.getParams(), false);
			}
		}

		explainJson(onCompleteListeners);
	}

	protected AsyncHttpClient client = null;

	/***
	 * 
	 * @param onCompleteListeners
	 */
	public void explainJson(OnCompleteListener onCompleteListeners) {
		try {
			RequestParams localRequestParams = new RequestParams();
			localRequestParams.put("f", "json");
			client.get(this.mapURL, localRequestParams,
					new ResultJsonResponseHandler(onCompleteListeners));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 异步网络请求监听
	 * @author ZiZhengzhuan
	 *
	 */
	private class ResultJsonResponseHandler extends JsonHttpResponseHandler {
		private OnCompleteListener onCompleteListeners = null;

		public ResultJsonResponseHandler(OnCompleteListener onCompleteListeners) {
			this.onCompleteListeners = onCompleteListeners;
		}

		@Override
		public void onSuccess(JSONArray response) {
			super.onSuccess(response);
		}

		@Override
		public void onSuccess(JSONObject response) {
			if (null != response) {
				boolean success = false;
				try {
					File file = new File(getMapserverPath());
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
					String fixstr = response.toString();
					FileOutputStream fileOutputStream = new FileOutputStream(
							file, true);
					fileOutputStream.write(fixstr.getBytes());
					fileOutputStream.close();
					success = true;
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
				if (null != onCompleteListeners) {
					onCompleteListeners.onCompleteListener(success, "");
				}
			}
			super.onSuccess(response);
		}

		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);

			if (null == content)
				return;

			boolean success = false;
			try {
				File file = new File(getMapserverPath());
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				String fixstr = content.replaceAll("\\\"", "");
				FileOutputStream fileOutputStream = new FileOutputStream(file,
						true);
				fileOutputStream.write(fixstr.getBytes());
				fileOutputStream.close();
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
			}
			if (null != onCompleteListeners) {
				onCompleteListeners.onCompleteListener(success, "");
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			super.onFailure(e, errorResponse);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onFailure(Throwable error) {
			super.onFailure(error);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
		}

		@Override
		public void onFinish() {
			super.onFinish();
		}
	}

	/***
	 * 创建SSL套接字
	 * 
	 * @date 2015-12-04
	 * @author ZiZhengzhuan
	 * @return
	 */
	public static SSLSocketFactory createSSLSocketFactory() {
		SSLSocketFactoryEx sf = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactoryEx.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sf;
	}

	// ////////////////////////////////////////

	public String getServiceDescription() {
		return this.serviceDescription;
	}

	public String getMapName() {
		return this.mapName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getCopyrightText() {
		return this.copyrightText;
	}

	public String getLayerInfos() {
		return this.layerInfos == null ? "" : this.layerInfos;
	}

	public String getTables() {
		return this.tables == null ? "" : this.tables;
	}

	public SpatialReference getSpatialReference() {
		return this.spatialReference;
	}

	public boolean isSingleFusedMapCache() {
		return this.singleFusedMapCache;
	}

	public String getTileInfo() {
		return this.tileInfo;
	}
	public Envelope getFullExtent() {
		return this.fullExtent == null ? null : (Envelope) this.fullExtent
				.getGeometry();
	}

	public SpatialReference getDefualtSpatialReference() {
		return this.defualtSpatialreference == null ? this.spatialReference : this.defualtSpatialreference;
	}

	public void setDefualtSpatialReference(SpatialReference paramSpatialReference) {
		this.defualtSpatialreference = paramSpatialReference;
	}

	public String getUnits() {
		return this.units;
	}

	public boolean isSupportsDynamicLayers() {
		return this.supportsDynamicLayers;
	}

	public TimeInfo getTimeInfo() {
		return this.timeInfo;
	}
	
	public String getMetas()
	{
		return this.metas;
	}
}