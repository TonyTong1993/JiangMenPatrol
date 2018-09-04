package com.ecity.cswatersupply.emergency.test;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.emergency.model.EQMonitorStationModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;

public class DemoData {

    public static List<EarthQuakeInfoModel> getEarthQuakeInfo() {

        List<EarthQuakeInfoModel> eqInfoModels = new ArrayList<EarthQuakeInfoModel>();
        EarthQuakeInfoModel eqInfoModel = new EarthQuakeInfoModel();
        eqInfoModel.setId(1111);
        eqInfoModel.setLongitude(22781);
        eqInfoModel.setLatitude(43484);
        eqInfoModel.setML(2.0);
        eqInfoModel.setDepth("13公里");
        eqInfoModel.setRegion("四川省甘孜州康定市");
        eqInfoModel.setTime("2016-09-30 17:55:32");
        eqInfoModels.add(eqInfoModel);

        EarthQuakeInfoModel eqInfoModel2 = new EarthQuakeInfoModel();
        eqInfoModel2.setId(2222);
        eqInfoModel2.setLongitude(22662);
        eqInfoModel2.setLatitude(43455);
        eqInfoModel2.setML(4.0);
        eqInfoModel2.setDepth("10公里");
        eqInfoModel2.setRegion("斐济群岛地区");
        eqInfoModel2.setTime("2014-05-30 17:55:32");
        eqInfoModels.add(eqInfoModel2);

        EarthQuakeInfoModel eqInfoModel3 = new EarthQuakeInfoModel();
        eqInfoModel3.setId(3333);
        eqInfoModel3.setLongitude(22862);
        eqInfoModel3.setLatitude(43383);
        eqInfoModel3.setML(5.0);
        eqInfoModel3.setDepth("12公里");
        eqInfoModel3.setRegion("汤加群岛");
        eqInfoModel3.setTime("2012-09-30 17:55:32");
        eqInfoModels.add(eqInfoModel3);

        EarthQuakeInfoModel eqInfoModel4 = new EarthQuakeInfoModel();
        eqInfoModel4.setId(4444);
        eqInfoModel4.setLongitude(23262);
        eqInfoModel4.setLatitude(43583);
        eqInfoModel4.setML(7.0);
        eqInfoModel4.setDepth("18公里");
        eqInfoModel4.setRegion("菲律宾棉兰群岛");
        eqInfoModel4.setTime("2008-05-12 17:55:32");
        eqInfoModels.add(eqInfoModel4);

        EarthQuakeInfoModel eqInfoModel5 = new EarthQuakeInfoModel();
        eqInfoModel5.setId(5555);
        eqInfoModel5.setLongitude(23462);
        eqInfoModel5.setLatitude(43783);
        eqInfoModel5.setML(1.2);
        eqInfoModel5.setDepth("6公里");
        eqInfoModel5.setRegion("宁夏回族自治区中卫市海原县");
        eqInfoModel5.setTime("2010-10-16 17:55:32");
        eqInfoModels.add(eqInfoModel5);

        return eqInfoModels;
    }

    public static List<EarthQuakeInfoModel> getSearchEarthQuakeInfo() {

        List<EarthQuakeInfoModel> eqInfoModels = new ArrayList<EarthQuakeInfoModel>();
        EarthQuakeInfoModel eqInfoModel = new EarthQuakeInfoModel();
        eqInfoModel.setId(1111);
        eqInfoModel.setLongitude(22781);
        eqInfoModel.setLatitude(43484);
        eqInfoModel.setML(2.0);
        eqInfoModel.setDepth("13公里");
        eqInfoModel.setRegion("四川省甘孜州康定市");
        eqInfoModel.setTime("2016-09-30 17:55:32");
        eqInfoModels.add(eqInfoModel);

        EarthQuakeInfoModel eqInfoModel2 = new EarthQuakeInfoModel();
        eqInfoModel2.setId(2222);
        eqInfoModel2.setLongitude(22662);
        eqInfoModel2.setLatitude(43455);
        eqInfoModel2.setML(4.0);
        eqInfoModel2.setDepth("10公里");
        eqInfoModel2.setRegion("斐济群岛地区");
        eqInfoModel2.setTime("2014-05-30 17:55:32");
        eqInfoModels.add(eqInfoModel2);

        EarthQuakeInfoModel eqInfoModel3 = new EarthQuakeInfoModel();
        eqInfoModel3.setId(3333);
        eqInfoModel3.setLongitude(22862);
        eqInfoModel3.setLatitude(43383);
        eqInfoModel3.setML(5.0);
        eqInfoModel3.setDepth("12公里");
        eqInfoModel3.setRegion("汤加群岛");
        eqInfoModel3.setTime("2012-09-30 17:55:32");
        eqInfoModels.add(eqInfoModel3);

        return eqInfoModels;
    }

    public static List<EQMonitorStationModel> getEQStations() {
        List<EQMonitorStationModel> eqStationModels = new ArrayList<EQMonitorStationModel>();
        EQMonitorStationModel eqStationModel = new EQMonitorStationModel();
        eqStationModel.setId("1");
        eqStationModel.setStationName("常州金坛地震台");
        eqStationModel.setLocation("金坛市顾龙山107号");
        eqStationModel.setStationType("前兆观测,测震,强震");
        eqStationModel.setManager("王军");
        eqStationModel.setTime("2012-09-30 17:55:32");
        eqStationModel.setLongitude(23062);
        eqStationModel.setLatitude(43583);
        eqStationModels.add(eqStationModel);

        EQMonitorStationModel eqStationModel2 = new EQMonitorStationModel();
        eqStationModel2.setId("2");
        eqStationModel2.setStationName("常州清明山地震台");
        eqStationModel2.setLocation("武进区横山桥镇");
        eqStationModel2.setStationType("前兆观测,测震");
        eqStationModel2.setManager("王军");
        eqStationModel2.setTime("2014-05-30 17:55:32");
        eqStationModel2.setLongitude(23265);
        eqStationModel2.setLatitude(43456);
        eqStationModels.add(eqStationModel2);

        EQMonitorStationModel eqStationModel3 = new EQMonitorStationModel();
        eqStationModel3.setId("3");
        eqStationModel3.setStationName("常州孟城地震台");
        eqStationModel3.setLocation("新北区孟河镇小黄山");
        eqStationModel3.setStationType("前兆观测,测震");
        eqStationModel3.setManager("王军");
        eqStationModel3.setTime("2010-10-16 17:55:32");
        eqStationModel3.setLongitude(23486);
        eqStationModel3.setLatitude(43292);
        eqStationModels.add(eqStationModel3);

        return eqStationModels;
    }
}
