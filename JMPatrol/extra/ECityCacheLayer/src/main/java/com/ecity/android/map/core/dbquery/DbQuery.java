package com.ecity.android.map.core.dbquery;

import com.ecity.android.map.core.querystruct.IdentiResult;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.ags.identify.IdentifyParameters;


/**
 * @class name：com.ecity.pipenetpalhd.utils
 * @class 测试db范围查询工具类
 * @anthor wangfeng
 * @time 2017/6/16 9:37
 */

public class DbQuery {

	public static IdentiResult[] queryDb(String dbPath,Envelope rect) {
		IdentifyExcuter identifyExcuter = new IdentifyExcuter(dbPath,
															  getIdentifyParameters(rect));
		try {
			IdentiResult[] results = identifyExcuter.getIdentiResults();
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static IdentifyParameters getIdentifyParameters(Envelope rect) {
		IdentifyParameters params = new IdentifyParameters();
		params.setTolerance(8);
		params.setDPI(96);// 设置地图的DPI
		params.setLayerMode(IdentifyParameters.ALL_LAYERS);// 设置识别模式
		Envelope env = new Envelope(412950.2237718022, 2871039.394089725, 452563.29302398243, 2898402.0585285956);
		params.setMapExtent(env);// 设置当前地图范围
		params.setGeometry(rect);// 设置识别位置
		params.setSpatialReference(SpatialReference.create(102113));// 设置坐标系
		params.setMapHeight(640);// 设置地图像素高
		params.setMapWidth(480);// 设置地图像素宽

		return params;
	}
}
