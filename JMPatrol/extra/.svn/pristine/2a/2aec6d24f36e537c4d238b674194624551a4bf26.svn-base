package com.ecity.mobile.android.bdlbslibrary.utils;

import com.ecity.mobile.android.bdlbslibrary.model.MyPoint;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @version V1.0
 * @Title BaiduUtils.java
 * @Package com.ecity.android.plugin.bdlocation.util
 * @Description
 * @Author Wangliu
 * @CreateDate 2015��7��15��
 * @email
 * @copyright ECity 2015
 */

public class BaiDuUtils {
    public BaiDuUtils() {
    }

    private static double[] MCBAND = {12890594.86, 8362377.87, 5591021, 3481989.83, 1678043.12, 0};
    private static double[] LLBAND = {75, 60, 45, 30, 15, 0};

    private static double[][] MC2LL = {{1.410526172116255e-8, 0.00000898305509648872, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 17337981.2},
                                     {-7.435856389565537e-9, 0.000008983055097726239, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 10260144.86},
                                     {-3.030883460898826e-8, 0.00000898305509983578, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37},
                                     {-1.981981304930552e-8, 0.000008983055099779535, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06},
                                     {3.09191371068437e-9, 0.000008983055096812155, 0.00006995724062, 23.10934304144901, -0.00023663490511, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4},
                                     {2.890871144776878e-9, 0.000008983055095805407, -3.068298e-8, 7.47137025468032, -0.00000353937994, -0.02145144861037, -0.00001234426596, 0.00010322952773, -0.00000323890364, 826088.5}};

    private static double[][] LL2MC = {{-0.0015702102444, 111320.7020616939, 1704480524535203.0, -10338987376042340.0, 26112667856603880.0, -35149669176653700.0, 26595700718403920.0, -10725012454188240.0, 1800819912950474.0, 82.5},
                                       {0.0008277824516172526, 111320.7020463578, 647795574.6671607, -4082003173.641316, 10774905663.51142, -15171875531.51559, 12053065338.62167, -5124939663.577472, 913311935.9512032, 67.5},
                                       {0.00337398766765, 111320.7020202162, 4481351.045890365, -23393751.19931662, 79682215.47186455, -115964993.2797253, 97236711.15602145, -43661946.33752821, 8477230.501135234, 52.5},
                                       {0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5},
                                       {-0.0003441963504368392, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5},
                                       {-0.0003218135878613132, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45}};

    /**
     * �ٶ�webī����ת��γ��
     *
     * @param cD
     * @return
     */
    public static com.ecity.mobile.android.bdlbslibrary.model.MyPoint convertMC2LL(MyPoint cD) {
        double[] cF = null;
        for (int cE = 0; cE < MCBAND.length; cE++) {
            if (cD.y >= MCBAND[cE]) {
                cF = MC2LL[cE];
                break;
            }
        }
        MyPoint T = convertor(cD, cF);
        return T;
    }

    /**
     * �ٶȾ�γ��תwebī����
     *
     * @param T
     * @return
     */
    public static MyPoint convertLL2MC(MyPoint T) {
        double[] cE = null;
        T.x = getLoop(T.x, -180, 180);
        T.y = getRange(T.y, -74, 74);

        for (int cD = 0; cD < LLBAND.length; cD++) {
            if (T.y >= LLBAND[cD]) {
                cE = LL2MC[cD];
                break;
            }
        }
        if (cE == null) {
            for (int cD = LLBAND.length - 1; cD >= 0; cD--) {
                if (T.y <= -LLBAND[cD]) {
                    cE = LL2MC[cD];
                    break;
                }
            }
        }
        MyPoint cF = convertor(T, cE);
        return cF;
    }

