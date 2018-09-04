package com.ecity.cswatersupply.workorder.widght;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

/**
 * 带声音提示的textView 当textView显示的时候，声音提示
 * 
 * @author gaokai
 *
 */
public class AudioTextView extends TextView {
    private MediaPlayer mPlayer;
    private boolean isSound;
    private static final boolean DEFAULT_ISSOUND = true; // 默认带声音提示

    public AudioTextView(Context context) {
        this(context, null);
    }

    public AudioTextView(Context context, AttributeSet attrs) {
        this(context, attrs, DEFAULT_ISSOUND);
    }

    public AudioTextView(final Context context, AttributeSet attrs, boolean isSound) {
        super(context, attrs);
        this.isSound = isSound;

        mPlayer = MediaPlayer.create(context, R.raw.newdatatoast); // create过不用prepare()
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
            }
        });
    }

    public void setSounded(boolean isSound) {
        this.isSound = isSound;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (isSound && visibility == VISIBLE) {
            try {
                mPlayer.start();
            } catch (IllegalStateException e) {
                Log.i("AudioTextView", e.getMessage());
            }
        }
    }

}
