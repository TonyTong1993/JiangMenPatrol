package com.ecity.medialibrary.model;

import java.util.List;

import com.ecity.medialibrary.model.AudioModel;

public interface IAudioRecordCallback {

	public void onRecordFinish(List<AudioModel> audios);
	public void onAudioDeleted(List<AudioModel> audios);
}