    /**
     * ��γ��תΪ�ٶȵľ�γ��
     *
     * @param p
     * @return
     * @throws Exception
     */
    public static MyPoint getBaiduGpsPointByGps(MyPoint p) throws Exception {
        String res = onlineTrans2Baidu(p.x, p.y);
        JSONObject jsonObj = new JSONObject(res);
        if (jsonObj.has("error") && jsonObj.getInt("error") > 0) {
            throw new Exception("������������ת������");
            //return null;
        }
        String sx = jsonObj.getString("x");
        String sy = jsonObj.getString("y");
        double gpsX = Double.valueOf(StringDecodeUtil.getFromBase64(sx));
        double gpsY = Double.valueOf(StringDecodeUtil.getFromBase64(sy));

        MyPoint pRet = new MyPoint();
        pRet.x = gpsX;
        pRet.y = gpsY;
        return pRet;
    }

    /**
     * �ٶȵľ�γ�Ⱦ�ƫ��ȡ��ʵλ��
     *
     * @param p
     * @return
     * @throws Exception
     */
    public static MyPoint getGpsPointByBaiduGps(MyPoint p) throws Exception {
        String res = onlineTrans2Baidu(p.x, p.y);
        JSONObject jsonObj = new JSONObject(res);
        if (jsonObj.has("error") && jsonObj.getInt("error") > 0) {
            throw new Exception("坐标转换失败");
        }
        String sx = jsonObj.getString("x");
        String sy = jsonObj.getString("y");
        double dx = Double.valueOf(StringDecodeUtil.getFromBase64(sx));
        double dy = Double.valueOf(StringDecodeUtil.getFromBase64(sy));

        double gpsX = 2 * p.x - dx;
        double gpsY = 2 * p.y - dy;

        MyPoint pRet = new MyPoint();
        pRet.x = gpsX;
        pRet.y = gpsY;

        return pRet;
    }


    private static String onlineTrans2Baidu(double x, double y) throws Exception {
        String baseUrl = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4";
        baseUrl += "&x=" + x;
        baseUrl += "&y=" + y;

        String result = "";
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String result1;
            while ((result1 = reader.readLine()) != null) {
                result += result1;
            }
            reader.close();
            con.disconnect();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return result;
    }


    private static MyPoint convertor(MyPoint cD, double[] cE) {
        if (cD == null || cE == null) {
            return null;
        }
        MyPoint ret = new MyPoint();
        double T = cE[0] + cE[1] * Math.abs(cD.x);
        double cC = Math.abs(cD.y) / cE[9];
        double cF = cE[2] + cE[3] * cC + cE[4] * cC * cC + cE[5] * cC * cC * cC + cE[6] * cC * cC * cC * cC + cE[7] * cC * cC * cC * cC * cC + cE[8] * cC * cC * cC * cC * cC * cC;
        T *= (cD.x < 0 ? -1 : 1);
        cF *= (cD.y < 0 ? -1 : 1);

        ret.x = T;
        ret.y = cF;
        return ret;
    }


    private static double getRange(double cD, double cC, double T) {
        cD = Math.max(cD, cC);
        cD = Math.min(cD, T);
        return cD;
    }

    private static double getLoop(double cD, double cC, double T) {
        while (cD > T) {
            cD -= T - cC;
        }
        while (cD < cC) {
            cD += T - cC;
        }
        return cD;
    }

    /**
     * ��γ��תΪ�ٶȵľ�γ��
     *
     * @param p
     * @return
     * @throws Exception
     */
    public static LocGeoPoint getBaiduGpsPointByGps(LocGeoPoint p) throws Exception {
        String res = onlineTrans2Baidu(p.x, p.y);
        JSONObject jsonObj = new JSONObject(res);
        if (jsonObj.has("error") && jsonObj.getInt("error") > 0) {
            throw new Exception("转换GPS坐标失败");
            //return null;
        }
        String sx = jsonObj.getString("x");
        String sy = jsonObj.getString("y");
        double gpsX = Double.valueOf(StringDecodeUtil.getFromBase64(sx));
        double gpsY = Double.valueOf(StringDecodeUtil.getFromBase64(sy));

        LocGeoPoint pRet = new LocGeoPoint();
        pRet.x = gpsX;
        pRet.y = gpsY;
        return pRet;
    }
}