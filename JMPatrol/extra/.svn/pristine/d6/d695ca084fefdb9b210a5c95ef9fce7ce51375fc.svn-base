package com.ecity.android.map.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ecity.android.map.core.ServiceExecutorManager;
import com.ecity.android.map.core.tile.TileSaveCallable;
import com.ecity.android.map.core.tile.TileSaveFutureTask;
import com.ecity.android.map.core.tile.TimeInfo;
import com.ecity.android.map.layer.ECityCacheableTiledServiceLayerInfo.OnCompleteListener;
import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZiZhengzhuan
 *
 */
public class ECityCacheableTiledServiceLayer extends TiledServiceLayer
		implements OnCompleteListener {
	
	private boolean searchLocalTile = false;
	private String cachePath;// 缓存目录
	private ECityCacheableTiledServiceLayerInfo layerInfoParser = null;
	private String layerUrl = "";
	private String layerName = "";
	private SpatialReference localSpatialReference;
	private Envelope fullEnvelope;
	private Envelope initEnvelope;

	private Point localPoint;
	private double[] arrayOfDoublescale;
	private double[] arrayOfDoubleres;
	private int cols;
	private int dpi;
	private int rows;
	private int k;
	private TileInfo localTileInfo;
	private boolean useCustomEnvelope = false;
	private boolean isInited;
	private Map<String, String> requestParam = new HashMap<String, String>();
	/***
	 * 设置图层完整范围，默认从对应的地图服务元数据里获取，但手动调用了setFullEnvelope， 或者手动调用setInitEnvelope
	 * 时，默认从元数据获取的这个值将无效
	 * 
	 * @param fullEnvelope
	 */
	public void setFullEnvelope(Envelope fullEnvelope) {
		this.fullEnvelope = fullEnvelope;
		useCustomEnvelope = true;
	}

	/***
	 * 设置图层的初始范围，默认从对应的地图服务元数据里获取，但手动调用了setFullEnvelope， 或者手动调用setInitEnvelope
	 * 时，默认从元数据获取的这个值将无效
	 * 
	 * @param initEnvelope
	 */
	public void setInitEnvelope(Envelope initEnvelope) {
		this.initEnvelope = initEnvelope;
		useCustomEnvelope = true;
	}

	public ECityCacheableTiledServiceLayer(String cachePath, String url,
			String layerName, boolean searchLocalTile) {
		super(url);
		this.searchLocalTile = searchLocalTile;
		this.layerUrl = url;
		this.layerName = layerName;
		this.cachePath = cachePath;
		isInited = false;
		setName(layerName);
		try {
			initCachData();
			// 读取文件
			layerInfoParser = new ECityCacheableTiledServiceLayerInfo(
					cachePath, url, layerName);
			if (searchLocalTile) {
				if (layerInfoParser.isMapserverFileExist()) {
					if (layerInfoParser.parserMapServer(layerName)) {
						initCachData();
						this.initLayer();
					} else {
						layerInfoParser.requestMapServerInfo(this);
					}
				} else {
					layerInfoParser.requestMapServerInfo(this);
				}
			} else {
				layerInfoParser.requestMapServerInfo(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			layerInfoParser.parserMapServer(layerName);
			// 设置
			localSpatialReference = SpatialReference.create(layerInfoParser
					.getWkid());
			setDefaultSpatialReference(localSpatialReference);

			if (!useCustomEnvelope) {
				fullEnvelope = layerInfoParser.getFullEnvelope();
				initEnvelope = layerInfoParser.getInitEnvelope();
			} else {
				if (null == fullEnvelope) {
					fullEnvelope = layerInfoParser.getFullEnvelope();
				}

				if (null == initEnvelope) {
					initEnvelope = layerInfoParser.getInitEnvelope();
				}
			}

			setFullExtent(fullEnvelope);
			setInitialExtent(initEnvelope);

			localPoint = layerInfoParser.getOrigin();
			arrayOfDoublescale = layerInfoParser.getScale();
			arrayOfDoubleres = layerInfoParser.getRes();
			cols = layerInfoParser.getCols();
			dpi = layerInfoParser.getDpi();
			rows = layerInfoParser.getRows();
			k = arrayOfDoublescale.length;
			localTileInfo = new TileInfo(localPoint, arrayOfDoublescale, arrayOfDoubleres, k, dpi, rows, cols);
			setTileInfo(localTileInfo);
			setUrl(layerUrl);
			setName(layerName);
			isInited = true;
			super.initLayer();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	@Override
	public TileInfo getTileInfo() {
		return this.localTileInfo;
	}

	@Override
	public Envelope getFullExtent() {
		return this.fullEnvelope;
	}

	@Override
	public SpatialReference getSpatialReference() {
		return this.localSpatialReference;
	}

	// 获得瓦片
	@Override
	protected byte[] getTile(int level, int col, int row) throws Exception {
		
		byte[] tileData = null;
		if (searchLocalTile) {
			try {
				tileData = getLocalTile(level, col, row);
				if (null != tileData && 0 != tileData.length) {
					return tileData;
				} else {
					tileData = requestTileImage(level, col, row);
					if (null != tileData) {// 保存到本地
						saveLocalTile(level, col, row, tileData);
					}
					
					return tileData;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			tileData = requestTileImage(level, col, row);
		}
		
		return tileData;
	}

	/***
	 * 从服务端请求瓦片数据
	 * @throws Exception 
	 */
	public byte[] requestTileImage(int level, int col, int row) throws Exception {
		String tileURL =  layerUrl + "/tile/" + level + "/" + row + "" + "/" + col;
		return com.esri.core.internal.io.handler.a.a(tileURL,requestParam);
	}
	
	// 初始化缓存路径
	private void initCachData() {
		File file = new File(getMapPathforCache(0, 0));
		if (!file.exists()) {
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
		if (!file.exists()){
			return null;
		}
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

	@Override
	public void onCompleteListener(boolean flg, String msg) {
		if (flg) {
			initCachData();
			this.initLayer();
		}
	}
}