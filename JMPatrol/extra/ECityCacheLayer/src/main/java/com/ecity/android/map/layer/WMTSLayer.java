package com.ecity.android.map.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.ecity.android.map.core.ServiceExecutorManager;
import com.ecity.android.map.core.tile.TileSaveCallable;
import com.ecity.android.map.core.tile.TileSaveFutureTask;
import com.ecity.android.map.core.tile.TimeInfo;
import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/***
 * 
 * Description:<br/>
 *
 * @version V1.0
 * @Author ZiZhengzhuan
 * @CreateDate 2015年2月29日
 * @email
 */
public class WMTSLayer extends TiledServiceLayer {
	private Map<String, String> requestParam = new HashMap<String, String>();
	private boolean searchLocalTile = false;
	private String cachePath;// 缓存目录
	private WMTSLayerServiceInfo mapserviceInfo = null;
	private String layerUrl = "";
	private String metaUrl = "";
	private String layerName = "";
	private SpatialReference localSpatialReference;
	private Envelope FullEnvelope;
	private Envelope initEnvelope;

	private Point localPoint;
	private double[] arrayOfDoublescale;
	private double[] arrayOfDoubleres;
	private int cols;
	private int dpi;
	private int rows;
	private int k;
	private TileInfo localTileInfo;
	private boolean isInited;
	private MetasDownloadHandler handler;

	/***
	 * 
	 * @param cachePath
	 * @param url
	 * @param layerName
	 * @param searchLocalTile
	 */
	public WMTSLayer(String cachePath, String url, String metaUrl,
			String layerName, boolean searchLocalTile) {
		super(true);
		//taskList = new ConcurrentLinkedQueue<TileGetFutureTask>();
		handler = new MetasDownloadHandler(this);
		this.searchLocalTile = searchLocalTile;
		this.layerUrl = url;
		this.metaUrl = metaUrl;
		this.layerName = layerName;
		this.cachePath = cachePath;
		this.isInited = false;
		setName(layerName);

		initCachData();
		// 读取文件
		mapserviceInfo = new WMTSLayerServiceInfo(cachePath, layerName);
		if (mapserviceInfo.isMapserverFileExist()) {
			if (mapserviceInfo.parserMapServer(layerName)) {
				if (searchLocalTile) {
					initCachData();
				}
				this.initLayer();
			}
		} else {
			requestMetas();
		}
	}
	
