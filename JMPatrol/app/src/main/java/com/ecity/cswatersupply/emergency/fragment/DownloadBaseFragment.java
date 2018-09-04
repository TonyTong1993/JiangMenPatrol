package com.ecity.cswatersupply.emergency.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.adapter.DownloadDefaultAdapter;
import com.ecity.cswatersupply.emergency.adapter.IDownloadItemClickListener;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.service.DownloadService;
import com.ecity.cswatersupply.emergency.utils.DownloadUtils;
import com.ecity.cswatersupply.emergency.utils.OpenFileUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.FileUtil;

public abstract class DownloadBaseFragment extends Fragment implements IDownloadItemClickListener<EmergencyPlanModel> {
    private List<EmergencyPlanModel> emergencyList;
    private DownloadReceiver mReceiver;
    private ListView listView;
    private DownloadDefaultAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(prepareLayoutResource(), null);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position, EmergencyPlanModel emergency,boolean redownload) {
        if(null == emergency){
            return;
        }
        
        if (emergency.getStatus() == EmergencyPlanModel.STATUS_DOWNLOADING || emergency.getStatus() == EmergencyPlanModel.STATUS_CONNECTING) {
            pause(emergency.getUrl());
        } else {
            String filePath = getEmergencyFilePath(emergency);
            if (FileUtil.getInstance(getActivity().getApplication()).hasFile(filePath)) {
                if(redownload){
                    FileUtil.deleteFile(new File(filePath));
                    download(position, emergency.getUrl(), emergency);
                } else {
                    Intent intent = OpenFileUtil.openFile(filePath);
                    if (null != intent) {
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                download(position, emergency.getUrl(), emergency);
            }
        }
    }

    protected int prepareLayoutResource() {
        return R.layout.fragment_downloadlistview;
    }

    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.ltv_download);
    }

    public void updateDataList(List<EmergencyPlanModel> list) {
        if (ListUtil.isEmpty(list)) {
            return;
        }

        if (null == emergencyList) {
            emergencyList = new ArrayList<EmergencyPlanModel>();
            emergencyList.addAll(list);
        } else {
            emergencyList.clear();
            emergencyList.addAll(list);
        }
        updateDataInfo();
        adapter = new DownloadDefaultAdapter(getActivity(), emergencyList);
        adapter.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    //更新界面
    protected void updateDataInfo() {
        if (null == emergencyList) {
            emergencyList = new ArrayList<EmergencyPlanModel>();
        }

        int size = emergencyList.size();
        for (int i = 0; i < size; i++) {
            EmergencyPlanModel model = emergencyList.get(i);
            model.setSavePath(getEmergencyFileDir(model));
            String fullPath = getEmergencyFilePath(model);
            File file = new File(fullPath);
            if (file.exists()) {
                model.setStatus(EmergencyPlanModel.STATUS_COMPLETE);
                model.setProgress(100);
            } else {
                DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(model.getUrl());
                if (downloadInfo != null) {
                    model.setProgress(downloadInfo.getProgress());
                    model.setDownloadPerSize(DownloadUtils.getDownloadPerSize(downloadInfo.getFinished(), downloadInfo.getLength()));
                    model.setStatus(EmergencyPlanModel.STATUS_PAUSED);
                }
            }
        }
    }

    //获得sd卡上的文件存储路径
    protected String getEmergencyFilePath(EmergencyPlanModel emergency) {
        if (null != emergency) {
            String path = getEmergencyFileDir(emergency) + "//" + emergency.getDoc();
            return path;
        }

        return "";
    }

    //获得sd卡上的文件存储路径
    protected String getEmergencyFileDir(EmergencyPlanModel model) {
        if (null != model) {
            String dir = FileUtil.getInstance(getActivity().getApplication()).getRootPath() + "//Emergency//" + model.getEntername();
            FileUtil.getInstance(getActivity().getApplication()).hasFileDir(dir);
            return dir;
        }

        return "";
    }

    //下载
    protected void download(int position, String tag, EmergencyPlanModel info) {
        DownloadService.intentDownload(getActivity(), tag, info);
    }

    //暂停
    protected void pause(String tag) {
        DownloadService.intentPause(getActivity(), tag);
    }

    //全部暂停
    protected void pauseAll() {
        DownloadService.intentPauseAll(getActivity());
    }

    //
    private boolean isCurrentListViewItemVisible(int position) {
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    private DownloadDefaultAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - listView.getFirstVisiblePosition();
        View view = listView.getChildAt(childPosition);
        return (DownloadDefaultAdapter.ViewHolder) view.getTag();
    }

    //注册广播
    private void register() {
        mReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
    }

    //反注册广播
    private void unRegister() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        }
    }

    //文件下载广播接收
    private class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action == null || !action.equals(DownloadService.ACTION_DOWNLOAD_BROAD_CAST)) {
                return;
            }
           
            final EmergencyPlanModel tmpInfo = (EmergencyPlanModel) intent.getSerializableExtra(DownloadService.EXTRA_DETAIL_INFO);
            if (tmpInfo == null) {
                return;
            }
            if (ListUtil.isEmpty(emergencyList)) {
                return;
            }
            
            int position = -1;
            int size = emergencyList.size();
            for(int i = 0;i<size;i++){
                EmergencyPlanModel emergency = emergencyList.get(i);
                if(tmpInfo.getDocUrl().equalsIgnoreCase(emergency.getDocUrl())){
                    position = i;
                    break;
                }
            }
            
            if(position == -1){
                return;
            }
            
            final EmergencyPlanModel emergency = emergencyList.get(position);
            final int status = tmpInfo.getStatus();
            switch (status) {
                case EmergencyPlanModel.STATUS_CONNECTING:
                    emergency.setStatus(EmergencyPlanModel.STATUS_CONNECTING);
                    if (isCurrentListViewItemVisible(position)) {
                        DownloadDefaultAdapter.ViewHolder holder = getViewHolder(position);
                        updateStatus(holder, emergency.getStatus());
                    }
                    break;

                case EmergencyPlanModel.STATUS_DOWNLOADING:
                    emergency.setStatus(EmergencyPlanModel.STATUS_DOWNLOADING);
                    emergency.setProgress(tmpInfo.getProgress());
                    emergency.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        DownloadDefaultAdapter.ViewHolder holder = getViewHolder(position);
                        holder.prg_downloadstate.setProgress(emergency.getProgress());
                        updateStatus(holder, emergency.getStatus());
                    }
                    break;
                case EmergencyPlanModel.STATUS_COMPLETE:
                    emergency.setStatus(EmergencyPlanModel.STATUS_COMPLETE);
                    emergency.setProgress(tmpInfo.getProgress());
                    emergency.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        DownloadDefaultAdapter.ViewHolder holder = getViewHolder(position);
                        updateStatus(holder, emergency.getStatus());
                        holder.prg_downloadstate.setProgress(emergency.getProgress());
                    }
                    break;

                case EmergencyPlanModel.STATUS_PAUSED:
                    emergency.setStatus(EmergencyPlanModel.STATUS_PAUSED);
                    if (isCurrentListViewItemVisible(position)) {
                        DownloadDefaultAdapter.ViewHolder holder = getViewHolder(position);
                        updateStatus(holder, emergency.getStatus());
                    }
                    break;
                case EmergencyPlanModel.STATUS_NOT_DOWNLOAD:
                    emergency.setStatus(EmergencyPlanModel.STATUS_NOT_DOWNLOAD);
                    emergency.setProgress(tmpInfo.getProgress());
                    emergency.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        DownloadDefaultAdapter.ViewHolder holder = getViewHolder(position);
                        updateStatus(holder, emergency.getStatus());
                        holder.prg_downloadstate.setProgress(emergency.getProgress());
                    }
                    break;
                case EmergencyPlanModel.STATUS_DOWNLOAD_ERROR:
                    emergency.setStatus(EmergencyPlanModel.STATUS_DOWNLOAD_ERROR);
                    emergency.setDownloadPerSize("");
                    if (isCurrentListViewItemVisible(position)) {
                        DownloadDefaultAdapter.ViewHolder holder = getViewHolder(position);
                        updateStatus(holder, emergency.getStatus());
                    }
                    break;
            }
        }

        private void updateStatus(DownloadDefaultAdapter.ViewHolder holder, int status) {
            if(null == holder){
                return;
            }
            
            holder.tv_state.setVisibility(View.VISIBLE);
            holder.tv_redownload.setVisibility(View.GONE);
            holder.prg_downloadstate.setVisibility(View.VISIBLE);
            switch (status) {
                case EmergencyPlanModel.STATUS_NOT_DOWNLOAD:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_download));
                    break;
                case EmergencyPlanModel.STATUS_CONNECTING:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_orange);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_waiting));
                    break;
                case EmergencyPlanModel.STATUS_CONNECT_ERROR:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_download));
                    break;
                case EmergencyPlanModel.STATUS_DOWNLOADING:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_downloading));
                    break;
                case EmergencyPlanModel.STATUS_PAUSED:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_orange);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_puased));
                    break;
                case EmergencyPlanModel.STATUS_DOWNLOAD_ERROR:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_download));
                    break;
                case EmergencyPlanModel.STATUS_COMPLETE:
                    //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
                    holder.tv_redownload.setVisibility(View.VISIBLE);
                    holder.prg_downloadstate.setVisibility(View.GONE);
                    holder.tv_state.setText(getResources().getString(R.string.str_emergency_downloaded));
                    break;
                default:
                    break;
            }
        }
    }
}
