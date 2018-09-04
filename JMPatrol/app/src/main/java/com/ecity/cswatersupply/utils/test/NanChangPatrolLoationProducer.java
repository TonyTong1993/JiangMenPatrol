package com.ecity.cswatersupply.utils.test;

import android.location.Location;
import android.location.LocationManager;

import com.zzz.ecity.android.applibrary.service.ALoationProducer;

public class NanChangPatrolLoationProducer extends ALoationProducer {
    private double[][] mockPoints;

    public NanChangPatrolLoationProducer() {
        this.mockPoints = nanchang_points;
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

    double[][] nanchang_points = new double[][]{
            {115.90883762793602,28.578301227259267},
            {115.90864386729527,28.580378472252445},
            {115.90832929126559,28.583029372327733},
            {115.90806049798627,28.585883025223783},
            {115.90715893250729,28.588579636030616},
            {115.90676735047721,28.591263709326242},
            {115.90638519268398,28.59303521284506},
            {115.90563572539669,28.596988232579744},
            {115.905552717099,28.59945507755885},
            {115.90518124439947,28.602037898218693},
            {115.90169528057342,28.60209421135577},
            {115.89899723630955,28.601886422602398}
    };
}
