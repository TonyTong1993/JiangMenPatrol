package com.ecity.android.map.test;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ecity.android.map.core.Constants;
import com.ecity.android.map.core.dbquery.DbQuery;
import com.ecity.android.map.core.graphic.CustomRenderer;
import com.ecity.android.map.core.local.PipeDB;
import com.ecity.android.map.core.querystruct.IdentiResult;
import com.ecity.android.map.core.util.GeometryUtil;
import com.ecity.android.map.core.meta.DbMetaInfo;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Grid;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity {

	private MapView                    mapView;
	private ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer;
	private GraphicsLayer              graphicsLayer;
	private static final String MAP_URL = "http://192.168.8.135:6080/arcgis/rest/services/fzdxt/MapServer";//福州地形图

	private Envelope rect;//查询的范围
	private List<Graphic> graphicList;


	protected void onCreate(Bundle savedInstanceState) {

		initView();
		startQueryDb("",rect);
	}

	/**
	 * 根据查询范围去操作本地db，查询得到对应范围结果集
	 * @param rect
	 */
	private void startQueryDb(final String dbPath,final Envelope rect) {
		Runnable runable = new Runnable() {
			@Override
			public void run() {
				IdentiResult[] results = DbQuery.queryDb(dbPath,rect);
				//查询本地db文件会调用此方法PipeDB.getInstance().isSQLiteDatabaseExisted()，在此方法中封装从本地db得到dbMetaInfoList的方法
				List<DbMetaInfo> dbMetaInfoList= PipeDB.getInstance().getDbMetaInfosFromDB();
				if (null != results) {
					if (results.length > 0) {
						Message msg = Message.obtain();
						msg.what = Constants.QUERY_DB_FINISH;
						msg.obj = results;
						handler.sendMessage(msg);
					}
				}
			}
		};

	}

	private void initView() {

		mapView.getGrid().setVisibility(false);
		mapView.setBackgroundColor(Color.argb(0, 255, 255, 255));
		mapView.setMapBackground(Color.WHITE, Color.argb(0, 255, 255, 255), 0, 0);
		mapView.getGrid().setType(Grid.GridType.NONE);
		mapView.setExtent(new Envelope(412950.2237718022, 2871039.394089725, 452563.29302398243, 2898402.0585285956));//地图加载范围
		arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(MAP_URL);
		graphicsLayer = new GraphicsLayer();

		// 加载图层
		mapView.addLayer(arcGISTiledMapServiceLayer);
		mapView.addLayer(graphicsLayer);// 添加客户端要素图层
		rect = new Envelope(433695, 2885745, 443657, 2893147);//小片查询范围
		//全区查询范围，一次从数据库加载过多内存溢出应分页查询
//		rect = new Envelope(412950.2237718022, 2871039.394089725, 452563.29302398243, 2898402.0585285956);
		graphicList=new ArrayList<>();

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.QUERY_DB_FINISH) {
				IdentiResult[] result = (IdentiResult[]) msg.obj;
				Graphic graphic = null;
				for (int i = 0; i < result.length; i++) {
					Geometry geometry = result[i].getGeometry();
					//此处由于离线查询管段没有结果，所以对于点击查询需要判断几何类型进行坐标范围存储
					String typeName = geometry.getType().name();
					Point point = GeometryUtil.GetGeometryCenter(geometry);
					Map<String, Object> atts = new HashMap<String, Object>();
					atts = result[i].getAttributes();
					atts.put("location", String.valueOf(point.getX()) + "," + String.valueOf(point.getY()));

					if (typeName.equalsIgnoreCase("point")) {
						graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED(), atts, 0);
					} else if (typeName.equalsIgnoreCase("polyline")) {
						graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_LINE_SOLID(), atts, 0);
					} else if (typeName.equalsIgnoreCase("polygon")) {
						graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_POLYGON_FILL(), atts, 0);
					}
					graphicList.add(graphic);
					//由于graphic过多导致程序崩溃，屏蔽
//					graphicsLayer.addGraphic(graphic);
//					LogUtil.i("画graphic。。。。。。。。。","第"+i+"个");
				}
			}
		}
	};
}
