package com.ecity.cswatersupply.emergency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.contact.model.Contact;
import com.ecity.cswatersupply.contact.model.ContactGroup;
import com.ecity.cswatersupply.emergency.model.BarCharEntry;
import com.ecity.cswatersupply.emergency.model.EQMonitorStationModel;
import com.ecity.cswatersupply.emergency.model.EQRefugeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.model.QBOModel;
import com.ecity.cswatersupply.emergency.network.response.NoticeModel;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;


public class EmergencyJsonUtil {

    public static List<EarthQuakeInfoModel> parseEarthQuakeInfoModels(JSONObject jsonObj) {
        List<EarthQuakeInfoModel> eqInfoModels = new ArrayList<EarthQuakeInfoModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("EarthQuakeEventInfo");
        if (jsonArray == null) {
            return eqInfoModels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject == null) {
                continue;
            }
            EarthQuakeInfoModel infoModel = new EarthQuakeInfoModel();
            if (isNum(jsonObject.optString("ID"))) {
                infoModel.setId(Integer.valueOf(jsonObject.optString("ID")));
            } else {
                continue;
            }
            if (isNum(jsonObject.optString("Longitude"))) {
                infoModel.setLongitude(Double.valueOf(jsonObject.optString("Longitude")));
            } else {
                infoModel.setLongitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("Latitude"))) {
                infoModel.setLatitude(Double.valueOf(jsonObject.optString("Latitude")));
            } else {
                infoModel.setLatitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("ML"))) {
                infoModel.setML(Double.valueOf(jsonObject.optString("ML")));
            } else {
                infoModel.setML(Double.NaN);
            }
            infoModel.setTime(jsonObject.optString("Time"));
            infoModel.setMS(jsonObject.optString("MS"));
            infoModel.setDepth(jsonObject.optString("Depth"));
            infoModel.setLocation(jsonObject.optString("Location"));
            infoModel.setDescription(jsonObject.optString("Description"));
            infoModel.setImages(jsonObject.optString("Images"));
            infoModel.setWaveFile(jsonObject.optString("WaveFile"));
            infoModel.setMemo(jsonObject.optString("Memo"));
            infoModel.setRegion(jsonObject.optString("Region"));
            infoModel.setType(jsonObject.optString("type"));
            infoModel.setInfluence(jsonObject.optString("Influence"));
            infoModel.setStatus(jsonObject.optString("status"));

            if (isNum(jsonObject.optString("flag"))) {
                infoModel.setFlag(Integer.valueOf(jsonObject.optString("flag")));
            } else {
                infoModel.setFlag(0);
            }
            if (isNum(jsonObject.optString("deaths"))) {
                infoModel.setDeaths(Integer.valueOf(jsonObject.optString("deaths")));
            } else {
                infoModel.setDeaths(0);
            }
            eqInfoModels.add(infoModel);
        }
        return eqInfoModels;
    }

    public static List<EarthQuakeInfoModel> parseImportEarthQuakeModels(JSONObject jsonObj) {
        List<EarthQuakeInfoModel> eqInfoModels = new ArrayList<EarthQuakeInfoModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("resultList");
        if (jsonArray == null) {
            return eqInfoModels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject == null) {
                continue;
            }
            EarthQuakeInfoModel infoModel = new EarthQuakeInfoModel();
            if (isNum(jsonObject.optString("id"))) {
                infoModel.setId(Integer.valueOf(jsonObject.optString("id")));
            } else {
                continue;
            }
            if (isNum(jsonObject.optString("longitude"))) {
                infoModel.setLongitude(Double.valueOf(jsonObject.optString("longitude")));
            } else {
                infoModel.setLongitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("latitude"))) {
                infoModel.setLatitude(Double.valueOf(jsonObject.optString("latitude")));
            } else {
                infoModel.setLatitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("level"))) {
                infoModel.setML(Double.valueOf(jsonObject.optString("level")));
            } else {
                infoModel.setML(Double.NaN);
            }
            infoModel.setTime(jsonObject.optString("time"));
            infoModel.setLocation(jsonObject.optString("addr"));
            infoModel.setDescription(jsonObject.optString("des"));
            infoModel.setImages(jsonObject.optString("pics"));
            infoModel.setRegion(jsonObject.optString("place"));
            infoModel.setInfluence(jsonObject.optString("influence"));
            infoModel.setStatus(jsonObject.optString("status"));

            if (isNum(jsonObject.optString("flag"))) {
                infoModel.setFlag(Integer.valueOf(jsonObject.optString("flag")));
            } else {
                infoModel.setFlag(0);
            }
            if (isNum(jsonObject.optString("deaths"))) {
                infoModel.setDeaths(Integer.valueOf(jsonObject.optString("deaths")));
            } else {
                infoModel.setDeaths(0);
            }
            eqInfoModels.add(infoModel);
        }
        return eqInfoModels;
    }

    public static List<EQMonitorStationModel> parseEQMonitorStationModels(JSONObject jsonObj) {
        List<EQMonitorStationModel> eqStationModels = new ArrayList<EQMonitorStationModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("StationBaseInfo");
        if (null == jsonArray) {
            return eqStationModels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            EQMonitorStationModel stationModel = new EQMonitorStationModel();
            stationModel.setId(jsonObject.optString("ID"));
            stationModel.setStationCode(jsonObject.optString("StationCode"));
            stationModel.setStationName(jsonObject.optString("StationName"));
            if (isNum(jsonObject.optString("Longitude"))) {
                stationModel.setLongitude(Double.valueOf(jsonObject.optString("Longitude")));
            } else {
                stationModel.setLongitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("Latitude"))) {
                stationModel.setLatitude(Double.valueOf(jsonObject.optString("Latitude")));
            } else {
                stationModel.setLongitude(Double.NaN);
            }
            stationModel.setLocation(jsonObject.optString("Location"));
            stationModel.setStationType(jsonObject.optString("StationType"));
            stationModel.setGeologicalType(jsonObject.optString("GeologicalType"));
            stationModel.setLightningLevel(jsonObject.optString("LightningLevel"));
            stationModel.setTime(jsonObject.optString("StartMonitorDate"));
            stationModel.setManager(jsonObject.optString("Manager"));
            stationModel.setTel(jsonObject.optString("Tel"));
            stationModel.setMoblieNumber(jsonObject.optString("MobileNumber"));
            stationModel.setImages(jsonObject.optString("images"));
            stationModel.setMemo(jsonObject.optString("Memo"));
            eqStationModels.add(stationModel);
        }
        return eqStationModels;
    }

    public static List<NoticeModel> parsseEQMessageModelForWH(JSONObject jsonObj) {
        List<NoticeModel> noticeModels = new ArrayList<>();
        JSONArray jsonArray = jsonObj.optJSONArray("items");
        if (jsonArray == null) {
            return noticeModels;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }

            NoticeModel model = new NoticeModel();
            model.setGid(jsonObject.optString("gid"));
            model.setContent(jsonObject.optString("info"));
            model.setCreater(jsonObject.optString("username"));
            model.setCreaterid(jsonObject.optString("userid"));
            model.setCreatetime(jsonObject.optString("createtime"));
            model.setRead(jsonObject.optString("isread").equals("1"));
            noticeModels.add(model);
        }

        return noticeModels;
    }

    public static List<EQRefugeInfoModel> parseEQRefugeModelForCZ(JSONObject jsonObj) {
        List<EQRefugeInfoModel> eqRefugeInfoModels = new ArrayList<>();
        JSONArray jsonArray = jsonObj.optJSONArray("features");
        if (jsonArray == null) {
            return eqRefugeInfoModels;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            JSONObject attributes = jsonObject.optJSONObject("attributes");
            JSONObject geometry = jsonObject.optJSONObject("geometry");

            EQRefugeInfoModel model = new EQRefugeInfoModel();
            model.setName(attributes.optString("名称"));
            model.setId(attributes.optString("编号"));
            model.setRegionalism(attributes.optString("所属行政区"));
            model.setScope(attributes.optString("范围"));
            model.setSpace(attributes.optString("占地面积"));
            model.setSafespace(attributes.optString("有效疏散面积"));
            model.setType(attributes.optString("类型"));
            model.setPeopleNum(attributes.optString("疏散人数"));
            model.setClassfy(attributes.optString("分类"));

            if (isNum(geometry.optString("x"))) {
                model.setLongitude(Double.valueOf(geometry.optString("x")));
            } else {
                model.setLongitude(Double.NaN);
            }
            if (isNum(geometry.optString("y"))) {
                model.setLatitude(Double.valueOf(geometry.optString("y")));
            } else {
                model.setLongitude(Double.NaN);
            }
            eqRefugeInfoModels.add(model);
        }

        return eqRefugeInfoModels;
    }

    public static List<EQRefugeInfoModel> parseEQRefugeForWH(JSONObject jsonObj) {
        List<EQRefugeInfoModel> refugeList = new ArrayList<>();
        return null;
    }

    //由于服务响应的key大小写关系，对于常州地震台站信息单独解析
    public static List<EQMonitorStationModel> parseEQMonitorStationModelsForCZ(JSONObject jsonObj) {
        List<EQMonitorStationModel> eqStationModels = new ArrayList<EQMonitorStationModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("StationBaseInfo");
        if (null == jsonArray) {
            return eqStationModels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            EQMonitorStationModel stationModel = new EQMonitorStationModel();
            stationModel.setId(jsonObject.optString("id"));
            stationModel.setStationCode(jsonObject.optString("stationcode"));
            stationModel.setStationName(jsonObject.optString("stationname"));
            if (isNum(jsonObject.optString("longitude"))) {
                stationModel.setLongitude(Double.valueOf(jsonObject.optString("longitude")));
            } else {
                stationModel.setLongitude(Double.NaN);
            }
            if (isNum(jsonObject.optString("latitude"))) {
                stationModel.setLatitude(Double.valueOf(jsonObject.optString("latitude")));
            } else {
                stationModel.setLongitude(Double.NaN);
            }
            stationModel.setLocation(jsonObject.optString("location"));
            stationModel.setStationType(jsonObject.optString("stationtype"));
            stationModel.setGeologicalType(jsonObject.optString("geologicaltype"));
            stationModel.setLightningLevel(jsonObject.optString("lightninglevel"));
            stationModel.setTime(jsonObject.optString("startmonitordate"));
            stationModel.setManager(jsonObject.optString("manager"));
            stationModel.setTel(jsonObject.optString("tel"));
            stationModel.setMoblieNumber(jsonObject.optString("mobilenumber"));
            stationModel.setImages(jsonObject.optString("images"));
            stationModel.setMemo(jsonObject.optString("memo"));
            eqStationModels.add(stationModel);
        }
        return eqStationModels;
    }

    public static Map<String, Object> parseQBODataModels(JSONObject jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        QBOModel models = new QBOModel();
        JSONObject jsonObject = jsonObj.optJSONObject("total");
        if (null != jsonObject) {
            models.setEqid(jsonObject.optString("eqid"));
            models.setPdead(jsonObject.optString("pdead"));
            models.setPss(jsonObject.optString("pss"));
            models.setPtotal(jsonObject.optString("ptotal"));
            models.setBnum(jsonObject.optString("bnum"));
            models.setLiedu(jsonObject.optString("liedu"));
            models.setMoney(jsonObject.optString("money"));
        }
        map.put("qboData", models);
        List<BarCharEntry> entries = new ArrayList<BarCharEntry>();
        JSONArray jsonArray = jsonObj.optJSONArray("build");
        if (null == jsonArray) {
            return map;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsnObj = jsonArray.optJSONObject(i);
            if (null == jsnObj) {
                continue;
            }
            BarCharEntry entry = new BarCharEntry();
            entry.setValue(jsnObj.optString("count"));
            entry.setStructure(jsnObj.optString("type"));
            entries.add(entry);
        }
        map.put("barCharData", entries);
        return map;
    }

    //解析灾情速报列表信息（原）
