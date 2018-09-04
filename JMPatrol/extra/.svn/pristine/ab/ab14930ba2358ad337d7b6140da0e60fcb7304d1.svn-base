package com.ecity.medialibrary.utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ecity.medialibrary.model.AudioModel;
import com.pocketdigi.utils.FLameUtils;

/**
 * AudioRecorder的工具类，从AudioRocoder录制raw文件，然后转成mp3<br/>
 * 依赖libs目录下的armeabi目录和flame.jar<br/>
 * 
 * 权限要求<br/>
 * 
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 * 
 * @author gaokai
 * 
 */
public class AudioRecorder2Mp3 {

	/**
	 * 记录上一个播放的音频
	 */
	private AudioModel lastAudio;
	/**
	 * 记录上一个播放的音频位置
	 */
	private int lastPos = -1;

	private Timer timer = new Timer(true);
	private Object mLock = new Object();

	public static final int UPDATE_TIME = 0;
	public static final int UPDATE_VOLUM = 1;
	/**
	 * 自定义最大分贝值
	 */
	private static final double MAX_DB = 80;
	private Context context;
	private int bufferSize = 0;
	private MediaPlayer mPlayer = null;

	/**
	 * 文件代号
	 */
	public static final int RAW = 0X00000001;
	public static final int MP3 = 0X00000002;
	public static final String MP3_FILE_SUFFIX = ".mp3";
	public static final String RAW_FILE_SUFFIX = ".raw";

	/**
	 * 文件路径
	 */
	private String rawPath;
	private String mp3Path;

	/**
	 * 采样频率 音频采样率是指录音设备在一秒钟内对声音信号的采样次数 采样频率越高声音的还原就越真实越自然
     * 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级 22.05KHz只能达到FM广播的声音品质
     * 44.1KHz则是理论上的CD音质界限 8,000 Hz - 电话所用采样率, 对于人的说话已经足够
	 */
	private static final int SAMPLE_RATE = 8000;

	/**
	 * 录音需要的一些变量
	 */
	private short[] mBuffer;
	private AudioRecord mRecorder;
	private Handler recordingHandler;
	private Handler playHandler;
	private Runnable runnable;

	public AudioRecorder2Mp3(Context context, Handler recordingHandler,
			Handler playHandler, Runnable runnable) {
		this.context = context;
		this.recordingHandler = recordingHandler;
		this.playHandler = playHandler;
		this.mPlayer = new MediaPlayer();
		this.runnable = runnable;
	}

	/**
	 * 录音状态
	 */
	private boolean isRecording = false;
	/**
	 * 是否转换ok
	 */
	private boolean convertOk = false;
	/**
	 * 开始录音
	 * @param audio
	 */
	public void startRecording(AudioModel audio) {
		if (null == recordingHandler || null == playHandler) {
			return;
		}
		// 如果正在录音，则返回
		if (audio.isRecording()) {
			return;
		}
		
		// 如果正在播放，则停止播放，开始录音
		if (null != mPlayer && mPlayer.isPlaying()) {
			mPlayer.release();
			mPlayer = null;
		}
		// 初始化
		if (mRecorder == null) {
			initRecorder();
		}
		recordingHandler.post(runnable);

		getFilePath(audio);
		try {
			mRecorder.startRecording();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			initRecorder();
			mRecorder.startRecording();
		}

		startBufferedWrite(audio);
		audio.setRecording(true);
		return;
	}

	/**
	 * 停止录音，并且转换文件,<br/>
	 * <b>这很可能是个耗时操作，建议在后台中做
	 */
	public void stopRecording(AudioModel audio) {
		if (!audio.isRecording()) {
			return;
		}
		// 停止
		mRecorder.stop();
		audio.setRecording(false);
		close();
		recordingHandler.removeCallbacks(runnable);
	}

	public void stopPlay() {
		if (null != mPlayer) {
			mPlayer.stop();
			mPlayer = null;
		}
	}

	/**
	 * 获取文件的路径
	 * 
	 * @param fileAlias
	 *            RAW or MP3
	 * @return
	 */
	public String getFilePath(int fileAlias) {
		if (fileAlias == RAW) {
			return rawPath;
		} else if (fileAlias == MP3) {
			return mp3Path;
		} else
			return null;
	}