	@Override
	protected void initLayer() {
		if (isInited) {
			return;
		}
		if (getID() == 0) {
			this.nativeHandle = create();
		}
		try {
			mapserviceInfo.parserMapServer(layerName);
			// 设置
			localSpatialReference = SpatialReference.create(mapserviceInfo
					.getWkid());
			setDefaultSpatialReference(localSpatialReference);
			FullEnvelope = mapserviceInfo.getFullEnvelope();
			initEnvelope = mapserviceInfo.getInitEnvelope();
			setFullExtent(FullEnvelope);
			setInitialExtent(initEnvelope);

			localPoint = mapserviceInfo.getOrigin();
			arrayOfDoublescale = mapserviceInfo.getScale();
			arrayOfDoubleres = mapserviceInfo.getRes();
			cols = mapserviceInfo.getCols();
			dpi = mapserviceInfo.getDpi();
			rows = mapserviceInfo.getRows();
			k = arrayOfDoublescale.length;
			localTileInfo = new TileInfo(localPoint, arrayOfDoublescale,
					arrayOfDoubleres, k, dpi, rows, cols);
			setTileInfo(localTileInfo);
			setUrl(layerUrl);
			setName(layerName);
			isInited = true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		super.initLayer();
	}
	@Override
	public TileInfo getTileInfo() {
		return this.localTileInfo;
	}

	@Override
	public Envelope getFullExtent() {
		return this.FullEnvelope;
	}

	@Override
	public SpatialReference getSpatialReference() {
		return this.localSpatialReference;
	}

	// 获得瓦片
	@Override
	protected byte[] getTile(int level, int col, int row) throws Exception {
		byte[] localtile = null;
		if (searchLocalTile) {
			try {
				localtile = getLocalTile(level, col, row);
				if (null != localtile && 0 != localtile.length) {
					return localtile;
				} else {
					byte[] servertile = requestTileImage(level, col, row);
					if (null != servertile){// 保存到本地
						saveLocalTile(level, col, row, servertile);
					}
					return servertile;
				}
			} catch (Exception e) {
			}
		} else {
			localtile = requestTileImage(level, col, row);
		}

		return localtile;
	}
	/***
	 * 从服务端请求瓦片数据
	 * @throws Exception 
	 */
	public byte[] requestTileImage(int level, int col, int row) throws Exception {
		return com.esri.core.internal.io.handler.a.a(getTiledServiceURL(level,col,row),requestParam);
	}
	
	private String getTiledServiceURL(int level, int col, int row) {
		if (null == mapserviceInfo) {
			return layerUrl + "/tile/" + level + "/" + row + "" + "/" + col;
		}

		if ("OGC".equalsIgnoreCase(mapserviceInfo.getUrlType())) {
			StringBuilder url = new StringBuilder(layerUrl);
			url.append("&X=").append(col).append("&Y=").append(row)
					.append("&L=").append(level);
			return url.toString();
		} else {
			return layerUrl + "/tile/" + level + "/" + row + "" + "/" + col;
		}
	}

	// 初始化缓存路径
	private void initCachData() {
		File file = new File(getMapPathforCache(0, 0));
		if (!file.exists()){
			file.mkdirs();
		}
	}

	/**
	 * 判断文件夹是否存在，如果不存在就创建
	 * 
	 * @param path
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

	private String getMapPathforCache(int level, int col) {
		String lastStr = cachePath.substring(cachePath.length() - 2);
		String fixedcachePath = "";
		if (lastStr.contains("/")) {
			fixedcachePath = cachePath + layerName + "//L" + level + "//C"
					+ col + "//";
		} else {
			fixedcachePath = cachePath + "//" + layerName + "//L" + level
					+ "//C" + col + "//";
		}
		hasFileDir(fixedcachePath);
		return fixedcachePath;
	}

	// 从SD卡读取地图图片
	private byte[] getLocalTile(int level, int col, int row) {
		String tileFilePath = getMapPathforCache(level, col) + row + ".zzz";
		File file = new File(tileFilePath);
		if (!file.exists())
			return null;
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(tileFilePath);
			if (null == bitmap)
				return null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}
	
	// 保存图片到本地
	private boolean saveLocalTile(int level, int col, int row, byte[] tile) {
		
		if (null == tile){
			return false;
		}
		
		if (tile.length == 0){
			return false;
		}
		
		TileSaveCallable callable = new TileSaveCallable(level,col,row,layerUrl,
				new TimeInfo(),tile,getMapPathforCache(level,col));
		TileSaveFutureTask futureTask = new TileSaveFutureTask(callable);
		ServiceExecutorManager.getServiceExecutor().execute(futureTask);
		return true;
	}

	private void requestMetas() {
		if (null == metaUrl || "".equalsIgnoreCase(metaUrl)) {
			return;
		}
		
		MetasDownloadRunnable runnable = new MetasDownloadRunnable(handler, metaUrl, mapserviceInfo.getMapserverPath());
		ServiceExecutorManager.getServiceExecutor().execute(runnable);
	}

	private static class MetasDownloadHandler extends Handler {
		private WeakReference<WMTSLayer> outer;

		public MetasDownloadHandler(WMTSLayer activity) {
			outer = new WeakReference<WMTSLayer>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MetasDownloadRunnable.GETMETA_SUCCESS) {
				WMTSLayer layer = outer.get();
				if (null != layer) {
					layer.initLayer();
				}
			}
		}
	}

	public class MetasDownloadRunnable implements Runnable {
		public static final int GETMETA_SUCCESS = 0x100;
		private Handler handler;
		private String configURL;
		private String cachePath;

		public MetasDownloadRunnable(Handler handler, String configURL,
				String cachePath) {
			this.handler = handler;
			this.configURL = configURL;
			this.cachePath = cachePath;
		}

		/**
		 * @return 从服务器端下载得到地图服务配置
		 */
		@Override
		public void run() {
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpParams params = null;
				params = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 1000 * 30);
				HttpConnectionParams.setSoTimeout(params, 1000 * 30);
				httpclient.setParams(params);
				HttpGet httpRequest = new HttpGet(configURL);
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 在线读取地图配置文件
					InputStream stream = httpResponse.getEntity().getContent();
					try {
						String vstr = inputInputStream2StringReader(stream,
								null);
						try {
							File file = new File(cachePath);
							if (file.exists()) {
								file.delete();
							}
							file.createNewFile();
							FileOutputStream fileOutputStream = new FileOutputStream(
									file, true);
							fileOutputStream.write(vstr.getBytes());
							fileOutputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null != handler) {
				handler.sendEmptyMessage(GETMETA_SUCCESS);
			}
		}

		/**
		 * 利用BufferedReader实现Inputstream转换成String <功能详细描述>
		 * 
		 * @param in
		 * @return String
		 */
		public String inputInputStream2StringReader(InputStream in,
				String encode) {
			String str = "";
			try {
				if (encode == null || encode.equals("")) {
					// 默认以utf-8形式
					encode = "utf-8";
				}
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, encode));
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
}