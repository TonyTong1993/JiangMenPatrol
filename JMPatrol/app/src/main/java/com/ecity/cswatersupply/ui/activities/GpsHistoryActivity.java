package com.ecity.cswatersupply.ui.activities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.task.GPSPositionReporter;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

public class GpsHistoryActivity extends BaseActivity {

    private CustomTitleView viewTitle;
    private String filter;
    public static final String FILTER = "FILTER";
    public static final String ACTIVITY_FILTER_ALL = "ACTIVITY_FILTER_ALL";
    public static final String ACTIVITY_FILTER_NOT_UPLOAD = "ACTIVITY_FILTER_NOT_UPLOAD";
    private List<GPSPositionBean> mGpsPositions;
    private SimpleAdapter adapter;
    private List<Map<String, String>> data;
    private ListView mListView;
    private InitListDataHandler mInitListDataHandler;
    private UIHandler gpsReportHandler;
    private final int PKG_SIZE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_history);
        gpsReportHandler = new UIHandler(this);
        initUI();
    }

    private static class UIHandler extends Handler {
        WeakReference<GpsHistoryActivity> out;

        UIHandler(GpsHistoryActivity activity) {
            out = new WeakReference<GpsHistoryActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null != out.get()) {
                LoadingDialogUtil.dismiss();
            }
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onActionButtonClicked(View view) {
        if (mGpsPositions.size() == 0) {
            ToastUtil.showShort("当前没有未上报的记录");
            return;
        }

        LoadingDialogUtil.show(this, "正在上传请稍后");
        report();
    }

    private void initUI() {
        mInitListDataHandler = new InitListDataHandler(this);
        filter = getIntent().getStringExtra(GpsHistoryActivity.FILTER);
        mListView = (ListView) findViewById(R.id.gpshistorylist);
        viewTitle = (CustomTitleView) findViewById(R.id.view_title_gps_history);

        if (ACTIVITY_FILTER_ALL.equalsIgnoreCase(filter)) {
            viewTitle.setTitleText(R.string.gps_history_title_all);
            LoadDataFormDb();
        } else if (ACTIVITY_FILTER_NOT_UPLOAD.equalsIgnoreCase(filter)) {
            viewTitle.setTitleText(R.string.gps_history_title_not_uploaded);
            viewTitle.setBtnStyle(BtnStyle.RIGHT_ACTION);
            viewTitle.setRightActionBtnText("上报");
            LoadDataFormDb();
        } else {
            // no logic to do.
        }
    }

    private void report() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GPSPositionBean> unLoadPosition = new ArrayList<GPSPositionBean>();
                for (int i = 0; i < mGpsPositions.size(); i++) {
                    unLoadPosition.add(mGpsPositions.get(i));
                    if (mGpsPositions.size() < PKG_SIZE
                            || (mGpsPositions.size() - i) < PKG_SIZE) {
                        GPSPositionReporter.getInstance().reportPositions(
                                unLoadPosition);
                    } else if (mGpsPositions.size() > PKG_SIZE
                            && i % PKG_SIZE == 0) {
                        GPSPositionReporter.getInstance().reportPositions(
                                unLoadPosition);
                        unLoadPosition.clear();
                    }
                }

                gpsReportHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void initListData() {
        data = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        for (GPSPositionBean bean : mGpsPositions) {
            map = new HashMap<String, String>();
            getDescription(map, bean);
            data.add(map);
        }
        adapter = new SimpleAdapter(this, data, R.layout.simple_list_item_gps,
                new String[] { "GPSFullInfo" }, new int[] { R.id.tv_gps_des });
        mListView.setAdapter(adapter);
    }

    private void LoadDataFormDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ACTIVITY_FILTER_ALL.equalsIgnoreCase(filter)) {
                    try {
                        User currentUser = HostApplication.getApplication()
                                .getCurrentUser();
                        if (null != currentUser) {
                            mGpsPositions = GPSPositionDao.getInstance()
                                    .getPositionBeans(currentUser.getId(), 7,
                                            2, 1, 0, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (ACTIVITY_FILTER_NOT_UPLOAD.equalsIgnoreCase(filter)) {
                    try {
                        User currentUser = HostApplication.getApplication()
                                .getCurrentUser();
                        if (null != currentUser) {
                            mGpsPositions = GPSPositionDao.getInstance()
                                    .getPositionBeans(currentUser.getId(), 7,
                                            0, 1, 0, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // no logic to do.
                }
                if (!ListUtil.isEmpty(mGpsPositions)) {
                    Message msg = mInitListDataHandler.obtainMessage();
                    msg.what = 1;
                    mInitListDataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void getDescription(Map<String, String> map, GPSPositionBean bean) {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.gps_state_num)).append(bean.getId())
                .append("\n");
        builder.append(getString(R.string.gps_state_coord_x))
                .append(bean.getx()).append("\n");
        builder.append(getString(R.string.gps_state_coord_y))
                .append(bean.gety()).append("\n");
        builder.append(getString(R.string.gps_state_time))
                .append(bean.getTime()).append("\n");
        builder.append(getString(R.string.gps_state_latitude))
                .append(bean.getlat()).append("\n");
        builder.append(getString(R.string.gps_state_longitude))
                .append(bean.getlon()).append("\n");
        builder.append(getString(R.string.gps_state_accuracy))
                .append(bean.getacu()).append("\n");
        builder.append(getString(R.string.gps_state_status))
                .append(bean.getStatus()).append("\n");
        builder.append(getString(R.string.gps_state_battery)).append(
                bean.getbattery());

        map.put("GPSFullInfo", builder.toString());
    }

    private static class InitListDataHandler extends Handler {
        private WeakReference<GpsHistoryActivity> mGPSHistoryActivity;

        public InitListDataHandler(GpsHistoryActivity acitivity) {
            mGPSHistoryActivity = new WeakReference<GpsHistoryActivity>(
                    acitivity);
        }

        @Override
        public void handleMessage(Message msg) {
            GpsHistoryActivity mActivity = mGPSHistoryActivity.get();
            if (null == mActivity) {
                return;
            }
            switch (msg.what) {
            case 1:
                mActivity.initListData();
                break;

            default:
                break;
            }
        }
    }

}
