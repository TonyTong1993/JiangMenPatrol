package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.AudiosDetailListAdapter;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.medialibrary.adapter.AudiosRecordAdapter;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.model.IAudioRecordCallback;
import com.ecity.medialibrary.utils.AudioRecordUtil;

public class AudioInspectItemViewXtd extends ABaseInspectItemView {
    private AudioRecordUtil audioRecordUtil;
    private AudiosRecordAdapter mAudioAdapter;
    private ArrayList<AudioModel> mAudiomodels = new ArrayList<AudioModel>();

    @Override
    protected void setup(View contentView) {
        ListViewForScrollView lvAudio = (ListViewForScrollView) contentView.findViewById(R.id.lv_audios);
        inflateAudio(lvAudio, mInspectItem);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_audio_select;
    }

    public void stopPlay() {
        if (mAudioAdapter != null) {
            mAudioAdapter.getAudioRecorderUtil().stopPlay();
        }
    }

    private void inflateAudio(ListViewForScrollView lvAudio, InspectItem item) {
        if (item.isEdit()) {
            audioRecordUtil = new AudioRecordUtil(context, new AudioRecordCallBack());
            mAudioAdapter = audioRecordUtil.getAudiosRecordAdapter(mAudiomodels);
            mAudioAdapter.setCallback(new AudioRecordCallBack());
            lvAudio.setAdapter(mAudioAdapter);
            lvAudio.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (arg2 == mAudiomodels.size()) {
                        audioRecordUtil.showRecordingAudioView(arg1);
                    }
                }
            });
            lvAudio.setAdapter(mAudioAdapter);

            /**武汉地震二次提交可编辑 需要优化*/
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(",");
                List<String> names = new ArrayList<String>(Arrays.asList(strs));
                for (String audio : names) {
                    AudioModel audioModel = new AudioModel();
                    audioModel.setName(audio);
                    audioModel.setFilePath(audio);
                    mAudioAdapter.finishRecordAudio(audioModel);
                }
            }

            /***/

        } else {
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(",");
                List<String> names = new ArrayList<String>(Arrays.asList(strs));
                // 服务返回的是录音文件名
                for (int i = 0; i < names.size(); i++) {
                    AudioModel audioModel = new AudioModel();
                    audioModel.setName(names.get(i));
                    mAudiomodels.add(audioModel);
                }
                AudiosDetailListAdapter adapter = new AudiosDetailListAdapter(context, mAudiomodels);
                lvAudio.setAdapter(adapter);
            }
        }
    }

    private class AudioRecordCallBack implements IAudioRecordCallback {

        @Override
        public void onRecordFinish(List<AudioModel> audios) {
            setAudiosValue();
            mAudioAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAudioDeleted(List<AudioModel> audios) {
            setAudiosValue();
        }
    }

    public ArrayList<AudioModel> getAudioModels() {
        return mAudiomodels;
    }

    private void setAudiosValue() {
        ArrayList<AudioModel> audios = (ArrayList<AudioModel>) mAudioAdapter.getAudios();
        StringBuilder sbPaths = new StringBuilder();
        for (AudioModel audio : audios) {
            if (sbPaths.length() > 0) {
                sbPaths.append(",");
            }
            sbPaths.append(audio.getFilePath());
        }

        mInspectItem.setValue(sbPaths.toString());
    }
}
