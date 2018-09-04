/**
 * @Title: ReturnJson.java
 * @Package com.ecity.android.plugin.maps
 * @Description:
 * @author Jimmy
 * @company 武汉易思迪信息科技有限公司 Corporation 2015
 * @date 2015年6月17日 下午4:31:30
 */
package com.ecity.android.map.layer;

import android.util.Log;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/***
 *
 * Description:<br/>
 *
 * @version V1.0
 * @Author ZiZhengzhuan
 * @CreateDate 2015年2月29日
 * @email
 */
public class DBLayerServiceInfo {
	private String   mapServerPath;		//地图服务路径
	private Point    origin; 			// 地图原点
	private int      wkid; 				// 地图坐标ID
	private Envelope initEnvelope; 		// 初始化范围
	private Envelope fullEnvelope; 		// 最大范围
	private double  minScale;           // 最小比例尺
	private double  maxScale;           // 最大比例尺
	/***
	 *
	 * @param mapServerPath
	 *            eg: ..//ECity//cache//map//
	 */
	public DBLayerServiceInfo(String mapServerPath) {
		this.mapServerPath = mapServerPath;
	}

	public Point getOrigin() {
		return origin;
	}

	public int getWkid() {
		return wkid;
	}

	public Envelope getInitEnvelope() {
		return initEnvelope;
	}

	public Envelope getFullEnvelope() {
		return fullEnvelope;
	}


	public double getMinScale() {
		return minScale;
	}

	public double getMaxScale() {
		return maxScale;
	}

	public boolean isMapserverFileExist() {
		File file = new File(getMapserverPath());
		if (file.isDirectory()) {
			return false;
		}
		return file.exists();
	}

	public String getMapserverPath() {
		String lastStr = mapServerPath.substring(mapServerPath.length() - 2);
		String mapServerPath = "";
		if (lastStr.contains("//")) {
			mapServerPath = mapServerPath + "mapserver.json";
		} else {
			mapServerPath = mapServerPath + "//mapserver.json";
		}
		return mapServerPath;
	}

	public boolean parserMapServer(String layerName) {
		RandomAccessFile raf = null;
		File file = null;
		String mapServerJson = "";
		file = new File(getMapserverPath());

		if (null == file || !file.exists()) {
			return false;
		}

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
			JSONObject jsonObject_spatialReference = jsonObject_tileInfo.getJSONObject("spatialReference");
			wkid = jsonObject_spatialReference.getInt("wkid");

			minScale = jsonObject_spatialReference.optDouble("minScale");
			maxScale = jsonObject_spatialReference.optDouble("maxScale");

			if( minScale < 1 ) {
				minScale = 1000000;
			}

			if( maxScale < 1 ) {
				maxScale = 5;
			}

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
			Log.i("ReturnJson", toString());
			flg = true;
		} catch (Exception e) {
			e.printStackTrace();
			flg = false;
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return flg;
	}

	@Override
	public String toString() {
		return "Json [ origin=" + origin + ", wkid=" + wkid + ", res=" + ", InitEnvelope=" + initEnvelope + ", FullEnvelope=" +
			   fullEnvelope + "]";
	}
}