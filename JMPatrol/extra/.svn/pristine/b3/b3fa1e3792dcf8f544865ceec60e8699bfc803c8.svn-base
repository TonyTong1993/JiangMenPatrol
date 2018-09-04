package com.ecity.medialibrary.activity;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.utils.MediaCacheManager;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class PlayVideoActivity extends Activity {
    private static final String TAG = PlayVideoActivity.class.getSimpleName();
    public static final String INTENT_KEY_VIDEOPATH = "INTENT_KEY_VIDEOPATH";// the path can be a local path or a web URL.
    public static final String INTENT_KEY_EDIT = "INTENT_KEY_EDIT";

    private MediaPlayer mediaPlayer;
    private SurfaceView svVideo;
    private SurfaceHolder shVideo;
    private Button btnPlay;
    private Button btnPause;
    private ProgressBar pbBuffer;
    private Button btnDelete;
    private Button btnConfirm;
    private String videoPath;
    private int position;
    private boolean isPreparing;
    private boolean isPlaying;
    private boolean isVideoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_paly);
        initDataSource();
        initUI();
        initListener();
    }

    private void initUI() {
        svVideo = (SurfaceView) findViewById(R.id.sv_video);
        shVideo = svVideo.getHolder();
        shVideo.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        pbBuffer = (ProgressBar) findViewById(R.id.pb_buffer);
        btnDelete = (Button) findViewById(R.id.video_bt_del);
        btnConfirm = (Button) findViewById(R.id.video_bt_enter);
        btnPlay.setVisibility(View.GONE);
    }

    private void initDataSource() {
        videoPath = getIntent().getStringExtra(INTENT_KEY_VIDEOPATH);
        isVideoEdit = getIntent().getBooleanExtra(INTENT_KEY_EDIT, false);
    }

    private void initListener() {
        btnPlay.setOnClickListener(new MyPlayOnClickListener());
        btnPause.setOnClickListener(new MyPauseOnClickListener());
        shVideo.addCallback(new MySHolderCallBack());
        svVideo.setOnClickListener(new MySurfaceViewOnClickListener());

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMediaPlayer();
                MediaCacheManager.vdodrr.remove(videoPath);
                setResult(RESULT_OK);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMediaPlayer();
                finish();
            }
        });
    }

    private void preparePlayVideo() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
        mediaPlayer.setOnErrorListener(new MyOnErrorListener());
        mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setDisplay(shVideo);
            mediaPlayer.prepareAsync();
            isPreparing = true;
            pbBuffer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    private void releaseMediaPlayer() {
        if (null != mediaPlayer) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_VOLUME_DOWN && keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_MUTE) {
            pbBuffer.setVisibility(View.GONE);
            releaseMediaPlayer();
        }

        return super.onKeyDown(keyCode, event);
    }

    private class MyPlayOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            playVideo();
        }
    }

    private void startVideo() {
        btnPlay.setVisibility(View.GONE);
        mediaPlayer.start();
        isPlaying = true;
    }

    private void playVideo() {
        if (position == 0) {
//            btnPlay.setVisibility(View.GONE);
//            preparePlayVideo();
            startVideo();
        } else if (position != 0) {
            btnPlay.setVisibility(View.GONE);
            mediaPlayer.start();
            isPlaying = true;
        }
        btnDelete.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
    }

    private class MyPauseOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            pauseVideo();
        }
    }

    private void pauseVideo() {
        btnPause.setVisibility(View.GONE);
        position = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        isPlaying = false;
        btnPlay.setVisibility(View.VISIBLE);
    }

    private class MySurfaceViewOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (isPreparing) {
                return;
            }

            if (isPlaying && (btnPause.getVisibility() != View.VISIBLE)) {
                btnPause.setVisibility(View.VISIBLE);
                pauseVideo();
                if(isVideoEdit) {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnConfirm.setVisibility(View.VISIBLE);
                } else {
                    btnDelete.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.GONE);
                }
            } else if (!isPlaying && (btnPlay.getVisibility() == View.VISIBLE)) {
                btnPlay.setVisibility(View.GONE);
                playVideo();
            } else if (!isPlaying && (btnPlay.getVisibility() != View.VISIBLE)) {
                btnPlay.setVisibility(View.VISIBLE);
            } else if (isPlaying && (btnPause.getVisibility() == View.VISIBLE)) {
                btnPause.setVisibility(View.GONE);
            } else {
                // no other logic
            }

        }
    }

    private class MySHolderCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (position == 0) {
                btnPlay.setVisibility(View.GONE);
                preparePlayVideo();// when open the activity, start to play video right now.
            } else if (position > 0) {
                mediaPlayer.seekTo(position);
                btnPlay.performClick();
            } else {
                //TODO:no logic to do.
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // nothing to do
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (null != mediaPlayer) {
                position = mediaPlayer.getCurrentPosition();
            }
            releaseMediaPlayer();
        }
    }

    private class MyOnCompletionListener implements OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
            finish();
        }
    }

    private class MyOnPreparedListener implements OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            // TODO Auto-generated method stub
            isPreparing = false;
            pbBuffer.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            if(isVideoEdit) {
                btnDelete.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyOnErrorListener implements OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            releaseMediaPlayer();
            finish();
            return true;
        }
    }
}
