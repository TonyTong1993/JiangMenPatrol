package com.ecity.medialibrary.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.model.IAudioRecordCallback;
import com.ecity.medialibrary.utils.AudioRecorder2Mp3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 此类描述的是：
 * 
 * @author: gaokai
 * @version: 2015年11月9日 下午5:31:18
 */

public class AudiosRecordAdapter extends BaseAdapter {
	private static final int TYPE_FIRST = 0;
	private static final int TYPE_SECOND = 1;
	private static final int FLING_MIN_DISTANCE = 10;// 移动最小距离

	private List<AudioModel> mAudios = new ArrayList<AudioModel>();
	private LayoutInflater mInflater = null;
	private AudioRecorder2Mp3 mAudioRecorderUtil;
	private Handler mRecordingHandler;
	private PlayHandler mPlayHandler;
	private float mPosX, mCurPosX;
	private IAudioRecordCallback callback;

	private int timeSecond = 0;
	private int timeSecond1 = 0;
	private int timeMinute = 0;
	private int timeMinute1 = 0;
	private String audioLength;
	
	/**
	 * 创建一个新的实例 AudiosListAdapter.
	 * 
	 * @param audios
	 */
	public AudiosRecordAdapter(Context context, List<AudioModel> audios,Handler recordingHandler) {
		mInflater = LayoutInflater.from(context);
		this.mAudios = audios;
		this.mRecordingHandler = recordingHandler;
		this.mPlayHandler = new PlayHandler(this);
		this.mAudioRecorderUtil = new AudioRecorder2Mp3(context,mRecordingHandler, mPlayHandler, startRunnable);
	}

	public AudioRecorder2Mp3 getAudioRecorderUtil() {
		return mAudioRecorderUtil;
	}

	public void setCallback(IAudioRecordCallback callback) {
		this.callback = callback;
	}

	public void removeRecordCallback() {
		mRecordingHandler.removeCallbacks(startRunnable);
		this.callback = null;
	}

	public void finishRecordAudio(AudioModel audio) {
		if (null != audio) {
			audio.setLength(audioLength);
			mAudios.add(audio);
			notifyDataSetChanged();
		}
	}

	public void resetRecordTime() {
		audioLength = "00:00";
		timeSecond = 0;
		timeSecond1 = 0;
		timeMinute = 0;
		timeMinute1 = 0;
	}
	
	private void checkDelete(int position,boolean isDelete){
		int size = mAudios.size();
		if(position >= size){
			return;
		}
		
		for(int i = 0; i< size; i++){
			mAudios.get(i).setDelete(false);
		}
		
		mAudios.get(position).setDelete(isDelete);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAudios.size() + 1;
	}

