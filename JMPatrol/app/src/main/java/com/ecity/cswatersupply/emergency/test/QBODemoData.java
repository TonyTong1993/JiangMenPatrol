package com.ecity.cswatersupply.emergency.test;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.BarCharEntry;
import com.ecity.cswatersupply.emergency.model.QBOGridModel;

public class QBODemoData {

    public static List<String> getRegions() {
        List<String> condition = new ArrayList<String>();
        condition.add("全部");
        condition.add("汉口");
        condition.add("武昌");
        condition.add("江夏");
        return condition;
    }

    public static List<QBOGridModel> getRandomGridDataSource() {
        List<QBOGridModel> items = new ArrayList<QBOGridModel>();
        List<QBOGridModel> list1 = new ArrayList<QBOGridModel>();
        List<QBOGridModel> list2 = new ArrayList<QBOGridModel>();
        List<QBOGridModel> list3 = new ArrayList<QBOGridModel>();
        List<QBOGridModel> list4 = new ArrayList<QBOGridModel>();
        List<QBOGridModel> list5 = new ArrayList<QBOGridModel>();
        List<QBOGridModel> list6 = new ArrayList<QBOGridModel>();

        list1.add(new QBOGridModel("死亡人数","110","人",R.color.red));
        list1.add(new QBOGridModel("死亡人数","100","人",R.color.red));
        list1.add(new QBOGridModel("死亡人数","150","人",R.color.red));
        list1.add(new QBOGridModel("死亡人数","170","人",R.color.red));
        list1.add(new QBOGridModel("死亡人数","160","人",R.color.red));
        list1.add(new QBOGridModel("死亡人数","120","人",R.color.red));
 
        list2.add(new QBOGridModel("受伤人数","500","人",R.color.red));
        list2.add(new QBOGridModel("受伤人数","405","人",R.color.red));
        list2.add(new QBOGridModel("受伤人数","520","人",R.color.red));
        list2.add(new QBOGridModel("受伤人数","580","人",R.color.red));
        list2.add(new QBOGridModel("受伤人数","600","人",R.color.red));
        list2.add(new QBOGridModel("受伤人数","580","人",R.color.red));

        list3.add(new QBOGridModel("受灾人数","989","人",R.color.red));
        list3.add(new QBOGridModel("受灾人数","909","人",R.color.red));
        list3.add(new QBOGridModel("受灾人数","789","人",R.color.red));
        list3.add(new QBOGridModel("受灾人数","900","人",R.color.red));
        list3.add(new QBOGridModel("受灾人数","999","人",R.color.red));
        list3.add(new QBOGridModel("受灾人数","850","人",R.color.red));

        list4.add(new QBOGridModel("地震烈度","4.1","度",R.color.col_app_theme));
        list4.add(new QBOGridModel("地震烈度","2.1","度",R.color.col_app_theme));
        list4.add(new QBOGridModel("地震烈度","3.6","度",R.color.col_app_theme));
        list4.add(new QBOGridModel("地震烈度","4.7","度",R.color.col_app_theme));
        list4.add(new QBOGridModel("地震烈度","6.1","度",R.color.col_app_theme));
        list4.add(new QBOGridModel("地震烈度","5.1","度",R.color.col_app_theme));

        list5.add(new QBOGridModel("房屋破坏","200","间",R.color.slateblue));
        list5.add(new QBOGridModel("房屋破坏","220","间",R.color.slateblue));
        list5.add(new QBOGridModel("房屋破坏","300","间",R.color.slateblue));
        list5.add(new QBOGridModel("房屋破坏","400","间",R.color.slateblue));
        list5.add(new QBOGridModel("房屋破坏","570","间",R.color.slateblue));
        list5.add(new QBOGridModel("房屋破坏","380","间",R.color.slateblue));

        list6.add(new QBOGridModel("财产损失","500","万元",R.color.moccasin));
        list6.add(new QBOGridModel("财产损失","540","万元",R.color.moccasin));
        list6.add(new QBOGridModel("财产损失","580","万元",R.color.moccasin));
        list6.add(new QBOGridModel("财产损失","600","万元",R.color.moccasin));
        list6.add(new QBOGridModel("财产损失","490","万元",R.color.moccasin));
        list6.add(new QBOGridModel("财产损失","700","万元",R.color.moccasin));

        items.add(list1.get((int) (Math.random() * list1.size())));
        items.add(list2.get((int) (Math.random() * list2.size())));
        items.add(list3.get((int) (Math.random() * list3.size())));
        items.add(list4.get((int) (Math.random() * list4.size())));
        items.add(list5.get((int) (Math.random() * list5.size())));
        items.add(list6.get((int) (Math.random() * list6.size())));

        return items;
    }

    public static List<BarCharEntry> getRandomBarDataSource() {
        List<BarCharEntry> entryList = new ArrayList<BarCharEntry>();
        List<BarCharEntry> list1 = new ArrayList<BarCharEntry>();
        List<BarCharEntry> list2 = new ArrayList<BarCharEntry>();
        List<BarCharEntry> list3 = new ArrayList<BarCharEntry>();

        list1.add(new BarCharEntry("150","土木结构"));
        list1.add(new BarCharEntry("120","土木结构"));
        list1.add(new BarCharEntry("130","土木结构"));
        list1.add(new BarCharEntry("160","土木结构"));
        list1.add(new BarCharEntry("100","土木结构"));
        list1.add(new BarCharEntry("250","土木结构"));
 
        list2.add(new BarCharEntry("255","砖混结构"));
        list2.add(new BarCharEntry("205","砖混结构"));
        list2.add(new BarCharEntry("225","砖混结构"));
        list2.add(new BarCharEntry("155","砖混结构"));
        list2.add(new BarCharEntry("275","砖混结构"));
        list2.add(new BarCharEntry("325","砖混结构"));

        list3.add(new BarCharEntry("200","框架结构"));
        list3.add(new BarCharEntry("220","框架结构"));
        list3.add(new BarCharEntry("240","框架结构"));
        list3.add(new BarCharEntry("280","框架结构"));
        list3.add(new BarCharEntry("300","框架结构"));
        list3.add(new BarCharEntry("250","框架结构"));

        entryList.add(list1.get((int) (Math.random() * list1.size())));
        entryList.add(list2.get((int) (Math.random() * list2.size())));
        entryList.add(list3.get((int) (Math.random() * list3.size())));

        return entryList;
    }
}
