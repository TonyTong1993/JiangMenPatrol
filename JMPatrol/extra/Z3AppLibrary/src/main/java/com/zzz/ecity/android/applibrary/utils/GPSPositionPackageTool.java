package com.zzz.ecity.android.applibrary.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.z3app.android.util.DateUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;

public class GPSPositionPackageTool {
	/***
	 * to json string
	 * 
	 * @return
	 */
	public static Map<String, String> toMap(GPSPositionBean bean) {
		if (null == bean) {
            return null;
        }

		Map<String, String> map = new HashMap<String, String>();

		String time = bean.getTime();
		if (null == time) {
            time = DateUtil.getDateEN();
        }
		if (time.contains(".")) {
			String strs[] = time.split("\\.");
			time = strs[0];
		}

		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getx()))) {
				DecimalFormat fmat = new DecimalFormat("#.000000");
				String dValue = fmat.format(bean.getx());
				map.put("x", dValue);
			} else {
                map.put("x", "0.0");
            }
		} catch (Exception e) {
			map.put("x", "0.0");
		}

		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.gety()))) {
				DecimalFormat fmat = new DecimalFormat("#.000");
				String dValue = fmat.format(bean.gety());
				map.put("y", dValue);
			} else {
                map.put("y", "0.0");
            }
		} catch (Exception e) {

			map.put("y", "0.0");
		}
		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getlon()))) {
				DecimalFormat fmat = new DecimalFormat("#.0000000");
				String dValue = fmat.format(bean.getlon());
				map.put("lon", dValue);
			} else {
                map.put("lon", "0.0");
            }
		} catch (Exception e) {

			map.put("lon", "0.0");
		}
		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getlat()))) {
				DecimalFormat fmat = new DecimalFormat("#.0000000");
				String dValue = fmat.format(bean.getlat());
				map.put("lat", dValue);
			} else {
                map.put("lat", "0.0");
            }
		} catch (Exception e) {

			map.put("lat", "0.0");
		}
		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getacu()))) {
				map.put("acu", String.valueOf(bean.getacu()));
			} else {
                map.put("acu", "0.0");
            }
		} catch (Exception e) {

			map.put("acu", "0.0");
		}
		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getbattery()))) {
				map.put("battery", String.valueOf(bean.getbattery()));
			} else {
                map.put("battery", "0");
            }
		} catch (Exception e) {

			map.put("battery", "0");
		}
		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getUserid()))) {
				map.put("userid", String.valueOf(bean.getUserid()));
			} else {
                map.put("userid", "0");
            }
		} catch (Exception e) {

			map.put("userid", "0");
		}
		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getSpeed()))) {
				map.put("speed", String.valueOf(bean.getSpeed()));
			} else {
                map.put("speed", "0");
            }
		} catch (Exception e) {
			map.put("speed", "0");
		}

		try {
			if (bean.isOverspeed()) {
				map.put("overspeed", "1");
			} else {
                map.put("overspeed", "0");
            }
		} catch (Exception e) {
			map.put("overspeed", "0");
		}

		try {
			if (bean.isRepay()) {
				map.put("repay", "1");
			} else {
                map.put("repay", "0");
            }
		} catch (Exception e) {
			map.put("repay", "0");
		}

		try {
			if (bean.isNightWatch()) {
				map.put("nightWatch", "1");
			} else {
                map.put("nightWatch", "0");
            }
		} catch (Exception e) {
			map.put("nightWatch", "0");
		}

		try {
			if (!StringUtil.isEmpty(String.valueOf(bean.getPlanTaskId()))) {
				map.put("planTaskId", String.valueOf(bean.getPlanTaskId()));
			} else {
                map.put("planTaskId", "-1");
            }
		} catch (Exception e) {
			map.put("planTaskId", "-1");
		}

		try {
			if (bean.isInDetourArea()) {
				map.put("inDetourArea", "1");
			} else {
                map.put("inDetourArea", "0");
            }
		} catch (Exception e) {
			map.put("inDetourArea", "0");
		}
		map.put("time", String.valueOf(time));
		String tag = bean.getTag();
		if (StringUtil.isEmpty(tag)) {
			tag = "0";
		}
		map.put("tag", tag);

		return map;
	}

	/***
	 * to json string
	 * 
	 * @return
	 */
	public static String toJSONString(List<GPSPositionBean> beanList) {
		// JSON格式数据解析对象
		JSONObject jObj = new JSONObject();
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (null != beanList) {
			/*
			 * for (GPSPositionBean gpsPositionBean : beanList) { Map<String,
			 * String> posMap = toMap(gpsPositionBean); JSONObject tmpJsonObj =
			 * new JSONObject(posMap); list.add(tmpJsonObj); }
			 */
			for (int i = 0; i < beanList.size(); i++) {
				Map<String, String> posMap = toMap(beanList.get(i));
				JSONObject tmpJsonObj = new JSONObject(posMap);
				list.add(tmpJsonObj);
			}
		}

		JSONArray jarray = new JSONArray(list);
		try {
			jObj.put("pos", jarray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jObj.toString();
	}

	/***
	 * to jsonArray string
	 * 
	 * @return
	 */
	public static String toJSONArrayString(List<GPSPositionBean> beanList) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (null != beanList) {
			/*
			 * for (GPSPositionBean gpsPositionBean : beanList) { Map<String,
			 * String> posMap = toMap(gpsPositionBean); JSONObject tmpJsonObj =
			 * new JSONObject(posMap); list.add(tmpJsonObj); }
			 */
			for (int i = 0; i < beanList.size(); i++) {
				Map<String, String> posMap = toMap(beanList.get(i));
				JSONObject tmpJsonObj = new JSONObject(posMap);
				list.add(tmpJsonObj);
			}
		}

		JSONArray jarray = new JSONArray(list);

		return jarray.toString();
	}

}