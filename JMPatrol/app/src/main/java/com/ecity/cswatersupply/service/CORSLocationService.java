package com.ecity.cswatersupply.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.utils.ArriveDetecter;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.mobile.android.library.gpsengine.ILocationChangedListener;
import com.z3pipe.mobile.android.corssdk.CORSSDK;
import com.z3pipe.mobile.android.corssdk.ICORSSDKCallback;
import com.z3pipe.mobile.android.corssdk.model.PTNLInfo;
import com.z3pipe.mobile.android.corssdk.model.PositionErrorStatistic;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.service.PositionService;
import com.zzz.ecity.android.applibrary.task.PositionReportTask;

import java.util.concurrent.LinkedBlockingQueue;

public class CORSLocationService extends Service {
    private int NOTIFICATION_ID_APP_IS_RUNNING = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = super.onStartCommand(intent, flags, startId);
        startGPS();
        return id;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        register();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.logo_main);
        builder.setTicker(getText(R.string.app_name_cswatersupply));
        builder.setContentTitle(getText(R.string.app_name_cswatersupply));
        builder.setContentText(getText(R.string.app_is_running_cors));
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID_APP_IS_RUNNING, notification);
        LogUtil.i(this, "CORSLocationService is started.");
    }

    @Override
    public void onDestroy() {
        stopGPS();
        unRegister();
        stopForeground(true);
        super.onDestroy();
    }

    /***
     * 开启GPS
     */
    private void startGPS() {
        //CORSSDK.getInstance().startSimulateLocation();
    }

    /***
     * 关闭GPS
     */
    private void stopGPS() {
        CORSSDK.getInstance().stop();
    }

    /**
     * 开启服务
     *
     * @param context
     */
    public static void startInstance(Context context) {
        Intent intent = new Intent(context, CORSLocationService.class);
        context.startService(intent);
        PositionService.setPauseReport(true);
        CORSSDK.getInstance().setup(context);
    }

    /**
     * 关闭服务
     *
     * @param context
     */
    public static void stopInstance(Context context) {
        Intent intent = new Intent(context, CORSLocationService.class);
        context.stopService(intent);
        PositionService.setPauseReport(false);
    }

    /***
     * 注册监听
     */
    private void register() {
        CORSSDK.getInstance().registerCallBack(callBack);
    }

    /***
     * 取消注册
     */
    private void unRegister() {
        CORSSDK.getInstance().unRegisterCallBack(callBack);
    }

    private ILocationChangedListener listener = new ILocationChangedListener() {

        @Override
        public void onLocationChanged(Location location) {
            SessionManager.currentLocation = location;
            try {
                GPSPositionBean gpsPositionBean = new GPSPositionBean();
                gpsPositionBean.setlat(location.getLatitude());
                gpsPositionBean.setlon(location.getLongitude());
                double[] locationPosition = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude(), CoordTransfer.ETRANSTYPE.Parameters);
                gpsPositionBean.setx(locationPosition[0]);
                gpsPositionBean.sety(locationPosition[1]);
                if (null == ArriveDetecter.mLinkQueueTrackPositions) {
                    ArriveDetecter.mLinkQueueTrackPositions = new LinkedBlockingQueue<GPSPositionBean>();
                }
                ArriveDetecter.mLinkQueueTrackPositions.add(gpsPositionBean);
            } catch (Exception e) {
                e.printStackTrace();
            }

            PositionReportTask.getInstance().addFilterLocation(location);
        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        }
    };

    private ICORSSDKCallback callBack = new ICORSSDKCallback() {

        @Override
        public void onConnected(String arg0, String arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onConnectedDevice(String arg0) {
            // TODO Auto-generated method stub
            PositionService.setPauseReport(true);
        }

        @Override
        public void onConnecting() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMessageCORSLoginFail(String arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onMessageCORSLoginSuccess() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMessageSwitchStateChanged(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMessageToast(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNotConnected() {
            PositionService.setPauseReport(false);
            PositionService.startInstance(HostApplication.getApplication().getApplicationContext());
        }

        @Override
        public void onReceiveDateLength(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReceiveLocation(SatelliteInfo arg0, Location location) {
            if (null == arg0 || location == null) {
                return;
            }

//            if(posIndex>=locs.length) {
//                posIndex = 0;
//            }
//
//            Location location = new Location(LocationManager.GPS_PROVIDER);
//            location.setLongitude(locs[posIndex][0]);
//            location.setLatitude(locs[posIndex][1]);
//            location.setSpeed(posIndex % 10 * 0.f);
//            location.setAccuracy(6);
//            location.setTime(System.currentTimeMillis());
//            posIndex++;

            SessionManager.satelliteInfo = arg0;
            switch (arg0.getQuality()) {
                case NOTPOSITIONED:

                    break;

                case SINGLEPOINT:
                case PSEUDORANGE:
                case FLOATINGPOINT:
                case FIXED:
                    listener.onLocationChanged(location);
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onReceiveNMEAData(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReceivePTNLInfo(PTNLInfo arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReceivePositionErrorStatisticInfo(
                PositionErrorStatistic arg0) {
            // TODO Auto-generated method stub

        }

    };

    private static int posIndex = 0;
    private static double[][] locs = new double[][]{
            {119.27900826396737, 26.042119208902182},
            {119.27903750856413, 26.042087355169098},
            {119.27908215086195, 26.042062367139216},
            {119.2790182225269, 26.04210429214288},
            {119.27897448016002, 26.042136432903362},
            {119.27891771612207, 26.042171032419365},
            {119.27886718296541, 26.042200718091703},
            {119.27882452582274, 26.04224453067104},
            {119.2787516260108, 26.042281156219424},
            {119.27871782840688, 26.042325019566366},
            {119.27867246964077, 26.042346798007973},
            {119.27859239560368, 26.04240800601326},
            {119.27851187565739, 26.04246301136352},
            {119.2784621877703, 26.04249590450237},
            {119.27839646623279, 26.042547698775305},
            {119.27835396722803, 26.04258138318856},
            {119.27825146870616, 26.042649326469153},
            {119.27815919437877, 26.04272156474597},
            {119.27805058365861, 26.042791348382583},
            {119.27796200129404, 26.0428517216296},
            {119.27782727283818, 26.042951621334502},
            {119.27772544213278, 26.043034680103744},
            {119.27763507171386, 26.04310391256634},
            {119.27758324434424, 26.043140446178466},
            {119.27755666392551, 26.04317230035046},
            {119.27751866443873, 26.04319561725003},
            {119.27749151832671, 26.04321321813033},
            {119.27752238479368, 26.04323966521811},
            {119.27755950972508, 26.043290519418473},
            {119.27763952114412, 26.043340334442224},
            {119.27769610836629, 26.04341572606813},
            {119.27776467723812, 26.043518346585685},
            {119.27781973841599, 26.04357524588279},
            {119.27785289711161, 26.04363701664997},
            {119.27788340228514, 26.043660974620703},
            {119.27791439190334, 26.04372687819027},
            {119.27797219911747, 26.043784713180592},
            {119.27801415167063, 26.04384217882245},
            {119.27801051617423, 26.043865451970746},
            {119.27805135375667, 26.04389173317166},
            {119.27810252765234, 26.043962845141433},
            {119.2781359275983, 26.04401421496698},
            {119.27818575362025, 26.04405761400565},
            {119.27822409674613, 26.044114720386414},
            {119.27826786577509, 26.04416842021475},
            {119.27831192439005, 26.044206795465406},
            {119.2783544530498, 26.044263291122633},
            {119.27838524947957, 26.044334139189488},
            {119.27841887441986, 26.044385795552696},
            {119.27845855043083, 26.044434691607336},
            {119.27849921459666, 26.044506974572435},
            {119.27854981489247, 26.044565833591776},
            {119.27858205709614, 26.044617605794404},
            {119.27864541159545, 26.044713910880677},
            {119.27872522751855, 26.0448038041044},
            {119.27877047972184, 26.044858417000412},
            {119.2788261367164, 26.04493474029306},
            {119.27889054498188, 26.04503990223725},
            {119.27892480558918, 26.045075051006915},
            {119.27899363365361, 26.045172923861838},
            {119.27906818101455, 26.04528678607654},
            {119.27912646726793, 26.04534572690368},
            {119.27916250554706, 26.0454232411838},
            {119.27920116679539, 26.04546942499758},
            {119.27926950251174, 26.0455721439727},
            {119.27931635501072, 26.04563237719819},
            {119.27930304327924, 26.045622429843846},
            {119.27938741135267, 26.045737499434807},
            {119.27943756007264, 26.04578748794608},
            {119.27951099930942, 26.045914126559456},
            {119.2797035754696, 26.046167386804665},
            {119.27978349841784, 26.04631078414573},
            {119.27991128410471, 26.04652471522474},
            {119.28000021205177, 26.046617500361364},
            {119.28008590822898, 26.046754286319064},
            {119.28029857874533, 26.047066302164666},
            {119.28035625631864, 26.047140605769666},
            {119.2804288791631, 26.04729789745159},
            {119.2803644974797, 26.04746315365466},
            {119.28029495441838, 26.04762701978589},
            {119.28032691357323, 26.047698820202875},
            {119.28037957615173, 26.0477404066618},
            {119.28042532514921, 26.0477987689561},
            {119.28050377154617, 26.047804544400815},
            {119.28055150111764, 26.047888661266906},
            {119.2805885756159, 26.047938687007978},
            {119.28062353649905, 26.0480938166003},
            {119.28066709521998, 26.048242261856437},
            {119.28076117635318, 26.048323224186873},
            {119.28081959624194, 26.048408374593638},
            {119.28099764262089, 26.04863167096314},
            {119.28122726499453, 26.048955399228177},
            {119.28126773354077, 26.04901614594437},
            {119.28131182114036, 26.049095955613716},
            {119.28136055310873, 26.04916326348355},
            {119.28143558519506, 26.049238406154767},
            {119.28151770611557, 26.049366542265773},
            {119.28163713681705, 26.0495150321756},
            {119.28167033529652, 26.049586281047198},
            {119.28175427396584, 26.049682660448582},
            {119.28182458279059, 26.04980046095313},
            {119.28193744103584, 26.049950528358995},
            {119.28203562226986, 26.050071058911502},
            {119.28217613776408, 26.05028275432511},
            {119.28245844722747, 26.050666846159746},
            {119.28253446114927, 26.05077899902914},
            {119.28259523032199, 26.050877810612587},
            {119.28270515401196, 26.05100362385114},
            {119.28274302071699, 26.05108017625931},
            {119.28292790065393, 26.05133515346091},
            {119.28302526345816, 26.051475769275374},
            {119.28309457234383, 26.051548510929614},
            {119.28273653056122, 26.05068713932132},
            {119.28291219661482, 26.051288261811415},
            {119.28305515934099, 26.051493233727115},
            {119.28319518090245, 26.051732490662204},
            {119.28328400649633, 26.051870696410973},
            {119.28355106003002, 26.05216119282126},
            {119.28372331217749, 26.052384402075877},
            {119.28379587386216, 26.052532142385648},
            {119.2839292362483, 26.05277086566379},
            {119.28451514796669, 26.05356310703561},
            {119.28461985467393, 26.05368980817929},
            {119.2848545341024, 26.053991383007045},
            {119.28492134565441, 26.054088443365316},
            {119.28498991984455, 26.05426112793689},
            {119.28507010008146, 26.054397320991256},
            {119.2851670860413, 26.054574830169802},
            {119.28521024559126, 26.054685359641155},
            {119.28525392213862, 26.054767157160537},
            {119.28528464906059, 26.054865735395964},
            {119.28532208129671, 26.055004429872255},
            {119.2853638176334, 26.05509776164679},
            {119.28537719141292, 26.055159692526576},
            {119.28539387017284, 26.055206495909086},
            {119.28539675291012, 26.055252324396275},
            {119.28538512422328, 26.05528242584065},
            {119.2854157122149, 26.05533194969436},
            {119.28542175525018, 26.055420028410158},
            {119.28543085471102, 26.055465082998733},
            {119.28544359786937, 26.0555782209639},
            {119.28545309365818, 26.055671849435978},
            {119.28546837856011, 26.05580829155125},
            {119.28545789302153, 26.055916439163372},
            {119.28546952763769, 26.056088611677858},
            {119.28547129789716, 26.05616392624335},
            {119.28545382383066, 26.056246358032894},
            {119.28545972839323, 26.056336068320643}
    };
}