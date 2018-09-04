package com.ecity.cswatersupply.utils.test;

import android.location.Location;
import android.location.LocationManager;

import com.zzz.ecity.android.applibrary.service.ALoationProducer;

public class XiaoGanPatrolLoationProducer extends ALoationProducer {
    private double[][] mockPoints;

    public XiaoGanPatrolLoationProducer() {
        this.mockPoints = xiaogan_points;
    }

    /**
     * 生成总数
     * 即:一共会调用 num 次generate方法
     *
     * @return
     */
    @Override
    public int getNum() {
        return mockPoints.length;
    }

    /**
     * 生成间隔 秒
     * 即:每interval秒会调用一次generate方法
     *
     * @return
     */
    @Override
    public int getInterval() {
        return 2;
    }

    @Override
    public Location generate(int index) {
        if (index >= mockPoints.length) {
            return null;
        }
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLongitude(mockPoints[index][0]);
        location.setLatitude(mockPoints[index][1]);
        location.setSpeed(index % 10 * 0.f);
        location.setAccuracy(6);
        location.setTime(System.currentTimeMillis());
        return location;
    }

    double[][] xiaogan_points = new double[][]{
            {113.94236273036653, 30.918791835300258},
            {113.94247649338516, 30.918300749821064},
            {113.94261528958448, 30.9175951614676},
            {113.94294371323849, 30.916878383498986},
            {113.94293529131266, 30.916178388474023},
            {113.94309531525617, 30.915410719891188},
            {113.94313306237761, 30.915049447286176},
            {113.943206508715, 30.914462385344454},
            {113.94331876567574, 30.913909211210974},
            {113.94341077924197, 30.913316512072825},
            {113.94362272752451, 30.912862201775564},
            {113.9438344576321, 30.912224829053766},
            {113.9438368568081, 30.91164247460758},
            {113.94408600762569, 30.911032907727513},
            {113.94423041393408, 30.91040071446322},
            {113.94445344256891, 30.90966694232111},
            {113.94461100704062, 30.908916206165017}
    };

}
