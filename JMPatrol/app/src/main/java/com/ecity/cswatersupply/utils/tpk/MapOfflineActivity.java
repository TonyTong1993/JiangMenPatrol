package com.ecity.cswatersupply.utils.tpk;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.tpk.OfflineMapManager.IOfflineMapResourceCallback;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.z3app.android.util.PreferencesUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class MapOfflineActivity extends BaseActivity implements IOfflineMapResourceCallback {
    private static final String KEY_LAYER_NAME = "KEY_LAYER_NAME";
    private static final String KEY_LOCAL_PATH = "KEY_LOCAL_PATH";
    private CustomTitleView title;
    private MapOfflineListAdapter mAdapter;
    public List<MapOfflineBean> listData = new ArrayList<MapOfflineBean>();
    public ListView listview;
    private Button btnStartDownload;
    private boolean isBackButtonAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_offline);
        initListView();
    }

    @SuppressLint("NewApi")
    private void initListView() {
        listData = OfflineMapManager.getInstance(this).getListDataSource();
        title = (CustomTitleView) findViewById(R.id.customTitleView);
        title.setTitleText(this.getResources().getString(R.string.map_offline_title));
        title.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        title.tv_title.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.selector_titleview_titletxt_shape));
        listview = (ListView) findViewById(R.id.lv_container);
        mAdapter = new MapOfflineListAdapter(this);
        mAdapter.setList(listData);
        listview.setAdapter(mAdapter);
        btnStartDownload = (Button) findViewById(R.id.btn_download_map_offline);

        btnStartDownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isBackButtonAllowed = false;
                btnStartDownload.setEnabled(false);
                OfflineMapManager.getInstance(MapOfflineActivity.this).setCallback(MapOfflineActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OfflineMapManager.getInstance(MapOfflineActivity.this).download();
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
        RefWatcher refWatcher = HostApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    public void onBackButtonClicked(View v) {
        backActionForbid();
    }

    @Override
    public void notifyNoResourceToDownload() {
        ToastUtil.showLong(HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.map_offline_download_resource_not_found));
        return;
    }

    @Override
    public void notifyNotEnoughDiskSpace() {
        ToastUtil.showLong(HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.map_offline_download_space_limited));
    }

    @Override
    public void notifyProgressUpdate(String layerName, String tpkUrl, float progress) {
        List<MapOfflineBean> listDatas = mAdapter.getList();
        for (MapOfflineBean bean : listDatas) {
            if (layerName.equals(bean.getMapTpkName())) {
                bean.setProgress(progress);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backActionForbid();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backActionForbid() {
        if (isBackButtonAllowed) {
            finish();
        } else {
            String msg = ResourceUtil.getStringById(R.string.map_offline_download_loading);
            ToastUtil.showShort(msg);
        }
    }

    @Override
    public void onComplete(String localPath) {
        String info = HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.map_offline_download_success);
        completeDownload("", localPath, info);
    }

    @Override
    public void onError(String layerName, String tpkUrl, String errorMsg) {
        btnStartDownload.setEnabled(true);
        isBackButtonAllowed = true;
        ToastUtil.showLong(errorMsg);
        finish();
    }

    @Override
    public void notifyNotValidNetWork() {
        btnStartDownload.setEnabled(true);
        ToastUtil.showLong(HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.map_offline_download_network));

    }

    @Override
    public void onSigleComplete(String layerName, String localPath) {
        String info = HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.map_offline_download_single_success);
        completeDownload(layerName, localPath, info);
    }

    private void completeDownload(String layerName, String localPath, String info) {
        String nameCache = PreferencesUtil.getString(this, KEY_LAYER_NAME, "");
        String pathCache = PreferencesUtil.getString(this, KEY_LOCAL_PATH, "");
        if (StringUtil.isEmpty(nameCache)) {
            nameCache = layerName;
            pathCache = localPath;
        } else {
            nameCache += ";" + layerName;
            pathCache += ";" + pathCache;
        }
        PreferencesUtil.putString(this, KEY_LAYER_NAME, nameCache);
        PreferencesUtil.putString(this, KEY_LOCAL_PATH, pathCache);
        ToastUtil.showLong(layerName + info);
    }
}
