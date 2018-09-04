package com.ecity.cswatersupply.adapter.checkitem;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.network.FileDownloader;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.utils.AudioRecorder2Mp3;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.PreferencesUtil;
import com.z3app.android.util.StringUtil;

public class AudiosDetailListAdapter extends BaseAdapter {

    private List<AudioModel> mAudioModles = null;
    private LayoutInflater mInflater = null;
    private Activity mActivity;
    private static AudioRecorder2Mp3 mAudioRecorderUtil;
    private static PlayHandler mPlayHandler;

    public AudiosDetailListAdapter(Activity activity, List<AudioModel> audiosPath) {
        mInflater = LayoutInflater.from(activity);
        this.mActivity = activity;
        this.mAudioModles = audiosPath;
        mPlayHandler = new PlayHandler(this);
        mAudioRecorderUtil = new AudioRecorder2Mp3(mActivity, null, mPlayHandler, null);
    }

    public static PlayHandler getPlayHandler() {
        return mPlayHandler;
    }

    public static AudioRecorder2Mp3 getAudioRecorder2Mp3() {
        return mAudioRecorderUtil;
    }

    @Override
    public int getCount() {
        return mAudioModles.size();
    }

    @Override
    public AudioModel getItem(int position) {
        return mAudioModles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<AudioModel> getAudiosPath() {
        return mAudioModles;
    }

    public void setAudios(List<AudioModel> audioModels) {
        this.mAudioModles = audioModels;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        AudioModel audio = null;
        if (null != mAudioModles && position < mAudioModles.size()) {
            audio = mAudioModles.get(position);
        }
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_audio_detail_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null != audio) {
            int state = audio.getPlayState();
            switch (state) {
                case AudioModel.OVER:
                case AudioModel.PAUSE:
                    viewHolder.iv_play.setVisibility(View.VISIBLE);
                    viewHolder.iv_pause.setVisibility(View.GONE);
                    break;
                case AudioModel.PLAY:
                    viewHolder.iv_play.setVisibility(View.GONE);
                    viewHolder.iv_pause.setVisibility(View.VISIBLE);
                    break;
            }
        }

        String fileName = mAudioModles.get(position).getName();
        if (!StringUtil.isBlank(fileName) && fileName.contains("/")) {
            String[] pathArr = fileName.split("/");
            fileName = pathArr[pathArr.length - 1];
        }

        viewHolder.tv_audioName.setText(fileName);

        viewHolder.iv_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AudioModel audio = mAudioModles.get(position);
                String fileName = audio.getName();
                String fileUrl = fileName;
                if(StringUtil.isBlank(fileUrl)){
                    return;
                }
                // 服务返回的是图片名字，本地组装成完整路径
                if(!fileUrl.startsWith("http")){
                    fileUrl = ServiceUrlManager.getInstance().getImageUrl() + fileName;
                }

                String[] paths = audio.getName().split("/");
                boolean newPath = false;
                StringBuilder sb = new StringBuilder();
                for(String  str:paths ){
                    if(str.equalsIgnoreCase("output")){
                        newPath = true;
                        continue;
                    }

                    if(newPath){
                        sb.append(str);
                    }
                }
                // 服务返回的是录音文件名，本地组装成完整路径
                String targetPath = FileUtil.getInstance(null).getMediaPathforCache() + fileName;
                if(newPath){
                    targetPath = FileUtil.getInstance(null).getMediaPathforCache() + sb.toString();
                }

                audio.setFilePath(targetPath);
                //目标文件夹已存在且下载成功，则播放
                if (FileUtil.getInstance(null).hasFile(targetPath)) {
                    mAudioRecorderUtil.Play(audio, position);
                } else {
                    //目标文件夹已存在但没有下载成功，则删除
                    File sourceFile = new File(targetPath);
                    FileUtil.deleteFile(sourceFile);
                    FileDownloader.execute(fileUrl, targetPath, CustomReportActivity1.class);
                    LoadingDialogUtil.show(mActivity, R.string.event_audio_download);
                }
            }
        });

        viewHolder.iv_pause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AudioModel audio = mAudioModles.get(position);
                if (null != mAudioRecorderUtil) {
                    mAudioRecorderUtil.Play(audio, position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView tv_audioName;
        private ImageView iv_play;
        private ImageView iv_pause;

        public ViewHolder(View view) {
            super();
            this.iv_play = (ImageView) view.findViewById(R.id.iv_play);
            this.iv_pause = (ImageView) view.findViewById(R.id.iv_pause);
            this.tv_audioName = (TextView) view.findViewById(R.id.tv_audioname);
        }
    }

    private static class PlayHandler extends Handler {
        private WeakReference<AudiosDetailListAdapter> audiosListWeak;

        public PlayHandler(AudiosDetailListAdapter audiosDetailListAdapter) {
            this.audiosListWeak = new WeakReference<AudiosDetailListAdapter>(audiosDetailListAdapter);
        }

        @Override
        public void handleMessage(Message msg) {
            AudiosDetailListAdapter adapter = audiosListWeak.get();
            int position = (Integer) msg.obj;
            int state = msg.what;

            adapter.updatePlayState(position, state);
        }
    }

    public void updatePlayState(int position, int state) {
        if (!ListUtil.isEmpty(mAudioModles)) {
            mAudioModles.get(position).setPlayState(state);
        }
        notifyDataSetChanged();
    }

}
