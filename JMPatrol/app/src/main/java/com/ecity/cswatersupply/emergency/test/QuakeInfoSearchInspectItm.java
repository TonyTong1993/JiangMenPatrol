package com.ecity.cswatersupply.emergency.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class QuakeInfoSearchInspectItm {

    public static List<InspectItem> getQuakeInfoSearchInspectItems() {
        List<InspectItem> inspectItems = new ArrayList<InspectItem>();
        InspectItem item1 = new InspectItem(true, false, EInspectItemType.DROPDOWNLIST, "zhenji", "震级", "", "", getMLSelectValues().toString());
        item1.setEdit(true);
        inspectItems.add(item1);

//        InspectItem item2 = new InspectItem(true, true, EInspectItemType.DROPDOWNLIST, "quyu", "区域", "", "", getRegionSelectValues().toString());
//        inspectItems.add(item2);

        InspectItem item3 = new InspectItem(true, false, EInspectItemType.DROPDOWNLIST, "nianfen", "年份", "", "", getYearSelectValues().toString());
        item3.setEdit(true);
        inspectItems.add(item3);

        InspectItem item4 = new InspectItem(true, false, EInspectItemType.TEXT, "address", "地名", "", "", "");
        item4.setEdit(true);
        inspectItems.add(item4);

        return inspectItems;
    }

    public static List<InspectItem> getImportQuakeInfoSearchInspectItems() {
        List<InspectItem> inspectItems = new ArrayList<InspectItem>();
        InspectItem item1 = new InspectItem(true, false, EInspectItemType.DROPDOWNLIST, "zhenji", "震级", "", "", getMLSelectValues().toString());
        item1.setEdit(true);
        inspectItems.add(item1);

        InspectItem item3 = new InspectItem(true, false, EInspectItemType.DROPDOWNLIST, "nianfen", "年份", "", "", getYearSelectValues().toString());
        item3.setEdit(true);
        inspectItems.add(item3);

        return inspectItems;
    }

    public static List<InspectItem> getQuickReportSearchInspectItems(List<String> reporters, int eqid) {
        List<InspectItem> inspectItems = new ArrayList<InspectItem>();
        String eqidStr = "";
        if(0 == eqid) {
            eqidStr = ResourceUtil.getStringById(R.string.item_empty);
        }
        if(StringUtil.isBlank(eqidStr)) {
            eqidStr = String.valueOf(eqid);
        }

        InspectItem item1 = new InspectItem(true, false, EInspectItemType.TEXT, "eqid", "地震编号", eqidStr, "", "");
        item1.setEdit(false);
        inspectItems.add(item1);

        InspectItem item2 = new InspectItem(true, true, EInspectItemType.DROPDOWNLIST, "shangbaoren", "上报人", "", "", getReportid(reporters));
        item2.setEdit(true);
        inspectItems.add(item2);
        return inspectItems;
    }

    public static JSONArray getMLSelectValues() {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject josn = new JSONObject();
            josn.put("alias", "全部");
            josn.put("name", "全部");
            jsonarray.put(josn);

            JSONObject josn1 = new JSONObject();
            josn1.put("alias", "0-2");
            josn1.put("name", "0-2");
            jsonarray.put(josn1);

            JSONObject josn2 = new JSONObject();
            josn2.put("alias", "2-4");
            josn2.put("name", "2-4");
            jsonarray.put(josn2);

            JSONObject josn3 = new JSONObject();
            josn3.put("alias", "4-6");
            josn3.put("name", "4-6");
            jsonarray.put(josn3);

            JSONObject josn4 = new JSONObject();
            josn4.put("alias", "6-9");
            josn4.put("name", "6-9");
            jsonarray.put(josn4);
            return jsonarray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getRegionSelectValues() {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject josn1 = new JSONObject();
            josn1.put("alias", "金坛");
            josn1.put("name", "金坛");
            jsonarray.put(josn1);

            JSONObject josn2 = new JSONObject();
            josn2.put("alias", "溧阳");
            josn2.put("name", "溧阳");
            jsonarray.put(josn2);

            JSONObject josn3 = new JSONObject();
            josn3.put("alias", "常州");
            josn3.put("name", "常州");
            jsonarray.put(josn3);
            return jsonarray;
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONArray getYearSelectValues() {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject josn = new JSONObject();
            josn.put("alias", "全部");
            josn.put("name", "全部");
            jsonarray.put(josn);

            JSONObject josn1 = new JSONObject();
            josn1.put("alias", "1970-1980");
            josn1.put("name", "1970-1980");
            jsonarray.put(josn1);

            JSONObject josn2 = new JSONObject();
            josn2.put("alias", "1980-1990");
            josn2.put("name", "1980-1990");
            jsonarray.put(josn2);

            JSONObject josn3 = new JSONObject();
            josn3.put("alias", "1990-2000");
            josn3.put("name", "1990-2000");
            jsonarray.put(josn3);

            JSONObject josn4 = new JSONObject();
            josn4.put("alias", "2000-2010");
            josn4.put("name", "2000-2010");
            jsonarray.put(josn4);

            JSONObject josn41 = new JSONObject();
            josn41.put("alias", "2010-2015");
            josn41.put("name", "2010-2015");
            jsonarray.put(josn41);

            JSONObject josn5 = new JSONObject();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            year += 1;
            josn5.put("alias", "2015-"+year);
            josn5.put("name", "2015-"+year);
            jsonarray.put(josn5);

            return jsonarray;
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONArray getReporterSelectValues() {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject josn1 = new JSONObject();
            josn1.put("alias", "dongqu_001");
            josn1.put("name", "dongqu_001");
            jsonarray.put(josn1);

            JSONObject josn2 = new JSONObject();
            josn2.put("alias", "dongqu_003");
            josn2.put("name", "dongqu_003");
            jsonarray.put(josn2);
            return jsonarray;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getReportid(List<String> reporters) {
        try {
            JSONArray jsonarray = new JSONArray();
            for(String reporter : reporters) {
                JSONObject josn = new JSONObject();
                josn.put("alias", reporter);
                josn.put("name", reporter);
                jsonarray.put(josn);
            }
            return jsonarray.toString();
        } catch (Exception e) {
        }
        return "";
    }

    public static List<InspectItem> getUnUsualReportInspectItems() {
        List<InspectItem> inspectItems = new ArrayList<InspectItem>();
        InspectItem item1 = new InspectItem(true, true, EInspectItemType.DROPDOWNLIST, "yichangleixing", "异常类型", "", "", getLeixingSelectValues().toString());
        item1.setEdit(true);
        inspectItems.add(item1);

        InspectItem item2 = new InspectItem(true, true, EInspectItemType.GEOMETRY, "yichangdidian", "异常地点", "", "", "");
        item2.setEdit(true);
        inspectItems.add(item2);

        InspectItem item3 = new InspectItem(true, true, EInspectItemType.TEXTEXT, "yichangmiaoshu", "异常描述", "", "", "");
        item3.setEdit(true);
        inspectItems.add(item3);

        InspectItem item4 = new InspectItem(true, false, EInspectItemType.IMAGE, "paizhao", "拍照", "", "", "");
        item4.setEdit(true);
        inspectItems.add(item4);

        InspectItem item5 = new InspectItem(true, false, EInspectItemType.VIDEO, "shipin", "视频", "", "", "");
        item5.setEdit(true);
        inspectItems.add(item5);

        InspectItem item6 = new InspectItem(true, false, EInspectItemType.AUDIO, "luyin", "录音", "", "", "");
        item6.setEdit(true);
        inspectItems.add(item6);

        return inspectItems;
    }

    public static JSONArray getLeixingSelectValues() {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject josn1 = new JSONObject();
            josn1.put("alias", "动物反常");
            josn1.put("name", "动物反常");
            jsonarray.put(josn1);

            JSONObject josn2 = new JSONObject();
            josn2.put("alias", "泉水上涌");
            josn2.put("name", "泉水上涌");
            jsonarray.put(josn2);

            JSONObject josn3 = new JSONObject();
            josn3.put("alias", "怪风狂起");
            josn3.put("name", "怪风狂起");
            jsonarray.put(josn3);

            return jsonarray;
        } catch (Exception e) {
        }
        return null;
    }
}
