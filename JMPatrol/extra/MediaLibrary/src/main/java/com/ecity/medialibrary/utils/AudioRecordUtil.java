package com.ecity.medialibrary.utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.adapter.AudiosRecordAdapter;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.model.IAudioRecordCallback;
import com.ecity.medialibrary.widght.VolumCircleBar;
import com.z3app.android.util.FileUtil;

public class AudioRecordUtil {
	private Context context;
	private TextView tvTime;
	private VolumCircleBar volumCircleBar;
	private AudiosRecordAdapter adapter;
	private AudioModel currentAudio;
	private static boolean isRecording;
	private PopupWindow popWindow;
	private IAudioRecordCallback callback;
	private LinearLayout ll_alertView,ll_recordView;

	public AudioRecordUtil(Context context, IAudioRecordCallback callback) {
		this.context = context;
		this.callback = callback;
	}

	public AudiosRecordAdapter getAudiosRecordAdapter(List<AudioModel> audios) {
		if (null == adapter) {
			adapter = new AudiosRecordAdapter(context, audios,new RecordingHandler(this));
		} else {
			adapter.setAudios(audios);
		}

		return adapter;
	}

	public void showRecordingAudioView(View parentView) {
		currentAudio = new AudioModel();
		LayoutInflater inflater = LayoutInflater.from(context);
		popWindow = new PopupWindow(context);
		View view = inflater.inflate(R.layout.view_audio_popup_window, null);
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		tvTime = (TextView) view.findViewById(R.id.tv_time);
		ll_alertView = (LinearLayout) view.findViewById(R.id.ll_alertview);
		ll_recordView = (LinearLayout) view.findViewById(R.id.ll_recordview);
		
		volumCircleBar = (VolumCircleBar) view.findViewById(R.id.volumCircleBar);
		popWindow.setContentView(view);
		popWindow.setWidth(displayMetrics.widthPixels);
		popWindow.setHeight(displayMetrics.heightPixels);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(false);
		popWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_popup_window));
		popWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		textViewInit();
		popWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (null != adapter && null != currentAudio && currentAudio.isRecording()) {
					adapter.getAudioRecorderUtil().stopRecording(currentAudio);
					adapter.removeRecordCallback();
					volumCircleBar.toggleRecord();
					FileUtil.deleteFile(new File(currentAudio.getTempPath()));
				}
			}
		});

		volumCircleBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((VolumCircleBar) v).toggleRecord();
				isRecording = !isRecording;
				if (isRecording) {
					startRecording(currentAudio);
				} else {
					stopRecording(currentAudio);
				}
			}
		});
	}

	protected void stopRecording(AudioModel audio) {
		if (null != adapter.getAudioRecorderUtil() && audio.isRecording()) {
			adapter.getAudioRecorderUtil().stopRecording(audio);
			String tempPath = audio.getTempPath();
			String filePath = audio.getFilePath();
			ll_alertView.setVisibility(View.VISIBLE);
			ll_recordView.setVisibility(View.GONE);		
			Raw2Mp3Task mTask = new Raw2Mp3Task();
			mTask.execute(tempPath, filePath);
		}
	}

	protected void startRecording(AudioModel audio) {
		if (!audio.isRecording()) {
			textViewInit();
			ll_alertView.setVisibility(View.GONE);
			ll_recordView.setVisibility(View.VISIBLE);		
			adapter.resetRecordTime();
			adapter.getAudioRecorderUtil().startRecording(audio);
		} else {
			Toast.makeText(context, R.string.hold_on_please, Toast.LENGTH_SHORT).show();
		}
	}

	private void textViewInit() {
		tvTime.setText("00:00");
	}

	private class Raw2Mp3Task extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result.equals(Boolean.TRUE)) {
				// 转换完毕，删除raw文件
				FileUtil.deleteFile(new File(currentAudio.getTempPath()));
				String path = currentAudio.getFilePath();
				String name = path.substring(path.lastIndexOf("/") + 1);
				currentAudio.setName(name);
				adapter.finishRecordAudio(currentAudio);
				currentAudio = null;
				if (null != callback) {
					callback.onRecordFinish(adapter.getAudios());
				}
			} else {
				Toast.makeText(context,R.string.event_report_error_format_conversion,Toast.LENGTH_SHORT).show();
			}
			if(null != adapter){
				adapter.getAudioRecorderUtil().cleanFile(AudioRecorder2Mp3.RAW);
			}
			
			if( null != popWindow){
				popWindow.dismiss();
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if (adapter.getAudioRecorderUtil().convert2mp3(params[0], params[1])) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	}

	private static class RecordingHandler extends Handler {
		private WeakReference<AudioRecordUtil> audiosListWeak;

		public RecordingHandler(AudioRecordUtil audioRecordUtil) {
			this.audiosListWeak = new WeakReference<AudioRecordUtil>(audioRecordUtil);
		}

		@Override
		public void handleMessage(Message msg) {
			AudioRecordUtil audioRecordUtil = audiosListWeak.get();
			if (null == audioRecordUtil) {
				return;
			}
			switch (msg.what) {
			case AudioRecorder2Mp3.UPDATE_TIME:
				String time = (String) msg.obj;
				audioRecordUtil.tvTime.setText(time);
				break;
			case AudioRecorder2Mp3.UPDATE_VOLUM:
				audioRecordUtil.volumCircleBar.updateVolumRate((Double) msg.obj);
				break;
			default:
				break;
			}

		}
	}
}
