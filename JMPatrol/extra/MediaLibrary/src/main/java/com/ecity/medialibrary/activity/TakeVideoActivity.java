package com.ecity.medialibrary.activity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.utils.MediaHelper;
import com.z3app.android.util.StringUtil;

public class TakeVideoActivity extends Activity implements
		SurfaceHolder.Callback {
	private static final String TAG = TakeVideoActivity.class.getSimpleName()
			.toString();
	public static final String INTENT_KEY_FILE = "INTENT_KEY_FILE";
	public static final String CURRENT_VIDEO_PATH = "CURRENT_VIDEO_PATH";

	private MediaPlayer mediaPlayer;
	private RelativeLayout videoRelativeLayout;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Button btnStart;
	private File videoFile;
	private MediaRecorder recorder;
	private Camera camera;
	private int countBegin;
	private int countEnd;
	private Button btnSave;
	private Button btnDelete;
	private Button btnPlay;
	private Button btnPause;
	private Timer timer;
	private TextView timeTxt;
	private static String currentVideoPath;

	public static String getCurrentVideoPath() {
		return currentVideoPath;
	}

	private int position;
	private boolean isRecording;
	private boolean isPlaying;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);
		initDataSource();
		initUI();
		initListener();
	}

	private void initUI() {
		timer = new Timer();
		videoRelativeLayout = (RelativeLayout) findViewById(R.id.video_relativeLayout);
		timeTxt = (TextView) findViewById(R.id.remain_time);
		timeTxt.setText("00:10");
		mSurfaceView = (SurfaceView) findViewById(R.id.sv_video);
		btnSave = (Button) findViewById(R.id.video_bt_enter);

		btnDelete = (Button) findViewById(R.id.video_bt_del);

		btnPlay = (Button) findViewById(R.id.btn_play);
		btnPause = (Button) findViewById(R.id.btn_pause);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		recorder = new MediaRecorder();
		btnStart = (Button) findViewById(R.id.btn_start);
	}

	private void initDataSource() {
		countBegin = 0;
		countEnd = 10;
		isRecording = false;
		isPlaying = false;
	}

	private void initListener() {
		btnSave.setOnClickListener(new MySaveOnClickListener());
		btnDelete.setOnClickListener(new MyDeleteOnClickListener());
		btnStart.setOnClickListener(new MyOnClickListener());
		btnPlay.setOnClickListener(new MyPlayOnClickListener());
		btnPause.setOnClickListener(new MyPauseOnClickListener());
		mSurfaceView.setOnClickListener(new MySurfaceViewOnClickListener());
		mSurfaceHolder.addCallback(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != timer) {
			timer.cancel();
		}
		releaseMediaRecorder();
		releaseCamera();
		releaseMediaPlayer();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			deleteVideoFile();
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean prepareVideoRecorder(File videoFile) {
		if (null != recorder) {
			recorder.setCamera(camera);
			recorder.setOnErrorListener(new myRecorderOnErrorListener());
			recorder.setOnInfoListener(new myRecorderInfoListener());
			recorder.setOrientationHint(90);
			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());

			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

			CamcorderProfile profile = null;
			if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
				profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
			} else if (CamcorderProfile
					.hasProfile(CamcorderProfile.QUALITY_LOW)) {
				profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
			}

			if (profile != null) {
				profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
				recorder.setProfile(profile);
			}

			recorder.setMaxDuration(10000);// 最大期限
			recorder.setOutputFile(videoFile.getAbsolutePath());// 保存路径

			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				Log.i(TAG, e.toString());
				return false;

			} catch (IOException e) {
				Log.i(TAG, e.toString());
				return false;
			}
		}
		return true;
	}

	private void releaseMediaRecorder() {
		if (recorder != null) {
			recorder.reset(); // clear recorder configuration
			recorder.release(); // release the recorder object
			recorder = null;
		}
	}

	private void releaseCamera() {
		if (camera != null) {
			try {
				camera.setPreviewDisplay(null);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	private void releaseMediaPlayer() {
		if (null != mediaPlayer) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	private void startVideo() {
		try {
			videoFile = MediaHelper.createVideoFile();
			currentVideoPath = videoFile.getAbsolutePath();
			if (prepareVideoRecorder(videoFile)) {
				if (null != recorder) {
					recorder.start();
					btnStart.setBackgroundResource(R.drawable.btn_end_video);
					timeTxt.setVisibility(View.VISIBLE);
					timer.schedule(new MyCountDownTimerTask(), 0, 1000);
					isRecording = true;
				}
			} else {
				releaseMediaRecorder();
			}
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			TakeVideoActivity.this.finish();
		}
	}

	private void stopVideo() {
		if (null != recorder) {
			recorder.setOnErrorListener(null);
			recorder.setOnInfoListener(null);
			recorder.setPreviewDisplay(null);
			try {
				recorder.stop();
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				deleteVideoFile();
				finish();
			} finally {
				releaseMediaRecorder();
				releaseCamera();
				isRecording = false;
				if (null != timer) {
					timer.cancel();
				}
				btnStart.setVisibility(View.GONE);
				videoRelativeLayout.setVisibility(View.VISIBLE);
				btnPlay.setVisibility(View.VISIBLE);
				timeTxt.setVisibility(View.GONE);
			}
		}
	}

	private void deleteVideoFile() {
		if (null != currentVideoPath) {
			File file = new File(currentVideoPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	private class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (isRecording) {
				stopVideo();
				preparePlayVideo();
			} else {
				startVideo();
			}
		}
	}

	private class MySaveOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (!StringUtil.isBlank(currentVideoPath)) {
				Intent intent = new Intent();
				intent.putExtra(CURRENT_VIDEO_PATH, currentVideoPath);
				setResult(RESULT_OK, intent);
				TakeVideoActivity.this.finish();
			}
		}
	}

	private class MyDeleteOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			deleteVideoFile();
			TakeVideoActivity.this.finish();
		}
	}

	private class MyPlayOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			mediaPlayer.start();
			isPlaying = true;
			btnPlay.setVisibility(View.GONE);
			videoRelativeLayout.setVisibility(View.GONE);
		}
	}

	private class MyPauseOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (isPlaying) {
				timer.cancel();
				position = mediaPlayer.getCurrentPosition();
				mediaPlayer.pause();
				isPlaying = false;
				btnPause.setVisibility(View.GONE);
				btnPlay.setVisibility(View.VISIBLE);
				videoRelativeLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	private class MySurfaceViewOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (isPlaying && btnPause.getVisibility() != View.VISIBLE) {
				btnPause.setVisibility(View.VISIBLE);
			} else if (isPlaying && btnPause.getVisibility() == View.VISIBLE) {
				btnPause.setVisibility(View.GONE);
			} else {
				// no other logic
			}
		}
	}

	private class MyMediaPlayerOnCompletionListener implements
			OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			isPlaying = false;
			if (null != timer) {
				timer.cancel();
			}
			btnPause.setVisibility(View.GONE);
			btnPlay.setVisibility(View.VISIBLE);
			videoRelativeLayout.setVisibility(View.VISIBLE);
		}
	}

	private class MyCountDownTimerTask extends TimerTask {

		@Override
		public void run() {
			if (null != timeTxt) {
				handleCountDownEvent();
			}
		}
	}

	private void handleCountDownEvent() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int remainTime = countEnd - countBegin;
				if (remainTime >= 0 && remainTime < 10) {
					timeTxt.setText("00:0" + String.valueOf(remainTime));
				} else if (remainTime >= 10) {
					timeTxt.setText("00:" + String.valueOf(remainTime));
				}
			}
		});
		if ((countBegin++) == countEnd) {
			timer.cancel();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					stopVideo();
					preparePlayVideo();
				}
			});
		}
	}

	private void preparePlayVideo() {
		File file = new File(currentVideoPath);
		if (!file.exists()) {
			return;
		}
		mediaPlayer = new MediaPlayer();
		mediaPlayer
				.setOnCompletionListener(new MyMediaPlayerOnCompletionListener());
		mediaPlayer.setAudioSessionId(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(currentVideoPath);
			mediaPlayer.setDisplay(mSurfaceHolder);
			mediaPlayer.prepare();
		} catch (Exception e) {
			Log.i(TakeVideoActivity.this.toString(), e.toString());
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		initpreview();
		if (position > 0) {
			preparePlayVideo();
			mediaPlayer.start();
			isPlaying = true;
			btnPlay.setVisibility(View.GONE);
			videoRelativeLayout.setVisibility(View.GONE);
		}
	}

	private void initpreview() {
		camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
		try {
			camera.setPreviewDisplay(mSurfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		camera.setDisplayOrientation(90);// 摄像图旋转90度
		camera.unlock();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// There is no need to override
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (null != mediaPlayer && mediaPlayer.isPlaying()) {
			position = mediaPlayer.getCurrentPosition();
			releaseMediaPlayer();
			releaseMediaRecorder();
			releaseCamera();
			if (null != timer) {
				timer.cancel();
				timer = null;
			}
		}
	}

	private class myRecorderOnErrorListener implements OnErrorListener {

		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			if (null != mr) {
				mr.reset();
			}
		}
	}

	private class myRecorderInfoListener implements OnInfoListener {

		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {

		}
	}
}