	@Override
	public AudioModel getItem(int position) {
		return mAudios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == (mAudios.size())) {
			return TYPE_SECOND;
		} else {
			return TYPE_FIRST;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AudioModel audio = null;
		if (null != mAudios && position < mAudios.size()) {
			audio = mAudios.get(position);
		}
		ViewHolderFirst viewHolder1 = null;
		ViewHolderSecond viewHolder2 = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case TYPE_FIRST:
				convertView = mInflater.inflate(R.layout.item_audiolist, null);
				viewHolder1 = new ViewHolderFirst(convertView);
				convertView.setTag(viewHolder1);
				break;
			case TYPE_SECOND:
				convertView = mInflater.inflate(R.layout.custom_form_file_list_item, null);
				viewHolder2 = new ViewHolderSecond(convertView);
				convertView.setTag(viewHolder2);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case TYPE_FIRST:
				viewHolder1 = (ViewHolderFirst) convertView.getTag();
				break;
			case TYPE_SECOND:
				viewHolder2 = (ViewHolderSecond) convertView.getTag();
				break;
			default:
				break;
			}
		}

		switch (type) {
		case TYPE_FIRST:
			viewHolder1.tv_audioName.setText(audio.getName());
			viewHolder1.tv_audioLength.setText(audio.getLength());
			if (null != audio) {
				int state = audio.getPlayState();
				switch (state) {
				case AudioModel.OVER:
				case AudioModel.PAUSE:
					viewHolder1.iv_play.setVisibility(View.VISIBLE);
					viewHolder1.iv_pause.setVisibility(View.GONE);
					break;
				case AudioModel.PLAY:
					viewHolder1.iv_play.setVisibility(View.GONE);
					viewHolder1.iv_pause.setVisibility(View.VISIBLE);
					break;
				}
			}
			bindEvent(position, viewHolder1);
			break;
		default:
			break;
		}
		return convertView;
	}

	private void bindEvent(final int position, final ViewHolderFirst viewHolder1) {
		viewHolder1.iv_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AudioModel audio = mAudios.get(position);
				if (null != mAudioRecorderUtil) {
					mAudioRecorderUtil.Play(audio, position);
				}
			}
		});

		viewHolder1.iv_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AudioModel audio = mAudios.get(position);
				if (null != mAudioRecorderUtil) {
					mAudioRecorderUtil.Play(audio, position);
				}
			}
		});

		viewHolder1.tv_remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				remove(position);
				viewHolder1.fL_play.setVisibility(View.VISIBLE);
				viewHolder1.tv_remove.setVisibility(View.GONE);
			}
		});
		
		if(mAudios.get(position).isDelete()){
			viewHolder1.fL_play.setVisibility(View.GONE);
			viewHolder1.tv_remove.setVisibility(View.VISIBLE);
		}else{
			viewHolder1.fL_play.setVisibility(View.VISIBLE);
			viewHolder1.tv_remove.setVisibility(View.GONE);
		}
		
		viewHolder1.rl_container.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mPosX = event.getX();
					//mPosY = event.getY();
				case MotionEvent.ACTION_MOVE:
					mCurPosX = event.getX();
					//mCurPosY = event.getY();
				case MotionEvent.ACTION_UP:
					if ((mPosX - mCurPosX > 0) && (Math.abs(mPosX - mCurPosX) > FLING_MIN_DISTANCE)) {
						viewHolder1.fL_play.setVisibility(View.GONE);
						viewHolder1.tv_remove.setVisibility(View.VISIBLE);
						checkDelete(position,true);
					} else if ((mCurPosX - mPosX > 0) && (Math.abs(mCurPosX - mPosX) > FLING_MIN_DISTANCE)) {
						viewHolder1.fL_play.setVisibility(View.VISIBLE);
						viewHolder1.tv_remove.setVisibility(View.GONE);
						checkDelete(position,false);
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	static class ViewHolderFirst {
		private TextView tv_audioName;
		private TextView tv_Length;
		private TextView tv_audioLength;
		private ImageView iv_play;
		private ImageView iv_pause;
		private RelativeLayout rl_container;
		private FrameLayout fL_play;
		private TextView tv_remove;

		public ViewHolderFirst(View view) {
			super();
			this.iv_play = (ImageView) view.findViewById(R.id.iv_play);
			this.iv_pause = (ImageView) view.findViewById(R.id.iv_pause);
			this.tv_audioName = (TextView) view.findViewById(R.id.tv_audioname);
			this.tv_Length = (TextView) view.findViewById(R.id.tv_length);
			this.tv_audioLength = (TextView) view.findViewById(R.id.tv_audiolength);
			this.rl_container = (RelativeLayout) view.findViewById(R.id.ll_container);
			this.fL_play = (FrameLayout) view.findViewById(R.id.framlayout_play);
			this.tv_remove = (TextView) view.findViewById(R.id.remove);
		}
	}

	static class ViewHolderSecond {
		// private ImageView iv_add;
		public ViewHolderSecond(View view) {
			// this.iv_add = (ImageView) view.findViewById(R.id.iv_add);
		}
	}

	public void remove(int position) {
		mAudios.remove(position);
		int size = mAudios.size();
		for(int i = 0 ; i < size;i++){
			mAudios.get(i).setDelete(false);
		}
		if(null != this.callback){
			this.callback.onAudioDeleted(mAudios);
		}
		notifyDataSetChanged();		
	}

	public void updatePlayState(int position, int state) {
		mAudios.get(position).setPlayState(state);
		notifyDataSetChanged();
	}

	public List<AudioModel> getAudios() {
		return mAudios;
	}

	public void setAudios(List<AudioModel> audios) {
		this.mAudios = audios;
	}

	private static class PlayHandler extends Handler {
		private WeakReference<AudiosRecordAdapter> audiosListWeak;

		public PlayHandler(AudiosRecordAdapter adapter) {
			this.audiosListWeak = new WeakReference<AudiosRecordAdapter>(adapter);
		}

		@Override
		public void handleMessage(Message msg) {
			AudiosRecordAdapter adapter = audiosListWeak.get();
			int position = (Integer) msg.obj;
			int state = msg.what;
			adapter.updatePlayState(position, state);
		}
	}

	private Runnable startRunnable = new Runnable() {
		// 将要执行的操作写在线程对象的run方法当中
		public void run() {

			if (timeMinute1 < 6) {
				timeSecond++;
				if (timeSecond == 10) {
					timeSecond = 0;
					timeSecond1++;
				}
				if (timeSecond1 == 6 && timeSecond == 0) {
					timeMinute++;
					timeSecond = 0;
					timeSecond1 = 0;
				}
				if (timeMinute == 10 && timeSecond1 == 0 && timeSecond == 0) {
					timeSecond = 0;
					timeSecond1 = 0;
					timeMinute = 0;
					timeMinute1++;
				}
			}
			audioLength = "" + timeMinute1 + timeMinute + ":" + timeSecond1 + timeSecond;
			Message msg = mRecordingHandler.obtainMessage();
			msg.obj = audioLength;
			msg.what = AudioRecorder2Mp3.UPDATE_TIME;
			mRecordingHandler.sendMessage(msg);
			// 调用Handler的postDelayed()方法
			// 这个方法的作用是：将要执行的线程对象放入到队列当中，待时间结束后，运行制定的线程对象
			// 第一个参数是Runnable类型：将要执行的线程对象
			// 第二个参数是long类型：延迟的时间，以毫秒为单位
			mRecordingHandler.postDelayed(startRunnable, 1000);
		}
	};
}
