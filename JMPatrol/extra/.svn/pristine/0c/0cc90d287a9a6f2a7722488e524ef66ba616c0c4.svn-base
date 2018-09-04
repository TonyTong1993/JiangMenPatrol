package com.ecity.android.map.layer;

import android.util.Log;

import com.ecity.android.httpforandroid.http.SSLSocketFactoryEx;
import com.ecity.android.httpforandroid.httpext.AsyncHttpClient;
import com.ecity.android.httpforandroid.httpext.JsonHttpResponseHandler;
import com.ecity.android.httpforandroid.httpext.RequestParams;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.KeyStore;
import java.util.Arrays;

public class ECityCacheableTiledServiceLayerInfo {
	private String mapURL;// 服务路径
	private String layerName;// 图层名称
	private String cachePath;// 缓存目录
	private int rows; // 瓦片高度
	private int cols; // 瓦片宽度
	private int dpi; // //瓦片分辨率
	private Point origin; // 地图原点
	private int wkid; // 地图坐标ID
	private double[] res; // 地图分辨率
	private double[] scale; // 比例尺
	private Envelope initEnvelope; // 初始化范围
	private Envelope fullEnvelope; // 最大范围

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
	public ECityCacheableTiledServiceLayerInfo(String cachePath, String mapURL, String layerName) {
		this.mapURL = mapURL;
		this.layerName = layerName;
		this.cachePath = cachePath;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getDpi() {
		return dpi;
	}

	public Point getOrigin() {
		return origin;
	}

	public int getWkid() {
		return wkid;
	}

	public double[] getRes() {
		return res;
	}

	public double[] getScale() {
		return scale;
	}

	public Envelope getInitEnvelope() {
		return initEnvelope;
	}

	public Envelope getFullEnvelope() {
		return fullEnvelope;
	}

	public boolean isMapserverFileExist() {
		File file = new File(getMapserverPath());
		if (file.isDirectory()) {
			return false;
		}
		return file.exists();
	}

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
				HttpProtocolParams.setUseExpectContinue(client.getHttpClient().getParams(), false);
			}
		}

		ExplainJson(onCompleteListeners);
	}

	protected AsyncHttpClient client = null;

	public void ExplainJson(OnCompleteListener onCompleteListeners) {
		try {
			RequestParams localRequestParams = new RequestParams();
			localRequestParams.put("f", "json");
			client.get(this.mapURL, localRequestParams, new ResultJsonResponseHandler(onCompleteListeners));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean parserMapServer(String layerName) {
		RandomAccessFile raf = null;
		File file = null;
		String mapServerJson = "";
		file = new File(getMapserverPath());

		if (null == file || !file.exists())
			return false;

		boolean flg = false;
		try {
			raf = new RandomAccessFile(file, "rwd");
			raf.seek(0);
			byte[] buffer = new byte[(int) raf.length()];
			raf.read(buffer);
			String contentStr = new String(buffer);
			mapServerJson = contentStr;// .replaceAll("r|n|null",
			// "").replaceAll("<.*?>", "");

			JSONObject mapserver = new JSONObject(mapServerJson);

			JSONObject jsonObject_tileInfo = mapserver.getJSONObject("tileInfo");
			JSONObject jsonObject_initialExtent = mapserver.getJSONObject("initialExtent");
			JSONObject jsonObject_fullExtent = mapserver.getJSONObject("fullExtent");
			JSONArray jsonArray_lods = jsonObject_tileInfo.getJSONArray("lods");
			JSONObject jsonObject_spatialReference = jsonObject_tileInfo.getJSONObject("spatialReference");
			rows = jsonObject_tileInfo.getInt("rows");
			cols = jsonObject_tileInfo.getInt("cols");
			dpi = jsonObject_tileInfo.getInt("dpi");
			wkid = jsonObject_spatialReference.getInt("wkid");
			double x = jsonObject_tileInfo.getJSONObject("origin").getDouble("x");
			double y = jsonObject_tileInfo.getJSONObject("origin").getDouble("y");
			origin = new Point(x, y);
			double xmin = jsonObject_initialExtent.getDouble("xmin");
			double ymin = jsonObject_initialExtent.getDouble("ymin");
			double xmax = jsonObject_initialExtent.getDouble("xmax");
			double ymax = jsonObject_initialExtent.getDouble("ymax");
			initEnvelope = new Envelope(xmin, ymin, xmax, ymax);
			xmin = jsonObject_fullExtent.getDouble("xmin");
			ymin = jsonObject_fullExtent.getDouble("ymin");
			xmax = jsonObject_fullExtent.getDouble("xmax");
			ymax = jsonObject_fullExtent.getDouble("ymax");
			fullEnvelope = new Envelope(xmin, ymin, xmax, ymax);

			int k = jsonArray_lods.length();
			res = new double[k];
			scale = new double[k];
			for (int i = 0; i < jsonArray_lods.length(); i++) {
				JSONObject jsonObject3 = (JSONObject) jsonArray_lods.opt(i);
				res[i] = jsonObject3.getDouble("resolution");
				scale[i] = jsonObject3.getDouble("scale");
			}

			Log.i("ReturnJson", toString());
			flg = true;
		} catch (Exception e) {
			e.printStackTrace();
			flg = false;
		} finally {
			try {
				if (raf != null)
					raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return flg;
	}

	@Override
	public String toString() {
		return "Json [rows=" + rows + ", cols=" + cols + ", dpi=" + dpi + ", origin=" + origin + ", wkid=" + wkid
			   + ", res=" + Arrays.toString(res) + ", scale=" + Arrays.toString(scale) + ", InitEnvelope="
			   + initEnvelope + ", FullEnvelope=" + fullEnvelope + "]";
	}

	public class ResultJsonResponseHandler extends JsonHttpResponseHandler {
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
					FileOutputStream fileOutputStream = new FileOutputStream(file, true);
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

			if (null == content){
				return;
			}

			boolean success = false;
			try {
				File file = new File(getMapserverPath());
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				// String fixstr = content.replaceAll("\\\"", "");
				FileOutputStream fileOutputStream = new FileOutputStream(file, true);
				fileOutputStream.write(content.getBytes());
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
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactoryEx.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sf;
	}

}