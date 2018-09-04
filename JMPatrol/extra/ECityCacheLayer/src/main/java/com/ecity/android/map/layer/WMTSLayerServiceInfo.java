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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/***
 *
 * Description:<br/>
 *
 * @version V1.0
 * @Author ZiZhengzhuan
 * @CreateDate 2015年2月29日
 * @email
 */
public class WMTSLayerServiceInfo {

	private String   layerName;// 图层名称
	private String   cachePath;// 缓存目录
	private int      rows; // 瓦片高度
	private int      cols; // 瓦片宽度
	private int      dpi; // //瓦片分辨率
	private Point    origin; // 地图原点
	private int      wkid; // 地图坐标ID
	private double[] res; // 地图分辨率
	private double[] scale; // 比例尺
	private Envelope initEnvelope; // 初始化范围
	private Envelope fullEnvelope; // 最大范围

	private String SERVICE       = "WMTS";
	private String VERSION       = "1.0.0";
	private String REQUEST       = "GetTile";
	private String STYLE         = "default";
	private String TILEMATRIXSET = "sss";
	private String OPTIONTYPE    = "KVP";
	private String urlType       = "ArcGIS";// OGC

	/***
	 *
	 * @param cachePath
	 *            eg: ..//ECity//cache//map//
	 * @param layerName
	 */
	public WMTSLayerServiceInfo(String cachePath, String layerName) {
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

	public String getOPTIONTYPE() {
		return OPTIONTYPE;
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

	public String getSERVICE() {
		if (null == SERVICE || SERVICE.isEmpty()) {
			SERVICE = "WMTS";
		}
		return SERVICE;
	}

	public String getVERSION() {
		if (null == VERSION || VERSION.isEmpty()) {
			VERSION = "1.0.0";
		}
		return VERSION;
	}

	public String getREQUEST() {
		if (null == REQUEST || REQUEST.isEmpty()) {
			REQUEST = "GetTile";
		}
		return REQUEST;
	}

	public String getUrlType() {
		return urlType;
	}

	public String getSTYLE() {
		if (null == STYLE || STYLE.isEmpty()) {
			STYLE = "default";
		}
		return STYLE;
	}

	public String getTILEMATRIXSET() {
		if (null == TILEMATRIXSET || TILEMATRIXSET.isEmpty()) {
			TILEMATRIXSET = "sss";
		}
		return TILEMATRIXSET;
	}

	public boolean isMapserverFileExist() {
		File file = new File(getMapserverPath());
		if (file.isDirectory()) {
			return false;
		}
		return file.exists();
	}

	public String getMapserverPath() {
		String lastStr = cachePath.substring(cachePath.length() - 2);
		String mapServerPath = "";
		if (lastStr.contains("//")) {
			mapServerPath = cachePath + layerName + "//mapserver.json";
		} else {
			mapServerPath = cachePath + "//" + layerName + "//mapserver.json";
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
			try {
				JSONObject jsonObject_options = new JSONObject(mapServerJson).getJSONObject("options");

				if (null != jsonObject_options) {
					SERVICE = jsonObject_options.getString("SERVICE");
					VERSION = jsonObject_options.getString("VERSION");
					REQUEST = jsonObject_options.getString("REQUEST");
					STYLE = jsonObject_options.getString("STYLE");
					TILEMATRIXSET = jsonObject_options.getString("TILEMATRIXSET");
					OPTIONTYPE = jsonObject_options.getString("OPTIONTYPE");

					if (jsonObject_options.has("urlType")) {
						urlType = jsonObject_options.getString("urlType");
					}
				}
			} catch (Exception e) {
			}
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
		return "Json [rows=" + rows + ", cols=" + cols + ", dpi=" + dpi + ", origin=" + origin + ", wkid=" + wkid + ", res=" +
			   Arrays.toString(res) + ", scale=" + Arrays.toString(scale) + ", InitEnvelope=" + initEnvelope + ", FullEnvelope=" +
			   fullEnvelope + "]";
	}
}