	/**
	 * 清理文件
	 * 
	 * @param cleanFlag
	 * RAW,MP3 or RAW|MP3
	 */
	public void cleanFile(int cleanFlag) {
		File f = null;
		try {
			switch (cleanFlag) {
			case MP3:
				f = new File(mp3Path);
				if (f.exists())
					f.delete();
				break;
			case RAW:
				f = new File(rawPath);
				if (f.exists())
					f.delete();
				break;
			case RAW | MP3:
				f = new File(rawPath);
				if (f.exists())
					f.delete();
				f = new File(mp3Path);
				if (f.exists())
					f.delete();
				break;
			}
			f = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭,可以先调用cleanFile来清理文件
	 */
	public void close() {
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
		context = null;
	}

	// -------内部的一些工具方�?-------
	/**
	 * 初始化
	 */
	private void initRecorder() {
		bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		mBuffer = new short[bufferSize];
		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,bufferSize);
	}

	/**
	 * 设置路径，第一个为raw文件，第二个为mp3文件 
	 * 
	 */
	private void getFilePath(AudioModel audio) {
		try {
			if (audio.getTempPath().isEmpty()) {
				File f = MediaHelper.createAudioFile(context, RAW_FILE_SUFFIX);
				audio.setTempPath(f.getAbsolutePath());
				// rawPath = f.getAbsolutePath();
			}
			if (audio.getFilePath().isEmpty()) {
				File f = MediaHelper.createAudioFile(context, MP3_FILE_SUFFIX);
				// mp3Path = f.getAbsolutePath();
				audio.setFilePath(f.getAbsolutePath());
			}
		} catch (Exception e) {
			Log.e("AudioRecorder2Mp3", e.getMessage(), e);
		}
	}

	/**
	 * 写入到raw文件
	 * 
	 * @param file
	 */
	private void startBufferedWrite(final AudioModel audio) {
		final File file = new File(audio.getTempPath());
		new Thread(new Runnable() {

			@Override
			public void run() {
				DataOutputStream output = null;
				try {

					output = new DataOutputStream(new BufferedOutputStream(
							new FileOutputStream(file)));
					while (audio.isRecording()) {
						final int readSize = mRecorder.read(mBuffer, 0,
								mBuffer.length);
						for (int i = 0; i < readSize; i++) {
							output.writeShort(mBuffer[i]);
						}
						// ----------------------------以下为计算分贝------------------------
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								while (audio.isRecording()) {
									long v = 0;
									// 将 buffer 内容取出，进行平方和运算
									for (int i = 0; i < mBuffer.length; i++) {
										v += mBuffer[i] * mBuffer[i];
									}
									// 平方和除以数据总长度，得到音量大小
									double mean = v / (double) readSize;
									double volume = (10 * Math.log10(mean));
									double rate = volume / MAX_DB;
									Log.i("test", String.valueOf(rate));
									if (rate < 0.5 || rate == 0.5) {
										rate = 0;
									} else if (0.4 < rate && rate < 0.5
											|| rate == 0.5) {
										rate -= 0.2;
									} else if (0.5 < rate && rate < 0.6
											|| rate == 0.6) {
										rate += 0.01;
									} else if (0.6 < rate && rate < 0.7
											|| rate == 0.7) {
										rate += 0.01;
									} else
										rate += 0.01;

									Message msg = new Message();
									msg.obj = rate;
									msg.what = UPDATE_VOLUM;
									recordingHandler.sendMessage(msg);
									synchronized (mLock) {
										try {
											mLock.wait(300);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}, 0, 500); // 延时0ms后执行，1000ms执行一次
						// ----------------------------------------------------------------------
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.flush();
						} catch (IOException e) {
							e.printStackTrace();

						} finally {
							try {
								output.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();
	}

	public boolean convert2mp3(String tempPath, String filePath) {
		// 开始转换
		try {
			FLameUtils lameUtils = new FLameUtils(1, SAMPLE_RATE, 96);
			convertOk = lameUtils.raw2mp3(tempPath, filePath);
		} catch (Exception e) {
			Log.e("AudioRecorder2Mp3", e.getMessage(), e);
			convertOk = false;
			Toast.makeText(context, "JNI调用出错", Toast.LENGTH_SHORT).show();
		}
		return convertOk;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	public String getRawPath() {
		return rawPath;
	}

	public void setRawPath(String rawPath) {
		this.rawPath = rawPath;
	}

	public String getMp3Path() {
		return mp3Path;
	}

	public void setMp3Path(String mp3Path) {
		this.mp3Path = mp3Path;
	}

	public void pausePlay(AudioModel audio) {
		if (null != mPlayer) {
			mPlayer.pause();
		}
	}

	public void Play(AudioModel audio, int position) {
		if (lastPlayNotOver()) {
			// 点击的是上一次
			if (-1 != lastPos && lastPos == position) {
				// 继续播放
				playRecording(audio, position);
				return;
			}
			// 点击的不是上一次
            // 1、重置上一次点击的条目
            // 2、停止播放，释放第一次播放的资源
			else {
				lastAudio.setPlayState(AudioModel.OVER);
				mPlayer.pause();
				mPlayer.release();
				mPlayer = null;
			}
		}
		playRecording(audio, position);
	}

	private void playRecording(AudioModel audio, int position) {

		// // 开始新的音频播放
		Message msg = playHandler.obtainMessage();
		switch (audio.getPlayState()) {
		case AudioModel.OVER:
			// 开始播放
			audio.setPlayState(AudioModel.PLAY);
			msg.what = AudioModel.PLAY;
			msg.obj = position;
			startPlay(audio, position);
			break;
		case AudioModel.PLAY:
			// 暂停播放
			audio.setPlayState(AudioModel.PAUSE);
			msg.what = AudioModel.PAUSE;
			msg.obj = position;
			pausePlay(audio);
			break;
		case AudioModel.PAUSE:
			// 继续播放
			audio.setPlayState(AudioModel.PLAY);
			msg.what = AudioModel.PLAY;
			msg.obj = position;
			continuePlay(audio);
			break;
		}
		playHandler.sendMessage(msg);
		lastAudio = audio;
		lastPos = position;
	}

	private void continuePlay(AudioModel audio) {
		if (null != mPlayer) {
			mPlayer.start();
		}
	}

	/**
	 * 此方法描述的是：上一个播放尚未完成
	 */
	private boolean lastPlayNotOver() {
		if (null != lastAudio && -1 != lastPos) {
			int state = lastAudio.getPlayState();
			if (state != AudioModel.OVER) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 此方法描述的是：开始播放
	 */
	private void startPlay(final AudioModel audio, final int position) {

		if (null == mPlayer) {
			mPlayer = new MediaPlayer();
		}
		try {
			mPlayer.setDataSource(audio.getFilePath());
			mPlayer.prepare();
			mPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				audio.setPlayState(AudioModel.OVER);
				Message msg = playHandler.obtainMessage();
				msg.obj = position;
				msg.what = AudioModel.OVER;
				playHandler.sendMessage(msg);
				mPlayer.release();
				mPlayer = null;
			}
		});
	}
}
