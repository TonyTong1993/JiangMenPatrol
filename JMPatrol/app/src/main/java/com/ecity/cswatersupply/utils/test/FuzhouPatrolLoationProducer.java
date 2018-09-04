package com.ecity.cswatersupply.utils.test;

import android.location.Location;
import android.location.LocationManager;

import com.zzz.ecity.android.applibrary.service.ALoationProducer;

public class FuzhouPatrolLoationProducer extends ALoationProducer {

    private double[][] mockPoints;

    public FuzhouPatrolLoationProducer() {
        this.mockPoints = ml_fuzhou_points;
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

    double[][] ml_fuzhou_points = new double[][] {
            {119.3167135369879, 26.05103492660272},
            {119.3170137331157, 26.050165933133123},
            {119.31720887653567, 26.049448965348642},
            {119.31701730051803, 26.04951158681929},
            {119.31705092693933, 26.04932080337537},
            {119.31714280308101, 26.0488989612142},
            {119.31722265404836, 26.048447686320664},
            {119.31725748754455, 26.047347935010997},
            {119.31722643682924, 26.04709442208461},
            {119.31739743573411, 26.04634614812767},
            {119.31741118308821, 26.046078159082477},
            {119.31761608635047, 26.04495311043244},
            {119.3178367335339, 26.04453970180072},
            {119.31872059027083, 26.04493750903145},
            {119.31924929062826, 26.044800911890807},
            {119.31960620681187, 26.044574088076665},
            {119.31987578912666, 26.044234254867817},
            {119.32017590084091, 26.043685928888774},
            {119.32010340419818, 26.043543185247916},
            {119.32035471676647, 26.043174644138283},
            {119.32070320600458, 26.04313654369379}
    };
}
