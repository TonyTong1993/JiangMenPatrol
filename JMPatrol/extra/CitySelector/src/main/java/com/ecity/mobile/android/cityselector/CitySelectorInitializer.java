package com.ecity.mobile.android.cityselector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.ecity.mobile.android.cityselector.model.CityModel;
import com.ecity.mobile.android.cityselector.model.DistrictModel;
import com.ecity.mobile.android.cityselector.model.PlaceInfoModel;
import com.ecity.mobile.android.cityselector.model.ProvinceModel;
import com.ecity.mobile.android.cityselector.utils.PreferencesUtil;

public class CitySelectorInitializer {
	public static String EXTRA_LASTCITY = "last_locatedcity";
	public String[] mProvinceDatas;
	public Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	public Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	public Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	public String mCurrentProviceName;
	public String mCurrentCityName;
	public String mCurrentDistrictName = "";
	public String mCurrentZipCode = "";
	private boolean inited = false;
	private Context context;
	private static CitySelectorInitializer instance;

	private PlaceInfoModel currentPlace;

	private CitySelectorInitializer() {

	}

	public static CitySelectorInitializer getInstance() {
		if (null == instance)
			instance = new CitySelectorInitializer();

		return instance;
	}

	public void initialize(Context context) {
		if (null == context)
			return;
		
		if(inited)
			return;
		
		this.context = context;
		currentPlace = getLastPlaceInfo();
		try {
			XmlResourceParser parser = context.getResources().getXml(R.xml.city);
			initProvinceDatas(parser);
		} catch (Exception e) {

		}
		if(null != mProvinceDatas && mProvinceDatas.length >0)
			inited = true;
	}

	public PlaceInfoModel getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(PlaceInfoModel currentPlace) {
		this.currentPlace = currentPlace;
	}

	public void save() {
		
		if (null == currentPlace)
			currentPlace = new PlaceInfoModel();

		currentPlace.setCityName(mCurrentCityName);
		currentPlace.setDistrictName(mCurrentDistrictName);
		currentPlace.setProviceName(mCurrentProviceName);
		currentPlace.setZipCode(mCurrentZipCode);

		setLastCity(currentPlace);
	}

	private List<ProvinceModel> parserProvince(XmlResourceParser parser) {
		if (null == parser)
			return null;
		List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
		try {
			CityModel city = new CityModel();
			DistrictModel district = new DistrictModel();
			ProvinceModel province = new ProvinceModel();

			List<DistrictModel> districtList = new ArrayList<DistrictModel>();
			List<CityModel> cityList = new ArrayList<CityModel>();

			int evnType = parser.getEventType();
			while (evnType != XmlPullParser.END_DOCUMENT) {
				switch (evnType) {
				case XmlPullParser.START_TAG:
					String tag = parser.getName();
					if (tag.equalsIgnoreCase("province")) {
						province = new ProvinceModel();
						province.setName(parser.getAttributeValue(0));
						cityList = new ArrayList<CityModel>();

					} else if (tag.equalsIgnoreCase("city")) {
						city = new CityModel();
						districtList = new ArrayList<DistrictModel>();
						city.setName(parser.getAttributeValue(0));

					} else if (tag.equalsIgnoreCase("district")) {
						district = new DistrictModel();
						district.setName(parser.getAttributeValue(0));
						district.setZipcode(parser.getAttributeValue(1));
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equalsIgnoreCase("district")) {
						if (null != district && null != districtList) {
							districtList.add(district);
						}
					} else if (parser.getName().equalsIgnoreCase("city")) {
						if (null != city && null != cityList) {
							if (null != districtList)
								city.setDistrictList(districtList);
							cityList.add(city);
						}
					} else if (parser.getName().equalsIgnoreCase("province")) {
						if (null != province && null != provinceList) {
							if (null != cityList)
								province.setCityList(cityList);
							provinceList.add(province);
						}
					}

					break;
				default:
					break;
				}
				evnType = parser.next();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return provinceList;
	}

	private void initProvinceDatas(XmlResourceParser parser) {
		List<ProvinceModel> provinceList = parserProvince(parser);
		try {
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				if (null != currentPlace) {
					for (int i = 1; i < provinceList.size(); i++) {
						if (provinceList
								.get(i)
								.getName()
								.equalsIgnoreCase(currentPlace.getProviceName()))
							mCurrentProviceName = provinceList.get(i).getName();
					}
				}
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					if (null != currentPlace) {
						for (int i = 1; i < cityList.size(); i++) {
							if (cityList
									.get(i)
									.getName()
									.equalsIgnoreCase(
											currentPlace.getCityName()))
								mCurrentCityName = cityList.get(i).getName();
						}
					}
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();

					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();

					if (null != currentPlace) {
						for (int i = 1; i < districtList.size(); i++) {
							if (districtList
									.get(i)
									.getName()
									.equalsIgnoreCase(
											currentPlace.getDistrictName())) {
								mCurrentDistrictName = districtList.get(i)
										.getName();
								mCurrentZipCode = districtList.get(i)
										.getZipcode();
							}
						}
					}

				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}

	}

	/***
	 */
	private void setLastCity(PlaceInfoModel place) {
		if (null == context)
			return;
		if (null == place) {
			place = new PlaceInfoModel();
			place.setCityName(mCurrentCityName);
			place.setDistrictName(mCurrentDistrictName);
			place.setProviceName(mCurrentProviceName);
			place.setZipCode(mCurrentZipCode);
		}
		PreferencesUtil.putString(context, EXTRA_LASTCITY, place.toString());
	}

	/***
	 * 
	 * @return
	 */
	private PlaceInfoModel getLastPlaceInfo() {
		if (null == context)
			return new PlaceInfoModel();
		String preference = PreferencesUtil.getString(context, EXTRA_LASTCITY,
				"");
		if (null == preference || preference.length() == 0
				|| preference.equalsIgnoreCase(""))
			return null;

		PlaceInfoModel place = new PlaceInfoModel();
		place.buildFromJson(preference);
		return place;
	}
}