//    public static List<EarthQuakeQuickReportModel> parseEQQuickReportModel(JSONObject jsonObj) {
//        List<EarthQuakeQuickReportModel> quickReportmodels = new ArrayList<EarthQuakeQuickReportModel>();
//        JSONArray jsonArray = jsonObj.optJSONArray("items");
//        if (null == jsonArray) {
//            return quickReportmodels;
//        }
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.optJSONObject(i);
//            if (null == jsonObject) {
//                continue;
//            }
//            EarthQuakeQuickReportModel quickReportModel = new EarthQuakeQuickReportModel();
//            JSONObject attributesJsonObject = jsonObject.optJSONObject("attributes");
//            if (null == attributesJsonObject) {
//                continue;
//            }
//            quickReportModel.setEarthQuakeId(Integer.valueOf(attributesJsonObject.optString("eqid")));
//            quickReportModel.setGid(Integer.valueOf(attributesJsonObject.optString("gid")));
//            quickReportModel.setSurveyAddress(attributesJsonObject.optString("address"));
//            quickReportModel.setSurveyPerson(attributesJsonObject.optString("reporter"));
//            quickReportModel.setSurveytTime(attributesJsonObject.optString("reporttime"));
//
//            JSONObject geometryJsonObject = jsonObject.optJSONObject("geometry");
//            if (null == geometryJsonObject) {
//                continue;
//            }
//            quickReportModel.setLongtitude(geometryJsonObject.optDouble("x"));
//            quickReportModel.setLatitude(geometryJsonObject.optDouble("y"));
//
//            quickReportmodels.add(quickReportModel);
//        }
//        return quickReportmodels;
//    }

    //解析灾情速报列表信息
    public static List<EarthQuakeQuickReportModel> parseEQQuickReportModel(JSONObject jsonObj) {
        List<EarthQuakeQuickReportModel> quickReportmodels = new ArrayList<EarthQuakeQuickReportModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("items");
        String directory = jsonObj.optString("virtualDirectory");
        if (null == jsonArray) {
            return quickReportmodels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            EarthQuakeQuickReportModel quickReportModel = new EarthQuakeQuickReportModel();
            quickReportModel.setGid(Integer.valueOf(jsonObject.optString("gid")));
            quickReportModel.setArea(jsonObject.optString("area"));
            quickReportModel.setSurveyAddress(jsonObject.optString("address"));
            quickReportModel.setSurveyPerson(jsonObject.optString("reporter"));
            quickReportModel.setReporterid(jsonObject.optString("reporterid"));
            quickReportModel.setSurveytTime(jsonObject.optString("createtime"));
            quickReportModel.setMemo(jsonObject.optString("memo"));
            if (!StringUtil.isBlank(jsonObject.optString("eqid"))){
                quickReportModel.setEarthQuakeId(Integer.valueOf(jsonObject.optString("eqid")));
            }
            quickReportModel.setDirectory(directory);

            quickReportModel.setLongtitude(Double.valueOf(jsonObject.optString("longitude")));
            quickReportModel.setLatitude(Double.valueOf(jsonObject.optString("latitude")));

            String efile = jsonObject.optString("efile");
            try {
                JSONObject jsonefile = new JSONObject(efile);
                if (jsonefile != null) {
                    quickReportModel.setImageUrl(jsonefile.optString("image"));
                    quickReportModel.setAudioUrl(jsonefile.optString("audio"));
                    quickReportModel.setVideoUrl(jsonefile.optString("video"));
                }


            } catch (JSONException e) {
            }
            quickReportmodels.add(quickReportModel);
        }
        return quickReportmodels;
    }

    //解析现场调查列表信息
    public static List<EarthQuakeQuickReportModel> parseInvestigationModel(JSONObject jsonObj) {
        List<EarthQuakeQuickReportModel> quickReportmodels = new ArrayList<EarthQuakeQuickReportModel>();
        JSONArray jsonArray = jsonObj.optJSONArray("items");
        if (null == jsonArray) {
            return quickReportmodels;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }
            EarthQuakeQuickReportModel quickReportModel = new EarthQuakeQuickReportModel();
            JSONObject attributesJsonObject = jsonObject.optJSONObject("attributes");
            if (null == attributesJsonObject) {
                continue;
            }
            quickReportModel.setGid(Integer.valueOf(attributesJsonObject.optString("gid")));
            quickReportModel.setEarthQuakeId(Integer.valueOf(attributesJsonObject.optString("eqid")));
            quickReportModel.setDid(Integer.valueOf(attributesJsonObject.optString("bid")));
            quickReportModel.setSurveyAddress(attributesJsonObject.optString("address"));
            quickReportModel.setSurveyPerson(attributesJsonObject.optString("reporter"));
            quickReportModel.setSurveytTime(attributesJsonObject.optString("reporttime"));

            JSONObject geometryJsonObject = jsonObject.optJSONObject("geometry");
            if (null == geometryJsonObject) {
                continue;
            }
            if (isNum(geometryJsonObject.optString("x"))) {
                quickReportModel.setLongtitude(Double.valueOf(geometryJsonObject.optString("x")));
            } else {
                quickReportModel.setLongtitude(Double.NaN);
            }
            if (isNum(geometryJsonObject.optString("y"))) {
                quickReportModel.setLatitude(Double.valueOf(geometryJsonObject.optString("y")));
            } else {
                quickReportModel.setLatitude(Double.NaN);
            }

            quickReportmodels.add(quickReportModel);
        }
        return quickReportmodels;
    }

    //解析联系人信息
    /**
     * 针对结果创建分类
     */
    public static ContactGroup constructResultNew(List<Contact> peoples) {
        if (peoples == null || peoples.isEmpty()) {
            return null;
        }
        ContactGroup rootGroup = new ContactGroup();
        for (Iterator iterator = peoples.iterator(); iterator.hasNext();) {
            Contact peopleResult = (Contact) iterator.next();
            rootGroup.getContacts().add(peopleResult);
        }
        //递归调用
        recursionNew(rootGroup);

        return rootGroup;
    }
    /**
     * @param group
     * 将一个分类中的people分类到child中
     *
     * 分类算法原理：
     * 1、最开始时，将所有待分类的人都作为根group的人员列表。
     * 2、从根group开始，根据分类字段key对应的value来决定子分类：
     *    遍历group的所有人，取到字段key的对应的value。
     *    如果是新的value，就作为新的子分类，否则在已有子分类中找到该value对应的分类
     *    并将当前的人（people）当做属于该子分类的人。
     * 3、当前的group所属的所有人都遍历完毕。则递归子分类（第二步）。
     * 4、递归退出条件：key的层级达到设定的层级。
     */

    private static void recursionNew(ContactGroup group) {
        List<String> keys = new ArrayList<String>();
        keys.add("typeName");
        keys.add("prefecturalLevelcity");
        keys.add("cityCounty");
        keys.add("villages");

        if (keys == null || keys.isEmpty()) {
            return;
        }
        List<String> lastKey = group.keyValues;

        int lastKeyLevel = lastKey.size();
        if (lastKeyLevel >= keys.size()) {
            return;
        }

        //使用该字段将group中的people列表分类。
        String curSpecKey = keys.get(lastKeyLevel);

        //无实际作用，仅用来判断是否存在
        Map<String, ContactGroup> mapGroups = new HashMap<String, ContactGroup>();

        //当前的group中待分配的people
        List<Contact> peoples = group.getContacts();

        for (Iterator iterator = peoples.iterator(); iterator.hasNext();) {
            Contact peopleResult = (Contact) iterator.next();
            if (peopleResult == null) {
                System.out.println("must be some error,check key:" + curSpecKey + ",level:" + lastKeyLevel);
                continue;
            }

            String curSpecValue = peopleResult.getValueOfKey(curSpecKey);
            if( StringUtil.isBlank(curSpecValue) ) {
                continue;
            }

            ContactGroup subGroup = null;
            if (mapGroups.containsKey(curSpecValue)) {
                subGroup = mapGroups.get(curSpecValue);
            } else {
                subGroup = new ContactGroup();
                subGroup.setGroupName(curSpecValue);
                mapGroups.put(curSpecValue, subGroup);
                group.getChildGroup().add(subGroup);
                subGroup.keyValues = new ArrayList<String>(lastKey);
                subGroup.keyValues.add(curSpecValue);
            }
            subGroup.getContacts().add(peopleResult);
            iterator.remove();
        }

        //当前分类完毕，递归子分类。
        List<ContactGroup> childGroups = group.getChildGroup();
        for (Iterator iterator = childGroups.iterator(); iterator.hasNext();) {
            ContactGroup eCityGroup = (ContactGroup) iterator.next();
            recursionNew(eCityGroup);
        }
    }

    public static ContactGroup parseContactsModel(JSONObject jsonObj) {
        ContactGroup contactGroup = new ContactGroup();
        List<Contact> contacts = new ArrayList<Contact>();
        List<String> ids = new ArrayList<>();
        JSONArray jsonArray = jsonObj.optJSONArray("resultList");
        if (null == jsonArray) {
            return contactGroup;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (null == jsonObject) {
                continue;
            }

            String id = jsonObject.optString("ID");
            if (ids.contains(id)) {
                continue;
            }
            ids.add(id);
            Contact contact = parseContact(jsonObject);
            contacts.add(contact);
        }

        if (!ListUtil.isEmpty(contacts)) {
            contactGroup = constructResultNew(contacts);
            //contactGroup = modifyContacts2ContactGroup(contacts);
            contactGroup.setGroupName(ResourceUtil.getStringById(R.string.contact_root));
        }
        return contactGroup;
    }

    private static Contact parseContact(JSONObject jsonObject) {
        Contact contact = new Contact();
        contact.setId(jsonObject.optString("ID"));
        contact.setType(jsonObject.optString("typeName"));
        contact.setSex(jsonObject.optString("sex"));
        contact.setName(jsonObject.optString("name"));
        contact.setDepartment(jsonObject.optString("dept"));
        contact.setPosition(jsonObject.optString("position"));
        contact.setTel(jsonObject.optString("tel"));
        contact.setMobile(jsonObject.optString("mobile"));
        contact.setCity(jsonObject.optString("prefecturalLevelcity"));
        contact.setRegion(jsonObject.optString("cityCounty"));
        contact.setVillage(jsonObject.optString("villages"));
        contact.setCommunity(jsonObject.optString("community"));
        return contact;
    }

    private static ContactGroup modifyContacts2ContactGroup(List<Contact> contacts) {
        ContactGroup contactGroup = new ContactGroup();
        List<String> list1 = null;
        List<Contact> list2 = null;
        Map<String, List<String>> map1 = new HashMap<>();
        Map<String, List<Contact>> map2 = new HashMap<>();

        for (Contact model : contacts) {
            if (!map1.containsKey(model.getType())) {
                list1 = new ArrayList<String>();
            } else {
                list1 = map1.get(model.getType());
            }
            if (!list1.contains(model.getDepartment())) {
                list1.add(model.getDepartment());
            }
            map1.put(model.getType(), list1);

            if (!map2.containsKey(model.getDepartment())) {
                list2 = new ArrayList<Contact>();
            } else {
                list2 = map2.get(model.getDepartment());
            }
            if (!list2.contains(model.getName())) {
                list2.add(model);
            }
            map2.put(model.getDepartment(), list2);
        }

        List<ContactGroup> firstGroup = new ArrayList<ContactGroup>();
        Set<String> typeSet = map1.keySet();
        for (String str : typeSet) {
            ContactGroup tempGroup = new ContactGroup();
            tempGroup.setGroupName(str);
            firstGroup.add(tempGroup);
        }
        contactGroup.setChildGroup(firstGroup);

        for (ContactGroup group : firstGroup) {
            List<String> depts = map1.get(group.getGroupName());
            List<ContactGroup> secondGroup = new ArrayList<ContactGroup>();
            for (String str : depts) {
                List<Contact> contacts1 = map2.get(str);
                ContactGroup tempGroup = new ContactGroup();
                tempGroup.setGroupName(str);
                tempGroup.setContacts(contacts1);
                secondGroup.add(tempGroup);
            }
            group.setChildGroup(secondGroup);
        }

        return contactGroup;
    }

    public static InspectItem parseEQQuickReportInspectInfo(JSONObject jsonObj) {
        InspectItem adaptItems = InspectItemAdapter.adaptEQZQSBItems(jsonObj);
//        setReposrterDefaultInfo(adaptItems);
        return adaptItems;
    }

  /*  private static void setReporterDefaultInfo(InspectItem adaptItems) {
        if (null == adaptItems) {
            return;
        }
            InspectItem itemTemp = adaptItems.get(i);

            if ("REPORTERID".equalsIgnoreCase(itemTemp.getName())) {
                adaptItems.get(i).setDefaultValue(HostApplication.getApplication().getCurrentUser().getId());
                continue;
            }
            if ("REPORTER".equalsIgnoreCase(itemTemp.getName())) {
                adaptItems.get(i).setDefaultValue(HostApplication.getApplication().getCurrentUser().getTrueName());
                continue;
            }
            if (ListUtil.isEmpty(itemTemp.getChilds())) {
                continue;
            } else {
                setReporterDefaultInfo(itemTemp.getChilds());
            }
    }*/

    private static boolean isNum(String str) {
        return StringUtil.isNum(str) && !("".equals(str));
    }
}
